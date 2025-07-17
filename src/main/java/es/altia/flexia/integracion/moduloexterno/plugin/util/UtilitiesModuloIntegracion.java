package es.altia.flexia.integracion.moduloexterno.plugin.util;

import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.DomicilioInteresadoModuloIntegracionVO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;
import org.apache.log4j.Logger;


public class UtilitiesModuloIntegracion {
    private static Logger log = Logger.getLogger(UtilitiesModuloIntegracion.class);

    public static Calendar toCalendar(String fecha,String formato){
        Calendar cal = null;
        try{
            if(fecha!=null && formato!=null){
                SimpleDateFormat sf = new SimpleDateFormat(formato);
                java.util.Date date = sf.parse(fecha);

                cal = Calendar.getInstance();
                cal.clear();
                cal.setTime(date);
            }
        }catch(Exception e){
            log.error("Error en la conversión a Calendar de la fecha: " + fecha + " con formato: " + formato);
        }

        return cal;
    }


    /**
     * Convierta un objeto Calendar en un objeto java.sql.Timestamp
     * @param cal: Calendar
     * @return java.sql.Timestamp
     */
    public static java.sql.Timestamp calendarToTimestamp(Calendar cal){
        java.sql.Timestamp result = null;

        if (cal!=null)
            result = new java.sql.Timestamp(cal.getTime().getTime());

        return result;
    }

   /**
     * Convierte un objeto del tiop java.sql.Timestamp en un Calendar
     * @param timestamp
     * @return Calendar */     
    public static Calendar timestampToCalendar(java.sql.Timestamp timestamp){
        Calendar salida = null;
        if(timestamp!=null){
            Calendar c = Calendar.getInstance();
            c.clear();
            c.setTimeInMillis(timestamp.getTime());
            salida = c;
        }
        return salida;
    }


    /**
     * Convierte un objeto de la clase DomicilioSimpleValueObject en un objeto de la clase DomicilioInteresadoModuloIntegracionVO
     * @param dsVO: DomicilioSimpleValueObject
     * @return DomicilioInteresadoModuloIntegracionVO
     */
    public static DomicilioInteresadoModuloIntegracionVO domicilioSimpleValueObjectToDomicilioInteresadoModuloIntegracionVO(DomicilioSimpleValueObject dsVO){
        DomicilioInteresadoModuloIntegracionVO dimiVO = new DomicilioInteresadoModuloIntegracionVO();
        
        dimiVO.setBarriada(dsVO.getBarriada());
        dimiVO.setBloque(dsVO.getBloque());
        dimiVO.setCodECO(dsVO.getCodECO());
        dimiVO.setCodESI(dsVO.getCodESI());
        dimiVO.setCodTipoUso(dsVO.getCodTipoUso());
        dimiVO.setCodigoPostal(dsVO.getCodigoPostal());
        dimiVO.setCodigoVia(dsVO.getCodigoVia());
        dimiVO.setDescESI(dsVO.getDescVia());
        dimiVO.setDescTipoUso(dsVO.getDescTipoUso());
        dimiVO.setDescripcionVia(dsVO.getDescVia());
        dimiVO.setDomicilio(dsVO.getDomicilio());
        dimiVO.setEscalera(dsVO.getEscalera());
        dimiVO.setIdDomicilio(dsVO.getIdDomicilio());
        dimiVO.setIdMunicipio(dsVO.getIdMunicipio());
        dimiVO.setIdMunicipioVia(dsVO.getIdMunicipioVia());
        dimiVO.setIdPais(dsVO.getIdPais());
        dimiVO.setIdPaisVia(dsVO.getIdPaisVia());
        dimiVO.setIdProvincia(dsVO.getIdProvincia());
        dimiVO.setIdProvinciaVia(dsVO.getIdProvinciaVia());
        dimiVO.setLetraDesde(dsVO.getLetraDesde());
        dimiVO.setLetraHasta(dsVO.getLetraHasta());
        dimiVO.setMunicipio(dsVO.getMunicipio());
        dimiVO.setNormalizado(dsVO.getNormalizado());
        dimiVO.setNumDesde(dsVO.getNumDesde());
        dimiVO.setNumHasta(dsVO.getNumHasta());
        dimiVO.setPais(dsVO.getPais());
        dimiVO.setPlanta(dsVO.getPlanta());
        dimiVO.setPortal(dsVO.getPortal());
        dimiVO.setProvincia(dsVO.getProvincia());
        dimiVO.setPuerta(dsVO.getPuerta());        
        return dimiVO;
    }
    
    
    /**
     * Convierte un objeto de la clase DomicilioSimpleValueObject en un objeto de la clase DomicilioInteresadoModuloIntegracionVO
     * @param dsVO: DomicilioSimpleValueObject
     * @return DomicilioInteresadoModuloIntegracionVO
     */
    public static DomicilioInteresadoModuloIntegracionVO domicilioSimpleValueObjectToDomicilioInteresadoModuloIntegracionVO(DomicilioSimpleValueObject dsVO,String codDomicilioPrincipal){
        DomicilioInteresadoModuloIntegracionVO dimiVO = new DomicilioInteresadoModuloIntegracionVO();
        
        dimiVO.setBarriada(dsVO.getBarriada());
        dimiVO.setBloque(dsVO.getBloque());
        dimiVO.setCodECO(dsVO.getCodECO());
        dimiVO.setCodESI(dsVO.getCodESI());
        dimiVO.setCodTipoUso(dsVO.getCodTipoUso());
        dimiVO.setCodigoPostal(dsVO.getCodigoPostal());
        dimiVO.setCodigoVia(dsVO.getCodigoVia());
        dimiVO.setDescESI(dsVO.getDescVia());
        dimiVO.setDescTipoUso(dsVO.getDescTipoUso());
        dimiVO.setDescripcionVia(dsVO.getDescVia());
        dimiVO.setDomicilio(dsVO.getDomicilio());
        dimiVO.setEscalera(dsVO.getEscalera());
        dimiVO.setIdDomicilio(dsVO.getIdDomicilio());
        dimiVO.setIdMunicipio(dsVO.getIdMunicipio());
        dimiVO.setIdMunicipioVia(dsVO.getIdMunicipioVia());
        dimiVO.setIdPais(dsVO.getIdPais());
        dimiVO.setIdPaisVia(dsVO.getIdPaisVia());
        dimiVO.setIdProvincia(dsVO.getIdProvincia());
        dimiVO.setIdProvinciaVia(dsVO.getIdProvinciaVia());
        dimiVO.setLetraDesde(dsVO.getLetraDesde());
        dimiVO.setLetraHasta(dsVO.getLetraHasta());
        dimiVO.setMunicipio(dsVO.getMunicipio());
        dimiVO.setNormalizado(dsVO.getNormalizado());
        dimiVO.setNumDesde(dsVO.getNumDesde());
        dimiVO.setNumHasta(dsVO.getNumHasta());
        dimiVO.setPais(dsVO.getPais());
        dimiVO.setPlanta(dsVO.getPlanta());
        dimiVO.setPortal(dsVO.getPortal());
        dimiVO.setProvincia(dsVO.getProvincia());
        dimiVO.setPuerta(dsVO.getPuerta());
        
        if(codDomicilioPrincipal!=null && dsVO.getIdDomicilio()!=null && codDomicilioPrincipal.equals(dsVO.getIdDomicilio()))
            dimiVO.setPorDefecto(true);
        
        return dimiVO;
    }

    /**
     * Convierte un Vector de objetos DomicilioSimpleValueObject en un ArrayList<DomicilioInteresadoModuloIntegracionVO>
     * @param domicilios: Vector<DomicilioSimpleValueObject>
     * @return ArrayList<DomicilioInteresadoModuloIntegracionVO>
     */
    public static ArrayList<DomicilioInteresadoModuloIntegracionVO> listDomicilioSimpleValueObjectTolistDomicilioInteresadoModuloIntegracionVO(Vector<DomicilioSimpleValueObject> domicilios){
        ArrayList<DomicilioInteresadoModuloIntegracionVO> salida = new ArrayList<DomicilioInteresadoModuloIntegracionVO>();
        for(int i=0;domicilios!=null && i<domicilios.size();i++){
            salida.add(domicilioSimpleValueObjectToDomicilioInteresadoModuloIntegracionVO(domicilios.get(i)));
        }
        return salida;
    }
    
    
     /**
     * Convierte un Vector de objetos DomicilioSimpleValueObject en un ArrayList<DomicilioInteresadoModuloIntegracionVO>
     * @param domicilios: Vector<DomicilioSimpleValueObject>
     * @return ArrayList<DomicilioInteresadoModuloIntegracionVO>
     */
    public static ArrayList<DomicilioInteresadoModuloIntegracionVO> listDomicilioSimpleValueObjectTolistDomicilioInteresadoModuloIntegracionVO(Vector<DomicilioSimpleValueObject> domicilios,String codDomicilioPrincipal){
        ArrayList<DomicilioInteresadoModuloIntegracionVO> salida = new ArrayList<DomicilioInteresadoModuloIntegracionVO>();
        for(int i=0;domicilios!=null && i<domicilios.size();i++){
            salida.add(domicilioSimpleValueObjectToDomicilioInteresadoModuloIntegracionVO(domicilios.get(i),codDomicilioPrincipal));
        }
        return salida;
    }

    /**
     * Comprueba si un String contien un dato de tipo int
     * @param dato: String a verificar
     * @return Un boolean
     */
    public static boolean isInteger(String dato){
        boolean exito = false;
        try{
            Integer.parseInt(dato);
            exito =  true;
        }catch(NumberFormatException e){
            exito = false;
        }
        return exito;
    }


    /**
     * Devuelve el mensaje de error genérico a mostrar para las operaciones de un módulo para las que se ha producido un error
     * durante su ejecución
     * @param codIdiomaUsuario: Código del idioma del usuario en Flexia
     * @param operacion: Nombre de la operación del módulo
     * @param modulo: Nombre del módulo al que pertenece la operación
     * @return String con el mensaje genérico a mostrar
     */
    public static String getMensajeGenericoErrorOperacionModulo(int codIdiomaUsuario,String operacion,String modulo){
        String mensaje = null;
         TraductorAplicacionBean traductor = new TraductorAplicacionBean();
         traductor.setApl_cod(ConstantesDatos.APP_GESTION_EXPEDIENTES);
         traductor.setIdi_cod(codIdiomaUsuario);

         mensaje = traductor.getDescripcion("msgFalloOpModuloIntegracion1") + " " + operacion + " " + traductor.getDescripcion("msgFalloOpModuloIntegracion2") +  " " +
                   modulo;

         return mensaje;
    }



    /**
     * Compone el mensaje de fallo de una opeación que se ha ejecutado al iniciar un trámite. Esta operación puede pertenecer tanto
     * a un módulo como a un servicio web.
     * @param mensaje: Cadena de texto en la que se hacen las sustituciones
     * @param operacion: Nombre de la operación que ha fallado
     * @param objeto: Nombre del módulo o servicio web al que pertenece la operación     
     * @param traductor: Objeto que permite recuperar etiquetas de idiomas de la BBDD
     * @return String con el texto completo a mostrar al usuario
     */
    public static String getMensajeFalloOperacionInicioTramite(String mensaje,String operacion,String objeto,TraductorAplicacionBean traductor){

        String msg = "";

        if(mensaje!=null && !"".equals(mensaje) && operacion!=null && !"".equals(operacion) && objeto!=null && !"".equals(objeto)){
            StringBuffer salida = new StringBuffer();

            mensaje = mensaje.replaceAll(ConstantesDatos.OPERACION, operacion);
            mensaje = mensaje.replaceAll(ConstantesDatos.OBJETO, objeto);            

            String[] tokens = mensaje.split(ConstantesDatos.DOT_COMMA);

            for(int i=0;tokens!=null && i<tokens.length;i++){
                if(tokens[i]!=null && tokens[i].startsWith("msg"))
                    salida.append(traductor.getDescripcion(tokens[i]));
                else{
                    salida.append(" ");
                    salida.append(tokens[i]);
                    salida.append(" ");
                }
                
            }// for
            msg = salida.toString();
        }
        return msg;
    }
    

}