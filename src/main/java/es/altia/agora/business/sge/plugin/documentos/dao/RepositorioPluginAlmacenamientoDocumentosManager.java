package es.altia.agora.business.sge.plugin.documentos.dao;

import es.altia.agora.business.administracion.ParametrosBDVO;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.vo.PluginAlmacenamientoVO;
import es.altia.agora.business.sge.plugin.documentos.vo.RepositorioDocumentacionDocumentosExternosPortafirmasVO;
import es.altia.agora.business.sge.plugin.documentos.vo.RepositorioDocumentacionProcedimientoVO;
import es.altia.agora.business.sge.plugin.documentos.vo.RepositorioDocumentacionRegistroVO;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.util.cache.CacheDatosFactoria;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.SortedMap;
import org.apache.log4j.Logger;


public class RepositorioPluginAlmacenamientoDocumentosManager {

    private Logger log = Logger.getLogger(RepositorioPluginAlmacenamientoDocumentosManager.class);
    
    
    public Hashtable<String,AlmacenDocumento> getPluginAlmacenamientoDocumentoProcedimiento(String codOrganizacion){
        
        Hashtable<String,AlmacenDocumento> salida = new Hashtable<String, AlmacenDocumento>();
        ArrayList<RepositorioDocumentacionProcedimientoVO> lista = null;        
        Connection con = null;
        
        try{
            con = getConnection(codOrganizacion);
            RepositorioPluginAlmacenamientoDocumentosDAO repoDAO = new RepositorioPluginAlmacenamientoDocumentosDAO();
            lista  = repoDAO.getPluginAlmacenamientoDocumentoProcedimiento(con);
            
            for(RepositorioDocumentacionProcedimientoVO repo: lista){
                String codProcedimiento = repo.getCodProcedimiento();
                String sImplClass        = repo.getImplClassPlugin();
                log.debug("codProcedimiento: " + codProcedimiento + ",implClass: " + sImplClass);
                
                Class clase = Class.forName(sImplClass);                               
                salida.put(codProcedimiento,(AlmacenDocumento)clase.newInstance());                
            }
            
        }catch(Exception e){
           log.error("Error al recuperar listado de plugin de almacenamiento por procedimiento: " + e.getMessage());
           e.printStackTrace();           
            
        }finally{
            try{
                if(con!=null) con.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return salida;
    }
    
    
    
    public AlmacenDocumento getPluginAlmacenamientoRegistro(String codOrganizacion){
        
        AlmacenDocumento salida = null;       
        ArrayList<RepositorioDocumentacionRegistroVO> lista = new ArrayList<RepositorioDocumentacionRegistroVO>();
        Connection con = null;
        
        try{
            con = getConnection(codOrganizacion);
            RepositorioPluginAlmacenamientoDocumentosDAO repoDAO = new RepositorioPluginAlmacenamientoDocumentosDAO();
            lista = repoDAO.getPluginAlmacenamientoDocumentoRegistro(con);
            
            if(lista!=null && lista.size()==1){
                
                String sImplClass = lista.get(0).getImplClassPlugin();
                Class clase = Class.forName(sImplClass);
                salida = (AlmacenDocumento)clase.newInstance();
            }
            
                        
        }catch(Exception e){
           log.error("Error al recuperar listado de plugin de almacenamiento por procedimiento: " + e.getMessage());
           e.printStackTrace();           
            
        }finally{
            try{
                if(con!=null) con.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return salida;
    }
    
    
    
    public AlmacenDocumento getPluginAlmacenamientoDocumentosExternosPortafirmas(String codOrganizacion){
        
        AlmacenDocumento salida = null;       
        ArrayList<RepositorioDocumentacionDocumentosExternosPortafirmasVO> lista = new ArrayList<RepositorioDocumentacionDocumentosExternosPortafirmasVO>();
        Connection con = null;
        
        try{
            con = getConnection(codOrganizacion);
            RepositorioPluginAlmacenamientoDocumentosDAO repoDAO = new RepositorioPluginAlmacenamientoDocumentosDAO();
            lista = repoDAO.getPluginAlmacenamientoDocumentosExternosPortafirmas(con);
            
            if(lista!=null && lista.size()==1){
                
                String sImplClass = lista.get(0).getImplClassPlugin();
                Class clase = Class.forName(sImplClass);
                salida = (AlmacenDocumento)clase.newInstance();
            }
            
                        
        }catch(Exception e){
           log.error("Error al recuperar listado de plugin de almacenamiento por procedimiento: " + e.getMessage());
           e.printStackTrace();           
            
        }finally{
            try{
                if(con!=null) con.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return salida;
    }
    
    
   /**
     * Operación que recupera los datos de conexión a la BBDD
     * @param codOrganizacion
     * @return AdaptadorSQLBD
     */    
    private Connection getConnection(String codOrganizacion){
        if(log.isDebugEnabled()) log.debug("getConnection ( codOrganizacion = " + codOrganizacion + " ) : BEGIN");
        Connection conexion = null;
        String jndi = null;
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String[] salida = null;
        AdaptadorSQLBD adapt = null;
            
        synchronized (this) {
            SortedMap<ArrayList<String>,ParametrosBDVO> listaParametrosBD = (SortedMap<ArrayList<String>,ParametrosBDVO>)CacheDatosFactoria.getImplParametrosBD().getDatos();
            if (listaParametrosBD!=null && !listaParametrosBD.isEmpty()) {
                for(Map.Entry<ArrayList<String>,ParametrosBDVO> entry : listaParametrosBD.entrySet()) {
                    ParametrosBDVO parametrosBD = entry.getValue();
                    if (parametrosBD.getCodOrganizacion() == Integer.parseInt(codOrganizacion) && 
                            parametrosBD.getCodAplicacion() == ConstantesDatos.APP_GESTION_EXPEDIENTES){
                        jndi = parametrosBD.getJndi();
                        break;
                    }
                }
                if (jndi!=null && gestor!=null && !"".equals(jndi) && !"".equals(gestor)){
                    salida = new String[7];
                    salida[0] = gestor;
                    salida[1] = "";
                    salida[2] = "";
                    salida[3] = "";
                    salida[4] = "";
                    salida[5] = "";
                    salida[6] = jndi;
                    adapt = new AdaptadorSQLBD(salida);
                    try {
                        conexion = adapt.getConnection();
                    } catch (BDException ex) {
                        ex.printStackTrace();
                    }
                }            
            }            
        }
        return conexion;
     }//getConnection
    
    
    
    /********************* NUEVO *******************************/     
    
    /**
     * Recupera la lista de plugins de almacenamiento disponibles en la tabla ALMACEN_DOC_DISPONIBLES
     * @param params: String[] que contiene los parámetros de conexión a la BBDD
     * @return ArrayList<PluginAlmacenamientoVO>
     */
    public ArrayList<PluginAlmacenamientoVO> getPluginAlmacenamientoDisponible(String[] params){
        ArrayList<PluginAlmacenamientoVO> lista = new ArrayList<PluginAlmacenamientoVO>();
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        
        try{            
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            RepositorioPluginAlmacenamientoDocumentosDAO repoDAO = new RepositorioPluginAlmacenamientoDocumentosDAO();
            lista = repoDAO.getPluginAlmacenamientoDisponible(con);
                        
        }catch(Exception e){
           log.error("Error al recuperar la lista de plugin de almacenamiento disponibles: " + e.getMessage());
           e.printStackTrace();           
            
        }finally{
            try{
                if(con!=null) con.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return lista;
    }
    
    
    /**
     * Recupera la información necesaria para carga la pantalla de mantenimiento de asignación de plugin
     * de almacenamiento a procedimientos
     * @param params: Parámetros de conexión a BBDD
     * @return 
     */    
    public Hashtable<String,Object> getInfoCargaPantallaMantenimientoPluginProcedimiento(String[] params, boolean indicarDigit){
        
        Hashtable<String,Object> salida = new Hashtable<String, Object>();
        ArrayList<RepositorioDocumentacionProcedimientoVO> asignacionPluginProcedimiento = new ArrayList<RepositorioDocumentacionProcedimientoVO>();
        ArrayList<PluginAlmacenamientoVO> lista = new ArrayList<PluginAlmacenamientoVO>();
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        
        try{            
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            RepositorioPluginAlmacenamientoDocumentosDAO repoDAO = new RepositorioPluginAlmacenamientoDocumentosDAO();
            lista = repoDAO.getPluginAlmacenamientoDisponible(con);
                        
            Hashtable<Integer,PluginAlmacenamientoVO> hPlugin = new Hashtable<Integer, PluginAlmacenamientoVO>();
            for(int i=0;lista!=null && i<lista.size();i++){                
                hPlugin.put(lista.get(i).getId(),lista.get(i));
            }
            
            asignacionPluginProcedimiento = repoDAO.getListadoProcedimientosPlugin(hPlugin,con,indicarDigit);            
            salida.put("listaPlugin",lista);
            salida.put("asignacionPluginProcedimiento",asignacionPluginProcedimiento);
                        
        }catch(Exception e){
           log.error("Error al recuperar la lista de plugin de almacenamiento disponibles: " + e.getMessage());
           e.printStackTrace();           
            
        }finally{
            try{
                if(con!=null) con.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return salida;
    }
    
    
    
    public void grabarPluginPorProcedimiento(int codOrganizacion,ArrayList<RepositorioDocumentacionProcedimientoVO> repositorio,String tieneDigitalizacion,String[] params) throws BDException,TechnicalException{        
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);
                   
            RepositorioPluginAlmacenamientoDocumentosDAO dao = new RepositorioPluginAlmacenamientoDocumentosDAO();
            dao.grabarPluginPorProcedimiento(repositorio,params[0],tieneDigitalizacion,con);
            
            adapt.finTransaccion(con);
            
            // Se actualiza la cache co nlso datos de los plugin de procedimiento
            
            Hashtable<String,AlmacenDocumento> listaPlugin = new Hashtable<String, AlmacenDocumento>();
            for(int i=0;i<repositorio.size();i++){
                RepositorioDocumentacionProcedimientoVO repo = repositorio.get(i);
                
                String codProcedimiento = repo.getCodProcedimiento();                
                String implClass = repo.getImplClassPlugin();
                                
                Class clase = Class.forName(implClass);
                AlmacenDocumento plugin = (AlmacenDocumento)clase.newInstance();                
                listaPlugin.put(codProcedimiento,plugin);
                
            }// for

            // Se actualiza la cache con la lista de plugin de almacenamiento para cada procedimiento
            AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(codOrganizacion)).recargarPluginProcedimiento(Integer.toString(codOrganizacion),listaPlugin);
                    
        }catch(BDException e){
            try{
                adapt.rollBack(con);
            }catch(Exception f){
                e.printStackTrace();
            }
            log.error("Error al obtener una conexión a la base de datos para grabar los plugins de almacenamiento asociados a cada procedimiento: " + e.getMessage());
            throw e;
        }catch(Exception e){
            try{
                adapt.rollBack(con);
            }catch(Exception f){
                e.printStackTrace();
            }
            log.error("Error al grabar los plugins de almacenamiento asociados a cada procedimiento: " + e.getMessage());
            throw new TechnicalException("Error al grabar los plugins de almacenamiento asociados a cada procedimiento: " + e.getMessage(),e);
        }
        finally{
            try{
                if(con!=null) con.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }        
    }
    
    
    
    /**
     * Recupera la información necesaria para poder mostrar en la pantalla de mantemiento de plugin de almacenamiento
     * de documentos a utilizar en el Registro de Entrada/Salida
     * @param params: Parámetros de conexión a la BBDD
     * @return Hashtable<String,Object>
     */
    public Hashtable<String,Object> getInfoCargaPantallaMantenimientoPluginRegistro(String[] params){        
        Hashtable<String,Object> salida = new Hashtable<String, Object>();
        
        ArrayList<RepositorioDocumentacionRegistroVO> asignacionPluginRegistro = new ArrayList<RepositorioDocumentacionRegistroVO>();
        ArrayList<PluginAlmacenamientoVO> lista = new ArrayList<PluginAlmacenamientoVO>();
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        
        try{            
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            RepositorioPluginAlmacenamientoDocumentosDAO repoDAO = new RepositorioPluginAlmacenamientoDocumentosDAO();
            lista = repoDAO.getPluginAlmacenamientoDisponible(con);
                        
            Hashtable<Integer,PluginAlmacenamientoVO> hPlugin = new Hashtable<Integer, PluginAlmacenamientoVO>();
            for(int i=0;lista!=null && i<lista.size();i++){                
                hPlugin.put(lista.get(i).getId(),lista.get(i));
            }
            
            asignacionPluginRegistro = repoDAO.getListadoPluginRegistro(hPlugin,con);            
            salida.put("listaPlugin",lista);
            salida.put("asignacionPluginRegistro",asignacionPluginRegistro);
                        
        }catch(Exception e){
           log.error("Error al recuperar la lista de plugin de almacenamiento disponibles: " + e.getMessage());
           e.printStackTrace();           
            
        }finally{
            try{
                if(con!=null) con.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return salida;
    }
     

    
    public void grabarPluginPorRegistro(int codOrganizacion,ArrayList<RepositorioDocumentacionRegistroVO> repositorio,String[] params) throws BDException,TechnicalException{        
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);
                   
            RepositorioPluginAlmacenamientoDocumentosDAO dao = new RepositorioPluginAlmacenamientoDocumentosDAO();
            dao.grabarPluginPorRegistro(repositorio,params[0], con);
            
            adapt.finTransaccion(con);
            
            // Se actualiza la cache de AlmacenDocumentoTramitacionFactoria con el plugin
            // que se invocará para el almacenamiento de documentos en el Registro de Entrada/Salida
            for(int i=0;i<repositorio.size();i++){
                RepositorioDocumentacionRegistroVO repo = repositorio.get(i);                
                String implClass = repo.getImplClassPlugin();                                
                Class clase = Class.forName(implClass);                
                // Se actualiza la cache con la lista de plugin de almacenamiento para cada procedimiento
                AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(codOrganizacion)).recargarPluginRegistro(Integer.toString(codOrganizacion),(AlmacenDocumento)clase.newInstance());                
            }// for
                    
        }catch(BDException e){
            try{
                adapt.rollBack(con);
            }catch(Exception f){
                e.printStackTrace();
            }
            log.error("Error al obtener una conexión a la base de datos para grabar los plugins de almacenamiento asociados a cada procedimiento: " + e.getMessage());
            throw e;
        }catch(Exception e){
            try{
                adapt.rollBack(con);
            }catch(Exception f){
                e.printStackTrace();
            }
            log.error("Error al grabar los plugins de almacenamiento asociados a cada procedimiento: " + e.getMessage());
            throw new TechnicalException("Error al grabar los plugins de almacenamiento asociados a cada procedimiento: " + e.getMessage(),e);
        }
        finally{
            try{
                if(con!=null) con.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }        
    }
     
}
