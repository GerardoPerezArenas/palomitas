package es.altia.agora.webservice.registro.rte;


import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.DocumentoValueObject;
import es.altia.agora.business.sge.TramitacionValueObject;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.persistence.manual.TramitacionDAO;
import es.altia.agora.business.sge.persistence.manual.FichaExpedienteDAO;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.persistence.AnotacionRegistroManager;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.sge.AsientoFichaExpedienteVO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.persistence.TercerosManager;
import es.altia.agora.interfaces.user.web.util.FormateadorTercero;
import es.altia.agora.webservice.registro.AccesoRegistro;
import es.altia.agora.webservice.registro.exceptions.InstanciacionRegistroException;
import es.altia.agora.webservice.registro.exceptions.RegistroException;
import es.altia.agora.webservice.registro.rte.cliente.FachadaClienteRTE;

import es.altia.common.exception.TechnicalException;
import es.altia.telematicregistry.TelematicRegistry_PortType;
import es.altia.telematicregistry.TelematicRegistry_ServiceLocator;
import es.altia.telematicregistry.serialization.xml.GetReceiptsInfoRequestXML;
import es.altia.telematicregistry.serialization.xml.GetReceiptsInfoResultXML;
import es.altia.telematicregistry.serialization.xml.objects.ReceiptInfoData;
import es.altia.telematicregistry.serialization.xml.objects.ReceiptsInfoRequest;
import es.altia.telematicregistry.serialization.xml.objects.ReceiptsInfoResult;
import es.altia.util.conexion.BDException;
import java.io.StringReader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import javax.xml.rpc.ServiceException;


import org.apache.log4j.Logger;


public class AccesoRegistroRTE implements AccesoRegistro {

    private Logger log = Logger.getLogger(AccesoRegistroRTE.class);
    
    private String nombreServicio;
    private String prefijoPropiedad; 
    private static final String REGISTRO = "REGISTRO";
    private TelematicRegistry_PortType ws;
    private static final String CONNECTION = "Registro";

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public String getPrefijoPropiedad() {
        return prefijoPropiedad;
    }

    public void setPrefijoPropiedad(String prefijoPropiedad) {
        this.prefijoPropiedad = prefijoPropiedad;
    }
    
    public Vector getAsientosEntradaRegistro(UsuarioValueObject uVO, TramitacionValueObject tVO, String[] params,
                                             String fechaDesde, String fechaHasta,String documento,String nombre,String apellido1,String apellido2,String codAsunto,String unidadRegistroAsunto,String tipoRegistroAsunto,String codUorDestino,String ejercicio,String numAnotacion, String codUorAnotacion)
            throws RegistroException {

        try {
            return TramitacionDAO.getInstance().getAsientosEntradaRegistro(uVO, tVO, params, fechaDesde, fechaHasta, nombreServicio,documento,nombre,apellido1,apellido2,codAsunto,unidadRegistroAsunto,tipoRegistroAsunto,codUorDestino,ejercicio,numAnotacion,codUorAnotacion);
        } catch (TramitacionException tramExc) {
            throw new RegistroException(tramExc, tramExc.getMessage());
        } catch (TechnicalException techExc) {
            throw new RegistroException(techExc, techExc.getMessage());
        }
    }

    public Vector getAsientosExpedientesHistorico(UsuarioValueObject uVO, TramitacionValueObject tVO, String[] params,
                                                  String fechaDesde, String fechaHasta,String documento,String nombre,String primerApellido,String segundoApellido,String codAsuntoSeleccionado,String unidadRegistroAsuntoSeleccionado,String tipoRegistroAsuntoSeleccionado,String codUorInterno, String ejercicio, String numAnotacion,String codUorAnotacion) throws RegistroException {

        if (log.isDebugEnabled()){log.debug(this.getClass().getName() + ".getAsientosExpedientesHistorico() : Método no implementado");}
        throw new InstanciacionRegistroException(this.getClass().getName() + ".getAsientosExpedientesHistorico() : Método no implementado", new Exception());
      
    }

    public RegistroValueObject getInfoAsientoConsulta(RegistroValueObject registroVO, String[] params)
            throws RegistroException {
        
        try {
               FachadaClienteRTE fachadaRTE = FachadaClienteRTE.getInstance(prefijoPropiedad);
               if (log.isDebugEnabled()){log.debug(this.getClass().getName() + ".cargaListaAsientosExpediente() : Fin ");}   
               registroVO =  fachadaRTE.buscarEntrada(registroVO,  params, nombreServicio);
               
               return registroVO;
               
        } catch (Exception are) {
           throw new RegistroException(are, are.getMessage());
        }
    }

    public void recuperarAsiento(TramitacionValueObject tramitacionVO, UsuarioValueObject usuarioVO, String[] params)
            throws RegistroException {

        if (log.isDebugEnabled()){log.debug(this.getClass().getName() + ".recuperarAsiento() : Método no implementado");}
        throw new InstanciacionRegistroException(this.getClass().getName() + ".recuperarAsiento() : Método no implementado", new Exception());
    }

    public void cambiaEstadoAsiento(TramitacionValueObject tramitacionVO, UsuarioValueObject usuarioVO, int estado,
                                    String[] params) throws RegistroException {
        if (log.isDebugEnabled()){log.debug(this.getClass().getName() + ".cambiaEstadoAsiento() : Método no implementado");}
        throw new InstanciacionRegistroException(this.getClass().getName() + ".cambiaEstadoAsiento() : Método no implementado", new Exception());
    }

    public void adjuntarExpedientesDesdeUnidadTramitadora(TramitacionValueObject tramitacionVO,
                                                          UsuarioValueObject usuarioVO, String[] params)
            throws RegistroException {
        if (log.isDebugEnabled()){log.debug(this.getClass().getName() + ".adjuntarExpedientesDesdeUnidadTramitadora() : Método no implementado");}
        throw new InstanciacionRegistroException(this.getClass().getName() + ".adjuntarExpedientesDesdeUnidadTramitadora() : Método no implementado", new Exception());
       
    }

    public ArrayList<AsientoFichaExpedienteVO> cargaListaAsientosExpediente(GeneralValueObject generalVO, UsuarioValueObject usuarioVO, 
                                               String[] params) throws RegistroException {

        if (log.isDebugEnabled()){log.debug(this.getClass().getName() + ".cargaListaAsientosExpediente() : INICIO ");}
         try {
               FachadaClienteRTE fachadaRTE = FachadaClienteRTE.getInstance(prefijoPropiedad);
               if (log.isDebugEnabled()){log.debug(this.getClass().getName() + ".cargaListaAsientosExpediente() : Fin ");}   
               return fachadaRTE.buscarEntradas(generalVO, usuarioVO, params, nombreServicio);
                 
            } catch (Exception ex) {
                throw new RegistroException(ex, ex.getMessage());
            }
    }

    public void iniciarExpedienteAsiento(GeneralValueObject generalVO, UsuarioValueObject usuarioVO, String[] params)
            throws RegistroException {

       if (log.isDebugEnabled()){log.debug(this.getClass().getName() + ".iniciarExpedienteAsiento() : Método no implementado");}
       throw new InstanciacionRegistroException(this.getClass().getName() + ".iniciarExpedienteAsiento() : Método no implementado", new Exception());
    }
    
    

    public DocumentoValueObject viewDocument(RegistroValueObject registroVO,int codDocumento, String[] params) throws RegistroException {        
        if (log.isDebugEnabled()){log.debug(this.getClass().getName() + ".viewDocument() : Método no implementado");}
        throw new InstanciacionRegistroException(this.getClass().getName() + ".viewDocument() : Método no implementado", new Exception());
    }
    
    @Override
    public Vector obtenerInteresados(RegistroValueObject gVO, String[] params)  throws RegistroException {
         if (log.isDebugEnabled()){log.debug(this.getClass().getName() + ".obtenerInteresados() : INICIO ");}
         try {
               //FachadaClienteRTE fachadaRTE = FachadaClienteRTE.getInstance(prefijoPropiedad);
               //if (log.isDebugEnabled()){log.debug(this.getClass().getName() + ".obtenerInteresados() : Fin ");}   
               //return fachadaRTE.getInteresados(gVO, params);
             return new Vector();
                 
            } catch (Exception ex) {
                throw new RegistroException(ex, ex.getMessage());
            }
    }

    @Override
    public Vector getAsientosEntradaRegistroPluginTecnico(UsuarioValueObject uvo, TramitacionValueObject tvo, String[] strings, String string, String string1, String string2, String string3, String string4, String string5, String string6, String string7, String string8, String string9, String string10, String string11, String string12, String string13, String string14, String string15) throws RegistroException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Vector getAsientosExpedientesHistoricoPluginTecnico(UsuarioValueObject uvo, TramitacionValueObject tvo, String[] strings, String string, String string1, String string2, String string3, String string4, String string5, String string6, String string7, String string8, String string9, String string10, String string11, String string12, String string13, String string14, String string15) throws RegistroException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
  

}
