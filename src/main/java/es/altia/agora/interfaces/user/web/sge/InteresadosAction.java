// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.sge;

// PAQUETES IMPORTADOS
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.InteresadoExpedienteVO;
import es.altia.agora.business.sge.persistence.InteresadosManager;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.interfaces.user.web.helper.ActionHelper;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;


public class InteresadosAction extends ActionSession  {
    protected static Log m_Log =
            LogFactory.getLog(InteresadosAction.class.getName());

    public ActionForward performSession(ActionMapping mapping,
                                        ActionForm form,
                                        HttpServletRequest request,
                                        HttpServletResponse response)
            throws IOException, ServletException {

        m_Log.debug("================= InteresadosAction ======================>");
        m_Log.debug(" opcion: " + request.getParameter("opcion"));
        ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));
        HttpSession session = request.getSession();

        // Validaremos los parametros del request especificados
        ActionErrors errors = new ActionErrors();
        String opcion = request.getParameter("opcion");

        // Rellenamos el form
        InteresadosForm intForm = (InteresadosForm)form;
        if (form == null) {
            m_Log.debug("Rellenamos el form de InteresadosForm");
            form = new InteresadosForm();
            if ("request".equals(mapping.getScope())){
                request.setAttribute(mapping.getAttribute(), form);
            }else{
                session.setAttribute(mapping.getAttribute(), form);
            }
        }

        UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        String[] params = usuario.getParamsCon();

        InteresadosManager intManager = InteresadosManager.getInstance();
        Vector listaInteresados = new Vector();

        GeneralValueObject gVO = new GeneralValueObject();

        if ("cargarRegistro".equals(opcion)){
        	 Vector listaRoles = new Vector();
        	String codMunicipio=request.getParameter("codMun");
        	String codProc = request.getParameter("codProc");
        	String munProc = request.getParameter("munProc");
        	
        	gVO.setAtributo("codMunicipio",codMunicipio);
            gVO.setAtributo("ejercicio",request.getParameter("codEje"));
            gVO.setAtributo("numero",request.getParameter("codNum"));
            gVO.setAtributo("codOur",request.getParameter("codOur"));
            gVO.setAtributo("codDep",request.getParameter("codDep"));
            gVO.setAtributo("codTip",request.getParameter("codTip"));
            gVO.setAtributo("codProc", codProc);
            gVO.setAtributo("munProc", munProc);
            // Interesados
        	if(codMunicipio!=null){
               listaInteresados = InteresadosManager.getInstance().getListaInteresadosRegistro (gVO,params);        	             
               intForm.setListaInteresados(listaInteresados);
               intForm.setExpTerVO(gVO);                              
        	}
        	// Roles
        	if (codProc!=null && !codProc.equals("")) { // Roles del procedimiento
        	    GeneralValueObject proc = new GeneralValueObject();
        	    proc.setAtributo("codMunicipio", munProc);
        	    proc.setAtributo("codProcedimiento", codProc);        	
        	    listaRoles = obtenerRolesProcedimiento(proc, params);
        	} else {  // Roles por defecto
        	    listaRoles = InteresadosManager.getInstance().getListaRolesRegistro(params);        	    
        	}
            intForm.setListaRoles(listaRoles);
        	
        }else if ("cargar".equals(opcion)){
            gVO.setAtributo("codMunicipio",request.getParameter("codMun"));
            gVO.setAtributo("codProcedimiento",request.getParameter("codProc"));
            gVO.setAtributo("ejercicio",request.getParameter("eje"));
            gVO.setAtributo("numero",request.getParameter("num"));
            Vector listaRoles = intManager.getListaRoles(gVO,params);
            intForm.setListaRoles(listaRoles);
            intForm.setExpTerVO(gVO);

        }else if("eliminar".equals(opcion)){

            gVO = (GeneralValueObject) intForm.getExpTerVO();
            gVO.setAtributo("codigoTercero",request.getParameter("tercero"));
            gVO.setAtributo("versionTercero",request.getParameter("versionTercero"));

            listaInteresados = intManager.eliminarInteresado(gVO,params);
            intForm.setListaInteresados(listaInteresados);
            opcion = "recargar";

        }else if("modificar".equals(opcion)){

            gVO = (GeneralValueObject) intForm.getExpTerVO();
            gVO.setAtributo("codigoTercero",request.getParameter("tercero"));
            gVO.setAtributo("versionTercero",request.getParameter("versionTercero"));
            gVO.setAtributo("rol",request.getParameter("codRol"));

            listaInteresados = intManager.modificarInteresado(gVO,params);
            if (listaInteresados != null){
                intForm.setListaInteresados(listaInteresados);
            }
            opcion = "recargar";

        }else if("alta".equals(opcion)){

            gVO = (GeneralValueObject) intForm.getExpTerVO();
            gVO.setAtributo("codigoTercero",request.getParameter("tercero"));
            gVO.setAtributo("versionTercero",request.getParameter("versionTercero"));
            gVO.setAtributo("domicilio",request.getParameter("domicilio"));
            gVO.setAtributo("rol",request.getParameter("codRol"));

            listaInteresados = intManager.altaInteresado(gVO,params);
            intForm.setListaInteresados(listaInteresados);
            opcion = "recargar";

        } else if("grabarInteresados".equals(opcion)){
            String listaCodTercero = request.getParameter("listaCodTercero");
            String listaVersionTercero = request.getParameter("listaVersionTercero");
            String listaCodDomicilio = request.getParameter("listaCodDomicilio");
            String listaRol = request.getParameter("listaRol");
            String listaMostrar = request.getParameter("listaMostrar");
            String codMunicipio = request.getParameter("codMunicipio");
            String codProcedimiento = request.getParameter("codProcedimiento");
            String ejercicio = request.getParameter("ejercicio");
            String numero = request.getParameter("numero");
            gVO.setAtributo("listaCodTercero",listaTemasSeleccionados(listaCodTercero));
            gVO.setAtributo("listaVersionTercero",listaTemasSeleccionados(listaVersionTercero));
            gVO.setAtributo("listaCodDomicilio",listaTemasSeleccionados(listaCodDomicilio));
            gVO.setAtributo("listaRol",listaTemasSeleccionados(listaRol));
            gVO.setAtributo("listaMostrar",listaTemasSeleccionados(listaMostrar));
            gVO.setAtributo("codMunicipio",codMunicipio);
            gVO.setAtributo("codProcedimiento",codProcedimiento);
            gVO.setAtributo("ejercicio",ejercicio);
            gVO.setAtributo("numero",numero);
            
        } else if("grabarInteresadosRegistro".equals(opcion)){
            String listaCodTercero = request.getParameter("listaCodTercero");
            String listaVersionTercero = request.getParameter("listaVersionTercero");
            String listaCodDomicilio = request.getParameter("listaCodDomicilio");
            String listaRol = request.getParameter("listaRol");
            String codMunicipio = request.getParameter("codMunicipio");
           // String codProcedimiento = request.getParameter("codProcedimiento");
            String ejercicio = request.getParameter("ejercicio");
            String numero = request.getParameter("numero");
            String codOur = request.getParameter("codOur");
            String codTip=request.getParameter("codTip");
            String codDep=request.getParameter("codDep");
            gVO.setAtributo("listaCodTercero",listaTemasSeleccionados(listaCodTercero));
            gVO.setAtributo("listaVersionTercero",listaTemasSeleccionados(listaVersionTercero));
            gVO.setAtributo("listaCodDomicilio",listaTemasSeleccionados(listaCodDomicilio));
            gVO.setAtributo("listaRol",listaTemasSeleccionados(listaRol));
            gVO.setAtributo("codMunicipio",codMunicipio);
            gVO.setAtributo("codOur",codOur);
            gVO.setAtributo("ejercicio",ejercicio);
            gVO.setAtributo("numero",numero);
            gVO.setAtributo("codTip",codTip);
            gVO.setAtributo("codDep",codDep);
        } else if("salir".equals(opcion)){

            if ((session.getAttribute(mapping.getAttribute()) != null))
                session.removeAttribute(mapping.getAttribute());

        }else{

            opcion = mapping.getInput();

        }
        m_Log.debug("<================= InteresadosAction ======================");
        
        return (mapping.findForward(opcion));
    }

    private Vector listaTemasSeleccionados(String listTemasSelecc) {
        Vector lista = new Vector();
        StringTokenizer valores = null;
        if (listTemasSelecc != null) {
            valores = new StringTokenizer(listTemasSelecc,"§¥",false);
            while (valores.hasMoreTokens()) {
                String valor = valores.nextToken();
                lista.addElement(valor);
            }
        }
        return lista;
    }
    
    /**
     *  Obtiene la lista de roles correspondientes al código y municipio de procedimiento que se pasan
     *  en el GeneralValueObject. Los roles del procedimiento traen de "valor por defecto" 1 y 0
     *  que se traducen a SI y NO.
     *  
     *  @returns Un Vector de GeneralValueObject con los roles del procedimiento.
     */
    private Vector<GeneralValueObject> obtenerRolesProcedimiento(GeneralValueObject procedimiento, String[] params){
        
        Vector<GeneralValueObject> rolesProc = InteresadosManager.getInstance().getListaRoles(procedimiento, params);
        for (GeneralValueObject rolProc : rolesProc) {
            rolProc.setAtributo("porDefecto", rolProc.getAtributo("porDefecto").equals("1") ? "SI" : "NO");
        }
        return rolesProc;
    }
    
}