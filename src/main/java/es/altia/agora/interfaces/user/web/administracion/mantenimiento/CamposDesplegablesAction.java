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
 * <p>Descripción: Clase CampoDesplegablesAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: ALTIA CONSULTORES & AYTOS CPD</p>
 * @author Jorge Hombre Tuñas
 * @version 1.0
 */


public class CamposDesplegablesAction extends ActionSession  {

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
    CamposDesplegablesManager mantManager = CamposDesplegablesManager.getInstance();
    if ("cargar".equalsIgnoreCase(opcion)){
      Vector lista = mantManager.getListaCampoDesplegable(params);
      mantForm.setListaCamposDesplegables(lista);
    }else if("eliminar".equalsIgnoreCase(opcion)){
      String identificador = request.getParameter("identificador");
      Vector lista = new Vector();
      lista = mantManager.eliminarCampoDesplegable(identificador,params);
      mantForm.setListaCamposDesplegables(lista);
      opcion = "vuelveCargar";
    }else if("modificar".equals(opcion)){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codigoAntiguo",request.getParameter("identificador"));
      gVO.setAtributo("codigo",request.getParameter("Codigo"));
      gVO.setAtributo("descripcion",request.getParameter("Descripcion"));
      Vector lista = mantManager.modificarCampoDesplegable(gVO,params);
      mantForm.setListaCamposDesplegables(lista);
      opcion = "vuelveCargar";
    }else if("alta".equals(opcion)){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codigo",request.getParameter("Codigo"));
      gVO.setAtributo("descripcion",request.getParameter("Descripcion"));
      Vector lista = mantManager.altaCampoDesplegable(gVO,params);
      mantForm.setListaCamposDesplegables(lista);
      opcion = "vuelveCargar";
    }else if("valores".equals(opcion)){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("campo",request.getParameter("Codigo"));
      gVO.setAtributo("descCampo",request.getParameter("Descripcion"));
      Vector lista = mantManager.getListaValoresCampoDesplegable(gVO,params);
      mantForm.setListaValoresCamposDesplegables(lista);
      mantForm.setOtrosDatos(gVO);
      opcion = "valores";
    }else if("altaValor".equals(opcion)){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("campo",request.getParameter("campo"));
      gVO.setAtributo("codigo",request.getParameter("Codigo"));
     String descripcion = request.getParameter("Descripcion");
      String descripcionIdiomaA = request.getParameter("descripcionIdiomaA");
      if (descripcionIdiomaA!=null && !descripcionIdiomaA.equals(""))	
          descripcion += "|" + descripcionIdiomaA;	
	
      gVO.setAtributo("descripcion",descripcion);
      Vector lista = mantManager.altaValorCampoDesplegable(gVO,params);
      mantForm.setListaValoresCamposDesplegables(lista);
      opcion = "vuelveCargarValores";
    }else if("modificarValor".equals(opcion)){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codigoAntiguo",request.getParameter("identificador"));
      gVO.setAtributo("campo",request.getParameter("campo"));
      gVO.setAtributo("codigo",request.getParameter("Codigo"));
      String descripcion = request.getParameter("Descripcion");
      String descripcionIdiomaA = request.getParameter("descripcionIdiomaA");	
      if (descripcionIdiomaA!=null && !descripcionIdiomaA.equals(""))	
          descripcion += "|" + descripcionIdiomaA;	
	
      gVO.setAtributo("descripcion",descripcion);
      Vector lista = mantManager.modificarValorCampoDesplegable(gVO,params);
      mantForm.setListaValoresCamposDesplegables(lista);
      opcion = "vuelveCargarValores";
    }else if("recuperarValor".equalsIgnoreCase(opcion)){
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("campo", request.getParameter("campo"));
        gVO.setAtributo("codigo", request.getParameter("Codigo"));
        Vector lista=mantManager.recuperarValorCampoDesplegable(gVO,params);
        mantForm.setListaValoresCamposDesplegables(lista);
        opcion="vuelveCargarValores";
    }else if("eliminarValor".equalsIgnoreCase(opcion)){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("campo",request.getParameter("campo"));
      gVO.setAtributo("codigo",request.getParameter("Codigo"));
      Vector lista = new Vector();
      lista = mantManager.eliminarValorCampoDesplegable(gVO,params);
      mantForm.setListaValoresCamposDesplegables(lista);
      opcion = "vuelveCargarValores";
    }else if("salir".equals(opcion)){
      if ((session.getAttribute(mapping.getAttribute()) != null))
        session.removeAttribute(mapping.getAttribute());
    } else if("comprobarEliminacionCampo".equals(opcion)){
        
            String identificador = request.getParameter("identificador");
            int salida = mantManager.comprobarEliminacionCampoDesplegable(identificador, params);
            m_Log.debug("salida:: " + salida);
            
            //  0 -> Se puede eliminar el campo desplegable externo
            //  1 -> El campo está asignado como campo a nivel de procedimiento
            //  2 -> El campo está asignado como campo a nivel de trámite
            // -1 -> No se ha podido obtener una conexión a la BBDD
            // -2 -> Se ha producido un error técnico al eliminar el desplegable                          
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println(salida);         
    }    
    else{
      opcion = mapping.getInput();
    }
    m_Log.debug("perform");
    return (mapping.findForward(opcion));
  }
}