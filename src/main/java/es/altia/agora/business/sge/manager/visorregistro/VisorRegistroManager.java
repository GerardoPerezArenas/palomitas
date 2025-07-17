/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.agora.business.sge.manager.visorregistro;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.DocumentoValueObject;
import es.altia.agora.business.sge.TramitacionValueObject;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.webservice.registro.AccesoRegistro;
import es.altia.agora.webservice.registro.AccesoRegistroFactoria;
import es.altia.agora.webservice.registro.GestorAccesoRegistro;
import es.altia.agora.webservice.registro.exceptions.RegistroException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;

/**
 * Manager que se encargade aglutinar todas aquellas llamadas desde cualquier otro módulo
 * que requiera interactuar con el subsistema de Registro.
 * @author oscar.rodriguez
 */
public class VisorRegistroManager {

    private Vector<String> idsServicios;
    private String prefijoPropiedad;

    protected static Log log = LogFactory.getLog(GestorAccesoRegistro.class.getName());
    private final static String ID_SERVICIO_POR_DEFECTO = "SGE";
    private Logger m_Log = Logger.getLogger(VisorRegistroManager.class);
    private static VisorRegistroManager instance = null;

    private VisorRegistroManager(){
    }

    public static VisorRegistroManager getInstance(){
        if(instance==null)
            instance = new VisorRegistroManager();
        return instance;
    }

    private GestorAccesoRegistro crearGestor(int codOrganizacion) {

        Config m_ConfigTerceros = ConfigServiceHelper.getConfig("Registro");
        m_Log.debug("------ TramitacionManager.crearGestor codOrganizacion: " + codOrganizacion);
        /*
        * Buscamos los identificadores de los servicios de busqueda instalados en el fichero techserver.
        * El resultado de la llamada sera una cadena con todos los identificadores separados por ';'.
        * Si no se lee ningun identificador del fichero de configuración, se utiliza el servicio de busqeuda por
        * defecto (Identificador = SGE).
        */
        String prefijoOrg = "Registro/" + codOrganizacion + "/";
        String strIdsServicios = m_ConfigTerceros.getString(prefijoOrg + "serviciosDisp");
        m_Log.debug("TramitacionManager crearGestor prefijoOrg: " + prefijoOrg);
        m_Log.debug("TramitacionManager crearGestor strIdsServicios: " + strIdsServicios);

        if (strIdsServicios == null || "".equals(strIdsServicios.trim())) {
            strIdsServicios = ID_SERVICIO_POR_DEFECTO;
            m_Log.error("NO SE HA PODIDO RECUPERAR UNA CADENA DE IDENTIFICADORES PARA LOS SERVICIOS DE BUSQUEDA DE " +
                    "TERCEROS. INICIAMOS CON EL SERVICIO DE BUSQUEDA POR DEFECTO");
        }

        /*
         * Transformamos la cadena de identificadores en un vector y cremos una instancia de la clase encargada de
         * gestionar la busqueda.
         */
        Vector<String> idsServiciosBusq = new Vector<String>();
        StringTokenizer idsTokenizer = new StringTokenizer(strIdsServicios, ";");
        while (idsTokenizer.hasMoreTokens()) idsServiciosBusq.add(idsTokenizer.nextToken());
        m_Log.debug("SE HAN RECUPERADO LOS SERVICIOS: " + idsServiciosBusq);
        if (idsServiciosBusq.size() == 0) {
            idsServiciosBusq.add(ID_SERVICIO_POR_DEFECTO);
            m_Log.error("HA OCURRIDO UN ERROR AL PROCESAR LA CADENA DE IDENTIFICADORES DE SERVICIOS DE BUSQUEDA DE " +
                    "TERCEROS. INICIAMOS CON EL SERVICIO DE BUSQEUDA POR DEFECTO");
        }

        return new GestorAccesoRegistro(idsServiciosBusq, prefijoOrg);
    }

    public HashMap getAsientosEntradaRegistro(UsuarioValueObject uVO, TramitacionValueObject tVO, String[] params,
            String fechaDesde, String fechaHasta, String documento, String nombre, String apellido1, String apellido2, String codAsunto, String unidadRegistroAsunto, String tipoRegistroAsunto, String codUorDestino, String ejercicio, String numAnotacion, String codUorAnotacion
                ) throws TramitacionException {

        GestorAccesoRegistro gestor = crearGestor(uVO.getOrgCod());
        return gestor.getAsientosEntradaRegistro(uVO, tVO, params, fechaDesde, fechaHasta, documento, nombre, apellido1, apellido2, codAsunto, unidadRegistroAsunto, tipoRegistroAsunto, codUorDestino, ejercicio, numAnotacion, codUorAnotacion);

    }
    
    
    public HashMap getAsientosEntradaRegistroPluginTecnico(UsuarioValueObject uVO, TramitacionValueObject tVO, String[] params,
            String fechaDesde, String fechaHasta, String documento, String nombre, String apellido1, String apellido2, String codAsunto,
            String unidadRegistroAsunto, String tipoRegistroAsunto, String codUorDestino, String ejercicio, String numAnotacion, String codUorAnotacion,
            String codClasificacionAsunto, String unidadRegistroClasifAsunto, String docTecnicoRegistro) throws TramitacionException {
        m_Log.debug("getAsientosEntradaRegistroPluginTecnico");
        GestorAccesoRegistro gestor = crearGestor(uVO.getOrgCod());
        return gestor.getAsientosEntradaRegistroPluginTecnico(uVO, tVO, params, fechaDesde, fechaHasta, documento, nombre, apellido1, apellido2, codAsunto, unidadRegistroAsunto, tipoRegistroAsunto, codUorDestino, ejercicio, numAnotacion, codUorAnotacion,codClasificacionAsunto, unidadRegistroClasifAsunto, docTecnicoRegistro);

    }
    
    
    

//    public HashMap getAsientosExpedientesHistorico(UsuarioValueObject uVO, TramitacionValueObject tVO, String[] params,
//                                                  String fechaDesde, String fechaHasta, String ejercicio,String numAnotacion) throws TramitacionException {
//
//        GestorAccesoRegistro gestor = crearGestor(uVO.getOrgCod());
//        return gestor.getAsientosExpedientesHistorico(uVO, tVO, params, fechaDesde, fechaHasta,ejercicio,numAnotacion);
//
//    }

    public HashMap getAsientosExpedientesHistorico(UsuarioValueObject uVO, TramitacionValueObject tVO, String[] params,
                                                  String fechaDesde, String fechaHasta,String documento,String nombre,String primerApellido,String segundoApellido,String codAsuntoSeleccionado,String unidadRegistroAsuntoSeleccionado,String tipoRegistroAsuntoSeleccionado,String codUorInterno, String ejercicio, String numAnotacion,String codUorAnotacion) throws TramitacionException {

        GestorAccesoRegistro gestor = crearGestor(uVO.getOrgCod());
        return gestor.getAsientosExpedientesHistorico(uVO, tVO, params, fechaDesde, fechaHasta,documento,nombre,primerApellido,segundoApellido,codAsuntoSeleccionado,unidadRegistroAsuntoSeleccionado,tipoRegistroAsuntoSeleccionado,codUorInterno, ejercicio, numAnotacion,codUorAnotacion);

    }
    
    
    
        public HashMap getAsientosExpedientesHistoricoPluginTecnicoReferencia(UsuarioValueObject uVO, TramitacionValueObject tVO, String[] params,
                                                  String fechaDesde, String fechaHasta,String documento,String nombre,String primerApellido,String segundoApellido,String codAsuntoSeleccionado,String unidadRegistroAsuntoSeleccionado,String tipoRegistroAsuntoSeleccionado,String codUorInterno, String ejercicio, String numAnotacion,String codUorAnotacion,String codClasificacionAsunto, String unidadRegistroClasifAsunto, String docTecnicoRegistro) throws TramitacionException {

        GestorAccesoRegistro gestor = crearGestor(uVO.getOrgCod());
        return gestor.getAsientosExpedientesHistoricoPluginTecnicoReferencia(uVO, tVO, params, fechaDesde, fechaHasta,documento,nombre,primerApellido,segundoApellido,codAsuntoSeleccionado,unidadRegistroAsuntoSeleccionado,tipoRegistroAsuntoSeleccionado,codUorInterno, ejercicio, numAnotacion,codUorAnotacion,codClasificacionAsunto,unidadRegistroClasifAsunto,docTecnicoRegistro);

    }
    
    public RegistroValueObject getInfoAsientoConsulta(RegistroValueObject registroVO, String[] params) throws TramitacionException {

        try{
            GestorAccesoRegistro gestorRegistro = crearGestor(registroVO.getIdOrganizacion());
            return gestorRegistro.getInfoAsientoConsulta(registroVO, params);

        } catch (RegistroException re) {
            throw new TramitacionException(re.getMensaje(), re);
        }
    }

    public void cambiaEstadoAsiento(TramitacionValueObject tramitacionVO, UsuarioValueObject usuarioVO, int estado,
                                    String[] params) throws TramitacionException {

        try{
            GestorAccesoRegistro gestorRegistro = crearGestor(usuarioVO.getOrgCod());
            gestorRegistro.cambiaEstadoAsiento(tramitacionVO, usuarioVO, estado, params);
        } catch (RegistroException re) {
            throw new TramitacionException(re.getMensaje(), re);
        }
    }

    public void recuperarAsiento(TramitacionValueObject tramitacionVO, UsuarioValueObject usuarioVO, String[] params)
    throws TramitacionException {
        try{
            GestorAccesoRegistro gestorRegistro = crearGestor(usuarioVO.getOrgCod());
            gestorRegistro.recuperarAsiento(tramitacionVO, usuarioVO, params);
        } catch (RegistroException re) {
            throw new TramitacionException(re.getMensaje(), re);
        }
    }

    public void adjuntarExpedientesDesdeUnidadTramitadora(TramitacionValueObject tramitacionVO,
                                                          UsuarioValueObject usuarioVO, String[] params)
    throws TramitacionException {

        try{
            GestorAccesoRegistro gestorRegistro = crearGestor(usuarioVO.getOrgCod());
            gestorRegistro.adjuntarExpedientesDesdeUnidadTramitadora(tramitacionVO, usuarioVO, params);
        } catch (RegistroException re) {
            throw new TramitacionException(re.getMensaje(), re);
        }
    }

    public HashMap cargaListaAsientosExpediente(GeneralValueObject gVO, UsuarioValueObject usuarioVO, String[] params) throws TramitacionException {

        GestorAccesoRegistro gestorRegistro = crearGestor(usuarioVO.getOrgCod());
        return gestorRegistro.cargaListaAsientosExpediente(gVO, usuarioVO, params);
    }

    public void iniciarExpedienteAsiento(GeneralValueObject gVO, UsuarioValueObject usuarioVO, String[] params)
            throws TramitacionException {
        try{
            GestorAccesoRegistro gestorRegistro = crearGestor(usuarioVO.getOrgCod());
            // Se pasan los parámetros en el gVO porque es necesario para el lanzador de operaciones de WS o de operaciones de módulos externos
            // en el caso de que se ejecute alguna operación al iniciar el trámite de inicio
            gVO.setAtributo("paramsBD",params);
            gestorRegistro.iniciarExpedienteAsiento(gVO, usuarioVO, params);
        } catch (RegistroException re) {
            throw new TramitacionException(re.getMensaje(), re);
        }
    }
  
    /**
     * Recupera el documento asociado a una anotación
     * @param registroVO: RegistroValueObject
     * @param codDocumento: Código documento
     * @param params: Parámetros de conexión a la BBDD
     * @return DocumentoValueObject con los datos del documento
     */
    public DocumentoValueObject viewDocument(RegistroValueObject registroVO,int codDocumento, String[] params) throws TramitacionException {
        try{            
            GestorAccesoRegistro gestorRegistro = crearGestor(registroVO.getIdOrganizacion());
            return gestorRegistro.viewDocument(registroVO, codDocumento, params);
        } catch (RegistroException re) {
            throw new TramitacionException(re.getMensaje(), re);
        }   
    }
    
      public  Vector getInteresados(RegistroValueObject rVO, String[] params) throws RegistroException {


        try {
             GestorAccesoRegistro gestorRegistro = crearGestor(rVO.getIdOrganizacion());
            return gestorRegistro.obtenerInteresados(rVO, params) ;
        } catch (RegistroException re) {
            log.error("SE HA PRODUCIDO UN ERROR AL EJECUTAR EL METODO DE BUSQUEDA DEL SERVICIO " + rVO.getIdOrganizacion());
            log.error("CONTINUAMOS CON EL SIGUIENTE SERVICIO DE LA LISTA");
            throw re;
        }
    }
}