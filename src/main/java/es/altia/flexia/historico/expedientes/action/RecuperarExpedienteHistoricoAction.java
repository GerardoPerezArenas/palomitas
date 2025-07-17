/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.historico.expedientes.action;

import com.google.gson.Gson;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.OperacionExpedienteVO;
import es.altia.agora.business.sge.ValorCampoSuplementarioVO;
import es.altia.flexia.historico.expediente.vo.AdjuntoComunicacionVO;
import es.altia.flexia.historico.expediente.vo.AdjuntoExtNotificacionVO;
import es.altia.flexia.historico.expediente.vo.AdjuntoNotificacionVO;
import es.altia.flexia.historico.expediente.vo.AutorizadoNotificacionVO;
import es.altia.flexia.historico.expediente.vo.ComunicacionVO;
import es.altia.flexia.historico.expediente.vo.CronoVO;
import es.altia.flexia.historico.expediente.vo.DocExtVO;
import es.altia.flexia.historico.expediente.vo.DocsFirmasVO;
import es.altia.flexia.historico.expediente.vo.DocumentoPresentadoVO;
import es.altia.flexia.historico.expediente.vo.DocumentoTramitacionFirmaVO;
import es.altia.flexia.historico.expediente.vo.DocumentoTramitacionFirmantesVO;
import es.altia.flexia.historico.expediente.vo.DocumentoTramitacionFlujoVO;
import es.altia.flexia.historico.expediente.vo.DocumentoTramitacionVO;
import es.altia.flexia.historico.expediente.vo.ExpedienteVO;
import es.altia.flexia.historico.expediente.vo.InteresadoExpedienteVO;
import es.altia.flexia.historico.expediente.vo.ListTramOrigVO;
import es.altia.flexia.historico.expediente.vo.NotificacionVO;
import es.altia.flexia.historico.expediente.vo.RegistroRelacionadoExternoVO;
import es.altia.flexia.historico.expediente.vo.RegistroRelacionadoVO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import es.altia.flexia.historico.expediente.vo.SalidaRecuperarExpedienteHistoricoVO;
import es.altia.flexia.historico.expediente.vo.SituacionExpedienteAnuladoVO;
import es.altia.flexia.historico.expedientes.dao.ExpedienteDAO;
import es.altia.flexia.historico.expedientes.dao.HistoricoExpedienteDAO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import javax.servlet.http.HttpSession;

/**
 *
 * @author altia
 */
public class RecuperarExpedienteHistoricoAction extends DispatchAction{
    
    private Logger log = Logger.getLogger(RecuperarExpedienteHistoricoAction.class);

    public ActionForward recuperarExpedienteHistorico(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        SalidaRecuperarExpedienteHistoricoVO salida = new SalidaRecuperarExpedienteHistoricoVO(); 
        Gson gson = new Gson();
        String numExpediente = request.getParameter("numExp");
        
        try{
            HttpSession session = request.getSession();
            UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
            if(usuario!=null){
                String[] params = usuario.getParamsCon();
                adapt = new AdaptadorSQLBD(params);
                con = adapt.getConnection();
                adapt.inicioTransaccion(con);
                HistoricoExpedienteDAO histExpDAO = HistoricoExpedienteDAO.getInstance();
                
                ExpedienteVO expediente = histExpDAO.getExpedienteHistorico(usuario.getOrgCod(), numExpediente, con);
                
                if (expediente == null) {
                    salida.setStatus(4);
                    salida.setDescStatus("Expediente inexistente en histórico");              
                } else {
                    ArrayList<CronoVO> cronogramasTramites = histExpDAO.getTramitesExpedienteHistorico(usuario.getOrgCod(), numExpediente, con);
                    
                    ArrayList<ListTramOrigVO> tramitesOrigen = histExpDAO.getListaTramitesOrigenHistorico(usuario.getOrgCod(), numExpediente, con);
   
                    ArrayList<InteresadoExpedienteVO> interesados = histExpDAO.getListaInteresadoExpedienteHistorico(usuario.getOrgCod(), numExpediente, con);
                    
                    ArrayList<DocumentoPresentadoVO> documentosPresentados = histExpDAO.getListaDocumentosPresentadosHistorico(usuario.getOrgCod(), numExpediente, con);
   
                    for (DocumentoPresentadoVO doc : documentosPresentados) {
                        ArrayList<DocsFirmasVO> firmasAux =  histExpDAO.getListaFirmasDocumentoPresentadoHistorico(doc.getCodDocumentoPresentado(), con);
                        if (firmasAux!=null)
                            doc.setFirmas(firmasAux);
                    }
                    
                    ArrayList<DocExtVO> documentosExternos = histExpDAO.getListaDocumentosExternosHistorico(usuario.getOrgCod(), numExpediente, con);
   
                    ArrayList<NotificacionVO> notificaciones = histExpDAO.getListaNotificacionesHistorico(usuario.getOrgCod(), numExpediente, con);
   
                    for (NotificacionVO notif : notificaciones) {
                        ArrayList<AdjuntoNotificacionVO> adjuntos =  histExpDAO.getListaAdjuntosNotificacionHistorico(notif.getCodNotificacion(), con);
                        if (adjuntos!=null)
                            notif.setAdjuntos(adjuntos);
                        
                        ArrayList<AdjuntoExtNotificacionVO> adjuntosExt =  histExpDAO.getListaAdjuntosExternosNotificacionHistorico(notif.getCodNotificacion(), con);
                        if (adjuntosExt!=null)
                            notif.setAdjuntosExternos(adjuntosExt);
                        
                        ArrayList<AutorizadoNotificacionVO> autorizadosNotif =  histExpDAO.getListaAutorizadosNotificacionHistorico(notif.getCodNotificacion(), con);
                        if (autorizadosNotif!=null)
                            notif.setAutorizados(autorizadosNotif);
                    }
                    
                    ArrayList<ComunicacionVO> comunicaciones = histExpDAO.getListaComunicacionesHistorico(usuario.getOrgCod(), numExpediente, con);
                    
                    for (ComunicacionVO com : comunicaciones) {
                        ArrayList<AdjuntoComunicacionVO> adjuntosCom =  histExpDAO.getListaAdjuntosComunicacionHistorico(com.getId(), con);
                        if (adjuntosCom!=null)
                            com.setAdjuntos(adjuntosCom);
                    }
                    
                    ArrayList<RegistroRelacionadoVO> registros = histExpDAO.getListaRegistroRelacionadoHistorico(usuario.getOrgCod(), numExpediente, con);

                    ArrayList<RegistroRelacionadoExternoVO> registrosExternos = histExpDAO.getListaRegistroRelacionadoExternoHistorico(usuario.getOrgCod(), numExpediente, con);

                    ArrayList<DocumentoTramitacionVO> documentosTramitacion = histExpDAO.getListaDocumentosTramitacionHistorico(usuario.getOrgCod(), numExpediente, con);

                    for (DocumentoTramitacionVO docTra : documentosTramitacion) {
                        ArrayList<DocumentoTramitacionFirmaVO> adjuntosCom =  histExpDAO.getFirmaDocumentoTramitacionHistorico(usuario.getOrgCod(), 
                                numExpediente,docTra.getCodTramite(),docTra.getOcurrenciaTramite(), docTra.getNumDocumento(),con);
                        if (adjuntosCom!=null)
                            docTra.setFirmas(adjuntosCom);
						
                        ArrayList<DocumentoTramitacionFirmantesVO> listaFirmantes =  histExpDAO.getUsuFirmantesDocumentoTramitacionHistorico(usuario.getOrgCod(), 
                                numExpediente,docTra.getCodTramite(),docTra.getOcurrenciaTramite(), docTra.getNumDocumento(),con);
                        if (listaFirmantes!=null)
                            docTra.setUsuariosFirmantes(listaFirmantes);
						
                        ArrayList<DocumentoTramitacionFlujoVO> listaFlujos =  histExpDAO.getFirmaFlujoDocumentoTramitacionHistorico(usuario.getOrgCod(), 
                                numExpediente,docTra.getCodTramite(),docTra.getOcurrenciaTramite(), docTra.getNumDocumento(),con);
                        if (listaFlujos!=null)
                            docTra.setFirmaFlujo(listaFlujos);
                    }
					
					//TODO tener en cuenta datos de flujo y circuito de firmas.
					//TODO tener en cuenta las tablas: E_DEF_FIRMA, FIRMA_FLUJO, FIRMA_CIRCUITO, ¿FIRMA_TIPO?
					//TODO tener en cuenta metadatos de documentos
                    
                    SituacionExpedienteAnuladoVO situacionExp = histExpDAO.getSituacionExpedienteAnulado(usuario.getOrgCod(), numExpediente, con);
                    
                    ArrayList<OperacionExpedienteVO> operaciones = histExpDAO.getListaOperacionesHist(usuario.getOrgCod(), numExpediente, con);
                    
                    ArrayList<ValorCampoSuplementarioVO> valoresDatosSup = histExpDAO.getValoresDatosSuplementarios(usuario.getOrgCod(),
                            numExpediente, adapt, con);
                    
                    //Se pasa a grabar en las tablas de expedientes activos
                    ExpedienteDAO expDAO = ExpedienteDAO.getInstance();
                    
                    expDAO.grabarExpediente(expediente, con);
                    expDAO.grabarTramitesExpediente(cronogramasTramites, con);
                    expDAO.grabarListaTramitesOrigen(tramitesOrigen, con);
                    expDAO.grabarListaInteresadoExpediente(interesados, con);
                    
                    expDAO.grabarListaDocumentosPresentados(documentosPresentados, con);
                    
                    for (DocumentoPresentadoVO doc : documentosPresentados) {
                        if (doc.getFirmas()!=null && !doc.getFirmas().isEmpty())
                            expDAO.grabarListaFirmasDocumentoPresentado(doc.getFirmas(),con);
                    }

                    expDAO.grabarListaDocumentosExternos(documentosExternos, con);
                    
                    expDAO.grabarListaNotificaciones(notificaciones, con);
                    
                    for (NotificacionVO notif : notificaciones) {
                        expDAO.grabarListaAdjuntosNotificacion(notif.getAdjuntos(), con);
                        expDAO.grabarListaAdjuntosExternosNotificacion(notif.getAdjuntosExternos(), con);
                        expDAO.grabarListaAutorizadosNotificacion(notif.getAutorizados(), con);
                    }
                    
                    expDAO.grabarListaComunicaciones(comunicaciones, con);
                    
                    for (ComunicacionVO com : comunicaciones) {
                        expDAO.grabarListaAdjuntosComunicacion(com.getAdjuntos(), con);
                    }
                    
                    expDAO.grabarListaRegistroRelacionado(registros, con);
                    expDAO.grabarListaRegistroRelacionadoExterno(registrosExternos, con);
                    
                    expDAO.grabarListaDocumentosTramitacion(documentosTramitacion, con);
                    
                    for (DocumentoTramitacionVO docTra : documentosTramitacion) {
                        expDAO.grabarFirmaDocumentoTramitacion(docTra.getFirmas(), con);
                        expDAO.grabarUsuFirmantesDocumentoTramitacion(docTra.getUsuariosFirmantes(), con);
                        expDAO.grabarFirmaFlujoDocumentoTramitacion(docTra.getFirmaFlujo(), con);
                    }
                    
					//TODO tener en cuenta datos de flujo y circuito de firmas.
					//TODO tener en cuenta las tablas: E_DEF_FIRMA, FIRMA_FLUJO, FIRMA_CIRCUITO, ¿FIRMA_TIPO?
					//TODO tener en cuenta metadatos de documentos
					
                    if (situacionExp != null)
                        expDAO.grabarSituacionExpedienteAnulado(situacionExp, con);
                    
                    expDAO.grabarOperaciones(operaciones, con);
                            
                    expDAO.grabarValoresDatosSuplementarios(valoresDatosSup, adapt, con); 
                    
                    //Se procede a eliminar el expediente de histórico a través de clave externa con borrado en cascada
                    histExpDAO.eliminarExpedienteHistorico(expediente.getIdProceso(), usuario.getIdUsuario(), con,params);
                    
                    adapt.finTransaccion(con);
                    salida.setStatus(0);
                    salida.setDescStatus("OK");
                }
            }
        }catch(BDException e){
            e.printStackTrace();
            salida.setStatus(2);
            salida.setDescStatus("Error al obtener una conexión a la BBDD");
            try {   // Se deshace la transacción
                adapt.rollBack(con);
            } catch (BDException ex) {
                ex.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
            salida.setStatus(3);
            salida.setDescStatus("Error al recuperrar expediente desde histórico");
            try {   // Se deshace la transacción
                adapt.rollBack(con);
            } catch (BDException ex) {
                ex.printStackTrace();
            }
        }
        
        retornarJSON(gson.toJson(salida),response);
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
