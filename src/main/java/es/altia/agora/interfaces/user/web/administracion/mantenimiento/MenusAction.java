// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.administracion.mantenimiento;

// PAQUETES IMPORTADOS
import es.altia.agora.business.escritorio.*;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.helper.*;
import es.altia.agora.business.administracion.mantenimiento.persistence.AplicacionesManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.OrganizacionesManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.ProcesosManager;
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


public class MenusAction extends ActionSession  {

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
    
    MenusManager mantManager = MenusManager.getInstance(); 
    GeneralValueObject gVO = recogerParametros(request);
    if ("cargar".equalsIgnoreCase(opcion)){
      Vector lista = OrganizacionesManager.getInstance().getListaOrganizaciones(params);
      mantForm.setListaOrganizaciones(lista);
      lista = AplicacionesManager.getInstance().getListaAplicaciones(params);
      mantForm.setListaAplicaciones(lista);      
    }else if ("cargarMenus".equalsIgnoreCase(opcion)){
      Vector lista = mantManager.getListaMenus(gVO,params);
      mantForm.setListaMenus(lista);
      mantForm.setOtrosDatos(gVO);
      lista = ProcesosManager.getInstance().getListaProcesos(gVO,params);
      mantForm.setListaProcesos(lista);      
      opcion = "vuelveCargar";
    }else if("altaMenu".equals(opcion)){
      Vector lista = mantManager.altaMenu(gVO,params);
      mantForm.setOtrosDatos(gVO);
      mantForm.setListaMenus(lista);      
      opcion = "vuelveCargar";
    } else if("modificarMenu".equals(opcion)){
      Vector lista = mantManager.modificarMenu(gVO,params);
      mantForm.setOtrosDatos(gVO);
      mantForm.setListaMenus(lista);      
      opcion = "vuelveCargar";
	} else if("eliminarMenu".equals(opcion)){
      Vector lista = mantManager.eliminarMenu(gVO,params);      
      mantForm.setOtrosDatos(gVO);
      mantForm.setListaMenus(lista);      
      opcion = "vuelveCargar";    
	} else if("altaPuntoMenu".equals(opcion)){
      Vector lista = mantManager.altaPuntoMenu(gVO,params);
      mantForm.setOtrosDatos(gVO);
      mantForm.setListaMenus(lista);      
      opcion = "vuelveCargar";
    } else if("altaTodosPuntosMenu".equals(opcion)){
      Vector listaMenuDiputacion = mantManager.obtenerMenuDiputacion(gVO,params);
      Vector lista = mantManager.altaMenuCompleto(gVO,listaMenuDiputacion,params);
      mantForm.setOtrosDatos(gVO);
      mantForm.setListaMenus(lista);      
      opcion = "vuelveCargar";
    } else if("modificarPuntoMenu".equals(opcion)){
      Vector lista = mantManager.modificarPuntoMenu(gVO,params);
      mantForm.setOtrosDatos(gVO);
      mantForm.setListaMenus(lista);      
      opcion = "vuelveCargar";
	} else if("eliminarPuntoMenu".equals(opcion)){
      Vector lista = mantManager.eliminarPuntoMenu(gVO,params);      
      mantForm.setOtrosDatos(gVO);
      mantForm.setListaMenus(lista);      
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
    gVO.setAtributo("codOrganizacion",request.getParameter("codOrganizacion"));
    gVO.setAtributo("codAplicacion",request.getParameter("codAplicacion"));    
    gVO.setAtributo("codMenu",request.getParameter("codMenu"));
    gVO.setAtributo("descMenu",request.getParameter("descMenu"));
    gVO.setAtributo("codProceso",request.getParameter("codProceso"));
    gVO.setAtributo("codElemento",request.getParameter("codElemento"));
    gVO.setAtributo("codPadre",request.getParameter("codPadre"));
    return gVO;
  }
  
  /*
  private Vector construirIdiomasDescripciones(String c) {
  	Vector r = new Vector();

    StringTokenizer codigos = null;

    if (c!= null) {
      codigos = new StringTokenizer(c,"§¥,",false);

      while (codigos.hasMoreTokens()) {
         String idioma = codigos.nextToken();
         if (codigos.hasMoreTokens()){
         	String descripcion = codigos.nextToken();
         	Vector id = new Vector();
         	id.addElement(idioma);
         	id.addElement(descripcion);
         	r.addElement(id);         
         	m_Log.debug("descripcion -->"+ idioma + " -- " + descripcion);
         }
      }
   }
   return r;
}
*/
}