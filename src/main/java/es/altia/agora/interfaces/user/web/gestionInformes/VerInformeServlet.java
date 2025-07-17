package es.altia.agora.interfaces.user.web.gestionInformes;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.agora.business.gestionInformes.SolicitudInformeValueObject;
import es.altia.agora.business.gestionInformes.persistence.SolicitudesInformesManager;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.persistence.AuditoriaManager;
import es.altia.common.service.auditoria.ConstantesAuditoria;

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
import org.apache.commons.lang.StringUtils;

public class VerInformeServlet extends HttpServlet {

    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("common");

    protected static Log log =
            LogFactory.getLog(VerInformeServlet.class.getName());

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        log.debug("INICIALIZANDO JFreeReportBoot.............................");
        //JFreeReportBoot.getInstance().start();
    }

    public void defaultAction(HttpServletRequest req, HttpServletResponse res) throws Exception {
        try {

            String tipoInforme = req.getParameter("tipo");
            HttpSession session = req.getSession(false);
            UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
            
            if (tipoInforme == null) {

                String codigoSolicitud = req.getParameter("codigoSolicitud");
                SolicitudInformeValueObject solicitudInformeVO = SolicitudesInformesManager.getInstance().obtenerSolicitud(usuario.getParamsCon(), codigoSolicitud);
                String fichero = solicitudInformeVO.getFichero();
                File file = new File(fichero);
                FileInputStream fIS = new FileInputStream(file);

                String nombre = fichero.substring(fichero.lastIndexOf("/") + 1, fichero.length());
                ServletOutputStream out = res.getOutputStream();

                res.setHeader("Content-Disposition", "inline;filename=\"" + nombre + "\"");
                res.setHeader("Content-Transfer-Encoding", "binary");
                
                if (solicitudInformeVO.getFormato().equals("PDF"))
                res.setContentType("application/pdf");
                else if (solicitudInformeVO.getFormato().equals("HTML"))
                    res.setContentType("text/html");
                else if (solicitudInformeVO.getFormato().equals("EXCEL") || 
                        solicitudInformeVO.getFormato().equals("CSV"))
                    res.setContentType("application/vnd.ms-excel");
                
                // Auditoria de acceso
                if (StringUtils.isNotEmpty(solicitudInformeVO.getCadenaCriterios())) {
                    solicitudInformeVO.setCadenaCriterios(
                            solicitudInformeVO.getCadenaCriterios().replaceAll(
                                    ConstantesAuditoria.EXPEDIENTE_SOLICITUD_INFORME_CRITERIO_NEWLINE,
                                    ConstantesAuditoria.EXPEDIENTE_SOLICITUD_INFORME_CRITERIO_SUSTITUIR_NEWLINE));
                }
                AuditoriaManager.getInstance().auditarAccesoSolicitudInforme(
                        ConstantesAuditoria.EXPEDIENTE_SOLICITUD_VER_INFORME_BUZON,
                        usuario, solicitudInformeVO);
                
                boolean eof = false;
                while (!eof) {
                    int buffer = fIS.read();
                    eof = (buffer == -1);
                    if (!eof)
                        out.write(buffer);
                }
                out.flush();
                out.close();
                fIS.close();

                /**
                 * Una vez que mostramos el informe, comprobamos si se realizó en modo directo.
                 * De ser así, eliminamos la solicitud.
                 */
                if (null!= req.getParameter("modo") && "directo".equals(req.getParameter("modo"))){
                    String[] params = usuario.getParamsCon();
                    SolicitudesInformesManager.getInstance().eliminarSolicitud(params, codigoSolicitud);
                }


            } else if (tipoInforme.equals("informeParticular")) {
                String rutaInforme = req.getParameter("ruta");
                String formato = req.getParameter("formato");
                log.debug("ABRIMOS EL FICHERO: " + rutaInforme + " CON FORMATO " + formato);


                FileInputStream fIS = null;
                ServletOutputStream out = null;
                
                if(formato.equals("EXCEL")){
                    File file = new File(m_ConfigTechnical.getString("PDF.base_dir") + File.separator
                    		+ rutaInforme);
                    fIS = new FileInputStream(file);

                    String nombre = rutaInforme.substring(rutaInforme.lastIndexOf("/") + 1, rutaInforme.length());
                    log.debug("UBICADO EN: " + rutaInforme);
                    out = res.getOutputStream();

                    res.setHeader("Content-Disposition", "attachment;filename=\"" + nombre + "\"");
                    res.setHeader("Content-Transfer-Encoding", "binary");
                    
                	res.setContentType("application/vnd.ms-excel");
                	}
                else if(formato.equals("PDF")){ 
                    File file = new File(m_ConfigTechnical.getString("PDF.base_dir") + File.separator
                    		+ usuario.getDtr() + File.separator + "pdf" + File.separator+ rutaInforme);
                    fIS = new FileInputStream(file);

                    String nombre = rutaInforme.substring(rutaInforme.lastIndexOf("/") + 1, rutaInforme.length());
                    log.debug("UBICADO EN: " + rutaInforme);
                    out = res.getOutputStream();

                    res.setHeader("Content-Disposition", "attachment;filename=\"" + nombre + "\"");
                    res.setHeader("Content-Transfer-Encoding", "binary");
                    
                	res.setContentType("application/pdf");
                	}
                
                boolean eof = false;
                while (!eof) {
                    int buffer = fIS.read();
                    eof = (buffer == -1);
                    if (!eof)
                        out.write(buffer);
                }
                out.flush();
                out.close();
                fIS.close();

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
            e.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            defaultAction(req, res);
        } catch (Exception e) {
            if (log.isErrorEnabled()) log.error("ERROR doPost:" + e);
            e.printStackTrace();
        }
    }
}
