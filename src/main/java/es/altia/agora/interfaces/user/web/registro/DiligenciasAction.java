// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.registro;

// PAQUETES IMPORTADOS
import es.altia.agora.business.escritorio.*;
import es.altia.agora.business.registro.*;
import es.altia.agora.business.registro.persistence.*;
import es.altia.agora.interfaces.user.web.helper.ActionHelper;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import java.util.Vector;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase DiligenciasAction</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class DiligenciasAction extends ActionSession {
  protected static Log m_Log =
          LogFactory.getLog(DiligenciasAction.class.getName());

  public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {
    m_Log.debug("perform");
    ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));
    HttpSession session = request.getSession();
    // Validaremos los parametros del request especificados
    ActionErrors errors = new ActionErrors();
    String opcion = request.getParameter("opcion");
    String fecha;
    String anotacion;

    if (m_Log.isInfoEnabled()) m_Log.info("la opcion en el action es " + opcion);

    // Rellenamos el form de Diligencias
    if (form == null) {
			m_Log.debug("Rellenamos el form de Diligencias");
			form = new DiligenciasForm();
			if ("request".equals(mapping.getScope())){
	  		request.setAttribute(mapping.getAttribute(), form);
			}else{
	  		session.setAttribute(mapping.getAttribute(), form);
			}
    }

    DiligenciasForm diligenciasForm = (DiligenciasForm)form;
    DiligenciasValueObject diligVO = new DiligenciasValueObject();
    DiligenciasManager diligMan = DiligenciasManager.getInstance();
    String subopcion = opcion.substring(0,6);
    if(m_Log.isDebugEnabled()) m_Log.debug("subopcion: " + subopcion);
    char tipo = opcion.charAt(6);
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    String[] params = usuario.getParamsCon();
    RegistroUsuarioValueObject usuarioR=(RegistroUsuarioValueObject)session.getAttribute("registroUsuario");
    int codDepto = usuarioR.getDepCod();
    int codUnidad = usuarioR.getUnidadOrgCod();
    /*// Comprobar q REGISTRO esta abierto.
    RegistroValueObject elRegistroESVO = new RegistroValueObject();
    elRegistroESVO.setIdentDepart(codDepto);
    elRegistroESVO.setUnidadOrgan(codUnidad);
    elRegistroESVO.setTipoReg(String.valueOf(tipo));
    Date fRegAbierto = RegistroAperturaCierreManager.getInstance().getFechaRegistroAbierto(elRegistroESVO,params);
    Fecha f=new Fecha();
    if (fRegAbierto != null) {*/
    diligVO.setCodDepto(codDepto);
    diligVO.setCodUnidad(codUnidad);
    diligVO.setFecha(request.getParameter("fecha"));//f.obtenerString(fRegAbierto));
    diligVO.setTipo(tipo);
    int correcto=0;
    if (subopcion.equals("buscar")){
      diligVO.setAnotacion(request.getParameter("txtAnotacion"));
      diligVO.setAnotacionBuscada(request.getParameter("txtAnotacion"));
      diligVO.setFechaBuscada(request.getParameter("fecha"));
      Vector lista = new Vector();
      lista=diligMan.loadVector(diligVO,params);
      if(m_Log.isDebugEnabled()) m_Log.debug("el tamaño de la lista es : " + lista.size());
      diligVO.setListaDiligencias(lista);
    } else if (subopcion.equals("listad")){
      diligVO.setFecha(request.getParameter("fechaAnotacionBuscada"));
      diligVO.setAnotacion(request.getParameter("txtAnotacionBuscada"));
      diligVO.setAnotacionBuscada(request.getParameter("txtAnotacionBuscada"));
      diligVO.setFechaBuscada(request.getParameter("fechaAnotacionBuscada"));
      Vector lista = new Vector();
      lista=diligMan.loadVector(diligVO,params);
      if(m_Log.isDebugEnabled()) m_Log.debug("el tamaño de la lista es : " + lista.size());
      diligVO.setListaDiligencias(lista);
      subopcion = "buscar";
    } else if (subopcion.equals("grabar")){
      diligVO.setAnotacion(request.getParameter("txtAnotacion"));
      if (request.getParameter("buscado").equals("SI")){
        correcto = diligMan.modify(diligVO,params);
      }else{
        correcto = diligMan.insert(diligVO,params);
      }
    } else if(subopcion.equals("recarE") || subopcion.equals("recarS")) {
    	diligVO.setAnotacion(request.getParameter("txtAnotacion"));	
    	diligVO.setAnotacionBuscada(request.getParameter("txtAnotacionBuscada"));
    	diligVO.setFechaBuscada(request.getParameter("fechaAnotacionBuscada"));	
    } else if(subopcion.equals("iniciE") || subopcion.equals("iniciS")) {
    	diligVO.setAnotacion(request.getParameter(""));	
	    diligVO.setFecha("");
    } else{ // borrar
      correcto = diligMan.delete(diligVO,params);
    }
    //}else subopcion="registroCerrado";
    /* Asignamos el ValueObject al formulario*/
    diligenciasForm.setCorrecto(correcto);
    diligenciasForm.setDiligencia(diligVO);

    /* Redirigimos al JSP de salida*/
    return (mapping.findForward(subopcion));
  }
}