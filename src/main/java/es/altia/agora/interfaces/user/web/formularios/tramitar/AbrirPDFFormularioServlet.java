package es.altia.agora.interfaces.user.web.formularios.tramitar;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.catalogoformularios.model.solicitudes.vo.FormularioTramitadoVO;
import es.altia.catalogoformularios.model.solicitudes.vo.TramiteTramitadoKey;
import es.altia.catalogoformularios.model.solicitudesfacade.FormularioFacade;
import es.altia.agora.business.sge.persistence.manual.TramitesExpedienteDAO;
import es.altia.catalogoformularios.util.MergePDF;
import es.altia.catalogoformularios.util.config.ConfigurationParametersManager;
import es.altia.catalogoformularios.util.exceptions.InternalErrorException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * Recupera de BD la plantilla adecuada, y la informacion para rellenarla
 * segun la opcion de apertura.
 * User: diana.pineiro
 * Date: 22-mar-2006
 */

public class AbrirPDFFormularioServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        Log m_Log =
            LogFactory.getLog(AbrirPDFFormularioServlet.class.getName());

        try{

            HttpSession session = request.getSession();
            int usuCod = 0;
            if ((session.getAttribute("usuario") != null)) {
                UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
                usuCod = usuario.getIdUsuario();
            }   else { // No hay usuario.
                throw new InternalErrorException("No hay usuario");
            }
            String[] params = new String[7];

            params[0] = ((UsuarioValueObject) (session.getAttribute("usuario"))).getParamsCon()[0];
            params[6] = ((UsuarioValueObject) (session.getAttribute("usuario"))).getParamsCon()[6];
            String raiz = request.getContextPath();
            String codigo = (String) request.getParameter("codigo");
            String opcion = request.getParameter("opcion");

            UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
            FormularioFacade sf = new FormularioFacade(String.valueOf(usuario.getOrgCod()));
            String contenido = "";

            String idSession = request.getHeader("cookie");
            idSession = ";" + idSession.replaceFirst("JSESSIONID","jsessionid");

            String enviar = "";
            String guardar="";
            String cancelar= response.encodeURL(raiz + "GetSolicitudes.do" + idSession);
            byte[] bytes = null;
            if (opcion.equals("imprimir")){
                bytes = sf.getPDFImprimir2(codigo);
            }else if (opcion.equals("nuevoadj")){
                actualizarRutaPlantillas(request);

                int municipio = Integer.parseInt(request.getParameter("mun"));
                String procedimiento = (String) request.getParameter("proc");
                int ejercicio = Integer.parseInt(request.getParameter("ejer"));
                String numero = (String) request.getParameter("num");
                int codTramite = Integer.parseInt(request.getParameter("tram"));
                int ocu = Integer.parseInt(request.getParameter("ocu"));
                String uorTramitadora = TramitesExpedienteDAO.getInstance().
                        getUnidadTramitadoraTramite(params, municipio, procedimiento,
                        numero, ocu, ejercicio, codTramite);
                session.setAttribute("uorTramitadoraFormulario", uorTramitadora);
                String tfCod = (String) request.getParameter("tfCod");
                if (m_Log.isDebugEnabled()) m_Log.debug("parametro tfCod ="+ tfCod);
                //Si no recibimos el codigo de la solicitud a la que agragar el adjunto,
                //buscar la solicitud de este expediente, supongo que solo hay una
                if ((tfCod==null) || ("".equals(tfCod))) {
                    tfCod = null;
                    Iterator forms = sf.getFormsOfExpediente(numero,String.valueOf(usuario.getOrgCod())).iterator();
                    while (forms.hasNext() && (tfCod==null)){
                        FormularioTramitadoVO elem = (FormularioTramitadoVO) forms.next();
                        if ("0".equals(elem.getTipo())){
                            tfCod = elem.getCodigo();
                        }
                    }
            }
                if (m_Log.isDebugEnabled()) m_Log.debug("padre del adjunto ="+tfCod);
                enviar = response.encodeURL(raiz + "/sge/FormsPDF.do" + idSession + "?opcion=ena");
                guardar= response.encodeURL(raiz + "/sge/FormsPDF.do" + idSession + "?opcion=gna");
                cancelar = response.encodeURL(raiz + "/sge/FormsPDF.do" + idSession + "?opcion=c");

                TramiteTramitadoKey tramite = new TramiteTramitadoKey(
                    municipio,procedimiento,ejercicio, numero, codTramite, ocu);

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("OBTENER CONTENIDO == usuCod = " +
                            usuCod + " codigo =" + codigo + " tfCod=" + tfCod + " tramite=" + tramite + " enviar=" + enviar + " guardar=" + guardar + " cancelar=" + cancelar);
                }
                contenido = sf.getPDFNewAdjunto(usuCod, Integer.parseInt(codigo),
                        tfCod, tramite, enviar, guardar, cancelar, uorTramitadora, String.valueOf(usuario.getOrgCod()));

            } else if (opcion.equals("editaradj")){
                actualizarRutaPlantillas(request);

                enviar = response.encodeURL(raiz + "/sge/FormsPDF.do" + idSession + "?opcion=eam");
                guardar= response.encodeURL(raiz + "/sge/FormsPDF.do" + idSession + "?opcion=gam");
                cancelar = response.encodeURL(raiz + "/sge/FormsPDF.do" + idSession + "?opcion=c");
                contenido = sf.getPDFEditar(codigo, enviar, guardar, cancelar);
            }
            
            if (opcion.equals("imprimir")){
            	response.setContentType("application/pdf");
            } else {
            	response.setContentType(MergePDF.getApplicationType());
            }
            
            response.setHeader("Content-Transfer-Encoding","binary");
            response.setHeader("Content-Disposition","inline;filename=\"" + "pdf" + codigo + "\"");

            ServletOutputStream out = response.getOutputStream();
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("***** AbrirPDFFormularioServlet desde SGE=" + contenido);
            }

            if (bytes == null) {
                bytes = contenido.getBytes("UTF-8");
            }

            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            BufferedInputStream bis = new BufferedInputStream(bais);
            byte buff[] = new byte[2048];
            int bytesRead;
            BufferedOutputStream bos = new BufferedOutputStream(out);
            while(-1 != (bytesRead = bis.read(buff, 0, buff.length))){
                bos.write(buff, 0, bytesRead);
            }
            bais.close();
            bis.close();
            bos.close();
            out.flush();
            out.close();

        }catch(InternalErrorException e){
            throw new ServletException(e);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        doGet(request, response);
    }

    private void actualizarRutaPlantillas(HttpServletRequest request){
        //ruta logica y fisica del servidor para poder cargar el pdf de plantilla...
        ConfigurationParametersManager.urlServidor =
        request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/";

        String realPath = this.getServletContext().getRealPath("");
        realPath = realPath.replace('\\','/');
        int fin = realPath.indexOf(request.getContextPath());
        realPath = realPath.substring(0, fin+1);
        ConfigurationParametersManager.pathServidor = realPath;
    }
}