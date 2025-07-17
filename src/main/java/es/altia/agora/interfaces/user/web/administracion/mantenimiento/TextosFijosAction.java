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
 * <p>Descripción: Clase TextosFijosAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Fernando Rueda Rueda
 * @version 1.0
 */


public class TextosFijosAction extends ActionSession
{

  public ActionForward performSession(ActionMapping mapping, ActionForm form,
                      HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {

    m_Log.debug("perform");
    ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));
    HttpSession session = request.getSession();

    // Validaremos los parametros del request especificados
    ActionErrors errors = new ActionErrors();
    String opcion = request.getParameter("opcion");
    if (m_Log.isInfoEnabled()) m_Log.info("la opcion en el action es " + opcion);

    // Rellenamos el form de Mantenimiento
    if (form == null)
    {
      m_Log.debug("Rellenamos el form de MantenimientosAdmin");
      form = new MantenimientosAdminForm();
      if ("request".equals(mapping.getScope()))
      {
        request.setAttribute(mapping.getAttribute(), form);
      }
      else
      {
        session.setAttribute(mapping.getAttribute(), form);
      }//del if
    }//del if
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    String[] params = usuario.getParamsCon();
    MantenimientosAdminForm mantForm = (MantenimientosAdminForm)form;
    AplicacionesManager aplicacionesManager = AplicacionesManager.getInstance();
    IdiomasManager idiomasManager = IdiomasManager.getInstance();
    TextosFijosManager textosFijosManager = TextosFijosManager.getInstance();
    mantForm.setVentana(request.getParameter("ventana"));
    GeneralValueObject gVO = recogerParametros(request);
    if ("cargar".equalsIgnoreCase(opcion))
    {
      Vector listaAplicaciones = aplicacionesManager.getListaAplicaciones(params);
      mantForm.setListaAplicaciones(listaAplicaciones);
      Vector listaIdiomas = idiomasManager.getListaIdiomas(params);
      mantForm.setListaIdiomas(listaIdiomas);
    }
    else if(opcion.equals("cargarTextosFijos"))
    {
      Vector listaTextosFijos = new Vector();
      listaTextosFijos = textosFijosManager.getListaTextosFijos(gVO,params);
      mantForm.setListaTextosFijos(listaTextosFijos);
    }
    else if("eliminar".equalsIgnoreCase(opcion))
    {
      Vector textosFijos = new Vector();
      textosFijos = textosFijosManager.eliminarTextoFijo(gVO,params);
      mantForm.setListaTextosFijos(textosFijos);
      opcion = "cargarTextosFijos";
    }
    else if("modificar".equals(opcion))
    {
      Vector textosFijos = new Vector();
      textosFijos = textosFijosManager.modificarTextoFijo(gVO,params);
      mantForm.setListaTextosFijos(textosFijos);
      opcion = "cargarTextosFijos";
    }
    else if("alta".equals(opcion))
    {
      Vector textosFijos = new Vector();
      textosFijos = textosFijosManager.altaTextoFijo(gVO,params);
      mantForm.setListaTextosFijos(textosFijos);
      opcion = "cargarTextosFijos";
    }
    else if("salir".equals(opcion))
    {
      if ((session.getAttribute(mapping.getAttribute()) != null))
        session.removeAttribute(mapping.getAttribute());
    }
    else
    {
      opcion = mapping.getInput();
    }
    m_Log.debug("perform");
    return (mapping.findForward(opcion));
  }//de la funcion

  private GeneralValueObject recogerParametros(HttpServletRequest request)
  {
    GeneralValueObject gVO = new GeneralValueObject();
    gVO.setAtributo("codApli",request.getParameter("codApli"));
    gVO.setAtributo("codText",request.getParameter("codText"));
    gVO.setAtributo("codIdi",request.getParameter("codIdi"));
    gVO.setAtributo("texto",request.getParameter("texto"));
    if (request.getParameter("estado")==null || request.getParameter("estado").equals("")) {
        gVO.setAtributo("estado","null");
    } else {        
        gVO.setAtributo("estado",request.getParameter("estado"));
    }
    gVO.setAtributo("fechaCreMod",request.getParameter("fechaCreMod"));
    gVO.setAtributo("codIdiAntiguo",request.getParameter("codIdiAntiguo"));
    gVO.setAtributo("codTextAntiguo",request.getParameter("codTextAntiguo"));
    return gVO;
  }//de la funcion
}//de la clase