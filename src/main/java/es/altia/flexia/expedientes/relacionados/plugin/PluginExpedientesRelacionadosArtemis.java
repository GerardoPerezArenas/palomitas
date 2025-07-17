package es.altia.flexia.expedientes.relacionados.plugin;

import es.altia.agora.business.sge.TramitacionValueObject;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.persistence.TramitacionManager;
import es.altia.flexia.expedientes.relacionados.plugin.artemis.facade.FichaExpedienteArtemisManager;
import es.altia.flexia.expedientes.relacionados.plugin.util.Conversor;
import es.altia.flexia.expedientes.relacionados.plugin.vo.ExpedienteRelacionadoVO;
import es.altia.flexia.expedientes.relacionados.plugin.artemis.vo.ExpedienteArtemisVO;
import es.altia.flexia.expedientes.relacionados.plugin.util.ConstantesDatosExpRelacionados;
import java.sql.Connection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

/**
 * Plugin de expedientes relacionados para el registro en la integración para Artemis
 * @author oscar.rodriguez
 */
public class PluginExpedientesRelacionadosArtemis implements PluginExpedientesRelacionados {

    public static final String PLUGIN_ORIGEN  = "ARTEMIS";
    private Logger log = Logger.getLogger(PluginExpedientesRelacionadosArtemis.class);

    public String cargar(Hashtable<String,String> argumentos,String url,String contexto){
        String dato = null;

        String[] dUrl = url.split("[?]");
        String urlUno = dUrl[0];

        StringBuffer salida = new StringBuffer();
        Enumeration<String> claves = argumentos.keys();
        while(claves.hasMoreElements()){
            String clave = claves.nextElement();
            salida.append(clave);
            salida.append("=");
            salida.append(argumentos.get(clave));
            salida.append("&");
        }

        String aux = salida.toString();
        dato         = contexto + urlUno + "?" + aux.substring(0,aux.length()-1);
        return dato;
    }
    
    public int insertarExpedienteRelacionado(ExpedienteRelacionadoVO expediente){
      int salida = 0;
      TramitacionValueObject tvo = Conversor.expedienteRelacionadoVOToTramitacionValueObject(expediente);
      tvo.setOrigenExpedienteRelacionado(PLUGIN_ORIGEN);      // Se indica el origen del expediente relacionado

      // Como se relaciona la anotación con un expediente de un procedimiento que no pertenece a FLEXIA, se recuperar
      // el código de municipio y código de procedimiento del fichero de configuración Registro.properties
      tvo.setUtilizarDatosExpedienteRelacionadoExterno(true);
      Connection connection        = expediente.getConnection(); // Conexión a una base de datos o esquema de Flexia
      try{

          ResourceBundle bundleRegistro = ResourceBundle.getBundle("Registro");

          tvo.setMunicipioExpedienteRelacionadoExterno(bundleRegistro.getString(expediente.getCodigoOrganizacion() + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CON_BARRA + PLUGIN_ORIGEN
                  + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CODMUNICIPIOPROCEDIMIENTO));

          String propiedad = bundleRegistro.getString(expediente.getCodigoOrganizacion() + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CON_BARRA + PLUGIN_ORIGEN
                  + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CODPROCEDIMIENTO);
          tvo.setCodProcedimientoExpedienteRelacionadoExterno(propiedad);
          
            salida = TramitacionManager.getInstance().insertarExpediente(connection, tvo, 0,"1");

        }catch(TramitacionException e){
            e.printStackTrace();
            log.error(e.getMessage());
        }

        return salida;
    }

    /**
     * Comprueba si existe un determinado expediente en Artemis
     * @param expediente: Datos del expediente
     * @return Retorna un 0 si el expediente existe en la BD de Artemis
     *               Retorna un 4 si el expediente no existe en la BD de Artemis
     */
    public int existeExpediente(ExpedienteRelacionadoVO expediente){
        int salida = 0;

        boolean exito = FichaExpedienteArtemisManager.getInstance().existeExpediente(expediente.getNumExpedienteRelacionado(),expediente.getCodigoOrganizacion());
        if(!exito)
            salida = 4;

        return salida;
    }


    public ExpedienteArtemisVO getInfoExpediente(String numExpediente,String codOrganizacion){
        ExpedienteArtemisVO expediente =new ExpedienteArtemisVO();

        try{
            expediente = FichaExpedienteArtemisManager.getInstance().getInfoExpediente(numExpediente, codOrganizacion);
        }catch(Exception e){
            e.printStackTrace();
        }

        return expediente;
    }


    /**
     * Recupera el número de expediente relacionado a una determinada anotación de registro procedente de la
     * base de datos de Artemis
     * @param expediente: ExpedienteRelacionadoVO con los datos necesarios de la anotación de registro y del expediente con
     * el que se relaciona
     * @return El mismo objeto que se pasa por parámetro pero con el nº de expediente, el código del procedimiento
     * y la descripción del mismo
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

            TramitacionManager.getInstance().getNumeroExpedienteRelacionadoExterno(tvo,PLUGIN_ORIGEN,con);
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
                expediente.setCodProcedimiento(null);
                expediente.setNombreProcedimiento(null);            
            }
        }

        return expediente;
    }

}