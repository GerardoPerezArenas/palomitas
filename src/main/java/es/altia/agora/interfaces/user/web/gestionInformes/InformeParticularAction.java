package es.altia.agora.interfaces.user.web.gestionInformes;

import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.gestionInformes.particular.CampoEntradaParticular;
import es.altia.agora.business.gestionInformes.particular.InformeParticularFacade;
import es.altia.agora.business.gestionInformes.particular.InformeParticularFactory;
import es.altia.agora.business.gestionInformes.particular.CodigoEtiqueta;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.ArrayList;

public class InformeParticularAction extends ActionSession {

    private static final String PREFIJO_PROP = "InformeParticular";
    private static final String SUFIJO_TITULO_INFORME = "tituloInforme";
    private static final String SUFIJO_LISTA_CAMPOS = "listaCampos";
    private static final String SUFIJO_TITULO_CAMPO = "tituloCampo";
    private static final String SUFIJO_TIPO_CAMPO = "tipoCampo";
    private static final String SUFIJO_TIPO_FICHERO = "tipoFichero";

    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        ResourceBundle infParticularProps = ResourceBundle.getBundle("es.altia.agora.business.gestionInformes.particular.InformeParticular");

        // Obtengo los datos de conexión a la Base de Datos.
        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        
        if (usuario.getIdioma()==ConstantesDatos.IDIOMA_GALLEGO)
        	infParticularProps = ResourceBundle.getBundle("es.altia.agora.business.gestionInformes.particular.InformeParticular_gl_ES"); 
        
        String[] params = usuario.getParamsCon();
        
        if (form == null) {
            form = new InformeParticularForm();
            if ("request".equals(mapping.getScope())) request.setAttribute(mapping.getAttribute(), form);
            else request.getSession().setAttribute(mapping.getAttribute(), form);
        }

        InformeParticularForm infParticularForm = (InformeParticularForm) form;

        String tipoInforme = request.getParameter("tipoInforme");

        String tituloInforme = infParticularProps.getString(PREFIJO_PROP + "/" + tipoInforme + "/" + SUFIJO_TITULO_INFORME);
        infParticularForm.setTituloInforme(tituloInforme);
        
        String tipoFichero = infParticularProps.getString(PREFIJO_PROP + "/" + tipoInforme + "/" + SUFIJO_TIPO_FICHERO);
        infParticularForm.setTipoFichero(tipoFichero);

        String strListaCampos = infParticularProps.getString(PREFIJO_PROP + "/" + tipoInforme + "/" + SUFIJO_LISTA_CAMPOS);
        StringTokenizer tokenizer = new StringTokenizer(strListaCampos, ";");
        ArrayList<String> listaIdsCampos = new ArrayList<String>();
        while (tokenizer.hasMoreTokens()) {
            listaIdsCampos.add(tokenizer.nextToken());
        }

        InformeParticularFacade infFacade = InformeParticularFactory.getImpl(tipoInforme, params);

        ArrayList<CampoEntradaParticular> listaCampos = new ArrayList<CampoEntradaParticular>();
        
        for (String idCampo:listaIdsCampos) {

            String tituloCampo = infParticularProps.getString(PREFIJO_PROP + "/" + tipoInforme + "/" + idCampo + "/" + SUFIJO_TITULO_CAMPO);
            String tipoCampo = infParticularProps.getString(PREFIJO_PROP + "/" + tipoInforme + "/" + idCampo + "/" + SUFIJO_TIPO_CAMPO);
            ArrayList<CodigoEtiqueta> codEtiquetas = new ArrayList<CodigoEtiqueta>();
            if (tipoCampo.equals(CampoEntradaParticular.TIPO_CAMPO_SELECT) || 
            		tipoCampo.equals(CampoEntradaParticular.TIPO_CAMPO_RADIO)
            		|| tipoCampo.equals(CampoEntradaParticular.TIPO_CAMPO_LIST)) {
                codEtiquetas = infFacade.getCodigosEtiquetasSelect(idCampo, usuario);
            }
            else if (tipoCampo.equals(CampoEntradaParticular.TIPO_CAMPO_CALENDAR) ||
            		tipoCampo.equals(CampoEntradaParticular.TIPO_CAMPO_RANGO_CALENDAR)){
            	codEtiquetas.add(new CodigoEtiqueta(idCampo,"I"+idCampo));            	
            }
            
            
            CampoEntradaParticular campo = new CampoEntradaParticular(idCampo, tipoCampo, tituloCampo, codEtiquetas);
            listaCampos.add(campo);
        
        }

        infParticularForm.setEstadoInforme("pedirDatos");
        infParticularForm.setTipoInforme(tipoInforme);
        
        request.setAttribute("listaCampos", listaCampos);
        request.setAttribute("InformeParticularForm", infParticularForm);

        request.getSession().setAttribute("fachadaInformeParticular", infFacade);

        return mapping.findForward("cargarPaginaDatos");
    }
}
