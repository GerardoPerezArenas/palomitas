package es.altia.agora.interfaces.user.web.administracion.mantenimiento;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import es.altia.common.service.config.*;
import es.altia.agora.business.administracion.mantenimiento.UsuariosGruposValueObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.upload.FormFile;


public class VerFicheroFirmaServlet extends HttpServlet
{
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("common");

    protected static Log log =
            LogFactory.getLog(VerFicheroFirmaServlet.class.getName());

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }
  public void defaultAction(HttpServletRequest req,HttpServletResponse res) throws Exception {

      HttpSession session = req.getSession(false);
      byte[] fichero=null;
      String tipo="";
      UsuariosGruposForm uForm = new UsuariosGruposForm();
      uForm = (UsuariosGruposForm) session.getAttribute("UsuariosGruposForm");
      UsuariosGruposValueObject uVO = new UsuariosGruposValueObject();
      uVO = uForm.getUsuariosGrupos();
   

      fichero = uVO.getFicheroFirmaFisico();
      tipo=uVO.getTipoFirma();

      ServletOutputStream out = res.getOutputStream();
      res.setContentType(tipo);
      out.write(fichero);
      out.close();
  }

  public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
      try     {
      
          defaultAction(req, res);
    } catch (Exception e) {
        if (log.isErrorEnabled()) log.error("ERROR doGet:" + e);
    }
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
    try {
        defaultAction(req, res);
    } catch (Exception e) {
        if (log.isErrorEnabled()) log.error("ERROR doGet:" + e);
    }
  }
}
