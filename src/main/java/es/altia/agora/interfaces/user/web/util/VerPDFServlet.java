package es.altia.agora.interfaces.user.web.util;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class VerPDFServlet extends HttpServlet
{
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("common");

    protected static Log log =
            LogFactory.getLog(VerPDFServlet.class.getName());

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }
  public void defaultAction(HttpServletRequest req,HttpServletResponse res) throws Exception {

      log.debug("***** Viniendo de: " + req.getHeader("referer"));
      HttpSession session = req.getSession();
      UsuarioValueObject elUsuario = new UsuarioValueObject();

      log.debug("*************** VerPDFServlet entrando *******************");
      String codOrg = "";

      if ((session.getAttribute("usuario") != null)) {
          elUsuario = (UsuarioValueObject) session.getAttribute("usuario");
          codOrg = elUsuario.getDtr();
      } else {
          codOrg = req.getParameter("directorio");
      }
     
      String fileSystem = m_ConfigTechnical.getString("PDF.base_dir");     
      String nombre = req.getParameter("nombre");
      String tipoFichero = req.getParameter("tipoFichero");
      log.debug("******** parametro dir: " + req.getParameter("dir"));
      log.debug("*********** fileSystem: " + fileSystem);
      log.debug("*********** nombre: " + nombre);
      log.debug("*********** tipoFichero: " + tipoFichero);

      if ((tipoFichero == null) || ("".equals(tipoFichero))) {
          tipoFichero = "pdf";
      }

      String direccionTotal = fileSystem+"/"+codOrg+"/"+tipoFichero+"/"+nombre+"."+tipoFichero;
      log.debug("VerPDFServlet: se va a leer el archivo " + direccionTotal);
     
      ServletOutputStream out = res.getOutputStream();
      java.io.FileInputStream fichero = new java.io.FileInputStream(direccionTotal);
      byte[] doc;
      int c;
      BufferedOutputStream bos = null;
      ByteArrayOutputStream blob = null;
      try {
          blob = new ByteArrayOutputStream();
          while ((c = fichero.read()) >= 0) {
              blob.write(c);
          }
          doc = blob.toByteArray();
          blob.close();
          if (doc != null) {
              /*res.setContentType("application/pdf");
              res.setContentLength(doc.length);
              out.write(doc);
              out.flush();
              out.close();*/

              if (tipoFichero != null) {
                  if ("html".equals(tipoFichero)) {
                      res.setContentType("text/html");
                  } else if ("csv".equals(tipoFichero)) {
                      res.setHeader("Content-disposition","attachment;filename="+nombre+"."+tipoFichero);
                      res.setContentType("application/vnd.ms-excel");                                          
                  } else {
                      res.setContentType("application/pdf");
                  }
              } else {
                  res.setContentType("application/pdf");
              }

                  res.setContentLength(doc.length);
              bos = new BufferedOutputStream(out);
              bos.write(doc, 0, doc.length);
          } else {
              log.error("FICHERO NULO EN VerPDFServlet.defaultAction");
          }
      } catch (Exception e) {
          if (log.isErrorEnabled()) {
              log.error("ERROR defaultAction:" + e);
          }
          throw e;
      } finally {
          if (blob != null) {
              blob.close();
          }
          if (bos != null) {
              bos.close();
          }
      }
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
