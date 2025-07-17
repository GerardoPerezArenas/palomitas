// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.administracion.mantenimiento;

// PAQUETES IMPORTADOS
import es.altia.agora.business.escritorio.*;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.helper.*;
import es.altia.agora.business.administracion.mantenimiento.persistence.AplicacionesManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.IdiomasManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.ProcesosManager;
import es.altia.agora.business.util.*;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase ProcesosAction</p>
 * <p>Empresa: ALTIA CONSULTORES</p>
 */


public class ProcesosAction extends ActionSession  {

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
    
    ProcesosManager mantManager = ProcesosManager.getInstance(); 
    GeneralValueObject gVO = recogerParametros(request);
    if ("cargar".equalsIgnoreCase(opcion)){
      Vector lista = AplicacionesManager.getInstance().getListaAplicaciones(params);
      mantForm.setListaAplicaciones(lista);
      lista = IdiomasManager.getInstance().getListaIdiomas(params);
      mantForm.setListaIdiomas(lista);
    }else if ("cargarProcesos".equalsIgnoreCase(opcion)){
      Vector lista = mantManager.getListaProcesos(gVO,params);
      mantForm.setListaProcesos(lista);
      opcion = "vuelveCargar";
    }else if("eliminar".equalsIgnoreCase(opcion)){
      Vector lista = new Vector();
      lista = mantManager.eliminarProceso(gVO,params);
      mantForm.setListaProcesos(lista);
      opcion = "vuelveCargar";
    }else if("modificar".equals(opcion)){
      Vector lista = mantManager.modificarProceso(gVO,params);
      mantForm.setListaProcesos(lista);
      opcion = "vuelveCargar";
    }else if("alta".equals(opcion)){
      Vector lista = mantManager.altaProceso(gVO,params);
      mantForm.setListaProcesos(lista);
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
    gVO.setAtributo("codAplicacion",request.getParameter("codAplicacion"));
    gVO.setAtributo("codProceso",request.getParameter("codProceso"));
    gVO.setAtributo("descProceso",request.getParameter("descProceso"));
    gVO.setAtributo("formulario",request.getParameter("formulario"));
    String cadenaID = request.getParameter("idiomas_descripciones");
    Vector listaID = new Vector();
    if (cadenaID != null)
    	if (!"".equals(cadenaID)) listaID = construirIdiomasDescripciones(cadenaID);
    gVO.setAtributo("idiomasDescripciones", listaID);    
    gVO.setAtributo("codEntidadAntiguo",request.getParameter("codEntidadAntiguo"));
    return gVO;
  }
  
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
         	if (m_Log.isDebugEnabled()) m_Log.debug("descripcion -->"+ idioma + " -- " + descripcion);
         }
      }
   }
   return r;
}

}