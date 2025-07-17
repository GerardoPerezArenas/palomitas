package es.altia.agora.interfaces.user.web.gestionInformes;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import java.util.Random;

import es.altia.agora.business.administracion.mantenimiento.persistence.CamposDesplegablesManager;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.gestionInformes.CamposDesplegablesValueObject;
import es.altia.agora.business.gestionInformes.CriteriosSolicitudValueObject;
import es.altia.agora.business.gestionInformes.CriteriosValueObject;
import es.altia.agora.business.gestionInformes.SolicitudInformeValueObject;
import es.altia.agora.business.gestionInformes.ValoresCamposDesplegablesValueObject;
import es.altia.agora.business.gestionInformes.persistence.FichaInformeManager;
import es.altia.agora.business.gestionInformes.persistence.GestionInformesManager;
import es.altia.agora.business.gestionInformes.persistence.SolicitudesInformesManager;
import es.altia.agora.business.gestionInformes.persistence.manual.SolicitudesInformesDAO;
import es.altia.agora.business.gestionInformes.tareas.ListenerGeneraInforme;
import es.altia.agora.business.gestionInformes.tareas.TareaGeneraInforme;
import es.altia.agora.business.registro.persistence.AuditoriaManager;
import es.altia.agora.business.sge.persistence.TramitacionManager;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.interfaces.user.web.helper.ActionHelper;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.service.auditoria.ConstantesAuditoria;
import es.altia.util.struts.StrutsUtilOperations;
import org.apache.commons.lang.math.NumberUtils;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase ProcedimientosAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: ALTIA CONSULTORES & AYTOS CPD</p>
 * @author Jorge Hombre Tuñas
 * @version 1.0
 */


public class SolicitudesInformesAction extends ActionSession {

  public ActionForward performSession(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {

    m_Log.debug("en SolicitudesInformesAction");
    ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));
    HttpSession session = request.getSession();

    // Validaremos los parametros del request especificados
    ActionErrors errors = new ActionErrors();
    String opcion = request.getParameter("opcion");
    if (m_Log.isInfoEnabled()) m_Log.info("la opcion en SolicitudesInformesAction es " + opcion);

    // Rellenamos el form de BusquedaTerceros
    if (form == null) {
      m_Log.debug("Rellenamos el form de solicitudes");
      form = new SolicitudesInformesForm();
      if ("request".equals(mapping.getScope())){
        request.setAttribute(mapping.getAttribute(), form);
      }else{
        session.setAttribute(mapping.getAttribute(), form);
      }
    }
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    String[] params = usuario.getParamsCon();
    SolicitudesInformesForm solForm = (SolicitudesInformesForm)form;

    if ("inicio".equalsIgnoreCase(opcion)) {
        Vector listaAmbitos = GestionInformesManager.getInstance().getListaAmbitos(params);
        solForm.setListaAmbitos(listaAmbitos);
        Vector listaProcedimientos = TramitacionManager.getInstance().getListaProcedimientos(usuario , params);
        solForm.setListaProcedimientos(listaProcedimientos);
        Vector lista = SolicitudesInformesManager.getInstance().getListaSolicitudes(usuario, params, null);
        solForm.setListaSolicitudes(lista);
        solForm.setOrigen("");
    } else if ("inicioProvisional".equalsIgnoreCase(opcion)) {
        String origen = request.getParameter("origen");
        solForm.setOrigen(origen);
        m_Log.debug("ORIGEN " + origen);
        Vector listaAmbitos = GestionInformesManager.getInstance().getListaAmbitos(params);
        solForm.setListaAmbitos(listaAmbitos);
        Vector listaProcedimientos = TramitacionManager.getInstance().getListaProcedimientos(usuario , params);
        solForm.setListaProcedimientos(listaProcedimientos);
        Vector lista = SolicitudesInformesManager.getInstance().getListaSolicitudes(usuario, params, origen);
        solForm.setListaSolicitudes(lista);
    } else if ("generacion".equalsIgnoreCase(opcion)) {
        Vector listaAmbitos = GestionInformesManager.getInstance().getListaAmbitos(params);
        solForm.setListaAmbitos(listaAmbitos);
        Vector listaCamposDesplegables = CamposDesplegablesManager.getInstance().getListaCampoDesplegable(params);
        GeneralValueObject gVO;
        Vector listaValoresCampoDesplegable;
        Vector camposDesplegables = new Vector();
        Vector listaValores;
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
        }
        solForm.setListaCamposDesplegables(camposDesplegables);
        Vector listaProcedimientos = TramitacionManager.getInstance().getListaProcedimientos(usuario ,usuario.getParamsCon());
        solForm.setListaProcedimientos(listaProcedimientos);
        Vector lista = GestionInformesManager.getInstance().getListaInformesPublicados(usuario, null);
        solForm.setListaInformes(lista);
        solForm.setOrigen("");
    } else if ("generacionProvisional".equalsIgnoreCase(opcion)) {
        String origen = request.getParameter("origen");
        solForm.setOrigen(origen);
        m_Log.debug("ORIGEN " + origen);
        Vector listaAmbitos = GestionInformesManager.getInstance().getListaAmbitos(params);
        solForm.setListaAmbitos(listaAmbitos);
        Vector listaCamposDesplegables = CamposDesplegablesManager.getInstance().getListaCampoDesplegable(params);
        GeneralValueObject gVO;
        Vector listaValoresCampoDesplegable;
        Vector camposDesplegables = new Vector();
        Vector listaValores;
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
        }
        solForm.setListaCamposDesplegables(camposDesplegables);
        Vector listaProcedimientos = TramitacionManager.getInstance().getListaProcedimientos(usuario ,usuario.getParamsCon());
        solForm.setListaProcedimientos(listaProcedimientos);
        Vector lista = GestionInformesManager.getInstance().getListaInformesPublicados(usuario, origen);
        solForm.setListaInformes(lista);
    } else if ("generar".equalsIgnoreCase(opcion)) {
        String origen = request.getParameter("origen");
        String dir = request.getParameter("dir");
        String codProcedimiento = request.getParameter("codProcedimiento");
        String codPlantilla = request.getParameter("codPlantilla");
        String tipoFichero = request.getParameter("tipoFichero");
        String modoVisualizar=request.getParameter("modoVisualizar");
        String descripcion = request.getParameter("descripcion");
        String listaCamposCriterio = request.getParameter("listaCamposCriterio");
        String listaTiposCriterio = request.getParameter("listaTiposCriterio");
        String listaValorOperCriterio = request.getParameter("listaValorOperCriterio");
        String listaValor1Criterio = request.getParameter("listaValor1Criterio");
        String listaValor2Criterio = request.getParameter("listaValor2Criterio");
        Vector camposCriterio = listaTemasSeleccionados(listaCamposCriterio);
        Vector tiposCriterio = listaTemasSeleccionados(listaTiposCriterio);
        Vector valorOperCriterio = listaTemasSeleccionados(listaValorOperCriterio);
        Vector valor1Criterio = listaTemasSeleccionados(listaValor1Criterio);
        Vector valor2Criterio = listaTemasSeleccionados(listaValor2Criterio);
        m_Log.debug("origen "+origen);
        m_Log.debug("codProcedimiento "+codProcedimiento);
        m_Log.debug("dir "+dir);
        m_Log.debug("codPlantilla "+codPlantilla);
        m_Log.debug("tipoFichero "+tipoFichero);
        m_Log.debug("modoVisualizar "+modoVisualizar);
        m_Log.debug("descripcion "+descripcion);
        m_Log.debug("listaCamposCriterio "+listaCamposCriterio);
        m_Log.debug("listaTiposCriterio "+listaTiposCriterio);
        m_Log.debug("listaValorOperCriterio "+listaValorOperCriterio);
        m_Log.debug("listaValor1Criterio "+listaValor1Criterio);
        m_Log.debug("listaValor2Criterio "+listaValor2Criterio);
        m_Log.debug("tiposCriterio.size() "+tiposCriterio.size());
        m_Log.debug("valorOperCriterio "+valorOperCriterio.size());
        m_Log.debug("valor1Criterio "+valor1Criterio.size());
        m_Log.debug("valor2Criterio "+valor2Criterio.size());

        //CONTROLAR LOS CRITERIOS
        Vector listaCriteriosSolicitud = new Vector();
        Vector listaCriterios = FichaInformeManager.getInstance().getListaCriterios(codPlantilla, params);
        SolicitudInformeValueObject siVO = new SolicitudInformeValueObject();
        siVO.setTieneCriterioInteresado(false);
        for (int i=0; i<listaCriterios.size(); i++ ) {
            CriteriosValueObject criterio = (CriteriosValueObject) listaCriterios.get(i);
            CriteriosSolicitudValueObject criterioSolicitud = new CriteriosSolicitudValueObject();
            String valorCriterio = "0";
            int pos = encontrarPosCriterio(camposCriterio, criterio.getCampo());
            m_Log.debug("CRITERIO : " + pos);
            if (valorOperCriterio.elementAt(pos) != null)
                valorCriterio = (String) valorOperCriterio.elementAt(pos);
            m_Log.debug("OPER : " + valorOperCriterio);
            String valor1 = (String) valor1Criterio.elementAt(pos);
            m_Log.debug("VALOR1 : " + valor1);
            String valor2 = "";
            if (valor2Criterio.elementAt(pos) != null && !valor2Criterio.elementAt(pos).equals("null") && !valor2Criterio.elementAt(pos).equals(""))
                valor2 = (String) valor2Criterio.elementAt(pos);
            m_Log.debug("VALOR2 criterio : " + valor2Criterio);
            m_Log.debug("VALOR2 : " + valor2);
            criterioSolicitud.setCampo(criterio.getCampo());
            if ("I".equals(criterio.getTipo())) siVO.setTieneCriterioInteresado(true);
            criterioSolicitud.setTipo(criterio.getTipo());
            criterioSolicitud.setCondicion(valorCriterio);
            criterioSolicitud.setValor1(valor1);
            criterioSolicitud.setValor2(valor2);
            criterioSolicitud.setTitulo(criterio.getTitulo());
            criterioSolicitud.setOrigen(criterio.getOrigen());
            criterioSolicitud.setTabla(criterio.getTabla());
            listaCriteriosSolicitud.add(criterioSolicitud);
            m_Log.debug("CriterioSolicitud "+criterioSolicitud);
        }

        
        siVO.setProcedimiento(codProcedimiento);
        siVO.setCodPlantilla(codPlantilla);
        siVO.setCodUsuario(String.valueOf(usuario.getIdUsuario()));
        siVO.setFormato(tipoFichero);
        siVO.setDescripcion(descripcion);
        siVO.setListaCriterios(listaCriteriosSolicitud);
        try {
            long res = SolicitudesInformesManager.getInstance().grabarSolicitud(siVO, params);
            if (res!=-1) {
                SchedulerFactory factoriaPlanificadores = new org.quartz.impl.StdSchedulerFactory();
		        Scheduler planificador = factoriaPlanificadores.getScheduler();

                Random rnd = new Random();
                double aleatorio= rnd.nextDouble();
                String nombreAleatorio="TGI_"+Double.toString(aleatorio);
                ListenerGeneraInforme jl = new ListenerGeneraInforme();
                planificador.addJobListener(jl);
                JobDetail jd = new JobDetail(nombreAleatorio, "SGE", TareaGeneraInforme.class);
                jd.getJobDataMap().put("codigoSolicitud",String.valueOf(res));
                jd.getJobDataMap().put("codigoPlantilla",codPlantilla);
                jd.getJobDataMap().put("tipoFichero",tipoFichero);
                jd.getJobDataMap().put("modoVisualizar",modoVisualizar);
                jd.getJobDataMap().put("usuario",usuario);
                jd.getJobDataMap().put("host",request.getHeader("Host"));
                jd.getJobDataMap().put("context",request.getContextPath());
                jd.getJobDataMap().put("solicitudInformeVO",siVO);
                jd.getJobDataMap().put("protocolo", StrutsUtilOperations.getProtocol(request));
                jd.addJobListener(jl.getName());
                String nombreAleatorioTrigger="Generador Informes_"+Double.toString(aleatorio);
                SimpleTrigger trigger = new SimpleTrigger(nombreAleatorioTrigger, "INFORMES");
                planificador.scheduleJob(jd, trigger);
                planificador.start();
                
                // Auditoria de acceso
                String pantalla = null;
                if ("2".equals(modoVisualizar)) {
                    pantalla = ConstantesAuditoria.EXPEDIENTE_SOLICITUD_GENERAR_INFORME_DIRECTO;
                } else {
                    pantalla = ConstantesAuditoria.EXPEDIENTE_SOLICITUD_GENERAR_INFORME_BUZON;
                }
                rellenarCadenaCriterios(siVO);
                AuditoriaManager.getInstance().auditarAccesoSolicitudInforme(
                        pantalla, usuario, siVO);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            // Redirigir a mensaje de error
        } catch (Exception e) {
            e.printStackTrace();
            // Redirigir a mensaje de error
        }
        Vector listaProcedimientos = TramitacionManager.getInstance().getListaProcedimientos(usuario , params);
        solForm.setListaProcedimientos(listaProcedimientos);
        Vector lista = SolicitudesInformesManager.getInstance().getListaSolicitudes(usuario, params, origen);
        solForm.setListaSolicitudes(lista);
        solForm.setOrigen(origen);
        if (dir.equals("1")&& modoVisualizar.equals("1")){
        	   m_Log.debug("***************************************************  inicio ");
            opcion="inicio";
        } else if (modoVisualizar.equals("2")){
        	
        	 String codinforme=siVO.getCodSolicitud();
        	 solForm.setCodSolicitud(codinforme);
        	m_Log.debug("******************************************************  mi opcion "+codinforme);
        	 opcion="inicioPantallaActual";

        		m_Log.debug("******************************************************  hola");
        }else{
        	m_Log.debug("******************************************************  inicioProvisional ");
        	opcion="inicioProvisional";
        }
    } else if ("verDetallesSolicitud".equalsIgnoreCase(opcion)) {
        m_Log.debug("verDetallesSolicitud");
        String codInforme = request.getParameter("codInforme");
        m_Log.debug("INFORME java"+request.getParameter("codInforme"));
        try {
            SolicitudInformeValueObject solicitudInformeVO = SolicitudesInformesManager.getInstance().obtenerSolicitud(usuario.getParamsCon(),codInforme);
            solForm.setSolicitudInforme(solicitudInformeVO);
        } catch (Exception e) {
            e.printStackTrace();
            opcion = "errorVerDetallesSolicitud";
        }
    } else if ("eliminarSolicitud".equalsIgnoreCase(opcion)) {
    	   m_Log.debug("******** +++++++++++++++++++++++ ++++++++++++++++++++++++++++ ENTRO");
        String origen = request.getParameter("origen");
        solForm.setOrigen(origen);
        m_Log.debug("******** +++++++++++++++++++++++ ++++++++++++++++++++++++++++ ORIGEN " + origen);
        String codSolicitud = request.getParameter("codInforme");
        m_Log.debug("******** +++++++++++++++++++++++ ++++++++++++++++++++++++++++ ORIGEN " + codSolicitud);
        try {
            SolicitudesInformesManager.getInstance().eliminarSolicitud(params, codSolicitud);
            Vector lista = SolicitudesInformesManager.getInstance().getListaSolicitudes(usuario, params, origen);
            solForm.setListaSolicitudes(lista);
            
            // Auditoria de acceso
            String mensaje = String.format("codSolicitud[%s]", codSolicitud);
            AuditoriaManager.getInstance().auditarAccesoSolicitudInforme(
                    ConstantesAuditoria.EXPEDIENTE_SOLICITUD_ELIMINAR_INFORME_BUZON, usuario, mensaje);
            
            opcion = "cargarSolicitudes";
        } catch (Exception e) {
            e.printStackTrace();
            opcion = "errorEliminarSolicitud";
        }
    } else if ("cargarSolicitudes".equalsIgnoreCase(opcion)) {
        String origen = request.getParameter("origen");
        solForm.setOrigen(origen);
        m_Log.debug("*************             ORIGEN " + origen);
        Vector lista = SolicitudesInformesManager.getInstance().getListaSolicitudes(usuario, params, origen);
        solForm.setListaSolicitudes(lista);
       
    }else if ("cargarSolicitudesPantalla".equalsIgnoreCase(opcion)) {
  
        String origen = request.getParameter("origen");
        solForm.setOrigen(origen);
        m_Log.debug("*************             ORIGEN " + origen);
        Vector lista = SolicitudesInformesManager.getInstance().getListaSolicitudes(usuario, params, origen);
        solForm.setListaSolicitudes(lista);
        opcion = "cargarSolicitudesPantallaActual";
    } else if("salir".equals(opcion)) {
        if ((session.getAttribute(mapping.getAttribute()) != null))
            session.removeAttribute(mapping.getAttribute());
    } else {
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

    private int encontrarPosCriterio(Vector listaCampos, String campo) {
        int pos = 0;
        boolean encontrado = false;
        while (!encontrado && pos<listaCampos.size()) {
            if (listaCampos.get(pos).equals(campo)) {
                encontrado = true;
            } else {
                pos++;
            }
        }
        return pos;
    }

    /**
     * Convierte los criterios a un formato legible
     * 
     * @param siVO 
     */
    private void rellenarCadenaCriterios(SolicitudInformeValueObject siVO) {
        StringBuilder cadenaCriterio = new StringBuilder();

        if (siVO != null) {
            Vector<CriteriosSolicitudValueObject> listaCriterios = siVO.getListaCriterios();

            CriteriosSolicitudValueObject criterio = null;
            for (int i = 0; i < listaCriterios.size(); i++) {
                criterio = listaCriterios.get(i);
                
                cadenaCriterio.append(
                        SolicitudesInformesDAO.getInstance().criterioSolicitudToString(
                            NumberUtils.createInteger(criterio.getCondicion()),
                            ConstantesAuditoria.EXPEDIENTE_SOLICITUD_INFORME_CRITERIO_INICIO_LINEA,
                            ConstantesAuditoria.EXPEDIENTE_SOLICITUD_INFORME_CRITERIO_SUSTITUIR_NEWLINE,
                            criterio.getTitulo(),
                            criterio.getValor1(),
                            criterio.getValor2()));
            }
            
            siVO.setCadenaCriterios(cadenaCriterio.toString());
        }
    }
}
