/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.interfaces.user.web.sge.documentofirma;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoCustomVO;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoFirmaPK;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoFirmaVO;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoProcedimientoFirmaVO;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoRelacionFirmaPK;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoRelacionFirmaVO;
import es.altia.agora.business.portafirmas.documentofirmafacade.DocumentoFirmaFacadeDelegate;
import es.altia.agora.business.portafirmas.documentofirmafacade.DocumentoRelacionFirmaFacadeDelegate;
import es.altia.agora.business.portafirmas.persistence.manual.DocsProcedimientoPortafirmasManager;
import es.altia.agora.business.portafirmas.persistence.manual.EDocExtPortafirmasManager;
import es.altia.agora.business.sge.persistence.FichaExpedienteManager;
import es.altia.agora.interfaces.user.web.sge.FichaExpedienteForm;
import es.altia.flexia.notificacion.persistence.AdjuntoNotificacionManager;
import es.altia.flexia.notificacion.vo.AdjuntoNotificacionVO;
import es.altia.util.facades.BusinessFacadeDelegateFactory;
import es.altia.util.persistance.searchcriterias.SearchCriteria;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Tiffany
 */
public class ViewBinarioFirma extends HttpServlet {

    protected static Log log =
            LogFactory.getLog(ViewBinarioFirma.class.getName());

    public void defaultAction(HttpServletRequest req, HttpServletResponse res) throws Exception {

        log.debug("EJECUTANDO SERVLET: VerDocumentoServlet");        

        HttpSession session = req.getSession();
        if (session == null) {
            log.error("SESION NULA EN VerDocumentoServlet.defaultAction");
            return;
        }        
        String tipoContenido = "application/octet-stream";
        byte[] fichero = null;

        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        String[] params = usuario.getParamsCon();


        /*Leo los parámetros del request para la descarga de la firma*/
        int tipoDocumento = Integer.valueOf(req.getParameter("tipoDocumento"));
        String municipio = req.getParameter("idMunicipio");
        String procedimiento = req.getParameter("idProcedimiento");
        String ejercicio = req.getParameter("idEjercicio");
        String expediente = req.getParameter("idNumeroExpediente");
        String tramite = req.getParameter("idTramite");
        String ocurrencia = req.getParameter("idOcurrenciaTramite");
        String codDocumento = req.getParameter("idNumeroDocumento");
        String numeroFirmaDocProcedimiento = req.getParameter("idNumFirma");
        log.debug("parametros="+ tipoDocumento + "-" + municipio + "-" + procedimiento + "-" + ejercicio +
                "-" + expediente + "-" + tramite + "-" + codDocumento + "-" + numeroFirmaDocProcedimiento);

        if (tipoDocumento==DocumentoCustomVO.DOCUMENTO_EXPEDIENTE) {
            DocumentoFirmaFacadeDelegate facade = (DocumentoFirmaFacadeDelegate) BusinessFacadeDelegateFactory.getFacadeDelegate(DocumentoFirmaFacadeDelegate.class);
                facade.setDsKey(params[params.length - 1]);
                DocumentoFirmaPK pk = new DocumentoFirmaPK(Integer.valueOf(municipio), procedimiento, 
                        Integer.valueOf(ejercicio), expediente,
                        Integer.valueOf(tramite),Integer.valueOf(ocurrencia),
                        Integer.valueOf(codDocumento), -1);
              
               //FichaExpedienteForm fichaForm = (FichaExpedienteForm)req.getSession().getAttribute("FichaExpedienteForm");                
               SearchCriteria searchCriteria = facade.scDocumentoFirmaByDocumento(pk);
               //searchCriteria.setExpedienteHistorico(fichaForm.isExpHistorico());
              
               /***********************/
                /*
                 * 0 --> Expediente activo, 1 --> Expediente historico, 2 --> Expediente no existe
                 */
                boolean estaHistorico = false;
                int historico = FichaExpedienteManager.getInstance().estaExpedienteHistorico(Integer.parseInt(ejercicio),expediente, params);                
                switch(historico){
                    case 0: estaHistorico = false;
                            break;
                    case 1: estaHistorico = true;
                            break;
                    default: estaHistorico = false;
                            break;                        
                }// switch                
                searchCriteria.setExpedienteHistorico(estaHistorico);
               /**********************/
               
               DocumentoFirmaVO docu= (DocumentoFirmaVO) facade.findDocumentoFirma(searchCriteria,facade.ocDocumentoFirmaByEstadoFirma(true), -1, -1).get(0);                               
               fichero = docu.getFirma();
               
        } else if (tipoDocumento==DocumentoCustomVO.DOCUMENTO_RELACION) {
            
            DocumentoRelacionFirmaFacadeDelegate facade = (DocumentoRelacionFirmaFacadeDelegate) BusinessFacadeDelegateFactory.getFacadeDelegate(DocumentoRelacionFirmaFacadeDelegate.class);
                facade.setDsKey(params[params.length - 1]);
                DocumentoRelacionFirmaPK pk = new DocumentoRelacionFirmaPK (Integer.valueOf(municipio), 
                        procedimiento, 
                        Integer.valueOf(ejercicio), expediente,
                        Integer.valueOf(tramite),Integer.valueOf(ocurrencia), 
                        Integer.valueOf(codDocumento), -1);
                List fff = facade.findDocumentoFirma(facade.scDocumentoFirmaByDocumento(pk), facade.ocDocumentoFirmaByEstadoFirma(true), -1, -1);
                DocumentoRelacionFirmaVO dd = (DocumentoRelacionFirmaVO) fff.get(0);
                fichero = dd.getFirma();
            
        } else if (tipoDocumento==DocumentoCustomVO.DOCUMENTO_EXTERNO) {
            fichero = EDocExtPortafirmasManager.getInstance().getDocumento
                        (numeroFirmaDocProcedimiento, params).getFirma();
        } else if (tipoDocumento==DocumentoCustomVO.DOCUMENTO_PROCEDIMIENTO) {
            ArrayList<DocumentoProcedimientoFirmaVO> resultado = 
                    DocsProcedimientoPortafirmasManager.getInstance().getDocumentoFirma(
                    numeroFirmaDocProcedimiento, params);
            fichero = resultado.get(0).getFirma();
                 
        }else if (tipoDocumento==DocumentoCustomVO.DOCUMENTO_EXTERNO_NOTIFICACION) {                        
            log.debug("************** codDocumento: " + codDocumento);
            
            AdjuntoNotificacionVO doc = AdjuntoNotificacionManager.getInstance().getDocumentoExternoNotificacion(Integer.parseInt(codDocumento), params);
            if(doc!=null && doc.getFirma()!=null)
                fichero = doc.getFirma().getBytes();
            /*
            ArrayList<DocumentoProcedimientoFirmaVO> resultado = 
                    DocsProcedimientoPortafirmasManager.getInstance().getDocumentoFirma(
                    numeroFirmaDocProcedimiento, params);
            fichero = resultado.get(0).getFirma();            */      
        }



        if (fichero != null) {
            BufferedOutputStream bos = null;
            try {

                res.setContentType(tipoContenido);
                res.setHeader("Content-Disposition", "inline; filename=FIRMA");
                res.setHeader("Content-Transfer-Encoding", "binary");

                ServletOutputStream out = res.getOutputStream();
                res.setContentLength(fichero.length);
                bos = new BufferedOutputStream(out);
                bos.write(fichero, 0, fichero.length);
                bos.flush();
                bos.close();


            } catch (Exception e) {
                if (log.isDebugEnabled()) {
                    log.debug("Excepcion en catch de VerDocumentoServlet.defaultAction()");
                }
                throw e;
            } finally {
                if (bos != null) {
                    bos.close();
                }
            }
        } else {
            log.error("FICHERO NULO EN VerDocumentoServlet.defaultAction");
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            if (log.isErrorEnabled()) {
                log.info("Entrando en el servlet VerDocumentosServlet");
            }
            defaultAction(req, res);
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("ERROR doGet:" + e);
            }
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            defaultAction(req, res);
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("ERROR doGet:" + e);
            }
        }
    }
}
