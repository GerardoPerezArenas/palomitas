// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.administracion;

// PAQUETES IMPORTADOS
import es.altia.agora.business.escritorio.*;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.helper.*;
import es.altia.agora.business.administracion.persistence.AutorizacionesInternasManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.AplicacionesManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.OrganizacionesManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.EntidadesManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.MenusManager;
import es.altia.agora.business.util.*;
import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;


import org.apache.struts.action.*;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase MenusAction</p>
 * <p>Empresa: ALTIA CONSULTORES</p>
 */


public class AutorizacionesInternasAction extends ActionSession  {

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
      m_Log.debug("Rellenamos el form de AutorizacionesInternasForm");
      form = new AutorizacionesInternasForm();
      if ("request".equals(mapping.getScope())){
        request.setAttribute(mapping.getAttribute(), form);
      }else{
        session.setAttribute(mapping.getAttribute(), form);
      }
    }
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    String[] params = usuario.getParamsCon();
    AutorizacionesInternasForm mantForm = (AutorizacionesInternasForm) form;
    
    GeneralValueObject gVO = recogerParametros(request);
    if ("cargar".equalsIgnoreCase(opcion)){
      Vector lista = OrganizacionesManager.getInstance().getListaOrganizaciones(params);
      mantForm.setListaOrganizaciones(lista);
      lista = AplicacionesManager.getInstance().getListaAplicaciones(params);
      mantForm.setListaAplicaciones(lista);
	  } else if ("cargarGen".equalsIgnoreCase(opcion)){
      Vector lista = OrganizacionesManager.getInstance().getListaOrganizaciones(params);
      mantForm.setListaOrganizaciones(lista);
      lista = AplicacionesManager.getInstance().getListaAplicaciones(params);
      mantForm.setListaAplicaciones(lista);
	  } else if ("cargarEntidades".equalsIgnoreCase(opcion)){
      Vector lista = EntidadesManager.getInstance().getListaEntidades(gVO, params);
      mantForm.setListaEntidades(lista);      
    } else if ("cargarAplicaciones".equalsIgnoreCase(opcion)){
      Vector lista = AplicacionesManager.getInstance().getListaAplicaciones(gVO,params);
      mantForm.setListaAplicaciones(lista);      
    } else if ("cargarBusqueda".equalsIgnoreCase(opcion)){
      Vector lista = AutorizacionesInternasManager.getInstance().getListaGrupos(gVO,params);
      mantForm.setListaGrupos(lista);
      lista = AutorizacionesInternasManager.getInstance().getListaUsuarios(gVO,params);
      mantForm.setListaUsuarios(lista);
      lista = MenusManager.getInstance().getListaMenus(gVO,params);
      mantForm.setListaMenus(lista);
      mantForm.setOtrosDatos(gVO);
      opcion = "vuelveCargar";
    } else if ("actualizarPermisosGrupo".equalsIgnoreCase(opcion)){
      AutorizacionesInternasManager.getInstance().actualizarPermisosGrupo(gVO,params);      
      Vector lista = AutorizacionesInternasManager.getInstance().getListaGrupos(gVO,params);
      mantForm.setListaGrupos(lista);
      lista = AutorizacionesInternasManager.getInstance().getListaUsuarios(gVO,params);
      mantForm.setListaUsuarios(lista);
      lista = MenusManager.getInstance().getListaMenus(gVO,params);
      mantForm.setListaMenus(lista);
      mantForm.setOtrosDatos(gVO);
      opcion = "vuelveCargar";
	  } else if ("actualizarPermisosUsuario".equalsIgnoreCase(opcion)){
      AutorizacionesInternasManager.getInstance().actualizarPermisosUsuario(gVO,params);      
      Vector lista = AutorizacionesInternasManager.getInstance().getListaGrupos(gVO,params);
      mantForm.setListaGrupos(lista);
      lista = AutorizacionesInternasManager.getInstance().getListaUsuarios(gVO,params);
      mantForm.setListaUsuarios(lista);
      lista = MenusManager.getInstance().getListaMenus(gVO,params);
      mantForm.setListaMenus(lista);
      mantForm.setOtrosDatos(gVO);
      opcion = "vuelveCargar";      
    } else if("salir".equals(opcion)){
      if ((session.getAttribute(mapping.getAttribute()) != null))
        session.removeAttribute(mapping.getAttribute());
    }else{
      opcion = mapping.getInput();
    }
    m_Log.debug("perform");
    return (mapping.findForward(opcion));
  }

  private GeneralValueObject recogerParametros(HttpServletRequest request){
    GeneralValueObject gVO = new GeneralValueObject();
    /*
    gVO.setAtributo("codOrganizacion",request.getParameter("codOrganizacion"));
    gVO.setAtributo("codAplicacion",request.getParameter("codAplicacion"));    
    gVO.setAtributo("codEntidad",request.getParameter("codEntidad"));    
    */
    gVO.setAtributo("codOrganizacion",request.getParameter("organizacion"));
    gVO.setAtributo("codAplicacion",request.getParameter("aplicacion"));    
    gVO.setAtributo("codEntidad",request.getParameter("entidad"));    

    gVO.setAtributo("codigoGU",request.getParameter("codigoGU"));      
    String cadena = request.getParameter("nuevosPermisos");
    Vector lista = new Vector();
    if (lista != null)
    	if (!"".equals(cadena)) lista = construirPermisos(cadena);
    gVO.setAtributo("nuevosPermisos", lista);        
    return gVO;    
  }
  
  private Vector construirPermisos(String c) {
  	Vector r = new Vector();

    StringTokenizer codigos = null;

    if (c!= null) {
      codigos = new StringTokenizer(c,"§¥,",false);

      while (codigos.hasMoreTokens()) {
         String proceso = codigos.nextToken();
         if (codigos.hasMoreTokens()){
         	String permiso = codigos.nextToken();
         	Vector id = new Vector();
         	id.addElement(proceso);
         	id.addElement(permiso);
         	r.addElement(id);         
         	if(m_Log.isDebugEnabled()) m_Log.debug("proceso -->"+ proceso+ " -- " + permiso);
         }
      }
   }
   return r;
}

}