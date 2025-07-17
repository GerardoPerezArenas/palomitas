package es.altia.agora.interfaces.user.web.terceros;

import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.terceros.persistence.TercerosManager;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.exception.TechnicalException;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

public class DepuracionCopiarDomiciliosAction extends ActionSession {

    protected static Log m_Log = LogFactory.getLog(BuscarTerceroDepuracionAction.class.getName());
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");

    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                        HttpServletResponse response)
            throws IOException, ServletException {
        
        m_Log.info("DepuracionCopiarDomiciliosAction --> Entramos en el Action");

        // Recuperamos los datos del usuario, asi como de la conexion.
        String[] params = null;
        HttpSession session = request.getSession();
        String codUsuario = null;

        if (session.getAttribute("usuario") != null){
            UsuarioValueObject usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
            params = usuarioVO.getParamsCon();
            codUsuario = Integer.toString(usuarioVO.getIdUsuario());
        }

        // Recuparemos los datos introducidos en la request.
        String codTercero = request.getParameter("codTerDepurado");
        String codDomsACopiar = request.getParameter("codDomiciliosACopiar");
        String tipoBusqueda = request.getParameter("tipoBusqueda");
        String fechaInicio = request.getParameter("fechaInicio");
        String fechaFin = request.getParameter("fechaFin");
        String nombre = request.getParameter("nombre");
        String apellido1 = request.getParameter("apellido1");
        String apellido2 = request.getParameter("apellido2");
        String documento = request.getParameter("documento");
        String codTipoDocumento = request.getParameter("codTipoDocumento");
        if (!(tipoBusqueda == null || ("").equals(tipoBusqueda))) session.setAttribute("tipoBusqueda",tipoBusqueda);
        if (!(fechaInicio == null || ("").equals(fechaInicio))) session.setAttribute("fechaInicio",fechaInicio);
        if (!(fechaFin == null || ("").equals(fechaFin))) session.setAttribute("fechaFin",fechaFin);
        if (!(nombre == null || ("").equals(nombre))) session.setAttribute("nombre",nombre);
        if (!(apellido1 == null || ("").equals(apellido1))) session.setAttribute("apellido1",apellido1);
        if (!(apellido2 == null || ("").equals(apellido2))) session.setAttribute("apellido2",apellido2);
        if (!(documento == null || ("").equals(documento))) session.setAttribute("documento",documento);
        if (!(codTipoDocumento == null || ("").equals(codTipoDocumento))) session.setAttribute("codTipoDocumento",codTipoDocumento);

        // Transformamos el String con los codigos de domicilios en un vector de los mismos.
        Vector codigosDomicilios = new Vector();
        StringTokenizer tokenizer = new StringTokenizer(codDomsACopiar, ",");
        while (tokenizer.hasMoreTokens()) {
            String nextCod = tokenizer.nextToken();
            if (!codigosDomicilios.contains(nextCod)) codigosDomicilios.add(nextCod);
        }

        try {
            TercerosManager.getInstance().copiarDomiciliosATercero(codigosDomicilios, codTercero, codUsuario, params);
        } catch (TechnicalException te) {
            if (te.getMessage().equals("yaExisten")) {
                return (mapping.findForward("yaExisten"));
            } else {
                te.printStackTrace();
                return (mapping.findForward("error"));
            }
        }

        session.setAttribute("codTercero", codTercero);
        m_Log.debug("CODIGO DE TERCERO: " + codTercero);
        return (mapping.findForward("default"));
    }
}
