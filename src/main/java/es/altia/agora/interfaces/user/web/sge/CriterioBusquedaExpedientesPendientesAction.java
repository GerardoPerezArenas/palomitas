package es.altia.agora.interfaces.user.web.sge;
import es.altia.agora.business.administracion.mantenimiento.CampoDesplegableVO;
import es.altia.agora.business.administracion.mantenimiento.TipoDocumentoVO;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.persistence.CamposListadoPendientesProcedimientoManager;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.technical.ConstantesDatos;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


public class CriterioBusquedaExpedientesPendientesAction extends ActionSession{
    private Logger log = Logger.getLogger(CriterioBusquedaExpedientesPendientesAction.class);
    private final String FORWARD_CRITERIO = "criterio";
    
    public ActionForward performSession(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {

        log.debug("================= CriterioBusquedaExpedientesPendientesAction ======================>");
        // Validaremos los parametros del request especificados
        HttpSession session = request.getSession();
        UsuarioValueObject usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");

        String opcion = request.getParameter("opcion");
        log.debug("Opcion : " + opcion);
        String codigo                 = request.getParameter("codigo");
        String nombre                 = request.getParameter("nombre");
        String esCampoSuplementario   = request.getParameter("esCampoSuplementario");
        String tipoCampoSuplementario = request.getParameter("tipoCampoSuplementario");
        String tipoCampoFijo          = request.getParameter("tipoCampoFijo");        
        String codigoDesplegable      = request.getParameter("codigoDesplegable");
        // Operador del criterio seleccionado
        String operador               = request.getParameter("operadorSeleccionado");
        // Valores de búsqueda para un criterio ya seleccionado
        String valores                = request.getParameter("valoresSeleccionado");
        // Código del campo correspondiente a un criterio ya seleccionado
        String codCampoSeleccionado   = request.getParameter("codigoCampoSeleccionado");
        
        String tipo=null;

        log.debug("codigo campo a buscar: " + codigo);
        log.debug("nombre campo a buscar: " + nombre);
        log.debug("esCampoSuplementario campo a buscar: " + esCampoSuplementario);
        log.debug("tipoCampoSuplementario campo a buscar: " + tipoCampoSuplementario);
        log.debug("tipoCampoFijo campo a buscar: " + tipoCampoFijo);
      
        if(esCampoSuplementario!=null && "true".equals(esCampoSuplementario) && tipoCampoSuplementario!=null
                && ConstantesDatos.TIPO_DATO_CAMPO_SUPL_DESPLEGABLE.equals(tipoCampoSuplementario) && codigoDesplegable!=null && !"".equals(codigoDesplegable)){

            ArrayList<CampoDesplegableVO> valoresDesplegable = CamposListadoPendientesProcedimientoManager.getInstance().getValoresDesplegable(codigoDesplegable,usuarioVO.getParamsCon());
            log.debug("Número de valores del desplegable recuperados:: " + valoresDesplegable.size());
            request.setAttribute("valores_desplegable",valoresDesplegable);
        }//if
              
        if(esCampoSuplementario!=null && "true".equals(esCampoSuplementario) && tipoCampoSuplementario!=null){
            // Si no es campo suplementario
            if(ConstantesDatos.TIPO_DATO_CAMPO_SUPL_NUMERICO.equals(tipoCampoSuplementario)){
                tipo = "N";
            }else
            if(ConstantesDatos.TIPO_DATO_CAMPO_SUPL_TEXTO_CORTO.equals(tipoCampoSuplementario)){
                tipo = "T";
            }else
            if(ConstantesDatos.TIPO_DATO_CAMPO_SUPL_FECHA.equals(tipoCampoSuplementario)){
                tipo = "F";
            }else
            if(ConstantesDatos.TIPO_DATO_CAMPO_SUPL_DESPLEGABLE.equals(tipoCampoSuplementario)){
                tipo = "D";
            }
        }else
        if((esCampoSuplementario==null || "".equals(esCampoSuplementario)) || "false".equals(esCampoSuplementario)){

            if(ConstantesDatos.TIPO_DATO_CAMPO_FIJO_TEXTO.equals(tipoCampoFijo)){
                tipo = "T";
            }else
            if(ConstantesDatos.TIPO_DATO_CAMPO_FIJO_FECHA.equals(tipoCampoFijo)){
                tipo = "F";
            }else
            if(ConstantesDatos.TIPO_DATO_CAMPO_FIJO_DOCUMENTO.equals(tipoCampoFijo)){
                tipo = "DN"; // Documento del interesado
                ArrayList<TipoDocumentoVO> tiposDocumento =  CamposListadoPendientesProcedimientoManager.getInstance().getTiposDocumento(usuarioVO.getParamsCon());
                request.setAttribute("tipos_documento",tiposDocumento);
            }else
            if(ConstantesDatos.TIPO_DATO_CAMPO_FIJO_NOMBREINTERESADO.equals(tipoCampoFijo)){
                tipo = "I";
            }else
            if(ConstantesDatos.TIPO_DATO_CAMPO_FIJO_NUMEXPEDIENTE.equals(tipoCampoFijo)){
                tipo = "E";
            }
        }

        request.setAttribute("codigo",codigo);
        request.setAttribute("nombre",nombre);
        request.setAttribute("esCampoSuplementario",esCampoSuplementario);
        request.setAttribute("tipoCampoSuplementario",tipoCampoSuplementario);
        request.setAttribute("tipoCampoFijo",tipoCampoFijo);
        request.setAttribute("codigoDesplegable",codigoDesplegable);
        request.setAttribute("tipo",tipo);
        /********************************/
        request.setAttribute("valores",valores);
        request.setAttribute("operador",operador);
        request.setAttribute("codCampoSeleccionado",codCampoSeleccionado);


        log.debug("<================= CriterioBusquedaExpedientesPendientesAction ======================");
        return mapping.findForward(FORWARD_CRITERIO);
    }

}
