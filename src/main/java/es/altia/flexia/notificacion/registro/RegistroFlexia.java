
package es.altia.flexia.notificacion.registro;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.flexia.notificacion.vo.ApunteRegistroVO;
import es.altia.flexia.notificacion.vo.DireccionRegistroVO;
import es.altia.flexia.notificacion.vo.TerceroRegistroVO;
import es.altia.agora.business.registro.persistence.manual.AnotacionRegistroDAO;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.technical.Fecha;
import es.altia.flexia.notificacion.vo.AutorizadoNotificacionVO;
import es.altia.flexia.notificacion.vo.NotificacionVO;
import es.altia.merlin.licitacion.exceptions.SleException;

import java.io.StringReader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.xml.rpc.ServiceException;
import org.apache.axis.client.Stub;
import org.apache.log4j.Logger;

public class RegistroFlexia {

    private static RegistroFlexia instance = null;
    private Logger log = Logger.getLogger(RegistroFlexia.class);   
    private String urlWS = null;
    
    public static final int REG_ANOTACION_BUSCAR_TODAS = -10;
    public static final int REG_ANOTACION_BUSCAR_NO_ANULADAS = -9;
    // Constantes para los estados de las anotaciones de registro.
    public static final int REG_ANOTACION_ESTADO_PENDIENTE = 0;
    public static final int REG_ANOTACION_ESTADO_ACEPTADA = 1;
    public static final int REG_ANOTACION_ESTADO_RECHAZADA = 2;
    public static final int REG_ANOTACION_ESTADO_ANULADA = 9;
    // Constantes para los tipos de entradas de registro dependiendo de origen y destino.
    public static final int REG_ANOT_TIPO_ORDINARIA = 0;
    public static final int REG_ANOT_TIPO_DESTINO_OTRO_REG = 1;
    public static final int REG_ANOT_TIPO_ORIGEN_OTRO_REG = 2;
    // Codigo de departamento por defecto
    public static final int REG_COD_DEP_DEFECTO = 1;
   
    private RegistroFlexia(){
       
    }
    
    public static RegistroFlexia getInstance(){
        if(instance==null) instance = new RegistroFlexia();
        return instance;
    }

  
    private String tratarCodigoProvinciaRT(String provincia){                        
        if(provincia!=null && provincia.length()==1){                
            provincia = "0" + provincia;                
        }
        return provincia;
    }
    
    

    /**
     * Crea una anotación de salida en el RT
     * @param arg0: ApunteRegistroVO cno los datos del objeto a dar de alta
     * @param codOrganizacion: Código de la organización
     * @param firma: firma de la notificación
     * @return
     * @throws es.altia.merlin.licitacion.exceptions.SleException
     */
    public ApunteRegistroVO crearSalida(es.altia.flexia.notificacion.vo.ApunteRegistroVO arg0, NotificacionVO notificacion,Connection con,String[] params) throws SleException {
        log.debug("RegistroTelematico.crearSalida =============>");
		// Se convierte el objeto que nos llega en un String
		//String mensaje = crearMensajeExit(arg0,codOrganizacion);
               

        ResourceBundle config = ResourceBundle.getBundle("notificaciones");
       
       

      try{
          String oficinaRegistro="0";
          String usuario ="5";
          int tipoDoc=999;
          
          try{
                 oficinaRegistro=config.getString(notificacion.getCodigoMunicipio()+"/UNIDAD_REGISTRO_SALIDA_NOTIFICACIONES");
                 usuario=config.getString(notificacion.getCodigoMunicipio()+"/USUARIO_REGISTRO_SALIDA_NOTIFICACIONES");
                 tipoDoc=Integer.parseInt(config.getString(notificacion.getCodigoMunicipio()+"/TIPODOC_REGISTRO_SALIDA_NOTIFICACIONES"));
          }catch (Exception e){
               log.error("Falta alguna popiedad en el notificaciones.properties");
          }
          RegistroValueObject elRegistroESVO = new RegistroValueObject();
          elRegistroESVO.setIdOrganizacion(notificacion.getCodigoMunicipio());
          elRegistroESVO.setIdEntidad(1);
          elRegistroESVO.setIdentDepart(1);
          elRegistroESVO.setUnidadOrgan(Integer.parseInt(oficinaRegistro));
          // Datos usuario que registra. 
          elRegistroESVO.setUsuarioQRegistra(usuario);
          elRegistroESVO.setDptoUsuarioQRegistra("1");
          elRegistroESVO.setUnidOrgUsuarioQRegistra(oficinaRegistro);
          elRegistroESVO.setIdUndTramitad(arg0.getCodUnidadOrigen());
          elRegistroESVO.setIdTipoDoc(tipoDoc);  //Properties
          // Fechas del servidor.
          Fecha f = new Fecha();
          Date fSistema = new Date();
          String hora = f.construirHora(fSistema);
          elRegistroESVO.setFecEntrada(Fecha.obtenerString(fSistema) + " " + hora);
          elRegistroESVO.setFecHoraDoc(Fecha.obtenerString(fSistema) + " " + hora);

          Calendar calendario = Calendar.getInstance();
          elRegistroESVO.setAnoReg(calendario.get(Calendar.YEAR));
        
       
          elRegistroESVO.setTipoAnot(0);
          elRegistroESVO.setIdTipoPers(0);
          elRegistroESVO.setCodTipoTransp("0");
          elRegistroESVO.setAsunto(arg0.getAsunto());
          elRegistroESVO.setTipoReg("S");
         
          
           
           ArrayList<AutorizadoNotificacionVO> terceros = notificacion.getAutorizados();
           
           Vector listaCodTercero=new Vector();
           Vector listaVersionTercero=new Vector();
           Vector listaCodDomicilio=new Vector();
           Vector listaRol=new Vector();

            for(int i=0;terceros!=null && i<terceros.size();i++){ 
                AutorizadoNotificacionVO tercero = terceros.get(i);
                elRegistroESVO.setCodInter(tercero.getCodigoTercero());
                elRegistroESVO.setNumModInfInt(tercero.getNumeroVersionTercero());
                elRegistroESVO.setDomicInter(tercero.getCodDomicilio());
      
                
                listaCodTercero.addElement (Integer.toString(tercero.getCodigoTercero()));
                listaVersionTercero.addElement(tercero.getNumeroVersionTercero());
                listaCodDomicilio.addElement(tercero.getCodDomicilio());
                listaRol.addElement(tercero.getRol());
                
               
                
            }
            
            elRegistroESVO.setlistaCodTercero(listaCodTercero);
            elRegistroESVO.setlistaVersionTercero(listaVersionTercero);
            elRegistroESVO.setlistaCodDomicilio(listaCodDomicilio);
            elRegistroESVO.setlistaRol(listaRol);
            
          
           
        
          elRegistroESVO=AnotacionRegistroDAO.getInstance().insertRegistroValueObject(con,elRegistroESVO, params);
          
           arg0.setFechaRT(elRegistroESVO.getFecHoraDoc());
           arg0.setNumeroRegistroRT(crearIdNotificacion(elRegistroESVO.getDptoUsuarioQRegistra(),elRegistroESVO.getUnidadOrgan(),elRegistroESVO.getTipoReg(),elRegistroESVO.getAnoReg(),elRegistroESVO.getNumReg()));

        
        } catch (Exception e) {
            log.error(".crearSalida() Error inesperado al crear el registro en Flexia ", e);
        } 
        
         return arg0;
}//crearSalida


  

private String crearIdNotificacion (String departamento, int unidad, String tipo, int ejercicio, long numero)
{
    String retorno="";
    
    retorno=departamento+unidad+tipo+ejercicio+numero;
    
    return retorno;
}




}
