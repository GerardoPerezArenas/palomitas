package es.altia.agora.interfaces.user.web.editor;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.editor.mantenimiento.persistence.DocumentosAplicacionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MostrarDocumento extends HttpServlet {

    protected static Log log =
            LogFactory.getLog(MostrarDocumento.class.getName());

    //Process the HTTP Get request
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        BufferedOutputStream bos = null;
        log.debug("entrando en servlet de visualizacion de documentos");
        try {
            if (!"".equals(request.getContextPath())) {
                uri = uri.substring(uri.indexOf(request.getContextPath()) + request.getContextPath().length(), uri.length());
            }
            StringTokenizer tokenizer = new StringTokenizer(uri, "/");
            String[] tokens = new String[tokenizer.countTokens()];
            int i = 0;
            while (tokenizer.hasMoreTokens()) {
                tokens[i++] = tokenizer.nextToken();
            }

            String[] params = {tokens[2], "", "", "", "", "", tokens[3]};

            String codDocumento = tokens[4];
                codDocumento = codDocumento.substring(9, codDocumento.indexOf("."));
            String extension = tokens[4].substring(tokens[4].indexOf(".") + 1);
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codDocumento", codDocumento);

            GeneralValueObject file = DocumentosAplicacionManager.getInstance().loadDocumento(gVO, params);

       
            //Editor Open Office
            if (extension.equals("odt")) {
                if (file != null) {

                    byte[] fichero = (byte[]) file.getAtributo("fichero");
                    response.setHeader("Content-Transfer-Encoding", "binary");
                    response.setContentType("application/vnd.oasis.opendocument.text");
                    response.setContentLength(fichero.length);
                    DataOutputStream out = new DataOutputStream(response.getOutputStream());
                    out.write(fichero, 0, fichero.length);
                    out.flush();
                    out.close();
                    log.debug(fichero.length);




                } else {
                    log.error("NO SE HA PODIDO RECUPERAR EL CONTENIDO DEL DOCUMENTO");
                    throw new ServletException("NO SE HA PODIDO RECUPERAR EL CONTENIDO DEL DOCUMENTO");
                }
            } //Editor Word
            else if (extension.equals("doc")) {
                if (file != null) {
                    response.setContentType("application/vnd.ms-word");
                    response.setHeader("Content-Transfer-Encoding", "binary");
                    response.setHeader("Content-Disposition", "inline; filename=\"" + file.getAtributo("nombre") + ".doc\"");
                    byte[] fichero = (byte[]) file.getAtributo("fichero");
                    response.setContentLength(fichero.length);
                    ServletOutputStream out = response.getOutputStream();
                    bos = new BufferedOutputStream(out);
                    bos.write(fichero, 0, fichero.length);
                } else {
                    log.error("NO SE HA PODIDO RECUPERAR EL CONTENIDO DEL DOCUMENTO");
                    throw new ServletException("NO SE HA PODIDO RECUPERAR EL CONTENIDO DEL DOCUMENTO");
                }
            }
        
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw ioe;
        } catch (ServletException se) {
            se.printStackTrace();
            throw se;
        } catch (Exception e) {
            //e.printStackTrace();
            throw new ServletException("NO SE HA PODIDO RECUPERAR EL CONTENIDO DEL DOCUMENTO", e);

        } finally {
            if (bos != null) {
                bos.close();
            }
        }
    }

    //Process the HTTP Post request
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}