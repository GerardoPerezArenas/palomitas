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
 * <p>Descripción: Clase EtiquetasAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Fernando Rueda Rueda
 * @version 1.0
 */


public class EtiquetasAction extends ActionSession  {

  String opcion;

  public ActionForward performSession(ActionMapping mapping, ActionForm form,
                      HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {

    m_Log.debug("perform");
    ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));
    HttpSession session = request.getSession();

    // Validaremos los parametros del request especificados
    ActionErrors errors = new ActionErrors();

    // Rellenamos el form de Administracion
    if (form == null) {
      m_Log.debug("Rellenamos el form de Administracion");
      form = new MantenimientosAdminForm();
      if ("request".equals(mapping.getScope())){
        request.setAttribute(mapping.getAttribute(), form);
      }else{
        session.setAttribute(mapping.getAttribute(), form);
      }
    }
    //Cogemos los parametros contenidos en la sesión
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    String[] params = usuario.getParamsCon();
    //Instanciamos el Form de Terceros que usaremos como contenedor de
    //datos
    MantenimientosAdminForm mantForm = (MantenimientosAdminForm)form;
    //Recogemos los parámetros del request, principalmente vienen de la jsp
    GeneralValueObject gVO = recogerParametros(request);
     if (m_Log.isInfoEnabled()) m_Log.info("la opcion en el action es " + opcion);
    //Instaciamos las clases necesarias para cargar las listas de datos
    //como Aplicaciones, TablasAtributos y Etiquetas
    AplicacionesManager aplicacionesManager = AplicacionesManager.getInstance();
    EtiquetasManager etiquetasManager = EtiquetasManager.getInstance();
    TablasAtributosManager tablasAtributosManager = TablasAtributosManager.getInstance();
    //Añadimos el tipo de ventana en el Form
    mantForm.setVentana(request.getParameter("ventana"));
    if ("cargar".equalsIgnoreCase(opcion))
    {
      //Cargamos las aplicaciones
      Vector listaAplicaciones = aplicacionesManager.getListaAplicaciones(params);
      mantForm.setListaAplicaciones(listaAplicaciones);
      //Cargamos la lista de Tablas de la BD junto con sus Campos
      Vector listaTablasAtributos = new Vector();
      listaTablasAtributos = tablasAtributosManager.getListaTablasAtributos(params);
      mantForm.setListaTablasAtributos(listaTablasAtributos);
      //Cargamos la lista de Etiquetas
      Vector listaEtiquetas = new Vector();
      listaEtiquetas = etiquetasManager.getListaEtiquetas(params);
      mantForm.setListaEtiquetas(listaEtiquetas);
    }
    else if("eliminar".equalsIgnoreCase(opcion))
    {
      Vector listaEtiquetas = new Vector();
      String codigo = (String)gVO.getAtributo("codEtiq");
      listaEtiquetas = etiquetasManager.eliminarEtiqueta(codigo,params);
      mantForm.setListaEtiquetas(listaEtiquetas);
      opcion = "cargarEtiquetas";
    }
    else if("modificar".equals(opcion))
    {
      Vector listaEtiquetas = new Vector();
      listaEtiquetas = etiquetasManager.modificarEtiqueta(gVO,params);
      mantForm.setListaEtiquetas(listaEtiquetas);
      opcion = "cargarEtiquetas";
    }
    else if("alta".equals(opcion))
    {
      Vector listaEtiquetas = new Vector();
      listaEtiquetas = etiquetasManager.altaEtiqueta(gVO,params);
      mantForm.setListaEtiquetas(listaEtiquetas);
      opcion = "cargarEtiquetas";
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
  }

  private GeneralValueObject recogerParametros(HttpServletRequest request){
    GeneralValueObject gVO = new GeneralValueObject();
    //Recogemos y asignamos al gVO los parametros con los nombres que les damos
    //en la jsp
    gVO.setAtributo("codEtiq",request.getParameter("codEtiq"));
    gVO.setAtributo("nomEtiq",request.getParameter("nomEtiq"));
    gVO.setAtributo("descEtiq",request.getParameter("descEtiq"));
    gVO.setAtributo("codAplic",request.getParameter("codAplic"));
    gVO.setAtributo("codCampo",request.getParameter("codCampo"));
    gVO.setAtributo("codEtiqAntiguo",request.getParameter("codEtiqAntiguo"));
    this.opcion = request.getParameter("opcion");
    return gVO;
  }
}