package es.altia.flexia.expedientes.relacionados.plugin.artemis.facade;

import es.altia.agora.business.sge.TramitacionValueObject;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.persistence.TramitacionManager;
import es.altia.flexia.expedientes.relacionados.plugin.artemis.dao.FichaExpedientesArtemisDAO;
import es.altia.flexia.expedientes.relacionados.plugin.artemis.vo.ExpedienteArtemisVO;
import es.altia.flexia.expedientes.relacionados.plugin.util.ConstantesDatosExpRelacionados;
import es.altia.flexia.expedientes.relacionados.plugin.vo.ExpedienteRelacionadoVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

/**
 *
 * @author oscar.rodriguez
 */
public class FichaExpedienteArtemisManager {

    private static FichaExpedienteArtemisManager instance = null;
    private Logger log = Logger.getLogger(FichaExpedienteArtemisManager.class);


    private FichaExpedienteArtemisManager(){}

    public static FichaExpedienteArtemisManager getInstance(){
        if(instance==null)
            instance = new FichaExpedienteArtemisManager();

        return instance;
    }

   /**
         * Recupera la información a mostrar de un determinado expediente  de Artemis en su ficha de expediente
         * @param numExpediente: Número de expediente
         * @param codOrganizacion: Código de la organización
         * @return ExpedienteArtemisVO con toda la información recuperada
         */
    public ExpedienteArtemisVO getInfoExpediente(String numExpediente,String codOrganizacion){
        Connection connection = null;
        ExpedienteArtemisVO expediente = null;
        try{
            log.debug("getInfoExpediente() init");            
            ResourceBundle bRegistro = ResourceBundle.getBundle("Registro");
            String plugin                     = bRegistro.getString(codOrganizacion + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS);
            String gestor      = bRegistro.getString(codOrganizacion + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CON_BARRA + plugin + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CONEXION_GESTOR);
            String url           = bRegistro.getString(codOrganizacion + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CON_BARRA + plugin + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CONEXION_URL);
            String driver      = bRegistro.getString(codOrganizacion + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CON_BARRA + plugin + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CONEXION_DRIVER);
            String usuario    = bRegistro.getString(codOrganizacion + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CON_BARRA + plugin + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CONEXION_USUARIO);
            String password = bRegistro.getString(codOrganizacion + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CON_BARRA + plugin + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CONEXION_PASSWORD);
         
            String[] params = new String[7];          
            params[0] = gestor;
            params[1] = driver;
            params[2] = url;
            params[3] = usuario;
            params[4] = password;
          
            AdaptadorSQLBD adaptador = new AdaptadorSQLBD(params);
            connection = adaptador.getConnection();
            
            expediente = FichaExpedientesArtemisDAO.getInstance().getInfoExpediente(numExpediente, connection);
            
        }catch(Exception e){
            e.printStackTrace();
            expediente = null;
        }finally{
            try{
                if(connection!=null) connection.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return expediente;

    }



  /**
     * Comprueba si existe un determinado expediente
     * @param numExpediente: Número del expediente
     * @param codOrganizacion: Código de la organización
     * @return Un boolean
     */
    public boolean existeExpediente(String numExpediente,String codOrganizacion){
        Connection connection = null;
        boolean exito = false;

        try{
            // SE RECUPERA LA INFORMACIÓN DE CONEXIÓN A LA BASE DE DATOS DE ARTEMIS            
            ResourceBundle bRegistro = ResourceBundle.getBundle("Registro");
            String plugin                     = bRegistro.getString(codOrganizacion + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS);
            String gestor      = bRegistro.getString(codOrganizacion + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CON_BARRA + plugin + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CONEXION_GESTOR);
            String url           = bRegistro.getString(codOrganizacion + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CON_BARRA + plugin + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CONEXION_URL);
            String driver      = bRegistro.getString(codOrganizacion + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CON_BARRA + plugin + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CONEXION_DRIVER);
            String usuario    = bRegistro.getString(codOrganizacion + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CON_BARRA + plugin + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CONEXION_USUARIO);
            String password = bRegistro.getString(codOrganizacion + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CON_BARRA + plugin + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CONEXION_PASSWORD);
         
            String[] params = new String[7];
            params[0] = gestor;
            params[1] = driver;
            params[2] = url;
            params[3] = usuario;
            params[4] = password;

            AdaptadorSQLBD adaptador = new AdaptadorSQLBD(params);
            connection = adaptador.getConnection();
            exito = FichaExpedientesArtemisDAO.getInstance().existeExpediente(numExpediente, connection);
            
        }catch(Exception e){
            e.printStackTrace();
            exito = false;
        }finally{
            try{
                if(connection!=null) connection.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return exito;
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
            numExp = tvo.getNumeroExpediente();
            codProc = tvo.getCodProcedimiento();
            descProc = tvo.getDescProcedimiento();

        } catch (TramitacionException te) {
            numExp = null;
            te.printStackTrace();
        } finally {
            // Se retorna el número de expediente, el código del procedimiento y la descripción del procedimiento
            if (expediente != null) {
                expediente.setNumExpedienteRelacionado(numExp);
                if (codProc != null && !codProc.equals("") && !codProc.equals("null")) {
                    expediente.setCodProcedimiento(codProc);
                    expediente.setNombreProcedimiento(descProc);
                }
            }
        }

        return expediente;
    }
   
}