package es.altia.agora.interfaces.user.web.formularios;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.formularios.persistence.FichaFormularioManager;
import es.altia.common.service.config.*;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class VerPDFFormServlet extends HttpServlet
{
   
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("common");
    protected static Config ConfigPlantillas = ConfigServiceHelper.getConfig("formulariosPdf");
    private static String origenPlantillas = "";
    private static String dirPlantillas = "";
    private static String servidor = "";
    private static String contexto = "";
    protected static Log log =
            LogFactory.getLog(VerPDFFormServlet.class.getName());

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }
  
  public void defaultAction(HttpServletRequest req,HttpServletResponse res) throws Exception {
      HttpSession session = req.getSession(false);
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
      String codigo = req.getParameter("codigo");
      String tipo = req.getParameter("tipo");
      String organizacion = req.getParameter("codOrg");
      /*Necesito la organizacion para visualizar le formulario.*/
      origenPlantillas = ConfigPlantillas.getString(organizacion+"/origenPlantillas");
     dirPlantillas = ConfigPlantillas.getString (organizacion+"/directorioPlantillas");
     servidor = ConfigPlantillas.getString(organizacion+"/servidorPlantillas");
     contexto = ConfigPlantillas.getString(organizacion+"/contextoPlantillas");
 
      /*Si las plantillas se guardan en BD hay que leer los datos y construir el PDF. Si se
       guardan en directorio, se consulta en el properties y se visualiza dicho fichero.*/
      log.debug (servidor+contexto+dirPlantillas);
      if (origenPlantillas.equals("BD")){
          FichaFormularioManager acceso = new FichaFormularioManager();
          byte[] fichero = acceso.getFicheroFormulario(codigo, tipo, usuario.getParamsCon());

          if (fichero != null){
          if (log.isDebugEnabled()) log.debug("FICHERO EN BD:" + fichero);
              BufferedOutputStream bos = null;
              try{
                  res.setContentType("application/pdf");
                  res.setContentLength(fichero.length);
                  ServletOutputStream out = res.getOutputStream();
                  bos = new BufferedOutputStream(out);
                  bos.write(fichero);
              } catch (Exception e) {
                  if (log.isErrorEnabled()) log.error("Excepcion en catch de VerPDFFormServlet.defaultAction " + e.getMessage());
                  throw e;
              } finally{
                  if(bos != null) bos.close();
              }
          } else {
              log.debug("EL FICHERO ES NULO EN VerPDFFormServlet.defaultAction");
          }
      }
      else if (origenPlantillas.equals("DIRECTORIO")){
        String nombre = codigo + "_";
        if (tipo.equals("p1")) nombre+="edicion.pdf"; 
        else nombre+="impresion.pdf";
        if (log.isDebugEnabled()) log.debug("FICHERO EN DIRECTORIO:" + nombre);

        URL dir = new URL(servidor+contexto+dirPlantillas+nombre);
        URLConnection urlConnection = dir.openConnection();
  try{
        InputStream iStream = urlConnection.getInputStream();
  
        ServletOutputStream out = res.getOutputStream();
        res.setHeader("Content-Disposition", "inline;filename=\"" + nombre + "\"");
        res.setHeader("Content-Transfer-Encoding", "binary");
        res.setContentType("application/pdf");

        boolean eof = false;
        while (!eof) {
            int buffer = iStream.read();
            eof = (buffer == -1);
            if (!eof)
                out.write(buffer);
        }
        out.flush();
        out.close();
        iStream.close(); 
        }catch(FileNotFoundException e){
        e.printStackTrace();
        }
      }
  }

  public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
      try     {
      defaultAction(req, res);
    } catch (Exception e) {
        if (log.isErrorEnabled()) log.error("ERROR doGet:" + e);
        e.printStackTrace();
    }
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
    try {
        defaultAction(req, res);
    } catch (Exception e) {
        if (log.isErrorEnabled()) log.error("ERROR doGet:" + e);
        e.printStackTrace();
    }
  }
}
