
package es.altia.flexia.tramitacion.externa.plugin;

import es.altia.flexia.tramitacion.externa.plugin.config.Configuracion;
import es.altia.flexia.tramitacion.externa.plugin.util.ConstantesDatosTramitacionExterna;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.apache.log4j.Logger;

public class TramitacionExternaCargador {

    private final String TRAMITACION_EXTERNA_CONFIG = "tramitacionExterna";
    private Logger log = Logger.getLogger(TramitacionExternaCargador.class);
    private static TramitacionExternaCargador instance = null;

    private TramitacionExternaCargador(){
    }


    public static TramitacionExternaCargador getInstance(){
        if(instance ==null)
            instance = new TramitacionExternaCargador();

        return instance;
    }

   /**
     * Recupera los plugin de tramitaci�n externa disponibles para una organizaci�n
     * @param codOrganizacion: C�digo de la organizaci�n   
     * @return ArrayList<TramitacionExternaBase>
     */
    public ArrayList<TramitacionExternaBase> getPluginDisponibles(int codOrganizacion){
        ArrayList<TramitacionExternaBase> plugins = new ArrayList<TramitacionExternaBase>();

        String vListaDisponibles  = Configuracion.getValor(codOrganizacion + ConstantesDatosTramitacionExterna.TRAMITACION_EXTERNA_DISPONIBLES,TRAMITACION_EXTERNA_CONFIG);
        String[] listaDisponibles = vListaDisponibles.split(ConstantesDatosTramitacionExterna.DOT_COMMA);
        for(int i=0;listaDisponibles!=null && i<listaDisponibles.length;i++){
            log.debug(" --- plugin disponible a tratar: " + listaDisponibles[i]);
            String nombrePlugin = listaDisponibles[i];
            // Se recupera la descripci�n del plugin
            String implClass = Configuracion.getValor(codOrganizacion + ConstantesDatosTramitacionExterna.TRAMITACION_EXTERNA + nombrePlugin + ConstantesDatosTramitacionExterna.IMPL_CLASS, TRAMITACION_EXTERNA_CONFIG);
            log.debug("El plugin " + nombrePlugin  + " est� implementado en la clase: " + implClass);

            String descripcionPlugin = Configuracion.getValor(codOrganizacion + ConstantesDatosTramitacionExterna.TRAMITACION_EXTERNA + nombrePlugin + ConstantesDatosTramitacionExterna.DESCRIPCION, TRAMITACION_EXTERNA_CONFIG);
            log.debug("descripcionPlugin: " + descripcionPlugin);
            if(implClass!=null && !"".equals(implClass)){

                if(descripcionPlugin!=null && !"".equals(descripcionPlugin)){                    
                    TramitacionExternaBase oClass = null;
                    try{
                        Class clase = Class.forName(implClass);
                        oClass = (TramitacionExternaBase)clase.newInstance();
                        int contadorEnlaces = 0;
                        String[] urls = null;
                        if(oClass!=null){                            
                            oClass.setDescripcionPluginDefinicionTramite(descripcionPlugin);
                            oClass.setNombrePlugin(nombrePlugin);
                            oClass.setCodOrganizacion(Integer.toString(codOrganizacion));
                            oClass.setImplClass(implClass);

                            /**** SE COMPRUEBA LOS ENLACES QUE PUEDE TENER EL PLUGIN Y SUS CORRESPONDIENTES PAR�METROS ******/
                            // Habr� que comprobar que existe el fichero de configuraci�n del plugin y que tiene url, parametros y m�todos
                            // para recuperare los par�metros. Adem�s se deber�a comprobar que los m�todos forman parte del plugin.
                            String urlsDisponibles = Configuracion.getValor(codOrganizacion + ConstantesDatosTramitacionExterna.TRAMITACION_EXTERNA + nombrePlugin + ConstantesDatosTramitacionExterna.URLS_DISPONIBLES,nombrePlugin);
                            log.debug("urlsDisponibles: " + urlsDisponibles);

                            if(urlsDisponibles!=null && !"".equals(urlsDisponibles)) urls = urlsDisponibles.split(ConstantesDatosTramitacionExterna.DOT_COMMA);
                            ArrayList<EnlaceTramitacionBase> enlaces = new ArrayList<EnlaceTramitacionBase>();
                            
                            for(int j=0;urls!=null && j<urls.length;j++){
                                String enlace = urls[j];
                                log.debug("enlace: " + enlace);
                                
                                String urlEnlace = Configuracion.getValor(codOrganizacion + ConstantesDatosTramitacionExterna.TRAMITACION_EXTERNA + nombrePlugin + ConstantesDatosTramitacionExterna.BARRA + enlace + ConstantesDatosTramitacionExterna.URL, nombrePlugin);
                                log.debug(" ******** urlEnlace: " + urlEnlace);

                                String parametros = Configuracion.getValor(codOrganizacion + ConstantesDatosTramitacionExterna.TRAMITACION_EXTERNA + nombrePlugin + ConstantesDatosTramitacionExterna.BARRA + enlace + ConstantesDatosTramitacionExterna.PARAMETROS, nombrePlugin);
                                log.debug("parametros: " + parametros);

                                String metodos = Configuracion.getValor(codOrganizacion + ConstantesDatosTramitacionExterna.TRAMITACION_EXTERNA + nombrePlugin + ConstantesDatosTramitacionExterna.BARRA + enlace + ConstantesDatosTramitacionExterna.METODOS, nombrePlugin);
                                log.debug("metodos: " + metodos);

                                if(parametros!=null && urlEnlace!=null && metodos!=null && !"".equals(parametros) && !"".equals(urlEnlace) && !"".equals(metodos)){

                                    String[] listaParametros = null;
                                    String[] listaMetodos    = null;

                                    if(parametros!=null && !"".equals(parametros)){
                                        listaParametros =  parametros.split(ConstantesDatosTramitacionExterna.DOT_COMMA);
                                        log.debug(" ***** N� de par�metros:: "  + listaParametros.length);
                                    }

                                    if(metodos!=null && !"".equals(metodos)){
                                        listaMetodos = metodos.split(ConstantesDatosTramitacionExterna.DOT_COMMA);
                                        log.debug(" ***** N� de m�todos:: "  + listaMetodos.length);
                                    }

                                    // S�lo interesan los enlaces a fichas de tramitaci�n que tenga el n�mero de par�metros y m�todos correctos
                                    if(listaParametros!=null && listaMetodos!=null && listaParametros.length==listaMetodos.length){
                                        // Se comprueba adem�s que los m�todos indicados en el fichero de configuraci�n existan realmente en la clase que implementa el plugin
                                        if(verificarMetodosClase(clase,listaMetodos)){

                                            EnlaceTramitacionBase enlaceVO = new EnlaceTramitacionBase();
                                            enlaceVO.setIdEnlaceConfiguracion(enlace);
                                            enlaceVO.setMetodos(listaMetodos);
                                            enlaceVO.setParametros(listaParametros);
                                            enlaceVO.setUrl(urlEnlace);
                                            enlaces.add(enlaceVO);
                                            contadorEnlaces++;
                                            
                                        }//if
                                    }//if
                                    
                                }// if

                            }// for

                            
                            if(contadorEnlaces>=1){
                                // Si al menos hay alg�n enlace v�lido se devuelve
                                oClass.setEnlaces(enlaces);
                                plugins.add(oClass);
                            }
                           
                        }// if

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                                        
                }// if
            }// if
        }// for

        return plugins;

    }// getPluginDisponibles




   /**
     * Recupera un determinado plugin de tramitaci�n externa de una organizaci�n determinada
     * @param codOrganizacion: C�digo de la organizaci�n
     * @param nombrePlugin: Nombre del plugin para buscarlo en el fichero de propiedades correspondiente
     * @param codUrl: C�digo que identifica a la url seleccionada para el plugin
     * @param url: Url de la ficha de tramitaci�n externa
     * @param implClass: String que contiene la clase que implementa
     * @return TramitacionExternaBase
     */
    public TramitacionExternaBase getPlugin(int codOrganizacion,String nombrePlugin,String codUrl,String url,String implClass){
        TramitacionExternaBase plugin = null;

        log.debug("getPlugin codOrganizacion: " + codOrganizacion + ", nombrePlugin: " + nombrePlugin + ", codUrl: " + codUrl + ", url: " + url + ", implClass: " + implClass);
        if(nombrePlugin!=null && !"".equals(nombrePlugin) && codUrl!=null && !"".equals(codUrl) && url!=null && !"".equals(url) && implClass!=null & !"".equals(implClass)){

            String property_url        = codOrganizacion + ConstantesDatosTramitacionExterna.TRAMITACION_EXTERNA + nombrePlugin + ConstantesDatosTramitacionExterna.BARRA +  codUrl + ConstantesDatosTramitacionExterna.URL;
            String property_parametros = codOrganizacion + ConstantesDatosTramitacionExterna.TRAMITACION_EXTERNA + nombrePlugin + ConstantesDatosTramitacionExterna.BARRA +  codUrl + ConstantesDatosTramitacionExterna.PARAMETROS;
            String property_metodos    = codOrganizacion + ConstantesDatosTramitacionExterna.TRAMITACION_EXTERNA + nombrePlugin + ConstantesDatosTramitacionExterna.BARRA +  codUrl + ConstantesDatosTramitacionExterna.METODOS;
            String property_descripcion= codOrganizacion + ConstantesDatosTramitacionExterna.TRAMITACION_EXTERNA + nombrePlugin + ConstantesDatosTramitacionExterna.DESCRIPCION;
            String property_bloqueo    = codOrganizacion + ConstantesDatosTramitacionExterna.TRAMITACION_EXTERNA + nombrePlugin + ConstantesDatosTramitacionExterna.BLOQUEO_FINALIZAR_TRAMITE;
            String property_ventanaConfig  = codOrganizacion + ConstantesDatosTramitacionExterna.TRAMITACION_EXTERNA + nombrePlugin + ConstantesDatosTramitacionExterna.PARAMETROS_CONFIGURACION_VENTANA;
            String property_bloqueo_retroceso = codOrganizacion + ConstantesDatosTramitacionExterna.TRAMITACION_EXTERNA + nombrePlugin + ConstantesDatosTramitacionExterna.BLOQUEO_RETROCESO_EXPEDIENTE;

            log.debug(" --- propiedad url a recuperar: " + property_url);
            log.debug(" --- propiedad parametros a recuperar: " + property_parametros);
            log.debug(" --- propiedad metodos a recuperar: " + property_metodos);
            log.debug(" --- propiedad descripci�n plugin a recuperar: " + property_descripcion);
            log.debug(" --- propiedad bloqueo: " + property_bloqueo);
            log.debug(" --- propiedad ventana configuraci�n: " + property_ventanaConfig);

            String valor_url         = Configuracion.getValor(property_url,nombrePlugin);
            String valor_parametros  = Configuracion.getValor(property_parametros,nombrePlugin);
            String valor_metodos     = Configuracion.getValor(property_metodos,nombrePlugin);
            String descripcionPlugin = Configuracion.getValor(property_descripcion,this.TRAMITACION_EXTERNA_CONFIG);
            String bloqueoFinalizar  = Configuracion.getValor(property_bloqueo,nombrePlugin);
            String ventanaConfig     = Configuracion.getValor(property_ventanaConfig,nombrePlugin);            
            String bloqueoRetroceso  = Configuracion.getValor(property_bloqueo_retroceso,nombrePlugin);
            
            log.debug(" --- propiedad valor url a recuperar: " + valor_url);
            log.debug(" --- propiedad valor parametros a recuperar: " + valor_parametros);
            log.debug(" --- propiedad valor metodos a recuperar: " + valor_metodos);
            log.debug(" --- propiedad valor descripcionPlugin a recuperar: " + descripcionPlugin);
            log.debug(" --- propiedad valor bloqueoFinalizar a recuperar: " + bloqueoFinalizar);
            log.debug(" --- propiedad valor bloqueoRetroceso a recuperar: " + bloqueoRetroceso);
            log.debug(" --- propiedad valor ventanaConfig a recuperar: " + ventanaConfig);

            if(valor_url!=null && valor_parametros!=null && valor_metodos!=null && !"".equals(valor_url) && !"".equals(valor_parametros)
                    && !"".equals(valor_metodos) && descripcionPlugin!=null && !"".equals(descripcionPlugin) && url.equals(valor_url)){

                Class clase = null;
                try{                    
                    clase = Class.forName(implClass);
                    plugin = (TramitacionExternaBase)clase.newInstance();

                }catch(Exception e){
                    log.error("Error al instanciar la clase del plugin " + implClass + ": " + e.getMessage());
                }

                if(plugin!=null){                   
                    String[] parametros = valor_parametros.split(ConstantesDatosTramitacionExterna.DOT_COMMA);
                    String[] metodos    = valor_metodos.split(ConstantesDatosTramitacionExterna.DOT_COMMA);

                    if(parametros!=null && metodos!=null && parametros.length==metodos.length && verificarMetodosClase(clase, metodos)){

                        plugin.setDescripcionPluginDefinicionTramite(descripcionPlugin);
                        plugin.setNombrePlugin(nombrePlugin);
                        plugin.setCodOrganizacion(Integer.toString(codOrganizacion));
                        plugin.setImplClass(implClass);
                        // En el fichero de configuraci�n puede que no exista la propiedad => sino existe se permite finalizar el expediente
                        if(bloqueoFinalizar!=null && !"".equals(bloqueoFinalizar) && "SI".equalsIgnoreCase(bloqueoFinalizar)){
                            plugin.setBloqueadoFinalizarTramite(true);
                        }else
                            plugin.setBloqueadoFinalizarTramite(false);

                        if(ventanaConfig!=null && !"".equals(ventanaConfig)){
                            // Si hay par�metros de configuraci�n de alto, ancho, etc .. de la ventana que contiene
                            // la ficha de tramitaci�n externa.
                            plugin.setParametrosVentana(ventanaConfig);
                        }
                        
                        if(bloqueoRetroceso!=null && !"".equals(bloqueoRetroceso) && "SI".equalsIgnoreCase(bloqueoRetroceso)){
                            plugin.setBloqueadoRetrocesoTramite(true);                            
                        }else
                            plugin.setBloqueadoRetrocesoTramite(false);                            

                        EnlaceTramitacionBase enlace = new EnlaceTramitacionBase();
                        enlace.setIdEnlaceConfiguracion(codUrl);
                        enlace.setMetodos(metodos);
                        enlace.setParametros(parametros);
                        enlace.setUrl(url);
                        
                        plugin.setPantallaDefinitiva(enlace);
                        
                    }// if
                }// if
            }// if
        }

        return plugin;

    }// getPlugin



   /**
     * Ejecuta el m�todo de un determinado objeto
     * @param objeto: Objeto sobre el que se ejecutar� uno de sus m�todos
     * @param nombreMetodo: Nombre del m�todo a ejecutar
     * @param tipoParams: Class[] con los tipos de los par�metros de entrada del m�todo. Ejemplo: Class[] c = {String.class,int.class,Boolean.class}
     * @param param: Valores de los par�metros de entrada que se pasan a la llamada �metros que se pasan al objeto. Ejemplo: Object[] d = {"prueba",1,true}
     * @return Object que contiene la salida del m�todo
     */
   public Object ejecutarMetodo(Object objeto,String nombreMetodo,Class[] tipoParametros,Object[] valoresParametros){
        Object valor ="";

        try{
            Class clase = Class.forName(objeto.getClass().getName());
            clase.newInstance();

            Method metodo = clase.getMethod(nombreMetodo,tipoParametros);
            valor         = metodo.invoke(objeto,valoresParametros);

        }catch(ClassNotFoundException e){
            log.error(e.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }

        return valor;
    }






    /**
     * Comprueba si una clase dispone de todos los m�todo que se indican en el par�metro listaMetodos
     * @param clase: Objeto de la que se verifican los m�todos
     * @param listaMetodos: Colecic�n con la lista de m�todos
     * @return boolean
     */
   private boolean verificarMetodosClase(Class clase,String[] listaMetodos){
       boolean exito = false;
       Method[] metodos = clase.getMethods();
       int contador = 0;
       if(metodos!=null && metodos.length>0 && listaMetodos!=null && listaMetodos.length>0){
           for(int i=0;i<listaMetodos.length;i++){
               String metodo_config = listaMetodos[i];
               for(int j=0;j<metodos.length;j++){
                   String metodo_clase = metodos[j].getName();
                   if(metodo_clase.equals(metodo_config)){
                        contador++;
                   }
               }// for
           }// for

           log.debug("contador: " + contador + ", listaMetodos: " + listaMetodos.length);
           // Si se han encontrado todos los m�todos en la clase => Entonces se devuelve un true
           if(contador==listaMetodos.length) exito = true;
       }// if


       
       return exito;
   }




}
