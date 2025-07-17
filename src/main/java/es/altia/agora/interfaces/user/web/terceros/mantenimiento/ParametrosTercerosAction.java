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
 * <p>Descripción: Clase ParametrosTercerosAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */


public class ParametrosTercerosAction extends ActionSession  {
  protected static Log m_Log =
          LogFactory.getLog(ParametrosTercerosAction.class.getName());
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
    PaisesManager paisesManager = PaisesManager.getInstance();
    ProvinciasManager provinciasManager = ProvinciasManager.getInstance();
    MunicipiosManager municipiosManager = MunicipiosManager.getInstance();
    TipoDocumentosManager tipDocsManager = TipoDocumentosManager.getInstance();
    TipoOcupacionManager tipoOcupacionManager = TipoOcupacionManager.getInstance();
    ParametrosTercerosManager parametrosTercerosManager = ParametrosTercerosManager.getInstance();
    mantForm.setVentana(request.getParameter("ventana"));
    if ("cargar".equalsIgnoreCase(opcion)){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codPais",codPais);
      gVO.setAtributo("codProvincia",codProvincia);
      Vector listaPaises = paisesManager.getListaPaises(params);
      Vector listaProvs = provinciasManager.getListaProvincias(gVO,params);
      Vector listaMuns = municipiosManager.getListaMunicipios(gVO,params);
      Vector listaTipoDocs = tipDocsManager.getListaTipoDocumentos(params);
      Vector listaTipoOcup = tipoOcupacionManager.getListaTipoOcupaciones(params);
      ParametrosTerceroValueObject parametros = parametrosTercerosManager.getParametrosTerceros(usuario.getOrgCod(), params);
      mantForm.setListaPaises(listaPaises);
      mantForm.setListaProvincias(listaProvs);
      mantForm.setListaMunicipios(listaMuns);
      mantForm.setListaTipoDocs(listaTipoDocs);
      mantForm.setListaUsoViviendas(listaTipoOcup);
      mantForm.setParametrosTerceros(parametros);
    }else if (opcion.equals("cargarProvincias")){
      Vector provs = new Vector();
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codPais",request.getParameter("codPais"));
      provs = provinciasManager.getListaProvincias(gVO,params);
      mantForm.setListaProvincias(provs);
    }else if (opcion.equals("cargarMunicipios")){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codPais",request.getParameter("codPais"));
      gVO.setAtributo("codProvincia",request.getParameter("codProvincia"));
      Vector muns = new Vector();
      muns = municipiosManager.getListaMunicipios(gVO,params);
      mantForm.setListaMunicipios(muns);
    }else if("modificar".equals(opcion)){
      ParametrosTerceroValueObject ptVO = recogerParametros(request,usuario);
      parametrosTercerosManager.modificarParametrosTerceros(ptVO,params);
      //Además de modificar los parámetros por defecto, ponérselos tb en la sesión del usuario
      ParametrosTerceroValueObject pt = parametrosTercerosManager.getParametrosTerceros(usuario.getOrgCod(), usuario.getParamsCon());
      if(session.getAttribute("parametrosTercero") != null) session.removeAttribute("parametrosTercero");
      session.setAttribute("parametrosTercero",pt);
    }else if("salir".equals(opcion)){
      if ((session.getAttribute(mapping.getAttribute()) != null))
        session.removeAttribute(mapping.getAttribute());
    }else{
      opcion = mapping.getInput();
    }

    return (mapping.findForward(opcion));
  }

  private ParametrosTerceroValueObject recogerParametros(HttpServletRequest request,UsuarioValueObject usuario){
    String pais = request.getParameter("codPais");
    String provincia = request.getParameter("codProvincia");
    String municipio = request.getParameter("codMunicipio");
    String tipoDoc = request.getParameter("codTipoDoc");
    String tipoOcupacion = request.getParameter("codTipoOcupacion");
    String tipoOcupacionPr = request.getParameter("codTipoOcupacionPr");
    String idMultiple = request.getParameter("identificMultiples");
    String lugar = request.getParameter("tratamientoLugar");

    ParametrosTerceroValueObject ptVO = new ParametrosTerceroValueObject();
    ptVO.setPais(pais);
    if(provincia!=null)
      ptVO.setPaisProvincia(pais);
    else
      ptVO.setPaisProvincia("");
    ptVO.setProvincia(provincia);
    if(municipio!=null){
      ptVO.setPaisMunicipio(pais);
      ptVO.setProvinciaMunicipio(provincia);
    }else{
      ptVO.setPaisMunicipio("");
      ptVO.setProvinciaMunicipio("");
    }
    ptVO.setMunicipio(municipio);
    ptVO.setTipoDocumento(tipoDoc);
    ptVO.setTipoOcupacion(tipoOcupacion);
    ptVO.setTipoOcupacionPrincipal(tipoOcupacionPr);
    idMultiple = (idMultiple!=null)?idMultiple:"0";
    ptVO.setIdentificadorMultiple(idMultiple);
    ptVO.setLugar(lugar);
    ptVO.setUsuario(String.valueOf(usuario.getIdUsuario()));
    return ptVO;
  }
}