// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.registro;

// PAQUETES IMPORTADOS
import es.altia.agora.business.escritorio.*;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.helper.*;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.util.struts.StrutsFileValidation;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Vector;
import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;
import sun.misc.BASE64Decoder;
import java.util.*;
import es.altia.agora.technical.Fecha;
import es.altia.flexia.registro.justificante.util.FileOperations;


public class EntregadosAnteriorAction extends ActionSession {

    public ActionForward performSession(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
         
            m_Log.debug("================= EntregadosAnteriorAction ======================>");
            HttpSession session = request.getSession();

            String opcion = request.getParameter("opcion");

            if (m_Log.isInfoEnabled()) {
                m_Log.info("la opcion en el action es " + opcion);
            }

            MantAnotacionRegistroForm regESForm = (MantAnotacionRegistroForm) session.getAttribute("MantAnotacionRegistroForm");
            m_Log.debug(regESForm);
            if (regESForm != null) {
                if (regESForm.getListaDocsAnteriores() != null && regESForm.getListaDocsAnteriores().size() > 0) {
                    for (int i = 0; i < regESForm.getListaDocsAnteriores().size(); i++) {
                        RegistroValueObject rvo = (RegistroValueObject) regESForm.getListaDocsAnteriores().get(i);
              
                    }
                }
            }

            if (form == null) {
                m_Log.debug("Rellenamos el form de fichero");
                form = new EntregadosAnteriorForm();
                if ("request".equals(mapping.getScope())) {
                    request.setAttribute(mapping.getAttribute(), form);
                } else {
                    session.setAttribute(mapping.getAttribute(), form);
                }
            }
            EntregadosAnteriorForm nuevoForm = (EntregadosAnteriorForm) form;
            m_Log.debug("EntregadosAnteriorAction - EntregadosAnteriorForm es: " + nuevoForm);
            if ("altaNueva".equals(opcion)) {
                m_Log.debug("opcion " + opcion);
                nuevoForm.setModificando(false);
                nuevoForm.setTipoDocumento("");
                nuevoForm.setOrgano("");
                nuevoForm.setNombreDocumento("");
                nuevoForm.setFechaDocumento("");
                opcion = "altaNueva";
            } else if ("anteriorAlta".equals(opcion)) {
                m_Log.debug("opcion " + opcion);
                String nombreDoc = nuevoForm.getNombreDocumento();
                
                m_Log.debug("nombre documento " + nombreDoc);
            }else if("modificar".equals(opcion)){
                m_Log.debug("opcion "+opcion);
                String nombreDocAnterior=request.getParameter("nombreDocAnterior");
                String tipoDocAnterior=request.getParameter("tipoDocAnterior");
                String organoDocAnterior=request.getParameter("organoDocAnterior");
                String fechaDocAnterior=request.getParameter("fechaDocAnterior");
                int codigo = Integer.parseInt((String)request.getParameter("codigo"));
                session.setAttribute("codigo", codigo);
                m_Log.debug("codigoAction "+codigo);
                nuevoForm.setNombreDocumento(nombreDocAnterior);
                nuevoForm.setTipoDocumento(tipoDocAnterior);
                nuevoForm.setOrgano(organoDocAnterior);
                nuevoForm.setFechaDocumento(fechaDocAnterior);
                nuevoForm.setModificando(true);
                
                m_Log.debug("MODIFICANDO ES: " + true);
                session.setAttribute("modificando", nuevoForm.isModificando()); 
                
                opcion="altaNueva";
            }else if("anteriorEliminar".equals(opcion)){
                
                int codigo=Integer.parseInt((String)request.getParameter("codigoDocAnterior"));
                m_Log.debug("action codigo "+codigo);
                session.setAttribute("codigo", codigo);
                opcion="anteriorEliminar";
            }

            m_Log.debug("<================= EntregadosAnteriorAction ======================");
            return (mapping.findForward(opcion));
        } catch (Exception e) {
            // Si ocurre algún error
            e.printStackTrace();
            m_Log.error("Se ha producido el siguiente error: " + e.getMessage());
            return null;
        }

    }
}
