package es.altia.agora.interfaces.user.web.sge;

// PAQUETES IMPORTADOS
import java.io.IOException;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import es.altia.agora.interfaces.user.web.helper.ActionHelper;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.sge.persistence.CargosManager;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Manuel Vera Silvestre
 * @version 1.0
 */

public class CargosAction extends ActionSession{

   protected static Log log =
           LogFactory.getLog(CargosAction.class.getName());

   public ActionForward performSession(ActionMapping mapping,ActionForm form,
	   HttpServletRequest request,HttpServletResponse response)
	   throws IOException, ServletException{
	if(m_Log.isInfoEnabled()) m_Log.info("En CargosAction");
	ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));
	ActionErrors errors = new ActionErrors();
	// Validaremos los parametros del request especificados
	HttpSession session = request.getSession();
	String opcion = request.getParameter("opcion");
	if(m_Log.isInfoEnabled()) m_Log.info("opción: " + opcion);
	UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
	String[] params = usuario.getParamsCon();
	if ("cargar".equalsIgnoreCase(opcion)){
	   if(m_Log.isDebugEnabled()) m_Log.debug("entro en cargar");
	   if (form == null) {
		form = new CargosForm();
		if ("request".equals(mapping.getScope())){
		   request.setAttribute(mapping.getAttribute(), form);
		   if(m_Log.isDebugEnabled()) m_Log.debug("el form está en el request");
		}
		else{
		   session.setAttribute(mapping.getAttribute(), form);
		   if(m_Log.isDebugEnabled()) m_Log.debug("el form está en el session");
		}
	   }
	   Vector lista = CargosManager.getInstance().loadCargos(params);
	   if(m_Log.isDebugEnabled()) m_Log.debug("El vector:" + lista.toString());
	   ((CargosForm)form).setLista(lista);
	   if(m_Log.isDebugEnabled()) m_Log.debug("El vector en el form: " + ((CargosForm)form).getLista().toString());
	   if(m_Log.isDebugEnabled()) m_Log.debug("Salgo del cargar");
	}
	else if("eliminar".equalsIgnoreCase(opcion)){
	   if(m_Log.isDebugEnabled()) m_Log.debug("entro en eliminar");
	   CargosForm cargosForm = (CargosForm)form;
	   String identificador = request.getParameter("identificador");
	   //if(m_Log.isInfoEnabled()) m_Log.info("el identificador es : " + identificador);
	   Vector lista = new Vector();
	   lista = CargosManager.getInstance().eliminarCargo(identificador,params);
	   cargosForm.setLista(lista);
	   opcion = "vuelveCargar";
	   if(m_Log.isDebugEnabled()) m_Log.debug("salgo del eliminar");
	}
	else if("modificar".equals(opcion)){
	   if(m_Log.isDebugEnabled()) m_Log.debug("entro en modificar");
	   CargosForm cargosForm = (CargosForm)form;
	   GeneralValueObject gVO = new GeneralValueObject();
	   gVO.setAtributo("cod",request.getParameter("txtCodigo"));
	   gVO.setAtributo("desc",request.getParameter("txtDescripcion"));
	   gVO.setAtributo("tipo",request.getParameter("descTipo"));
	   gVO.setAtributo("cargo",request.getParameter("cargo"));
	   gVO.setAtributo("tratam",request.getParameter("tratam"));
	   //gVO.setAtributo("sit",request.getParameter("sit"));
	   gVO.setAtributo("sit","A");
	   Vector lista = CargosManager.getInstance().modificarCargo(gVO,params);
	   cargosForm.setLista(lista);
	   opcion = "vuelveCargar";
	   if(m_Log.isDebugEnabled()) m_Log.debug("salgo del modificar");
	}
	else if("alta".equals(opcion)){
	   if(m_Log.isDebugEnabled()) m_Log.debug("entro en el alta");
	   CargosForm cargosForm = (CargosForm)form;
	   GeneralValueObject gVO = new GeneralValueObject();
	   gVO.setAtributo("cod",request.getParameter("txtCodigo"));
	   gVO.setAtributo("desc",request.getParameter("txtDescripcion"));
	   gVO.setAtributo("tipo",request.getParameter("descTipo"));
	   gVO.setAtributo("cargo",request.getParameter("cargo"));
	   gVO.setAtributo("tratam",request.getParameter("tratam"));
	   //gVO.setAtributo("sit",request.getParameter("sit"));
	   gVO.setAtributo("sit","A");
	   Vector lista = CargosManager.getInstance().altaCargo(gVO,params);
	   cargosForm.setLista(lista);
	   opcion = "vuelveCargar";
	   if(m_Log.isDebugEnabled()) m_Log.debug("salgo del alta");
	}
	else if("salir".equals(opcion)){
	   if(m_Log.isDebugEnabled()) m_Log.debug("entro en salir");
	   if ((session.getAttribute(mapping.getAttribute()) != null))
		session.removeAttribute(mapping.getAttribute());
	   if(m_Log.isDebugEnabled()) m_Log.debug("salgo del salir");
	}
	else
	   opcion = mapping.getInput();

	return (mapping.findForward(opcion));
   }
}