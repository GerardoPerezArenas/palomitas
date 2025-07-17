package es.altia.flexia.interfaces.user.web.carga.parcial.fichaexpediente;

import com.google.gson.Gson;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.AsientoFichaExpedienteVO;
import es.altia.agora.business.sge.DefinicionAgrupacionCamposAgrupadosValueObject;
import es.altia.agora.business.sge.DocumentoInicioExpedienteVO;
import es.altia.agora.business.sge.ExpedienteOtroDocumentoVO;
import es.altia.agora.business.sge.FicheroVO;
import es.altia.agora.business.sge.MetadatosDocumentoVO;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.ValorCampoSuplementarioVO;
import es.altia.agora.business.sge.manager.visorregistro.VisorRegistroManager;
import es.altia.agora.business.sge.persistence.DatosSuplementariosManager;
import es.altia.agora.business.sge.persistence.FichaExpedienteManager;
import es.altia.agora.business.sge.persistence.TramitacionExpedientesManager;
import es.altia.agora.business.sge.persistence.manual.ExpedienteOtroDocumentoDAO;
import es.altia.agora.business.sge.persistence.manual.FichaExpedienteDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.interfaces.user.web.sge.FichaExpedienteForm;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.technical.EstructuraCampoAgrupado;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.business.comunicaciones.persistence.ComunicacionesFlexiaDAO;
import es.altia.flexia.business.comunicaciones.vo.ComunicacionVO;
import es.altia.flexia.business.expediente.vo.EnlaceVO;
import es.altia.flexia.interfaces.user.web.carga.parcial.fichaexpediente.vo.DatosExpedienteVO;
import es.altia.flexia.interfaces.user.web.carga.parcial.fichaexpediente.vo.ResultadoTratamientoCargaParcialExpedienteVO;
import es.altia.flexia.notificacion.plugin.FactoriaPluginNotificacion;
import es.altia.flexia.notificacion.plugin.PluginListadoNotificacion;
import es.altia.flexia.notificacion.plugin.PluginNotificacion;
import es.altia.flexia.notificacion.persistence.NotificacionDAO;
import es.altia.flexia.notificacion.vo.NotificacionVO;
import es.altia.util.commons.StringOperations;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;
import java.net.URLEncoder;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 * Action que es llamada por medio de peticiones AJAX para cargar el contenido de la ficha de expediente,
 * según vaya siendo solicitada desde /jsp/sge/fichaExpediente.jsp
 * @author oscar
 */
public class CargaFichaExpedienteAction extends DispatchAction{
    
    private Logger log = Logger.getLogger(CargaFichaExpedienteAction.class);
    
    /**
     * Método que es llamada cuando se desea recuperar la lista de documentos externos de un expediente
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception 
     */
    public ActionForward cargarDocumentosExternosExpediente(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        
        try{
            String codMunicipio = request.getParameter("codMunicipio");
            String ejercicio    = request.getParameter("ejercicio");
            String numExpediente  = request.getParameter("numero");
            String expHistorico  = request.getParameter("expHistorico");
            
           
            if(StringOperations.stringNoNuloNoVacio(codMunicipio) && StringOperations.stringNoNuloNoVacio(ejercicio) && StringOperations.stringNoNuloNoVacio(numExpediente)){                 
                
                // Se recupera de la sesión el parámetro usuario con la información del usuario logueado
                HttpSession session = request.getSession();
                UsuarioValueObject usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                
                if(usuarioVO!=null){
                
                    String[] params = usuarioVO.getParamsCon();
                    
                    adapt = new AdaptadorSQLBD(params);
                    con = adapt.getConnection();

                    ArrayList<ExpedienteOtroDocumentoVO> documentosExternos = ExpedienteOtroDocumentoDAO.getInstance().listaOtrosDocumentosExpediente(codMunicipio,ejercicio,numExpediente,expHistorico,con);                       
                    if(documentosExternos!=null){                       
                       // Se convierte la colección de documentos externos a JSON                        
                       retornarJSON(new Gson().toJson(documentosExternos),response);
                    }
                }
            }else
                log.warn("Para recuperar la lista de documentos externos del expediente es necesario el código del municipio, ejercicio y número del expediente");
        }catch(BDException e){
            e.printStackTrace();
            log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
        }
        catch(Exception e){
            e.printStackTrace();
            log.error("Error al recupera la lista de documentos externos del expediente: " + e.getMessage());
            
        }finally{
            adapt.devolverConexion(con);
        }
        
        return null;
    }
    
    
    
    
   /**
     * Método que se encarga de recuperar las notificaciones electronicas asociadas al expediente
     * @param mapping: Objeto de tipo ActionMapping
     * @param form: ActionForm con el formulariop
     * @param request: Objeto de tipo HttpServletRequest
     * @param response: Objeto de tipo HttpServletResponse
     * @return ActionForward
     * @throws Exception si ocurre algún error
     */
     public ActionForward cargarNotificacionesExpediente(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        int ejercicio;
        String codProc;
        Config m_Conf = ConfigServiceHelper.getConfig("notificaciones");
        
        try{
            String numExpediente  = request.getParameter("numero");
            String expHistorico  = request.getParameter("expHistorico");            
           
           
            if(StringOperations.stringNoNuloNoVacio(numExpediente)){                                 
                // #279909: 
                if(!numExpediente.contains("/")){
                    log.warn("El numero de expediente indicado es incorrecto");
                    throw new Exception("El numero de expediente indicado es incorrecto");
                } else {
                    String[] partes = numExpediente.split("/");
                    ejercicio = Integer.parseInt(partes[0]);
                    codProc = partes[1];
                    
                    // Se recupera de la sesión el parámetro usuario con la información del usuario logueado
                    HttpSession session = request.getSession();
                    UsuarioValueObject usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                    Config confExpediente = ConfigServiceHelper.getConfig("Expediente");

                    if(usuarioVO!=null){

                        String[] params = usuarioVO.getParamsCon();

                        adapt = new AdaptadorSQLBD(params);
                        con = adapt.getConnection();

                        PluginListadoNotificacion pluginNotificacion = FactoriaPluginNotificacion.getImplListaNotif(Integer.toString(usuarioVO.getOrgCod()));
                 
                        ArrayList<NotificacionVO> notificaciones = pluginNotificacion.getNotificacionesExpediente(usuarioVO.getOrgCod(),usuarioVO.getIdioma(),numExpediente,expHistorico,con);                                
                        
                        String tramiteNotificado  = request.getParameter("tramiteNotificado");
                        ArrayList<TramitacionExpedientesValueObject> tramitesNotificados = null;
                        if(tramiteNotificado!=null && tramiteNotificado.equals("1")){
                            // Recuperamos los trámites notificados sin notificación electrónica
                             tramitesNotificados = TramitacionExpedientesManager.getInstance().getDatosTramitesNotificadosNoE(usuarioVO.getOrgCod(), ejercicio, codProc, numExpediente, Boolean.parseBoolean(expHistorico), params);
                        }
                       
                       HashMap<String,ArrayList> resultado = new HashMap<String,ArrayList>();
                       resultado.put("notificaciones",notificaciones);
                       resultado.put("tramitesNotificados",tramitesNotificados);
                       
                       String json = new Gson().toJson(resultado);
                       retornarJSON(json,response);                   
                    }
                }
            }else
                log.warn("Para recuperar las notificaciones del expediente es necesario el número del expediente");
            
        }catch(BDException e){
            e.printStackTrace();
            log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
        }
        catch(Exception e){
            e.printStackTrace();
            log.error("Error al recuperar las notificacioens del expediente: " + e.getMessage());
            
        }finally{
            adapt.devolverConexion(con);
        }        
        return null;
    }
     
     
     
    /**
     * Método que se encarga de recuperar las comunicaciones asociadas al expediente
     * @param mapping: Objeto de tipo ActionMapping
     * @param form: ActionForm con el formulariop
     * @param request: Objeto de tipo HttpServletRequest
     * @param response: Objeto de tipo HttpServletResponse
     * @return ActionForward
     * @throws Exception si ocurre algún error
     */
     public ActionForward cargarComunicacionesExpediente(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        
        try{
            String numExpediente  = request.getParameter("numero"); 
            String expHistorico  = request.getParameter("expHistorico"); 
           
            if(StringOperations.stringNoNuloNoVacio(numExpediente)){                                 
                // Se recupera de la sesión el parámetro usuario con la información del usuario logueado
                HttpSession session = request.getSession();
                UsuarioValueObject usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                
                if(usuarioVO!=null){
                
                    String[] params = usuarioVO.getParamsCon();
                    
                    adapt = new AdaptadorSQLBD(params);
                    con = adapt.getConnection();

                    String[] datos = numExpediente.split("/");
                    int ejercicio = Integer.parseInt(datos[0]);
                    
                    ArrayList<ComunicacionVO> comunicaciones = (ArrayList<ComunicacionVO>)ComunicacionesFlexiaDAO.getInstance().getComunicaciones(Integer.toString(usuarioVO.getOrgCod()),ejercicio,numExpediente,expHistorico,con);
                    
                    if(comunicaciones!=null){                       
                       // Se convierte la colección de documentos externos a JSON                        
                       retornarJSON(new Gson().toJson(comunicaciones),response);
                    }
                }
            }else
                log.warn("Para recuperar las notificaciones del expediente es necesario el número del expediente");
            
        }catch(BDException e){
            e.printStackTrace();
            log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
        }
        catch(Exception e){
            e.printStackTrace();
            log.error("Error al recuperar las notificacioens del expediente: " + e.getMessage());
            
        }finally{
            adapt.devolverConexion(con);
        }        
        return null;
    } 
     
     
    
     
   /**
     * Método que se encarga de recuperar el contenido de la pestaña "Otros datos", que incluye los enlaces, asientos 
     * y documentos de expediente
     * @param mapping: Objeto de tipo ActionMapping
     * @param form: ActionForm con el formulariop
     * @param request: Objeto de tipo HttpServletRequest
     * @param response: Objeto de tipo HttpServletResponse
     * @return ActionForward
     * @throws Exception si ocurre algún error
     */
     public ActionForward cargarOtrosDatos(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        ResultadoTratamientoCargaParcialExpedienteVO resultado = new ResultadoTratamientoCargaParcialExpedienteVO();
        
        try{
            String numExpediente    = request.getParameter("numero");      
            String ejercicio        = request.getParameter("ejercicio");   
            String codProcedimiento = request.getParameter("codProcedimiento");
            String expHistorico = request.getParameter("expHistorico");
           
           
            if(StringOperations.stringNoNuloNoVacio(numExpediente) && StringOperations.stringNoNuloNoVacio(codProcedimiento) && StringOperations.stringNoNuloNoVacio(ejercicio)){ 
                // Se recupera de la sesión el parámetro usuario con la información del usuario logueado
                HttpSession session = request.getSession();
                UsuarioValueObject usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                
                if(usuarioVO!=null){                
                    String[] params = usuarioVO.getParamsCon();                    
                    adapt = new AdaptadorSQLBD(params);
                    con = adapt.getConnection();
                    
                    FichaExpedienteDAO fichaDAO = FichaExpedienteDAO.getInstance();
                    
                    /** Se recuperan los enlaces del procedimiento, si los hubiese **/
                    ArrayList<EnlaceVO> enlaces = fichaDAO.cargaListaEnlacesActivos(usuarioVO.getOrgCod(),codProcedimiento,con);
                    
                    /**
                     * Se recupera listado de los Documentos Aportados anteriormente por el ciudadano, si los hubiese
                     */
                    ArrayList<FicheroVO> documentosAportados = FichaExpedienteManager.getInstance().cargarListaFicherosAportadosAnterior("E",numExpediente, params);

                    /** Se recuperan los asientos de registro asociados al expediente, si los hubiese **/
                    GeneralValueObject gVO = new GeneralValueObject();
                    gVO.setAtributo("numero",numExpediente);
                     gVO.setAtributo("expHistorico",expHistorico);
                    
                    FichaExpedienteDAO.getInstance().getRegistroRelacionado(gVO,con);				
                    HashMap resultadoConsulta = VisorRegistroManager.getInstance().cargaListaAsientosExpediente(gVO, usuarioVO, params);                
                    ArrayList<AsientoFichaExpedienteVO> asientos = (ArrayList<AsientoFichaExpedienteVO>) resultadoConsulta.get("resultados");
                    ArrayList<String> errores = (ArrayList<String>) resultadoConsulta.get("errores");
                    
                    /*** Se recupera los documentos de inicio de expediente, si los hubiese **/
                    DocumentoInicioExpedienteVO doc = new DocumentoInicioExpedienteVO();
                    doc.setCodOrganizacion(usuarioVO.getOrgCod());
                    doc.setCodProcedimiento(codProcedimiento);
                    doc.setNumExpediente(numExpediente);
                    doc.setEjercicio(ejercicio);
                    doc.setExpedienteHistorico(expHistorico.equalsIgnoreCase("true")?true:false);
        
                    ArrayList<DocumentoInicioExpedienteVO> documentos = fichaDAO.cargaListaDocumentosExpediente(doc, adapt, con);
                    
                    resultado.setStatus(0);
                    resultado.setDescStatus("OK");
                    resultado.setEnlaces(enlaces);
                    resultado.setAsientos(asientos);
                    resultado.setDocumentos(documentos);
                    resultado.setErrores(errores);
                    resultado.setDocumentosAportados(documentosAportados);
                    
                    if(resultado!=null){                       
                       // Se convierte la colección de documentos externos a JSON                        
                       retornarJSON(new Gson().toJson(resultado),response);
                    } 
                }
            }else
                log.warn("Para recuperar las notificaciones del expediente es necesario el número del expediente");
            
        }catch(BDException e){
            e.printStackTrace();
            log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
            resultado.setStatus(1);
            resultado.setDescStatus("ERROR AL CARGAR EL CONTENIDO DE LA PESTAÑA OTROS DATOS: " + e.getMessage());
            retornarJSON(new Gson().toJson(resultado),response);
        }
        catch(Exception e){
            e.printStackTrace();
            log.error("Error al cargar el contenido de la pestaña Otros datos del expediente: " + e.getMessage());
            resultado.setStatus(1);
            resultado.setDescStatus("ERROR AL CARGAR EL CONTENIDO DE LA PESTAÑA OTROS DATOS: " + e.getMessage());
            retornarJSON(new Gson().toJson(resultado),response);
            
        }finally{
            adapt.devolverConexion(con);
        }        
        return null;
    } 
     
     
     
     
   /**
     * Método que se encarga de recuperar la estructura de los datos suplementarios y sus valores para un 
     * determinado expediente
     * @param mapping: Objeto de tipo ActionMapping
     * @param form: ActionForm con el formulariop
     * @param request: Objeto de tipo HttpServletRequest
     * @param response: Objeto de tipo HttpServletResponse
     * @return ActionForward
     * @throws Exception si ocurre algún error
     */
     public ActionForward cargarDatosSuplementarios(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        ResultadoTratamientoCargaParcialExpedienteVO resultado = new ResultadoTratamientoCargaParcialExpedienteVO();
        
        try{
            String numExpediente    = request.getParameter("numero");      
            String ejercicio        = request.getParameter("ejercicio");   
            String codProcedimiento = request.getParameter("codProcedimiento");
            String expHistorico = request.getParameter("expHistorico");
           
            if(StringOperations.stringNoNuloNoVacio(numExpediente) && StringOperations.stringNoNuloNoVacio(codProcedimiento) && StringOperations.stringNoNuloNoVacio(ejercicio)){ 
                // Se recupera de la sesión el parámetro usuario con la información del usuario logueado
                HttpSession session = request.getSession();
                UsuarioValueObject usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                
                if(usuarioVO!=null){                
                    String[] params = usuarioVO.getParamsCon();                    
                    adapt = new AdaptadorSQLBD(params);
                    con = adapt.getConnection();
                    
                    DatosExpedienteVO dato = new DatosExpedienteVO();
                    dato.setCodOrganizacion(usuarioVO.getOrgCod());
                    dato.setEjercicio(Integer.parseInt(ejercicio));
                    dato.setNumExpediente(numExpediente);
                    dato.setCodProcedimiento(codProcedimiento);
                    dato.setCodTramite(null);
                    dato.setDesdeJsp(ConstantesDatos.DESDE_JSP_FICHA_EXPEDIENTE);
                    dato.setConsultaCampos(null);
                    dato.setExpHistorico("true".equals(expHistorico)?true:false);
                    
                               
                    FichaExpedienteDAO fichaDAO = FichaExpedienteDAO.getInstance();                                                            
                    ArrayList<DefinicionAgrupacionCamposAgrupadosValueObject> agrupaciones = fichaDAO.cargaEstructuraAgrupacionCampos(dato,codProcedimiento, adapt,con,params);
                    resultado.setAgrupaciones(agrupaciones);     
                    
                    
                    resultado.setPosicionarCamposSuplementariosExpediente(0);
                    if(fichaDAO.posicionarVistaCamposSuplementariosExpediente(codProcedimiento,con))
                        resultado.setPosicionarCamposSuplementariosExpediente(1);
                    
                    
                    /***** SE GENERA LA LISTA DE NOMBRES DE LOS FICHEROS, SUS ESTADOS, TIPOS MIME Y LONGITUDES YA QUE 
                     ****  SE UTILIZAN PARA VISUALIZAR EL FICHERO **/
                    FichaExpedienteForm fichaExpedienteForm = (FichaExpedienteForm)session.getAttribute("FichaExpedienteForm");
                    if(fichaExpedienteForm!=null){
                        
                        GeneralValueObject nombresFicheros    = new GeneralValueObject();
                        GeneralValueObject longitudesFicheros = new GeneralValueObject();
                        GeneralValueObject tiposFicheros      = new GeneralValueObject();
                        GeneralValueObject estadosFicheros    = new GeneralValueObject();
                        GeneralValueObject metadatosFicheros  = new GeneralValueObject();
                        
                        ArrayList<DefinicionAgrupacionCamposAgrupadosValueObject> agrup = resultado.getAgrupaciones();
                        for(int i=0;agrup!=null && i<agrup.size();i++){
                            
                            ArrayList<EstructuraCampoAgrupado> estructura = agrup.get(i).getEstructura();
                            
                            for(int j=0;estructura!=null && j<estructura.size();j++){                                
                                
                                 if(estructura.get(j).getCodTipoDato().equals("6")){//desplegable
                                    ArrayList descs = new ArrayList(estructura.get(j).getListaDescDesplegable());
                                    Vector nuevasDescs = new Vector();
                                    for(int k=0;k<descs.size();k++){
                                        String valor = (String) descs.get(k);
                                        byte[] isoBytes = valor.getBytes(); 
                                        for(int pos=0;pos<isoBytes.length;pos++){
                                            if(isoBytes[pos]==63){
                                                isoBytes[pos]=(byte) 0xA4;
                                            }
                                        }
                                        String nuevoValor = new String(isoBytes, "ISO-8859-15");
                                         log.info("Valores de desplegable-----> "+nuevoValor);
                                        nuevasDescs.addElement(nuevoValor);
                                   }

                                    estructura.get(j).setListaDescDesplegable(nuevasDescs);
                                }
                                String codCampo   = estructura.get(j).getCodCampo();
                                String codTramite = estructura.get(j).getCodTramite();
                                String ocurrencia = estructura.get(j).getOcurrencia();                                
                                if(estructura.get(j).getCodTipoDato().equals(ConstantesDatos.TIPO_CAMPO_FICHERO_STR)){
                                    ValorCampoSuplementarioVO valor = estructura.get(j).getValorCampo();
                                    if(valor!=null){
                                        
                                        if(codTramite!=null && ocurrencia!=null && codTramite.length()>0 && ocurrencia.length()>0){
                                            codCampo = codCampo + "_" + ocurrencia;
                                        }
                                        
                                        nombresFicheros.setAtributo(codCampo,(String)valor.getValorDatoSuplementario());
                                        longitudesFicheros.setAtributo(codCampo,(Integer)valor.getLongitudFichero());
                                        tiposFicheros.setAtributo(codCampo,(String)valor.getTipoMimeFichero());                                        
                                        estadosFicheros.setAtributo(codCampo,ConstantesDatos.ESTADO_DOCUMENTO_GRABADO);                                         
                                        metadatosFicheros.setAtributo(codCampo, valor.getMetadatosFichero());                                         
                                    }
                                }
                                
                            }// for

                            fichaExpedienteForm.setListaNombreFicheros(nombresFicheros);
                            fichaExpedienteForm.setListaLongitudFicherosDisco(longitudesFicheros);
                            fichaExpedienteForm.setListaTiposFicheros(tiposFicheros);
                            fichaExpedienteForm.setListaEstadoFicheros(estadosFicheros);
                            fichaExpedienteForm.setListaMetadatosFicheros(metadatosFicheros);
                            
                             agrup.get(i).setEstructura(estructura);
                        }
                        resultado.setAgrupaciones(agrup);
                    }
                    
                    if(resultado!=null){                       
                       // Se convierte la colección de documentos externos a JSON  
                        
                       retornarJSON(new Gson().toJson(resultado),response);
                    } 
                }
            }else
                log.warn("Para recuperar las notificaciones del expediente es necesario el número del expediente");
            
        }catch(BDException e){
            e.printStackTrace();
            log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
            resultado.setStatus(1);
            resultado.setDescStatus("ERROR AL CARGAR EL CONTENIDO DE LA PESTAÑA OTROS DATOS: " + e.getMessage());
            retornarJSON(new Gson().toJson(resultado),response);
        }
        catch(Exception e){
            e.printStackTrace();
            log.error("Error al cargar el contenido de la pestaña Otros datos del expediente: " + e.getMessage());
            resultado.setStatus(1);
            resultado.setDescStatus("ERROR AL CARGAR EL CONTENIDO DE LA PESTAÑA OTROS DATOS: " + e.getMessage());
            retornarJSON(new Gson().toJson(resultado),response);
            
        }finally{
            adapt.devolverConexion(con);
        }        
        return null;
    }      
    
     
    /**
     * Método que es llamado para recuperar la lista de campos de expediente, de tipo numérico y de fecha, para actualizar el formato de los 
     * valores de los númericos, y las alarmas para los campos de tipo fecha, respectivamente.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return 
     * @throws Exception 
     */
    public ActionForward cargarCamposExpedienteActualizarEnFicha(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        
        try{
            String numExpediente  = request.getParameter("numero");            
           
            if(StringOperations.stringNoNuloNoVacio(numExpediente)){                                 
                // Se recupera de la sesión el parámetro usuario con la información del usuario logueado
                HttpSession session = request.getSession();
                UsuarioValueObject usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                
                if(usuarioVO!=null){
                
                    String[] params = usuarioVO.getParamsCon();
                    
                    adapt = new AdaptadorSQLBD(params);
                    con = adapt.getConnection();

                    
                    ArrayList<ValorCampoSuplementarioVO> campos = DatosSuplementariosManager.getInstance().getCamposExpedienteActualizarEnFicha(usuarioVO.getOrgCod(),numExpediente,params);
                    
                    if(campos!=null){                       
                       // Se convierte la colección de campos suplementarios a JSON                        
                       retornarJSON(new Gson().toJson(campos),response);
                    } 
                }
            }else
                log.warn("Para recuperar las notificaciones del expediente es necesario el número del expediente");
            
        }catch(BDException e){
            e.printStackTrace();
            log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
        }
        catch(Exception e){
            e.printStackTrace();
            log.error("Error al recuperar las notificacioens del expediente: " + e.getMessage());
            
        }finally{
            adapt.devolverConexion(con);
        }        
        return null;
    }
     
     
     
     
    
    /**
     * Método llamado para devolver un String en formato JSON al cliente que ha realiza la petición 
     * a alguna de las operaciones de este action
     * @param json: String que contiene el JSON a devolver
     * @param response: Objeto de tipo HttpServletResponse a través del cual se devuelve la salida
     * al cliente que ha realizado la solicitud
     */
    private void retornarJSON(String json,HttpServletResponse response){
        
        try{
            if(json!=null){
                response.setCharacterEncoding("UTF-8");                
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
                out.close();
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    
    
    
    
    
}
