package es.altia.agora.business.sge.persistence;

import es.altia.agora.business.administracion.mantenimiento.CampoDesplegableVO;
import es.altia.agora.business.administracion.mantenimiento.CamposListadoParametrizablesProcedimientoVO;
import es.altia.agora.business.administracion.mantenimiento.CriterioBusquedaPendientesVO;
import es.altia.agora.business.administracion.mantenimiento.TipoDocumentoVO;
import es.altia.agora.business.sge.CampoListadoPendientesProcedimientoVO;
import es.altia.agora.business.sge.persistence.manual.CamposListadoPendientesProcedimientoDAO;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.log4j.Logger;
import es.altia.agora.business.sge.CampoSuplementarioVO;


/**
 *
 * @author Administrador
 */
public class CamposListadoPendientesProcedimientoManager {
    private static CamposListadoPendientesProcedimientoManager instance = null;
    private Logger log = Logger.getLogger(CamposListadoPendientesProcedimientoManager.class);

    private CamposListadoPendientesProcedimientoManager(){
    }

    public static CamposListadoPendientesProcedimientoManager getInstance(){
        if(instance==null)
            instance = new CamposListadoPendientesProcedimientoManager();

        return instance;
    }


    public ArrayList<CampoListadoPendientesProcedimientoVO> getCamposDisponibles(String codProcedimiento,String codMunicipio,String[] params){        
        ArrayList<CampoListadoPendientesProcedimientoVO> campos = null;
        Connection con = null;

        try{
            log.debug("getCamposDisponibles ========>");
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            campos = CamposListadoPendientesProcedimientoDAO.getInstance().getCamposDisponibles(codProcedimiento, codMunicipio, con);
            log.debug(" El número de campos disponibles es: " + campos.size());
            log.debug("<======== getCamposDisponibles ");

        }catch(BDException e){
            e.printStackTrace();
        }finally{
            try{
                if(con!=null) con.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return campos;
    }// getCamposDisponibles





    /**
     * Comprueba si un procedimiento tiene definida una vista propia de expedientes pendientes
     * @param codProcedimiento: Código del procedimiento
     * @param codMunicipio: Código del municipio
     * @param params: Parámetros de conexión a la BBDD
     * @return True si tiene y false en caso contrario */
    public boolean tieneProcedimientoVistaExpedientesPendientes(String codProcedimiento,int codMunicipio,String[] params){
        boolean exito = false;
        Connection con = null;
        AdaptadorSQLBD adapt = null;

        try{
            log.debug("tieneProcedimientoVistaExpedientesPendientes ========>");
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            exito = CamposListadoPendientesProcedimientoDAO.getInstance().tieneProcedimientoVistaExpedientesPendientes(codProcedimiento,codMunicipio,con);
            log.debug("exito : " + exito);
            log.debug("<======== tieneProcedimientoVistaExpedientesPendientes ");

        }catch(BDException e){
            e.printStackTrace();
        }finally{

            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                e.printStackTrace();
            }
        }        
        return exito;
                
    }// tieneProcedimientoVistaExpedientesPendientes


   /**
     * Recupera los campos del listado de expedientes pendientes de un determinado procedimiento
     * @param codProcedimiento: Código del procedimiento
     * @param codMunicipio: Código del municipio     
     * @param params: Parámetros de conexión a la base de datos
     * @return Vector<CamposListadosParametrizablesVO>
     */
    public Vector<CamposListadoParametrizablesProcedimientoVO> getCamposListado(String codProcedimiento,int codMunicipio,int codIdioma,String[] params){
        Vector<CamposListadoParametrizablesProcedimientoVO> campos = new Vector<CamposListadoParametrizablesProcedimientoVO>();
        AdaptadorSQLBD adaptador = null;
        Connection con = null;
        try{
            log.debug("CamposListadoPendientesProcedimientoManager ===============>");
            adaptador = new AdaptadorSQLBD(params);
            con = adaptador.getConnection();
            campos = CamposListadoPendientesProcedimientoDAO.getInstance().getCamposListado(codProcedimiento, codMunicipio,codIdioma,con);
            log.debug(" " + campos.size());
            log.debug("<=============== CamposListadoPendientesProcedimientoManager");
            
        }catch(BDException e){
            e.printStackTrace();
        }finally{
            try{
                adaptador.devolverConexion(con);
            }catch(BDException e){
                e.printStackTrace();
            }
        }
        return campos;
    }


    public String getValorCampoSuplementario(String numExpediente,String ejercicio,String codMunicipio,String codigoCampo,int tipoCampo,String[] params){
        AdaptadorSQLBD adapt = null;
        Connection conexion = null;
        String valor = null;
        try{
           log.debug(this.getClass().getName() + ".getValorCampoSuplementario =============================>");
           adapt = new AdaptadorSQLBD(params);
           conexion = adapt.getConnection();
           valor = CamposListadoPendientesProcedimientoDAO.getInstance().getValorCampoSuplementario(numExpediente, ejercicio, codMunicipio, codigoCampo, tipoCampo, conexion);
           log.debug(this.getClass().getName() + ".getValorCampoSuplementario: La salida es " + valor + " <=============================");
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                adapt.devolverConexion(conexion);
            }catch(BDException e){
                e.printStackTrace();
            }
        }

        return valor;
    }
    
    
    
    
  public Hashtable <String,String> getValorCampoSuplementario2(String numExpediente,String ejercicio,String codMunicipio,Vector<CampoSuplementarioVO> camposSuplementarios,String[] params){
        AdaptadorSQLBD adapt = null;
        Connection conexion = null;
        String valor = null; 
         Hashtable<String,String> campos = new Hashtable<String,String>();
        try{
           log.debug(this.getClass().getName() + ".getValorCampoSuplementario =============================>");
           adapt = new AdaptadorSQLBD(params);
           
           campos = CamposListadoPendientesProcedimientoDAO.getInstance().getValorCampoSuplementario2(numExpediente, ejercicio, codMunicipio,camposSuplementarios,adapt);
           log.debug(this.getClass().getName() + ".getValorCampoSuplementario: La salida es " + valor + " <=============================");
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                adapt.devolverConexion(conexion);
            }catch(BDException e){
                e.printStackTrace();
            }
        }

        return campos;
    }



  /**
     * Recupera los campos que pasan a ser criterios de búsqueda en la lista desplegable que se carga en la pantalla de expedientes pendientes
     * @param codProcedimiento: Código del procedimiento. Si esta a null se devuelven sólo los campos fijos, si no es nulo, se recupera también los campos suplementarios
     * de procedimiento de tipo texto corto, fecha, numérico y desplegable
     * @param codMunicipio: Código del municipio
     * @param idioma: Código del idioma del usuario que está utilizando la aplicación
     * @param params: Parámetros de conexión
     * @return ArrayList<CriterioBusquedaPendientesVO>
     */
    public ArrayList<CriterioBusquedaPendientesVO> getCriteriosBusquedaExpedientesPendientes(String codProcedimiento,String codMunicipio,int idioma,String[] params){
        ArrayList<CriterioBusquedaPendientesVO> criterios = new ArrayList<CriterioBusquedaPendientesVO>();
        AdaptadorSQLBD adaptador = null;
        Connection con = null;

        try{
            TraductorAplicacionBean traductor = new TraductorAplicacionBean();
            traductor.setIdi_cod(idioma);
            traductor.setApl_cod(ConstantesDatos.APP_GESTION_EXPEDIENTES);

            String aux = "";
            /** SE RECUPERAN LOS CAMPOS FIJOS **/
            // Número de expediente
            CriterioBusquedaPendientesVO c1 = new CriterioBusquedaPendientesVO();            
            c1.setCodigo("1");
            try{
               aux = traductor.getDescripcion("etiqNumExpedCorto").toUpperCase();
            }catch(Exception e){
                aux = "";
                e.printStackTrace();
            }
            c1.setNombre(aux);
            c1.setTipoCampoFijo(ConstantesDatos.TIPO_DATO_CAMPO_FIJO_NUMEXPEDIENTE);
            c1.setCampoSuplementario(false);
            criterios.add(c1);

            // Identificación o documento
            CriterioBusquedaPendientesVO c2 = new CriterioBusquedaPendientesVO();            
            c2.setCodigo("2");
            try{
               aux = traductor.getDescripcion("etiqIdentificacion").toUpperCase();
            }catch(Exception e){
                aux = "";
                e.printStackTrace();
            }
            c2.setNombre(aux);
            c2.setTipoCampoFijo(ConstantesDatos.TIPO_DATO_CAMPO_FIJO_DOCUMENTO);
            c2.setCampoSuplementario(false);
            criterios.add(c2);

            // Nombre del interesado
            CriterioBusquedaPendientesVO c3 = new CriterioBusquedaPendientesVO();
            c3.setCodigo("3");
            try{
               aux = traductor.getDescripcion("etiq_IntPrin").toUpperCase();
            }catch(Exception e){
                aux = "";
                e.printStackTrace();
            }
            c3.setNombre(aux);
            c3.setTipoCampoFijo(ConstantesDatos.TIPO_DATO_CAMPO_FIJO_NOMBREINTERESADO);
            c3.setCampoSuplementario(false);
            criterios.add(c3);

            // Fecha de inicio del expediente
            CriterioBusquedaPendientesVO c4 = new CriterioBusquedaPendientesVO();
            c4.setCodigo("4");
            try{
               aux = traductor.getDescripcion("gEtiq_fecIni").toUpperCase();
            }catch(Exception e){
                aux = "";
                e.printStackTrace();
            }
            c4.setNombre(aux);
            c4.setTipoCampoFijo(ConstantesDatos.TIPO_DATO_CAMPO_FIJO_FECHA);
            c4.setCampoSuplementario(false);
            criterios.add(c4);

            // Asunto
            CriterioBusquedaPendientesVO c5 = new CriterioBusquedaPendientesVO();
            c5.setCodigo("5");
            try{
               aux = traductor.getDescripcion("rotulo_asunto").toUpperCase();
            }catch(Exception e){
                aux = "";
                e.printStackTrace();
            }
            c5.setNombre(aux);
            c5.setTipoCampoFijo(ConstantesDatos.TIPO_DATO_CAMPO_FIJO_TEXTO);
            c5.setCampoSuplementario(false);
            criterios.add(c5);

            // Observaciones
            CriterioBusquedaPendientesVO c6 = new CriterioBusquedaPendientesVO();
            c6.setCodigo("6");
            try{
               aux = traductor.getDescripcion("etiqObs").toUpperCase();
            }catch(Exception e){
                aux = "";
                e.printStackTrace();
            }
            c6.setNombre(aux);
            c6.setTipoCampoFijo(ConstantesDatos.TIPO_DATO_CAMPO_FIJO_TEXTO);
            c6.setCampoSuplementario(false);
            criterios.add(c6);
         
            /** SE RECUPERAN LOS CAMPOS SUPLEMENTARIOS DE PROCEDIMIENTO DE LOS TIPOS TEXTO CORTO, FECHA, NUMÉRICO Y DESPLEGABLE **/
            if(codProcedimiento!=null){            
                adaptador = new AdaptadorSQLBD(params);
                con = adaptador.getConnection();
                criterios = CamposListadoPendientesProcedimientoDAO.getInstance().getCamposSuplemProcCriterioBusqueda(codProcedimiento, codMunicipio, criterios, con);
                adaptador.devolverConexion(con);
            }
            
        }catch(BDException e){
            e.printStackTrace();
        }finally{
            try{
                if(adaptador!=null){
                    adaptador.devolverConexion(con);
                }
            }catch(BDException e){
                e.printStackTrace();
            }
        }

        return criterios;
    }




     public ArrayList<CampoDesplegableVO> getValoresDesplegable(String codigoCampo,String[] params){
         ArrayList<CampoDesplegableVO> campos = new ArrayList<CampoDesplegableVO>();
         Connection con = null;
         AdaptadorSQLBD adaptador = null;
         try{
             adaptador = new AdaptadorSQLBD(params);
             con = adaptador.getConnection();
             campos = CamposListadoPendientesProcedimientoDAO.getInstance().getValoresDesplegable(codigoCampo, con);

         }catch(BDException e){
             e.printStackTrace();
         }finally{
             try{
                 adaptador.devolverConexion(con);
             }catch(BDException e){
                 e.printStackTrace();
             }
         }

         return campos;
     }


     public ArrayList<TipoDocumentoVO> getTiposDocumento(String[] params){
         ArrayList<TipoDocumentoVO> campos = new ArrayList<TipoDocumentoVO>();
         Connection con = null;
         AdaptadorSQLBD adaptador = null;
         try{
             adaptador = new AdaptadorSQLBD(params);
             con = adaptador.getConnection();
             campos = CamposListadoPendientesProcedimientoDAO.getInstance().getTiposDocumento(con);

         }catch(BDException e){
             e.printStackTrace();
         }finally{
             try{
                 adaptador.devolverConexion(con);
             }catch(BDException e){
                 e.printStackTrace();
             }
         }

         return campos;
     }
}