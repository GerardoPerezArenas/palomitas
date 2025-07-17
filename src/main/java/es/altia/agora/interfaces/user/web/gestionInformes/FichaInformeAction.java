package es.altia.agora.interfaces.user.web.gestionInformes;

import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.helper.ActionHelper;
import es.altia.agora.interfaces.user.web.gestionInformes.exception.InstanciacionDatosInformesException;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.persistence.TramitacionManager;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.DefinicionProcedimientosValueObject;
import es.altia.agora.business.gestionInformes.persistence.FichaInformeManager;
import es.altia.agora.business.gestionInformes.persistence.GestionInformesManager;
import es.altia.agora.business.gestionInformes.FichaInformeValueObject;
import es.altia.agora.business.gestionInformes.InformeValueObject;
import es.altia.agora.business.gestionInformes.CamposDesplegablesValueObject;
import es.altia.agora.business.gestionInformes.ValoresCamposDesplegablesValueObject;
import es.altia.agora.business.administracion.mantenimiento.persistence.CamposDesplegablesManager;
import es.altia.agora.business.util.GeneralValueObject;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionErrors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Vector;
import java.util.StringTokenizer;

public class FichaInformeAction extends ActionSession {

  public ActionForward performSession(ActionMapping mapping, ActionForm form,
                      HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {

    m_Log.debug("--------------------->FichaInformeAction");
    ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));
    HttpSession session = request.getSession();

    // Validaremos los parametros del request especificados
    ActionErrors errors = new ActionErrors();
    String opcion = request.getParameter("opcion");
    if (m_Log.isInfoEnabled()) m_Log.info("la opcion en el action es " + opcion);

    // Rellenamos el form de Ficha Informe
    if (form == null) {
      m_Log.debug("Rellenamos el form de Ficha Informe");
      form = new FichaInformeForm();
      if ("request".equals(mapping.getScope())){
        request.setAttribute(mapping.getAttribute(), form);
      }else{
        session.setAttribute(mapping.getAttribute(), form);
      }
    }
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    String[] params = usuario.getParamsCon();
    FichaInformeForm infForm = (FichaInformeForm)form;
    if ("iniciarInforme".equalsIgnoreCase(opcion)) {
        Vector listaAmbitos = GestionInformesManager.getInstance().getListaAmbitos(params);
        m_Log.debug("listaAmbitos "+listaAmbitos);
        infForm.setListaAmbitos(listaAmbitos);
        Vector listaProcedimientos = TramitacionManager.getInstance().getListaProcedimientos(usuario ,params);
        infForm.setListaProcedimientos(listaProcedimientos);
        infForm.setCodAmbito("");
        infForm.setDescAmbito("");
        infForm.setCodProcedimiento("");
        infForm.setDescProcedimiento("");
    } else if ("altaInforme".equalsIgnoreCase(opcion)) {
        m_Log.debug("NOMBRE : " + infForm.getNombre());
        m_Log.debug("PROCEDIMIENTO : " + infForm.getCodProcedimiento());
        m_Log.debug("PROCEDIMIENTO : " + infForm.getDescProcedimiento());
        m_Log.debug("AMBITO : " + infForm.getCodAmbito());
        m_Log.debug("AMBITO : " + infForm.getDescAmbito());
        infForm.setCodMunicipio(String.valueOf(usuario.getOrgCod()));
        int resultado = FichaInformeManager.getInstance().altaInforme(infForm.getFichaInformeVO(), params);
        m_Log.debug("PLANTILLA : " + infForm.getCodPlantilla());
        
    } else if ("modificarInforme".equalsIgnoreCase(opcion)) {
    	// TODO : el siguiente trozo de codigo deberia estar en un DAO, el action no es sitio para esto, en la
    	// siguiente opcion de esta accion tambien hay que cargar los campos desplegables y he tenido que copiar 
    	// y pegar este trozo
    	Vector listaCamposDesplegables = CamposDesplegablesManager.getInstance().getListaCampoDesplegable(params);
        GeneralValueObject gVO;
        Vector listaValoresCampoDesplegable;
        Vector camposDesplegables = new Vector();
        Vector listaValores;
        CamposDesplegablesValueObject camposDesplegablesVO;
        ValoresCamposDesplegablesValueObject valoresCamposDesplegablesVO;        
        m_Log.debug("Recuperación campos desplegables");
        for (int i=0;i<listaCamposDesplegables.size();i++) {
            gVO = (GeneralValueObject) listaCamposDesplegables.get(i);
            gVO.setAtributo("campo",gVO.getAtributo("codigo"));
            listaValoresCampoDesplegable = CamposDesplegablesManager.getInstance().getListaValoresCampoDesplegable(gVO, params);
            camposDesplegablesVO = new CamposDesplegablesValueObject();
            camposDesplegablesVO.setCodigoCampo((String)gVO.getAtributo("codigo"));
            camposDesplegablesVO.setDescripcionCampo((String)gVO.getAtributo("descripcion"));
            listaValores = new Vector();
            for (int k=0;k<listaValoresCampoDesplegable.size();k++) {
                valoresCamposDesplegablesVO = new ValoresCamposDesplegablesValueObject();
                gVO = (GeneralValueObject) listaValoresCampoDesplegable.get(k);
                valoresCamposDesplegablesVO.setCodigoValor((String)gVO.getAtributo("codigoValor"));
                valoresCamposDesplegablesVO.setDescripcionValor((String)gVO.getAtributo("descripcionValor"));
                listaValores.add(valoresCamposDesplegablesVO);
            }
            camposDesplegablesVO.setListaValores(listaValores);
            camposDesplegables.add(camposDesplegablesVO);
            m_Log.debug("Campo desplegable : " + camposDesplegablesVO);
        }
        // --> Hasta aqui el trozo que deberia estar en un DAO
        infForm.setListaCamposDesplegables(camposDesplegables);
        FichaInformeValueObject fiVO = FichaInformeManager.getInstance().cargarInforme(infForm.getCodPlantilla(), String.valueOf(usuario.getOrgCod()), params);
        infForm.setFichaInformeVO(fiVO);
        Vector listaProcedimientos = TramitacionManager.getInstance().getListaProcedimientos(usuario ,params);
        infForm.setListaProcedimientos(listaProcedimientos);
        
    } else if ("cargarListaCriterios".equalsIgnoreCase(opcion)) {
        m_Log.debug("cargarListaCriterios");
        
        // NOTA: codigo copiado y pegado de mas arriba
        m_Log.debug("Recuperación campos desplegables");
        Vector listaCamposDesplegables = CamposDesplegablesManager.getInstance().getListaCampoDesplegable(params);
        GeneralValueObject gVO;
        Vector listaValores;
        Vector listaValoresCampoDesplegable;
        Vector camposDesplegables = new Vector();
        CamposDesplegablesValueObject camposDesplegablesVO;
        ValoresCamposDesplegablesValueObject valoresCamposDesplegablesVO;
        for (int i=0;i<listaCamposDesplegables.size();i++) {
            gVO = (GeneralValueObject) listaCamposDesplegables.get(i);
            gVO.setAtributo("campo",gVO.getAtributo("codigo"));
            listaValoresCampoDesplegable = CamposDesplegablesManager.getInstance().getListaValoresCampoDesplegable(gVO, params);
            camposDesplegablesVO = new CamposDesplegablesValueObject();
            camposDesplegablesVO.setCodigoCampo((String)gVO.getAtributo("codigo"));
            camposDesplegablesVO.setDescripcionCampo((String)gVO.getAtributo("descripcion"));
            listaValores = new Vector();
            for (int k=0;k<listaValoresCampoDesplegable.size();k++) {
                valoresCamposDesplegablesVO = new ValoresCamposDesplegablesValueObject();
                gVO = (GeneralValueObject) listaValoresCampoDesplegable.get(k);
                valoresCamposDesplegablesVO.setCodigoValor((String)gVO.getAtributo("codigoValor"));
                valoresCamposDesplegablesVO.setDescripcionValor((String)gVO.getAtributo("descripcionValor"));
                listaValores.add(valoresCamposDesplegablesVO);
            }
            camposDesplegablesVO.setListaValores(listaValores);
            camposDesplegables.add(camposDesplegablesVO);
            m_Log.debug("Campo desplegable : " + camposDesplegablesVO);
        }
        // Hasta aqui el codigo copiado y pegado
        infForm.setListaCamposDesplegables(camposDesplegables);
        
        String codInforme = request.getParameter("codInforme");
        m_Log.debug("INFORME "+request.getParameter("codInforme"));
        infForm.setCodPlantilla(codInforme);
        FichaInformeValueObject fiVO = FichaInformeManager.getInstance().cargarInforme(codInforme, String.valueOf(usuario.getOrgCod()), params);
        Vector listaCriterios = new Vector();
        try {
            FachadaDatosInformes fachadaDatos = FactoriaDatosInformes.getImpl(fiVO.getModAmbito());
            listaCriterios = fachadaDatos.getListaCriFinal(fiVO,params);
        } catch (InstanciacionDatosInformesException e) {
            e.printStackTrace();
        }
        infForm.setListaCriInf(listaCriterios);
    } else if ("grabarInforme".equalsIgnoreCase(opcion)) {
        FichaInformeValueObject fiVO = infForm.getFichaInformeVO();
        //CRITERIOS INFORME
        String listaCriInfCodigo = request.getParameter("listaCriInfSeleccionadosCodigo");
        fiVO.setListaCriInfCodigo(listaTemasSeleccionados(listaCriInfCodigo));
        String listaCriInfCondCri = request.getParameter("listaCriInfSeleccionadosCondCri");
        fiVO.setListaCriInfCondCri(listaTemasSeleccionados(listaCriInfCondCri));
        String listaCriInfValor1 = request.getParameter("listaCriInfSeleccionadosValor1");
        fiVO.setListaCriInfValor1(listaTemasSeleccionados(listaCriInfValor1));
        String listaCriInfValor2 = request.getParameter("listaCriInfSeleccionadosValor2");
        fiVO.setListaCriInfValor2(listaTemasSeleccionados(listaCriInfValor2));
        String listaCriInfTitulo = request.getParameter("listaCriInfSeleccionadosTitulo");
        fiVO.setListaCriInfTitulo(listaTemasSeleccionados(listaCriInfTitulo));
        String listaCriInfOrigen = request.getParameter("listaCriInfSeleccionadosOrigen");
        fiVO.setListaCriInfOrigen(listaTemasSeleccionados(listaCriInfOrigen));
        String listaCriInfTabla = request.getParameter("listaCriInfSeleccionadosTabla");
        fiVO.setListaCriInfTabla(listaTemasSeleccionados(listaCriInfTabla));
        //CABECERA INFORME
        String listaCabInfCodigo = request.getParameter("listaCabInfSeleccionadosCodigo");
        String listaCabInfPosx = request.getParameter("listaCabInfSeleccionadosPosx");
        String listaCabInfPosy = request.getParameter("listaCabInfSeleccionadosPosy");
        fiVO.setListaCabInfCodigo(listaTemasSeleccionados(listaCabInfCodigo));
        fiVO.setListaCabInfPosx(listaTemasSeleccionados(listaCabInfPosx));
        fiVO.setListaCabInfPosy(listaTemasSeleccionados(listaCabInfPosy));
        //PIE INFORME
        String listaPieInfCodigo = request.getParameter("listaPieInfSeleccionadosCodigo");
        fiVO.setListaPieInfCodigo(listaTemasSeleccionados(listaPieInfCodigo));
        //CABERCERA PÁGINA
        String listaCabPagCodigo = request.getParameter("listaCabPagSeleccionadosCodigo");
        String listaCabPagPosx = request.getParameter("listaCabPagSeleccionadosPosx");
        String listaCabPagPosy = request.getParameter("listaCabPagSeleccionadosPosy");
        fiVO.setListaCabPagCodigo(listaTemasSeleccionados(listaCabPagCodigo));
        fiVO.setListaCabPagPosx(listaTemasSeleccionados(listaCabPagPosx));
        fiVO.setListaCabPagPosy(listaTemasSeleccionados(listaCabPagPosy));
        //PIE PAGINA
        String listaPiePagCodigo = request.getParameter("listaPiePagSeleccionadosCodigo");
        fiVO.setListaPiePagCodigo(listaTemasSeleccionados(listaPiePagCodigo));
        //PERMISOS
        String listaPermisosCodigo = request.getParameter("listaPermisosSeleccionadosCodigo");
        fiVO.setListaUOR(listaTemasSeleccionados(listaPermisosCodigo));
        //CUERPO: CAMPOS
        String listaCamposCodigo = request.getParameter("listaCamposSeleccionadosCodigo");
        String listaCamposNombre = request.getParameter("listaCamposSeleccionadosNombre");
        String listaCamposTabla = request.getParameter("listaCamposSeleccionadosTabla");
        String listaCamposPosx = request.getParameter("listaCamposSeleccionadosPosx");
        String listaCamposPosy = request.getParameter("listaCamposSeleccionadosPosy");
        String listaCamposAlign = request.getParameter("listaCamposSeleccionadosAlign");
        String listaCamposAncho = request.getParameter("listaCamposSeleccionadosAncho");
        String listaCamposElipsis = request.getParameter("listaCamposSeleccionadosElipsis");
        String listaCamposOrden = request.getParameter("listaCamposSeleccionadosOrden");
        m_Log.debug("**************** entro  "+listaCamposOrden);
        fiVO.setListaCamposCodigo(listaTemasSeleccionados(listaCamposCodigo));
        fiVO.setListaCamposNombre(listaTemasSeleccionados(listaCamposNombre));
        fiVO.setListaCamposTabla(listaTemasSeleccionados(listaCamposTabla));
        fiVO.setListaCamposPosx(listaTemasSeleccionados(listaCamposPosx));
        fiVO.setListaCamposPosy(listaTemasSeleccionados(listaCamposPosy));
        fiVO.setListaCamposAlign(listaTemasSeleccionados(listaCamposAlign));
        fiVO.setListaCamposAncho(listaTemasSeleccionados(listaCamposAncho));
        fiVO.setListaCamposElipsis(listaTemasSeleccionados(listaCamposElipsis));
        fiVO.setListaCamposOrden(listaTemasSeleccionados(listaCamposOrden));
        int resultado = FichaInformeManager.getInstance().grabarInforme(fiVO, params);
        GestionInformesForm gestInformesForm = new GestionInformesForm();
        Vector lista = GestionInformesManager.getInstance().getListaInformes(params);
        Vector listaProcedimientos = TramitacionManager.getInstance().getListaProcedimientos(usuario ,params);                
        
        gestInformesForm.setListaProcedimientos(listaProcedimientos);
        gestInformesForm.setListaInformes(lista);
        request.setAttribute("GestionInformesForm", gestInformesForm);

    } else if("verificarInforme".equals(opcion)) {
        m_Log.debug("verificarInforme");
        String codPlantilla = request.getParameter("codPlantilla");
        String nombrePlantilla = request.getParameter("nombre");
        String codProcedimiento = request.getParameter("codProcedimiento");
        String descAmbito = request.getParameter("descAmbito");
        m_Log.debug("codPlantilla " + codPlantilla);
        m_Log.debug("nombrePlantilla " + nombrePlantilla);
        m_Log.debug("codProcedimiento " + codProcedimiento);
        m_Log.debug("descAmbito " + descAmbito);
        if (!(FichaInformeManager.getInstance().yaExiste(nombrePlantilla, params))) {
            DefinicionProcedimientosValueObject defProcVO = new DefinicionProcedimientosValueObject();
            defProcVO.setCodMunicipio("0");
            defProcVO.setCodAplicacion(String.valueOf(usuario.getAppCod()));
            String jndi = DefinicionProcedimientosManager.getInstance().obtenerJndi(defProcVO,params);
            String[] paramsEntornoPruebas = new String[7];
            paramsEntornoPruebas[0] = params[0];
            paramsEntornoPruebas[6] = jndi;
            if (FichaInformeManager.getInstance().hayDatosEnCuerpoInforme(codPlantilla, paramsEntornoPruebas)) {
                if (codProcedimiento!=null && !codProcedimiento.equals("")) {
                    if (FichaInformeManager.getInstance().camposDisponibles(codPlantilla, codProcedimiento, descAmbito, usuario, paramsEntornoPruebas, params)) {
                        if (FichaInformeManager.getInstance().
                            criteriosDisponibles(codPlantilla, codProcedimiento, descAmbito, usuario, paramsEntornoPruebas,params))
                        infForm.setVerificacion("correcta");
                        else infForm.setVerificacion("camposNoDisponibles");
                    } else {
                        infForm.setVerificacion("camposNoDisponibles");
                    }
                } else {
                    infForm.setVerificacion("correcta");
                }
            } else {
                infForm.setVerificacion("correcta");
            }
        } else {
            infForm.setVerificacion("yaExiste");
        }
        request.setAttribute("importando","si");

    } else if("descargarInforme".equals(opcion)) {
        m_Log.debug("descargarInforme");
        String codPlantilla = request.getParameter("codPlantilla");
        String nombrePlantilla = request.getParameter("nombre");
        String codProcedimiento = request.getParameter("codProcedimiento");
        String descAmbito = request.getParameter("descAmbito");
        m_Log.debug("codPlantilla " + codPlantilla);
        m_Log.debug("nombrePlantilla " + nombrePlantilla);
        m_Log.debug("codProcedimiento " + codProcedimiento);
        m_Log.debug("descAmbito " + descAmbito);
        InformeValueObject informeVO;
        if (!(FichaInformeManager.getInstance().yaExiste(nombrePlantilla, params))) {
            DefinicionProcedimientosValueObject defProcVO = new DefinicionProcedimientosValueObject();
            defProcVO.setCodMunicipio("0");
            defProcVO.setCodAplicacion(String.valueOf(usuario.getAppCod()));
            String jndi = DefinicionProcedimientosManager.getInstance().obtenerJndi(defProcVO,params);
            String[] paramsEntornoPruebas = new String[7];
            paramsEntornoPruebas[0] = params[0];
            paramsEntornoPruebas[6] = jndi;
             /*
             * Primero tengo que comprobar los permisos y determinar si se puede o no realizar la iportación
             */
            if (FichaInformeManager.getInstance().permisosDisponibles(codPlantilla, codProcedimiento, descAmbito, usuario, paramsEntornoPruebas, params))
            {
            if (FichaInformeManager.getInstance().hayDatosEnCuerpoInforme(codPlantilla, paramsEntornoPruebas)) {
                if (codProcedimiento!=null && !codProcedimiento.equals("")) {
                    if (FichaInformeManager.getInstance().
                            camposDisponibles(codPlantilla, codProcedimiento, descAmbito, usuario, paramsEntornoPruebas,params)) {
                        /*Aqui tengo que comprobar los criterios. Si no son válidos tampoco se importa.*/

                        if (FichaInformeManager.getInstance().
                            criteriosDisponibles(codPlantilla, codProcedimiento, descAmbito, usuario, paramsEntornoPruebas,params)) {
                        try {
                            informeVO = FichaInformeManager.getInstance().getInforme(paramsEntornoPruebas,codPlantilla);
                            FichaInformeManager.getInstance().insertarInforme(informeVO,params);
                            infForm.setVerificacion("importacionRealizada");
                        } catch (Exception e) {
                            e.printStackTrace();
                            infForm.setVerificacion("importacionNoRealizada");
                        }
                    } else {
                        infForm.setVerificacion("camposNoDisponibles");
                    }
                } else {
                        infForm.setVerificacion("camposNoDisponibles");
                    }
                } else {
                    try {
                        informeVO = FichaInformeManager.getInstance().getInforme(paramsEntornoPruebas,codPlantilla);
                        FichaInformeManager.getInstance().insertarInforme(informeVO,params);
                        infForm.setVerificacion("importacionRealizada");
                    } catch (Exception e) {
                        e.printStackTrace();
                        infForm.setVerificacion("importacionNoRealizada");
                    }
                }
            } else {
                try {
                    informeVO = FichaInformeManager.getInstance().getInforme(paramsEntornoPruebas,codPlantilla);
                    FichaInformeManager.getInstance().insertarInforme(informeVO,params);
                    infForm.setVerificacion("importacionRealizada");
                } catch (Exception e) {
                    e.printStackTrace();
                    infForm.setVerificacion("importacionNoRealizada");
                }
            }
        }
            else{
                infForm.setVerificacion("permisosNoDisponibles");
            }
        } else {
            infForm.setVerificacion("yaExiste");
        }
        request.setAttribute("importando","si");
        opcion = "verificarInforme";

    } else if("salir".equals(opcion)){
      if ((session.getAttribute(mapping.getAttribute()) != null))
        session.removeAttribute(mapping.getAttribute());
    } else{
      opcion = mapping.getInput();
    }
    m_Log.debug("perform");
    return (mapping.findForward(opcion));
  }

    private Vector listaTemasSeleccionados(String listTemasSelecc) {
      Vector lista = new Vector();
      StringTokenizer valores = null;
      if (listTemasSelecc != null) {
        valores = new StringTokenizer(listTemasSelecc,"§¥",false);
        while (valores.hasMoreTokens()) {
          String valor = valores.nextToken();
          lista.addElement(valor);
        }
      }
      return lista;
    }
}