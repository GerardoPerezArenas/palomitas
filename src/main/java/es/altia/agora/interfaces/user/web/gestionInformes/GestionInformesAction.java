package es.altia.agora.interfaces.user.web.gestionInformes;

// PAQUETES IMPORTADOS
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.helper.*;
import es.altia.agora.business.sge.persistence.TramitacionManager;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.DefinicionProcedimientosValueObject;
import es.altia.agora.business.gestionInformes.persistence.GestionInformesManager;
import es.altia.agora.business.gestionInformes.persistence.FichaInformeManager;
import es.altia.agora.business.gestionInformes.InformeValueObject;
import es.altia.agora.business.gestionInformes.InformeTableModel;
import es.altia.agora.business.gestionInformes.FichaInformeValueObject;
import es.altia.agora.business.gestionInformes.CriteriosValueObject;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.escritorio.persistence.UsuarioManager;
import es.altia.agora.business.util.GeneralValueObject;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;


/**
 * <p>T�tulo: Proyecto @gora</p>
 * <p>Descripci�n: Clase ProcedimientosAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: ALTIA CONSULTORES & AYTOS CPD</p>
 * @author Jorge Hombre Tu�as
 * @version 1.0
 */


public class GestionInformesAction extends ActionSession  {

  public ActionForward performSession(ActionMapping mapping, ActionForm form,
                      HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {

    m_Log.debug("perform");
    ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));
    HttpSession session = request.getSession();

    // Validaremos los parametros del request especificados
    ActionErrors errors = new ActionErrors();
    String opcion = request.getParameter("opcion");
    if (m_Log.isInfoEnabled()) m_Log.info("la opcion en GestionInformesAction es " + opcion);

    // Rellenamos el form de BusquedaTerceros
    if (form == null) {
      m_Log.debug("Rellenamos el form de informes");
      form = new GestionInformesForm();
      if ("request".equals(mapping.getScope())){
        request.setAttribute(mapping.getAttribute(), form);
      }else{
        session.setAttribute(mapping.getAttribute(), form);
      }
    }
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    String[] params = usuario.getParamsCon();
    GestionInformesForm infForm = (GestionInformesForm)form;

    if ("inicio".equalsIgnoreCase(opcion)) {
        Vector listaAmbitos = GestionInformesManager.getInstance().getListaAmbitos(params);
        m_Log.debug("listaAmbitos "+listaAmbitos);
        infForm.setListaAmbitos(listaAmbitos);
        Vector listaProcedimientos = TramitacionManager.getInstance().getListaProcedimientos(usuario ,params);
        infForm.setListaProcedimientos(listaProcedimientos);
        Vector lista = GestionInformesManager.getInstance().getListaInformes(usuario.getParamsCon());
        infForm.setListaInformes(lista);
    } else if ("importarInforme".equalsIgnoreCase(opcion)) {
        DefinicionProcedimientosValueObject defProcVO = new DefinicionProcedimientosValueObject();
        defProcVO.setCodMunicipio("0");
        defProcVO.setCodAplicacion(String.valueOf(usuario.getAppCod()));
        String jndi = DefinicionProcedimientosManager.getInstance().obtenerJndi(defProcVO,params);
        String[] paramsEntornoPruebas = new String[7];
        UsuarioValueObject usuarioPruebas = new UsuarioValueObject();
        usuarioPruebas.setOrgCod(0);
        usuario.setEntCod(usuario.getEntCod());
        paramsEntornoPruebas[0] = params[0];
        paramsEntornoPruebas[6] = jndi;
        Vector listaProcedimientos = TramitacionManager.getInstance().getListaProcedimientos(usuarioPruebas, paramsEntornoPruebas);
        infForm.setListaProcedimientos(listaProcedimientos);
        Vector lista = GestionInformesManager.getInstance().getListaInformes(paramsEntornoPruebas);
        infForm.setListaInformes(lista);
        request.setAttribute("importando","si");
        opcion = "inicio";
    } else if("publicarInforme".equals(opcion)) {
        m_Log.debug("publicarInforme");        
        String codInforme = request.getParameter("codInforme");
        String publicar = request.getParameter("publicar");
        m_Log.debug("INFORME "+request.getParameter("codInforme"));
        m_Log.debug("PUBLICAR "+request.getParameter("publicar"));
        if (GestionInformesManager.getInstance().hayPermiso(codInforme, params)){
            infForm.setHayPermiso("SI");                   
            if (FichaInformeManager.getInstance().hayDatosEnCuerpoInforme(codInforme, params) || publicar.equals("0")) {
                FichaInformeManager.getInstance().publicarInforme(codInforme, publicar, params);
            }
        } else infForm.setHayPermiso("NO");       
        Vector lista = GestionInformesManager.getInstance().getListaInformes(usuario.getParamsCon());
        infForm.setListaInformes(lista);
        infForm.setCodPlantilla(codInforme);
        infForm.setPublicar(publicar);
             
    }else if("eliminarInforme".equalsIgnoreCase(opcion)){ 
    	//Se necesita el codigo del informe(clave primaria) para borrar en PLANT_INFORMES 
    	String codInforme = request.getParameter("codInforme");
    	m_Log.debug("--> Codigo de la plantilla : " + codInforme);
    	int resultado = FichaInformeManager.getInstance().eliminarInforme(codInforme, params);
    	m_Log.debug("--> Resultado : " + resultado);	
    	
        Vector lista = GestionInformesManager.getInstance().getListaInformes(params);
        infForm.setListaInformes(lista);

    } else if("salir".equals(opcion)) {
        if ((session.getAttribute(mapping.getAttribute()) != null))
            session.removeAttribute(mapping.getAttribute());
    } else if ("presentacion".equals(opcion)){
    } else {
      opcion = mapping.getInput();
    }
    m_Log.debug("perform");
    return (mapping.findForward(opcion));
  }
}