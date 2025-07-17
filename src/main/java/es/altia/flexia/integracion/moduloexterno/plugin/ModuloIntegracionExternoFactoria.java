package es.altia.flexia.integracion.moduloexterno.plugin;

import es.altia.agora.business.escritorio.persistence.UsuarioManager;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.ModuloIntegracionExternoManager;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.DatosPantallaModuloVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.EstructuraCampoModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.EstructuraEtiquetaModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.OperacionModuloIntegracionExternoVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

/**
 * Factoria de los módulos de integración
 */
public class ModuloIntegracionExternoFactoria {

    private static ModuloIntegracionExternoFactoria instance = null;
    private static final String FILE_CONFIGURATION_PRINCIPAL      = "ModulosIntegracion";
    private Logger log = Logger.getLogger(ModuloIntegracionExternoFactoria.class);
    
    private ModuloIntegracionExternoFactoria(){
    }

    public static ModuloIntegracionExternoFactoria getInstance(){
        if(instance==null)
            instance = new ModuloIntegracionExternoFactoria();

        return instance;
    }

    /**
     * Comprueba si una determinada organización tiene algún módulo de integración activado
     * @param codOrganizacion: Código de la organización
     * @return boolean
     */
    public boolean tieneModulosIntegracionActivados(int codOrganizacion){
        boolean salida = false;
        try{
            
            ResourceBundle bundle = ResourceBundle.getBundle(FILE_CONFIGURATION_PRINCIPAL);
            String property       = codOrganizacion + ConstantesDatos.MODULOS_INTEGRACION_ACTIVOS;
            String aux            = bundle.getString(property);
            if(aux!=null && !"".equals(aux))
                salida = true;

        }catch(Exception e){
            log.error(e.getMessage());
            salida = false;
        }
        return salida;
    }

   
    /**
     * Devuelve la instancia de la clase que implementa un determinado módulo
     * @param codOrganizacion: Código de la organización
     * @param nombreModulo: Nombre del módulo
     * @param bundle: Fichero de configuración del que extraer los datos
     * @return ModuloIntegracionExterno o null sino se puede instanciar la clase
     */
    public ModuloIntegracionExterno getImplClass(int codOrganizacion,String nombreModulo){
        ModuloIntegracionExterno implClass = null;
        try{

            String property  = codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + nombreModulo + ConstantesDatos.MODULO_IMPL_CLASS;
            String nameClass = getValor(property,FILE_CONFIGURATION_PRINCIPAL);
            log.debug("nameClass: " + nameClass);

            Class clase = Class.forName(nameClass);
            implClass = (ModuloIntegracionExterno)clase.newInstance();
            implClass.setNombreModulo(nombreModulo);
            implClass.setDescripcionModulo(getValor(codOrganizacion+ ConstantesDatos.MODULO_INTEGRACION + nombreModulo + ConstantesDatos.DESCRIPCION_MODULO,nombreModulo));


        }catch(Exception e){
            implClass = null;
            log.error(e.getMessage());
        }
        return implClass;
    }

    /**
     * Recupera una colección con todos los módulos de integración activados para una determinada organización
     * @param codOrganizacion: Código de la organización
     * @return ArrayList<ModuloIntegracionExterno>
     */
     public ArrayList<ModuloIntegracionExterno> getImplClass(int codOrganizacion){
        ArrayList<ModuloIntegracionExterno> salida = new ArrayList<ModuloIntegracionExterno>();
        try{
            
            // SE OBTIENE LA LISTA DE MÓDULOS ACTIVOS PARA LA ORGANIZACIÓN
            String modulosActivos = getValor(codOrganizacion + ConstantesDatos.MODULOS_INTEGRACION_ACTIVOS,FILE_CONFIGURATION_PRINCIPAL);
            if(modulosActivos!=null && !"".equals(modulosActivos)){

                String[] listaModulos = modulosActivos.split(ConstantesDatos.COMMA);
                for(int i=0;listaModulos!=null && i<listaModulos.length;i++){
                    // PARA CADA MÓDULO SE OBTIENE LA CLASE QUE LO IMPLEMENTA Y SE RECUPERA EL
                    // NOMBRE DEL MÓDULO
                    String modulo = listaModulos[i];
                    ModuloIntegracionExterno implClass = this.getImplClass(codOrganizacion, modulo);
                    String propiedadDescModulo = codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.DESCRIPCION_MODULO;
                    String descripcion = this.getValor(propiedadDescModulo,modulo);
                    
                    if(implClass!=null && descripcion!=null && !"".equals(descripcion) && modulo!=null && !"".equals(modulo)){
                        // SE RECUPERA LA DESCRIPCIÓN DEL MÓDULO DEL FICHERO DE CONFIGURACIÓN
                        implClass.setNombreModulo(modulo);
                        implClass.setDescripcionModulo(descripcion);
                        implClass.setListaOperacionesDisponibles(this.getListaOperacionesDisponibles(codOrganizacion, modulo,descripcion,modulo));
                        salida.add(implClass);
                   }
                }//for

            }//if

        }catch(Exception e){
            log.error(e.getMessage());
            salida = new ArrayList<ModuloIntegracionExterno>();
        }

        return salida;
     }


     /**
      * Recupera la lista de operaciones disponibles para un determinado módulo. La lista de operaciones se utiliza para asociar un
      * trámite a una operación de un módulo. También se recupera el resto de información asociada a cada operación como la url
      * de la pantalla de configuración y sus dimensiones
      * @param codOrganizacion: Código de la organización
      * @param nombreModulo: Nombre del módulo
      * @param NOMBRE_FICHERO_CONFIGURACION: Nombre del fichero de configuración
      * @return ArrayList<OperacionModuloIntegracionExternoVO>
      */
     private ArrayList<OperacionModuloIntegracionExternoVO> getListaOperacionesDisponibles(int codOrganizacion,String nombreModulo,String descripcionModulo,String NOMBRE_FICHERO_CONFIGURACION){
        ArrayList<OperacionModuloIntegracionExternoVO> salida = new ArrayList<OperacionModuloIntegracionExternoVO>();
        // Operaciones disponibles para realizar la configuración del módulo
        String operaciones  = this.getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + nombreModulo + ConstantesDatos.BARRA + ConstantesDatos.LISTA_OPERACIONES_DISPONIBLES_MODULO,NOMBRE_FICHERO_CONFIGURACION);
        int codigo = 1;
        if(operaciones!=null && !"".equals(operaciones)){

            String[] listaOps = operaciones.split(ConstantesDatos.COMMA);
            for(int i=0;listaOps!=null && i<listaOps.length;i++){
                String operacion = listaOps[i];
                OperacionModuloIntegracionExternoVO op = new OperacionModuloIntegracionExternoVO();
                op.setCodigoOperacion(Integer.toString(codigo));
                op.setTituloOperacion(operacion);
                op.setNombreModulo(nombreModulo);
                op.setDescripcionModulo(descripcionModulo);

                String urlPantallaConfiguracion = codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + nombreModulo + ConstantesDatos.BARRA +
                                                  operacion + ConstantesDatos.URL_PANTALLA_CONFIGURACION_OPERACION_MODULO;
                
                String url = this.getValor(urlPantallaConfiguracion,NOMBRE_FICHERO_CONFIGURACION);
                if(url!=null && !"".equals(url))
                    op.setUrlPantallaConfiguracion(url);
                else
                    op.setUrlPantallaConfiguracion(null);

                String altoPantallaConfiguracion = codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + nombreModulo + ConstantesDatos.BARRA +
                                                   operacion + ConstantesDatos.ALTO_PANTALLA_CONFIGURACION_OPERACION_MODULO;

                String anchoPantallaConfiguracion = codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + nombreModulo + ConstantesDatos.BARRA +
                                                    operacion + ConstantesDatos.ANCHO_PANTALLA_CONFIGURACION_OPERACION_MODULO;
                
                String alto  = this.getValor(altoPantallaConfiguracion,NOMBRE_FICHERO_CONFIGURACION);
                String ancho = this.getValor(anchoPantallaConfiguracion,NOMBRE_FICHERO_CONFIGURACION);
                op.setAltoPantallaConfiguracion(null);
                op.setAnchoPantallaConfiguracion(null);
                
                if(isInteger(alto) && isInteger(ancho)){
                    op.setAltoPantallaConfiguracion(alto);
                    op.setAnchoPantallaConfiguracion(ancho);
                }
                salida.add(op);
                codigo++;

            }// for
        }// if
        return salida;
    }

   
   /**
     * Ejecuta el método de un determinado objeto
     * @param objeto: Objeto sobre el que se ejecutará uno de sus métodos
     * @param nombreMetodo: Nombre del método a ejecutar
     * @param tipoParams: Class[] con los tipos de los parámetros de entrada del método. Ejemplo: Class[] c = {String.class,int.class,Boolean.class}
     * @param param: Valores de los parámetros de entrada que se pasan a la llamada ámetros que se pasan al objeto. Ejemplo: Object[] d = {"prueba",1,true}
     * @return Puede devolver 2 valores: 0: Si no se ha ejecutado correctamente
     *                                   1: Si se ha ejecutado correctamente
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
    * Recupera el valor de una determinada propiedad del fichero de configuración
    * @param propiedad: Nombre de la propiedad
    * @return el valor o null si existe o no se ha podido recuperar
    */
   public String getValor(String propiedad,String nameFile){
       String salida = null;
       try{
           ResourceBundle bundle = ResourceBundle.getBundle(nameFile);
           salida = bundle.getString(propiedad);

       }catch(Exception e){           
           log.debug("No se ha podido recuperar la propiedad " + propiedad + " del fichero de configuración " + nameFile);
       }

       return salida;
   }

   /**
    * Comprueba si un String contiene un dato numérico de tipo entero
    * @param dato: String a validar
    * @return Boolean
    */
   private boolean isInteger(String dato){
       boolean exito = false;
       try{
           if(dato!=null && !"".equals(dato)){
                Integer.parseInt(dato);
                exito = true;
           }
       }catch(NumberFormatException e){
           exito = false;
       }
       return exito;
   }

   
   /**
     * Recupera los módulos de integración activos para una organización y recupera para un determinado trámite de un procedimiento las pantallas de tramitación que tenga definidas
     * @param codOrganizacion: Código de la organización
     * @params codProcedimiento: Cod. del procedimiento
     * @param codTramite: Código del trámite
     * @param relacion: True si la pantalla de tramitación pertenece a una relación de expediente y false en otro caso
     * @param codUsuario: Código del usuario
     * @param params: Parámetros de conexión a la BD
     * @return ArrayList<ModuloIntegracionExterno>
     */
     //public ArrayList<ModuloIntegracionExterno> getImplClassModuloTramiteConPantallaTramitacion(int codOrganizacion,String codProcedimiento,int codTramite,boolean relacion,String[] params){
   public ArrayList<ModuloIntegracionExterno> getImplClassModuloTramiteConPantallaTramitacion(int codOrganizacion,String codProcedimiento,int codTramite,boolean relacion,int codUsuario,String[] params){
        ArrayList<ModuloIntegracionExterno> salida = new ArrayList<ModuloIntegracionExterno>();
        log.debug("==================> getImplClassModuloTramiteConPantallaTramitacion ");

        try{
            String valor = getValor(codOrganizacion + ConstantesDatos.MODULOS_INTEGRACION_ACTIVOS,this.FILE_CONFIGURATION_PRINCIPAL);
            String[] modulosTramite = valor.split(ConstantesDatos.COMMA);

            for(int i=0;modulosTramite!=null && i<modulosTramite.length;i++){
                String modulo = modulosTramite[i];
                log.debug("modulo: " + modulo);
                String url = "";                
                String opProcesar  ="";
                String nombrePantallasTramite = "";

                if(modulo!=null && !"".equals(modulo)){

                    ModuloIntegracionExterno implClass = this.getImplClass(codOrganizacion, modulo);
                    if(implClass!=null){
                        String propiedadDescModulo = codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.DESCRIPCION_MODULO;
                        String descripcion = this.getValor(propiedadDescModulo,modulo);
                        // SE RECUPERA LA DESCRIPCIÓN DEL MÓDULO DEL FICHERO DE CONFIGURACIÓN
                        implClass.setNombreModulo(modulo);
                        implClass.setDescripcionModulo(descripcion);
                        implClass.setListaOperacionesDisponibles(this.getListaOperacionesDisponibles(codOrganizacion, modulo,descripcion,modulo));

                        ArrayList<DatosPantallaModuloVO> pantallas = new ArrayList<DatosPantallaModuloVO>();

                        String c_url = "";
                        String c_nombrePantallasTramite = "";
                        String c_proceso = "";
                        if(!relacion){
                            // Si el trámite no pertenece a una relación de expedientes
                            c_url                    = ConstantesDatos.URL_PANTALLA_TRAMITACION_OPERACION_MODULO;
                            c_nombrePantallasTramite = ConstantesDatos.PANTALLA_TRAMITE_NOMBRE_PANTALLAS;
                            c_proceso                = ConstantesDatos.PANTALLA_TRAMITE_OPERACION_PROCESAR;

                        }else{
                            // Si el trámite pertenece a una relación de expedientes
                            c_url                    = ConstantesDatos.URL_PANTALLA_TRAMITACION_RELACION;
                            c_nombrePantallasTramite = ConstantesDatos.PANTALLA_TRAMITE_RELACION_NOMBRE_PANTALLAS;
                            c_proceso                = ConstantesDatos.PANTALLA_TRAMITE_RELACION_OPERACION_PROCESAR;
                        }

                        //String pNombrePantallas = codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA + codProcedimiento + ConstantesDatos.BARRA + codTramite + ConstantesDatos.PANTALLA_TRAMITE_NOMBRE_PANTALLAS;
                        String pNombrePantallas = codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA + codProcedimiento + ConstantesDatos.BARRA + codTramite + c_nombrePantallasTramite;
                        
                        
                        nombrePantallasTramite = getValor(pNombrePantallas,modulo);
                        if(nombrePantallasTramite!=null && !"".equals(nombrePantallasTramite)){
                            String[] listaPantallas = nombrePantallasTramite.split(ConstantesDatos.DOT_COMMA);
                            for(int j=0;listaPantallas!=null && j<listaPantallas.length;j++){
                                // Si el módulo está activo se recuperan las url de tramitación que tenga activas                                
                                url        = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA + codProcedimiento + ConstantesDatos.BARRA + codTramite + c_url + ConstantesDatos.BARRA + listaPantallas[j],modulo);
                                opProcesar = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA + codProcedimiento + ConstantesDatos.BARRA + codTramite + c_proceso + ConstantesDatos.BARRA + listaPantallas[j],modulo);

                                if(url!=null && !"".equals(url)){
                                    if(implClass!=null && descripcion!=null && !"".equals(descripcion)){
                                        if(url!=null && !"".equals(url)){
                                            
                                            // SE COMPRUEBA SI HAY ALGUNA RESTRICCIÓN PARA LA PANTALLA POR UNIDAD ORGANIZATIVA
                                            String listaUnidadesOrganicas = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA + codProcedimiento + ConstantesDatos.BARRA + codTramite + ConstantesDatos.BARRA + listaPantallas[j] + ConstantesDatos.CODIGOS_UNIDADES_ORGANICAS_VALIDAS,modulo);
                                            String[] unidades = null;
                                            if(listaUnidadesOrganicas!=null && !"".equals(listaUnidadesOrganicas)){
                                                unidades = listaUnidadesOrganicas.split(ConstantesDatos.COMMA);                            
                                            }
                                                    
                                            boolean continuar = false;
                                            if(unidades==null || unidades.length==0)
                                                continuar = true;
                                            else
                                            if(UsuarioManager.getInstance().tieneUsuarioPermisoSobreUnidadesOrganizativas(codOrganizacion, codUsuario, unidades, params))
                                                continuar = true;
                                            
                                            
                                            if(continuar){
                                                DatosPantallaModuloVO pantVO = new DatosPantallaModuloVO();
                                                pantVO.setNombrePantalla(listaPantallas[j]);
                                                pantVO.setUrl(url);
                                                pantVO.setOperacionProceso(opProcesar);
                                                pantVO.setCodProcedimiento(codProcedimiento);
                                                pantVO.setPantallaProcedimiento(false);
                                                pantVO.setCodTramite(codTramite);
                                                pantallas.add(pantVO);
                                            }
                                            
                                        }//if
                                   }//if
                              }
                            }// for
                        }else{

                            String pUrl = codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA + codProcedimiento + ConstantesDatos.BARRA + codTramite + ConstantesDatos.URL_PANTALLA_TRAMITACION_OPERACION_MODULO;
                            String pOp  = codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA + codProcedimiento + ConstantesDatos.BARRA + codTramite + ConstantesDatos.OPERACION_PROCESAR;
                            url        = getValor(pUrl,modulo);
                            opProcesar = getValor(pOp,modulo);

                            if(url!=null && !"".equals(url)){
                                if(implClass!=null && descripcion!=null && !"".equals(descripcion)){
                                    if(url!=null && !"".equals(url)){       
                                        DatosPantallaModuloVO pantVO = new DatosPantallaModuloVO();
                                        pantVO.setNombrePantalla(null);
                                        pantVO.setUrl(url);
                                        pantVO.setOperacionProceso(opProcesar);
                                        pantVO.setCodProcedimiento(codProcedimiento);
                                        pantVO.setPantallaProcedimiento(false);
                                        pantVO.setCodTramite(codTramite);
                                        pantallas.add(pantVO);
                                    }//if
                               }//if
                         
                            }// if
                    }// else

                    implClass.setListaPantallasTramite(pantallas);
                    salida.add(implClass);
                    
                }// if                
            }// if
         }// for
          
        }catch(Exception e){
            log.error(e.getMessage());
            salida = new ArrayList<ModuloIntegracionExterno>();
        }

        return salida;
     }

   
   /**
     * Recupera los módulos de integración activos para una organización que tengan asociados pantalla que se muestran a nivel de expediente de un
     * de un determinado procedimiento
     * @param codOrganizacion: Código de la organización
     * @param codProcedimiento Código del procedimiento
     * @param relacion: Indica si el expediente pertenece a una relación entre expedientes
     * @param codUsuario: Código del usuario
     * @param params: Parámetros de conexión a la BD
     * @return ArrayList<ModuloIntegracionExterno>
     */
      public ArrayList<ModuloIntegracionExterno> getImplClassModuloConPantallaExpediente(int codOrganizacion,String codProcedimiento,boolean relacion,int codUsuario,String[] params){
        ArrayList<ModuloIntegracionExterno> salida = new ArrayList<ModuloIntegracionExterno>();
        log.debug("==================> getImplClassModuloConPantallaExpediente ");

        try{
            String valor = getValor(codOrganizacion + ConstantesDatos.MODULOS_INTEGRACION_ACTIVOS,FILE_CONFIGURATION_PRINCIPAL);
            String[] modulosTramite = valor.split(ConstantesDatos.COMMA);

            for(int i=0;modulosTramite!=null && i<modulosTramite.length;i++){
                String modulo = modulosTramite[i];
                log.debug("modulo: " + modulo);
                String url = "";
                String opProcesar = "";
                
                if(modulo!=null && !"".equals(modulo)){
                    // Si el módulo está activo se recuperan las url de tramitación que tenga activas
                    String pantalla = null;
                    String proceso = null;
                    String nombrePantallasExpediente = null;
                    if(!relacion){ // Si no pertenece a una relación entre expedientes
                        pantalla               = ConstantesDatos.URL_PANTALLA_EXPEDIENTE_OPERACION_MODULO;
                        nombrePantallasExpediente = ConstantesDatos.PANTALLA_EXPEDIENTE_NOMBRE_PANTALLAS;                        
                        proceso                = ConstantesDatos.PANTALLA_EXPEDIENTE_OPERACION_PROCESAR;
                    }
                    else{ // Si pertenece a una relación entre expedientes
                        pantalla   = ConstantesDatos.URL_PANTALLA_EXPEDIENTE_RELACION_OPERACION_MODULO;
                        nombrePantallasExpediente = ConstantesDatos.PANTALLA_EXPEDIENTE_RELACION_NOMBRE_PANTALLAS;                        
                        proceso                = ConstantesDatos.PANTALLA_EXPEDIENTE_RELACION_OPERACION_PROCESAR;
                    }
                                       
                    nombrePantallasExpediente = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA + codProcedimiento + nombrePantallasExpediente,modulo);
                    log.debug(" *********** Para el módulo " + modulo + "hay un número de pantallas de expediente: " + nombrePantallasExpediente);

                    /**********  Descripción del módulo ********************/
                    String propiedadDescModulo = codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.DESCRIPCION_MODULO;
                    String descripcion = this.getValor(propiedadDescModulo,modulo);
                    /**********  Lista que almacena las pantallas de expediente de cada módulo ******************/
                    ArrayList<DatosPantallaModuloVO> pantallas = new ArrayList<DatosPantallaModuloVO>();

                    ModuloIntegracionExterno implClass = this.getImplClass(codOrganizacion, modulo);
                    if(implClass!=null){
                        implClass.setNombreModulo(modulo);
                        implClass.setDescripcionModulo(descripcion);
                        implClass.setListaOperacionesDisponibles(this.getListaOperacionesDisponibles(codOrganizacion, modulo,descripcion,modulo));

                        if(nombrePantallasExpediente!=null && !"".equals(nombrePantallasExpediente)){
                           // Si existe
                           String[] listaPantallas = nombrePantallasExpediente.split(ConstantesDatos.DOT_COMMA);
                           for(int j=0;j<listaPantallas.length;j++)
                           {
                               log.debug("Tratando la pantalla de expediente " + listaPantallas[j] + " del módulo " + modulo);

                                                
                               url = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA + codProcedimiento + pantalla + ConstantesDatos.BARRA + listaPantallas[j],modulo);
                               opProcesar = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA + codProcedimiento + proceso + ConstantesDatos.BARRA + listaPantallas[j],modulo);

                               if(url!=null && !"".equals(url)){                                                                                
                                   // SE COMPRUEBA SI HAY ALGUNA RESTRICCIÓN PARA LA PANTALLA POR UNIDAD ORGANIZATIVA                                                                            
                                    String listaUnidadesOrganicas = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA + codProcedimiento + ConstantesDatos.BARRA + listaPantallas[j] + ConstantesDatos.CODIGOS_UNIDADES_ORGANICAS_VALIDAS,modulo);
                                    String[] unidades = null;
                                    if(listaUnidadesOrganicas!=null && !"".equals(listaUnidadesOrganicas)){
                                        unidades = listaUnidadesOrganicas.split(ConstantesDatos.COMMA);                            
                                    }

                                    boolean continuar = false;
                                    if(unidades==null || unidades.length==0)
                                        continuar = true;
                                    else
                                    if(UsuarioManager.getInstance().tieneUsuarioPermisoSobreUnidadesOrganizativas(codOrganizacion, codUsuario, unidades, params))
                                        continuar = true;
                                   
                                    if(continuar){
                                        DatosPantallaModuloVO pantVO = new DatosPantallaModuloVO();
                                        pantVO.setNombrePantalla(listaPantallas[j]);
                                        pantVO.setUrl(url);
                                        pantVO.setOperacionProceso(opProcesar);
                                        pantVO.setCodProcedimiento(codProcedimiento);
                                        pantVO.setPantallaProcedimiento(true);
                                        pantallas.add(pantVO);
                                    }

                               }//if
                           }// for
                       }else{
                            // Para mantener la compatibilidad con los módulos que sólo permitían cargar una pestaña a nivel de expediente por
                            // procedimiento y módulo. Si no está definida la propiedad se comprueba si existe sin añadir a la propiedad el sufijo con el número
                            // de pantalla, si la hay se recupera. En el caso de existir más de una pantalla definida de este modo, se recupera la última.

                            url = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA + codProcedimiento + pantalla,modulo);
                            opProcesar = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA + codProcedimiento + proceso,modulo);

                            if(url!=null && !"".equals(url)){
                                DatosPantallaModuloVO pantVO = new DatosPantallaModuloVO();
                                pantVO.setNombrePantalla(null);
                                pantVO.setUrl(url);
                                pantVO.setOperacionProceso(opProcesar);
                                pantVO.setCodProcedimiento(codProcedimiento);
                                pantVO.setPantallaProcedimiento(true);
                                pantallas.add(pantVO);
                            }//if


                       }// else

                       // Se indican las pantallas a cargar del mismo módulo para un determinado procedimiento. También se guarda en la colección la
                      // operación que se encargar de procesar cada pantalla en caso de ser necesario
                      implClass.setListaPantallasExpediente(pantallas);
                      salida.add(implClass);
                    }// if implClass
                }

            }// for

        }catch(Exception e){
            log.error(e.getMessage());
            salida = new ArrayList<ModuloIntegracionExterno>();
        }

        return salida;
     }

      
      
      
     public ArrayList<ModuloIntegracionExterno> getImplClassModuloConPantallaExpediente(int codOrganizacion,String codProcedimiento,boolean relacion,int codUsuario,Connection con){
        ArrayList<ModuloIntegracionExterno> salida = new ArrayList<ModuloIntegracionExterno>();
        log.debug("==================> getImplClassModuloConPantallaExpediente ");

        try{
            String valor = getValor(codOrganizacion + ConstantesDatos.MODULOS_INTEGRACION_ACTIVOS,FILE_CONFIGURATION_PRINCIPAL);
            String[] modulosTramite = valor.split(ConstantesDatos.COMMA);

            for(int i=0;modulosTramite!=null && i<modulosTramite.length;i++){
                String modulo = modulosTramite[i];
                log.debug("modulo: " + modulo);
                String url = "";
                String opProcesar = "";
                
                if(modulo!=null && !"".equals(modulo)){
                    // Si el módulo está activo se recuperan las url de tramitación que tenga activas
                    String pantalla = null;
                    String proceso = null;
                    String nombrePantallasExpediente = null;
                    if(!relacion){ // Si no pertenece a una relación entre expedientes
                        pantalla               = ConstantesDatos.URL_PANTALLA_EXPEDIENTE_OPERACION_MODULO;
                        nombrePantallasExpediente = ConstantesDatos.PANTALLA_EXPEDIENTE_NOMBRE_PANTALLAS;                        
                        proceso                = ConstantesDatos.PANTALLA_EXPEDIENTE_OPERACION_PROCESAR;
                    }
                    else{ // Si pertenece a una relación entre expedientes
                        pantalla   = ConstantesDatos.URL_PANTALLA_EXPEDIENTE_RELACION_OPERACION_MODULO;
                        nombrePantallasExpediente = ConstantesDatos.PANTALLA_EXPEDIENTE_RELACION_NOMBRE_PANTALLAS;                        
                        proceso                = ConstantesDatos.PANTALLA_EXPEDIENTE_RELACION_OPERACION_PROCESAR;
                    }
                                       
                    nombrePantallasExpediente = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA + codProcedimiento + nombrePantallasExpediente,modulo);
                    log.debug(" *********** Para el módulo " + modulo + "hay un número de pantallas de expediente: " + nombrePantallasExpediente);

                    /**********  Descripción del módulo ********************/
                    String propiedadDescModulo = codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.DESCRIPCION_MODULO;
                    String descripcion = this.getValor(propiedadDescModulo,modulo);
                    /**********  Lista que almacena las pantallas de expediente de cada módulo ******************/
                    ArrayList<DatosPantallaModuloVO> pantallas = new ArrayList<DatosPantallaModuloVO>();

                    ModuloIntegracionExterno implClass = this.getImplClass(codOrganizacion, modulo);
                    if(implClass!=null){
                        implClass.setNombreModulo(modulo);
                        implClass.setDescripcionModulo(descripcion);
                        implClass.setListaOperacionesDisponibles(this.getListaOperacionesDisponibles(codOrganizacion, modulo,descripcion,modulo));

                        if(nombrePantallasExpediente!=null && !"".equals(nombrePantallasExpediente)){
                           // Si existe
                           String[] listaPantallas = nombrePantallasExpediente.split(ConstantesDatos.DOT_COMMA);
                           for(int j=0;j<listaPantallas.length;j++)
                           {
                               log.debug("Tratando la pantalla de expediente " + listaPantallas[j] + " del módulo " + modulo);

                                                
                               url = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA + codProcedimiento + pantalla + ConstantesDatos.BARRA + listaPantallas[j],modulo);
                               opProcesar = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA + codProcedimiento + proceso + ConstantesDatos.BARRA + listaPantallas[j],modulo);

                               if(url!=null && !"".equals(url)){                                                                                
                                   // SE COMPRUEBA SI HAY ALGUNA RESTRICCIÓN PARA LA PANTALLA POR UNIDAD ORGANIZATIVA                                                                            
                                    String listaUnidadesOrganicas = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA + codProcedimiento + ConstantesDatos.BARRA + listaPantallas[j] + ConstantesDatos.CODIGOS_UNIDADES_ORGANICAS_VALIDAS,modulo);
                                    String[] unidades = null;
                                    if(listaUnidadesOrganicas!=null && !"".equals(listaUnidadesOrganicas)){
                                        unidades = listaUnidadesOrganicas.split(ConstantesDatos.COMMA);                            
                                    }

                                    boolean continuar = false;
                                    if(unidades==null || unidades.length==0)
                                        continuar = true;
                                    else
                                    //if(UsuarioManager.getInstance().tieneUsuarioPermisoSobreUnidadesOrganizativas(codOrganizacion, codUsuario, unidades, params))
                                    if(UsuarioManager.getInstance().tieneUsuarioPermisoSobreUnidadesOrganizativas(codOrganizacion, codUsuario, unidades, con))
                                        continuar = true;
                                   
                                    if(continuar){
                                        DatosPantallaModuloVO pantVO = new DatosPantallaModuloVO();
                                        pantVO.setNombrePantalla(listaPantallas[j]);
                                        pantVO.setUrl(url);
                                        pantVO.setOperacionProceso(opProcesar);
                                        pantVO.setCodProcedimiento(codProcedimiento);
                                        pantVO.setPantallaProcedimiento(true);
                                        pantallas.add(pantVO);
                                    }

                               }//if
                           }// for
                       }else{
                            // Para mantener la compatibilidad con los módulos que sólo permitían cargar una pestaña a nivel de expediente por
                            // procedimiento y módulo. Si no está definida la propiedad se comprueba si existe sin añadir a la propiedad el sufijo con el número
                            // de pantalla, si la hay se recupera. En el caso de existir más de una pantalla definida de este modo, se recupera la última.

                            url = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA + codProcedimiento + pantalla,modulo);
                            opProcesar = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA + codProcedimiento + proceso,modulo);

                            if(url!=null && !"".equals(url)){
                                DatosPantallaModuloVO pantVO = new DatosPantallaModuloVO();
                                pantVO.setNombrePantalla(null);
                                pantVO.setUrl(url);
                                pantVO.setOperacionProceso(opProcesar);
                                pantVO.setCodProcedimiento(codProcedimiento);
                                pantVO.setPantallaProcedimiento(true);
                                pantallas.add(pantVO);
                            }//if


                       }// else

                       // Se indican las pantallas a cargar del mismo módulo para un determinado procedimiento. También se guarda en la colección la
                      // operación que se encargar de procesar cada pantalla en caso de ser necesario
                      implClass.setListaPantallasExpediente(pantallas);
                      salida.add(implClass);
                    }// if implClass
                }

            }// for

        }catch(Exception e){
            log.error(e.getMessage());
            salida = new ArrayList<ModuloIntegracionExterno>();
        }

        return salida;
     }
      
      
      
      
 

  /**
     * Recupera una lista de funciones javascripts de la/s pestaña/s de expediente de los módulos que están asociados a un determinado procedimiento que
     * serán llamados cada vez que se graba un expediente, ya que pueda haber datos del expediente que sean utilizados en estas pestañas y al pulsar el botón
     * de Grabar, será necesario que estas pestañas se actualicen de alguna manera. De este modo al llamar a estas funciones javascript, éstas podrán realizar la acción correspondiente como actualizarse.
     * @param codOrganizacion: Código de la organización
     * @param codProcedimiento: Código del procedimiento
     * @return ArrayList<String> con el nombre de las funciones javascript a llamar.Si no hay definida
     * ninguna función javascript, entonces la colección estará vacía.
     */
    public ArrayList<String> getFuncionesJavascriptActualizarPantallaExpediente(int codOrganizacion,String codProcedimiento){
        ArrayList<String> funciones = new ArrayList<String>();
        try{
            
            String activo = getValor(codOrganizacion + ConstantesDatos.MODULOS_INTEGRACION_ACTIVOS,FILE_CONFIGURATION_PRINCIPAL);
            if(activo!=null && !"".equals(activo)){

                String[] activos = activo.split(ConstantesDatos.COMMA);
                for(int i=0;activos!=null && i<activos.length;i++){
                    String modulo = activos[i];
                    String nombrePantallasExpedienteProcedimiento = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA + codProcedimiento + ConstantesDatos.PANTALLA_EXPEDIENTE_NOMBRE_PANTALLAS,modulo);
                    if(nombrePantallasExpedienteProcedimiento!=null && !"".equals(nombrePantallasExpedienteProcedimiento)){

                        String[] listaPantallas = nombrePantallasExpedienteProcedimiento.split(ConstantesDatos.DOT_COMMA);
                        for(int j=0;listaPantallas!=null && j<listaPantallas.length;j++){

                            // Si está definida la propiedad con el número de pestañas de expediente para el modulo y el procedimiento
                            String property = codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA
                                    + codProcedimiento + ConstantesDatos.ACTUALIZAR_PANTALLA_EXPEDIENTE + ConstantesDatos.BARRA + listaPantallas[j];

                            log.debug("property: " + property);
                            String valor = getValor(property,modulo);
                            log.debug("valor: " + valor);
                            if(valor!=null && !"".equals(valor))
                                funciones.add(valor);
                        }// for numero
                        
                   }// if numero
                   else{
                        // Para mantener la compatibilidad con módulos antiguos en los que sólo podía cargarse una pantalla de expediente por módulo
                        // y procedimiento. Si no está definida la propiedad que indica el número de pantallas de expediente de un procedimiento y módulo determinado, se supone
                        // que sólo hay una
                        String property = codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA
                                + codProcedimiento + ConstantesDatos.ACTUALIZAR_PANTALLA_EXPEDIENTE;

                        log.debug("property: " + property);
                        String valor = getValor(property,modulo);
                        log.debug("valor: " + valor);
                        if(valor!=null && !"".equals(valor)) funciones.add(valor);

                   }// else

                }// for modulos
            }// if activo
        }catch(Exception e){
            funciones = new ArrayList<String>();
            log.error(e.getMessage());
        }
        
        return funciones;
    }



   /**
     * Recupera una lista de funciones javascripts de la/s pestaña/s de trámite de los módulos que están asociados a un determinado trámite y procedimiento que
     * serán llamados cada vez que se graba un trámite y pueda haber valores de campos suplementarios del mismo que sean utilizados en alguna de las pestañas
     * de trámite que se cargan. De este modo al llamar a estas funciones javascript, éstas podrán realizar la acción correspondiente como actualizarse.
     * @param codOrganizacion: Código de la organización
     * @param codTramite: Código del trámite
     * @param codProcedimiento: Código del procedimiento
     * @param relacion: Si está a true el trámite pertenece a una relación entre expediente y si es false no
     * @return ArrayList<String> con el nombre de las funciones javascript a llamar.Si no hay definida
     * ninguna función javascript, entonces la colección estará vacía.
     */
    public ArrayList<String> getFuncionesJavascriptActualizarPantallaTramitacion(int codOrganizacion,int codTramite,String codProcedimiento,boolean relacion){
        ArrayList<String> funciones = new ArrayList<String>();
        try{

            String activo = getValor(codOrganizacion + ConstantesDatos.MODULOS_INTEGRACION_ACTIVOS,FILE_CONFIGURATION_PRINCIPAL);
            if(activo!=null && !"".equals(activo)){

                String[] activos = activo.split(ConstantesDatos.COMMA);
                for(int i=0;activos!=null && i<activos.length;i++){
                    String modulo = activos[i];

                    String actualizar = "";
                    String pantalla = "";
                    if(!relacion){
                        actualizar = ConstantesDatos.ACTUALIZAR_PANTALLA_TRAMITE;
                        pantalla = ConstantesDatos.PANTALLA_TRAMITE_NOMBRE_PANTALLAS;
                    }
                    else{
                        actualizar = ConstantesDatos.ACTUALIZAR_PANTALLA_TRAMITE_RELACION;
                        pantalla = ConstantesDatos.PANTALLA_TRAMITE_RELACION_NOMBRE_PANTALLAS;
                    }


                    //String nombrePantallasTramite = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA + codProcedimiento + ConstantesDatos.BARRA + codTramite + ConstantesDatos.PANTALLA_TRAMITE_NOMBRE_PANTALLAS,modulo);
                    String nombrePantallasTramite = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA + codProcedimiento + ConstantesDatos.BARRA + codTramite + pantalla,modulo);
                    if(nombrePantallasTramite!=null && !"".equals(nombrePantallasTramite)){

                        String[] listaPantallas = nombrePantallasTramite.split(ConstantesDatos.DOT_COMMA);
                        for(int j=0;listaPantallas!=null && j<listaPantallas.length;j++){
                           
                            String property = codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA
                                    + codProcedimiento + ConstantesDatos.BARRA + codTramite + actualizar + ConstantesDatos.BARRA + listaPantallas[j];

                            log.debug("property: " + property);
                            String valor = getValor(property,modulo);
                            log.debug("valor: " + valor);
                            if(valor!=null && !"".equals(valor))
                                funciones.add(valor);
                        }// for numero

                   }// if numero
                   else{
                        // Para mantener la compatibilidad con módulos antiguos en los que sólo podía cargarse una pestaña de trámite por módulo, trámite
                        // y procedimiento.
                        
                        String property = codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA
                                + codProcedimiento + ConstantesDatos.BARRA + codTramite + actualizar;

                        log.debug("property: " + property);
                        String valor = getValor(property,modulo);
                        log.debug("valor: " + valor);
                        if(valor!=null && !"".equals(valor)) funciones.add(valor);

                   }// else

                }// for modulos
          }// if activo
        }catch(Exception e){
            funciones = new ArrayList<String>();
            log.error(e.getMessage());
        }

        return funciones;
    }
    
    
     /**
     * Recupera una lista de funciones javascripts de la/s pestaña/s de trámite de los módulos que están asociados a un determinado trámite y procedimiento que
     * serán llamados cada vez que se accede a un trámite. 
     * De este modo al llamar a estas funciones javascript, éstas podrán realizar la acción correspondiente.
     * @param codOrganizacion: Código de la organización
     * @param codTramite: Código del trámite
     * @param codProcedimiento: Código del procedimiento
     * @return ArrayList<String> con el nombre de las funciones javascript a llamar.Si no hay definida
     * ninguna función javascript, entonces la colección estará vacía.
     */
    public ArrayList<String> getFuncionesJavascriptAccederPantallaTramitacion(int codOrganizacion,int codTramite,String codProcedimiento){
        ArrayList<String> funciones = new ArrayList<String>();
        try{

            String activo = getValor(codOrganizacion + ConstantesDatos.MODULOS_INTEGRACION_ACTIVOS,FILE_CONFIGURATION_PRINCIPAL);
            if(activo!=null && !"".equals(activo)){

                String[] activos = activo.split(ConstantesDatos.COMMA);
                for(int i=0;activos!=null && i<activos.length;i++){
                    String modulo = activos[i];

                    String acceder = "";
                    String pantalla = "";
                    acceder = ConstantesDatos.ONLOAD_PANTALLA_TRAMITE;
                    pantalla = ConstantesDatos.PANTALLA_TRAMITE_NOMBRE_PANTALLAS;
                   


                    //String nombrePantallasTramite = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA + codProcedimiento + ConstantesDatos.BARRA + codTramite + ConstantesDatos.PANTALLA_TRAMITE_NOMBRE_PANTALLAS,modulo);
                    String nombrePantallasTramite = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA + codProcedimiento + ConstantesDatos.BARRA + codTramite + pantalla,modulo);
                    if(nombrePantallasTramite!=null && !"".equals(nombrePantallasTramite)){

                        String[] listaPantallas = nombrePantallasTramite.split(ConstantesDatos.DOT_COMMA);
                        for(int j=0;listaPantallas!=null && j<listaPantallas.length;j++){
                           
                            String property = codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA
                                    + codProcedimiento + ConstantesDatos.BARRA + codTramite + acceder + ConstantesDatos.BARRA + listaPantallas[j];

                            log.debug("Property: ------->" + property);
                            String valor = getValor(property,modulo);
                            log.debug("Valor:----------> " + valor);
                            if(valor!=null && !"".equals(valor))
                                funciones.add(valor);
                        }// for numero

                   }// if numero
                   else{
                        // Para mantener la compatibilidad con módulos antiguos en los que sólo podía cargarse una pestaña de trámite por módulo, trámite
                        // y procedimiento.
                        
                        String property = codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA
                                + codProcedimiento + ConstantesDatos.BARRA + codTramite + acceder;

                        log.debug("Property:--------> " + property);
                        String valor = getValor(property,modulo);
                        log.debug("Valor:------------> " + valor);
                        if(valor!=null && !"".equals(valor)) funciones.add(valor);

                   }// else

                }// for modulos
          }// if activo
        }catch(Exception e){
            funciones = new ArrayList<String>();
            log.error(e.getMessage());
        }

        return funciones;
    }
    
    
  /**
     * Recupera una lista con las implementaciones de los módulos activos
     * en Flexia y que tienen asociada una pantalla de definición de procedimiento,
     * para un procedimiento concreto
     * @param codOrganizacion: Código de la organización
     * @param codProcedimiento: Código del procedimiento
     * @return ArrayList<String> 
     */
    public ArrayList<ModuloIntegracionExterno>getImplClassModuloConPantallaDefinicionProcedimiento(int codOrganizacion,String codProcedimiento,String[] params){
        ArrayList<ModuloIntegracionExterno> salida = new ArrayList<ModuloIntegracionExterno>();
        log.debug("==================> getImplClassModuloConPantallaDefinicionProcedimiento ");

        try{
            String valor = getValor(codOrganizacion + ConstantesDatos.MODULOS_INTEGRACION_ACTIVOS,FILE_CONFIGURATION_PRINCIPAL);
            String[] modulosTramite = valor.split(ConstantesDatos.COMMA);

            for(int i=0;modulosTramite!=null && i<modulosTramite.length;i++){
                String modulo = modulosTramite[i];
                log.debug("modulo: " + modulo);
                String url = "";
                String opProcesar = "";

                if(modulo!=null && !"".equals(modulo)){
                    // Si el módulo está activo se recuperan las url de tramitación que tenga activas
                    String pantalla = null;
                    String proceso = null;
                    String nombrePantallasExpediente = null;
                    
                        pantalla               = ConstantesDatos.URL_PANTALLA_PROCEDIMIENTO_OPERACION_MODULO;
                        nombrePantallasExpediente = ConstantesDatos.PANTALLA_DEFINICION_PROCEDIMIENTO_NOMBRE_PANTALLAS;
                        proceso                = ConstantesDatos.PANTALLA_DEFINICION_PROCEDIMIENTO_OPERACION_PROCESAR;
                    
                    nombrePantallasExpediente = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA + codProcedimiento + nombrePantallasExpediente,modulo);
                    log.debug(" *********** Para el módulo " + modulo + " hay un número de pantallas de def procedimientos: " + nombrePantallasExpediente);

                    /**********  Descripción del módulo ********************/
                    String propiedadDescModulo = codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.DESCRIPCION_MODULO;
                    String descripcion = this.getValor(propiedadDescModulo,modulo);
                    
					/**********  Lista que almacena las pantallas de expediente de cada módulo ******************/
                    ArrayList<DatosPantallaModuloVO> pantallas = new ArrayList<DatosPantallaModuloVO>();
                    ModuloIntegracionExterno implClass = this.getImplClass(codOrganizacion, modulo);
                   
					if(implClass!=null){
                        implClass.setNombreModulo(modulo);
                        implClass.setDescripcionModulo(descripcion);
                        implClass.setListaOperacionesDisponibles(this.getListaOperacionesDisponibles(codOrganizacion, modulo,descripcion,modulo));
                        
                        if(nombrePantallasExpediente!=null && !"".equals(nombrePantallasExpediente)){
                           String[] listaPantallas = nombrePantallasExpediente.split(ConstantesDatos.DOT_COMMA);
                           for(int j=0;j<listaPantallas.length;j++)
                           {
                               url = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA + codProcedimiento + pantalla + ConstantesDatos.BARRA + listaPantallas[j],modulo);
                               opProcesar = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA + codProcedimiento + proceso + ConstantesDatos.BARRA + listaPantallas[j],modulo);
                               if(url!=null && !"".equals(url)){
                                    DatosPantallaModuloVO pantVO = new DatosPantallaModuloVO();
                                    pantVO.setNombrePantalla(listaPantallas[j]);
                                    pantVO.setUrl(url);
                                    pantVO.setOperacionProceso(opProcesar);
                                    pantVO.setCodProcedimiento(codProcedimiento);
                                    pantVO.setPantallaProcedimiento(true);
                                    pantallas.add(pantVO);

                               }//if
                           }// for
                       }else{
                            // Para mantener la compatibilidad con los módulos que sólo permitían cargar una pestaña a nivel de expediente por
                            // procedimiento y módulo. Si no está definida la propiedad se comprueba si existe sin añadir a la propiedad el sufijo con el número
                            // de pantalla, si la hay se recupera. En el caso de existir más de una pantalla definida de este modo, se recupera la última.

                            url = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA + codProcedimiento + pantalla,modulo);
                            opProcesar = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA + codProcedimiento + proceso,modulo);
                            
                            if(url!=null && !"".equals(url)){
                                DatosPantallaModuloVO pantVO = new DatosPantallaModuloVO();
                                pantVO.setNombrePantalla(null);
                                pantVO.setUrl(url);
                                pantVO.setOperacionProceso(opProcesar);
                                pantVO.setCodProcedimiento(codProcedimiento);
                                pantVO.setPantallaProcedimiento(true);
                                pantallas.add(pantVO);
                            }//if


                       }// else

                       // Se indican las pantallas a cargar del mismo módulo para un determinado procedimiento. También se guarda en la colección la
                      // operación que se encargar de procesar cada pantalla en caso de ser necesario
                        /**
                      for (Iterator<DatosPantallaModuloVO> iter = pantallas.iterator(); iter.hasNext();) {
                            log.debug("Url:" +iter.next().getUrl());
                            
                      }**/
                      
                    }// if implClass
                                        
                     
                    if(pantallas!=null && pantallas.size()>0){
                        implClass.setListaPantallasDefinicionProcedimiento(pantallas);
                        salida.add(implClass);
                    }                    
                }

            }// for

        }catch(Exception e){
            log.error(e.getMessage());
            salida = new ArrayList<ModuloIntegracionExterno>();
        }
        return salida;

    }
 
    
   /**
     * Recupera una lista de módulos con las implementaciones de los módulos activos
     * en Flexia y que tienen definidos datos de consulta
     * relacionados con un determinado procedimiento.
     * @param codOrganizacion: Código de la organización
     * @param codProcedimiento: Código del procedimiento
     * @return ArrayList<ModuloIntegracionExterno> 
     */   
 public ArrayList<ModuloIntegracionExterno> getImplClassModuloConCamposConsulta
         (int codOrganizacion, String codProcedimiento, String[] params) {
    
     log.debug("==================> getImplClassModuloConCamposConsulta. BEGIN. para el codProcedimiento: "+codProcedimiento);
     ArrayList<ModuloIntegracionExterno> resultado= new ArrayList<ModuloIntegracionExterno>(); 
     //Vamos a buscar si existen las propiedades correspondientes en el fichero de configuración
     //del módulo
     
     try{
            String valor = getValor(codOrganizacion + ConstantesDatos.MODULOS_INTEGRACION_ACTIVOS,FILE_CONFIGURATION_PRINCIPAL);
            String[] modulosTramite = valor.split(ConstantesDatos.COMMA);

            for(int i=0;modulosTramite!=null && i<modulosTramite.length;i++){
                String modulo = modulosTramite[i];
                log.debug("getImplClassModuloConCamposConsulta. Modulo : " + modulo);
             

                if(modulo!=null && !"".equals(modulo)){
                    // Si el módulo está activo tenemos que recuperar los datos de consulta relacionados con el procedimiento
                    String camposConsultaString = null;
                    String rotuloS="";
                    String sqlS="";
                    String  tamanhoS="";
                    String tipoS="";
                    camposConsultaString = ConstantesDatos.CAMPOS_CONSULTA;
                    String nombreTablaS=ConstantesDatos.TIPO_CAMPOS_CONSULTA_NOMBRE_TABLA;
                    String nombreCampoDescripcionesS=ConstantesDatos.TIPO_CAMPOS_CONSULTA_DESCRIPCIONES;
                    String nombreCampoCodigoS=ConstantesDatos.TIPO_CAMPOS_CONSULTA_CODIGOS;
                            
                    rotuloS=ConstantesDatos.ROTULO_CAMPOS_CONSULTA;
                    sqlS=ConstantesDatos.SQL_CAMPOS_CONSULTA;
                    tamanhoS=ConstantesDatos.TAMANHO_CAMPOS_CONSULTA;
                    tipoS=ConstantesDatos.TIPO_CAMPOS_CONSULTA;
                    
                            
                    String nombreCamposConsulta = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA + codProcedimiento + camposConsultaString,modulo);
                    log.debug(" *********** Para el módulo " + modulo + " hay los siguientes campos de consulta " + nombreCamposConsulta);
                    /**********  Descripción del módulo ********************/
                    String propiedadDescModulo = codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.DESCRIPCION_MODULO;
                    String descripcion = this.getValor(propiedadDescModulo,modulo);
                   //Por cada campo de consulta, tenemos qeu crear una estructura EstructuraCampoModuloIntegracionVO
                   ArrayList<EstructuraCampoModuloIntegracionVO> campos = new ArrayList<EstructuraCampoModuloIntegracionVO>();
                   ModuloIntegracionExterno implClass = this.getImplClass(codOrganizacion, modulo);
                   
		   if(implClass!=null){
                        implClass.setNombreModulo(modulo);
                        implClass.setDescripcionModulo(descripcion);
                        if(nombreCamposConsulta!=null && !"".equals(nombreCamposConsulta)){
                           String[] camposConsulta = nombreCamposConsulta.split(ConstantesDatos.DOT_COMMA);
                           for(int j=0;j<camposConsulta.length;j++){
                            log.debug("Campo Consulta: "+camposConsulta[j]);
                            String rotulo = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.CONSULTA + codProcedimiento +ConstantesDatos.BARRA+ camposConsulta[j] + rotuloS,modulo);
                            log.debug("Rotulo: "+rotulo);
                            String tamanho= getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.CONSULTA + codProcedimiento +ConstantesDatos.BARRA+ camposConsulta[j] + tamanhoS,modulo);
                            log.debug("Tamanho: "+tamanho);
                            String tipo=getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.CONSULTA + codProcedimiento +ConstantesDatos.BARRA+ camposConsulta[j] + tipoS,modulo);
                            log.debug("Tipo: "+tipo);
                            String sql=  getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.CONSULTA + codProcedimiento +ConstantesDatos.BARRA+ camposConsulta[j] + sqlS,modulo);
                            log.debug("SQL: "+ sql);
                             
                            //Además tb necesitamos cargar de la BD, los valores de los campos desplegables, si es q los hai
                           ArrayList<ValorCampoDesplegableModuloIntegracionVO> valoresDesplegable=new ArrayList<ValorCampoDesplegableModuloIntegracionVO>(); 
                           
                           String nombreTabla=getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.CONSULTA + codProcedimiento +ConstantesDatos.BARRA+ camposConsulta[j]+ nombreTablaS,modulo);
                           log.debug("Nombre Tabla: "+ nombreTabla);
                           String nombreCampoCodigo=getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.CONSULTA + codProcedimiento + ConstantesDatos.BARRA+camposConsulta[j]+ nombreCampoCodigoS,modulo);
                           log.debug("Nombre Campo Codigo: "+ nombreCampoCodigo);
                           String nombreCampoDescripcion=getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.CONSULTA + codProcedimiento + ConstantesDatos.BARRA+camposConsulta[j]+ nombreCampoDescripcionesS,modulo);
                           log.debug("Nombre Campo Descripcion: "+ nombreCampoDescripcion);
                           
                           if(tipo!=null && "6".equals(tipo)){
                                if((nombreTabla!=null) &&  (!("".equals(nombreTabla))) && (nombreCampoCodigo!=null)
                                     && (!("".equals(nombreCampoCodigo))) && (nombreCampoDescripcion!=null) && (!("".equals(nombreCampoDescripcion))) ){


                                     valoresDesplegable=ModuloIntegracionExternoManager.getInstance().getValoresCampoConsultaDesplegable(nombreTabla, nombreCampoCodigo, nombreCampoDescripcion, params);
                                }else{
                                    // Si no se ha definido un nombre de tabla ni campos de base de datos con el código y la descripción.
                                    // Se comprueba
                                        
                                    String propiedadCodigosCampos = codOrganizacion + "/MODULO_INTEGRACION/" + modulo + "/CONSULTA/" + codProcedimiento + "/" + camposConsulta[j] + "/VALORES/CODIGOS";
                                    String propiedadDescripcionCampos = codOrganizacion + "/MODULO_INTEGRACION/" + modulo + "/CONSULTA/" + codProcedimiento + "/" + camposConsulta[j] + "/VALORES/DESCRIPCIONES";
                                    
                                    String valorCodigosCampos     = getValor(propiedadCodigosCampos,modulo);
                                    String valorDescripcionCampos = getValor(propiedadDescripcionCampos,modulo);
                                    
                                    if(valorCodigosCampos!=null && !"".equals(valorCodigosCampos) && valorDescripcionCampos!=null && !"".equals(valorDescripcionCampos)){
                                        String[] codigos = valorCodigosCampos.split(";");
                                        String[] descripciones = valorDescripcionCampos.split(";");
                                        
                                        if(codigos!=null && descripciones!=null && codigos.length==descripciones.length){
                                         
                                            for(int z=0;z<codigos.length;z++){
                                                
                                                ValorCampoDesplegableModuloIntegracionVO valorDesp = new ValorCampoDesplegableModuloIntegracionVO();
                                                valorDesp.setCodigo(codigos[z]);
                                                valorDesp.setDescripcion(descripciones[z]);
                                                valoresDesplegable.add(valorDesp);
                                            }
                                            
                                        }//if
                                        
                                    }//if
                                }// else        
                            }// if
                                   
                            
                            EstructuraCampoModuloIntegracionVO estructuraVO = new EstructuraCampoModuloIntegracionVO();
                            estructuraVO.setCodCampo(camposConsulta[j]);
                            estructuraVO.setRotulo(rotulo);
                            if(tamanho!=null && !"".equals(tamanho)) estructuraVO.setTamanho(Integer.parseInt(tamanho));                            
                            estructuraVO.setTipoCampo(Integer.parseInt(tipo));
                            estructuraVO.setConsultaSql(sql); 
                            estructuraVO.setValoresDesplegable(valoresDesplegable);
                            campos.add(estructuraVO);
                           }// for
                       }

                     
                      
                   }// if implClass
                                        
                     
                   if(campos!=null && campos.size()>0){
                       implClass.setCamposConsulta(campos);
                       resultado.add(implClass);
                   } 
                    
                }

            }// for

        }catch(Exception e){
            log.error(e.getMessage());
            resultado = new ArrayList<ModuloIntegracionExterno>();
        }
    
     log.debug("==================> getImplClassModuloConCamposConsulta. END. tamanho de la lista devuelta"+resultado.size() );
     return resultado;
 }
 
 /**
     * Recupera una lista de módulos con las implementaciones de los módulos activos
     * en Flexia y que tienen definidos etiquetas para exportar a plantillas
     * relacionados con un determinado procedimiento.
     * @param codOrganizacion: Código de la organización
     * @param codProcedimiento: Código del procedimiento
     * @param params: Parámetros para acceder a la BD
     * @param interesado: Indica si la plantilla es de Interesado (valor "S")
     * @param relacion: Indica si la plantilla es de Relacion (valor "S")
     * @return ArrayList<ModuloIntegracionExterno> 
     */   
 public ArrayList<ModuloIntegracionExterno> getImplClassModuloConEtiquetas
         (int codOrganizacion, String codProcedimiento, String[] params, String interesado, String relacion) {
    
     log.debug("==================> getImplClassModuloConEtiquetas. BEGIN. para el codProcedimiento: "+codProcedimiento+
             " con Interesado: "+interesado + "y relacion: " +relacion);
     ArrayList<ModuloIntegracionExterno> resultado= new ArrayList<ModuloIntegracionExterno>(); 
     
     
     
     try{
            String valor = getValor(codOrganizacion + ConstantesDatos.MODULOS_INTEGRACION_ACTIVOS,FILE_CONFIGURATION_PRINCIPAL);
            String[] modulosTramite = valor.split(ConstantesDatos.COMMA);

            for(int i=0;modulosTramite!=null && i<modulosTramite.length;i++){
                String modulo = modulosTramite[i];
                log.debug("getImplClassModuloConEtiquetas. Modulo : " + modulo);
               
                if(modulo!=null && !"".equals(modulo)){
                    
                    /**********  Descripción del módulo ********************/
                    String propiedadDescModulo = codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.DESCRIPCION_MODULO;
                    String descripcion = this.getValor(propiedadDescModulo,modulo);
                    
                    String etiquetaS=ConstantesDatos.ETIQUETAS;
                    String sqlS=ConstantesDatos.SQL_ETIQUETA;
                    String nombreS=ConstantesDatos.NOMBRE_ETIQUETA;
                    String tipoS=ConstantesDatos.TIPO_ETIQUETA;
                    String codigoS=ConstantesDatos.CODIGO_ETIQUETA;
                    String nombreColumnaS=ConstantesDatos.SQL_NOMBRE_COLUMNA;
                    String campoS=ConstantesDatos.CAMPOE;
                    String whereS=ConstantesDatos.CLAUSULA_WHERE;
                    String and1S=ConstantesDatos.CLAUSULA_AND1;
                    String and2S=ConstantesDatos.CLAUSULA_AND2;
                    String and3S=ConstantesDatos.CLAUSULA_AND3;
                    String formatoFechaS=ConstantesDatos.FORMATO_FECHA;
                    String formatoNumeroS=ConstantesDatos.FORMATO_NUMERO;
                       
                    //Inicializamos el vector de etiquetas de Plantilla de Interesado
                    ArrayList<EstructuraEtiquetaModuloIntegracionVO> etiquetasADevolverPlantillaI = new ArrayList<EstructuraEtiquetaModuloIntegracionVO>();
                    //Inicializamos el vector de etiquetas de Plantilla de Relacion
                    ArrayList<EstructuraEtiquetaModuloIntegracionVO> etiquetasADevolverPlantillaR = new ArrayList<EstructuraEtiquetaModuloIntegracionVO>();
                    //Inicializamos el vector de etiquetas de Plantilla "nada"
                    ArrayList<EstructuraEtiquetaModuloIntegracionVO> etiquetasADevolverPlantillaN = new ArrayList<EstructuraEtiquetaModuloIntegracionVO>();
                    
                    
                  // Vamos a recuperar las etiquetas por plantillas (actualmente existen tres tipos de plantillas) 
                  // Recuperar las etiquetas de la plantilla de Interesado
                  if ("S".equals(interesado)){
                        String etiquetasPlantillaI = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA +
                    codProcedimiento+ ConstantesDatos.PLANTILLA_I + etiquetaS,modulo);
                        log.debug("Etiquetas para la plantillaI:"+etiquetasPlantillaI);
                        if(etiquetasPlantillaI!=null && !"".equals(etiquetasPlantillaI)){
                            String[] listaEtiquetasPlantillaI = etiquetasPlantillaI.split(ConstantesDatos.DOT_COMMA);
                            for(int j=0;j<listaEtiquetasPlantillaI.length;j++){
                                log.debug("Etiqueta de Plantilla I: "+listaEtiquetasPlantillaI[j]);

                                String nombreEtiqueta = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA          +  codProcedimiento +ConstantesDatos.PLANTILLA_I+ ConstantesDatos.BARRA+listaEtiquetasPlantillaI[j] +nombreS,modulo);
                                log.debug("Nombre Etiqueta de la Plantilla I: "+nombreEtiqueta);

                                 String tipoEtiqueta = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                  +ConstantesDatos.BARRA         +  codProcedimiento +ConstantesDatos.PLANTILLA_I+ ConstantesDatos.BARRA+listaEtiquetasPlantillaI[j] +tipoS,modulo);
                                log.debug("Tipo Etiqueta de la Plantilla I: "+tipoEtiqueta);
                                
                                 String formatoFecha = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA  +  codProcedimiento +ConstantesDatos.PLANTILLA_I +ConstantesDatos.BARRA+ listaEtiquetasPlantillaI[j] +formatoFechaS,modulo);
                                log.debug("Formato Fecha de la etiqueta de la Plantilla I: "+formatoFecha);
                                
                                 String formatoNumero = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA  +  codProcedimiento +ConstantesDatos.PLANTILLA_I +ConstantesDatos.BARRA+ listaEtiquetasPlantillaI[j] +formatoNumeroS,modulo);
                                log.debug("Formato Numero  de la etiqueta de la Plantilla I: "+formatoNumero);
                                
                                String codigoEtiqueta = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA          +  codProcedimiento +ConstantesDatos.PLANTILLA_I+ConstantesDatos.BARRA+ listaEtiquetasPlantillaI[j] +codigoS,modulo);
                                log.debug("Codigo Etiqueta de la Plantilla I: "+codigoEtiqueta);
                                
                                String sqlEtiqueta = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA       +  codProcedimiento +ConstantesDatos.PLANTILLA_I+ConstantesDatos.BARRA+ listaEtiquetasPlantillaI[j] +sqlS,modulo);
                                log.debug("Sql Etiqueta de la PlantillaI: "+sqlEtiqueta);
                            
                                 String nombreColumnaEtiqueta = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA       +  codProcedimiento +ConstantesDatos.PLANTILLA_I+ConstantesDatos.BARRA+ listaEtiquetasPlantillaI[j] +nombreColumnaS,modulo);
                                 log.debug("Nombre columna de la PlantillaI: "+ nombreColumnaEtiqueta);
                                
                                 String clausulaWhere = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA       +  codProcedimiento +ConstantesDatos.PLANTILLA_I+ConstantesDatos.BARRA+ listaEtiquetasPlantillaI[j] +whereS,modulo);
                                 log.debug("Nombre de la clausula Where de la PlantillaI: "+ clausulaWhere);
                                
                                 String campoWhere=getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA       +  codProcedimiento +ConstantesDatos.PLANTILLA_I+ConstantesDatos.BARRA+ listaEtiquetasPlantillaI[j] +whereS+campoS,modulo);
                                 log.debug("Campo del where: "+ campoWhere);
                                
                                 String clausulaAnd1 = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA       +  codProcedimiento +ConstantesDatos.PLANTILLA_I+ConstantesDatos.BARRA+ listaEtiquetasPlantillaI[j] +and1S,modulo);
                                 log.debug("Nombre de la clausula And1 de la PlantillaI: "+ clausulaAnd1);
                                
                                 String campoAnd1=getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA       +  codProcedimiento +ConstantesDatos.PLANTILLA_I+ConstantesDatos.BARRA+ listaEtiquetasPlantillaI[j] +and1S+campoS,modulo);
                                 log.debug("Campo del and1: "+ campoAnd1);
                                
                                 String clausulaAnd2 = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA       +  codProcedimiento +ConstantesDatos.PLANTILLA_I+ConstantesDatos.BARRA+ listaEtiquetasPlantillaI[j] +and2S,modulo);
                                 log.debug("Nombre de la clausula And 2 de la PlantillaI: "+ clausulaAnd2);
                                
                                 String campoAnd2=getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA       +  codProcedimiento +ConstantesDatos.PLANTILLA_I+ConstantesDatos.BARRA+ listaEtiquetasPlantillaI[j] +and2S+campoS,modulo);
                                 log.debug("Campo del and2: "+ campoAnd2);
                                
                                 String clausulaAnd3 = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA       +  codProcedimiento +ConstantesDatos.PLANTILLA_I+ConstantesDatos.BARRA+ listaEtiquetasPlantillaI[j] +and3S,modulo);
                                 log.debug("Nombre de la clausula And3 de la PlantillaI: "+ clausulaAnd3);
                                
                                 
                                //Por cada etiqueta tenemos que crear un EstructuraEtiquetaModuloIntegracionVO
                                EstructuraEtiquetaModuloIntegracionVO nuevaEtiqueta=new EstructuraEtiquetaModuloIntegracionVO();
                                nuevaEtiqueta.setCodigoEtiqueta(codigoEtiqueta);
                                nuevaEtiqueta.setNombreEtiqueta(nombreEtiqueta);
                                //El tipoEtiqueta es un propiedad obligatoria, nunca debería ser nula
                               //  pero si está mal definida en el fichero de configuración, y no hacemos
                                //la comprobación de distinto de null--> falla.
                                if(tipoEtiqueta!=null){
                                   nuevaEtiqueta.setTipoEtiqueta(Integer.parseInt(tipoEtiqueta));
                                }
                                nuevaEtiqueta.setFormatoFecha(formatoFecha);
                                nuevaEtiqueta.setFormatoNumero(formatoNumero);
                                nuevaEtiqueta.setSqlS(sqlEtiqueta);
                                nuevaEtiqueta.setNombreColumna(nombreColumnaEtiqueta);
                                nuevaEtiqueta.setAnd1(clausulaAnd1);
                                nuevaEtiqueta.setCampoAnd1(campoAnd1);
                                nuevaEtiqueta.setAnd2(clausulaAnd2);
                                nuevaEtiqueta.setCampoAnd2(campoAnd2);
                                nuevaEtiqueta.setAnd3(clausulaAnd3);
                                nuevaEtiqueta.setWhereS(clausulaWhere);
                                nuevaEtiqueta.setCampoWhere(campoWhere);
                                
                                etiquetasADevolverPlantillaI.add(nuevaEtiqueta);
                            }
                        }
                  }
                  else if ("S".equals(relacion)){
                        
                       
                        String etiquetasPlantillaR = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA +
                        codProcedimiento+  ConstantesDatos.PLANTILLA_R + etiquetaS,modulo);
                      

                        log.debug("Etiquetas para la plantillaR:"+etiquetasPlantillaR);
                        if(etiquetasPlantillaR!=null && !"".equals(etiquetasPlantillaR)){
                            String[] listaEtiquetasPlantillaR = etiquetasPlantillaR.split(ConstantesDatos.DOT_COMMA);
                            for(int j=0;j<listaEtiquetasPlantillaR.length;j++){
                                log.debug("Etiqueta de Plantilla R: "+listaEtiquetasPlantillaR[j]);

                                String nombreEtiqueta = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                +ConstantesDatos.BARRA           +  codProcedimiento +ConstantesDatos.PLANTILLA_R+ConstantesDatos.BARRA+ listaEtiquetasPlantillaR[j] +nombreS,modulo);
                                log.debug("Nombre Etiqueta de la Plantilla R: "+nombreEtiqueta);

                                 String tipoEtiqueta = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA          +  codProcedimiento +ConstantesDatos.PLANTILLA_R+ConstantesDatos.BARRA+ listaEtiquetasPlantillaR[j] +tipoS,modulo);
                                log.debug("TipoEtiqueta de la Plantilla R: "+tipoEtiqueta);
                             
                                String formatoFecha = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA  +  codProcedimiento +ConstantesDatos.PLANTILLA_R +ConstantesDatos.BARRA+ listaEtiquetasPlantillaR[j] +formatoFechaS,modulo);
                                log.debug("Formato Fecha de la etiqueta de la Plantilla R: "+formatoFecha);
                                
                                String formatoNumero = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA  +  codProcedimiento +ConstantesDatos.PLANTILLA_R +ConstantesDatos.BARRA+ listaEtiquetasPlantillaR[j] +formatoNumeroS,modulo);
                                log.debug("Formato Numero de la etiqueta de la Plantilla R: "+formatoNumero);
                                
                                
                                String codigoEtiqueta = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                  +ConstantesDatos.BARRA         +  codProcedimiento +ConstantesDatos.PLANTILLA_R+ ConstantesDatos.BARRA+ listaEtiquetasPlantillaR[j] +codigoS,modulo);
                                log.debug("Codigo Etiqueta de la Plantilla R: "+codigoEtiqueta);
                                
                                
                                String sqlEtiqueta = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                  +ConstantesDatos.BARRA         +  codProcedimiento +ConstantesDatos.PLANTILLA_R+ ConstantesDatos.BARRA+listaEtiquetasPlantillaR[j] +sqlS,modulo);
                                log.debug("Sql Etiqueta de la plantilla R "+sqlEtiqueta);
                                
                                
                                String nombreColumnaEtiqueta = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA       +  codProcedimiento +ConstantesDatos.PLANTILLA_R+ ConstantesDatos.BARRA+ listaEtiquetasPlantillaR[j] +nombreColumnaS,modulo);
                               
                                 log.debug("Nombre columna de la PlantillaR: "+ nombreColumnaEtiqueta);
                                
                                  String clausulaWhere = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA       +  codProcedimiento +ConstantesDatos.PLANTILLA_R+ConstantesDatos.BARRA+ listaEtiquetasPlantillaR[j] +whereS,modulo);
                                 log.debug("Nombre de la clausula Where de la PlantillaR: "+ clausulaWhere);
                                
                                 String campoWhere=getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA       +  codProcedimiento +ConstantesDatos.PLANTILLA_R+ConstantesDatos.BARRA+ listaEtiquetasPlantillaR[j] +whereS+campoS,modulo);
                                 log.debug("Campo del where de la Plantilla R: "+ campoWhere);
                                
                                 String clausulaAnd1 = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA       +  codProcedimiento +ConstantesDatos.PLANTILLA_R+ConstantesDatos.BARRA+ listaEtiquetasPlantillaR[j] +and1S,modulo);
                                 log.debug("Nombre de la clausula AND1 de la PlantillaR: "+ nombreColumnaEtiqueta);
                                
                                 String campoAnd1=getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA       +  codProcedimiento +ConstantesDatos.PLANTILLA_R+ConstantesDatos.BARRA+ listaEtiquetasPlantillaR[j] +and1S+campoS,modulo);
                                 log.debug("Campo del and1: "+ campoAnd1);
                                
                                 String clausulaAnd2 = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA       +  codProcedimiento +ConstantesDatos.PLANTILLA_R+ConstantesDatos.BARRA+ listaEtiquetasPlantillaR[j] +and2S,modulo);
                                 log.debug("Nombre de la clausula Where de la PlantillaR: "+ nombreColumnaEtiqueta);
                                
                                 String campoAnd2=getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA       +  codProcedimiento +ConstantesDatos.PLANTILLA_R+ConstantesDatos.BARRA+ listaEtiquetasPlantillaR[j] +and2S+campoS,modulo);
                                 log.debug("Campo del and2: "+ campoAnd2);
                                
                                 String clausulaAnd3 = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA       +  codProcedimiento +ConstantesDatos.PLANTILLA_R+ConstantesDatos.BARRA+ listaEtiquetasPlantillaR[j] +and3S,modulo);
                                 log.debug("Nombre de la clausula AND3 PlantillaR: "+ clausulaAnd3);
                                
                                 
                                  //Por cada etiqueta tenemos que crear un EstructuraEtiquetaModuloIntegracionVO
                                EstructuraEtiquetaModuloIntegracionVO nuevaEtiqueta=new EstructuraEtiquetaModuloIntegracionVO();
                                nuevaEtiqueta.setCodigoEtiqueta(codigoEtiqueta);
                                nuevaEtiqueta.setNombreEtiqueta(nombreEtiqueta);
                                //El tipoEtiqueta es un propiedad obligatoria, nunca debería ser nula
                               //  pero si está mal definida en el fichero de configuración, y no hacemos
                                //la comprobación de distinto de null--> falla.
                                if(tipoEtiqueta!=null){
                                   nuevaEtiqueta.setTipoEtiqueta(Integer.parseInt(tipoEtiqueta));
                                }
                                nuevaEtiqueta.setFormatoFecha(formatoFecha);
                                nuevaEtiqueta.setFormatoNumero(formatoNumero);
                                nuevaEtiqueta.setSqlS(sqlEtiqueta);
                                nuevaEtiqueta.setNombreColumna(nombreColumnaEtiqueta);
                                nuevaEtiqueta.setAnd1(clausulaAnd1);
                                nuevaEtiqueta.setCampoAnd1(campoAnd1);
                                nuevaEtiqueta.setAnd2(clausulaAnd2);
                                nuevaEtiqueta.setCampoAnd2(campoAnd2);
                                nuevaEtiqueta.setAnd3(clausulaAnd3);
                                nuevaEtiqueta.setWhereS(clausulaWhere);
                                nuevaEtiqueta.setCampoWhere(campoWhere);
                                log.debug("Etiqueta de plantilla R:"+nuevaEtiqueta.toString());
                                etiquetasADevolverPlantillaR.add(nuevaEtiqueta);
                            }
                        }
                  }else{ //Las plantillas son excluyentes, un documento pertenece a una y solo una plantilla
                  //if ("S".equals(relacion)){ //En principio estas las cargamos siempre
                        
                        String plantillaNS =ConstantesDatos.PLANTILLA_N; 
                        String etiquetasPlantillaN = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo + ConstantesDatos.BARRA +
                            codProcedimiento+ plantillaNS + etiquetaS,modulo);
                        
                        try{
                      
                        log.debug("Etiquetas para la plantillaN:"+etiquetasPlantillaN);
                        if(etiquetasPlantillaN!=null && !"".equals(etiquetasPlantillaN)){
                            String[] listaEtiquetasPlantillaN = etiquetasPlantillaN.split(ConstantesDatos.DOT_COMMA);
                            for(int j=0;j<listaEtiquetasPlantillaN.length;j++){
                                log.debug("Etiqueta de Plantilla N: "+listaEtiquetasPlantillaN[j]);

                                String nombreEtiqueta = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo+ 
                               ConstantesDatos.BARRA +  codProcedimiento +ConstantesDatos.PLANTILLA_N+ ConstantesDatos.BARRA +listaEtiquetasPlantillaN[j] +nombreS,modulo);
                                log.debug("Nombre Etiqueta de la plantilla N: "+nombreEtiqueta);

                                 String tipoEtiqueta = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo+ 
                                 ConstantesDatos.BARRA +  codProcedimiento +ConstantesDatos.PLANTILLA_N+ConstantesDatos.BARRA + listaEtiquetasPlantillaN[j] +tipoS,modulo);
                                log.debug("Tipo Etiqueta de la plantilla N: "+tipoEtiqueta);
                            
                                 String formatoFecha = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA  +  codProcedimiento +ConstantesDatos.PLANTILLA_N +ConstantesDatos.BARRA+ listaEtiquetasPlantillaN[j] +formatoFechaS,modulo);
                                log.debug("Formato Fecha de la etiqueta de la Plantilla N: "+formatoFecha);
                                
                                 String formatoNumero= getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA  +  codProcedimiento +ConstantesDatos.PLANTILLA_N +ConstantesDatos.BARRA+ listaEtiquetasPlantillaN[j] +formatoNumeroS,modulo);
                                log.debug("Formato Numero de la etiqueta de la Plantilla N: "+formatoNumero);
                                
                                String codigoEtiqueta = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo+ 
                                   ConstantesDatos.BARRA +  codProcedimiento +ConstantesDatos.PLANTILLA_N+ConstantesDatos.BARRA + listaEtiquetasPlantillaN[j]+codigoS,modulo);
                                log.debug("Codigo Etiqueta de la plantilla N: "+codigoEtiqueta);
                                
                                
                                String sqlEtiqueta = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo +
                                  ConstantesDatos.BARRA +  codProcedimiento +ConstantesDatos.PLANTILLA_N+ConstantesDatos.BARRA + listaEtiquetasPlantillaN[j] +sqlS,modulo);
                                log.debug("Sql Etiqueta de la plantilla N: "+sqlEtiqueta);
                                
                                
                                 String nombreColumnaEtiqueta = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA       +  codProcedimiento +ConstantesDatos.PLANTILLA_N+ ConstantesDatos.BARRA+ listaEtiquetasPlantillaN[j] +nombreColumnaS,modulo);
                                 log.debug("Nombre Columna Etiqueta de la plantilla N: "+nombreColumnaEtiqueta);
                                 
                                 String clausulaWhere = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA       +  codProcedimiento +ConstantesDatos.PLANTILLA_N+ConstantesDatos.BARRA+ listaEtiquetasPlantillaN[j] +whereS,modulo);
                                 log.debug("Nombre de la clausula Where de la Plantilla N: "+ clausulaWhere);
                                
                                 String campoWhere=getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA       +  codProcedimiento +ConstantesDatos.PLANTILLA_N+ConstantesDatos.BARRA+ listaEtiquetasPlantillaN[j] +whereS+campoS,modulo);
                                 log.debug("Campo del where: "+ campoWhere);
                                
                                 String clausulaAnd1 = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA       +  codProcedimiento +ConstantesDatos.PLANTILLA_N+ConstantesDatos.BARRA+ listaEtiquetasPlantillaN[j] +and1S,modulo);
                                 log.debug("Nombre de la clausula AND 1 de la PlantillaN: "+ clausulaAnd1);
                                
                                 String campoAnd1=getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA       +  codProcedimiento +ConstantesDatos.PLANTILLA_N+ConstantesDatos.BARRA+ listaEtiquetasPlantillaN[j] +and1S+campoS,modulo);
                                 log.debug("Campo del campoAnd1: "+ campoAnd1);
                                
                                 String clausulaAnd2 = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA       +  codProcedimiento +ConstantesDatos.PLANTILLA_N+ConstantesDatos.BARRA+ listaEtiquetasPlantillaN[j] +and2S,modulo);
                                 log.debug("Nombre de la clausula AND2 de la PlantillaN: "+ clausulaAnd2);
                                
                                 String campoAnd2=getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA       +  codProcedimiento +ConstantesDatos.PLANTILLA_N+ConstantesDatos.BARRA+ listaEtiquetasPlantillaN[j] +and2S+campoS,modulo);
                                 log.debug("Campo del campoAnd2: "+ campoAnd2);
                                
                                 String clausulaAnd3 = getValor(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION + modulo 
                                 +ConstantesDatos.BARRA       +  codProcedimiento +ConstantesDatos.PLANTILLA_N+ConstantesDatos.BARRA+ listaEtiquetasPlantillaN[j] +and3S,modulo);
                                 log.debug("Nombre de la clausulaAnd3: "+ clausulaAnd3);
                                 
                                 //Por cada etiqueta tenemos que crear un EstructuraEtiquetaModuloIntegracionVO
                                EstructuraEtiquetaModuloIntegracionVO nuevaEtiqueta=new EstructuraEtiquetaModuloIntegracionVO();
                                nuevaEtiqueta.setCodigoEtiqueta(codigoEtiqueta);
                                nuevaEtiqueta.setNombreEtiqueta(nombreEtiqueta);
                                //El tipoEtiqueta es un propiedad obligatoria, nunca debería ser nula
                               //  pero si está mal definida en el fichero de configuración, y no hacemos
                                //la comprobación de distinto de null--> falla.
                                if(tipoEtiqueta!=null){
                                   nuevaEtiqueta.setTipoEtiqueta(Integer.parseInt(tipoEtiqueta));
                                }
                                nuevaEtiqueta.setFormatoFecha(formatoFecha);
                                nuevaEtiqueta.setFormatoNumero(formatoNumero);
                                nuevaEtiqueta.setSqlS(sqlEtiqueta);
                                nuevaEtiqueta.setNombreColumna(nombreColumnaEtiqueta);
                                nuevaEtiqueta.setAnd1(clausulaAnd1);
                                nuevaEtiqueta.setCampoAnd1(campoAnd1);
                                nuevaEtiqueta.setAnd2(clausulaAnd2);
                                nuevaEtiqueta.setCampoAnd2(campoAnd2);
                                nuevaEtiqueta.setAnd3(clausulaAnd3);
                                nuevaEtiqueta.setWhereS(clausulaWhere);
                                nuevaEtiqueta.setCampoWhere(campoWhere);
                                
                                etiquetasADevolverPlantillaN.add(nuevaEtiqueta);
                            }
                        }
                        
                        }catch(Exception e){
                            log.error(" ****************** ERROR EN MODULOINTEGRACIONEXTERNOFACTORIA: " + e.getMessage());
                            e.printStackTrace();
                        }
                  }
                  
                   ModuloIntegracionExterno implClass = this.getImplClass(codOrganizacion, modulo);
                   ArrayList<EstructuraEtiquetaModuloIntegracionVO> etiquetasModulo= new ArrayList<EstructuraEtiquetaModuloIntegracionVO>();
                   etiquetasModulo.addAll(etiquetasADevolverPlantillaI);
                   etiquetasModulo.addAll(etiquetasADevolverPlantillaR);
                   etiquetasModulo.addAll(etiquetasADevolverPlantillaN);
		   if(implClass!=null ){
                        implClass.setNombreModulo(modulo);
                        implClass.setDescripcionModulo(descripcion);
                        implClass.setEtiquetas(etiquetasModulo);
                        resultado.add(implClass);
                   }// if implClass
                
                   
                   
                   
                    
                }

            }// for

        }catch(Exception e){
            log.error(" **************************** ERROR EN ModuloIntegracionExternoFactoria.getImplClassModuloConEtiquetas(): " + e.getMessage());
            e.printStackTrace();
            resultado = new ArrayList<ModuloIntegracionExterno>();
        }
    
        log.debug(" =====================> resultado a devolver:: " + resultado.size());
           
     return resultado;
 }
         
public boolean verificarSiExisteMetodo(Object objeto, String nombreMetodo, Class[] tipoParametros) {
        boolean existeMetodo = false;

        try {
            Class clase = Class.forName(objeto.getClass().getName());
            clase.newInstance();

            Method metodo = clase.getMethod(nombreMetodo, tipoParametros);
            
            existeMetodo = true;
        } catch (NoSuchMethodException e) {
            existeMetodo = false;
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        
        return existeMetodo;
    }
 
         
 
}