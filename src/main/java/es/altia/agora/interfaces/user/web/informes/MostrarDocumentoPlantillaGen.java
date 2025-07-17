package es.altia.agora.interfaces.user.web.informes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

public class MostrarDocumentoPlantillaGen extends HttpServlet {

    protected static Log log =
            LogFactory.getLog(MostrarDocumentoPlantillaGen.class.getName());

	//Process the HTTP Get request
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		String uri = request.getRequestURI();
        BufferedOutputStream bos = null;
		try {
			if(!"".equals(request.getContextPath())){
              uri = uri.substring(uri.indexOf(request.getContextPath()) + request.getContextPath().length(), uri.length());
            }
            StringTokenizer tokenizer = new StringTokenizer(uri, "/");
			String[] tokens = new String[tokenizer.countTokens()];
			int i = 0;
			while (tokenizer.hasMoreTokens())
				tokens[i++] = tokenizer.nextToken();

			String[] params = { tokens[2], "", "", "", "", "", tokens[3] };

			String codDocumento = tokens[4];
			codDocumento = codDocumento.substring(9, codDocumento.indexOf("."));
			
			es.altia.agora.business.geninformes.utils.bd.InformeXerador ix =
				new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.bd
					.InformeXerador(
					null);
			es.altia.util.HashtableWithNull tabla =
				new es.altia.util.HashtableWithNull();

			//String[] params = obtenerParametrosConexion(request);
			tabla.put("PARAMS", params);
			tabla.put("COD_INFORMEXERADOR", codDocumento);
			es.altia.util.conexion.Cursor salida =
				ix.ConsultaInformeXerador(tabla);
			salida.next();
			byte[] file = (byte[]) salida.getObject("F_DOT");

			if ((file != null) && (file.length > 0)) {

				response.setContentType("application/msword");
				response.setHeader("Content-Transfer-Encoding", "binary");
				response.setHeader(
					"Content-Disposition",
					"inline; filename=\"Documento" + codDocumento + ".doc\"");
                response.setContentLength(file.length);
				ServletOutputStream out = response.getOutputStream();
                bos = new BufferedOutputStream(out);
				bos.write(file);
			} else {
                log.error("FICHERO NULO EN MostrarDocumentoPlantillaGen.doGet");
			}

		} catch (Exception ex) {
			//ex.printStackTrace();
                     log.error("Error al mostrarDocumentoPlantillaGen:  "+ex.getMessage());
		} finally {
            if(bos != null) bos.close();
		}
	}

	//Process the HTTP Post request
	public void doPost(
		HttpServletRequest request,
		HttpServletResponse response)
		throws ServletException, IOException {
		doGet(request, response);
	}

	private String[] obtenerParametrosConexion(HttpServletRequest request)
		throws Exception {
		try {
			String[] params = new String[7];
			params[0] = request.getParameter("bd");
			params[1] = "";
			params[2] = "";
			params[3] = "";
			params[4] = "";
			params[5] = "";
			params[6] = request.getParameter("jndi");

			return params;
		} catch (Exception e) {
			throw e;
		}
	}

}