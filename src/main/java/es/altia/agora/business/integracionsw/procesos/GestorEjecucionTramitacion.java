package es.altia.agora.business.integracionsw.procesos;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.text.NumberFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.altia.agora.business.gestionInformes.persistence.CampoValueObject;
import es.altia.agora.business.integracionsw.AvanzarRetrocederSWVO;
import es.altia.agora.business.integracionsw.DefinicionParametroEntradaVO;
import es.altia.agora.business.integracionsw.DefinicionParametroSalidaVO;
import es.altia.agora.business.integracionsw.InfoConfTramSWVO;
import es.altia.agora.business.integracionsw.InfoServicioWebVO;
import es.altia.agora.business.integracionsw.ParametroConfigurableVO;
import es.altia.agora.business.integracionsw.PeticionSWVO;
import es.altia.agora.business.integracionsw.TipoServicioWebVO;
import es.altia.agora.business.integracionsw.exception.EjecucionSWException;
import es.altia.agora.business.integracionsw.exception.FaltaDatoObligatorioException;
import es.altia.agora.business.integracionsw.exception.NoServicioWebDefinidoException;
import es.altia.agora.business.integracionsw.persistence.DefinicionOperacionesSWManager;
import es.altia.agora.business.integracionsw.persistence.DefinicionSWTramitacionManager;
import es.altia.agora.business.integracionsw.persistence.MantenimientoSWManager;
import es.altia.agora.business.integracionsw.persistence.RecolectorDatosManager;
import es.altia.agora.business.integracionsw.persistence.ReconstruccionSWManager;
import es.altia.agora.business.integracionsw.persistence.manual.DefinicionSWTramitacionDAO;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.persistence.DatosSuplementariosManager;
import es.altia.agora.business.sge.persistence.manual.DatosSuplementariosDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.technical.EstructuraCampo;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.exception.EjecucionOperacionModuloIntegracionException;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.manual.ModuloIntegracionExternoDAO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.TareasPendientesInicioTramiteVO;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import org.apache.axis.AxisFault;

import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoParamAdicionales;
import java.util.Map;

public class GestorEjecucionTramitacion {

   
	 protected static Log m_Log = LogFactory.getLog(GestorEjecucionTramitacion.class.getName());


    public void ejecutarSWTramitacion(PeticionSWVO peticionSW, AdaptadorSQLBD oad, Connection con) throws EjecucionSWException, NoServicioWebDefinidoException, FaltaDatoObligatorioException, EjecucionOperacionModuloIntegracionException {
    	
    	m_Log.debug("Inicio ejecutarSWTramitacion-------------------------------------------");
    	DefinicionSWTramitacionManager defTramManager = DefinicionSWTramitacionManager.getInstance();
    	try {
            int tipoAvz = 0;
            if(peticionSW.isAvanzar())
                tipoAvz = 1;
            else
            if(peticionSW.isRetroceder())
                tipoAvz = 0;
            else
            if(peticionSW.isIniciar())
                tipoAvz = 2;

            /** original 
            Vector listaSW = defTramManager.getListaConfSW(peticionSW.getCodMunicipio(),
                    	peticionSW.getCodProcedimiento(), peticionSW.getCodTramite(), tipoAvz, peticionSW.getParams());
                        **/            
            
            
             Vector listaSW = defTramManager.getListaConfSW(peticionSW.getCodMunicipio(),
                    	peticionSW.getCodProcedimiento(), peticionSW.getCodTramite(), tipoAvz,peticionSW.getOcurrencia(),peticionSW.getNumExpediente(),peticionSW.getEjercicio(), peticionSW.isOcurrenciaCerrada(),peticionSW.getParams());
            
			int i =0;
			for (Iterator it = listaSW.iterator(); it.hasNext();) {
				AvanzarRetrocederSWVO avRetSW= (AvanzarRetrocederSWVO) it.next();
				
				try {
					ejecutar1SW(peticionSW, defTramManager, avRetSW, oad, con);
				} catch (EjecucionSWException e) {
					Vector listaReves = reves(listaSW,i);
					m_Log.debug("La lista al reves es: " + listaReves);		
                    peticionSW.setRetroceder(true);
                    peticionSW.setAvanzar(false);
                    peticionSW.setIniciar(false);
                    
					for (Iterator it2 = listaReves.iterator();it2.hasNext();) {
						AvanzarRetrocederSWVO avRetSWRet= (AvanzarRetrocederSWVO) it2.next();
						ejecutar1SW(peticionSW, defTramManager, avRetSWRet, oad, con);
					}
					throw e;
				}
				i++;
			}

		} catch (AnotacionRegistroException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		m_Log.debug("Fin ejecutarSWTramitacion----------------------------------------");
    }

    
    private int contarNumeroTareasInicio(Vector listaSW){
        int num =0;
        for(int i=0;listaSW!=null && i<listaSW.size();i++){
            
            AvanzarRetrocederSWVO dato = (AvanzarRetrocederSWVO)listaSW.get(i);
            if(dato.getCfoIniciar()!=-1 && dato.getCodIniciar()!=-1)
                num++;
        }
        return num;
    }


    /**
     * Ejecuta sólo las tareas de inicio de un trámite. Si falla alguna se guarda en la lista de tareas pendientes y las que venga a continuación también, aunque
     * todavía no hayan sido ejecutadas
     * @param peticionSW: Petición con los datos del trámite y expediente al que pertenece para el que se ejecutarán las operaciones de inicio
     * @param oad: AdaptadorSQLBD
     * @param con: conexión a la BD
     * @throws es.altia.agora.business.integracionsw.exception.EjecucionSWException
     * @throws es.altia.agora.business.integracionsw.exception.NoServicioWebDefinidoException
     * @throws es.altia.agora.business.integracionsw.exception.FaltaDatoObligatorioException
     * @throws es.altia.flexia.integracion.moduloexterno.plugin.exception.EjecucionOperacionModuloIntegracionException
     */
     public void ejecutarSWTramitacionOperacionesInicio(PeticionSWVO peticionSW, AdaptadorSQLBD oad, Connection con) throws EjecucionSWException, NoServicioWebDefinidoException, FaltaDatoObligatorioException, EjecucionOperacionModuloIntegracionException {

        m_Log.debug("");
    	m_Log.debug("Inicio ejecutarSWTramitacionTareasInicio -------------------------------------------");
    	DefinicionSWTramitacionManager defTramManager = DefinicionSWTramitacionManager.getInstance();
        AvanzarRetrocederSWVO avRetSW = null;
        
    	try {
           
            Vector listaSW = defTramManager.getListaConfSW(peticionSW.getCodMunicipio(),
                    	peticionSW.getCodProcedimiento(), peticionSW.getCodTramite(), peticionSW.getParams());
                       
            long cfoIniciarActual = -1;
            int codIniciarActual  = -1;

            int contador = 0;
            
            
		for (Iterator it = listaSW.iterator(); it.hasNext();) {
				avRetSW= (AvanzarRetrocederSWVO) it.next();
                cfoIniciarActual = avRetSW.getCfoIniciar();
                codIniciarActual = avRetSW.getCodIniciar();
                
                try{
                    /** prueba **/
                    contador++;
                    /** prueba **/
                    if(cfoIniciarActual!=-1 && codIniciarActual!=-1){
                        ejecutar1SW(peticionSW, defTramManager, avRetSW, oad, con);
                        //contador++;
                    }

                }catch (EjecucionSWException eswe) {
                    m_Log.error("******************** EjecucionSWException error en ejecución de operación de web service: " + eswe.getMensaje() );
                    String nombreMensaje = null;
                    int errorPersonalizado = 0;
  
                    if(eswe.getTraza() instanceof AxisFault){
                        // HA FALLADO LA LLAMADA AL SERVICIO
                        nombreMensaje = "msgErrorWSLlamadaServicio1;@OPERACION;msgErrorWSLlamadaServicio2;@OBJETO;msgErrorWSLlamadaServicio3";
                    }else
                    if(eswe.getTraza() instanceof MalformedURLException){
                        // LA URL DE ACCESO AL SERVICIO WEB NO ES CORRECTO
                        nombreMensaje = "msgErrorWSLlamadaServicio1;@OPERACION;msgErrorWSLlamadaServicio2;@OBJETO;msgErrorWSURLMalFormada3";
                    }else
                    if(eswe.getTraza() instanceof RemoteException){
                        // HA OCURRIDO UN ERROR MIENTRAS SE ANALIZABAN LOS DATOS DE ENTRADA
                        nombreMensaje = "msgErrorWSLlamadaServicio1;@OPERACION;msgErrorWSLlamadaServicio2;@OBJETO;msgErrorWSDatosEntrada";
                    }else
                    if(eswe.getTraza() instanceof InternalErrorException){
                        // HA OCURRIDO UN ERROR MIENTRAS SE ANALIZABAN LOS PARÁMETROS DE ENTRADA
                        nombreMensaje = "msgErrorWSLlamadaServicio1;@OPERACION;msgErrorWSLlamadaServicio2;@OBJETO;msgErrorWSParamEntrada";
                    }

                    // Si la operación se ejecuta al iniciar el trámite y ha fallado, se guarda como una tarea pendiente de inicio.
                    // Si se ha ejecutado una tarea pendiente de inicio y se produce un fallo, no se vuelve a almacenar en base de datos porque daría error
                    ModuloIntegracionExternoDAO.getInstance().insertarTareaPendienteInicio(peticionSW.getCodMunicipio(),peticionSW.getCodTramite(), peticionSW.getOcurrencia(), peticionSW.getNumExpediente(), avRetSW.getCfoIniciar(),errorPersonalizado,nombreMensaje, con);
                    
                    break;
                }catch(NoServicioWebDefinidoException f){
                    String nombreMensaje = "msgErrorWSNoDefinido";
                    int errorPersonalizado = 0;

                    // Si la operación se ejecuta al iniciar el trámite y ha fallado, se guarda como una tarea pendiente de inicio
                    // Si se ha ejecutado una tarea pendiente de inicio y se produce un fallo, no se vuelve a almacenar en base de datos porque daría error
                    ModuloIntegracionExternoDAO.getInstance().insertarTareaPendienteInicio(peticionSW.getCodMunicipio(),peticionSW.getCodTramite(), peticionSW.getOcurrencia(), peticionSW.getNumExpediente(), avRetSW.getCfoIniciar(),errorPersonalizado,nombreMensaje, con);
                    break;
                }catch(FaltaDatoObligatorioException g){

                    int errorPersonalizado = 0;
                    String nombreMensaje = "msgErrorWSFaltaCampoTramite1;" + g.getNombreCampo() + ";msgErrorWSFaltaCampoTramite2;@OPERACION;msgErrorWSFaltaCampoTramite3;@OBJETO";

                    m_Log.error("******************** FaltaDatoObligatorioException " + g.getMessage() + ", nombreCampo: " + g.getNombreCampo());
                    // Si la operación se ejecuta al iniciar el trámite y ha fallado, se guarda como una tarea pendiente de inicio
                    // Si se ha ejecutado una tarea pendiente de inicio y se produce un fallo, no se vuelve a almacenar en base de datos porque daría error
                    ModuloIntegracionExternoDAO.getInstance().insertarTareaPendienteInicio(peticionSW.getCodMunicipio(),peticionSW.getCodTramite(), peticionSW.getOcurrencia(), peticionSW.getNumExpediente(), avRetSW.getCfoIniciar(),errorPersonalizado,nombreMensaje,con);

                    break;
                }catch(EjecucionOperacionModuloIntegracionException h){
                    String nombreMensaje = null;
                    int errorPersonalizado = 0;
                    
                    if(h.getNombreModulo()!=null && !"".equals(h.getCodigoErrorOperacion()) && h.getCodigoErrorOperacion()!=null && !"".equals(h.getCodigoErrorOperacion())){
                       
                        try{
                            ResourceBundle  resource = ResourceBundle.getBundle(h.getNombreModulo());
                            if(resource!=null){
                                // Se obtiene el nombre del mensaje de error correspondiente a la operación del módulo que ha fallado
                                nombreMensaje = resource.getString(peticionSW.getCodMunicipio() + ConstantesDatos.MODULO_INTEGRACION + h.getOperacion() + ConstantesDatos.MENSAJE_ERROR_MODULO_EXTERNO + h.getCodigoErrorOperacion());
                                errorPersonalizado = 1;
                            }
                        }catch(Exception ex){
                            m_Log.error("Error al leer las propiedades de gestión de errores personalizadas del módulo: " + h.getNombreModulo() + " para la operación "
                                    + h.getOperacion() + ". Se muestra un mensaje de error genérico.");

                           nombreMensaje = "msgFalloOpModuloIntegracion1;" + h.getOperacion() + ";msgFalloOpModuloIntegracion2;" + h.getNombreModulo();
                           errorPersonalizado = 0;

                        }// catch
                    }// if
                    
                    m_Log.error("******************** EjecucionOperacionModuloIntegracionException falla la operación: " + h.getOperacion() + " del módulo: " + h.getNombreModulo());
                    // Si la operación se ejecuta al iniciar el trámite y ha fallado, se guarda como una tarea pendiente de inicio.
                    // Si se ha ejecutado una tarea pendiente de inicio y se produce un fallo, no se vuelve a almacenar en base de datos porque daría error
                    ModuloIntegracionExternoDAO.getInstance().insertarTareaPendienteInicio(peticionSW.getCodMunicipio(),peticionSW.getCodTramite(), peticionSW.getOcurrencia(), peticionSW.getNumExpediente(), avRetSW.getCfoIniciar(),errorPersonalizado,nombreMensaje,con);
                    break;
               }                
			}// for

            /*
            // EL RESTO DE TAREAS DE INICIO QUE HUBIESE A CONTINUACIÓN DE LA QUE HA FALLADO YA NO SE EJECUTAN, SE MARCAN COMO TAREAS PENDIENTES DE INICIO
            // PARA LA OCURRENCIA DEL TRÁMITE EN CUESTIÓN.
             */
            int i=1;
            avRetSW = null;
			for (Iterator it = listaSW.iterator(); it.hasNext();) {
                avRetSW= (AvanzarRetrocederSWVO) it.next();
                if(avRetSW.getCodIniciar()!=-1 && avRetSW.getCfoIniciar()!=-1){
                    if(i>contador){                    
                    //if(avRetSW.getCodIniciar()!=-1 && avRetSW.getCfoIniciar()!=-1){

                        // SE OBTIENE INFORMACIÓN DE LA OPERACIONES DE INICIO QUE NO SE VAN A PODER EJECUTAR
                        TareasPendientesInicioTramiteVO tarea = ModuloIntegracionExternoDAO.getInstance().getInfoOperacion(peticionSW.getCodMunicipio(),peticionSW.getCodTramite(),avRetSW.getCfoIniciar(), con);
                        String mensaje = null;
                        if(tarea!=null && tarea.isOperacionModulo())
                            mensaje = "msgErrorModIniciarTramite1;@OPERACION;msgErrorModIniciarTramite2;@OBJETO;msgErrorModIniciarTramite3";
                        else
                        if(tarea!=null && !tarea.isOperacionModulo())
                            mensaje = "msgErrorWSIniciarTramite1;@OPERACION;msgErrorWSIniciarTramite2;@OBJETO;msgErrorWSIniciarTramite3";

                        // SE GUARDA COMO TAREA DE INICIO LAS OPERACIONES QUE VIENE A CONTINUACIÓN DE LA QUE HA FALLADO, YA QUE TAMBIÉN ESTARÁN PENDIENTES
                        ModuloIntegracionExternoDAO.getInstance().insertarTareaPendienteInicio(peticionSW.getCodMunicipio(),peticionSW.getCodTramite(), peticionSW.getOcurrencia(), peticionSW.getNumExpediente(), avRetSW.getCfoIniciar(),0,mensaje,con);

                    }// if
                }// if
                i++;
                
            }// for
            
		} catch (AnotacionRegistroException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		m_Log.debug("Fin ejecutarSWTramitacionTareasInicio----------------------------------------");
        m_Log.debug("");
    }

  

     /**
      * Recupera el nombre de una operación de base de datos
      * @param avRetSW: Instancia de AvanzarRetrocederSWVO con los datos de la operación a a ejecutar
      * @param tipo: Toma los valores
      *         0 -> Retroceder
      *         1 -> Avanzar
      *         2 -> Iniciar
      * @param codTramite: Código del trámite al que está asociada la operación
      * @param codMunicipio: Código del municipio/organización
      * @param con: Conexión a la bd
      * @return Nombre operación
      */
     private String getNombreOperacion(AvanzarRetrocederSWVO avRetSW,int tipo,int codTramite,int codMunicipio,Connection con){
        String titulo ="";

        InfoConfTramSWVO info = null;
        if(avRetSW.getTipoOperacion()!=null && "MODULO".equals(avRetSW.getTipoOperacion())){
            try{
                long cfo = -1;
                if(tipo==0)
                    cfo = avRetSW.getCfoRetroceder();
                else
                if(tipo==1)
                    cfo = avRetSW.getCfoAvanzar();
                else
                    cfo = avRetSW.getCfoIniciar();

                info = DefinicionSWTramitacionDAO.getInstance().getInfoOperacionModulo(cfo, con);
                titulo = info.getTituloOperacion();
                m_Log.debug(" ====> titulo de la operación del módulo todavía pendiente: " + titulo);
            }catch(TechnicalException e){
                m_Log.error("TechnicalException : " + e.getMessage());
            }catch(InternalErrorException e){
                m_Log.error("InternalErrorException : " + e.getMessage());
            }

        }else
        if(avRetSW.getTipoOperacion()!=null && "WS".equals(avRetSW.getTipoOperacion())){
           /* try{ */
            long cfo = -1;
            if(tipo==0)
                cfo = avRetSW.getCfoRetroceder();
            else
            if(tipo==1)
                cfo = avRetSW.getCfoAvanzar();
            else
                cfo = avRetSW.getCfoIniciar();
           
            titulo = DefinicionSWTramitacionDAO.getInstance().getTituloOperacionWS(codMunicipio,codTramite,cfo,con);
            m_Log.debug(" ====> titulo de la operación del WS todavía pendiente: " + titulo);

        }

        return titulo;
     }
     


     /**
     * Ejecuta las tareas pendientes de inicio que tiene una ocurrencia de trámite de un expediente
     * @param peticionSW: Petición con los datos del trámite y expediente al que pertenece para el que se ejecutarán las operaciones de inicio
     * @param oad: AdaptadorSQLBD
     * @param con: conexión a la BD
     * @throws es.altia.agora.business.integracionsw.exception.EjecucionSWException
     * @throws es.altia.agora.business.integracionsw.exception.NoServicioWebDefinidoException
     * @throws es.altia.agora.business.integracionsw.exception.FaltaDatoObligatorioException
     * @throws es.altia.flexia.integracion.moduloexterno.plugin.exception.EjecucionOperacionModuloIntegracionException
     * @return "OK" si se han ejecutado todas las tareas pendientes y un mensaje con las operaciones que no se han podido
     * ejecutar correctamente
     */
     //public String  ejecutarSWTramitacionTareasPendientesInicio(PeticionSWVO peticionSW, AdaptadorSQLBD oad, Connection con) throws EjecucionSWException, NoServicioWebDefinidoException, FaltaDatoObligatorioException, EjecucionOperacionModuloIntegracionException {
     public String  ejecutarSWTramitacionTareasPendientesInicio(PeticionSWVO peticionSW, AdaptadorSQLBD oad, Connection con){
        String salida = "";
        m_Log.debug("");
    	m_Log.debug("Inicio ejecutarSWTramitacionTareasPendientesInicio -------------------------------------------");
    	DefinicionSWTramitacionManager defTramManager = DefinicionSWTramitacionManager.getInstance();
        AvanzarRetrocederSWVO avRetSW = null;
    	try {
            
            Vector listaSW = defTramManager.getListaConfSWTareasPendientes(peticionSW.getCodMunicipio(),
                          peticionSW.getCodProcedimiento(), peticionSW.getCodTramite(),peticionSW.getOcurrencia(),peticionSW.getNumExpediente(),peticionSW.getParams());

            int contador = 0;
            boolean eliminada = true;
            int tareasPendientes = listaSW.size();
			for (Iterator it = listaSW.iterator(); it.hasNext();) {
				avRetSW= (AvanzarRetrocederSWVO) it.next();

                try{
                    ejecutar1SW(peticionSW, defTramManager, avRetSW, oad, con);
                    contador++;

                    // SI LA EJECUCIÓN DE LA TAREA PENDIENTE HA SIDO CORRECTA, SE ELIMINA DE LA BASE DE DATOS
                    /**************** SI EL RESULTADO ES CORRECTO Y ES UNA OPERACIÓN DE UN SERVICIO WEB QUE HA QUEDADO PENDIENTE ***************************/
                    if(peticionSW.isIniciar()){
                        // Se ha ejecutado una tarea de inicio pendiente de un web service para el trámite, entonces se borra la tarea
                        // de la base de datos
                        //ModuloIntegracionExternoDAO.getInstance().eliminarTareasPendienteInicio(peticionSW.getCodMunicipio(),peticionSW.getCodTramite(),peticionSW.getOcurrencia(),peticionSW.getNumExpediente(), con);
                        eliminada = ModuloIntegracionExternoDAO.getInstance().eliminarTareasPendienteInicio(avRetSW.getIdTareaPendiente(), con);
                        if(!eliminada){
                            break;
                        }
                    }//if
                    
                }catch (EjecucionSWException eswe) {
                    // Si la operación se ejecuta al iniciar el trámite y ha fallado, se guarda como una tarea pendiente de inicio.
                    // Si se ha ejecutado una tarea pendiente de inicio y se produce un fallo, no se vuelve a almacenar en base de datos porque daría error                    
                    //throw eswe;
                    break;
                }catch(NoServicioWebDefinidoException f){
                    // Si la operación se ejecuta al iniciar el trámite y ha fallado, se guarda como una tarea pendiente de inicio
                    // Si se ha ejecutado una tarea pendiente de inicio y se produce un fallo, no se vuelve a almacenar en base de datos porque daría error
                   
                    //throw e;
                    break;
                }catch(FaltaDatoObligatorioException g){
                    // Si la operación se ejecuta al iniciar el trámite y ha fallado, se guarda como una tarea pendiente de inicio
                    // Si se ha ejecutado una tarea pendiente de inicio y se produce un fallo, no se vuelve a almacenar en base de datos porque daría error
                   
                    //throw e;
                    break;
                }catch(EjecucionOperacionModuloIntegracionException h){
                    // Si la operación se ejecuta al iniciar el trámite y ha fallado, se guarda como una tarea pendiente de inicio.
                    // Si se ha ejecutado una tarea pendiente de inicio y se produce un fallo, no se vuelve a almacenar en base de datos porque daría error
                   
                    //throw e;
                    break;
                }
			}// for

            m_Log.debug(" ejecutarSWTramitacionTareasPendientesInicio la tarea de inicio que ha fallado cfoIniciar : " + avRetSW.getCfoIniciar() +
                    " y codOperacion:" + avRetSW.getCodIniciar() + " con el valor de contador: " + contador);

           if(contador==tareasPendientes){
                salida = "";
           }else{
                // SE COMPRUEBA CUAL ES LA ÚLTIMA OPERACIÓN QUE SE HA EJECUTADO Y QUE HA FALLADO PARA RECUPERAR
                // EL NOMBRE DE LA OPERACIÓN
                StringBuffer msg = new StringBuffer();
                
                String tituloOpActual = this.getNombreOperacion(avRetSW, 2,peticionSW.getCodTramite(),peticionSW.getCodMunicipio(), con);
                if(tituloOpActual!=null && !"".equals(tituloOpActual)){
                    msg.append(tituloOpActual);
                    msg.append(ConstantesDatos.COMMA_SIMPLE);
                }

                // SE GENERA UN MENSAJE CON LAS OPERACIONES DE INICIO QUE TODAVÍA ESTÁN PENDIENTES DE EJECUTAR
                // A CONTINUACIÓN DE LA QUE NO SE HA EJECUTADO CORRECTAMENTE
                int i=0;
                avRetSW = null;
                
                for (Iterator it = listaSW.iterator(); it.hasNext();) {
                    avRetSW= (AvanzarRetrocederSWVO) it.next();
                    String titulo = null;
                    if(i>contador){
                        // Se recupera el nombre de la operación de iniciar                        
                        titulo = this.getNombreOperacion(avRetSW, 2,peticionSW.getCodTramite(),peticionSW.getCodMunicipio(),con);
                        if(titulo!=null && !"".equals(titulo)){
                            msg.append(titulo);
                            msg.append(ConstantesDatos.COMMA_SIMPLE);
                        }
                   } // if
                   i++;
                }// for

                if(msg!=null && msg.length()>0){
                    salida = msg.toString();
                    // PARA ELIMINAR LA ÚLTIMA COMA
                    if(salida.endsWith(ConstantesDatos.COMMA_SIMPLE))
                        salida = salida.substring(0, salida.length()-1);
                }
              
           }// else

		} catch (AnotacionRegistroException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        m_Log.debug("Fin ejecutarSWTramitacionTareasPendientesInicio----------------------------------------");
        m_Log.debug("");
        return salida;

    }


    private Vector reves (Vector lista, int i) {
    	Vector listaRev = new Vector();
    	for(int j = (i-1); j>=0;j--) {
    		listaRev.addElement(lista.get(j));
    	}
    	return listaRev;
    }

    private void ejecutar1SW(PeticionSWVO peticionSW, DefinicionSWTramitacionManager defTramManager, AvanzarRetrocederSWVO avRetSW, AdaptadorSQLBD oad, Connection con)
    	throws EjecucionSWException, NoServicioWebDefinidoException,FaltaDatoObligatorioException, EjecucionOperacionModuloIntegracionException {
    	m_Log.debug("--------------------------------------------------------------");
    	m_Log.debug("Inicio ejecutar1SW-----------------------------------------------");
    	m_Log.debug("--------------------------------------------------------------");
        
        DefinicionOperacionesSWManager defOpsManager = DefinicionOperacionesSWManager.getInstance();
        MantenimientoSWManager mantManager = MantenimientoSWManager.getInstance();
        ReconstruccionSWManager reconManager = ReconstruccionSWManager.getInstance();

        InfoConfTramSWVO infoSWActual = null;
        try {
            // Primero, recuperamos los datos de la operacion que va a ser lanzada (codigo de operacion y si es
            // obligatoria u opcional.
            InfoConfTramSWVO[] infoSWTramite = defTramManager.getInfoSWTramite(avRetSW,peticionSW.getParams());

            if (peticionSW.isAvanzar())
                infoSWActual = infoSWTramite[0];
            else
            if(peticionSW.isRetroceder())
                infoSWActual = infoSWTramite[1];
            else
                infoSWActual = infoSWTramite[2];

            if (infoSWActual == null) throw new NoServicioWebDefinidoException();

            if(!infoSWActual.isOperacionModulo())
            {
                // LA OPERACIÓN CONFIGURADA ES DE UN SERVICIO WEB
                
                // Segundo, recuperamos todos los posibles parametros definidos para la operacion seleccionada.
                Collection<DefinicionParametroEntradaVO> parametrosEntrada =
                        defOpsManager.getParamsDefEntrada(infoSWActual.getCodOpSW(), peticionSW.getParams());
                Collection parametrosSalida =
                        defOpsManager.getParamsDefSalida(infoSWActual.getCodOpSW(), peticionSW.getParams());

                // Completamos los datos de entrada con los datos definidos como constantes en la definicion
                // del procedimiento.
                completarConDatosProcedimiento(parametrosEntrada, infoSWActual.getListaParamsEntrada());

                // Recupero todos los posibles campos de un procedimiento y los añado a un HashMap indexado por el nombre
                // del campo.
                HashMap<String, CampoValueObject> mapaCamposProc = recuperoDatosProc(peticionSW.getCodMunicipio(),
                        peticionSW.getCodProcedimiento(), peticionSW.getParams());
                completarConDatosExpediente(parametrosEntrada, infoSWActual.getListaParamsEntrada(), peticionSW, mapaCamposProc);
                
                completarConOtrosDatos(parametrosEntrada, infoSWActual.getListaParamsEntrada(), peticionSW);

                // Ya se han recuperado todos los datos del expediente.
                // Reconstruimos ahora la llamada al Servicio Web.
                GeneralValueObject gVO = defOpsManager.getInfoGeneralOperacion(infoSWActual.getCodOpSW(), peticionSW.getParams());
                int codigoSW = Integer.parseInt((String)gVO.getAtributo("codigoSW"));
                String nombreOp = (String)gVO.getAtributo("nombreOpSW");
                InfoServicioWebVO infoSW = mantManager.getInfoGeneralSWByCodigo(codigoSW, peticionSW.getParams());

                Collection oneOperation = new ArrayList();
                oneOperation.add(reconManager.reconstruirOperacionVO(infoSWActual.getCodOpSW(), peticionSW.getParams()));
                infoSW.setOperacionesSW(oneOperation);

                HashMap paramsSW = processCadenaParams(parametrosEntrada);
 
                PreparacionOperacionSW preparacion = new PreparacionOperacionSW(infoSW);
                infoSW = preparacion.rellenarDatosParametros(paramsSW, nombreOp);
                m_Log.info("(No Error. Log Control: Inicio llamada WS "+nombreOp+ " expediente: "+peticionSW.getNumExpediente());
                LanzadorServicioWeb launcher = new LanzadorServicioWeb(infoSW);
                TipoServicioWebVO tipoRetorno = launcher.llamarServicioWeb(nombreOp, peticionSW.getParams());
                m_Log.info("(No Error. Log Control: Fin llamada WS "+nombreOp+ " expediente: "+peticionSW.getNumExpediente());
                
                Vector resultados = preparacion.recuperarDatosSalida(tipoRetorno, new Vector(parametrosSalida));

                boolean resultadoCorrecto = fueResultadoCorrecto(parametrosSalida, resultados);
                m_Log.debug("El resultado del Servicio Web fue: " + resultadoCorrecto);

 
                if (!resultadoCorrecto) {
                    String causaError = getErrores(parametrosSalida, resultados);
                    throw new EjecucionSWException(new Exception(), "EL SERVICIO WEB DEVUELVE UN ERROR: " + causaError, infoSWActual.isObligatorio());
                }

                // Guardamos los datos en los expedientes.
                guardarDatosExpediente(parametrosSalida, resultados, infoSWActual.getListaParamsSalida(), peticionSW, mapaCamposProc,oad, con);
            }else{

                ModuloIntegracionExternoFactoria factoria = ModuloIntegracionExternoFactoria.getInstance();
                ModuloIntegracionExterno modulo = factoria.getImplClass(peticionSW.getCodMunicipio(), infoSWActual.getNombreModulo());                
                boolean correcto = true;
                Object salidaOperacion = null;
                if(modulo!=null){
                   // Parametros de metodo antiguo. El nuevo tiene un nuevo parametro para no tener que cambiar la firma	
                    // Cada vez que se quiere anadir uno nuevo	
                    Class[] tipoParametros = new Class[] {int.class, int.class, int.class, String.class};	
                    Object[] valoresParametros = null;		
                    // Verificamos si es necesario llamar al metodo antiguo o al nuevo	
                    if (factoria.verificarSiExisteMetodo(modulo, infoSWActual.getTituloOperacion(), tipoParametros)) {	
                        valoresParametros = new Object[] {peticionSW.getCodMunicipio(),peticionSW.getCodTramite(),peticionSW.getOcurrencia(),peticionSW.getNumExpediente()};	
                        	
                        if (m_Log.isDebugEnabled()) {	
                            m_Log.debug(String.format("Llmada a metodo con firma antigua: %s(int, int, int, String)",	
                                    infoSWActual.getTituloOperacion()));	
                        }	
                    } else {	
                        ModuloIntegracionExternoParamAdicionales paramAdicionales = new ModuloIntegracionExternoParamAdicionales();	
                        paramAdicionales.setOrigenLlamada(peticionSW.getOrigenLlamada());	
                        tipoParametros      = new Class[] {int.class, int.class, int.class, String.class, ModuloIntegracionExternoParamAdicionales.class};	
                        valoresParametros  = new Object[] {peticionSW.getCodMunicipio(), peticionSW.getCodTramite(), peticionSW.getOcurrencia(), peticionSW.getNumExpediente(), paramAdicionales};	
                        	
                        if (m_Log.isDebugEnabled()) {	
                            m_Log.debug(String.format("Llmada a metodo con firma nueva: %s(int, int, int, String, ModuloIntegracionExternoParamAdicionales)",	
                                    infoSWActual.getTituloOperacion()));	
                        }	
                    }

                    salidaOperacion = factoria.ejecutarMetodo(modulo,infoSWActual.getTituloOperacion(), tipoParametros, valoresParametros);
                    if((salidaOperacion instanceof String) && (!"0".equals(salidaOperacion))){
                        correcto = false;
                    }
                }else{                    
                    correcto = false;
                }

                if(!correcto){
                    String codError = (String)salidaOperacion;
                    throw new EjecucionOperacionModuloIntegracionException(infoSWActual.getTituloOperacion(),modulo.getDescripcionModulo(),modulo.getNombreModulo(),codError);
                }
                
            }

        } catch (InternalErrorException iee) {
            iee.printStackTrace();
        } catch (EjecucionSWException eswe) {
            // Captura errores de ejecucion del servicio web, y comprueba le definición de la llamada para 
            // comprobar si el servicio web es obligatorio u opcional que se cumpla su ejecucion.
           
            if (infoSWActual != null) eswe.setStopEjecucion(infoSWActual.isObligatorio());
            throw eswe;        
        }catch(NoServicioWebDefinidoException e){
            throw e;
        }catch(FaltaDatoObligatorioException e){            
            throw e;
        }catch(EjecucionOperacionModuloIntegracionException e){
            
            throw e;
        }
        
        m_Log.debug("--------------------------------------------------------------");
        m_Log.debug("Fin ejecutar1SW-----------------------------------------------");
        m_Log.debug("--------------------------------------------------------------");
        
    }

    private void completarConDatosProcedimiento(Collection paramsEntrada, Collection datosDefinicion) {

        for (Iterator itParamsEntrada = paramsEntrada.iterator(); itParamsEntrada.hasNext(); ) {
            DefinicionParametroEntradaVO defEntrada = (DefinicionParametroEntradaVO) itParamsEntrada.next();
            // No miramos aquellos datos que son constantes.
            if (defEntrada.getTipoParametro() != 2) {
                for (Iterator itDatosProc = datosDefinicion.iterator(); itDatosProc.hasNext(); ) {
                    ParametroConfigurableVO paramProc = (ParametroConfigurableVO) itDatosProc.next();
                    if (paramProc.getTipoValorPaso() == 0 &&
                            paramProc.getNombreDefinicion().equals(defEntrada.getDescParam()))
                        defEntrada.setValorDefecto(paramProc.getValorConstante());
                }
            }
        }
    }

    private HashMap<String, CampoValueObject> recuperoDatosProc(int codMunicipio, String codProcedimiento, String[] params)
    throws InternalErrorException {

        DefinicionSWTramitacionManager defManager = DefinicionSWTramitacionManager.getInstance();
        Vector<CampoValueObject> campos = defManager.getListaCampos(codMunicipio, codProcedimiento, true, 0, params);

        HashMap<String, CampoValueObject> mapaCampos = new HashMap<String, CampoValueObject>();
        for (CampoValueObject campo : campos) {
            mapaCampos.put(campo.getTitulo(), campo);
        }
        return mapaCampos;
    }

    private void completarConDatosExpediente(Collection<DefinicionParametroEntradaVO> paramsEntrada,
                                             Collection<ParametroConfigurableVO> datosDefinicion,
                                             PeticionSWVO peticion, HashMap<String, CampoValueObject> mapaCampos)
    throws InternalErrorException, FaltaDatoObligatorioException {
        m_Log.info("completarConDatosExpediente() BEGIN.");
        int indexParamEnt = 0;
        int indexDatoDef = 0;
        for (DefinicionParametroEntradaVO defEntrada: paramsEntrada) {
            m_Log.debug("paramEntrada " + indexParamEnt++ + " es = " + defEntrada);
            // No miramos aquellos datos que son constantes.
            if (defEntrada.getTipoParametro() != 2) {
                for (ParametroConfigurableVO paramProc: datosDefinicion) {
                    if (paramProc.getTipoValorPaso() == 1 && paramProc.getNombreDefinicion().equals(defEntrada.getDescParam())) {
                        m_Log.debug("datoDefinicion " + indexDatoDef++ + " es = " + paramProc);

                        CampoValueObject campo = mapaCampos.get(paramProc.getCodCampoExp());
                        m_Log.debug("Campo es = " + campo);    
                        String valor = "";
                        if (campo != null) {
                            valor = recuperaDatoExpediente(campo.getOrigen(), campo.getTabla(), peticion);
                            if (defEntrada.isEsObligatorio() && valor.equals(""))
                                throw new FaltaDatoObligatorioException(campo.getTitulo());
                        }
                        defEntrada.setValorDefecto(valor);
                    }                   
                }
            }
        }
    }

    private String recuperaDatoExpediente(String origenCampo, String tablaCampo, PeticionSWVO peticion) throws InternalErrorException {
        m_Log.info(String.format("recuperaDatoExpediente() BEGIN - origenCampo = %s, tablaCampo = %s", origenCampo, tablaCampo));
        int codMunicipio = peticion.getCodMunicipio();
        int ocurrencia = peticion.getOcurrencia();
        String numExpediente = peticion.getNumExpediente();
        int codTramite = peticion.getCodTramite();
        String[] params = peticion.getParams();

        RecolectorDatosManager manager = RecolectorDatosManager.getInstance();

        String valor = "";
        if (tablaCampo.equals("V_CRO")) {
            valor = manager.getDatoVistaCRO(ocurrencia, numExpediente, codTramite, origenCampo, params);
        }
        if (tablaCampo.equals("INT")) {
            valor = manager.getDatoVistaINT(codMunicipio, numExpediente, origenCampo, params);
        }
        if (tablaCampo.equals("E_TNU")) {
            valor = manager.getDatoTablaTNU(codMunicipio, numExpediente, origenCampo, params);
        }
        if (tablaCampo.equals("E_TNUT")) {
            valor = manager.getDatoTablaTNUT(codMunicipio, numExpediente, origenCampo,ocurrencia, params);
        }
        if (tablaCampo.equals("E_TXT")) {
            valor = manager.getDatoTablaTXT(codMunicipio, numExpediente, origenCampo, params);
        }
        if (tablaCampo.equals("E_TXTT")) {
            valor = manager.getDatoTablaTXTT(codMunicipio, numExpediente, origenCampo,ocurrencia, params);
        }
        if (tablaCampo.equals("E_TFE")) {
            valor = manager.getDatoTablaTFE(codMunicipio, numExpediente, origenCampo, params);
        }
        if (tablaCampo.equals("E_TFET")) {
            valor = manager.getDatoTablaTFET(codMunicipio, numExpediente, origenCampo,ocurrencia, params);
        }
        if (tablaCampo.equals("E_TTL")) {
            valor = manager.getDatoTablaTTL(codMunicipio, numExpediente, origenCampo, params);
        }
        if (tablaCampo.equals("E_TTLT")) {
            valor = manager.getDatoTablaTTLT(codMunicipio, numExpediente, origenCampo,ocurrencia, params);
        }
        if (tablaCampo.equals("E_TDE")) {
            valor = manager.getDatoTablaTDE(codMunicipio, numExpediente, origenCampo, params);
        }
        if (tablaCampo.equals("E_TDET")) {
            valor = manager.getDatoTablaTDET(codMunicipio, numExpediente, origenCampo, ocurrencia, params);
        }

        return valor;
    }

    private void completarConOtrosDatos(Collection<DefinicionParametroEntradaVO> paramsEntrada, Collection<ParametroConfigurableVO> datosDefinicion, PeticionSWVO peticion){
        m_Log.info("completarConOtrosDatos() BEGIN.");
        int indexParamEnt = 0;
        int indexDatoDef = 0;
        for (DefinicionParametroEntradaVO defEntrada: paramsEntrada) {
            m_Log.debug("paramEntrada " + indexParamEnt++ + " es = " + defEntrada);
            // Miramos solo datos que son variables pero no toman el valor del expediente
            if (defEntrada.getTipoParametro() == 1) {
                for (ParametroConfigurableVO paramProc: datosDefinicion) {
                    if (paramProc.getTipoValorPaso() == 2 && paramProc.getNombreDefinicion().equals(defEntrada.getDescParam())) {
                        m_Log.debug("datoDefinicion " + indexDatoDef++ + " es = " + paramProc);
                        if(paramProc.getCodCampoExp().toLowerCase().contains("login")) {
                            defEntrada.setValorDefecto(peticion.getUsuario());
                        }
                    }                   
                }
            }
        }
    }
    
    private HashMap<String, String> processCadenaParams(Collection<DefinicionParametroEntradaVO> paramsEntrada) {
        HashMap<String, String> params = new HashMap<String, String>();
        for (DefinicionParametroEntradaVO defParam: paramsEntrada) {
            String key = defParam.getDescParam();
            String value = defParam.getValorDefecto();
            params.put(key, value);
        }
        return params;
    }

    private boolean fueResultadoCorrecto(Collection paramsSalida, Collection valoresSalida) {
        boolean salidaCorrecta = true;
        Iterator itValores = valoresSalida.iterator();
        for (Iterator itParams = paramsSalida.iterator(); itParams.hasNext();) {
            DefinicionParametroSalidaVO defParam = (DefinicionParametroSalidaVO) itParams.next();
            String valor = (String)itValores.next();
            if (defParam.getTipoDato() == 1) {
                salidaCorrecta = salidaCorrecta && (valor.equals(defParam.getValorCorrecto()));
            }
        }
        return salidaCorrecta;
    }

    private String getErrores(Collection paramsSalida, Collection valoresSalida) {
        String strErrores = "";
        Iterator itValores = valoresSalida.iterator();
        for (Iterator itParams = paramsSalida.iterator(); itParams.hasNext();) {
            DefinicionParametroSalidaVO defParam = (DefinicionParametroSalidaVO) itParams.next();
            String valor = (String)itValores.next();
            if (defParam.getTipoDato() == 2) {
                strErrores += " | " + valor;
            }
        }
        return strErrores;
    }

    private void guardarDatosExpediente(Collection datosSalida, Collection resultados, Collection datosDefinicion,
                                             PeticionSWVO peticion, HashMap mapaCampos, AdaptadorSQLBD oad, Connection con) throws InternalErrorException {

        m_Log.debug(mapaCampos);
        Iterator itResultados = resultados.iterator();
        for (Iterator itParamsSalida = datosSalida.iterator(); itParamsSalida.hasNext(); ) {
            DefinicionParametroSalidaVO defSalida = (DefinicionParametroSalidaVO) itParamsSalida.next();
            String valorResultado = (String)itResultados.next();
            // No miramos aquellos datos que son constantes.
            for (Iterator itDatosProc = datosDefinicion.iterator(); itDatosProc.hasNext(); ) {
                    ParametroConfigurableVO paramProc = (ParametroConfigurableVO) itDatosProc.next();
                    m_Log.debug("|||||| " + paramProc.getNombreDefinicion() + " |||||| " + defSalida.getDescParam());
                    if (paramProc.getTipoValorPaso() == 1 && paramProc.getNombreDefinicion().equals(defSalida.getDescParam())) {
                        m_Log.debug("Son el mismo campo. Buscamos el campo:" + paramProc.getCodCampoExp());
                        CampoValueObject campo = (CampoValueObject)mapaCampos.get(paramProc.getCodCampoExp());

                        if (campo != null) {
                            m_Log.debug("Se encuentra el campo: " + campo.getOrigen() + " para la tabla " + campo.getTabla());
                            if (valorResultado != null) guardarDatoExpediente(campo.getOrigen(), campo.getTabla(), peticion, valorResultado,oad, con);
                        }
                    }
            }
        }
    }

    
    
    private void guardarDatoExpediente(String origenCampo, String tablaCampo, PeticionSWVO peticion, String valorResultado,AdaptadorSQLBD oad, Connection con) throws InternalErrorException {

        int ocurrencia = peticion.getOcurrencia();
        int codMunicipio = peticion.getCodMunicipio();
        String numExpediente = peticion.getNumExpediente();
        String[] params = peticion.getParams();
        int ejercicio = peticion.getEjercicio();

        DatosSuplementariosManager manager = DatosSuplementariosManager.getInstance();

        if (tablaCampo.equals("E_TNU")) {
            // Vamos a intentar reformatear el dato al formato en que los guarda el SGE.
            Double doubleValue = Double.parseDouble(valorResultado);
            NumberFormat numFormat = NumberFormat.getInstance();
            valorResultado = numFormat.format(doubleValue);
            
            EstructuraCampo estCampo = new EstructuraCampo();
            estCampo.setCodCampo(origenCampo);
            estCampo.setCodTipoDato("1");
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codMunicipio", Integer.toString(codMunicipio));
            gVO.setAtributo("ejercicio", Integer.toString(ejercicio));
            gVO.setAtributo("numero", numExpediente);
            gVO.setAtributo(origenCampo, valorResultado);
            Vector<EstructuraCampo> estructuras = new Vector<EstructuraCampo>();
            estructuras.add(estCampo);
            Vector<GeneralValueObject> valores = new Vector<GeneralValueObject>();
            valores.add(gVO);
            manager.grabarDatosSuplementariosConsultas(estructuras, valores, params, oad, con);
        } else if (tablaCampo.equals("E_TNUT")) {
            String[] datosCampo = origenCampo.split("_");
            int codTramite = Integer.parseInt(datosCampo[1]);
            if (codTramite == peticion.getCodTramite()) {
                String codigoCampo = datosCampo[2];
                TramitacionExpedientesValueObject tEVO = new TramitacionExpedientesValueObject();
                tEVO.setCodMunicipio(Integer.toString(codMunicipio));
                tEVO.setCodProcedimiento(peticion.getCodProcedimiento());
                tEVO.setEjercicio(Integer.toString(ejercicio));
                tEVO.setNumeroExpediente(numExpediente);
                tEVO.setCodTramite(Integer.toString(codTramite));
                tEVO.setOcurrenciaTramite(Integer.toString(ocurrencia));
                // Vamos a intentar reformatear el dato al formato en que los guarda el SGE.
                Double doubleValue = Double.parseDouble(valorResultado);
                NumberFormat numFormat = NumberFormat.getInstance();
                valorResultado = numFormat.format(doubleValue);

                EstructuraCampo estCampo = new EstructuraCampo();
                estCampo.setCodCampo(codigoCampo);
                estCampo.setCodTipoDato("1");
                Vector<EstructuraCampo> estructuras = new Vector<EstructuraCampo>();
                estructuras.add(estCampo);
                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo(codigoCampo, valorResultado);
                try {
                    DatosSuplementariosDAO.getInstance().grabarDatosSuplementariosTramite2(estructuras, gVO, con, tEVO, oad);
                } catch (TechnicalException te) {
                    te.printStackTrace();
                    throw new InternalErrorException(te);
                } catch (BDException bde) {
                    bde.printStackTrace();
                    throw new InternalErrorException(bde);
                }
            }
        } else if (tablaCampo.equals("E_TXT")) {
            EstructuraCampo estCampo = new EstructuraCampo();
            estCampo.setCodCampo(origenCampo);
            estCampo.setCodTipoDato("2");
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codMunicipio", Integer.toString(codMunicipio));
            gVO.setAtributo("ejercicio", Integer.toString(ejercicio));
            gVO.setAtributo("numero", numExpediente);
            gVO.setAtributo(origenCampo, valorResultado);
            Vector<EstructuraCampo> estructuras = new Vector<EstructuraCampo>();
            estructuras.add(estCampo);
            Vector<GeneralValueObject> valores = new Vector<GeneralValueObject>();
            valores.add(gVO);
            manager.grabarDatosSuplementariosConsultas(estructuras, valores, params, oad, con);
        } else if (tablaCampo.equals("E_TXTT")) {
            String[] datosCampo = origenCampo.split("_");
            int codTramite = Integer.parseInt(datosCampo[1]);
            if (codTramite == peticion.getCodTramite()) {
                String codigoCampo = datosCampo[2];
                TramitacionExpedientesValueObject tEVO = new TramitacionExpedientesValueObject();
                tEVO.setCodMunicipio(Integer.toString(codMunicipio));
                tEVO.setCodProcedimiento(peticion.getCodProcedimiento());
                tEVO.setEjercicio(Integer.toString(ejercicio));
                tEVO.setNumeroExpediente(numExpediente);
                tEVO.setCodTramite(Integer.toString(codTramite));
                tEVO.setOcurrenciaTramite(Integer.toString(ocurrencia));
                EstructuraCampo estCampo = new EstructuraCampo();
                estCampo.setCodCampo(codigoCampo);
                estCampo.setCodTipoDato("2");
                Vector<EstructuraCampo> estructuras = new Vector<EstructuraCampo>();
                estructuras.add(estCampo);
                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo(codigoCampo, valorResultado);
                try {
                    DatosSuplementariosDAO.getInstance().grabarDatosSuplementariosTramite2(estructuras, gVO, con, tEVO, oad);
                } catch (TechnicalException te) {
                    te.printStackTrace();
                    throw new InternalErrorException(te);
                } catch (BDException bde) {
                    bde.printStackTrace();
                    throw new InternalErrorException(bde);
                }
            }
        } else if (tablaCampo.equals("E_TFE")) {
            EstructuraCampo estCampo = new EstructuraCampo();
            estCampo.setCodCampo(origenCampo);
            estCampo.setCodTipoDato("3");
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codMunicipio", Integer.toString(codMunicipio));
            gVO.setAtributo("ejercicio", Integer.toString(ejercicio));
            gVO.setAtributo("numero", numExpediente);
            gVO.setAtributo(origenCampo, valorResultado);
            Vector<EstructuraCampo> estructuras = new Vector<EstructuraCampo>();
            estructuras.add(estCampo);
            Vector<GeneralValueObject> valores = new Vector<GeneralValueObject>();
            valores.add(gVO);
            manager.grabarDatosSuplementariosConsultas(estructuras, valores, params, oad, con);
        } else if (tablaCampo.equals("E_TFET")) {
            String[] datosCampo = origenCampo.split("_");
            int codTramite = Integer.parseInt(datosCampo[1]);
            if (codTramite == peticion.getCodTramite()) {
                String codigoCampo = datosCampo[2];
                TramitacionExpedientesValueObject tEVO = new TramitacionExpedientesValueObject();
                tEVO.setCodMunicipio(Integer.toString(codMunicipio));
                tEVO.setCodProcedimiento(peticion.getCodProcedimiento());
                tEVO.setEjercicio(Integer.toString(ejercicio));
                tEVO.setNumeroExpediente(numExpediente);
                tEVO.setCodTramite(Integer.toString(codTramite));
                tEVO.setOcurrenciaTramite(Integer.toString(ocurrencia));
                EstructuraCampo estCampo = new EstructuraCampo();
                estCampo.setCodCampo(codigoCampo);
                estCampo.setCodTipoDato("3");
                Vector<EstructuraCampo> estructuras = new Vector<EstructuraCampo>();
                estructuras.add(estCampo);
                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo(codigoCampo, valorResultado);
                try {
                    DatosSuplementariosDAO.getInstance().grabarDatosSuplementariosTramite2(estructuras, gVO, con, tEVO, oad);
                } catch (TechnicalException te) {
                    te.printStackTrace();
                    throw new InternalErrorException(te);
                } catch (BDException bde) {
                    bde.printStackTrace();
                    throw new InternalErrorException(bde);
                }
            }
        }
        if (tablaCampo.equals("E_TTL")) {
            EstructuraCampo estCampo = new EstructuraCampo();
            estCampo.setCodCampo(origenCampo);
            estCampo.setCodTipoDato("4");
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codMunicipio", Integer.toString(codMunicipio));
            gVO.setAtributo("ejercicio", Integer.toString(ejercicio));
            gVO.setAtributo("numero", numExpediente);
            gVO.setAtributo(origenCampo, valorResultado);
            Vector<EstructuraCampo> estructuras = new Vector<EstructuraCampo>();
            estructuras.add(estCampo);
            Vector<GeneralValueObject> valores = new Vector<GeneralValueObject>();
            valores.add(gVO);
            manager.grabarDatosSuplementariosConsultas(estructuras, valores, params, oad, con);
        } else if (tablaCampo.equals("E_TTLT")) {
            String[] datosCampo = origenCampo.split("_");
            int codTramite = Integer.parseInt(datosCampo[1]);
            if (codTramite == peticion.getCodTramite()) {
                String codigoCampo = datosCampo[2];
                TramitacionExpedientesValueObject tEVO = new TramitacionExpedientesValueObject();
                tEVO.setCodMunicipio(Integer.toString(codMunicipio));
                tEVO.setCodProcedimiento(peticion.getCodProcedimiento());
                tEVO.setEjercicio(Integer.toString(ejercicio));
                tEVO.setNumeroExpediente(numExpediente);
                tEVO.setCodTramite(Integer.toString(codTramite));
                tEVO.setOcurrenciaTramite(Integer.toString(ocurrencia));
                EstructuraCampo estCampo = new EstructuraCampo();
                estCampo.setCodCampo(codigoCampo);
                estCampo.setCodTipoDato("4");
                Vector<EstructuraCampo> estructuras = new Vector<EstructuraCampo>();
                estructuras.add(estCampo);
                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo(codigoCampo, valorResultado);
                try {
                    DatosSuplementariosDAO.getInstance().grabarDatosSuplementariosTramite2(estructuras, gVO, con, tEVO, oad);
                } catch (TechnicalException te) {
                    te.printStackTrace();
                    throw new InternalErrorException(te);
                } catch (BDException bde) {
                    bde.printStackTrace();
                    throw new InternalErrorException(bde);
                }
            }
        } else if (tablaCampo.equals("E_TDE")) {
            EstructuraCampo estCampo = new EstructuraCampo();
            estCampo.setCodCampo(origenCampo);
            estCampo.setCodTipoDato("6");
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codMunicipio", Integer.toString(codMunicipio));
            gVO.setAtributo("ejercicio", Integer.toString(ejercicio));
            gVO.setAtributo("numero", numExpediente);
            gVO.setAtributo(origenCampo, valorResultado);
            Vector<EstructuraCampo> estructuras = new Vector<EstructuraCampo>();
            estructuras.add(estCampo);
            Vector<GeneralValueObject> valores = new Vector<GeneralValueObject>();
            valores.add(gVO);
            manager.grabarDatosSuplementariosConsultas(estructuras, valores, params, oad, con);
        } else if (tablaCampo.equals("E_TDET")) {
            String[] datosCampo = origenCampo.split("_");
            int codTramite = Integer.parseInt(datosCampo[1]);
            if (codTramite == peticion.getCodTramite()) {
                String codigoCampo = datosCampo[2];
                TramitacionExpedientesValueObject tEVO = new TramitacionExpedientesValueObject();
                tEVO.setCodMunicipio(Integer.toString(codMunicipio));
                tEVO.setCodProcedimiento(peticion.getCodProcedimiento());
                tEVO.setEjercicio(Integer.toString(ejercicio));
                tEVO.setNumeroExpediente(numExpediente);
                tEVO.setCodTramite(Integer.toString(codTramite));
                tEVO.setOcurrenciaTramite(Integer.toString(ocurrencia));
                EstructuraCampo estCampo = new EstructuraCampo();
                estCampo.setCodCampo(codigoCampo);
                estCampo.setCodTipoDato("6");
                Vector<EstructuraCampo> estructuras = new Vector<EstructuraCampo>();
                estructuras.add(estCampo);
                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo(codigoCampo, valorResultado);
                try {
                    DatosSuplementariosDAO.getInstance().grabarDatosSuplementariosTramite2(estructuras, gVO, con, tEVO, oad);
                } catch (TechnicalException te) {
                    te.printStackTrace();
                    throw new InternalErrorException(te);
                } catch (BDException bde) {
                    bde.printStackTrace();
                    throw new InternalErrorException(bde);
                }
            }
        }
    }

}
