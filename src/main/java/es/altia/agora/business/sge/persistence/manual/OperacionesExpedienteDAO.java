/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.business.sge.persistence.manual;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORsDAO;
import es.altia.agora.business.sge.ConsultaExpedientesValueObject;
import es.altia.agora.business.sge.OperacionExpedienteVO;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.OperacionesExpedienteHelper;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.util.commons.DateOperations;
import java.sql.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author adrian.freixeiro
 */
public class OperacionesExpedienteDAO {
    
      private static OperacionesExpedienteDAO instance = null;
      protected static Log m_Log =  LogFactory.getLog(OperacionesExpedienteDAO.class.getName());
    
     protected OperacionesExpedienteDAO() {
        super();
     }
     
    public static OperacionesExpedienteDAO getInstance() {
        // si no hay ninguna instancia de esta clase tenemos que crear una
        if (instance == null) {
        // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo
        synchronized (OperacionesExpedienteDAO.class) {
            if (instance == null) {
            instance = new OperacionesExpedienteDAO();
            }
        }
        }
        return instance;
    }
	
	public void registrarOperacionExpediente(OperacionExpedienteVO operacion, Connection con) throws TechnicalException{
		
		operacion.setDescripcionOperacion(OperacionesExpedienteHelper.generarDescripcionOperacionExpediente(operacion));
		insertarOperacionExpediente(operacion, con);
	}
	
    public void registrarAltaExpediente(GeneralValueObject infoExp, Connection con) throws TechnicalException{
        OperacionExpedienteVO operacion = new OperacionExpedienteVO();
        operacion.setCodMunicipio(Integer.parseInt((String) infoExp.getAtributo("codMunicipio")));
        operacion.setEjercicio(Integer.parseInt((String) infoExp.getAtributo("ejercicio")));
        operacion.setNumExpediente((String) infoExp.getAtributo("numero"));
        operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_ALTA_EXPEDIENTE);
        operacion.setFechaOperacion(new GregorianCalendar());
        operacion.setCodUsuario(Integer.parseInt((String) infoExp.getAtributo("usuario")));
        
        String codUOR = (String) infoExp.getAtributo("codUOR");
        String descUOR = "";
        if (codUOR != null && !"".equals(codUOR))
            descUOR = UORsDAO.getInstance().getDescripcionUOR(Integer.parseInt(codUOR), con);

        infoExp.setAtributo("descUOR", descUOR);
        
        String fechaOper = DateOperations.extraerFechaTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion())) + 
                " " + DateOperations.extraerHoraTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()));
        
        operacion.setDescripcionOperacion(OperacionesExpedienteHelper.generarDescripcionAltaExpediente(infoExp,fechaOper));
        
        insertarOperacionExpediente(operacion,con);
    }
    
    public void registrarGrabarExpediente(GeneralValueObject infoExp, Connection con) throws TechnicalException {
        OperacionExpedienteVO operacion = new OperacionExpedienteVO();
        operacion.setCodMunicipio(Integer.parseInt((String) infoExp.getAtributo("codMunicipio")));
        operacion.setEjercicio(Integer.parseInt((String) infoExp.getAtributo("ejercicio")));
        operacion.setNumExpediente((String) infoExp.getAtributo("numero"));
        operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_GRABAR_EXPEDIENTE);
        operacion.setFechaOperacion(new GregorianCalendar());
        operacion.setCodUsuario(Integer.parseInt((String) infoExp.getAtributo("usuario")));
        
        String fechaOper = DateOperations.extraerFechaTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion())) + 
                " " + DateOperations.extraerHoraTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()));

        operacion.setDescripcionOperacion(OperacionesExpedienteHelper.generarDescripcionGrabarExpediente(infoExp,fechaOper));

        insertarOperacionExpediente(operacion,con);
    }
    
    public void registrarReabrirExpediente(GeneralValueObject infoReabrirExp, Connection con) throws TechnicalException {
        OperacionExpedienteVO operacion = new OperacionExpedienteVO();
        operacion.setCodMunicipio(Integer.parseInt((String) infoReabrirExp.getAtributo("codMunicipio")));
        operacion.setEjercicio(Integer.parseInt((String) infoReabrirExp.getAtributo("ejercicio")));
        operacion.setNumExpediente((String) infoReabrirExp.getAtributo("numero"));
        operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_REABRIR_EXPEDIENTE);
        operacion.setFechaOperacion(new GregorianCalendar());
        operacion.setCodUsuario(Integer.parseInt((String) infoReabrirExp.getAtributo("usuario")));
        
        String fechaOper = DateOperations.extraerFechaTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion())) + 
                " " + DateOperations.extraerHoraTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()));

        operacion.setDescripcionOperacion(OperacionesExpedienteHelper.generarDescripcionReabrirExpediente(infoReabrirExp,fechaOper));

        insertarOperacionExpediente(operacion,con);
    }
    
    public void registrarFinalizarExpediente(TramitacionExpedientesValueObject infoFinExp, Connection con) throws TechnicalException {
        OperacionExpedienteVO operacion = new OperacionExpedienteVO();
        operacion.setCodMunicipio(Integer.parseInt(infoFinExp.getCodMunicipio()));
        operacion.setEjercicio(Integer.parseInt(infoFinExp.getEjercicio()));
        operacion.setNumExpediente((infoFinExp.getNumero()!=null && !"".equals(infoFinExp.getNumero().trim()))?infoFinExp.getNumero():infoFinExp.getNumeroExpediente());
        operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_FINALIZAR_EXPEDIENTE);
        operacion.setFechaOperacion(new GregorianCalendar());
        operacion.setCodUsuario(Integer.parseInt(infoFinExp.getCodUsuario()));
        
        String fechaOper = DateOperations.extraerFechaTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion())) + 
                " " + DateOperations.extraerHoraTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()));
        
        operacion.setDescripcionOperacion(OperacionesExpedienteHelper.generarDescripcionFinalizarExpediente(infoFinExp,fechaOper));

        insertarOperacionExpediente(operacion,con);
    }
    
    public void registrarAnularExpediente(TramitacionExpedientesValueObject infoAnularExp, Connection con) throws TechnicalException {
        OperacionExpedienteVO operacion = new OperacionExpedienteVO();
        operacion.setCodMunicipio(Integer.parseInt(infoAnularExp.getCodMunicipio()));
        operacion.setEjercicio(Integer.parseInt(infoAnularExp.getEjercicio()));
        operacion.setNumExpediente(infoAnularExp.getNumero());
        operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_ANULAR_EXPEDIENTE);
        operacion.setFechaOperacion(new GregorianCalendar());
        operacion.setCodUsuario(Integer.parseInt(infoAnularExp.getCodUsuario()));
        
        String fechaOper = DateOperations.extraerFechaTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion())) + 
                " " + DateOperations.extraerHoraTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()));
        
        operacion.setDescripcionOperacion(OperacionesExpedienteHelper.generarDescripcionAnularExpediente(infoAnularExp,fechaOper));
        
        insertarOperacionExpediente(operacion,con);
    }
    
    public void registrarGrabarTramite(TramitacionExpedientesValueObject infoTramite,  Connection con) throws TechnicalException {
        OperacionExpedienteVO operacion = new OperacionExpedienteVO();
        operacion.setCodMunicipio(Integer.parseInt(infoTramite.getCodMunicipio()));
        operacion.setEjercicio(Integer.parseInt(infoTramite.getEjercicio()));
        operacion.setNumExpediente((infoTramite.getNumero()!=null && !"".equals(infoTramite.getNumero().trim()))?infoTramite.getNumero():infoTramite.getNumeroExpediente());
        operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_GRABAR_TRAMITE);
        operacion.setFechaOperacion(new GregorianCalendar());
        operacion.setCodUsuario(Integer.parseInt(infoTramite.getCodUsuario()));
        
        String fechaOper = DateOperations.extraerFechaTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion())) + 
                " " + DateOperations.extraerHoraTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()));

        operacion.setDescripcionOperacion(OperacionesExpedienteHelper.generarDescripcionGrabarTramite(infoTramite,fechaOper));

        insertarOperacionExpediente(operacion,con);
    }
    
    public void registrarIniciarTramite(GeneralValueObject infoTramite, boolean manual, Connection con) throws TechnicalException {
        OperacionExpedienteVO operacion = new OperacionExpedienteVO();
        operacion.setCodMunicipio(Integer.parseInt((String) infoTramite.getAtributo("codMunicipio")));
        operacion.setEjercicio(Integer.parseInt((String) infoTramite.getAtributo("ejercicio")));
        operacion.setNumExpediente((String) infoTramite.getAtributo("numero"));
        if (manual) {
            operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_INICIAR_TRAMITE_MANUAL);
        } else {
            operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_INICIAR_TRAMITE);
        }
        operacion.setFechaOperacion(new GregorianCalendar());
        operacion.setCodUsuario(Integer.parseInt((String) infoTramite.getAtributo("usuario")));
        
        String fechaOper = DateOperations.extraerFechaTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion())) + 
                " " + DateOperations.extraerHoraTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()));
        
        operacion.setDescripcionOperacion(OperacionesExpedienteHelper.generarDescripcionIniciarTramite(infoTramite, manual, fechaOper));

        insertarOperacionExpediente(operacion,con);
    }
    
    public void registrarFinalizarTramite(TramitacionExpedientesValueObject infoTramite,  Boolean desfavorable, Connection con) throws TechnicalException {
        OperacionExpedienteVO operacion = new OperacionExpedienteVO();
        operacion.setCodMunicipio(Integer.parseInt(infoTramite.getCodMunicipio()));
        operacion.setEjercicio(Integer.parseInt(infoTramite.getEjercicio()));
        operacion.setNumExpediente((infoTramite.getNumero()!=null && !"".equals(infoTramite.getNumero().trim()))?infoTramite.getNumero():infoTramite.getNumeroExpediente());
        operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_AVANZAR_TRAMITE);
        operacion.setFechaOperacion(new GregorianCalendar());
        operacion.setCodUsuario(Integer.parseInt(infoTramite.getCodUsuario()));
        
        String fechaOper = DateOperations.extraerFechaTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion())) + 
                " " + DateOperations.extraerHoraTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()));
        
        operacion.setDescripcionOperacion(OperacionesExpedienteHelper.generarDescripcionFinalizarTramite(infoTramite,desfavorable,fechaOper));

        insertarOperacionExpediente(operacion,con);
    }
    
    public void registrarRetrocederTramite(GeneralValueObject infoTramite, Connection con) throws TechnicalException {
        OperacionExpedienteVO operacion = new OperacionExpedienteVO();
        operacion.setCodMunicipio(Integer.parseInt((String) infoTramite.getAtributo("codMunicipio")));
        operacion.setEjercicio(Integer.parseInt((String) infoTramite.getAtributo("ejercicio")));
        operacion.setNumExpediente((String) infoTramite.getAtributo("numero"));
        operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_RETROCEDER_TRAMITE);
        operacion.setFechaOperacion(new GregorianCalendar());
        operacion.setCodUsuario(Integer.parseInt((String) infoTramite.getAtributo("usuario")));
        
        String fechaOper = DateOperations.extraerFechaTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion())) + 
                " " + DateOperations.extraerHoraTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()));
        
        operacion.setDescripcionOperacion(OperacionesExpedienteHelper.generarDescripcionRetrocederTramite(infoTramite,fechaOper));

        insertarOperacionExpediente(operacion,con);
    }
    
    public void registrarRetrocederTramiteOrigen(GeneralValueObject infoTramite, Connection con) throws TechnicalException {
        OperacionExpedienteVO operacion = new OperacionExpedienteVO();
        operacion.setCodMunicipio(Integer.parseInt((String) infoTramite.getAtributo("codMunicipio")));
        operacion.setEjercicio(Integer.parseInt((String) infoTramite.getAtributo("ejercicio")));
        operacion.setNumExpediente((String) infoTramite.getAtributo("numero"));
        operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_RETROCEDER_TRAMITE_ORIGEN);
        operacion.setFechaOperacion(new GregorianCalendar());
        operacion.setCodUsuario(Integer.parseInt((String) infoTramite.getAtributo("usuario")));

        String fechaOper = DateOperations.extraerFechaTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()))
                + " " + DateOperations.extraerHoraTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()));

        operacion.setDescripcionOperacion(OperacionesExpedienteHelper.generarDescripcionRetrocederTramiteOrigen(infoTramite, fechaOper));

        insertarOperacionExpediente(operacion, con);
    }
    
    public void registrarReabrirTramite(GeneralValueObject infoTramite, Connection con) throws TechnicalException {
        OperacionExpedienteVO operacion = new OperacionExpedienteVO();
        operacion.setCodMunicipio(Integer.parseInt((String) infoTramite.getAtributo("codMunicipio")));
        operacion.setEjercicio(Integer.parseInt((String) infoTramite.getAtributo("ejercicio")));
        operacion.setNumExpediente((String) infoTramite.getAtributo("numero"));
        operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_REABRIR_TRAMITE);
        operacion.setFechaOperacion(new GregorianCalendar());
        operacion.setCodUsuario(Integer.parseInt((String) infoTramite.getAtributo("usuario")));

        String fechaOper = DateOperations.extraerFechaTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()))
                + " " + DateOperations.extraerHoraTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()));

        operacion.setDescripcionOperacion(OperacionesExpedienteHelper.generarDescripcionReabrirTramite(infoTramite, fechaOper));
        insertarOperacionExpediente(operacion, con);
    }
    
    public void registrarAltaInteresado(int codOrg, String numExpediente, int codUsu, String nomUsu, 
            TercerosValueObject tercero, Connection con) throws TechnicalException {
        
        String ejercicio = numExpediente.split(Pattern.quote("/"))[0];
                
        OperacionExpedienteVO operacion = new OperacionExpedienteVO();
        operacion.setCodMunicipio(codOrg);
        operacion.setEjercicio(Integer.parseInt(ejercicio));
        operacion.setNumExpediente(numExpediente);
        operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_ALTA_INTERESADO);
        operacion.setFechaOperacion(new GregorianCalendar());
        operacion.setCodUsuario(codUsu);
        
        String fechaOper = DateOperations.extraerFechaTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion())) + 
                " " + DateOperations.extraerHoraTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()));
        
        operacion.setDescripcionOperacion(OperacionesExpedienteHelper.generarDescripcionAltaInteresado(tercero,codUsu,nomUsu,fechaOper));

        insertarOperacionExpediente(operacion,con);
    }

    public void registrarModificacionInteresado(GeneralValueObject gVO, boolean cambioVersion, boolean cambioRol, 
                boolean cambioDomicilio, boolean cambioNotifElec, boolean cambioDatos, Connection con) throws TechnicalException {
        
        OperacionExpedienteVO operacion = new OperacionExpedienteVO();
        operacion.setCodMunicipio(Integer.parseInt((String) gVO.getAtributo("codMunicipio")));
        operacion.setEjercicio(Integer.parseInt((String) gVO.getAtributo("ejercicio")));
        operacion.setNumExpediente((String) gVO.getAtributo("numero"));
        operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_MODIFICAR_INTERESADO);
        operacion.setFechaOperacion(new GregorianCalendar());
        operacion.setCodUsuario(Integer.parseInt((String) gVO.getAtributo("usuario")));
        
        String fechaOper = DateOperations.extraerFechaTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion())) + 
                " " + DateOperations.extraerHoraTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()));
        
        operacion.setDescripcionOperacion(OperacionesExpedienteHelper.generarDescripcionModificacionInteresado(gVO,
                cambioVersion,cambioRol,cambioDomicilio,cambioNotifElec,cambioDatos,fechaOper));

        insertarOperacionExpediente(operacion,con);
    }
    
    public void registrarEliminacionInteresado(int codOrg, String numExpediente, int codUsu, String nomUsu, 
            TercerosValueObject tercero, Connection con) throws TechnicalException {
        
        String ejercicio = numExpediente.split(Pattern.quote("/"))[0];
                
        OperacionExpedienteVO operacion = new OperacionExpedienteVO();
        operacion.setCodMunicipio(codOrg);
        operacion.setEjercicio(Integer.parseInt(ejercicio));
        operacion.setNumExpediente(numExpediente);
        operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_ELIMINAR_INTERESADO);
        operacion.setFechaOperacion(new GregorianCalendar());
        operacion.setCodUsuario(codUsu);
        
        String fechaOper = DateOperations.extraerFechaTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion())) + 
                " " + DateOperations.extraerHoraTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()));
        
        operacion.setDescripcionOperacion(OperacionesExpedienteHelper.generarDescripcionEliminacionInteresado(tercero,codUsu,nomUsu,fechaOper));

        insertarOperacionExpediente(operacion,con);
    }
    
    public void registrarAnhadirRelacion(ConsultaExpedientesValueObject infoRelExp, Connection con) throws TechnicalException {
        OperacionExpedienteVO operacion = new OperacionExpedienteVO();
        operacion.setCodMunicipio(Integer.parseInt(infoRelExp.getCodMunicipio()));
        operacion.setEjercicio(Integer.parseInt(infoRelExp.getEjercicioIni()));
        operacion.setNumExpediente(infoRelExp.getNumeroExpedienteIni());
        operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_ANHADIR_RELACION);
        operacion.setFechaOperacion(new GregorianCalendar());
        operacion.setCodUsuario(Integer.parseInt(infoRelExp.getUsuario()));
        
        String fechaOper = DateOperations.extraerFechaTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion())) + 
                " " + DateOperations.extraerHoraTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()));
        
        operacion.setDescripcionOperacion(OperacionesExpedienteHelper.generarDescripcionAnhadirRelacion(infoRelExp,fechaOper));

        insertarOperacionExpediente(operacion,con);
    }
    
    public void registrarEliminarRelacion(ConsultaExpedientesValueObject infoRelExp, Connection con) throws TechnicalException {
        OperacionExpedienteVO operacion = new OperacionExpedienteVO();
        operacion.setCodMunicipio(Integer.parseInt(infoRelExp.getCodMunicipio()));
        operacion.setEjercicio(Integer.parseInt(infoRelExp.getEjercicioIni()));
        operacion.setNumExpediente(infoRelExp.getNumeroExpedienteIni());
        operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_ELIMINAR_RELACION);
        operacion.setFechaOperacion(new GregorianCalendar());
        operacion.setCodUsuario(Integer.parseInt(infoRelExp.getUsuario()));
        
        String fechaOper = DateOperations.extraerFechaTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion())) + 
                " " + DateOperations.extraerHoraTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()));
        
        operacion.setDescripcionOperacion(OperacionesExpedienteHelper.generarDescripcionEliminarRelacion(infoRelExp,fechaOper));

        insertarOperacionExpediente(operacion,con);
    }
    
    public void registrarAltaDocumentoExpediente(Documento doc, String nomUsuario, boolean externo, Connection con) throws TechnicalException {
        OperacionExpedienteVO operacion = new OperacionExpedienteVO();
        operacion.setCodMunicipio(doc.getCodMunicipio());
        operacion.setEjercicio(doc.getEjercicio());
        operacion.setNumExpediente(doc.getNumeroExpediente());
        
        if (externo)
            operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_ALTA_DOC_EXP_EXT);
        else
            operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_ALTA_DOC_EXP);
        
        operacion.setFechaOperacion(new GregorianCalendar());
        operacion.setCodUsuario(doc.getCodUsuario());
        
        String fechaOper = DateOperations.extraerFechaTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion())) + 
                " " + DateOperations.extraerHoraTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()));
        
        operacion.setDescripcionOperacion(OperacionesExpedienteHelper.generarDescripcionAltaDocumentoExpediente(doc,
                nomUsuario, externo, fechaOper));

        insertarOperacionExpediente(operacion,con);
    }
    
    public void registrarEliminacionDocumentoExpediente(Documento doc, String nomUsuario, boolean externo, Connection con) throws TechnicalException {
        OperacionExpedienteVO operacion = new OperacionExpedienteVO();
        operacion.setCodMunicipio(doc.getCodMunicipio());
        operacion.setEjercicio(doc.getEjercicio());
        operacion.setNumExpediente(doc.getNumeroExpediente());
        
        if (externo)
            operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_ELIMINAR_DOC_EXP_EXT);
        else
            operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_ELIMINAR_DOC_EXP);
        
        operacion.setFechaOperacion(new GregorianCalendar());
        operacion.setCodUsuario(doc.getCodUsuario());
        
        String fechaOper = DateOperations.extraerFechaTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion())) + 
                " " + DateOperations.extraerHoraTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()));
        
        operacion.setDescripcionOperacion(OperacionesExpedienteHelper.generarDescripcionEliminacionDocumentoExpediente(doc,
                nomUsuario, externo, fechaOper));

        insertarOperacionExpediente(operacion,con);
    }
    
    public void registrarAltaDocumentoTramite(Documento doc, String nomUsuario, Connection con) throws TechnicalException {
        OperacionExpedienteVO operacion = new OperacionExpedienteVO();
        operacion.setCodMunicipio(doc.getCodMunicipio());
        operacion.setEjercicio(doc.getEjercicio());
        operacion.setNumExpediente(doc.getNumeroExpediente());
        operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_ALTA_DOC_TRAMITE);
        operacion.setFechaOperacion(new GregorianCalendar());
        operacion.setCodUsuario(doc.getCodUsuario());
        
        String fechaOper = DateOperations.extraerFechaTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion())) + 
                " " + DateOperations.extraerHoraTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()));
        
        operacion.setDescripcionOperacion(OperacionesExpedienteHelper.generarDescripcionAltaDocumentoTramite(doc,
                nomUsuario, fechaOper));

        insertarOperacionExpediente(operacion,con);
    }
    
    public void registrarEliminacionDocumentoTramite(Documento doc, String nomUsuario, Connection con) throws TechnicalException {
        OperacionExpedienteVO operacion = new OperacionExpedienteVO();
        operacion.setCodMunicipio(doc.getCodMunicipio());
        operacion.setEjercicio(doc.getEjercicio());
        operacion.setNumExpediente(doc.getNumeroExpediente());
        operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_ELIMINAR_DOC_TRAMITE);
        operacion.setFechaOperacion(new GregorianCalendar());
        operacion.setCodUsuario(doc.getCodUsuario());
        
        String fechaOper = DateOperations.extraerFechaTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion())) + 
                " " + DateOperations.extraerHoraTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()));
        
        operacion.setDescripcionOperacion(OperacionesExpedienteHelper.generarDescripcionEliminacionDocumentoTramite(doc,
                nomUsuario, fechaOper));

        insertarOperacionExpediente(operacion,con);
    }

    public void insertarOperacionExpediente(OperacionExpedienteVO historico,Connection con) throws TechnicalException{
	   
        PreparedStatement ps = null;
      
        try {
            String sql = "INSERT INTO OPERACIONES_EXPEDIENTE (ID_OPERACION, COD_MUNICIPIO, EJERCICIO, " + 
                    "NUM_EXPEDIENTE, TIPO_OPERACION, FECHA_OPERACION, COD_USUARIO, DESCRIPCION_OPERACION) " +
                  "VALUES (SEQ_OPERACIONES_EXPEDIENTE.NextVal,?,?,?,?,?,?,?)";
            
            if (m_Log.isDebugEnabled()) m_Log.debug(sql);
            
            ps = con.prepareStatement(sql);
            
            int i = 1;
            ps.setInt(i++, historico.getCodMunicipio());
            ps.setInt(i++, historico.getEjercicio());
            ps.setString(i++, historico.getNumExpediente());
            ps.setInt(i++, historico.getTipoOperacion());
            ps.setTimestamp(i++, DateOperations.toTimestamp(historico.getFechaOperacion()));
            ps.setInt(i++, historico.getCodUsuario());     
            ps.setString(i++, historico.getDescripcionOperacion());

            ps.executeUpdate();
            ps.close();
         }catch (Exception e){
            if (m_Log.isErrorEnabled()) {m_Log.error(e.getMessage());}
            e.printStackTrace();            
            throw new TechnicalException(e.getMessage(), e);
        } finally {
            try{
                if (ps!=null) {ps.close();}
            }catch (SQLException esql){
                if (m_Log.isErrorEnabled()) {m_Log.error(esql.getMessage());} 
                esql.printStackTrace();                
                throw new TechnicalException(esql.getMessage(), esql);
            }
         }
    }    
    
    public ArrayList <OperacionExpedienteVO> recuperarOperacionesExpediente(int codOrganizacion, 
            String numExpediente, boolean isExpHistorico, TraductorAplicacionBean traductor, Connection con)
            throws TechnicalException {
        m_Log.debug("OperacionesExpedienteManager.recuperarOperacionesExpediente()::BEGIN");
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList <OperacionExpedienteVO> resultado = new ArrayList <OperacionExpedienteVO>();
    
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ID_OPERACION, COD_MUNICIPIO, EJERCICIO, ") 
               .append("NUM_EXPEDIENTE, TIPO_OPERACION, FECHA_OPERACION, COD_USUARIO ")
               .append("FROM ");
            if (isExpHistorico) {
                sql.append("HIST_OPERACIONES_EXPEDIENTE ");
            } else {
                sql.append("OPERACIONES_EXPEDIENTE ");
            }     
            sql.append("WHERE COD_MUNICIPIO = ? AND NUM_EXPEDIENTE = ? ORDER BY FECHA_OPERACION DESC");
            
            if (m_Log.isDebugEnabled())m_Log.debug(sql.toString());
         
            ps = con.prepareStatement(sql.toString());
            ps.setInt(1, codOrganizacion);
            ps.setString(2, numExpediente);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                OperacionExpedienteVO operacionExpedienteVO = new OperacionExpedienteVO();
                operacionExpedienteVO.setIdOperacion(rs.getInt("ID_OPERACION"));
                operacionExpedienteVO.setEjercicio(rs.getInt("EJERCICIO"));                
                operacionExpedienteVO.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));                                
                operacionExpedienteVO.setTipoOperacion(rs.getInt("TIPO_OPERACION"));
                Timestamp fecha = rs.getTimestamp("FECHA_OPERACION");
                operacionExpedienteVO.setFechaOperacion(DateOperations.timestampToCalendar(fecha)); 
                
                if (traductor != null) {
	                switch(operacionExpedienteVO.getTipoOperacion()){
	                    case ConstantesDatos.TIPO_MOV_ALTA_EXPEDIENTE: 
	                        operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpAltaExp"));
	                        break;
	                    case ConstantesDatos.TIPO_MOV_GRABAR_EXPEDIENTE: 
	                        operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpGrabarExp"));
	                        break;
	                    case ConstantesDatos.TIPO_MOV_GRABAR_TRAMITE: 
	                        operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpGrabarTram"));
	                        break;
	                    case ConstantesDatos.TIPO_MOV_AVANZAR_TRAMITE: 
	                        operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpFinalizarTram"));
	                        break;
	                    case ConstantesDatos.TIPO_MOV_RETROCEDER_TRAMITE: 
	                        operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpRetrocTram"));
	                        break;
	                    case ConstantesDatos.TIPO_MOV_FINALIZAR_EXPEDIENTE: 
	                        operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpFinalizaExp"));
	                        break;
	                    case ConstantesDatos.TIPO_MOV_ANULAR_EXPEDIENTE: 
	                        operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpAnulaExp"));
	                        break;
	                    case ConstantesDatos.TIPO_MOV_ALTA_INTERESADO: 
	                        operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpIntAlt"));
	                        break;
	                    case ConstantesDatos.TIPO_MOV_MODIFICAR_INTERESADO: 
	                        operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpIntModificar"));
	                        break;
	                    case ConstantesDatos.TIPO_MOV_ELIMINAR_INTERESADO: 
	                        operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpIntEliminar"));
	                        break;
	                    case ConstantesDatos.TIPO_MOV_REABRIR_EXPEDIENTE: 
	                        operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpReabrirExp"));
	                        break;
	                    case ConstantesDatos.TIPO_MOV_ANHADIR_RELACION: 
	                        operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpAnhadirRel"));
	                        break;
	                    case ConstantesDatos.TIPO_MOV_ELIMINAR_RELACION: 
	                        operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpEliminarRel"));
	                        break;
	                    case ConstantesDatos.TIPO_MOV_ALTA_DOC_EXP: 
	                        operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpAltDocExp"));
	                        break;
	                    case ConstantesDatos.TIPO_MOV_ELIMINAR_DOC_EXP: 
	                        operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpElimDocExp"));
	                        break;
	                    case ConstantesDatos.TIPO_MOV_ALTA_DOC_EXP_EXT: 
	                        operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpAltDocExt"));
	                        break;
	                    case ConstantesDatos.TIPO_MOV_ELIMINAR_DOC_EXP_EXT: 
	                        operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpElimDocExt"));
	                        break;
	                    case ConstantesDatos.TIPO_MOV_ALTA_DOC_TRAMITE: 
	                        operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpAltDocTra"));
	                        break;
	                    case ConstantesDatos.TIPO_MOV_ELIMINAR_DOC_TRAMITE: 
	                        operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpElimDocTra"));
	                        break;
	                    case ConstantesDatos.TIPO_MOV_ALTA_DOC_SUPL_TRAMITE: 
	                        operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpAltDocSupTra"));
	                        break;
	                    case ConstantesDatos.TIPO_MOV_CAMBIO_DOM_INTERESADO: 
	                        operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpIntCambioDom"));
	                        break;
	                    case ConstantesDatos.TIPO_MOV_GRABAR_CAMPOS_EXPEDIENTE: 
	                        operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpGrabarSupExp"));
	                        break;
	                    case ConstantesDatos.TIPO_MOV_GRABAR_CAMPOS_TRAMITE: 
	                        operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpGrabarSupTra"));
	                        break;
	                    case ConstantesDatos.TIPO_MOV_INICIAR_TRAMITE:
	                        operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpIniciarTram"));
	                        break;
	                    case ConstantesDatos.TIPO_MOV_INICIAR_TRAMITE_MANUAL:
	                        operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpIniciarTramManual"));
	                        break;
	                    case ConstantesDatos.TIPO_MOV_RETROCEDER_TRAMITE_ORIGEN:
	                        operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpRetrocTramOrigen"));
	                        break;
                        case ConstantesDatos.TIPO_MOV_BLOQUEAR_TRAMITE:
	                        operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpBloquearTram"));
	                        break;
                        case ConstantesDatos.TIPO_MOV_DESBLOQUEAR_TRAMITE:
	                        operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpDesbloquearTram"));
	                        break;
                        case ConstantesDatos.TIPO_MOV_CAMBIO_UTR:
	                        operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpCambioUtr"));  
	                        break;
                        case ConstantesDatos.TIPO_MOV_REABRIR_TRAMITE:
                            operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpReabrirTramite"));  
                            break;
                        case ConstantesDatos.TIPO_MOV_ANULAR_DEUDA:
                            operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpAnularDeuda"));  
                            break;
                        case ConstantesDatos.TIPO_MOV_BORRAR_CAMPOS_TRAMITE:
                            operacionExpedienteVO.setTipoOperacionTxt(traductor.getDescripcion("eMovExpBorrarCamposTram"));  
                            break;
                        default: operacionExpedienteVO.setTipoOperacionTxt("--");
                            break;
	                }

	                operacionExpedienteVO.setFechaOperacionTxt(DateOperations.extraerFechaTimeStamp(fecha) + 
	                        " " + DateOperations.extraerHoraTimeStamp(fecha));
                }
                
                operacionExpedienteVO.setCodUsuario(rs.getInt("COD_USUARIO"));
                                
                resultado.add(operacionExpedienteVO);
            }
            rs.close();
       }catch (Exception e){
            if (m_Log.isErrorEnabled()) {m_Log.error(e.getMessage());}
            e.printStackTrace();            
            throw new TechnicalException(e.getMessage(), e);
        } finally {
            try{
                if (rs!=null) {rs.close();}                
                if (ps!=null) {ps.close();}
            }catch(SQLException sqle){
                if (m_Log.isErrorEnabled()) {m_Log.error(sqle.getMessage());}                
                sqle.printStackTrace();                
                throw new TechnicalException(sqle.getMessage(), sqle);
            }
        }
        return resultado;
    }
    
    public OperacionExpedienteVO recuperarOperacion(int idOperacion, boolean isExpHistorico, Connection con) throws TechnicalException{

        PreparedStatement ps = null;
        ResultSet rs = null;

        OperacionExpedienteVO resultado = new OperacionExpedienteVO();
    
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ID_OPERACION, COD_MUNICIPIO, EJERCICIO, ") 
               .append("NUM_EXPEDIENTE, TIPO_OPERACION, FECHA_OPERACION, COD_USUARIO, DESCRIPCION_OPERACION ")
               .append("FROM ");
            if (isExpHistorico) {
                sql.append("HIST_OPERACIONES_EXPEDIENTE ");
            } else {
                sql.append("OPERACIONES_EXPEDIENTE ");
            }     
            sql.append("WHERE ID_OPERACION = ? ORDER BY FECHA_OPERACION");
            
            if (m_Log.isDebugEnabled())m_Log.debug(sql.toString());
         
            ps = con.prepareStatement(sql.toString());
            ps.setInt(1, idOperacion);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                resultado = new OperacionExpedienteVO();
                resultado.setIdOperacion(idOperacion);
                resultado.setEjercicio(rs.getInt("EJERCICIO"));                
                resultado.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));                                
                resultado.setTipoOperacion(rs.getInt("TIPO_OPERACION"));
                Timestamp fecha = rs.getTimestamp("FECHA_OPERACION");
                resultado.setFechaOperacion(DateOperations.timestampToCalendar(fecha)); 
                resultado.setFechaOperacionTxt(DateOperations.extraerFechaTimeStamp(fecha) + 
                        " " + DateOperations.extraerHoraTimeStamp(fecha));
                resultado.setCodUsuario(rs.getInt("COD_USUARIO"));
                resultado.setDescripcionOperacion(rs.getString("DESCRIPCION_OPERACION"));                                
            }
            rs.close();
       }catch (Exception e){
            if (m_Log.isErrorEnabled()) {m_Log.error(e.getMessage());}
            e.printStackTrace();            
            throw new TechnicalException(e.getMessage(), e);
        } finally {
            try{
                if (rs!=null) {rs.close();}                
                if (ps!=null) {ps.close();}
            }catch(SQLException sqle){
                if (m_Log.isErrorEnabled()) {m_Log.error(sqle.getMessage());}                
                sqle.printStackTrace();                
                throw new TechnicalException(sqle.getMessage(), sqle);
            }
        }
        return resultado;
    }

    public void registrarBloquearTramite(GeneralValueObject infoTramite, Connection con)
            throws TechnicalException {
        OperacionExpedienteVO operacion = new OperacionExpedienteVO();
        operacion.setCodMunicipio(Integer.parseInt((String) infoTramite.getAtributo("codMunicipio")));
        operacion.setEjercicio(Integer.parseInt((String) infoTramite.getAtributo("ejercicio")));
        operacion.setNumExpediente((String) infoTramite.getAtributo("numero"));
        operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_BLOQUEAR_TRAMITE);
        operacion.setFechaOperacion(new GregorianCalendar());
        operacion.setCodUsuario(Integer.parseInt((String) infoTramite.getAtributo("usuario")));
        
        String fechaOper = DateOperations.extraerFechaTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion())) + 
                " " + DateOperations.extraerHoraTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()));
        
        operacion.setDescripcionOperacion(OperacionesExpedienteHelper.generarDescripcionBloquearTramite(infoTramite,fechaOper));

        insertarOperacionExpediente(operacion,con);
    }

    public void registrarDesbloquearTramite(GeneralValueObject infoTramite, Connection con)
            throws TechnicalException {
        OperacionExpedienteVO operacion = new OperacionExpedienteVO();
        operacion.setCodMunicipio(Integer.parseInt((String) infoTramite.getAtributo("codMunicipio")));
        operacion.setEjercicio(Integer.parseInt((String) infoTramite.getAtributo("ejercicio")));
        operacion.setNumExpediente((String) infoTramite.getAtributo("numero"));
        operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_DESBLOQUEAR_TRAMITE);
        operacion.setFechaOperacion(new GregorianCalendar());
        operacion.setCodUsuario(Integer.parseInt((String) infoTramite.getAtributo("usuario")));
        
        String fechaOper = DateOperations.extraerFechaTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion())) + 
                " " + DateOperations.extraerHoraTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()));

        operacion.setDescripcionOperacion(OperacionesExpedienteHelper.generarDescripcionDesbloquearTramite(infoTramite, fechaOper));

        insertarOperacionExpediente(operacion, con);
    }
    
}
