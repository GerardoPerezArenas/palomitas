package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.ConfInformeValueObject;
import es.altia.agora.business.sge.persistence.ConfInformeManager;
import es.altia.agora.interfaces.user.web.helper.ActionHelper;
import es.altia.agora.interfaces.user.web.sge.ConfInformeForm;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import java.io.IOException;

import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public final class ConfInformeAction extends ActionSession {
	protected static Log m_Log =
            LogFactory.getLog(ConfInformeAction.class.getName());

    public ActionForward performSession(	ActionMapping mapping,
											ActionForm form,
											HttpServletRequest request,
											HttpServletResponse response) throws IOException, ServletException{


  String lStrParam1   = "";
  String lStrParam2   = "";
  String lStrParam3   = "";
  String lStrParam4   = "";
  String lStrParam5   = "";
  String lStrParam6   = "";

  String lStrParamV1   = "";
  String lStrParamV2   = "";
  String lStrParamV3   = "";
  String lStrParamV4   = "";
  String lStrParamV5   = "";
  String lStrParamV6   = "";

  String lStrArea     = "ARE";
  String lStrUnidad   = "UTR";
  String lStrTipoProc = "TPR";
  String lStrProc     = "PRO";
  String lStrTipoTram = "CLS";
  String lStrTramite  = "TRA";  

  int lIntIdxArea     = 0;  
  int lIntIdxUnidad   = 0;
  int lIntIdxTipoProc = 0;
  int lIntIdxProc     = 0;
  int lIntIdxTipoTram = 0;
  int lIntIdxTramite  = 0;

  m_Log.debug("======================== ConfInformeAction ===================>");
		ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));
        ActionErrors errors = new ActionErrors();		
		HttpSession session = request.getSession();		
		UsuarioValueObject usuarioVO = null;
		String[] params = null;
	
  String lStrCrit1   = request.getParameter("criterio1");
  String lStrCrit2   = request.getParameter("criterio2");
  String lStrCrit3   = request.getParameter("criterio3");
  String lStrCrit4   = request.getParameter("criterio4");
  String lStrCrit5   = request.getParameter("criterio5");
  String lStrCrit6   = request.getParameter("criterio6");

  String lIntValor1  = request.getParameter("valor1");
  String lIntValor2  = request.getParameter("valor2");
  String lIntValor3  = request.getParameter("valor3");
  String lIntValor4  = request.getParameter("valor4");
  String lIntValor5  = request.getParameter("valor5");
  String lIntValor6  = request.getParameter("valor6");
  

  if       (lStrCrit1.equals(lStrArea)){lStrParam1 = lStrCrit1; lStrParamV1 = lIntValor1;}
  else {if (lStrCrit2.equals(lStrArea)){lStrParam1 = lStrCrit2; lStrParamV1 = lIntValor2;}
  else {if (lStrCrit3.equals(lStrArea)){lStrParam1 = lStrCrit3; lStrParamV1 = lIntValor3;}
  else {if (lStrCrit4.equals(lStrArea)){lStrParam1 = lStrCrit4; lStrParamV1 = lIntValor4;}
  else {if (lStrCrit5.equals(lStrArea)){lStrParam1 = lStrCrit5; lStrParamV1 = lIntValor5;}
  else {if (lStrCrit6.equals(lStrArea)){lStrParam1 = lStrCrit6; lStrParamV1 = lIntValor6;}
  else {lStrParam1 = ""; lStrParamV1 = "";}}}}}}

  if       (lStrCrit1.equals(lStrUnidad)){lStrParam2 = lStrCrit1; lStrParamV2 = lIntValor1;}
  else {if (lStrCrit2.equals(lStrUnidad)){lStrParam2 = lStrCrit2; lStrParamV2 = lIntValor2;}
  else {if (lStrCrit3.equals(lStrUnidad)){lStrParam2 = lStrCrit3; lStrParamV2 = lIntValor3;}
  else {if (lStrCrit4.equals(lStrUnidad)){lStrParam2 = lStrCrit4; lStrParamV2 = lIntValor4;}
  else {if (lStrCrit5.equals(lStrUnidad)){lStrParam2 = lStrCrit5; lStrParamV2 = lIntValor5;}
  else {if (lStrCrit6.equals(lStrUnidad)){lStrParam2 = lStrCrit6; lStrParamV2 = lIntValor6;}
  else {lStrParam2 = ""; lStrParamV2 = "";}}}}}}

  if       (lStrCrit1.equals(lStrTipoProc)){lStrParam3 = lStrCrit1; lStrParamV3 = lIntValor1;}
  else {if (lStrCrit2.equals(lStrTipoProc)){lStrParam3 = lStrCrit2; lStrParamV3 = lIntValor2;}
  else {if (lStrCrit3.equals(lStrTipoProc)){lStrParam3 = lStrCrit3; lStrParamV3 = lIntValor3;}
  else {if (lStrCrit4.equals(lStrTipoProc)){lStrParam3 = lStrCrit4; lStrParamV3 = lIntValor4;}
  else {if (lStrCrit5.equals(lStrTipoProc)){lStrParam3 = lStrCrit5; lStrParamV3 = lIntValor5;}
  else {if (lStrCrit6.equals(lStrTipoProc)){lStrParam3 = lStrCrit6; lStrParamV3 = lIntValor6;}
  else {lStrParam3 = ""; lStrParamV3 = "";}}}}}}

  if       (lStrCrit1.equals(lStrProc)){lStrParam4 = lStrCrit1; lStrParamV4 = lIntValor1;}
  else {if (lStrCrit2.equals(lStrProc)){lStrParam4 = lStrCrit2; lStrParamV4 = lIntValor2;}
  else {if (lStrCrit3.equals(lStrProc)){lStrParam4 = lStrCrit3; lStrParamV4 = lIntValor3;}
  else {if (lStrCrit4.equals(lStrProc)){lStrParam4 = lStrCrit4; lStrParamV4 = lIntValor4;}
  else {if (lStrCrit5.equals(lStrProc)){lStrParam4 = lStrCrit5; lStrParamV4 = lIntValor5;}
  else {if (lStrCrit6.equals(lStrProc)){lStrParam4 = lStrCrit6; lStrParamV4 = lIntValor6;}
  else {lStrParam4 = ""; lStrParamV4 = "";}}}}}}

  if       (lStrCrit1.equals(lStrTipoTram)){lStrParam5 = lStrCrit1; lStrParamV5 = lIntValor1;}
  else {if (lStrCrit2.equals(lStrTipoTram)){lStrParam5 = lStrCrit2; lStrParamV5 = lIntValor2;}
  else {if (lStrCrit3.equals(lStrTipoTram)){lStrParam5 = lStrCrit3; lStrParamV5 = lIntValor3;}
  else {if (lStrCrit4.equals(lStrTipoTram)){lStrParam5 = lStrCrit4; lStrParamV5 = lIntValor4;}
  else {if (lStrCrit5.equals(lStrTipoTram)){lStrParam5 = lStrCrit5; lStrParamV5 = lIntValor5;}
  else {if (lStrCrit6.equals(lStrTipoTram)){lStrParam5 = lStrCrit6; lStrParamV5 = lIntValor6;}
  else {lStrParam5 = ""; lStrParamV5 = "";}}}}}}

  if       (lStrCrit1.equals(lStrTramite)){lStrParam6 = lStrCrit1; lStrParamV6 = lIntValor1;}
  else {if (lStrCrit2.equals(lStrTramite)){lStrParam6 = lStrCrit2; lStrParamV6 = lIntValor2;}
  else {if (lStrCrit3.equals(lStrTramite)){lStrParam6 = lStrCrit3; lStrParamV6 = lIntValor3;}
  else {if (lStrCrit4.equals(lStrTramite)){lStrParam6 = lStrCrit4; lStrParamV6 = lIntValor4;}
  else {if (lStrCrit5.equals(lStrTramite)){lStrParam6 = lStrCrit5; lStrParamV6 = lIntValor5;}
  else {if (lStrCrit6.equals(lStrTramite)){lStrParam6 = lStrCrit6; lStrParamV6 = lIntValor6;}
  else {lStrParam6 = ""; lStrParamV6 = "";}}}}}}

  // Rellenamos el form de ConfInformeForm 

		if (form == null) {								
			m_Log.debug("Rellenamos el form de ConfInformeForm");
			form = new ConfInformeForm();
			if ("request".equals(mapping.getScope()))
				request.setAttribute(mapping.getAttribute(), form);
			else
				session.setAttribute(mapping.getAttribute(), form);
		}				

		ConfInformeForm informeForm = (ConfInformeForm)form;

		if(session.getAttribute("usuario") != null){
			usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");	
			params = usuarioVO.getParamsCon();
		}

		String opcion = request.getParameter("opcion");							

        UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        String codUsuario         = Integer.toString(usuario.getIdUsuario());
        String codOrganizacion = Integer.toString(usuario.getOrgCod());

        if ("cargar".equals(opcion)) {
            String combo = request.getParameter("combo");
            ConfInformeValueObject confInformeVO = new ConfInformeValueObject(combo, 
                    lStrParam1, lStrParam2, lStrParam3, lStrParam4, lStrParam5, lStrParam6, 
                    lStrParamV1, lStrParamV2, lStrParamV3, lStrParamV4, lStrParamV5, lStrParamV6);
            //Vector lista = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
            Vector lista = ConfInformeManager.getInstance().cargarCombos(confInformeVO,codUsuario,codOrganizacion, params);
            informeForm.setOpciones(lista);
	}
			
        m_Log.debug("<======================== ConfInformeAction =====================");	
		return (mapping.findForward(opcion));			
		
	}
}
