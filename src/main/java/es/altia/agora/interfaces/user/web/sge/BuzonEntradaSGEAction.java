package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.InicioExpedienteValueObject;
import es.altia.agora.business.sge.persistence.BuzonEntradaSGEManager;
import es.altia.agora.interfaces.user.web.helper.ActionHelper;
import es.altia.agora.interfaces.user.web.sge.BuzonEntradaSGEForm;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

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

public final class BuzonEntradaSGEAction extends ActionSession {
	protected static Log m_Log =
            LogFactory.getLog(BuzonEntradaSGEAction.class.getName());

    public ActionForward performSession(	ActionMapping mapping,
											ActionForm form,
											HttpServletRequest request,
											HttpServletResponse response) throws IOException, ServletException{

		m_Log.debug("perform");
		ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));
        ActionErrors errors = new ActionErrors();		
		HttpSession session = request.getSession();		
		UsuarioValueObject usuarioVO = null;
		String[] params = null;
		
		BuzonEntradaSGEForm buzonForm = (BuzonEntradaSGEForm)form;
		if(session.getAttribute("usuario") != null){
			usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");	
			params = usuarioVO.getParamsCon();
		}
		String opcion = request.getParameter("opcion");							

		if("cargar".equals(opcion)){
			// Rellenamos el form de BuzonEntradaSGEForm
			if (form == null) {								
				m_Log.debug("Rellenamos el form de BuzonEntradaSGEForm");
				form = new BuzonEntradaSGEForm();
				if ("request".equals(mapping.getScope()))
					request.setAttribute(mapping.getAttribute(), form);
				else
					session.setAttribute(mapping.getAttribute(), form);
			}				

			Vector lista= new Vector();
			lista = BuzonEntradaSGEManager.getInstance().loadIniciados(usuarioVO);					
			buzonForm.setIniciados(lista);		

			String recargar = request.getParameter("recargar");	
			if("true".equals(recargar)) opcion = "recargar";

		}else if("aceptar".equals(opcion)){
			Vector listaInicio = new Vector();
			listaInicio = obtenerVector(request.getParameter("departamentoRes"), request.getParameter("unidadOrgRes"), request.getParameter("ejercicioRes"), request.getParameter("numeroRes"), request.getParameter("tipoRes"), "0");
			int resultado = BuzonEntradaSGEManager.getInstance().modifyEstado(listaInicio, params);

			if ((resultado != -1)&&(listaInicio.size() == (resultado+1))) opcion = "aceptar";
			else opcion = "aceptarError";

		}else if("rechazar".equals(opcion)){
			Vector listaInicio = new Vector();
			listaInicio = obtenerVector(request.getParameter("departamentoRes"), request.getParameter("unidadOrgRes"), request.getParameter("ejercicioRes"), request.getParameter("numeroRes"), request.getParameter("tipoRes"), "2");
			int resultado = BuzonEntradaSGEManager.getInstance().modifyEstado(listaInicio, params);			
			
			if ((resultado != -1)&&(listaInicio.size() == (resultado+1))) opcion = "rechazar";
			else opcion = "rechazarError";
		}
			
		return (mapping.findForward(opcion));			
		
	}

	private Vector obtenerVector(String listaDepartamentoRes, String listaUnidadOrgRes, String listaEjercicioRes, String listaNumeroRes, String listaTipoRes, String estado) {

		Vector lista = new Vector();
		StringTokenizer departamentoRes = null;
		StringTokenizer unidadOrgRes = null;
		StringTokenizer ejercicioRes = null;
		StringTokenizer numeroRes = null;
		StringTokenizer tipoRes = null;				
		
		if (listaDepartamentoRes!= null) {
			departamentoRes = new StringTokenizer(listaDepartamentoRes,"зе,",false);
			unidadOrgRes = new StringTokenizer(listaUnidadOrgRes,"зе,",false);
			ejercicioRes = new StringTokenizer(listaEjercicioRes,"зе,",false);
			numeroRes = new StringTokenizer(listaNumeroRes,"зе,",false);
			tipoRes = new StringTokenizer(listaTipoRes,"зе,",false);
			while(departamentoRes.hasMoreTokens()) {
				InicioExpedienteValueObject inicioVO = new InicioExpedienteValueObject();
				inicioVO.setDepartamentoRes(departamentoRes.nextToken());
				inicioVO.setUnidadOrgRes(unidadOrgRes.nextToken());
				inicioVO.setEjercicioRes(ejercicioRes.nextToken());
				inicioVO.setNumeroRes(numeroRes.nextToken());
				inicioVO.setTipoRes(tipoRes.nextToken());	
				inicioVO.setEstado(estado);				
				lista.addElement(inicioVO);
			}
		}		
		return lista;
	  }
}
