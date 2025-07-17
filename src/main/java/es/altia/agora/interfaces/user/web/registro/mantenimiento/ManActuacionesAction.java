// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.registro.mantenimiento;

// PAQUETES IMPORTADOS
import es.altia.agora.business.escritorio.RegistroUsuarioValueObject;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.mantenimiento.ManActuacionesValueObject;
import es.altia.agora.business.registro.mantenimiento.persistence.ManActuacionesManager;
import es.altia.agora.business.registro.persistence.RegistroAperturaCierreManager;
import es.altia.agora.interfaces.user.web.helper.ActionHelper;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.technical.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import java.io.IOException;

import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.struts.action.*;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase ManActuacionesAction </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class ManActuacionesAction extends ActionSession {
  protected static Log m_Log =
          LogFactory.getLog(ManActuacionesAction.class.getName());

  public ActionForward performSession(	ActionMapping mapping,
                  ActionForm form,
                  HttpServletRequest request,
                  HttpServletResponse response)
    throws IOException, ServletException {

    String[] params = null;
    m_Log.debug("perform");
    ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));
    ActionErrors errors = new ActionErrors();

    HttpSession session = request.getSession();
    RegistroUsuarioValueObject regUsuarioVO= new RegistroUsuarioValueObject();
    if (session.getAttribute("usuario") != null && session.getAttribute("registroUsuario") !=null){
      UsuarioValueObject usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
      regUsuarioVO = (RegistroUsuarioValueObject)session.getAttribute("registroUsuario");
      params = usuarioVO.getParamsCon();
    }

    String opcion = request.getParameter("opcion");
    int cod_dep;
    int cod_uni;
    cod_dep= regUsuarioVO.getDepCod();
    cod_uni= regUsuarioVO.getUnidadOrgCod();
    ManActuacionesValueObject manActVO = new ManActuacionesValueObject();
    Date fRegCerradoE = new Date();
    Date fRegCerradoS = new Date();
    RegistroValueObject elRegistroESVO = new RegistroValueObject();


    if (opcion.equals("cargar")){

      // Rellenamos el form de Unidades Organicas
      if (form == null) {
        m_Log.debug("Rellenamos el form de Mantenimiento Actuaciones");
        form = new ManActuacionesForm();
        if ("request".equals(mapping.getScope())){
          request.setAttribute(mapping.getAttribute(), form);
        }else{
          session.setAttribute(mapping.getAttribute(), form);
        }
      }

      ManActuacionesForm manActForm = (ManActuacionesForm)form;

      /* Obtenemos ahora el mantenimientoTemas de la capa de negocio*/
      elRegistroESVO.setIdentDepart(cod_dep);
      elRegistroESVO.setUnidadOrgan(cod_uni);
      elRegistroESVO.setTipoReg("E");
      fRegCerradoE = RegistroAperturaCierreManager.getInstance().getFechaRegistroCerrado(elRegistroESVO,params);
      Fecha f = new Fecha();
      String fechaCerradoE = f.obtenerString(fRegCerradoE);
      elRegistroESVO.setTipoReg("S");
      fRegCerradoS = RegistroAperturaCierreManager.getInstance().getFechaRegistroCerrado(elRegistroESVO,params);
      String fechaCerradoS = f.obtenerString(fRegCerradoS);
      Vector lista = new Vector();
      lista = ManActuacionesManager.getInstance().buscaActuaciones(params);
      manActForm.setCodigos(lista);
      Vector util= new Vector();
      util = ManActuacionesManager.getInstance().buscaActuacionesUtilizados(params);
      manActForm.setUtilizados(util);
      Vector utilizadosCerrados = new Vector();
      utilizadosCerrados = ManActuacionesManager.getInstance().buscaTemasUtilizadosCerrados(fechaCerradoE,fechaCerradoS,params);
      manActForm.setUtilizadosCerrados(utilizadosCerrados);

      /* Redirigimos al JSP de salida*/
      return (mapping.findForward(opcion));

    }else if(opcion.equals("eliminar")) {
        elRegistroESVO.setIdentDepart(cod_dep);
        elRegistroESVO.setUnidadOrgan(cod_uni);
        elRegistroESVO.setTipoReg("E");
        fRegCerradoE = RegistroAperturaCierreManager.getInstance().getFechaRegistroCerrado(elRegistroESVO,params);
        Fecha f = new Fecha();
        String fechaCerradoE = f.obtenerString(fRegCerradoE);
        elRegistroESVO.setTipoReg("S");
        fRegCerradoS = RegistroAperturaCierreManager.getInstance().getFechaRegistroCerrado(elRegistroESVO,params);
        String fechaCerradoS = f.obtenerString(fRegCerradoS);
        ManActuacionesForm actuacionesForm = (ManActuacionesForm)form;
        String identificador = request.getParameter("identificador");
        if(m_Log.isDebugEnabled()) m_Log.debug("el identificador es : " + identificador);
        manActVO.setIde(Integer.parseInt(identificador));
        if(m_Log.isDebugEnabled()) m_Log.debug("el identificador es : " + manActVO.getIde());
        Vector lista = new Vector();
        lista = ManActuacionesManager.getInstance().eliminarTema(manActVO,params);
        actuacionesForm.setCodigos(lista);
        Vector util= new Vector();
        util = ManActuacionesManager.getInstance().buscaActuacionesUtilizados(params);
        actuacionesForm.setUtilizados(util);
        Vector utilizadosCerrados = new Vector();
        utilizadosCerrados = ManActuacionesManager.getInstance().buscaTemasUtilizadosCerrados(fechaCerradoE,fechaCerradoS,params);
        actuacionesForm.setUtilizadosCerrados(utilizadosCerrados);
        return (mapping.findForward("vuelveCargar"));

      } else if(opcion.equals("modificar")) {
        elRegistroESVO.setIdentDepart(cod_dep);
        elRegistroESVO.setUnidadOrgan(cod_uni);
        elRegistroESVO.setTipoReg("E");
        fRegCerradoE = RegistroAperturaCierreManager.getInstance().getFechaRegistroCerrado(elRegistroESVO,params);
        Fecha f = new Fecha();
        String fechaCerradoE = f.obtenerString(fRegCerradoE);
        elRegistroESVO.setTipoReg("S");
        fRegCerradoS = RegistroAperturaCierreManager.getInstance().getFechaRegistroCerrado(elRegistroESVO,params);
        String fechaCerradoS = f.obtenerString(fRegCerradoS);
        ManActuacionesForm actuacionesForm = (ManActuacionesForm)form;
        String identificador = request.getParameter("identificador");
        if(m_Log.isDebugEnabled()) m_Log.debug("el identificador es : " + identificador);
        manActVO.setIde(Integer.parseInt(identificador));
        String codigo = request.getParameter("txtCodigo");
        String descripcion = request.getParameter("txtDescripcion");
/*        String diaDesde = request.getParameter("diaDesde");
        String mesDesde = request.getParameter("mesDesde");
        String anoDesde = request.getParameter("anoDesde");
        String diaHasta = request.getParameter("diaHasta");
        String mesHasta = request.getParameter("mesHasta");
        String anoHasta = request.getParameter("anoHasta");
		String fechaDesde = diaDesde + "/" + mesDesde + "/" + anoDesde;
*/		String inputFechaDesde = request.getParameter("inputFechaDesde");
		String inputFechaHasta= request.getParameter("inputFechaHasta");
		String fechaDesde = inputFechaDesde;

        String fechaHasta;
/*        if("".equals(diaHasta) || "".equals(mesHasta) || "".equals(anoHasta) ) {
          fechaHasta = null;
        }
        else {
          fechaHasta = diaHasta + "/" + mesHasta + "/" + anoHasta;
        }
*/		if ("".equals(inputFechaHasta) )
	          fechaHasta = null;
		else fechaHasta = inputFechaHasta;
		
        manActVO.setCodigo(codigo);
        manActVO.setDescripcion(descripcion);
        manActVO.setFechaDesde(fechaDesde);
        manActVO.setFechaHasta(fechaHasta);
        Vector lista = new Vector();
        lista = ManActuacionesManager.getInstance().modificarTema(manActVO,params);
        actuacionesForm.setCodigos(lista);
        Vector util= new Vector();
        util = ManActuacionesManager.getInstance().buscaActuacionesUtilizados(params);
        actuacionesForm.setUtilizados(util);
        Vector utilizadosCerrados = new Vector();
        utilizadosCerrados = ManActuacionesManager.getInstance().buscaTemasUtilizadosCerrados(fechaCerradoE,fechaCerradoS,params);
        actuacionesForm.setUtilizadosCerrados(utilizadosCerrados);
        return (mapping.findForward("vuelveCargar"));

      } else if(opcion.equals("alta")) {
        elRegistroESVO.setIdentDepart(cod_dep);
        elRegistroESVO.setUnidadOrgan(cod_uni);
        elRegistroESVO.setTipoReg("E");
        fRegCerradoE = RegistroAperturaCierreManager.getInstance().getFechaRegistroCerrado(elRegistroESVO,params);
        Fecha f = new Fecha();
        String fechaCerradoE = f.obtenerString(fRegCerradoE);
        elRegistroESVO.setTipoReg("S");
        fRegCerradoS = RegistroAperturaCierreManager.getInstance().getFechaRegistroCerrado(elRegistroESVO,params);
        String fechaCerradoS = f.obtenerString(fRegCerradoS);
        ManActuacionesForm actuacionesForm = (ManActuacionesForm)form;
        String codigo = request.getParameter("txtCodigo");
        String descripcion = request.getParameter("txtDescripcion");
/*        String diaDesde = request.getParameter("diaDesde");
        String mesDesde = request.getParameter("mesDesde");
        String anoDesde = request.getParameter("anoDesde");
        String diaHasta = request.getParameter("diaHasta");
        String mesHasta = request.getParameter("mesHasta");
        String anoHasta = request.getParameter("anoHasta");
		String fechaDesde = diaDesde + "/" + mesDesde + "/" + anoDesde;
*/		String inputFechaDesde = request.getParameter("inputFechaDesde");
		String inputFechaHasta= request.getParameter("inputFechaHasta");
		String fechaDesde = inputFechaDesde;

        String fechaHasta;
/*        if("".equals(diaHasta) || "".equals(mesHasta) || "".equals(anoHasta) ) {
          fechaHasta = null;
        }
        else {
          fechaHasta = diaHasta + "/" + mesHasta + "/" + anoHasta;
        }
*/		if ("".equals(inputFechaHasta) )
	          fechaHasta = null;
		else fechaHasta = inputFechaHasta;
		
        manActVO.setCodigo(codigo);
        manActVO.setDescripcion(descripcion);
        manActVO.setFechaDesde(fechaDesde);
        manActVO.setFechaHasta(fechaHasta);
        Vector lista = new Vector();
        lista = ManActuacionesManager.getInstance().altaTema(manActVO,params);
        if(m_Log.isDebugEnabled()) m_Log.debug("el tamaño del vector lista en el action es : " + lista.size());
        actuacionesForm.setCodigos(lista);
        Vector util= new Vector();
        util = ManActuacionesManager.getInstance().buscaActuacionesUtilizados(params);
        actuacionesForm.setUtilizados(util);
        Vector utilizadosCerrados = new Vector();
        utilizadosCerrados = ManActuacionesManager.getInstance().buscaTemasUtilizadosCerrados(fechaCerradoE,fechaCerradoS,params);
        actuacionesForm.setUtilizadosCerrados(utilizadosCerrados);
        return (mapping.findForward("vuelveCargar"));

      } else if(opcion.equals("salir")){

      if ((session.getAttribute("ManActuacionesForm") != null))
        session.removeAttribute("ManActuacionesForm");
      /* Redirigimos al JSP de salida*/
      return (mapping.findForward(opcion));

      }else{

      return (new ActionForward(mapping.getInput()));

    }
  }

}