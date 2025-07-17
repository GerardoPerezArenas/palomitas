/*______________________________BOF_________________________________*/
package es.altia.agora.interfaces.user.web.portafirmas.delegacionfirma;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.portafirmas.delegacionfirmafacade.DelegacionFirmaFacadeDelegate;
import es.altia.agora.interfaces.user.web.portafirmas.GlobalNames;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.portafirmasexternocliente.factoria.PluginPortafirmasExternoClienteFactoria;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.facades.BusinessFacadeDelegateFactory;
import es.altia.util.struts.DefaultAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.http.HttpSession;
import org.pentaho.reporting.libraries.base.util.StringUtils;

/**
 * @version $\Date$ $\Revision$
 */
public class PopUpSeleccionUsuarioAction extends DefaultAction {
    /*_______Constants______________________________________________*/
    public static final String CLSNAME="PopUpSeleccionUsuarioAction";
    private static final String MAPPING_SUCCESS="PopUpSeleccionUsuarioForm";

    /*_______Atributes______________________________________________*/

    /*_______Operations_____________________________________________*/

    protected ActionForward doPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InternalErrorException, ModelException {
        /* Recuperar parámetros de conexión */
        //UsuarioValueObject usuario = SessionManager.getAuthenticatedUser(request);
        //String[] params = usuario.getParamsCon();
        //if (_log.isInfoEnabled()) _log.info(CLSNAME+".doPerform() PARAMS = {"+BasicTypesOperations.toString(params,",")+"}");
        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        String opcion = request.getParameter("opcion");

        /* Do search */
        DelegacionFirmaFacadeDelegate facade = (DelegacionFirmaFacadeDelegate) BusinessFacadeDelegateFactory.getFacadeDelegate(DelegacionFirmaFacadeDelegate.class);
        //facade.setDsKey(params[params.length-1]);
facade.resetDsKey();        

        /* Comprueba si existe portafirmas externo */
        boolean obligatorioNIF = false;
        boolean obligBuzonFirma = false;
        if (usuario != null) {
            String codOrganizacion = String.valueOf(usuario.getOrgCod());
            boolean existePortafirmasExterno = PluginPortafirmasExternoClienteFactoria.getExistePortafirmasExterno(codOrganizacion);

            if (existePortafirmasExterno) {
                // Determinamos si es obligatorio que los usuarios tengan NIF
                Config configPortafirmas = ConfigServiceHelper.getConfig("Portafirmas");
                String propObligatorioNif = configPortafirmas.getString(
                        String.format("%s/PortafirmasExterno/obligatorioFirmanteDocumentoIdentificativo",
                                codOrganizacion));

                if (StringUtils.equalsIgnoreCase(ConstantesDatos.SI, propObligatorioNif)) {
                    obligatorioNIF = true;
                }

                /* Determinamos si hay que comprobar que exista el usuario en el
                 * Portafirmas externo desde la pantalla
                 */
                if ("ConComprobacionUsuarioPortafirmas".equals(opcion)) {
                    request.setAttribute("existePortafirmasExterno", existePortafirmasExterno);
                }
            }
        }
        
        //Se obtienen los tipos de procedimientos del fichero properties Portafirmas.properties
        ResourceBundle expPortafirmas = ResourceBundle.getBundle("Portafirmas");
        String propiedad = usuario.getOrgCod()+"/Portafirmas";
        String portafirmas = expPortafirmas.getString(propiedad);
        
        //Si el portafirmas es de tipo LANBIDE el NIF no es obligatorio pero si el Buzon de Firmas
        if (portafirmas != null && !"".equals(portafirmas) && "LAN".equals(portafirmas)) {
            obligatorioNIF = false;
            obligBuzonFirma = true;
        }

        List listaVOs = facade.findUsuariosDelegables(facade.scUsuarioDelegableByFirmante(true, obligatorioNIF, obligBuzonFirma),facade.ocUsuarioDelegableByFirmante(true),-1,-1);

        /* Fill form */
        PopUpSeleccionUsuarioForm concreteForm = (PopUpSeleccionUsuarioForm) form;
        concreteForm.setListaUsuarios(listaVOs);

        /* Return result */
        ActionForward result = mapping.findForward(MAPPING_SUCCESS);
        return result;
    }//doPerform

    protected String getMainPageMapping() {
        return GlobalNames.MAINPAGE_GLOBAL_FORWARD;
    }

    protected String getInternalErrorMapping() {
        return GlobalNames.INTERNALERROR_GLOBAL_FORWARD;
    }

    protected String getModelErrorMapping() {
        return GlobalNames.MODELERROR_GLOBAL_FORWARD;
    }

}//class
/*______________________________EOF_________________________________*/
