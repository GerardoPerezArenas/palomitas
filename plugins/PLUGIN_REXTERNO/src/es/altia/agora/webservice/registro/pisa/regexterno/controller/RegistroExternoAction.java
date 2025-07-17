package es.altia.agora.webservice.registro.pisa.regexterno.controller;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.util.combo.ComboCVO;
import es.altia.agora.webservice.registro.pisa.regexterno.model.persistence.RegistroExternoManager;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author ivan.perez
 */
public class RegistroExternoAction extends ActionSession {

    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                        HttpServletResponse response)
            throws IOException, ServletException {
        
                if (m_Log.isDebugEnabled()) m_Log.debug("ENTRAMOS EN EL ACTION DE RegistroExterno");

        // Recuperamos el usuario y los datos de conexion.
        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        String[] params = usuario.getParamsCon();

        // Recuperamos la opcion de la request.
        String opcion = request.getParameter("opcion");
        if (m_Log.isDebugEnabled()) m_Log.debug("LA OPCION EN EL ACTION ES " + opcion);

        // Recuperamos el formulario asociado.
        form = new AnotacionRegistroExternoForm();
        if ("request".equals(mapping.getScope())) request.setAttribute(mapping.getAttribute(), form);
        else session.setAttribute(mapping.getAttribute(), form);
        
        RegistroExternoManager regExtManager = RegistroExternoManager.getInstance();
        if (opcion.equals("cargarDefecto")) {
            String fechaActual = getFechaActual();
            ComboCVO comboTiposDoc = regExtManager.getComboTiposDocumentos(params);
            ComboCVO comboUORsAccede = regExtManager.getComboUnidadesAccede(params);
            request.setAttribute("fechaActual", fechaActual);
            request.setAttribute("comboTiposDoc", comboTiposDoc);
            request.setAttribute("comboUORsAccede", comboUORsAccede);             

//            m_Log.debug(comboTiposDoc.getElementosCombo());
//            m_Log.debug(comboUORsAccede.getElementosCombo());
        }         
        return mapping.findForward(opcion);
    }
    
    private String getFechaActual() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar fecha = Calendar.getInstance();
        Date date = fecha.getTime();
        String fechaActual = dateFormat.format(date);
        return fechaActual;
    }
}
