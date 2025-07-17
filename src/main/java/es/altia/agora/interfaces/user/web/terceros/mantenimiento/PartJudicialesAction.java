// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.terceros.mantenimiento;

// PAQUETES IMPORTADOS
import es.altia.agora.business.escritorio.*;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.helper.*;
import es.altia.agora.business.terceros.ParametrosTerceroValueObject;
import es.altia.agora.business.terceros.mantenimiento.persistence.*;
import es.altia.agora.business.util.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase EntidadesSingularesAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: ALTIA CONSULTORES</p>
 * @author Jorge Hombre Tuñas
 * @version 1.0
 */


public class PartJudicialesAction extends ActionSession  {
  protected static Log m_Log =
          LogFactory.getLog(PartJudicialesAction.class.getName());
  String codPais = "";
  String codProvincia = "";
  String codMunicipio = "";

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
      m_Log.debug("Rellenamos el form de MantenimientosTerceros");
      form = new MantenimientosTercerosForm();
      if ("request".equals(mapping.getScope())){
        request.setAttribute(mapping.getAttribute(), form);
      }else{
        session.setAttribute(mapping.getAttribute(), form);
      }
    }
    //Cogemos los parametros contenidos en la sesión
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    ParametrosTerceroValueObject paramsTercero = (ParametrosTerceroValueObject)session.getAttribute("parametrosTercero");
    codPais = paramsTercero.getPais();
    codProvincia = paramsTercero.getProvincia();
    codMunicipio = paramsTercero.getMunicipio();
    String[] params = usuario.getParamsCon();
    //Instanciamos el Form de Terceros que usaremos como contenedor de
    //datos
    MantenimientosTercerosForm mantForm = (MantenimientosTercerosForm)form;
    //Recogemos los parámetros del request, principalmente vienen de la jsp
    GeneralValueObject gVO = recogerParametros(request);
    ProvinciasManager provinciasManager = ProvinciasManager.getInstance();
    PartJudicialesManager partJudicialesManager = PartJudicialesManager.getInstance();
    //Añadimos el tipo de ventana en el Form
    mantForm.setVentana(request.getParameter("ventana"));
    if ("cargar".equalsIgnoreCase(opcion))
    {
    	GeneralValueObject g = new GeneralValueObject();
    	g.setAtributo("codPais",codPais);
    	g.setAtributo("codProvincia",codProvincia);
      Vector listaProvincias = provinciasManager.getListaProvincias(g,params);
      mantForm.setListaProvincias(listaProvincias);

    }
    else if(opcion.equals("cargarPartJudiciales"))
    {
      Vector listaPartJudiciales = new Vector();
      listaPartJudiciales = partJudicialesManager.getListaPartJudiciales(gVO,params);
      mantForm.setListaPartJudiciales(listaPartJudiciales);
      mantForm.setOperacion("cargarPartJudiciales");
      opcion = "oculto";
    }
    else if("eliminar".equalsIgnoreCase(opcion))
    {
      Vector listaPartJudiciales = new Vector();
      listaPartJudiciales = partJudicialesManager.eliminarPartJudiciales(gVO,params);
      if (listaPartJudiciales!=null && listaPartJudiciales.size()!=0) {
        gVO = (GeneralValueObject) listaPartJudiciales.firstElement();
      }
      String puedeEliminar = (String) gVO.getAtributo("puedeEliminar");
      if(puedeEliminar != null && !puedeEliminar.equals("")) {
      	mantForm.setOperacion("noPuedeEliminar");
      } else {
        mantForm.setListaPartJudiciales(listaPartJudiciales);
      mantForm.setOperacion("cargarPartJudiciales");
      }
      opcion = "oculto";
    }
    else if("modificar".equals(opcion))
    {
      Vector listaPartJudiciales = new Vector();
      listaPartJudiciales = partJudicialesManager.modificarPartJudiciales(gVO,params);
      mantForm.setListaPartJudiciales(listaPartJudiciales);
      mantForm.setOperacion("cargarPartJudiciales");
      opcion = "oculto";
    }
    else if("alta".equals(opcion))
    {
      Vector listaPartJudiciales = new Vector();
      listaPartJudiciales = partJudicialesManager.altaPartJudiciales(gVO,params);
      mantForm.setListaPartJudiciales(listaPartJudiciales);
      mantForm.setOperacion("cargarPartJudiciales");
      opcion = "oculto";
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
    return (mapping.findForward(opcion));
  }

  private GeneralValueObject recogerParametros(HttpServletRequest request){
    GeneralValueObject gVO = new GeneralValueObject();
    //Recogemos y asignamos al gVO los parametros con los nombres que les damos
    //en la jsp
    gVO.setAtributo("codPais",request.getParameter("codPais"));
    gVO.setAtributo("codProvincia",request.getParameter("codProvincia"));
    gVO.setAtributo("codPartJudicial",request.getParameter("codPartJudicial"));
    gVO.setAtributo("nombre",request.getParameter("nombre"));
    gVO.setAtributo("nombreLargo",request.getParameter("nombreLargo"));
    return gVO;
  }
}