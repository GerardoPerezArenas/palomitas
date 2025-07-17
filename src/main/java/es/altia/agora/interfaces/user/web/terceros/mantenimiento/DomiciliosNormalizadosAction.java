// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.terceros.mantenimiento;

// PAQUETES IMPORTADOS
import es.altia.agora.business.escritorio.*;

import es.altia.agora.business.terceros.*;
import es.altia.agora.business.terceros.mantenimiento.persistence.*;
import es.altia.agora.business.util.*;
import es.altia.agora.interfaces.user.web.util.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import java.io.*;

import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase DomiciliosNormalizadosAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */


public class DomiciliosNormalizadosAction extends ActionSession {
  protected static Log m_Log =
          LogFactory.getLog(DomiciliosNormalizadosAction.class.getName());
  String codPais = "";
  String codProvincia = "";
  String codMunicipio = "";

  public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
    m_Log.debug("perform");
    HttpSession session = request.getSession();

    // Validaremos los parametros del request especificados
    ActionErrors errors = new ActionErrors();
    String opcion = request.getParameter("opcion");
    if (m_Log.isInfoEnabled()) m_Log.info("la opcion en el action es " + opcion);

    // Rellenamos el form de Padron
    if (form == null) {
      m_Log.debug("Rellenamos el form de Padron");
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
    int mostrarFamilia = Integer.parseInt((String)paramsTercero.getFamiliasPadron());
    String[] params = usuario.getParamsCon();
    MantenimientosTercerosForm mantForm = (MantenimientosTercerosForm) form;
    PaisesManager paisesManager = PaisesManager.getInstance();
    ProvinciasManager provinciasManager = ProvinciasManager.getInstance();
    MunicipiosManager municipiosManager = MunicipiosManager.getInstance();
    TiposViasManager tipoViasManager = TiposViasManager.getInstance();
    EcosManager ecosManager = EcosManager.getInstance();
    EntidadesSingularesManager esisManager = EntidadesSingularesManager.getInstance();
    NucleosManager nucleosManager = NucleosManager.getInstance();
    ViasManager viasManager = ViasManager.getInstance();
    CodPostalesManager codPostalesManager = CodPostalesManager.getInstance();
    PlantasManager plantasManager = PlantasManager.getInstance();
    EscalerasManager escalerasManager = EscalerasManager.getInstance();
    TiposNumeracionManager tipoNumeracionManager = TiposNumeracionManager.getInstance();
    TiposViviendaManager tiposViviendaManager = TiposViviendaManager.getInstance();
    TramerosManager tramosManager = TramerosManager.getInstance();
    DomiciliosManager domiciliosManager = DomiciliosManager.getInstance();
    mantForm.setOpcion(opcion);
    //mantForm.setOperacion(request.getParameter("operacion"));
    if(opcion.equals("inicializar")){
      Vector listaTVias = new Vector();
      Vector listaECOs = new Vector();
      Vector listaESIs = new Vector();
      Vector listaNUCs = new Vector();
      Vector listaVias = new Vector();
      Vector listaPlantas = new Vector();
      Vector listaEscaleras = new Vector();
      Vector listaCodPostales = new Vector();
      Vector listaTipoNumeracion = new Vector();
      Vector listaTipoOcupacion = new Vector();
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codPais",codPais);
      gVO.setAtributo("codProvincia",codProvincia);
      gVO.setAtributo("codMunicipio",codMunicipio);
      gVO.setAtributo("codECO","");
      gVO.setAtributo("codEntidadColectiva","");
      gVO.setAtributo("codESI","");
      gVO.setAtributo("codNUC","");
      gVO.setAtributo("numDesde","");
      gVO.setAtributo("tipoNumeracion","");
      listaTVias = tipoViasManager.getListaTiposVias(params);
      listaECOs = ecosManager.getListaEcos(gVO,params);
      listaESIs = esisManager.getListaEntidadesSingulares(gVO,params);
      listaNUCs = nucleosManager.getListaNucleos(gVO,params);
      listaVias = viasManager.getListaVias(params,gVO);
      listaEscaleras = escalerasManager.getListaEscaleras(params);
      listaPlantas = plantasManager.getListaPlantas(params);
      listaCodPostales = codPostalesManager.getListaCodPostales(params,gVO);
      listaTipoNumeracion = tipoNumeracionManager.getListaTiposNumeraciones(params);
      listaTipoOcupacion = tiposViviendaManager.getListaTiposVivienda(params);
      mantForm.setListaTipoVias(listaTVias);
      mantForm.setListaEcos(listaECOs);
      mantForm.setListaEsis(listaESIs);
      mantForm.setListaNucleos(listaNUCs);
      mantForm.setListaVias(listaVias);
      mantForm.setListaEscaleras(listaEscaleras);
      mantForm.setListaPlantas(listaPlantas);
      mantForm.setListaCodPostales(listaCodPostales);
      mantForm.setListaNumeraciones(listaTipoNumeracion);
      mantForm.setListaUsoViviendas(listaTipoOcupacion);
      mantForm.setOperacion(request.getParameter("operacion"));
    }else if(opcion.equals("cargarListas")){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codPais",codPais);
      gVO.setAtributo("codProvincia",codProvincia);
      gVO.setAtributo("codMunicipio",codMunicipio);
      gVO.setAtributo("codECO",request.getParameter("codECO"));
      gVO.setAtributo("codEntidadColectiva",request.getParameter("codECO"));
      gVO.setAtributo("codESI",request.getParameter("codESI"));
      gVO.setAtributo("codNUC",request.getParameter("codNUC"));
      gVO.setAtributo("numDesde","");
      gVO.setAtributo("tipoNumeracion","");
      Vector esis = new Vector();
      Vector nucleos = new Vector();
      Vector vias = new Vector();
      esis = esisManager.getListaEntidadesSingulares(gVO,params);
      nucleos = nucleosManager.getListaNucleos(gVO,params);
      vias = viasManager.getListaVias(params,gVO);
      mantForm.setListaEsis(esis);
      mantForm.setListaNucleos(nucleos);
      mantForm.setListaVias(vias);
      opcion="oculto";
    }else if(opcion.equals("cargarVias")){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codPais",request.getParameter("codPais"));
      gVO.setAtributo("codProvincia",request.getParameter("codProvincia"));
      gVO.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
      gVO.setAtributo("codECO",request.getParameter("codECO"));
      gVO.setAtributo("codESI",request.getParameter("codESI"));
      gVO.setAtributo("codNUC",request.getParameter("codNUC"));
      gVO.setAtributo("numDesde","");
      gVO.setAtributo("tipoNumeracion","");
      Vector vias = new Vector();
      vias = viasManager.getListaVias(params,gVO);
      mantForm.setListaVias(vias);
      opcion="oculto";
    }else if(opcion.equals("cargarDomicilios")){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codPais",request.getParameter("codPais"));
      gVO.setAtributo("codProvincia",request.getParameter("codProvincia"));
      gVO.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
      gVO.setAtributo("codECO",request.getParameter("codECO"));
      gVO.setAtributo("codESI",request.getParameter("codESI"));
      gVO.setAtributo("codNUC",request.getParameter("codNUC"));
      gVO.setAtributo("idVia",request.getParameter("idVia"));
      gVO.setAtributo("codTipoNumeracion","");
      Vector domicilios = domiciliosManager.getListaDomicilios(gVO,params);
      Vector tramos = tramosManager.getListaTrameros(gVO,params);
      mantForm.setListaDomicilios(domicilios);
      mantForm.setListaTrameros(tramos);
      opcion="oculto";
    }else if("eliminar".equalsIgnoreCase(opcion)){
      GeneralValueObject gVO = recogerParametrosEliminarDomicilio(request);
      gVO.setAtributo("usuario",String.valueOf(usuario.getIdUsuario()));
      GeneralValueObject resultado = domiciliosManager.eliminarDomicilio(gVO,params);
      mantForm.setOperacion((String)resultado.getAtributo("operacion"));
      mantForm.setListaDomicilios((Vector)resultado.getAtributo("domicilios"));
      opcion="oculto";
    }else if("modificar".equals(opcion)){
      GeneralValueObject gVO = recogerParametrosInsertarModificarDomicilio(request);
      gVO.setAtributo("usuario",String.valueOf(usuario.getIdUsuario()));
      GeneralValueObject resultado = domiciliosManager.modificarDomicilio(gVO,params);
      mantForm.setOperacion((String)resultado.getAtributo("operacion"));
      mantForm.setListaDomicilios((Vector)resultado.getAtributo("domicilios"));
      opcion="oculto";
    }else if("alta".equals(opcion)){
      GeneralValueObject gVO = recogerParametrosInsertarModificarDomicilio(request);
      gVO.setAtributo("usuario",String.valueOf(usuario.getIdUsuario()));
      GeneralValueObject resultado = domiciliosManager.altaDomicilio(gVO,params);
      mantForm.setOperacion((String)resultado.getAtributo("operacion"));
      mantForm.setListaDomicilios((Vector)resultado.getAtributo("domicilios"));
      opcion="oculto";
    }else if("salir".equals(opcion)){
      if ((session.getAttribute(mapping.getAttribute()) != null))
        session.removeAttribute(mapping.getAttribute());
    }else{
      opcion = mapping.getInput();
    }
    /* Redirigimos al JSP de salida*/
    return (mapping.findForward(opcion));
  }

  public GeneralValueObject recogerParametrosInsertarModificarDomicilio(HttpServletRequest request){
    GeneralValueObject gVO = new GeneralValueObject();
    gVO.setAtributo("codPais",codPais);
    gVO.setAtributo("codProvincia",codProvincia);
    gVO.setAtributo("codMunicipio",codMunicipio);
    gVO.setAtributo("codECO",request.getParameter("codECO"));
    gVO.setAtributo("codESI",request.getParameter("codESI"));
    gVO.setAtributo("codNUC",request.getParameter("codNUC"));
    gVO.setAtributo("idVia",request.getParameter("idVia"));
    gVO.setAtributo("codTipoNumeracion",request.getParameter("codTipoNumeracion"));
    gVO.setAtributo("numDesde",request.getParameter("numDesde"));
    gVO.setAtributo("letraDesde",request.getParameter("letraDesde"));
    gVO.setAtributo("numHasta",request.getParameter("numHasta"));
    gVO.setAtributo("letraHasta",request.getParameter("letraHasta"));
    gVO.setAtributo("bloque",request.getParameter("bloque"));
    gVO.setAtributo("portal",request.getParameter("portal"));
    gVO.setAtributo("escalera",request.getParameter("codEscalera"));
    gVO.setAtributo("planta",request.getParameter("codPlanta"));
    gVO.setAtributo("puerta",request.getParameter("puerta"));
    gVO.setAtributo("km",request.getParameter("km"));
    gVO.setAtributo("hm",request.getParameter("hm"));
    gVO.setAtributo("observaciones",request.getParameter("observaciones"));
    gVO.setAtributo("codigoPostal",request.getParameter("descPostal"));
    gVO.setAtributo("codTipoVivienda",request.getParameter("codTipoVivienda"));
    gVO.setAtributo("haCambiadoDistrito",request.getParameter("haCambiadoDistrito"));
    gVO.setAtributo("distrito",request.getParameter("distrito"));
    gVO.setAtributo("seccion",request.getParameter("seccion"));
    gVO.setAtributo("letra",request.getParameter("letra"));
    gVO.setAtributo("codTramo",request.getParameter("codTramo"));
    gVO.setAtributo("codDPO",request.getParameter("codDPO"));
    gVO.setAtributo("codDSU",request.getParameter("codDSU"));
    return gVO;
  }

  public GeneralValueObject recogerParametrosEliminarDomicilio(HttpServletRequest request){
    GeneralValueObject gVO = new GeneralValueObject();
    gVO.setAtributo("codPais",codPais);
    gVO.setAtributo("codProvincia",codProvincia);
    gVO.setAtributo("codMunicipio",codMunicipio);
    gVO.setAtributo("codECO",request.getParameter("codECO"));
    gVO.setAtributo("codESI",request.getParameter("codESI"));
    gVO.setAtributo("codNUC",request.getParameter("codNUC"));
    gVO.setAtributo("idVia",request.getParameter("idVia"));
    gVO.setAtributo("codDPO",request.getParameter("codDPO"));
    gVO.setAtributo("codDSU",request.getParameter("codDSU"));
    gVO.setAtributo("tieneTerceros",request.getParameter("tieneTerceros"));
    return gVO;
  }

}