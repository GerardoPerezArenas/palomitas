package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.InicioExpedienteValueObject;
import es.altia.agora.business.sge.persistence.InicioExpedienteManager;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.persistence.TercerosManager;
import es.altia.agora.interfaces.user.web.helper.ActionHelper;
import es.altia.agora.interfaces.user.web.sge.InicioExpedienteForm;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import java.io.IOException;

import java.util.Iterator;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public final class InicioExpedienteAction extends ActionSession {
	protected static Log m_Log =
            LogFactory.getLog(InicioExpedienteAction.class.getName());

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
		if(session.getAttribute("usuario") != null){
			usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");	
			params = usuarioVO.getParamsCon();
		}

		InicioExpedienteForm inicioForm = (InicioExpedienteForm)form;

		String opcion = request.getParameter("opcion");							

		if("cargar".equals(opcion)){
			// Rellenamos el form de InicioExpedienteForm
			if (form == null) {								
				m_Log.debug("Rellenamos el form de InicioExpedienteForm");
				form = new InicioExpedienteForm();
				if ("request".equals(mapping.getScope()))
					request.setAttribute(mapping.getAttribute(), form);
				else
					session.setAttribute(mapping.getAttribute(), form);
			}				
			
			InicioExpedienteValueObject inicioVO = new InicioExpedienteValueObject();	

			inicioVO.setRegistro(request.getParameter("registro"));
			inicioVO.setDescProcedimiento(request.getParameter("nomProcedimiento"));			
			inicioVO.setFechaInicio(request.getParameter("fechaInicio"));	
			inicioVO.setTercero(request.getParameter("tercero"));
			inicioVO.setVersion(request.getParameter("version"));		

			inicioVO.setCodMunicipio(request.getParameter("codMunicipio"));//El mun sera igual a la org del usuario.
			inicioVO.setCodProcedimiento(request.getParameter("codProcedimiento"));
			inicioVO.setEjercicio(request.getParameter("ejercicio"));
			inicioVO.setNumero(request.getParameter("numero"));

			inicioVO.setDepartamentoRes(request.getParameter("departamentoRes"));
			inicioVO.setUnidadOrgRes(request.getParameter("unidadOrgRes"));
			inicioVO.setEjercicioRes(request.getParameter("ejercicioRes"));
			inicioVO.setNumeroRes(request.getParameter("numeroRes"));
			inicioVO.setTipoRes(request.getParameter("tipoRes"));

			inicioVO.setTercero(request.getParameter("tercero"));
			inicioVO.setVersion(request.getParameter("version"));
			
			TercerosValueObject tercerosVO = new TercerosValueObject();
			tercerosVO.setIdentificador(request.getParameter("tercero"));
			tercerosVO.setVersion(request.getParameter("version"));	
			tercerosVO.setIdDomicilio(request.getParameter("domicilio"));
			
			Vector datosTercero= new Vector();
			datosTercero = TercerosManager.getInstance().getByHistorico(tercerosVO, params);	
			Iterator iterTer = datosTercero.iterator();
			while(iterTer.hasNext()){
				TercerosValueObject terceroVO = (TercerosValueObject)iterTer.next();
				inicioVO.setCodTipoDocumento(terceroVO.getTipoDocumento());
				inicioVO.setDesTipoDocumento("");
				inicioVO.setDocumento(terceroVO.getDocumento());
				String solicitante = terceroVO.getNombre();
				if((terceroVO.getApellido1() != null)&&(terceroVO.getApellido2() != null)){
					solicitante = terceroVO.getApellido1() + " " + terceroVO.getApellido2() + ", " + solicitante;
				}
				inicioVO.setNombre(solicitante);
				inicioVO.setTelefono(terceroVO.getTelefono());
				inicioVO.setEmail(terceroVO.getEmail());

				Vector datosDomicilio = new Vector();
				datosDomicilio = terceroVO.getDomicilios();	
				Iterator iterDom = datosDomicilio.iterator();
				while(iterDom.hasNext()){
					DomicilioSimpleValueObject domicilioVO = (DomicilioSimpleValueObject)iterDom.next();
					inicioVO.setProvincia(domicilioVO.getProvincia());
					inicioVO.setMunicipio(domicilioVO.getMunicipio());
					inicioVO.setDomicilio(domicilioVO.getDomicilio());
					inicioVO.setPoblacion(domicilioVO.getBarriada());
					inicioVO.setCodigoPostal(domicilioVO.getCodigoPostal());
				}
			}

			inicioForm.setInicioExpedienteVO(inicioVO);

			Vector procedimientos = new Vector();		
			procedimientos = InicioExpedienteManager.getInstance().loadProcedimientos(usuarioVO);

			inicioForm.setProcedimientos(procedimientos);
			
		}else if("iniciar".equals(opcion)){				
			int insertados = -1;
			InicioExpedienteValueObject inicioVO = inicioForm.getInicioExpedienteVO();
			inicioVO.setEstado("0");

			String iniciado = request.getParameter("iniciado");
			if("true".equals(iniciado)){
				inicioVO.setIniciado("true");				
				insertados = InicioExpedienteManager.getInstance().insertExpediente(inicioVO, params);	
			}else{
				inicioVO.setIniciado("false");	
				inicioVO.setCodMunicipio(String.valueOf(usuarioVO.getOrgCod()));
				insertados = InicioExpedienteManager.getInstance().insertExpediente(inicioVO, params);	
			}			
			
			inicioForm.setInicioExpedienteVO(inicioVO);
			opcion = "salir";
		}

		return (mapping.findForward(opcion));
	}
}