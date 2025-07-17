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

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase IdiomasAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */


public class IdiomasAction extends ActionSession  {

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
    IdiomasManager mantManager = IdiomasManager.getInstance(); 
    GeneralValueObject gVO = recogerParametros(request);
    
    if ("cargar".equalsIgnoreCase(opcion)){
      Vector lista = mantManager.getListaIdiomas(params);
      mantForm.setListaIdiomas(lista);
    }else if("eliminar".equalsIgnoreCase(opcion)){
      String identificador = request.getParameter("identificador");
    	m_Log.debug("identificador "+identificador);
    	Vector listaUsuarios = mantManager.buscarUsuarios(identificador,params);
    	// Hay que comprobar si existen usuarios con el idioma que se quiere borrar asociado
    	if (listaUsuarios.size()==0) {
    		Vector listaIdiomas = mantManager.eliminarIdioma(identificador,params);
    	   	mantForm.setListaIdiomas(listaIdiomas);
    	   	opcion = "vuelveCargar";
    	} else {    		
    		Vector lista = mantManager.getListaIdiomas(params);
    		// Para sacar de la lista el idioma seleccionado    		
    		for (int indice = 0; indice < lista.size(); indice++) {
    			GeneralValueObject vo = (GeneralValueObject) lista.get(indice);
    			if (vo.getAtributo("codigo").equals(identificador))
    				lista.remove(indice);
    		}    		
    		// Se le pasa la lista al Form
    		Vector idiomas = new Vector();
    		for (int indice = 0; indice < lista.size(); indice++) {
    			GeneralValueObject vo = (GeneralValueObject) lista.get(indice);
    			mantForm = new MantenimientosAdminForm();
    			mantForm.setCodigoIdioma((String)vo.getAtributo("codigo"));
    			mantForm.setNombreIdioma((String)vo.getAtributo("descripcion"));
    			idiomas.add(mantForm);
    		}    		
    		session.setAttribute("MantenimientoIdiomas.listaIdiomas", idiomas);
    		session.setAttribute("MantenimientoIdiomas.codIdiomaEliminar", identificador);
    		opcion = "asignarIdioma";
    	}
    } else if("actualizarIdiomaUsuarios".equals(opcion)) {
    	String nuevoCodIdioma = request.getParameter("nuevoCodIdioma");
    	m_Log.debug("nuevoCodIdioma = " + nuevoCodIdioma);
    	String codIdiomaEliminar = request.getParameter("codIdiomaEliminar");
    	m_Log.debug("codIdiomaEliminar = " + codIdiomaEliminar);
    	Vector listaIdiomas = mantManager.eliminarIdiomaConUsuarios(codIdiomaEliminar, nuevoCodIdioma, params);
	   	mantForm.setListaIdiomas(listaIdiomas);
	   	m_Log.debug("Tamaño lista de idiomas = " +  listaIdiomas.size());
      opcion = "vuelveCargar";
    }else if("modificar".equals(opcion)){
        String identificador = request.getParameter("identificador");
        gVO.setAtributo("codigo", identificador);
      Vector lista = mantManager.modificarIdioma(gVO,params);
      mantForm.setListaIdiomas(lista);
      opcion = "vuelveCargar";
    }else if("alta".equals(opcion)){
      Vector lista = mantManager.altaIdioma(gVO,params);
      mantForm.setListaIdiomas(lista);
      opcion = "vuelveCargar";
    }else if("salir".equals(opcion)){
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
    gVO.setAtributo("codigo",request.getParameter("codigo"));
    gVO.setAtributo("descripcion",request.getParameter("descripcion"));
    gVO.setAtributo("clave",request.getParameter("clave"));
    return gVO;  
  }
}