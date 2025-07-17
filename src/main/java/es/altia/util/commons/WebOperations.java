package es.altia.util.commons;

import es.altia.agora.technical.ConstantesDatos;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WebOperations {

    protected static Log log = LogFactory.getLog(WebOperations.class.getName());
    
    public static enum HTTP_METHOD {
        GET,
        POST;
    } 

    public static void descargarUrl(String urlDestino, HTTP_METHOD method, String sessionId, Map<String, Object> params, OutputStream out) throws MalformedURLException, IOException {
        InputStream resultadoUrl = null;
        HttpURLConnection conexion = null;

        if (log.isDebugEnabled()) {
            log.debug(String.format("urlDestino: %s", urlDestino));
            log.debug(String.format("sessionId: %s", sessionId));
            log.debug(String.format("numero de params: %d",  (params != null) ? params.size() : 0));
            log.debug(String.format("out es nulo?: %b", out == null));
        }
        
        try {
            URL url = new URL(urlDestino);

            //Propiedades de la llamada
            conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod(method.name());
            conexion.setDoOutput(true);
            conexion.setDoInput(true);
            conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            if (StringUtils.isNotEmpty(sessionId) && !urlDestino.contains("jsessionid")) {
                conexion.setRequestProperty("Cookie", "JSESSIONID=" + URLEncoder.encode(sessionId, ConstantesDatos.CHARSET_UTF_8));
            }
            if (params != null) {
                byte[] postParamContent = new byte[0];
                postParamContent = getPostParamContent(params);
                conexion.setRequestProperty("Content-Length", String.valueOf(postParamContent.length));
                conexion.getOutputStream().write(postParamContent);
            }
            if (log.isDebugEnabled()) {
                log.debug(String.format("conexion: %s", conexion.toString()));
            }

            // Obteniendo el contenido de la llamada URL
            resultadoUrl = conexion.getInputStream();
            if (log.isDebugEnabled()) {
                log.debug(String.format("resultadoUrl: %s", resultadoUrl.toString()));
            }
            IOUtils.copy(resultadoUrl, out);
        } finally {
            IOUtils.closeQuietly(resultadoUrl);

            if (conexion != null) {
                conexion.disconnect();
            }
        }
    }

    public static void descargarUrlGet(String urlDestino, String sessionId, OutputStream out) throws MalformedURLException, IOException {
        if (log.isDebugEnabled()) {
            log.debug("Se accede descargarUrlGet");
        }
        descargarUrl(urlDestino, HTTP_METHOD.GET, sessionId, null, out);
    }
    
    public static void descargarUrlPost(String urlDestino, String sessionId, Map<String, Object> params, OutputStream out) throws MalformedURLException, IOException {
         if (log.isDebugEnabled()) {
            log.debug("Se accede descargarUrlPost");
        }
        descargarUrl(urlDestino, HTTP_METHOD.POST, sessionId, params, out);
    }
    
    private static byte[] getPostParamContent(Map<String, Object> params) throws UnsupportedEncodingException {
        if (log.isDebugEnabled()) {
            log.debug("Se accede getPostParamContent");
        }
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) {
                postData.append('&');
            }
            postData.append(URLEncoder.encode(param.getKey(), ConstantesDatos.CHARSET_UTF_8));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), ConstantesDatos.CHARSET_UTF_8));
        }

        return postData.toString().getBytes(ConstantesDatos.CHARSET_UTF_8);
    }
    
    /**
     * Método llamado para devolver un String en formato JSON al cliente que ha
     * realiza la petición a alguna de las operaciones de este action
     *
     * @param json: String que contiene el JSON a devolver
     * @param response: Objeto de tipo HttpServletResponse a través del cual se
     * devuelve la salida al cliente que ha realizado la solicitud
     */
    public static void retornarJSON(String json, HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("Se accede retornarJSON");
        }
        try {
            if (json != null) {
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
                out.close();
            }

        } catch (IOException ioe) {
            log.error(ioe);
        }
    }
}
