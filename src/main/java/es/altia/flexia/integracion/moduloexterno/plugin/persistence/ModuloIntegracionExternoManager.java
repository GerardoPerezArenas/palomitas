package es.altia.flexia.integracion.moduloexterno.plugin.persistence;

import es.altia.agora.business.integracionsw.PeticionSWVO;
import es.altia.agora.business.integracionsw.procesos.GestorEjecucionTramitacion;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.util.commons.BasicTypesOperations;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.manual.ModuloIntegracionExternoDAO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.EstructuraEtiquetaModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.TareasPendientesInicioTramiteVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.apache.log4j.Logger;

public class ModuloIntegracionExternoManager {

    private static ModuloIntegracionExternoManager instance = null;
    private Logger log = Logger.getLogger(ModuloIntegracionExternoManager.class);

    private ModuloIntegracionExternoManager(){
    }

    public static ModuloIntegracionExternoManager getInstance(){
        if(instance==null)
            instance = new ModuloIntegracionExternoManager();

        return instance;
    }


    /**
     * Comprueba si un tr�mite de un determinado expediente tiene tareas de inicio pendientes de ejecutar
     * @param codMunicipio: C�digo del municipio
     * @param codTramite: C�digo del tr�mite
     * @param ocurrenciaTramite: Ocurrencia del tr�mite
     * @param numExpediente: N� del expediente
     * @param params: Parametros de conexion BD
     * @return boolean
     */
    public boolean tieneTareasPendienteInicio(int codMunicipio,int codTramite,int ocurrenciaTramite,String numExpediente,String[] params) {
        boolean exito = false;
        Connection con = null;
        AdaptadorSQLBD adaptador = null;
        
        try{
            adaptador = new AdaptadorSQLBD(params);
            con = adaptador.getConnection();
            exito = ModuloIntegracionExternoDAO.getInstance().tieneTareasPendienteInicio(codMunicipio, codTramite, ocurrenciaTramite, numExpediente, con);

        }catch(BDException e){
            e.printStackTrace();
        }finally{
            try{
                adaptador.devolverConexion(con);
            }catch(BDException e){
                e.printStackTrace();
            }
        }

        return exito;
    }


    /**
     * Este m�todo ejecuta las tareas pendientes de inicio de una ocurrencia de un tr�mite de un determinado expediente
     * @param codMunicipio
     * @param codTramite
     * @param ocurrencia
     * @param ejercicio
     * @param codProcedimiento
     * @param numExpediente
     * @param params
     * @return  "OK" si todo ha ido bien o el mensaje con las operaciones que no se han podido ejecutar
     */
   public String ejecutarTareasPendientesInicioTramite(int codMunicipio,int codTramite,int ocurrencia,int ejercicio,String codProcedimiento,String numExpediente, String origenLlamada, String[] params){
        
        String mensaje = null;
        Connection con = null;
        AdaptadorSQLBD abd = null;
        try {
            
            abd = new AdaptadorSQLBD(params);
            con = abd.getConnection();

            log.debug(" ------------------ Inicio ModuloIntegracionExternoManager ");
            log.debug(" ---- codMunicipio: " + codMunicipio);
            log.debug(" ---- codTramite: " + codTramite);
            log.debug(" ---- ocurrencia: " + ocurrencia);
            log.debug(" ---- ejercicio: " + ejercicio);
            log.debug(" ---- codProcedimiento: " + codProcedimiento);
            log.debug(" ---- numExpediente: " + numExpediente);

            PeticionSWVO peticionSW = new PeticionSWVO(codMunicipio, codProcedimiento,
                    codTramite, false, false,true, numExpediente, ocurrencia, ejercicio, false, params); 
            peticionSW.setOrigenLlamada(origenLlamada);
            GestorEjecucionTramitacion launcher = new GestorEjecucionTramitacion();
            mensaje = launcher.ejecutarSWTramitacionTareasPendientesInicio(peticionSW,abd, con);
            log.debug(" El resultado de ejecutar las tareas pendientes " + mensaje);
            
        }catch(BDException e){
            e.printStackTrace();
        }       
        finally{
            try{
                abd.devolverConexion(con);
            }catch(BDException e){
                e.printStackTrace();
            }
        }
        return mensaje;
    }


     /**
     * Recupera las tareas pendientes de inicio de una ocurrencia de un determinado tr�mite
     * @param codMunicipio: C�digo del municipio
     * @param codTramite: C�digo del tr�mite
     * @param ocurrenciaTramite: Ocurrencia del tr�mite
     * @param numExpediente: N� del expediente
     * @param codigoIdiomaUsuario: C�digo del idioma del usuario en Flexia para poder recuperar los mensajes de error personalizados
     *
     * @param params: Parametros de conexion BD
     * @return ArrayList<TareasPendientesInicioTramiteVO>
     */
    public ArrayList<TareasPendientesInicioTramiteVO> getTareasPendientesInicio(int codMunicipio,int codTramite,int ocurrenciaTramite,String numExpediente,int codIdiomaUsuario,String[] params) {
        ArrayList<TareasPendientesInicioTramiteVO> tareas = null;
        Connection con = null;
        AdaptadorSQLBD adaptador = null;

        try{
            adaptador = new AdaptadorSQLBD(params);
            con = adaptador.getConnection();
            tareas = ModuloIntegracionExternoDAO.getInstance().getTareasPendientesInicio(codMunicipio,codTramite,ocurrenciaTramite,numExpediente,codIdiomaUsuario,con);

        }catch(BDException e){
            log.error("Error al obtener la conexi�n a la BD en getTareasPendientesInicio: " + e.getMessage());
        }finally{
            try{
                adaptador.devolverConexion(con);
            }catch(BDException e){
                e.printStackTrace();
            }
        }

        return tareas;
    }


     /**
     * Comprueba si una determinada operaci�n de un m�dulo externo o de un WS si tiene
     * @param codMunicipio: C�digo del municipio
     * @param codTramite: C�digo del tr�mite
     * @param orden: Orden
     * @param codProcedimiento: C�digo del procedimiento
     * @param con: Conexi�n a la BBDD
     * @return TareasPendientesInicioTramiteVO o null
     */
    public boolean tieneOperacionTareasPendientesAsociadas(int codMunicipio,int codTramite,int orden,String codProcedimiento,String[] params){
        Connection con = null;
        AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
        boolean exito = false;
        
        try{
            
            con = adapt.getConnection();            
            exito = ModuloIntegracionExternoDAO.getInstance().tieneOperacionTareasPendientesAsociadas(codMunicipio, codTramite, orden, codProcedimiento, con);
            
        }catch(BDException e){
            exito = false;
            log.error("Error en el m�todo tieneOperacionTareasPendientesAsociadas al recuperar una conexi�n a la BBDD: " + e.getMensaje());            
        }finally{
            try{
                if(adapt!=null) adapt.devolverConexion(con);
            }catch(BDException e){
                log.error("Error en el m�todo tieneOperacionTareasPendientesAsociadas al cerrar la conexi�n a la BBDD: " + e.getMensaje());
            }
        }        
        return exito;
    }

    
    
     public ArrayList<ValorCampoDesplegableModuloIntegracionVO> getValoresCampoConsultaDesplegable
            (String nombreTabla, String campoCodigo, String campoDescripcion, String[] params) throws SQLException {
          
        AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
        Connection con=null;
        ArrayList<ValorCampoDesplegableModuloIntegracionVO> resultado= new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        try{
            
            con = adapt.getConnection();            
            resultado = ModuloIntegracionExternoDAO.getInstance().getValoresCampoConsultaDesplegable(nombreTabla, campoCodigo, campoDescripcion, con);
            
        }catch(BDException e){
            resultado = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
            log.error("Error en el m�todo getValoresCampoConsultaDesplegable al recuperar una conexi�n a la BBDD: " + e.getMensaje());            
        }finally{
            try{
                if(adapt!=null) adapt.devolverConexion(con);
            }catch(BDException e){
                log.error("Error en el m�todo getValoresCampoConsultaDesplegable al cerrar la conexi�n a la BBDD: " + e.getMensaje());
            }
        }        
        return resultado;
     }
     
     /**
      * 
      * @param params Informaci�n para la conexi�n a la BD
      * @param etiqueta Informaci�n de la etiqueta para construir la sentencia SELECT y sobre el formato
      * en el que se devolver� la etiqueta seg�n su tipo
      * @param numExpediente n�mero del expediente del que estamos generando el documento
      * @param codTramite tr�mite en el cual estamos generando el documento
      * @param ocuTramite n�mero de ocurrencia del tr�mite en el cual estamos generando el documento
      * @return
      * @throws SQLException 
      */
     public String getValorDeEtiqueta( String[] params, EstructuraEtiquetaModuloIntegracionVO etiqueta, String numExpediente, String codTramite, String ocuTramite) throws SQLException {
        
        log.debug("getValorDeEtiqueta.BEGIN" ); 
        log.debug("getValorDeEtiqueta.Columna: "+ etiqueta.getNombreColumna() ); 
        log.debug("getValorDeEtiqueta.numExpediente: "+ numExpediente ); 
        log.debug("getValorDeEtiqueta.codTramite: "+ codTramite ); 
        log.debug("getValorDeEtiqueta.ocuTramite: "+ ocuTramite); 
        AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
        Connection con=null;
        String resultado="";
        //Necesitamos conocer el tipo de la etiqueta, para darle el formato adecuado
        Integer tipo;     
        try{
            
            con = adapt.getConnection();            
            
            resultado = ModuloIntegracionExternoDAO.getInstance().getValorDeEtiqueta(con,etiqueta,numExpediente,codTramite,ocuTramite);
          
       
      
        }catch(BDException e){
            log.error("Error en el m�todo getValorDeEtiqueta al recuperar una conexi�n a la BBDD: " + e.getMensaje());            
        }finally{
            try{
                if(adapt!=null) adapt.devolverConexion(con);
            }catch(BDException e){
                log.error("Error en el m�todo getValorDeEtiqueta al cerrar la conexi�n a la BBDD: " + e.getMensaje());
            }
        } 
        log.debug("getValorDeEtiqueta.END.Resultado devuelto: "+resultado ); 
        return resultado;
     }
     
     
     
}