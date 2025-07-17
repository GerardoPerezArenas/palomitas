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
 * <p>Descripción: Clase AplicacionesAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */


public class AplicacionesAction extends ActionSession  {

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
    AplicacionesManager mantManager = AplicacionesManager.getInstance();
  	Vector listaIdiomas = IdiomasManager.getInstance().getListaIdiomas(params);
  	mantForm.setListaIdiomas(listaIdiomas);
    GeneralValueObject gVO = recogerParametros(request);
    
    if ("cargar".equalsIgnoreCase(opcion)){
      Vector lista = mantManager.getListaAplicaciones(params);
      mantForm.setListaAplicaciones(lista);
    }else if("eliminar".equalsIgnoreCase(opcion)){
      Vector lista = new Vector();
      String codigo = (String)gVO.getAtributo("codigo");
      lista = mantManager.eliminarAplicacion(codigo,params);
      mantForm.setListaAplicaciones(lista);
      opcion = "vuelveCargar";
    }else if("modificar".equals(opcion)){
      Vector lista = mantManager.modificarAplicacion(gVO,params);
      mantForm.setListaAplicaciones(lista);
      opcion = "vuelveCargar";
    }else if("alta".equals(opcion)){
      Vector lista = mantManager.altaAplicacion(gVO,params);
      mantForm.setListaAplicaciones(lista);
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
    String seguridad = request.getParameter("Seguridad");
    String accesoDefecto = request.getParameter("Acceso");
    String conEntidades = request.getParameter("Entidades");
    String conEjercicios = request.getParameter("Ejercicios");
    String cadenaTitulos = request.getParameter("titulos_idiomas");
    Vector titulos = construirTitulosIdiomas(cadenaTitulos);
    m_Log.debug ("**********************"+titulos);

    
    GeneralValueObject gVO = new GeneralValueObject();
    gVO.setAtributo("codigo",request.getParameter("codigo")); //CÓDIGO DE LA APLICACIÓN.
    gVO.setAtributo("descripcion",request.getParameter("descripcion"));
    gVO.setAtributo("ejecutable",request.getParameter("ejecutable"));
    gVO.setAtributo("icono",request.getParameter("icono"));            
    gVO.setAtributo("seguridad",seguridad);
    gVO.setAtributo("accesoDefecto",accesoDefecto);
    gVO.setAtributo("diccionario",request.getParameter("diccionario"));
    gVO.setAtributo("informes",request.getParameter("informes"));
    gVO.setAtributo("version",request.getParameter("version"));
    gVO.setAtributo("conEntidades",conEntidades);
    gVO.setAtributo("conEjercicios",conEjercicios);
    gVO.setAtributo("codigoAntiguo",request.getParameter("codigoAntiguo"));
    gVO.setAtributo("titulos", titulos);
    
    
    return gVO;
  }
  
  
  private Vector construirTitulosIdiomas(String cadena) {
	  	Vector r = new Vector();

	    StringTokenizer codigos = null;

	    if (cadena!= null) {
	      codigos = new StringTokenizer(cadena,"§¥,",false);

	      while (codigos.hasMoreTokens()) {
	         String idioma = codigos.nextToken();
	         if (codigos.hasMoreTokens()){
	         	String titulo = codigos.nextToken();
	         	Vector id = new Vector();
	         	id.addElement(idioma);
	         	id.addElement(titulo);
	         	r.addElement(id);         
	         	if (m_Log.isDebugEnabled()) m_Log.debug("descripcion -->"+ idioma + " -- " + titulo);
	         }
	      }
	   }
	   return r;
	}  
  
  
  
  
  
}