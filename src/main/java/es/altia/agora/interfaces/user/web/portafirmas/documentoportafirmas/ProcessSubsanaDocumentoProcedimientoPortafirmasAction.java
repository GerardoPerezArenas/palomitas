/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.interfaces.user.web.portafirmas.documentoportafirmas;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.integracionsw.exception.EjecucionSWException;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoProcedimientoFirmaVO;
import es.altia.agora.business.portafirmas.persistence.manual.DocsProcedimientoPortafirmasManager;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.notificacion.dao.MailsTramitacionDAO;
import es.altia.agora.business.sge.persistence.FirmasDocumentoProcedimientoManager;
import es.altia.agora.business.sge.persistence.OperacionesExpedienteManager;
import es.altia.agora.business.sge.persistence.TramitacionExpedientesManager;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.interfaces.user.web.portafirmas.GlobalNames;
import es.altia.agora.interfaces.user.web.portafirmas.SessionManager;
import es.altia.agora.technical.EstructuraNotificacion;
import es.altia.agora.webservice.tramitacion.servicios.WSException;
import es.altia.common.exception.TechnicalException;
import es.altia.util.cache.CacheDatosFactoria;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.struts.DefaultAction;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 *
 * @author montserrat.rodriguez
 */
public class ProcessSubsanaDocumentoProcedimientoPortafirmasAction extends DefaultAction {
          /*_______Constants______________________________________________*/
    private static final String CLSNAME = "ProcessSusbanaDocumentoProcedimientoPortafirmasAction";
    private static final String MESSAGE_SUCCESS = "Portafirmas.FirmaDocumentoPortafirmasForm.DocumentoSubsanado";
    private static final String MESSAGE_SUCCESS_FAIL = "Portafirmas.FirmaDocumentoPortafirmasForm.DocumentoNoSubsanado";
    private static final String MESSAGE_NO_PERMITE_SUBSANACION = "Portafirmas.FirmaDocumentoPortafirmasForm.DocumentoNoPermiteSubsanacion";
    private static final Log _log =
            LogFactory.getLog(ProcessSubsanaDocumentoProcedimientoPortafirmasAction.class.getName());
    private static int FLUJO_LISTA_TRAMITES_NO_FAVORABLE = 2; 
    private static int FLUJO_LISTA_TRAMITES_FAVORABLE = 1; 

    private String pDataSourceKey = null;

    /*_______Operations_____________________________________________*/
    protected ActionForward doPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InternalErrorException, ModelException {
        if (_log.isInfoEnabled()) _log.info(CLSNAME+".doPerform() BEGIN");

        /* Aceptamos la subsanación, por lo que eliminamos de la sesion el parametro de valor seleccionado en caso de cancelar */
        SessionManager.removeSelectedIndex(request);

        /* Cast form */
        FirmaDocumentoPortafirmasActionForm concreteForm = (FirmaDocumentoPortafirmasActionForm) form;
        if (_log.isDebugEnabled()) _log.debug(CLSNAME+".doPerform() ConcreteForm = "+concreteForm);

        /* Retrieve DataSource key */
        this.pDataSourceKey = SessionManager.getDataSourceKey(request);

        /* Save Signature */
        int idUsuarioAutenticado  = SessionManager.getAuthenticatedUser(request).getIdUsuario();
                
        UsuarioValueObject usuarioVO = (UsuarioValueObject)request.getSession().getAttribute("usuario");
        GeneralValueObject tramite = permiteTramitacionSubsanacion(concreteForm, usuarioVO);
        Vector tramites;
        
        String mensaje = null;
        if (tramite != null) {
            _log.debug(ProcessSubsanaDocumentoProcedimientoPortafirmasAction.class.getName() + "--> Se realiza el avance del trámite");
            boolean mensajeNoFinalizado = false;
            concreteForm.setIdTramite(Integer.parseInt((String)tramite.getAtributo("codTramite")));
            Vector tramitesIniciar = obtenerTramitesSiguientes(concreteForm, usuarioVO);
            String[] params = usuarioVO.getParamsCon();
            
            TramitacionExpedientesValueObject traExpVO = new TramitacionExpedientesValueObject();
            traExpVO.setListaEMailsAlIniciar(new Vector());
            traExpVO.setListaEMailsAlFinalizar(new Vector());
            traExpVO.setCodUsuario(Integer.toString(usuarioVO.getIdUsuario()));
            traExpVO.setNombreUsuario(usuarioVO.getNombreUsu());
            traExpVO.setCodIdiomaUsuario(usuarioVO.getIdioma());            
            traExpVO.setListaTramitesIniciar(tramitesIniciar);
            traExpVO.setCodMunicipio((String)tramite.getAtributo("codMunicipio"));
            traExpVO.setCodOrganizacion((String)tramite.getAtributo("codMunicipio"));
            traExpVO.setCodProcedimiento((String)tramite.getAtributo("codProcedimiento"));
            traExpVO.setEjercicio((String)tramite.getAtributo("ejercicio"));
            traExpVO.setNumeroExpediente((String)tramite.getAtributo("numeroExpediente"));
            traExpVO.setNumero((String)tramite.getAtributo("numeroExpediente"));
            traExpVO.setCodTramite((String)tramite.getAtributo("codTramite"));
            traExpVO.setOcurrenciaTramite((String)tramite.getAtributoONulo("ocurrenciaTramite"));
            try {
                tramites = TramitacionExpedientesManager.getInstance().finalizarConSubsanacion(traExpVO, params, true);
                if (tramites != null) {
                    DocumentoProcedimientoFirmaVO doc = new DocumentoProcedimientoFirmaVO();
                    doc.setIdPresentado(concreteForm.getIdPresentado());            
                    doc.setCodigoUsuarioFirma(Integer.toString(idUsuarioAutenticado));
                    doc.setFechaFirma(Calendar.getInstance());
                    doc.setObservaciones(concreteForm.getObservaciones());
                    doc.setIdNumFirma(concreteForm.getIdNumFirma());

                    mensajeNoFinalizado = DocsProcedimientoPortafirmasManager.getInstance().
                    subsanarDocumento(doc, usuarioVO.getParamsCon());
                    
                    this.notificar(traExpVO, params);
                    
                    OperacionesExpedienteManager.getInstance().registrarFinalizarTramite(traExpVO, true, params);
                }
            } catch (WSException wse) {
                ponerMensajeFalloSW(traExpVO, wse);
                if (wse.isMandatoryExecution()) {
                    mensajeNoFinalizado = true;
                }
            } catch (TramitacionException te) {
                traExpVO.setMensajeSW(te.getMessage());
                mensajeNoFinalizado = true;
            } catch (EjecucionSWException eswe) {
                ponerMensajeFalloSW(traExpVO, eswe);
                if (eswe.isStopEjecucion()) {
                    mensajeNoFinalizado = true;
                }
            }
            
            if(mensajeNoFinalizado) {
                mensaje = MESSAGE_SUCCESS;
            } else {
                mensaje = MESSAGE_SUCCESS_FAIL;
            }
            _log.debug(ProcessSubsanaDocumentoProcedimientoPortafirmasAction.class.getName() + "<-- Se realiza el avance del trámite");
        } else {
            mensaje = MESSAGE_NO_PERMITE_SUBSANACION;
        }

        /* Do search */
        PrepareSearchDocumentoPortafirmasAction searchAction =  new PrepareSearchDocumentoPortafirmasAction();
        searchAction.execute(mapping,form,request,response);
       
        /* Save messages */
        saveSingleMessage(request,new ActionMessage(mensaje));

        /* Return ActionForward */
        final ActionForward result;
        if (concreteForm.getDoPopUp()) {
            result = mapping.findForward(searchAction.getPopUpMappingKey());
        } else if (concreteForm.getDoPrintPreview()) {
            result = mapping.findForward(searchAction.getPrintPreviewMappingKey());
        } else {
            result = mapping.findForward(searchAction.getDefaultMappingKey());
        }
        if (_log.isDebugEnabled()) {
            _log.debug(CLSNAME+ ".doPerform() END  Jumping to "+ result.getName() + "-----");
        }
        return result;
    }//doPerform

    protected String getMainPageMapping() {
        return GlobalNames.MAINPAGE_GLOBAL_FORWARD;
    }

    protected String getInternalErrorMapping() {
        return GlobalNames.INTERNALERROR_GLOBAL_FORWARD;
    }

    protected String getModelErrorMapping() {
        return GlobalNames.MODELERROR_GLOBAL_FORWARD;
    }
    
    private GeneralValueObject permiteTramitacionSubsanacion (FirmaDocumentoPortafirmasActionForm form, UsuarioValueObject usuario) 
            throws TechnicalException {
        _log.debug(ProcessSubsanaDocumentoProcedimientoPortafirmasAction.class.getName() + "--> permiteTramitacionSubsanacion");
        GeneralValueObject tramite = null;
        String[] params = usuario.getParamsCon();
        int documento = form.getIdNumeroDocumento();
        int municipio = form.getIdMunicipio();
        String procedimiento = form.getIdProcedimiento();
        int docPresentado = form.getIdPresentado();
        String expediente = form.getIdNumeroExpediente();
        int idNumFirma = form.getIdNumFirma();
        
        boolean permiteSubsanacion = FirmasDocumentoProcedimientoManager.getInstance().permiteSubsanacion(documento, 
                municipio, procedimiento, docPresentado, idNumFirma, params);
        if (permiteSubsanacion) {
            // Si permite Subsanacion tendremos que ver si el expediente cumple las condiciones para ser subsanado.
            tramite = TramitacionExpedientesManager.getInstance().expedientePermiteSubsanacion(expediente, params);
        }
        _log.debug(ProcessSubsanaDocumentoProcedimientoPortafirmasAction.class.getName() + "<-- permiteTramitacionSubsanacion: " + tramite);
        return tramite;
    }
    
    private Vector<TramitacionExpedientesValueObject> obtenerTramitesSiguientes (FirmaDocumentoPortafirmasActionForm form, UsuarioValueObject usuario) 
            throws TechnicalException {
        _log.debug(ProcessSubsanaDocumentoProcedimientoPortafirmasAction.class.getName() + "--> obtenerTramitesSiguientes");
        Vector<TramitacionExpedientesValueObject> listaTramites = null;
        String[] params = usuario.getParamsCon();
        String municipio = String.valueOf(form.getIdMunicipio());
        String procedimiento = form.getIdProcedimiento();
        String tramite = String.valueOf(form.getIdTramite());
        listaTramites = TramitacionExpedientesManager.getInstance().obtenerTramitesSiguientes(municipio, procedimiento, tramite, FLUJO_LISTA_TRAMITES_NO_FAVORABLE, params);
        _log.debug(ProcessSubsanaDocumentoProcedimientoPortafirmasAction.class.getName() + "<-- obtenerTramitesSiguientes: " + listaTramites.size());
        return listaTramites;
    }

    /**
     * Pone en el VO el mensaje adecuado con el error que devuelve un servicio
     * web en una WSException
     * @param tramExpVO
     * @param wse
     */
    private void ponerMensajeFalloSW(TramitacionExpedientesValueObject tramExpVO, WSException wse) {
        String msg = "Fallo en ejecución de servicio web ";
	if (wse.isMandatoryExecution()) {
            msg += "obligatorio. ";
	} else {
            msg += "no obligatorio. ";
	}
	tramExpVO.setMensajeSW(msg + wse.getMessage());
    }

    private void ponerMensajeFalloSW(TramitacionExpedientesValueObject tramExpVO, EjecucionSWException eswe) {
	String msg = "Fallo en ejecución de servicio web obligatorio. ";
	tramExpVO.setMensajeSW(msg + eswe.getMensaje());
    }
    
    
    
    /**
     *  Recupera la unidad tramitadora de un trámite recien iniciado
     * @param codOrganizacion: cod organizacióbn
     * @param numExpediente: Nº del expediente
     * @param codTramite: Código del trámite
     * @param con: Conexión a la BBDD
     * @return Código de la uor
     */
    private String getCodUorTramitadoraTramite(String codOrganizacion,String numExpediente,String codTramite,Connection con){
        
        ResultSet rs = null;
        Statement st = null;
        String codUor = null;
        
        try{
            String[] datos = numExpediente.split("/");
            
            String sql = "SELECT CRO_UTR FROM E_CRO WHERE CRO_NUM='" + numExpediente + "' AND CRO_PRO='" + datos[1] + "' " + 
                        " AND CRO_MUN=" + codOrganizacion + " AND CRO_TRA=" + codTramite + " AND CRO_OCU=1";
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
               codUor = rs.getString("CRO_UTR");
            }
            
            
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }            
        }        
        return codUor;        
    }
    
    
    private boolean notificar(TramitacionExpedientesValueObject tramite,String[] params){
        boolean exito = false;
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
                        
            Vector<GeneralValueObject> interesados = this.getInteresadosExpediente(tramite.getCodOrganizacion(),tramite.getEjercicio(),tramite.getNumeroExpediente(), con);
            _log.debug("Interesados del expediente recuperados: " + interesados.size());
            
            
            String codOrganizacion = tramite.getCodOrganizacion();
            String codProcedimiento = tramite.getCodProcedimiento();
            String numExpediente = tramite.getNumeroExpediente();
            
                        
            Vector<TramitacionExpedientesValueObject> tramitesIniciar = (Vector<TramitacionExpedientesValueObject>)tramite.getListaTramitesIniciar();
            for(int i=0;tramitesIniciar!=null && i<tramitesIniciar.size();i++){
                
                TramitacionExpedientesValueObject tVO = (TramitacionExpedientesValueObject)tramitesIniciar.get(i);
                
                String codTramite = tVO.getCodTramite();
                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo("codOrganizacion",codOrganizacion);
                gVO.setAtributo("codMunicipio",codOrganizacion);
                gVO.setAtributo("codProcedimiento",codProcedimiento);
                
                gVO.setAtributo("codTramite",tVO.getCodTramite());
                gVO.setAtributo("codUORTramiteIniciado",this.getCodUorTramitadoraTramite(codOrganizacion,numExpediente,tramite.getCodTramite(),con));
                gVO.setAtributo("codInteresados",interesados);    
                gVO.setAtributo("usuario",tramite.getCodUsuario());
                                
                EstructuraNotificacion estructura = getMailsUsuariosAlIniciar(gVO,con,params[6]);
                 
                MailsTramitacionDAO mailsDAO= MailsTramitacionDAO.getInstance();
                _log.debug("Antes de enviar el mail al tramite ========>");
                mailsDAO.envioInicioTramite(estructura, numExpediente, codProcedimiento, Integer.parseInt(codTramite),con);
                _log.debug("Después de enviar el mail al tramite ========>");                  
                
            }// for
            
            
        }catch(BDException e){            
            _log.debug("Error al recuperar conexión a la BBDD:: " + e.getMessage());
            e.printStackTrace();
            
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                _log.debug("Error al cerrar la conexión con la BBDD: " + e.getMessage());
            }
        }        
        return exito;
        
    }
    
    
    /** 
     * Recupera la estructura de mail que hay que enviar para notificar el inicio de un trámite
     ***/
    
    private Vector<GeneralValueObject> getInteresadosExpediente(String codOrganizacion,String ejercicio,String numExpediente,Connection con){
        Vector<GeneralValueObject> salida = new Vector<GeneralValueObject>();
        ResultSet rs = null;
        Statement st = null;
        
        
        try{
            
            String sql = "SELECT EXT_TER,EXT_NVR FROM E_EXT WHERE EXT_NUM='" + numExpediente + "' AND EXT_MUN=" + codOrganizacion + " AND EXT_EJE=" + ejercicio;
            _log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                String codTercero = rs.getString("EXT_TER");
                String versionTercero = rs.getString("EXT_NVR");
                
                GeneralValueObject gvo = new GeneralValueObject();
                gvo.setAtributo("codigo",codTercero);
                gvo.setAtributo("version",versionTercero);                
                salida.add(gvo);
            }
            
        }catch(SQLException e){
            e.printStackTrace();                    
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return salida;        
    }
    
    
    
    
   private EstructuraNotificacion getMailsUsuariosAlIniciar(GeneralValueObject gVO,Connection con,String jndi){
        
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = null;
        int resultado=0;
        EstructuraNotificacion eNotif = new EstructuraNotificacion();
        try {
            if(_log.isDebugEnabled()) _log.debug("entra en getMailsUsuariosAlIniciar");

            String codOrganizacion = (String)gVO.getAtributo("codOrganizacion");
            String codMunicipio = (String)gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
            String codTramite = (String) gVO.getAtributo("codTramite");
            String codUORTramiteIniciado = (String) gVO.getAtributo("codUORTramiteIniciado");
            Vector codInteresados = (Vector) gVO.getAtributo("codInteresados");
            String codUsuario = (String) gVO.getAtributo("usuario");
            if(_log.isDebugEnabled()) _log.debug("UOR DEL TRAMITE QUE SE INICIA: " + codUORTramiteIniciado);
            if(_log.isDebugEnabled()) _log.debug("COD INTERESADOS : " + codInteresados);
            if(_log.isDebugEnabled()) _log.debug("COD USUARIO : " + codUsuario);
            String uti = "";
            String usi = "";
            String ini = "";
            String uiti = "";
            String uiei = "";
            String uor_mail = "";
            String uor_usu = "";
            String usu_mail = "";
            String int_mail = "";
            String usu_mail_tramite;
            String usu_mail_exped;
            Vector mailsUOR = new Vector();
            Vector mailsUsusUOR = new Vector();
            Vector mailsInteresados = new Vector();
            String mailUsuInicioTramite ="";
            String mailUsuInicioExpediente = "";
            Vector usuarios = new Vector();

            sql = "SELECT TRA_UTI,TRA_USI,TRA_INI ,TRA_NOTIF_UITI, TRA_NOTIF_UIEI" + 
                    " FROM e_tra WHERE TRA_MUN=" + codMunicipio + " AND TRA_PRO='" + codProcedimiento + "'" + 
                    " AND TRA_COD=" +codTramite;
                    
            if(_log.isDebugEnabled()) _log.debug(sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while(rs.next()) {
                uti = rs.getString("TRA_UTI");
                usi = rs.getString("TRA_USI");
                ini = rs.getString("TRA_INI");
                uiti = rs.getString("TRA_NOTIF_UITI");
                uiei = rs.getString("TRA_NOTIF_UIEI");
                resultado = 1;
            }
            rs.close();
            st.close();
            if (resultado > 0) {

                sql = "SELECT TML_VALOR " + 
                        " FROM E_TML WHERE TML_MUN=" + codMunicipio + " AND TML_PRO='" + codProcedimiento + "' " + 
                        " AND TML_TRA=" +codTramite;
                if(_log.isDebugEnabled()) _log.debug(sql);
                st = con.prepareStatement(sql);
                rs = st.executeQuery();
                while(rs.next()) {
                    eNotif.setNombreTramite(rs.getString("TML_VALOR"));
                }
                rs.close();
                st.close();
                if (uti.equals("S")) { //coger mail de a_uor buscando por uor	
                    UORDTO uorDTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(jndi,codUORTramiteIniciado);	
                    if (uorDTO!=null && uorDTO.getUor_email()!=null && !uorDTO.getUor_email().equals("")) {
                            if(_log.isDebugEnabled()) _log.debug("UNIDAD TRAMITADORA MAIL: "+uor_mail);
                            mailsUOR.addElement(uor_mail);
                        }
                    
                }
                if (usi.equals("S")) {
                    //coger usuarios de a_uou buscando por uor
                    sql = "SELECT UOU_USU FROM " + es.altia.agora.business.util.GlobalNames.ESQUEMA_GENERICO + "a_uou a_uou WHERE " +
                            "UOU_UOR=" + codUORTramiteIniciado + " AND "
                            + "UOU_ORG=" + codOrganizacion + " AND UOU_USU<>" + codUsuario;
                    
                    if(_log.isDebugEnabled()) _log.debug(sql);
                    st = con.prepareStatement(sql);
                    rs = st.executeQuery();
                    while(rs.next()) {
                        uor_usu = rs.getString("UOU_USU");
                        if (!(uor_usu==null) && !(uor_usu.equals(""))) {
                            if(_log.isDebugEnabled()) _log.debug("CODIGO USUARIO: "+uor_usu);
                            usuarios.addElement(uor_usu);
                        }
                    }
                    rs.close();
                    st.close();
                    //coger mail de a_usu buscando por cod_usu
                    for (int i=0;i<usuarios.size();i++) {
                        sql = "SELECT USU_EMAIL FROM " + es.altia.agora.business.util.GlobalNames.ESQUEMA_GENERICO + "a_usu a_usu WHERE " +
                               " USU_COD=" + usuarios.elementAt(i);
                        if(_log.isDebugEnabled()) _log.debug(sql);
                        st = con.prepareStatement(sql);
                        rs = st.executeQuery();
                        while(rs.next()) {
                            usu_mail = rs.getString("USU_EMAIL");
                            if (!(usu_mail==null) && !(usu_mail.equals(""))) {
                                if(_log.isDebugEnabled()) _log.debug("USUARIO MAIL: "+usu_mail);
                                mailsUsusUOR.addElement(usu_mail);
                            }
                        }
                        rs.close();
                        st.close();
                    }
                }
                if (ini.equals("S")) {
                    sql = "SELECT EXT_TER, EXT_NVR FROM E_EXT WHERE EXT_NUM=? AND EXT_MUN=? AND EXT_EJE=?";
                    st = con.prepareStatement(sql);
                    st.setString(1, (String)gVO.getAtributo("numero"));
                    st.setString(2, codMunicipio);
                    st.setString(3, (String)gVO.getAtributo("ejercicio"));

                    _log.debug("Consulta de terceros del expediente: " + sql);
                    rs = st.executeQuery();



                    while (rs.next()) {
                        GeneralValueObject res = new GeneralValueObject();
                        res.setAtributo("codigo", rs.getString("EXT_TER"));
                        res.setAtributo("version", rs.getString("EXT_NVR"));
                        codInteresados.add(res);
                    }

                    //coger mail de t_ter buscando por codInteresados
                    if (codInteresados!=null) {
                        for (int i=0;i<codInteresados.size();i++) {
                            GeneralValueObject resVO = (GeneralValueObject) codInteresados.elementAt(i);
                            sql = "SELECT TER_DCE FROM t_ter WHERE TER_COD " +
                                    "=" + (String) resVO.getAtributo("codigo")+ "AND TER_NVE="+
                                    (String) resVO.getAtributo("version");
                            if(_log.isDebugEnabled()) _log.debug(sql);
                            st = con.prepareStatement(sql);
                            rs = st.executeQuery();
                            while(rs.next()) {
                                int_mail = rs.getString("TER_DCE");
                                if (!(int_mail==null) && !(int_mail.equals(""))) {
                                    if(_log.isDebugEnabled()) _log.debug("INTERESADO MAIL: "+int_mail);
                                    mailsInteresados.addElement(int_mail);
                                }
                            }
                            rs.close();
                            st.close();
                        }
                    }
                }
                if (uiti.equals("S")) {
                    //coger el mail E_CRO
                    sql = "SELECT USU_EMAIL FROM " + es.altia.agora.business.util.GlobalNames.ESQUEMA_GENERICO + "a_usu a_usu "
                            + " A_USU.USU_COD= E_CRO.CRO_USU )"
                            + " WHERE E_CRO.CRO_NUM = ?"
                            + " AND E_CRO.CRO_TRA = ?";
                    st = con.prepareStatement(sql);
                    st.setString(1, (String) gVO.getAtributo("numero"));
                    st.setString(2, codTramite);
                    _log.debug("Consulta Mail Usuario Inicio Tramite" + sql);

                    rs = st.executeQuery();

                    while (rs.next()) {
                        usu_mail_tramite = rs.getString("USU_EMAIL");
                        if (!(usu_mail_tramite == null) && !(usu_mail_tramite.equals(""))) {
                            if (_log.isDebugEnabled()) {
                                _log.debug("USUARIO MAIL: " + usu_mail_tramite);
                            }
                            mailUsuInicioTramite = usu_mail_tramite;
                        }
                    }
                    rs.close();
                    st.close();

                }
                if (uiei.equals("S")) {
                    //coger el mail de usuario inicio expediente 
                    sql = "SELECT USU_EMAIL FROM " + es.altia.agora.business.util.GlobalNames.ESQUEMA_GENERICO + "a_usu a_usu "
                            + " INNER JOIN E_EXP ON (A_USU.USU_COD= E_EXP.EXP_USU )"
                            + " WHERE E_EXP.EXP_NUM = ?";

                    st = con.prepareStatement(sql);
                    st.setString(1, (String) gVO.getAtributo("numero"));

                    _log.debug("Consulta Mail Usuario Inicio Expediente: " + sql);

                    rs = st.executeQuery();

                    while (rs.next()) {
                        usu_mail_exped = rs.getString("USU_EMAIL");
                        if (!(usu_mail_exped == null) && !(usu_mail_exped.equals(""))) {
                            if (_log.isDebugEnabled()) {
                                _log.debug("USUARIO MAIL: " + usu_mail_exped);
                            }
                            mailUsuInicioExpediente = usu_mail_exped;
                        }
                    }
                    rs.close();
                    st.close();

                }
                eNotif.setListaEMailsUOR(mailsUOR);
                eNotif.setListaEMailsUsusUOR(mailsUsusUOR);
                eNotif.setListaEMailsInteresados(mailsInteresados);
                eNotif.setListaEmailsUsuInicioTramite(mailUsuInicioTramite);
                eNotif.setListaEmailsUsuInicioExped(mailUsuInicioExpediente);
            }
            if(_log.isDebugEnabled()) _log.debug("sale de getMailsUsuariosAlIniciar y devuelve: "+eNotif);
        } catch (Exception e) {
            if(_log.isErrorEnabled()) _log.error("Exception: "+e.getMessage());
        }
        return eNotif;
    }
    
    
    
    
    

    
}
