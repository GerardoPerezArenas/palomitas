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
 * <p>Descripción: Clase DistritosAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */


public class DistritosAction extends ActionSession  {
  protected static Log m_Log =
          LogFactory.getLog(DistritosAction.class.getName());
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
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    ParametrosTerceroValueObject paramsTercero = (ParametrosTerceroValueObject)session.getAttribute("parametrosTercero");
    codPais = paramsTercero.getPais();
    codProvincia = paramsTercero.getProvincia();
    codMunicipio = paramsTercero.getMunicipio();
    String[] params = usuario.getParamsCon();
    MantenimientosTercerosForm mantForm = (MantenimientosTercerosForm)form;
    ProvinciasManager provinciasManager = ProvinciasManager.getInstance();
    MunicipiosManager municipiosManager = MunicipiosManager.getInstance();
    DistritosManager distritosManager = DistritosManager.getInstance();
    mantForm.setVentana(request.getParameter("ventana"));
    GeneralValueObject gVO = recogerParametros(request);
    if ("cargar".equalsIgnoreCase(opcion)){
      GeneralValueObject gVO1 = new GeneralValueObject();
      gVO1.setAtributo("codPais",codPais);
      gVO1.setAtributo("codProvincia",codProvincia);
      Vector lista = provinciasManager.getListaProvincias(gVO1,params);
      Vector listaM = municipiosManager.getListaMunicipios(gVO1,params);
      mantForm.setListaProvincias(lista);
      mantForm.setListaMunicipios(listaM);
    }else if (opcion.equals("cargarMunicipios")){
      GeneralValueObject gVO1 = new GeneralValueObject();
      gVO1.setAtributo("codPais",request.getParameter("codPais"));
      gVO1.setAtributo("codProvincia",request.getParameter("codProvincia"));
      Vector muns = new Vector();
      muns = municipiosManager.getListaMunicipios(gVO1,params);
      mantForm.setListaMunicipios(muns);
    }else if(opcion.equals("cargarDistritos")){
      Vector distritos = new Vector();
      distritos = distritosManager.getListaDistritos(gVO,params);
      mantForm.setListaDistritos(distritos);
    }else if("eliminar".equalsIgnoreCase(opcion)){
      Vector distritos = new Vector();
      distritos = distritosManager.eliminarDistrito(gVO,params);
      if(distritos.size() != 0) {
        gVO = (GeneralValueObject) distritos.firstElement();
      }
      String puedeEliminar = (String) gVO.getAtributo("puedeEliminar");
      if(puedeEliminar != null && !puedeEliminar.equals("")) {
      	opcion = "noPuedeEliminar";
      } else {
        mantForm.setListaDistritos(distritos);
        opcion = "cargarDistritos";
      }
    }else if("modificar".equals(opcion)){
      Vector distritos = new Vector();
      distritos = distritosManager.modificarDistrito(gVO,params);
      mantForm.setListaDistritos(distritos);
      opcion = "cargarDistritos";
    }else if("alta".equals(opcion)){
      Vector distritos = new Vector();
      distritos = distritosManager.altaDistrito(gVO,params);
      mantForm.setListaDistritos(distritos);
      opcion = "cargarDistritos";
    }else if("salir".equals(opcion)){
      if ((session.getAttribute(mapping.getAttribute()) != null))
        session.removeAttribute(mapping.getAttribute());
    }else{
      opcion = mapping.getInput();
    }
    return (mapping.findForward(opcion));
  }

  private GeneralValueObject recogerParametros(HttpServletRequest request){
    GeneralValueObject gVO = new GeneralValueObject();
    gVO.setAtributo("codPais",request.getParameter("codPais"));
    gVO.setAtributo("codProvincia",request.getParameter("codProvincia"));
    gVO.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
    gVO.setAtributo("codDistrito",request.getParameter("codDistrito"));
    gVO.setAtributo("codDistritoAntiguo",request.getParameter("codDistritoAntiguo"));
    gVO.setAtributo("descDistrito",request.getParameter("descDistrito"));
    gVO.setAtributo("nombreLargo",request.getParameter("nombreLargo"));
    gVO.setAtributo("idObjetoGrafico",request.getParameter("idObjetoGrafico"));
    return gVO;
  }
}