package es.altia.agora.interfaces.user.web.formularios.tramitar;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.persistence.manual.TramitacionExpedientesDAO;
import es.altia.agora.business.sge.persistence.manual.TramitesExpedienteDAO;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.catalogoformularios.model.solicitudes.vo.TramiteTramitadoKey;
import es.altia.catalogoformularios.model.solicitudesfacade.FormularioFacade;
import es.altia.catalogoformularios.model.solicitudesfacade.vo.DatosFormularioPDF;
import es.altia.catalogoformularios.util.XMLHelper;
import es.altia.catalogoformularios.util.exceptions.InternalErrorException;
import es.altia.common.exception.TechnicalException;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.w3c.dom.Document;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase ProcedimientosAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: ALTIA CONSULTORES & AYTOS CPD</p>
 * @author Jorge Hombre Tuñas
 * @version 1.0
 */
public class FormsPDFAction extends ActionSession {

    public ActionForward performSession(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        try {

            /* Obtener datos xml del formulario. */
            Document datosXML = XMLHelper.parse(request.getInputStream());
            DatosFormularioPDF f = new DatosFormularioPDF(datosXML);

            String opcion = request.getParameter("opcion");
            m_Log.debug(".....FormsPDF OPCION='" + opcion + "' recibe:" + XMLHelper.xmlDocToString(datosXML));
            HttpSession session = request.getSession();

            String uorTramitadora = null;
            int usuCod = 0;
            UsuarioValueObject usuario = null;
            String[] params = new String[7];

                if ((session.getAttribute("usuario") != null)) {
                    usuario = (UsuarioValueObject) session.getAttribute("usuario");
                    usuCod = usuario.getIdUsuario();
                } else { // No hay usuario.
                    throw new InternalErrorException("No hay usuario");
                }

            params[0] = ((UsuarioValueObject) (UsuarioValueObject) session.getAttribute("usuario")).getParamsCon()[0];
            params[6] = ((UsuarioValueObject) (UsuarioValueObject) session.getAttribute("usuario")).getParamsCon()[6];


            FormularioFacade sf = new FormularioFacade(String.valueOf(usuario.getOrgCod()));
            if ("ena".equals(opcion)) {
                //Enviar Nuevo Adjunto
                m_Log.debug("....lamando a enviarNuevoAdjunto");
                if (uorTramitadora == null) {
                    uorTramitadora = TramitesExpedienteDAO.getInstance().getUnidadTramitadoraTramite(params, f.getDatosFormulario().munInstancia, f.getDatosFormulario().procTramite, f.getDatosFormulario().numInstancia, f.getDatosFormulario().ocuTramite, f.getDatosFormulario().ejerInstancia, f.getDatosFormulario().codTramite);
                }
                sf.enviarNuevoAdjunto(f, usuCod, uorTramitadora);
            } else if ("gna".equals(opcion)) {
                //Guardar Nuevo Adjunto
                m_Log.debug("....lamando a guardarNuevoAdjunto");
                sf.guardarNuevoAdjunto(f);
            } else if ("gam".equals(opcion)) {
                //guardar adjunto modificado
                sf.guardarSolicitudModificada(f);
            } else if ("eam".equals(opcion)) {
                //enviar adjunto modificado
                if (uorTramitadora == null) {
                    uorTramitadora = TramitesExpedienteDAO.getInstance().getUnidadTramitadoraTramite(params, f.getDatosFormulario().munInstancia, f.getDatosFormulario().procTramite, f.getDatosFormulario().numInstancia, f.getDatosFormulario().ocuTramite, f.getDatosFormulario().ejerInstancia, f.getDatosFormulario().codTramite);
                }
                sf.enviarSolicitudModificada(f, usuCod, uorTramitadora, String.valueOf(usuario.getOrgCod()));
            }

            //si opcion=c -> cancelar -> no hacer nada
            //si ena, gna -> hay un formulario mas en la lista
            //   eam -> ha cambiado el estado del formulario
            if (("ena".equals(opcion)) || ("gna".equals(opcion)) || ("eam".equals(opcion))) {
                //volver a obtener los formularios del tramite
                TramiteTramitadoKey tramite = new TramiteTramitadoKey(
                        f.getDatosFormulario().munInstancia,
                        f.getDatosFormulario().procTramite,
                        f.getDatosFormulario().ejerInstancia,
                        f.getDatosFormulario().numInstancia,
                        f.getDatosFormulario().codTramite,
                        f.getDatosFormulario().ocuTramite);
                Collection pdfs = sf.getFormsOfTramite(tramite);
                request.setAttribute("formsPDF", pdfs);
            }
            //si enviamos adj recargar datos suplementarios
            if (("ena".equals(opcion)) || ("eam".equals(opcion))) {
                HashMap datosSup = new HashMap();

                Vector estructura = new Vector();
                Vector valores = new Vector();
                try {
                    //recuperar los datos suplementarios de este tramite de la BD
                    TramitacionExpedientesValueObject tEVO = new TramitacionExpedientesValueObject();
                    tEVO.setCodMunicipio(String.valueOf(f.getDatosFormulario().munInstancia));
                    tEVO.setCodProcedimiento(f.getDatosFormulario().procTramite);
                    tEVO.setEjercicio(String.valueOf(f.getDatosFormulario().ejerInstancia));
                    tEVO.setOcurrenciaTramite(String.valueOf(f.getDatosFormulario().ocuTramite));
                    tEVO.setNumeroExpediente(f.getDatosFormulario().numInstancia);
                    tEVO.setCodTramite(String.valueOf(f.getDatosFormulario().codTramite));
                    tEVO.setUsuario(String.valueOf(usuCod));
                    tEVO.setCodOrganizacion(String.valueOf(usuario.getOrgCod()));
                    tEVO.setCodEntidad(String.valueOf(usuario.getEntCod()));

                    params = usuario.getParamsCon();

                    estructura = TramitacionExpedientesDAO.getInstance().cargaEstructuraDatosSuplementarios(tEVO, params);
                    valores = TramitacionExpedientesDAO.getInstance().cargaValoresDatosSuplementarios(tEVO, estructura, params);

                } catch (TechnicalException te) {
                    m_Log.error("JDBC Technical problem " + te.getMessage());
                }
                request.setAttribute("estrDtsSupForm", estructura);
                request.setAttribute("valorDtsSupForm", valores);
            }

            request.setAttribute("opcion", opcion);

            m_Log.debug("perform");
            return (mapping.findForward("Success"));

        } catch (Exception e) {
            throw new ServletException(e);
        }

    }
}
