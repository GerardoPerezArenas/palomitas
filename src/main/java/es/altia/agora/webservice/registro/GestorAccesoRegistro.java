package es.altia.agora.webservice.registro;

import es.altia.agora.business.sge.AsientoFichaExpedienteVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Vector;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.DocumentoValueObject;
import es.altia.agora.business.sge.TramitacionValueObject;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.webservice.registro.exceptions.RegistroException;
import es.altia.agora.webservice.registro.exceptions.InstanciacionRegistroException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import java.util.*;

public class GestorAccesoRegistro {

    private Vector<String> idsServicios;
    private String prefijoPropiedad;

    protected static Log log = LogFactory.getLog(GestorAccesoRegistro.class.getName());

    public GestorAccesoRegistro(Vector<String> idsServicios, String prefijoPropiedad) {
        this.idsServicios = idsServicios;
        this.prefijoPropiedad = prefijoPropiedad;
    }

    public Vector<String> getIdsServicios() {
        return idsServicios;
    }

    public void setIdsServicios(Vector<String> idsServicios) {
        this.idsServicios = idsServicios;
    }

    public String getPrefijoPropiedad() {
        return prefijoPropiedad;
    }

    public void setPrefijoPropiedad(String prefijoPropiedad) {
        this.prefijoPropiedad = prefijoPropiedad;
    }

    public HashMap getAsientosEntradaRegistro(UsuarioValueObject uVO, TramitacionValueObject tVO, String[] params,
                                             String fechaDesde, String fechaHasta,String documento,String nombre,String apellido1,String apellido2,String codAsunto,String unidadRegistroAsunto,String tipoRegistroAsunto,String codUorDestino,String ejercicio, String numAnotacion, String codUorAnotacion) {

        Vector asientosEncontrados = new Vector();
        Collection<String> errores = new ArrayList<String>();

        Config m_ConfigRegistro = ConfigServiceHelper.getConfig("Registro");


        ArrayList<String> busquedaDesactivada = new ArrayList<String>();
        /*
        * Recorremos el vector de identificadores. Recuperamos la implementacion correspondiente de
        * AccesoRegistro a partir del identificador. Ejecutamos el metodo getAsientosEntradaRegistro de la implementacion
        * para recuperar los asientos de ese servicio.
        */
        for (String idServicio : idsServicios) {


            try {
                AccesoRegistro registro    = AccesoRegistroFactoria.getImpl(idServicio, prefijoPropiedad);
                String strContinuar        = m_ConfigRegistro.getString(registro.getPrefijoPropiedad() + "continuar");
                String strBusquedaDesactivada = "";
                                
                log.debug("PARA EL SERVICIO " + idServicio + " LA BUSQUEDA EXTENDIDA ACTIVADA? :: " + busquedaDesactivada);
                boolean continuar = Boolean.parseBoolean(strContinuar);

                try{
                    // Se comprueba si la búsqueda extendida está desactivada (el servicio sólo admite búsqueda por )
                    // para el servicio y de este modo generar un mensaje de error
                    ResourceBundle bRegistro = ResourceBundle.getBundle("Registro");
                    strBusquedaDesactivada = bRegistro.getString(registro.getPrefijoPropiedad() + "BusquedaExtendidaDesactivada");
                    log.debug("strBusquedaDesactivada:" + strBusquedaDesactivada);
                    if(strBusquedaDesactivada!=null && strBusquedaDesactivada.equalsIgnoreCase("SI")){
                        busquedaDesactivada.add(idServicio);
                    }

                }catch(Exception e){
                    log.debug("Se lanza una excepcion:" + e);
                }

                Vector asientosServicio = registro.getAsientosEntradaRegistro(uVO, tVO, params, fechaDesde, fechaHasta, documento, nombre, apellido1, apellido2, codAsunto,unidadRegistroAsunto,tipoRegistroAsunto, codUorDestino, ejercicio, numAnotacion,codUorAnotacion);
                asientosEncontrados.addAll(asientosServicio);
                if (asientosServicio.size() > 0 && !continuar) {
                    return buildEstructuraRetorno(asientosEncontrados, errores,busquedaDesactivada);
                }

            } catch (InstanciacionRegistroException ire) {
                log.error("NO SE HA PODIDO INSTANCIAR LA CLASE QUE IMPLEMENTA EL SERVICIO " + idServicio);
                log.debug("CONTINUAMOS CON EL SIGUIENTE SERVICIO DE LA LISTA");
                errores.add(idServicio);
            } catch (RegistroException re) {
                log.error("SE HA PRODUCIDO UN ERROR AL EJECUTAR EL METODO DE BUSQUEDA DEL SERVICIO " + idServicio);
                log.debug("CONTINUAMOS CON EL SIGUIENTE SERVICIO DE LA LISTA");
                errores.add(idServicio);
            }
        }

        return buildEstructuraRetorno(asientosEncontrados, errores,busquedaDesactivada);
    }
    
    
    public HashMap getAsientosEntradaRegistroPluginTecnico(UsuarioValueObject uVO, TramitacionValueObject tVO, String[] params,
                                             String fechaDesde, String fechaHasta,String documento,String nombre,String apellido1,String apellido2,String codAsunto,String unidadRegistroAsunto,String tipoRegistroAsunto,String codUorDestino,String ejercicio, String numAnotacion, String codUorAnotacion, String codClasificacionAsunto,String unidadRegistroClasifAsunto,String docTecnicoRegistro) {

        Vector asientosEncontrados = new Vector();
        Collection<String> errores = new ArrayList<String>();

        Config m_ConfigRegistro = ConfigServiceHelper.getConfig("Registro");


        ArrayList<String> busquedaDesactivada = new ArrayList<String>();
        /*
        * Recorremos el vector de identificadores. Recuperamos la implementacion correspondiente de
        * AccesoRegistro a partir del identificador. Ejecutamos el metodo getAsientosEntradaRegistro de la implementacion
        * para recuperar los asientos de ese servicio.
        */
        for (String idServicio : idsServicios) {


            try {
                AccesoRegistro registro    = AccesoRegistroFactoria.getImpl(idServicio, prefijoPropiedad);
                String strContinuar        = m_ConfigRegistro.getString(registro.getPrefijoPropiedad() + "continuar");
                String strBusquedaDesactivada = "";
                                
                log.debug("PARA EL SERVICIO " + idServicio + " LA BUSQUEDA EXTENDIDA ACTIVADA? :: " + busquedaDesactivada);
                boolean continuar = Boolean.parseBoolean(strContinuar);

                try{
                    // Se comprueba si la búsqueda extendida está desactivada (el servicio sólo admite búsqueda por )
                    // para el servicio y de este modo generar un mensaje de error
                    ResourceBundle bRegistro = ResourceBundle.getBundle("Registro");
                    strBusquedaDesactivada = bRegistro.getString(registro.getPrefijoPropiedad() + "BusquedaExtendidaDesactivada");
                    log.debug("strBusquedaDesactivada:" + strBusquedaDesactivada);
                    if(strBusquedaDesactivada!=null && strBusquedaDesactivada.equalsIgnoreCase("SI")){
                        busquedaDesactivada.add(idServicio);
                    }

                }catch(Exception e){
                    log.debug("Se lanza una excepcion:" + e);
                }

                Vector asientosServicio = registro.getAsientosEntradaRegistroPluginTecnico(uVO, tVO, params, fechaDesde, fechaHasta, documento, nombre, apellido1, apellido2, codAsunto,unidadRegistroAsunto,tipoRegistroAsunto, codUorDestino, ejercicio, numAnotacion,codUorAnotacion,codClasificacionAsunto,unidadRegistroClasifAsunto,docTecnicoRegistro);
                asientosEncontrados.addAll(asientosServicio);
                if (asientosServicio.size() > 0 && !continuar) {
                    return buildEstructuraRetorno(asientosEncontrados, errores,busquedaDesactivada);
                }

            } catch (InstanciacionRegistroException ire) {
                log.error("NO SE HA PODIDO INSTANCIAR LA CLASE QUE IMPLEMENTA EL SERVICIO " + idServicio);
                log.debug("CONTINUAMOS CON EL SIGUIENTE SERVICIO DE LA LISTA");
                errores.add(idServicio);
            } catch (RegistroException re) {
                log.error("SE HA PRODUCIDO UN ERROR AL EJECUTAR EL METODO DE BUSQUEDA DEL SERVICIO " + idServicio);
                log.debug("CONTINUAMOS CON EL SIGUIENTE SERVICIO DE LA LISTA");
                errores.add(idServicio);
            }
        }

        return buildEstructuraRetorno(asientosEncontrados, errores,busquedaDesactivada);
    }
    

    public HashMap getAsientosExpedientesHistorico(UsuarioValueObject uVO, TramitacionValueObject tVO, String[] params,
                                                  String fechaDesde, String fechaHasta,String documento,String nombre,String primerApellido,String segundoApellido,String codAsuntoSeleccionado,String unidadRegistroAsuntoSeleccionado,String tipoRegistroAsuntoSeleccionado,String codUorInterno, String ejercicio, String numAnotacion, String  codUorAnotacion) {

        Vector asientosEncontrados = new Vector();
        Collection<String> errores = new ArrayList<String>();

        Config m_ConfigRegistro = ConfigServiceHelper.getConfig("Registro");
        ArrayList<String> busquedaDesactivada = new ArrayList<String>();

        /*
        * Recorremos el vector de identificadores. Recuperamos la implementacion correspondiente de
        * AccesoRegistro a partir del identificador. Ejecutamos el metodo getAsientosExpedientesHistorico de la implementacion
        * para recuperar los asientos de ese servicio.
        */
        for (String idServicio : idsServicios) {

            try {
                AccesoRegistro registro = AccesoRegistroFactoria.getImpl(idServicio, prefijoPropiedad);
                String strContinuar = m_ConfigRegistro.getString(registro.getPrefijoPropiedad() + "continuar");
                boolean continuar = Boolean.parseBoolean(strContinuar);
                String strBusquedaDesactivada = "";
                Vector asientosServicio = registro.getAsientosExpedientesHistorico(uVO, tVO, params, fechaDesde, fechaHasta,documento,nombre,primerApellido,segundoApellido,codAsuntoSeleccionado,unidadRegistroAsuntoSeleccionado,tipoRegistroAsuntoSeleccionado,codUorInterno, ejercicio, numAnotacion,codUorAnotacion);
                asientosEncontrados.addAll(asientosServicio);

                try{
                    // Se comprueba si la búsqueda extendida está desactivada (el servicio sólo admite búsqueda por )
                    // para el servicio y de este modo generar un mensaje de error
                    ResourceBundle bRegistro = ResourceBundle.getBundle("Registro");
                    strBusquedaDesactivada = bRegistro.getString(registro.getPrefijoPropiedad() + "BusquedaExtendidaDesactivada");
                    if(strBusquedaDesactivada!=null && strBusquedaDesactivada.equalsIgnoreCase("SI")){
                        busquedaDesactivada.add(idServicio);
                    }

                }catch(Exception e){                    
                }
                
                if (asientosServicio.size() > 0 && !continuar) {
                    return buildEstructuraRetorno(asientosEncontrados, errores,busquedaDesactivada);
                }

            } catch (InstanciacionRegistroException ire) {
                log.error("NO SE HA PODIDO INSTANCIAR LA CLASE QUE IMPLEMENTA EL SERVICIO " + idServicio);
                log.debug("CONTINUAMOS CON EL SIGUIENTE SERVICIO DE LA LISTA");
                errores.add(idServicio);
            } catch (RegistroException re) {
                log.error("SE HA PRODUCIDO UN ERROR AL EJECUTAR EL METODO DE BUSQUEDA DEL SERVICIO " + idServicio);
                log.debug("CONTINUAMOS CON EL SIGUIENTE SERVICIO DE LA LISTA");
                errores.add(idServicio);
            }

        }

        return buildEstructuraRetorno(asientosEncontrados, errores,busquedaDesactivada);

    }
    
    
    
        public HashMap getAsientosExpedientesHistoricoPluginTecnicoReferencia(UsuarioValueObject uVO, TramitacionValueObject tVO, String[] params,
                                                  String fechaDesde, String fechaHasta,String documento,String nombre,String primerApellido,String segundoApellido,String codAsuntoSeleccionado,String unidadRegistroAsuntoSeleccionado,String tipoRegistroAsuntoSeleccionado,String codUorInterno, String ejercicio, String numAnotacion, String  codUorAnotacion, String codClasificacionAsunto,String unidadRegistroClasifAsunto,String docTecnicoRegistro) {

        Vector asientosEncontrados = new Vector();
        Collection<String> errores = new ArrayList<String>();

        Config m_ConfigRegistro = ConfigServiceHelper.getConfig("Registro");
        ArrayList<String> busquedaDesactivada = new ArrayList<String>();

        /*
        * Recorremos el vector de identificadores. Recuperamos la implementacion correspondiente de
        * AccesoRegistro a partir del identificador. Ejecutamos el metodo getAsientosExpedientesHistorico de la implementacion
        * para recuperar los asientos de ese servicio.
        */
        for (String idServicio : idsServicios) {

            try {
                AccesoRegistro registro = AccesoRegistroFactoria.getImpl(idServicio, prefijoPropiedad);
                String strContinuar = m_ConfigRegistro.getString(registro.getPrefijoPropiedad() + "continuar");
                boolean continuar = Boolean.parseBoolean(strContinuar);
                String strBusquedaDesactivada = "";
                Vector asientosServicio = registro.getAsientosExpedientesHistoricoPluginTecnico(uVO, tVO, params, fechaDesde, fechaHasta,documento,nombre,primerApellido,segundoApellido,codAsuntoSeleccionado,unidadRegistroAsuntoSeleccionado,tipoRegistroAsuntoSeleccionado,codUorInterno, ejercicio, numAnotacion,codUorAnotacion,codClasificacionAsunto,unidadRegistroClasifAsunto,docTecnicoRegistro);
                asientosEncontrados.addAll(asientosServicio);

                try{
                    // Se comprueba si la búsqueda extendida está desactivada (el servicio sólo admite búsqueda por )
                    // para el servicio y de este modo generar un mensaje de error
                    ResourceBundle bRegistro = ResourceBundle.getBundle("Registro");
                    strBusquedaDesactivada = bRegistro.getString(registro.getPrefijoPropiedad() + "BusquedaExtendidaDesactivada");
                    if(strBusquedaDesactivada!=null && strBusquedaDesactivada.equalsIgnoreCase("SI")){
                        busquedaDesactivada.add(idServicio);
                    }

                }catch(Exception e){                    
                }
                
                if (asientosServicio.size() > 0 && !continuar) {
                    return buildEstructuraRetorno(asientosEncontrados, errores,busquedaDesactivada);
                }

            } catch (InstanciacionRegistroException ire) {
                log.error("NO SE HA PODIDO INSTANCIAR LA CLASE QUE IMPLEMENTA EL SERVICIO " + idServicio);
                log.debug("CONTINUAMOS CON EL SIGUIENTE SERVICIO DE LA LISTA");
                errores.add(idServicio);
            } catch (RegistroException re) {
                log.error("SE HA PRODUCIDO UN ERROR AL EJECUTAR EL METODO DE BUSQUEDA DEL SERVICIO " + idServicio);
                log.debug("CONTINUAMOS CON EL SIGUIENTE SERVICIO DE LA LISTA");
                errores.add(idServicio);
            }

        }

        return buildEstructuraRetorno(asientosEncontrados, errores,busquedaDesactivada);

    }

    public RegistroValueObject getInfoAsientoConsulta(RegistroValueObject registroVO, String[] params) throws RegistroException {

        String idServicio = registroVO.getIdServicioOrigen();

        try {
            AccesoRegistro registro = AccesoRegistroFactoria.getImpl(idServicio, prefijoPropiedad);
            return registro.getInfoAsientoConsulta(registroVO, params);            
        } catch (RegistroException re) {
            log.error("SE HA PRODUCIDO UN ERROR AL EJECUTAR EL METODO DE BUSQUEDA DEL SERVICIO " + idServicio);
            log.debug("CONTINUAMOS CON EL SIGUIENTE SERVICIO DE LA LISTA");
            throw re;
        }
    }

    public void cambiaEstadoAsiento(TramitacionValueObject tramitacionVO, UsuarioValueObject usuarioVO, int estado,
                                    String[] params) throws RegistroException {

        String idServicio = tramitacionVO.getOrigen();

        try {
            AccesoRegistro registro = AccesoRegistroFactoria.getImpl(idServicio, prefijoPropiedad);            
            registro.cambiaEstadoAsiento(tramitacionVO, usuarioVO, estado, params);
            
        } catch (RegistroException re) {
            log.error("SE HA PRODUCIDO UN ERROR AL EJECUTAR EL METODO DE BUSQUEDA DEL SERVICIO " + idServicio);
            log.debug("CONTINUAMOS CON EL SIGUIENTE SERVICIO DE LA LISTA");
            throw re;
        }
    }

    public void recuperarAsiento(TramitacionValueObject tramitacionVO, UsuarioValueObject usuarioVO, String[] params)
    throws RegistroException {

        String idServicio = tramitacionVO.getOrigen();

        try {
            AccesoRegistro registro = AccesoRegistroFactoria.getImpl(idServicio, prefijoPropiedad);
            registro.recuperarAsiento(tramitacionVO, usuarioVO, params);
        } catch (RegistroException re) {
            log.error("SE HA PRODUCIDO UN ERROR AL EJECUTAR EL METODO DE BUSQUEDA DEL SERVICIO " + idServicio);
            log.debug("CONTINUAMOS CON EL SIGUIENTE SERVICIO DE LA LISTA");
            throw re;
        }
    }

    public void adjuntarExpedientesDesdeUnidadTramitadora(TramitacionValueObject tramitacionVO,
                                                          UsuarioValueObject usuarioVO, String[] params)
    throws RegistroException {

        String idServicio = tramitacionVO.getOrigen();

        try {
            AccesoRegistro registro = AccesoRegistroFactoria.getImpl(idServicio, prefijoPropiedad);
            registro.adjuntarExpedientesDesdeUnidadTramitadora(tramitacionVO, usuarioVO, params);
        } catch (RegistroException re) {
            log.error("SE HA PRODUCIDO UN ERROR AL EJECUTAR EL METODO DE BUSQUEDA DEL SERVICIO " + idServicio);
            log.debug("CONTINUAMOS CON EL SIGUIENTE SERVICIO DE LA LISTA");
            throw re;
        }
    }

    public HashMap cargaListaAsientosExpediente(GeneralValueObject gVO, UsuarioValueObject usuarioVO, String[] params) {

        ArrayList<AsientoFichaExpedienteVO> asientosEncontrados = new ArrayList<AsientoFichaExpedienteVO>();
        Collection<String> errores = new ArrayList<String>();

        Config m_ConfigRegistro = ConfigServiceHelper.getConfig("Registro");

        /*
        * Recorremos el vector de identificadores. Recuperamos la implementacion correspondiente de
        * AccesoRegistro a partir del identificador. Ejecutamos el metodo cargaListaAsientosExpediente de la implementacion
        * para recuperar los asientos de ese servicio.
        */
        for (String idServicio : idsServicios) {

            try {
                AccesoRegistro registro = AccesoRegistroFactoria.getImpl(idServicio, prefijoPropiedad);
                String strContinuar = m_ConfigRegistro.getString(registro.getPrefijoPropiedad() + "continuar");
                boolean continuar = Boolean.parseBoolean(strContinuar);
                gVO.setAtributo("idServicio", idServicio);
                List<AsientoFichaExpedienteVO> asientosServicio = registro.cargaListaAsientosExpediente(gVO, usuarioVO, params);
                if (asientosServicio!= null){
                    asientosEncontrados.addAll(asientosServicio);
                    if (asientosServicio.size() > 0 && !continuar) {
                        return buildEstructuraRetorno(asientosEncontrados, errores);
                    }
                }

            } catch (InstanciacionRegistroException ire) {
                log.error("NO SE HA PODIDO INSTANCIAR LA CLASE QUE IMPLEMENTA EL SERVICIO " + idServicio);
                log.debug("CONTINUAMOS CON EL SIGUIENTE SERVICIO DE LA LISTA");
                errores.add(idServicio);
            } catch (RegistroException re) {
                log.error("SE HA PRODUCIDO UN ERROR AL EJECUTAR EL METODO DE BUSQUEDA DEL SERVICIO " + idServicio);
                log.debug("CONTINUAMOS CON EL SIGUIENTE SERVICIO DE LA LISTA");
                errores.add(idServicio);
            }

        }

        return buildEstructuraRetorno(asientosEncontrados, errores);
    }

    public void iniciarExpedienteAsiento(GeneralValueObject gVO, UsuarioValueObject usuarioVO, String[] params)
            throws RegistroException {

        String idServicio = (String)gVO.getAtributo("origenServicio");
       
        try {
            
             //No debería venir nunca vacío, pero lo controlamos
             if((idServicio!=null) && (!"".equals(idServicio))){
               
                AccesoRegistro registro = AccesoRegistroFactoria.getImpl(idServicio, prefijoPropiedad);
                registro.iniciarExpedienteAsiento(gVO, usuarioVO, params);
             }else
             {
                log.debug("El idServicio viene vacío. No se puede iniciar el expediente para la anotación: "+  (String)gVO.getAtributo("ejercicioAsiento")+"/"+(String)gVO.getAtributo("numeroAsiento"));
             }
            
        } catch (RegistroException re) {
            log.error("SE HA PRODUCIDO UN ERROR AL EJECUTAR EL METODO DE BUSQUEDA DEL SERVICIO " + idServicio);
            log.debug("CONTINUAMOS CON EL SIGUIENTE SERVICIO DE LA LISTA");
            throw re;
        }
    }

    private HashMap buildEstructuraRetorno(Vector asientos, Collection<String> errores,ArrayList<String> busquedaExtendidaDesactivada) {
        HashMap estructuraRetorno = new HashMap();
        estructuraRetorno.put("resultados", asientos);
        estructuraRetorno.put("errores", errores);
        estructuraRetorno.put("busquedaExtendidaDesactivada", busquedaExtendidaDesactivada);
        return estructuraRetorno;
    }
    
    private HashMap buildEstructuraRetorno(ArrayList<AsientoFichaExpedienteVO> asientos, Collection<String> errores) {
        HashMap estructuraRetorno = new HashMap();
        estructuraRetorno.put("resultados", asientos);
        estructuraRetorno.put("errores", errores);
        return estructuraRetorno;
    }

 /** 
     * Recupera el documento asociado a una anotación 
     * @param registroVO: RegistroValueObject
     * @param codDocumento: Código documento
     * @param params: Parámetros de conexión a la BBDD
     * @return DocumentoValueObject con los datos del documento
     */
    public DocumentoValueObject viewDocument(RegistroValueObject registroVO,int codDocumento, String[] params) throws RegistroException {

        String idServicio = registroVO.getIdServicioOrigen();

        try {
            AccesoRegistro registro = AccesoRegistroFactoria.getImpl(idServicio, prefijoPropiedad);            
            return registro.viewDocument(registroVO, codDocumento, params);
        } catch (RegistroException re) {
            log.error("SE HA PRODUCIDO UN ERROR AL EJECUTAR EL METODO DE BUSQUEDA DEL SERVICIO " + idServicio);
            log.debug("CONTINUAMOS CON EL SIGUIENTE SERVICIO DE LA LISTA");
            throw re;
        }
    }
    
     public  Vector obtenerInteresados(RegistroValueObject rVO, String[] params) throws RegistroException {

        String idServicio = rVO.getIdServicioOrigen();

        try {
            AccesoRegistro registro = AccesoRegistroFactoria.getImpl(idServicio, prefijoPropiedad);
            return registro.obtenerInteresados(rVO, params) ;
        } catch (RegistroException re) {
            log.error("SE HA PRODUCIDO UN ERROR AL EJECUTAR EL METODO DE BUSQUEDA DEL SERVICIO " + idServicio);
            log.debug("CONTINUAMOS CON EL SIGUIENTE SERVICIO DE LA LISTA");
            throw re;
        }
    }
}
