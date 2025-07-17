package es.altia.flexia.terceros.integracion.externa.factoria;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.flexia.terceros.integracion.externa.excepciones.InstantiationTerceroExternoException;
import es.altia.flexia.terceros.integracion.externa.servicio.AltaTerceroExterno;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

public class AltaTerceroExternoFactoria {
    
    public static Integer ERROR_INSTANCIA_CLASE_SISTEMA_EXTERNO_TERCEROS = 1;
    public static Integer ERROR_EJECUCION_SISTEMA_EXTERNO_TERCEROS = 2;
    public static Integer ERROR_CAMPOS_OBLIGATORIOS_SISTEMA_EXTERNO_TERCEROS = 3;
    public static Integer ERROR_RESTRICCIONES_ALTA_SISTEMA_EXTERNO_TERCEROS = 4;
    public static Integer ERROR_RESTRICCIONES_MOD_SISTEMA_EXTERNO_TERCEROS = 5;
    public static Integer ERROR_SALIDA_ALTA_TERCERO_SISTEMA_EXTERNO_TERCEROS = 6;
    private static String FICHERO_CONFIGURACION = "Terceros";
    private static Logger log = Logger.getLogger(AltaTerceroExternoFactoria.class);


    public static AltaTerceroExterno getServicio(String codOrganizacion,String idServicio) throws InstantiationTerceroExternoException{
        AltaTerceroExterno salida = null;
        
        try {
            String propiedad  = ConstantesDatos.ALTA_TERCERO_EXTERNO + codOrganizacion + ConstantesDatos.BARRA +  idServicio + ConstantesDatos.ALTA_TERCERO_EXTERNO_IMPLCLASS;
            ResourceBundle config = ResourceBundle.getBundle(FICHERO_CONFIGURACION);
            String nameClass = config.getString(propiedad);

            Class clase = Class.forName(nameClass);
            salida = (AltaTerceroExterno)clase.newInstance();
            salida.setCodOrganizacion(codOrganizacion);
            salida.setNOMBRE_SERVICIO(idServicio);

            
            // Se comprueba si hay definidas restricciones de tipos de documentos para el alta de un tercero
            String pRestriccionesTipoDocumentoAlta = ConstantesDatos.ALTA_TERCERO_EXTERNO + codOrganizacion + ConstantesDatos.BARRA +  idServicio + ConstantesDatos.ALTA_TERCERO_EXTERNO_TIPOS_DOCS_VALIDOS_ALTA;
            String restriccionesTipoDocumentoAlta = config.getString(pRestriccionesTipoDocumentoAlta);
            
            if(restriccionesTipoDocumentoAlta!=null && !"".equals(restriccionesTipoDocumentoAlta)){

                String[] datos = restriccionesTipoDocumentoAlta.split(ConstantesDatos.DOT_COMMA);
                if(datos!=null && datos.length>=1){                    
                    salida.setRestriccionesTipoDocumentoAlta(new ArrayList<String>(Arrays.asList(datos)));
                }                
            }

            // Se comprueba si hay definidas restricciones de tipos de documentos para la modificación de un tercero
            String pRestriccionesTipoDocumentoMod = ConstantesDatos.ALTA_TERCERO_EXTERNO + codOrganizacion + ConstantesDatos.BARRA +  idServicio + ConstantesDatos.ALTA_TERCERO_EXTERNO_TIPOS_DOCS_VALIDOS_MOD;
            String restriccionesTipoDocumentoMod = config.getString(pRestriccionesTipoDocumentoMod);
            if(restriccionesTipoDocumentoMod!=null && !"".equals(restriccionesTipoDocumentoMod)){

                String[] datos = restriccionesTipoDocumentoMod.split(ConstantesDatos.DOT_COMMA);
                if(datos!=null && datos.length>=1){
                    salida.setRestriccionesTipoDocumentoMod(new ArrayList<String>(Arrays.asList(datos)));
                }
            }

        } catch (Exception cnfe) {            
            cnfe.printStackTrace();
            throw new InstantiationTerceroExternoException("NO SE HA ENCONTRADO LA CLASE QUE SE QUIERE INSTANCIAR " +
                    "EN EL FICHERO DE CONFIGURACION " + FICHERO_CONFIGURACION, cnfe);
        }        
        return salida;
    }


    /**
     * Devuelve todos los servicios de terceros externos disponibles
     * @param codOrganizacion: Código de la organización
     * @return ArrayList<String>
     */
    /*
    public static ArrayList<String> getServiciosTercerosExternosDisponibles(String codOrganizacion){
        ArrayList<String> servicios = new ArrayList<String>();

        String propiedad = ConstantesDatos.ALTA_TERCERO_EXTERNO + codOrganizacion + ConstantesDatos.ALTA_TERCERO_EXTERNO_SERVICIOS_DISPONIBLES;
        String listaServicios = getValor(propiedad,FICHERO_CONFIGURACION);

        if(listaServicios!=null && !"".equals(listaServicios)){
            String[] lista = listaServicios.split(ConstantesDatos.DOT_COMMA);
            List<String> servs = Arrays.asList(lista);

            if(servs!=null && servs.size()>=1){
                servicios = new ArrayList<String>(servs);
            }
        }

        return servicios;
    } */

    public static ArrayList<String> getServiciosTercerosExternosDisponibles(String codOrganizacion, int codAplicacion){
        ArrayList<String> servicios = new ArrayList<String>();
        ArrayList<String> auxiliar = new ArrayList<String>();

        String propiedad = ConstantesDatos.ALTA_TERCERO_EXTERNO + codOrganizacion + ConstantesDatos.ALTA_TERCERO_EXTERNO_SERVICIOS_DISPONIBLES;
        String listaServicios = getValor(propiedad,FICHERO_CONFIGURACION);

        if(listaServicios!=null && !"".equals(listaServicios)){
            String[] lista = listaServicios.split(ConstantesDatos.DOT_COMMA);
            List<String> servs = Arrays.asList(lista);

            if(servs!=null && servs.size()>=1){
                auxiliar = new ArrayList<String>(servs);
            }
        }

        // Se comprueba si alguno de los servicios externos tiene restringida el alta desde un determinado módulo de Flexia
        for(String servicio: auxiliar){
            if(!altaTerceroRestringidaServicio(codOrganizacion,servicio,codAplicacion))
                servicios.add(servicio);
        }

        return servicios;
    }


  /**
    * Comprueba si el alta en un sistema de terceros externo está restringida para realizarla desde un determinado
    * módulo de aplicación de Flexia.
    * @param codOrganizacion: Código de la organización
    * @param strServicio: Nombre del servicio
    * @param codAplicacion: Código  de la aplicación
    * @return Un boolean
    */
   private static boolean altaTerceroRestringidaServicio(String codOrganizacion,String strServicio,int codAplicacion){
       boolean exito = false;

       log.debug("codOrganizacion: " + codOrganizacion + ",strServicio: " + strServicio + ",codAplicacion: " + codAplicacion);
       String propiedad = ConstantesDatos.ALTA_TERCERO_EXTERNO + codOrganizacion + ConstantesDatos.BARRA + strServicio + ConstantesDatos.RESTRICCIONES_ALTA_TERCERO;
       String valor = getValor(propiedad,"Terceros");
       log.debug("propiedad: " + propiedad + ",valor: " + valor);
       if(valor!=null && !"".equals(valor)){
           String[] datos = valor.split(ConstantesDatos.DOT_COMMA);
           //for(int i=0;datos!=null && i<datos.length;i++){
           for(String codigo: datos){
               if(isInteger(codigo)){
                    int aux = Integer.parseInt(codigo);
                    if(aux==codAplicacion) exito = true;
               }// if
           }// for
       }// if
       return exito;
   }


   /**
     * Comprueba en el fichero de configuración, si el alta de un tercero en un sistema externo (en el caso de que exista alguno habilitado)
     * requiere transaccionalidad, es decir, para dar de alta en tercero en Flexia, que también se de de alta en el/los sistema/s de tercero/s
     * disponibles. En el caso de que no exista la propiedad, se asume que el alta requiere transaccionalidad
     * @param codOrganizacion: Código de la organización
     * @return boolean
     */
    public static boolean altaTerceroExternoRequiereTransaccionalidad(String codOrganizacion){
        boolean exito = false;

        String propiedad = ConstantesDatos.ALTA_TERCERO_EXTERNO + codOrganizacion + ConstantesDatos.ALTA_TERCERO_TRANSACCIONAL;
        String valor     = getValor(propiedad,FICHERO_CONFIGURACION);

        if((valor!=null && !"".equals(valor) && "true".equals(valor)) || (valor==null || "".equals(valor))){
            exito = true;
        }else        
            exito = false;

        return exito;
    }

  /**
    * Recupera el valor de una determinada propiedad del fichero de configuración
    * @param propiedad: Nombre de la propiedad
    * @return el valor o null si existe o no se ha podido recuperar
    */
   private static String getValor(String propiedad,String nameFile){
       String salida = null;
       
       try{
           ResourceBundle bundle = ResourceBundle.getBundle(nameFile);
           salida = bundle.getString(propiedad);
       }catch(Exception e){
           log.error("No se ha podido recuperar la propiedad " + propiedad + " del fichero de configuración " + nameFile);
       }
       return salida;
   }

  /**
    * Comprueba si un String contiene un dato de tipo numérico
    * @param dato: String
    * @return true si todo correcto y false en caso contrario
    */
   private static boolean isInteger(String dato){
       boolean exito = false;
       try{
            Integer.parseInt(dato);
            exito = true;
       }catch(NumberFormatException e){
           exito = false;
       }
       return exito;
   }
   
}