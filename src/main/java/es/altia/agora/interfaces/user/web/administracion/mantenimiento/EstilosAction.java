// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.administracion.mantenimiento;

// PAQUETES IMPORTADOS
import es.altia.agora.business.escritorio.*;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.helper.*;
import es.altia.agora.business.administracion.mantenimiento.persistence.*;
import es.altia.agora.business.util.*;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.commons.fileupload.*;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase OrganizacionesAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */


public class EstilosAction extends ActionSession  {

  public ActionForward performSession(ActionMapping mapping, ActionForm form, 
                      HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {

    m_Log.debug("perform");
    ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));
    HttpSession session = request.getSession();

    // Validaremos los parametros del request especificados
    ActionErrors errors = new ActionErrors();
    String opcion = request.getParameter("opcion");
    if (m_Log.isInfoEnabled()) m_Log.info("la opcion en el action es " + opcion);

    // Rellenamos el form de BusquedaTerceros
    if (form == null) {
      m_Log.debug("Rellenamos el form de MantenimientosAdminForm");
      form = new MantenimientosAdminForm();
      if ("request".equals(mapping.getScope())){
        request.setAttribute(mapping.getAttribute(), form);
      }else{
        session.setAttribute(mapping.getAttribute(), form);
      }
    }
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    String[] params = usuario.getParamsCon();
    MantenimientosAdminForm mantForm = (MantenimientosAdminForm)form;
    EstilosManager mantManager = EstilosManager.getInstance(); 
    GeneralValueObject gVO = recogerParametros(request);
    mantForm.setOperacion("");
    if ("cargar".equalsIgnoreCase(opcion)){
          m_Log.debug("************   cargar organizacion en estilo");
        Vector lista = mantManager.getListaOrganizaciones(params);
        Vector listaCss = mantManager.getListaCss(params);
        mantForm.setListaCss(listaCss);
        mantForm.setListaOrganizaciones(lista);
    
    }else if ("cargarLocal".equalsIgnoreCase(opcion)){
          m_Log.debug("************   cargar organizacion en estilo");
        Vector lista = mantManager.getListaOrganizaciones(params);
        Vector listaCss = mantManager.getListaCss(params);
        mantForm.setListaCss(listaCss);
        mantForm.setListaOrganizaciones(lista);
    
         opcion = "cargarLocal";
    
    
    }else if("modificarOrgCss".equalsIgnoreCase(opcion)){
        m_Log.debug("************   Modificar el estilo de una organizacion");
        String codCss = request.getParameter("codCss");
        String codOrg = request.getParameter("codOrg");

        Vector lista = mantManager.modificarCssOrganizacion(gVO,params);
         mantForm.setListaOrganizaciones(lista);
         opcion = "vuelveCargar";

    
     }else if("modificarCss".equalsIgnoreCase(opcion)){
        m_Log.debug("************   Modificar Css");
        String codCss = request.getParameter("codiCss");
        String activo = request.getParameter("act");
  m_Log.debug("************   Modificar Css"+request.getParameter("act"));
         m_Log.debug("************   Modificar Css"+activo);
        Vector lista = mantManager.modificarCss(gVO,params);
        
          
         mantForm.setListaCss(lista);
         opcion = "vuelveCargar";
         
     }else if("eliminarCss".equalsIgnoreCase(opcion)){
        m_Log.debug("************   Eliminar Css");
        String codCss = request.getParameter("codiCss");

  m_Log.debug("************   Eliminar Css"+request.getParameter("act"));
         m_Log.debug("************   Eliminar Css"+codCss);
        Vector lista = mantManager.eliminarCss(gVO,params);
        
          
         mantForm.setListaCss(lista);
         opcion = "vuelveCargar";

     }else if("documentoNuevo".equals(opcion)){
           m_Log.debug("************  docuemento nuevo  ");
        opcion="documentoNuevo";

     }else if("prototipo".equals(opcion)){
          m_Log.debug("************  PROTOTIPO  ");
          String codCss = request.getParameter("codCss");
            m_Log.debug("************   cargar Css"+codCss);
            Vector lista = mantManager.buscarCss(gVO,params); 
             mantForm.setListaCss(lista);
            
        opcion="prototipo";

   
    }else{
      opcion = mapping.getInput();
    }
    m_Log.debug("perform");
    return (mapping.findForward(opcion));
  }

  private GeneralValueObject recogerParametros(HttpServletRequest request){
    GeneralValueObject gVO = new GeneralValueObject();
    gVO.setAtributo("codCss",request.getParameter("codCss"));
    gVO.setAtributo("codOrg",request.getParameter("codOrg"));
    gVO.setAtributo("activo",request.getParameter("act"));
     gVO.setAtributo("codiCss",request.getParameter("codiCss"));
    return gVO;  
  }
}