package es.altia.flexia.expedientes.relacionados.plugin;

import es.altia.agora.business.sge.TramitacionValueObject;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.persistence.PermisoProcRestringidoManager;
import es.altia.agora.business.sge.persistence.TramitacionManager;
import es.altia.agora.business.sge.persistence.manual.TramitacionDAO;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.expedientes.relacionados.plugin.util.Conversor;
import es.altia.flexia.expedientes.relacionados.plugin.vo.ExpedienteRelacionadoVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 * Plugin de expedientes relacionados de Flexia para el registro
 * @author oscar.rodriguez
 */
public class PluginExpedientesRelacionadosFlexia implements PluginExpedientesRelacionados{
     private final String URL_FLEXIA_DEFECTO = "/sge/FichaExpediente.do";
     public static final String PLUGIN_ORIGEN  = "FLEXIA";
     private Logger log = Logger.getLogger(PluginExpedientesRelacionadosFlexia.class);

     public String cargar(Hashtable<String,String> valores,String url,String contexto){
         String dato = null;

         // Si no se indica la url es porque no está definida entonces se ha cargado el plugin de flexia y se indica la url
         // por defecto para este plugin
         if(url==null) url = URL_FLEXIA_DEFECTO;

        String[] dUrl = url.split("[?]");
        String urlUno = dUrl[0];

        StringBuffer salida = new StringBuffer();
        Enumeration<String> claves = valores.keys();
        while(claves.hasMoreElements()){
            String clave = claves.nextElement();
            salida.append(clave);
            salida.append("=");
            salida.append(valores.get(clave));
            salida.append("&");
        }

        String aux = salida.toString();
        dato         = contexto + urlUno + "?" + aux.substring(0,aux.length()-1);
        return dato;
     }

     /**
      * Da de alta un expediente relacionado para una anotación de registro. Es llamado cuando se inserta el expediente relacionado o se actualiza ese dato.
      * @param expediente: Datos del expediente relacionado necesario para poder relacionar el expediente con la anotación de registro.
      * @return Un int que indica el número de registros o insertados o actualizados
      */
    public int insertarExpedienteRelacionado(ExpedienteRelacionadoVO expediente){
        int salida = 0;

        TramitacionValueObject tvo = Conversor.expedienteRelacionadoVOToTramitacionValueObject(expediente);
        tvo.setOrigenExpedienteRelacionado(PLUGIN_ORIGEN);
        tvo.setUtilizarDatosExpedienteRelacionadoExterno(false);
        Connection connection        = expediente.getConnection();
        try{
            salida = TramitacionManager.getInstance().insertarExpediente(connection, tvo, 0,"1");
            
        }catch(TramitacionException e){
            e.printStackTrace();
            log.error(e.getMessage());
        }
        
        return salida;
    }


    /**
     * Comrpueba si un expediente y que un determinado usuario tenga
     * permiso sobre la unidad de inicio del expediente y sobre algunas de las unidades tramitadoras
     * @param expediente: ExpedienteRelacionadoVO que contiene los datos del expediente
     * @return 0 --> El expediente existe y el usuario tiene permiso sobre las unidad de inicio y sobre alguna de las
     *                       unidades tramitadoras
     *               1 --> No tiene permiso sobre la unidad de inicio del expediente pero si sobre alguna de las unidades tramitadoras del expediente
     *               2 --> Tiene permiso sobre la unidad de inicio del expediente pero no sobre alguna de las unidades tramitadoras del expediente
     *               3 --> No tiene permiso ni sobre la unidad de inicio del expediente ni sobre alguna de las unidades tramitadoras
     *               4 --> No existe el expediente en la base de datos
     *               5 --> Expediente mal relacionado
     */
    public int existeExpediente(ExpedienteRelacionadoVO expediente) throws TechnicalException{
        int salida = 4;

        String codOrganizacion = expediente.getCodigoOrganizacion();
        String codigoUsuario    = expediente.getCodigoUsuario();
        String numExpediente  = expediente.getNumExpedienteRelacionado();
        String codProcedimiento = "";
        String[] params           = expediente.getParams();
        Connection connection = null;
        AdaptadorSQLBD adaptador = null;

        TramitacionValueObject tvo = new TramitacionValueObject();
        tvo.setNumeroExpediente(numExpediente);
        String[] datosExp = numExpediente.split(ConstantesDatos.BARRA);
        if(datosExp!=null && datosExp.length==3){
            codProcedimiento = datosExp[1];
            log.debug("Código del procedimiento : " + codProcedimiento);
        }
        tvo.setCodProcedimiento(codProcedimiento);
       

        boolean continuar = true;
        int localizadoExpediente = -1;
        try{
            // Se obtiene la conexión a la base de datos
            if(expediente.getParams()!=null && expediente.getConnection()==null){
                adaptador = new AdaptadorSQLBD(params);
                connection                          = adaptador.getConnection();
            } else if (expediente.getConnection() != null) {
                connection = expediente.getConnection();
             // Se comprueba si el expediente existe y si se ha relacionado correctamente con el procedimiento adecuado
            }
            localizadoExpediente           = TramitacionManager.getInstance().localizaExpedienteDesdeRegistro(connection, tvo);

        log.debug(" Expediente localizado desde registro: " + localizadoExpediente);
        if(localizadoExpediente==-1){
            // No existe el expediente
            salida = 4;
            continuar = false;
            } else if (localizadoExpediente == 2) {
            // El expediente está mal relacionado
            salida = 5;
            continuar = false;
        }
        boolean estaRestringido = DefinicionProcedimientosManager.getInstance().estaProcedimientoRestringido(codProcedimiento, params);
        log.debug("El procedimiento está restringido: " + estaRestringido);

        if(estaRestringido){
                    continuar = PermisoProcRestringidoManager.getInstance().tieneUsuarioPermisoSobreProcedimientoRestringido(codigoUsuario, codOrganizacion, codProcedimiento, params);
                    log.debug("El usuario " + codigoUsuario + " de la organización " + codOrganizacion + " tiene  permiso sobre el procedimiento restringido: " + continuar);
                if (!continuar) {
                    salida = 6; //El usuario no tiene permiso sobre el procedimiento restringido

                }
        }
        if(continuar){            
            boolean permisoUnidadInicio = false;
            boolean permisoUnidadesTramitadoras = false;
                permisoUnidadInicio               = TramitacionDAO.getInstance().tienePermisoSobreUnidadInicio(Integer.parseInt(codOrganizacion),Integer.parseInt(codigoUsuario),numExpediente, connection);
                permisoUnidadesTramitadoras = TramitacionDAO.getInstance().tienePermisoUnidadesTramitadoras(numExpediente, Integer.parseInt(codigoUsuario),Integer.parseInt(codOrganizacion),connection);
            log.debug("permisoUnidadInicio " + permisoUnidadInicio);
            log.debug("permisoUnidadesTramitadoras " + permisoUnidadesTramitadoras);

            // Se indica cual será el código que devuelve el método
                if (permisoUnidadInicio && permisoUnidadesTramitadoras) {
                salida = 0;

                } else if (!permisoUnidadInicio && permisoUnidadesTramitadoras) {
                salida = 1;

                } else if (permisoUnidadInicio && !permisoUnidadesTramitadoras) {
                salida = 2;

                } else if (!permisoUnidadInicio && !permisoUnidadesTramitadoras) {
                salida = 3;

        }
            }          
        } catch (BDException bDException) {
            bDException.printStackTrace();
            salida = 4;
        } catch (TramitacionException tramitacionException) {
            tramitacionException.printStackTrace();
            salida = 4;
        } catch (NumberFormatException numberFormatException) {
            numberFormatException.printStackTrace();
            salida = 4;
        } finally {
        // Se comprueba si hay que cerrar la conexión con la base de datos de Flexia al haber realizado esta operación
                if (expediente!=null && expediente.isCerrarConexionFlexia()) SigpGeneralOperations.devolverConexion(adaptador, connection);
            }
        return salida;    
    }

    /**
     * Recupera el número de expediente relacionado a una determinada anotación de registro.
     * @param expediente: ExpedienteRelacionadoVO con los datos necesarios de la anotación de registro y del expediente con
     * el que se relaciona
     * @return
     */
    public ExpedienteRelacionadoVO getNumeroExpedienteRelacionado(ExpedienteRelacionadoVO expediente){
        String numExp = null;
        String codProc = null;
        String descProc = null;

        TramitacionValueObject tvo = new TramitacionValueObject();        
        tvo.setCodDepartamento(expediente.getCodigoDepartamento());
        tvo.setCodUnidadRegistro(expediente.getCodigoUnidadRegistro());
        tvo.setTipoRegistro(expediente.getTipoRegistro());
        tvo.setEjercicioRegistro(expediente.getEjercicioRegistro());
        tvo.setNumero(expediente.getNumeroRegistro());
        Connection con = expediente.getConnection();
        try {
            
            TramitacionManager.getInstance().getNumeroExpediente(tvo, con);
            /* Original
            numExp = tvo.getNumeroExpediente();
            codProc = tvo.getCodProcedimiento();
            descProc = tvo.getDescProcedimiento();
            // Se retorna el número de expediente, el código del procedimiento y la descripción del procedimiento
            if (expediente != null) {
                expediente.setNumExpedienteRelacionado(numExp);
                if (codProc != null && !codProc.equals("") && !codProc.equals("null")) {
                    expediente.setCodProcedimiento(codProc);
                    expediente.setNombreProcedimiento(descProc);
                }
            }
            */
            ArrayList<String> listaNumExpedientes = new ArrayList<String>();
            ArrayList<String> listaCodProcExpedientes = new ArrayList<String>();
            
            if(tvo.getExpedienteRelacionado() != null && tvo.getExpedienteRelacionado().getNumExpedientesRelacionados() != null
                    && tvo.getExpedienteRelacionado().getNumExpedientesRelacionados().size() > 0){
                listaNumExpedientes.addAll(tvo.getExpedienteRelacionado().getNumExpedientesRelacionados());
            }/*if(tvo.getExpedienteRelacionado().getNumExpedientesRelacionados() != null
                    && tvo.getExpedienteRelacionado().getNumExpedientesRelacionados().size() > 0)*/
                
            if(tvo.getExpedienteRelacionado() != null && tvo.getExpedienteRelacionado().getCodProcedimientoExpedientesRelacionados() != null
                    && tvo.getExpedienteRelacionado().getCodProcedimientoExpedientesRelacionados().size() > 0){
                listaCodProcExpedientes.addAll(tvo.getExpedienteRelacionado().getCodProcedimientoExpedientesRelacionados());
            }/*if( tvo.getExpedienteRelacionado().getCodProcedimientoExpedientesRelacionados() != null
                    && tvo.getExpedienteRelacionado().getCodProcedimientoExpedientesRelacionados().size() > 0)*/
                
            expediente.setNumExpedientesRelacionados(listaNumExpedientes);
            expediente.setCodProcedimientoExpedientesRelacionados(listaCodProcExpedientes);
        } catch (TramitacionException te) {
            numExp = null;
            te.printStackTrace();
        }
        return expediente;
    }


}
