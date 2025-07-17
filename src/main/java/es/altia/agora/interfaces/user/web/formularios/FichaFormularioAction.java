// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.formularios;

// PAQUETES IMPORTADOS
import es.altia.agora.business.escritorio.*;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.helper.*;
import es.altia.agora.business.formularios.persistence.FichaFormularioManager;
import es.altia.agora.business.formularios.persistence.FormulariosManager;
import es.altia.agora.business.formularios.mantenimiento.persistence.TiposManager;
import es.altia.agora.business.util.*;
import es.altia.agora.business.sge.persistence.TramitacionManager;
import es.altia.agora.business.sge.persistence.DefinicionTramitesManager;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.DefinicionTramitesValueObject;
import es.altia.agora.business.administracion.mantenimiento.persistence.UORsManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.CargosManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.UsuariosGruposManager;

import es.altia.arboles.impl.ArbolImpl;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;


/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase ProcedimientosAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: ALTIA CONSULTORES & AYTOS CPD</p>
 * @author Jorge Hombre Tuñas
 * @version 1.0
 */


public class FichaFormularioAction extends ActionSession  {

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

    if (form == null) {
      m_Log.debug("Rellenamos el form de formularios");
      form = new FichaFormularioForm();
      if ("request".equals(mapping.getScope())){
        request.setAttribute(mapping.getAttribute(), form);
      }else{
        session.setAttribute(mapping.getAttribute(), form);
      }
    }
    FichaFormularioForm fichaForm = (FichaFormularioForm)form;
    FichaFormularioManager formManager = FichaFormularioManager.getInstance();

    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    int codOrg = usuario.getOrgCod();
    int codEnt = usuario.getEntCod();
    String cOrg = Integer.toString(codOrg);
    String cEnt = Integer.toString(codEnt);
    String[] params = usuario.getParamsCon();
    FormulariosForm formForm = new FormulariosForm();
    
    
    if("alta".equals(opcion)){
    	Vector listaPrecargas = FichaFormularioManager.getInstance().getListaPrecargas(cOrg);
        Vector listaTipos = TiposManager.getInstance().getListaTipos(usuario.getParamsCon());
        Vector listaProcedimientos = TramitacionManager.getInstance().getListaProcedimientos(usuario ,usuario.getParamsCon());
        Vector listaAreas = DefinicionProcedimientosManager.getInstance().getListaArea(params);
        Vector listaAreasPorDefecto = FichaFormularioManager.getInstance().cargarAreasPorDefecto(params);
        Vector listaFormulariosTipo0 = FormulariosManager.getInstance().getListaFormulariosTipo0(usuario.getParamsCon());

        //===============================
        // seleccionar el jndi para el esquema apropiado
        String jndi = UsuariosGruposManager.getInstance().obtenerJNDI(
                cEnt, cOrg, String.valueOf(usuario.getAppCod()), params);
        String[] paramsNuevos = new String[7];
        paramsNuevos[0] = params[0];
        paramsNuevos[6] = jndi;
        Vector listaUORs = UORsManager.getInstance().getListaUORs(false,paramsNuevos);
        ArbolImpl arbolUORs = UORsManager.getInstance().getArbolUORs(false,false,false, paramsNuevos);
        Vector listaCargos = CargosManager.getInstance().getListaUORs(paramsNuevos);
        ArbolImpl arbolCargos = CargosManager.getInstance().getArbolUORs(false, paramsNuevos);

         // Lista de unidades de registro
        Vector listaUnidadesRegistroOrigen = UORsManager.getInstance().getListaUORsDeRegistro(params);
        Vector listaUnidadesRegistroDestino = UORsManager.getInstance().getListaUORsDeAlta(params);

        fichaForm.setListaTipos(listaTipos);
        fichaForm.setListaProcedimientos(listaProcedimientos);
        fichaForm.setListaAreas(listaAreas);
        fichaForm.setListaAreasPorDefecto(listaAreasPorDefecto);
        fichaForm.setListaFormulariosTipo0(listaFormulariosTipo0);
        fichaForm.setListaUORs(listaUORs);
        fichaForm.setArbolUORs(arbolUORs);
        fichaForm.setListaCargos(listaCargos);
        fichaForm.setArbolCargos(arbolCargos);
        fichaForm.setListaUORSRegistroOrigen(listaUnidadesRegistroOrigen);
        fichaForm.setListaUORSRegistroDestino(listaUnidadesRegistroDestino);

        fichaForm.setListaPrecargas(listaPrecargas);



        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("validacionFicheroEdicion", "nulo");
        gVO.setAtributo("validacionFicheroImpresion", "nulo");
        gVO.setAtributo("codigo", "");
        gVO.setAtributo("nombre", "");
        gVO.setAtributo("codVisible", "");
        gVO.setAtributo("version", "1");
        gVO.setAtributo("fAlta", "");
        gVO.setAtributo("fBaja", "");
        gVO.setAtributo("tipo", "");
        gVO.setAtributo("codRel", "");
        gVO.setAtributo("procedimiento", "");
        gVO.setAtributo("area", "");
        gVO.setAtributo("instruc", "");
        gVO.setAtributo("dTramite", "");
        gVO.setAtributo("gTramite", "");
        gVO.setAtributo("cerrarT1", "");
        gVO.setAtributo("instancia", "");
        gVO.setAtributo("registro", "");
        gVO.setAtributo("unidades", "");
        gVO.setAtributo("visible", "");
        gVO.setAtributo("tipoFirma", "");
        gVO.setAtributo("dmte", "");
        gVO.setAtributo("tipoRestriccionFormulario", "0");
        fichaForm.setListaRestricciones(new Vector());
        fichaForm.setPrecargasSeleccionadas(new Vector());
        fichaForm.setListaTramites(new Vector());
        fichaForm.setFormularioVO(gVO);

        opcion="alta";
    } else if("grabar".equals(opcion)){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("municipio",String.valueOf(usuario.getOrgCod()));
      m_Log.debug("______________________________MUNICIPIO " + gVO.getAtributo("municipio"));
      gVO.setAtributo("formCod",request.getParameter("formCod"));
      gVO.setAtributo("formNombre",request.getParameter("formNombre"));
      gVO.setAtributo("formVersion",request.getParameter("formVersion"));
      gVO.setAtributo("codTipo",request.getParameter("codTipo")); 
      gVO.setAtributo("codigoInterno", request.getParameter("codigoInterno"));
      if (request.getParameter("codFormRel") == null) {
          gVO.setAtributo("codFormRel","0");
      } else {
      gVO.setAtributo("codFormRel",request.getParameter("codFormRel"));
      }
      m_Log.debug("______________________________COD_FORM_REL --" + gVO.getAtributo("codigoInterno")+"--");
      gVO.setAtributo("fechaAlta",request.getParameter("fechaAlta"));
      gVO.setAtributo("fechaBaja",request.getParameter("fechaBaja"));
      String accesible = request.getParameter("accesible");
      if(accesible == null) {
          gVO.setAtributo("accesible","0");
        } else gVO.setAtributo("accesible","1");
      gVO.setAtributo("codTipoDemandante",request.getParameter("codTipoDemandante"));
      gVO.setAtributo("codTipoFirma",request.getParameter("codTipoFirma"));
      m_Log.debug("______________________________COD_TIPO FIRMA --" + gVO.getAtributo("codTipoFirma")+"--");
      if (request.getParameter("codProcedimiento") == null) {
          gVO.setAtributo("codProcedimiento","0");
      } else {
      gVO.setAtributo("codProcedimiento",request.getParameter("codProcedimiento"));
      }
            gVO.setAtributo("codArea",request.getParameter("codArea"));

      m_Log.debug("______________________________COD_PROCEDIMIENTO --" + gVO.getAtributo("codProcedimiento")+"--");


      String gRegistro = request.getParameter("gRegistro");
      // Si se genera un registro, entonces debemos seleccionar las unidades de registro por defecto
      if(gRegistro == null) {
          gVO.setAtributo("gRegistro","0");
      } else{
          gVO.setAtributo("gRegistro","1");
          // obtenemos las unidades de registro de origen y destino
          gVO.setAtributo("codUnidadRegOrigen",request.getParameter("UorRegOrigen"));
          gVO.setAtributo("codUnidadRegDestino",request.getParameter("UorRegDestino"));
      }


      String gInstancia = request.getParameter("gInstancia");
      if(gInstancia == null) {
          gVO.setAtributo("gInstancia","0");
        } else gVO.setAtributo("gInstancia","1");
      String gTramite = request.getParameter("gTramite");
      if(gTramite == null) {
          gVO.setAtributo("gTramite","0");
        } else gVO.setAtributo("gTramite","1");
      String dTramite = request.getParameter("dTramite");
      if(dTramite == null) {
          gVO.setAtributo("dTramite","0");
        } else gVO.setAtributo("dTramite","1");
      String cerrarT1 = request.getParameter("cerrarT1");
      if(cerrarT1 == null) {
          gVO.setAtributo("cerrarT1","0");
      } else gVO.setAtributo("cerrarT1","1");
      String visible = request.getParameter("visible");
      if(visible == null) {
          gVO.setAtributo("visible","0");
        } else gVO.setAtributo("visible","1");
      gVO.setAtributo("instrucciones",request.getParameter("instrucciones"));

      FormFile formFichero = fichaForm.getFichero();
      if (formFichero.getFileSize() != 0) {
          m_Log.debug("Fichero1 Cargado CORRECTAMENTE"+formFichero.getFileData());
          gVO.setAtributo("fichero",formFichero);
      } else {
          m_Log.debug("Fichero1 NO Cargado CORRECTAMENTE");
          gVO.setAtributo("fichero",null);
      }

      FormFile formFichero2 = fichaForm.getFichero2();
      if (formFichero2.getFileSize() != 0) {
          m_Log.debug("Fichero2 Cargado CORRECTAMENTE "+formFichero2.getFileData());
          gVO.setAtributo("fichero2",formFichero2);
      } else {
          m_Log.debug("Fichero2 NO Cargado CORRECTAMENTE");
          gVO.setAtributo("fichero2",null);
      }

      //TRÁMITES
        gVO.setAtributo("listaTramProc",listaTemasSeleccionados(request.getParameter("listaTramProc")));
        gVO.setAtributo("listaTramCod",listaTemasSeleccionados(request.getParameter("listaTramCod")));
        gVO.setAtributo("listaTramRel",listaTemasSeleccionados(request.getParameter("listaTramRel")));
        gVO.setAtributo("listaTramEst",listaTemasSeleccionados(request.getParameter("listaTramEst")));
      //TRÁMITES
      m_Log.debug("TRAMITES");
      m_Log.debug(listaTemasSeleccionados(request.getParameter("listaTramProc")));
      m_Log.debug(listaTemasSeleccionados(request.getParameter("listaTramCod")));
      m_Log.debug(listaTemasSeleccionados(request.getParameter("listaTramRel")));
      m_Log.debug(listaTemasSeleccionados(request.getParameter("listaTramEst")));
    //RESTRICCIONES
      gVO.setAtributo("listaRestriccionesUOR",listaTemasSeleccionados(request.getParameter("listaRestriccionesUOR")));
      gVO.setAtributo("listaRestriccionesCargo",listaTemasSeleccionados(request.getParameter("listaRestriccionesCargo")));
    //RESTRICCIONES
    m_Log.debug("RESTRICCIONES");
    m_Log.debug(listaTemasSeleccionados(request.getParameter("listaRestriccionesUOR")));
    m_Log.debug(listaTemasSeleccionados(request.getParameter("listaRestriccionesCargo")));


    // Tipo de restriccion del formulario
    gVO.setAtributo("tipoRestriccionFormulario", request.getParameter("tipoRestriccionForm"));
    m_Log.debug("\n tipoRestriccionFormulario = " + gVO.getAtributo("tipoRestriccionFormulario"));
    
      Vector lista = formManager.altaFormulario(gVO,params);
      String codigoForm = FichaFormularioManager.getInstance().getCodNoVisible(request.getParameter("formCod"),params);
      Vector precargasSeleccionadas=listaTemasSeleccionados(request.getParameter("listaPrecargasXml"));
      FichaFormularioManager.getInstance().setPrecargasFormulario(codigoForm,precargasSeleccionadas,params);


      m_Log.debug("Insertado CORRECTAMENTE");
      formForm.setListaFormularios(lista);
      opcion = "inicio";
    }else if("cargar".equals(opcion)){
        String codFormAntiguo = request.getParameter("formCod");
        GeneralValueObject gVO;
        gVO=formManager.cargarFormulario(codFormAntiguo,params, String.valueOf(codOrg));
        gVO.setAtributo("validacionFicheroEdicion", formManager.validarVisualizacionFichero(codFormAntiguo, "p1", params,String.valueOf(codOrg)));
        gVO.setAtributo("validacionFicheroImpresion", formManager.validarVisualizacionFichero(codFormAntiguo, "p2", params,String.valueOf(codOrg)));
        fichaForm.setFormularioVO(gVO);
        m_Log.debug ("------------" + formManager.validarVisualizacionFichero(codFormAntiguo, "p1", params, String.valueOf(codOrg)));
        m_Log.debug ("------------" + formManager.validarVisualizacionFichero(codFormAntiguo, "p2", params, String.valueOf(codOrg)));
        
        Vector listaTipos = TiposManager.getInstance().getListaTipos(usuario.getParamsCon());
        Vector listaProcedimientos = TramitacionManager.getInstance().getListaProcedimientos(usuario ,usuario.getParamsCon());
        Vector listaAreas = DefinicionProcedimientosManager.getInstance().getListaArea(params);
        Vector listaAreasPorDefecto = FichaFormularioManager.getInstance().cargarAreasPorDefecto(params);
        Vector listaFormulariosTipo0 = FormulariosManager.getInstance().getListaFormulariosTipo0(usuario.getParamsCon());
    	Vector listaPre = FichaFormularioManager.getInstance().getListaPrecargas();
    	Vector precargasSeleccionadas=FichaFormularioManager.getInstance().getPrecargasFormulario(codFormAntiguo, params);

       
        DefinicionTramitesValueObject dTVO = new DefinicionTramitesValueObject();
        dTVO.setTxtCodigo((String)gVO.getAtributo("procedimiento"));
        dTVO.setCodMunicipio(String.valueOf(usuario.getOrgCod()));
        dTVO.setNumeroTramite("");
        Vector listaTramitesProcedimiento = DefinicionTramitesManager.getInstance().getListaTramites(dTVO,usuario.getParamsCon());

        // seleccionar el jndi para el esquema apropiado
        String jndi = UsuariosGruposManager.getInstance().obtenerJNDI(
                cEnt, cOrg, String.valueOf(usuario.getAppCod()), params);
        String[] paramsNuevos = new String[7];
        paramsNuevos[0] = params[0];
        paramsNuevos[6] = jndi;
        Vector listaUORs = UORsManager.getInstance().getListaUORs(false,paramsNuevos);
        ArbolImpl arbolUORs = UORsManager.getInstance().getArbolUORs(false,false,false, paramsNuevos);
        Vector listaCargos = CargosManager.getInstance().getListaUORs(paramsNuevos);
        ArbolImpl arbolCargos = CargosManager.getInstance().getArbolUORs(false, paramsNuevos);

        // Lista de unidades de registro
        Vector listaUnidadesRegistroOrigen = UORsManager.getInstance().getListaUORsDeRegistro(params);
        Vector listaUnidadesRegistroDestino = UORsManager.getInstance().getListaUORsDeAlta(params);

        fichaForm.setPrecargasSeleccionadas(precargasSeleccionadas);
        fichaForm.setListaPrecargas(listaPre);
        fichaForm.setListaTipos(listaTipos);
        fichaForm.setListaProcedimientos(listaProcedimientos);
        fichaForm.setListaAreas(listaAreas);
        fichaForm.setListaAreasPorDefecto(listaAreasPorDefecto);
        fichaForm.setListaFormulariosTipo0(listaFormulariosTipo0);
        fichaForm.setListaUORs(listaUORs);
        fichaForm.setArbolUORs(arbolUORs);
        fichaForm.setListaCargos(listaCargos);
        fichaForm.setArbolCargos(arbolCargos);

        fichaForm.setListaUORSRegistroOrigen(listaUnidadesRegistroOrigen);
        fichaForm.setListaUORSRegistroDestino(listaUnidadesRegistroDestino);

        fichaForm.setListaTramites(formManager.cargarTramitesFormulario(codFormAntiguo,usuario.getOrgCod(),params));        
        m_Log.debug("Tramites: "+fichaForm.getListaTramites());
        fichaForm.setListaRestricciones(formManager.cargarRestriccionesFormulario(codFormAntiguo,params));
        m_Log.debug("Restricciones: "+fichaForm.getListaRestricciones());
        fichaForm.setListaTramitesProcedimiento(listaTramitesProcedimiento);
        opcion="cargar";
    }else if("modificar".equals(opcion)){
      m_Log.debug("EMPEZANDO a modificar");
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("municipio",String.valueOf(usuario.getOrgCod()));
      m_Log.debug("______________________________MUNICIPIO " + request.getParameter("codigoInterno"));
      gVO.setAtributo("formCod",request.getParameter("formCod"));
      gVO.setAtributo("formNombre",request.getParameter("formNombre"));
      gVO.setAtributo("formVersion",request.getParameter("formVersion"));
      gVO.setAtributo("codTipo",request.getParameter("codTipo"));
      gVO.setAtributo("codigoInterno", request.getParameter("codigoInterno"));

      Vector precargasSeleccionadas=listaTemasSeleccionados(request.getParameter("listaPrecargasXml"));
      FichaFormularioManager.getInstance().setPrecargasFormulario(request.getParameter("codigoInterno"),
    		  								precargasSeleccionadas,params);

      if (request.getParameter("codFormRel") == null) {
          gVO.setAtributo("codFormRel","0");
      } else {
      gVO.setAtributo("codFormRel",request.getParameter("codFormRel"));
      }
      gVO.setAtributo("fechaAlta",request.getParameter("fechaAlta"));
      gVO.setAtributo("fechaBaja",request.getParameter("fechaBaja"));
      String accesible = request.getParameter("accesible");
      m_Log.debug("Accesible "+accesible);
      if(accesible == null) {
          gVO.setAtributo("accesible","0");
        } else gVO.setAtributo("accesible","1");
      m_Log.debug("Accesible "+gVO.getAtributo("accesible"));
      gVO.setAtributo("codTipoDemandante",request.getParameter("codTipoDemandante"));
      gVO.setAtributo("codTipoFirma",request.getParameter("codTipoFirma"));
      if (request.getParameter("codProcedimiento") == null) {
          gVO.setAtributo("codProcedimiento","0");
      } else {
      gVO.setAtributo("codProcedimiento",request.getParameter("codProcedimiento"));
      }
            gVO.setAtributo("codArea",request.getParameter("codArea"));

      m_Log.debug("______________________________COD_PROCEDIMIENTO --" + gVO.getAtributo("codProcedimiento")+"--");

      String gRegistro = request.getParameter("gRegistro");
      // Si se genera un registro, entonces debemos seleccionar las unidades de registro por defecto
      if(gRegistro == null) {
          m_Log.debug("\n\n\n   gRegistro en IF= " + gRegistro);
          gVO.setAtributo("gRegistro","0");
         
      } else {
          m_Log.debug("\n\n\n   gRegistro en ELSE= " + gRegistro);
          gVO.setAtributo("gRegistro","1");
          // obtenemos las unidades de registro de origen y destino
          gVO.setAtributo("codUnidadRegOrigen",request.getParameter("UorRegOrigen"));
          gVO.setAtributo("codUnidadRegDestino",request.getParameter("UorRegDestino"));
      }

      String gInstancia = request.getParameter("gInstancia");
      if(gInstancia == null) {
          gVO.setAtributo("gInstancia","0");
        } else gVO.setAtributo("gInstancia","1");
      String gTramite = request.getParameter("gTramite");
      if(gTramite == null) {
          gVO.setAtributo("gTramite","0");
        } else gVO.setAtributo("gTramite","1");
      String dTramite = request.getParameter("dTramite");
      if(dTramite == null) {
          gVO.setAtributo("dTramite","0");
        } else gVO.setAtributo("dTramite","1");
      String cerrarT1 = request.getParameter("cerrarT1");
      if(cerrarT1 == null) {
          gVO.setAtributo("cerrarT1","0");
      } else gVO.setAtributo("cerrarT1","1");
      String visible = request.getParameter("visible");
      if(visible == null) {
          gVO.setAtributo("visible","0");
        } else gVO.setAtributo("visible","1");
      gVO.setAtributo("instrucciones",request.getParameter("instrucciones"));

      FormFile formFichero = fichaForm.getFichero();
      if (formFichero.getFileSize() != 0) {
          gVO.setAtributo("fichero",formFichero);
          m_Log.debug("Fichero1 Cargado CORRECTAMENTE");
      } else {
          gVO.setAtributo("fichero",null);
          m_Log.debug("Fichero1 NO Cargado CORRECTAMENTE");
      }

      FormFile formFichero2 = fichaForm.getFichero2();
      if (formFichero2.getFileSize() != 0) {
          gVO.setAtributo("fichero2",formFichero2);
          m_Log.debug("Fichero2 Cargado CORRECTAMENTE");
      } else {
          gVO.setAtributo("fichero2",null);
          m_Log.debug("Fichero2 NO Cargado CORRECTAMENTE");
      }

      //TRÁMITES
        gVO.setAtributo("listaTramProc",listaTemasSeleccionados(request.getParameter("listaTramProc")));
        gVO.setAtributo("listaTramCod",listaTemasSeleccionados(request.getParameter("listaTramCod")));
        gVO.setAtributo("listaTramRel",listaTemasSeleccionados(request.getParameter("listaTramRel")));
        gVO.setAtributo("listaTramEst",listaTemasSeleccionados(request.getParameter("listaTramEst")));
      //TRÁMITES
      //RESTRICCIONES
        gVO.setAtributo("listaRestriccionesUOR",listaTemasSeleccionados(request.getParameter("listaRestriccionesUOR")));
        gVO.setAtributo("listaRestriccionesCargo",listaTemasSeleccionados(request.getParameter("listaRestriccionesCargo")));
      //RESTRICCIONES
      m_Log.debug("RESTRICCIONES");
      m_Log.debug(listaTemasSeleccionados(request.getParameter("listaRestriccionesUOR")));
      m_Log.debug(listaTemasSeleccionados(request.getParameter("listaRestriccionesCargo")));

        // Tipo de restriccion del formulario
        gVO.setAtributo("tipoRestriccionFormulario", request.getParameter("tipoRestriccionForm"));
        m_Log.debug("\n tipoRestriccionFormulario = " + gVO.getAtributo("tipoRestriccionFormulario"));

      Vector lista = formManager.modificarFormulario(gVO,params);
      formForm.setListaFormularios(lista);
      opcion = "inicio";
    }else if("modificarFichero".equals(opcion)){
        Vector lista = formManager.modificarFormularioFichero(fichaForm,params);
        formForm.setListaFormularios(lista);
        opcion = "inicio";
    }else if("nuevaVersion".equals(opcion)){
        String codFormAntiguo = request.getParameter("formCod");
        GeneralValueObject gVO;
        gVO=formManager.cargarFormulario(codFormAntiguo,params, String.valueOf(codOrg));
        m_Log.debug("Cargado CORRECTAMENTE");
        fichaForm.setFormularioVO(gVO);
        m_Log.debug("Cargado 2 CORRECTAMENTE");
        Vector listaTipos = TiposManager.getInstance().getListaTipos(usuario.getParamsCon());
        Vector listaProcedimientos = TramitacionManager.getInstance().getListaProcedimientos(usuario ,usuario.getParamsCon());
        Vector listaAreas = DefinicionProcedimientosManager.getInstance().getListaArea(usuario.getParamsCon());
        Vector listaAreasPorDefecto = FichaFormularioManager.getInstance().cargarAreasPorDefecto(usuario.getParamsCon());
        Vector listaFormulariosTipo0 = FormulariosManager.getInstance().getListaFormulariosTipo0(usuario.getParamsCon());
    	Vector listaPre = FichaFormularioManager.getInstance().getListaPrecargas();
    	Vector precargasSeleccionadas=FichaFormularioManager.getInstance().getPrecargasFormulario(codFormAntiguo, params);

        DefinicionTramitesValueObject dTVO = new DefinicionTramitesValueObject();
        dTVO.setTxtCodigo((String)gVO.getAtributo("procedimiento"));
        dTVO.setCodMunicipio(String.valueOf(usuario.getOrgCod()));
        dTVO.setNumeroTramite("");
        Vector listaTramitesProcedimiento = DefinicionTramitesManager.getInstance().getListaTramites(dTVO,usuario.getParamsCon());

        fichaForm.setListaTipos(listaTipos);
        fichaForm.setListaProcedimientos(listaProcedimientos);
        fichaForm.setListaFormulariosTipo0(listaFormulariosTipo0);
        //===============================
        // seleccionar el jndi para el esquema apropiado
        String jndi = UsuariosGruposManager.getInstance().obtenerJNDI(
                cEnt, cOrg, String.valueOf(usuario.getAppCod()), params);
        String[] paramsNuevos = new String[7];
        paramsNuevos[0] = params[0];
        paramsNuevos[6] = jndi;
        Vector listaUORs = UORsManager.getInstance().getListaUORs(false,paramsNuevos);
        ArbolImpl arbolUORs = UORsManager.getInstance().getArbolUORs(false,false,false, paramsNuevos);
        Vector listaCargos = CargosManager.getInstance().getListaUORs(paramsNuevos);
        ArbolImpl arbolCargos = CargosManager.getInstance().getArbolUORs(false, paramsNuevos);

        fichaForm.setListaTipos(listaTipos);
        fichaForm.setPrecargasSeleccionadas(precargasSeleccionadas);
        fichaForm.setListaPrecargas(listaPre);
        fichaForm.setListaProcedimientos(listaProcedimientos);
        fichaForm.setListaAreas(listaAreas);
        fichaForm.setListaAreasPorDefecto(listaAreasPorDefecto);
        fichaForm.setListaFormulariosTipo0(listaFormulariosTipo0);
        fichaForm.setListaUORs(listaUORs);
        fichaForm.setArbolUORs(arbolUORs);
        fichaForm.setListaCargos(listaCargos);
        fichaForm.setArbolCargos(arbolCargos);

        fichaForm.setListaTramites(formManager.cargarTramitesFormulario(codFormAntiguo,usuario.getOrgCod(),params));        
        m_Log.debug("Tramites: "+fichaForm.getListaTramites());
        fichaForm.setListaRestricciones(formManager.cargarRestriccionesFormulario(codFormAntiguo,params));
        m_Log.debug("Restricciones: "+fichaForm.getListaRestricciones());
        fichaForm.setListaTramitesProcedimiento(listaTramitesProcedimiento);
        opcion="nuevaVersion";
    } else if("eliminar".equalsIgnoreCase(opcion)){
      String codFormAntiguo = request.getParameter("formCod");
      
      FichaFormularioManager.getInstance().eliminarPrecargas(request.getParameter("formCod"),params);
      
      Vector lista = new Vector();
      lista = formManager.eliminarFormulario(codFormAntiguo,params);
      m_Log.debug("Eliminado CORRECTAMENTE. Lista vacia:"+lista.isEmpty());
      //formForm.setListaFormularios(lista);
      opcion = "inicio";
    }else if("cargarTramitesProcedimiento".equals(opcion)){
        DefinicionTramitesValueObject dTVO = new DefinicionTramitesValueObject();
        dTVO.setTxtCodigo(request.getParameter("codProcedimiento"));
        dTVO.setCodMunicipio(String.valueOf(usuario.getOrgCod()));
        dTVO.setNumeroTramite("");
        Vector listaTramitesProcedimiento = DefinicionTramitesManager.getInstance().getListaTramites(dTVO,usuario.getParamsCon());
        fichaForm.setListaTramitesProcedimiento(listaTramitesProcedimiento);
    }else if("salir".equals(opcion)){
      if ((session.getAttribute(mapping.getAttribute()) != null))
        session.removeAttribute(mapping.getAttribute());
    }else{
      opcion = mapping.getInput();
    }
    m_Log.debug("perform");
    return (mapping.findForward(opcion));
  }

    private Vector listaTemasSeleccionados(String listSelecc)	{
      Vector lista = new Vector();
      StringTokenizer codigos =	null;
      if (listSelecc != null) {
        codigos = new StringTokenizer(listSelecc,"§¥",false);
        while	(codigos.hasMoreTokens()) {
          String cod = codigos.nextToken();
          lista.addElement(cod);
        }
      }
      return lista;
    }
}