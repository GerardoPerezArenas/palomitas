package es.altia.agora.interfaces.user.web.sge;

import com.itextpdf.text.pdf.codec.Base64;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.flexia.business.comunicaciones.AdjuntoComunicacionFlexiaManager;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.business.comunicaciones.vo.AdjuntoComunicacionVO;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;

public class DescargaAdjuntoComunicacionServlet extends HttpServlet {

    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("common");

    protected static Log log =
            LogFactory.getLog(DescargaAdjuntoComunicacionServlet.class.getName());

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        log.debug("INICIALIZANDO JFreeReportBoot.............................");
        //JFreeReportBoot.getInstance().start();
    }

    public void defaultAction(HttpServletRequest req, HttpServletResponse res) throws Exception {
        try {

            HttpSession session = req.getSession();
            
            String idCom = req.getParameter("idCom");
            String idAdj = req.getParameter("idAdj");
            
            UsuarioValueObject usuarioVO = null;    
            String[] params = null;
		
            if(session.getAttribute("usuario") != null){
			usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");	
			params = usuarioVO.getParamsCon();
            }
            
            AdjuntoComunicacionVO adjunto =   AdjuntoComunicacionFlexiaManager.getInstance().getAdjunto(Long.parseLong(idCom), Long.parseLong(idAdj), params);
            if (adjunto!=null){
                 String nombreFichero = adjunto.getNombre() ;
                String tipoContenido =  "application/x-download";
                res.setContentType(tipoContenido);

                if (adjunto.getContenido()!= null){
                    byte[] contenido = Base64.decode(adjunto.getContenido());

                    res.setHeader("Content-Disposition", "attachment; filename=" + nombreFichero);
                    ServletOutputStream out = res.getOutputStream();
                    res.setContentLength(contenido.length);
                    BufferedOutputStream bos = new BufferedOutputStream(out);
                    bos.write(contenido, 0, contenido.length);
                    bos.close(); 
                }
           }
            

        } catch (Exception e) {
            if (log.isErrorEnabled()) log.error("Excepcion en catch de VerInformeServlet.defaultAction " + e.getMessage());
            throw e;
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            defaultAction(req, res);
        } catch (Exception e) {
            if (log.isErrorEnabled()) log.error("ERROR doGet:" + e);
           
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            defaultAction(req, res);
        } catch (Exception e) {
            if (log.isErrorEnabled()) log.error("ERROR doPost:" + e);
            
        }
    }
}
