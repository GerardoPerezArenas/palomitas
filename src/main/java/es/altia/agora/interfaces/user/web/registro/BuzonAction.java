// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.registro;

// CLASES IMPORTADAS
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.persistence.AnotacionRegistroManager;
import es.altia.agora.business.registro.persistence.BuzonManager;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.persistence.TercerosManager;
import es.altia.agora.interfaces.user.web.helper.ActionHelper;
import es.altia.agora.interfaces.user.web.terceros.BusquedaTercerosForm;
import es.altia.agora.interfaces.user.web.util.ActionSession;
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
 * @author Manuel Vera Silvestre
 * @version 1.0
 */

public class BuzonAction extends ActionSession{
   protected static Log m_Log =
           LogFactory.getLog(BuzonAction.class.getName());

   public ActionForward performSession(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
     throws IOException, ServletException{

  m_Log.debug("perform");
  if(m_Log.isDebugEnabled()) m_Log.debug("Llego al buzón");
  ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));//no se para q sirve
  HttpSession session = request.getSession();//cojo la sesión
  if(m_Log.isDebugEnabled()) m_Log.debug("voy a por el usuario");
  UsuarioValueObject uvo = (UsuarioValueObject)session.getAttribute("usuario");
  String[] parametros = uvo.getParamsCon();
  if(m_Log.isDebugEnabled()) m_Log.debug("voy a por acceso");
  String acceso = request.getParameter("acceso");
  if(m_Log.isDebugEnabled()) m_Log.debug("valor del acceso: " + acceso);
  if(acceso.equalsIgnoreCase("consultar")){
     if(m_Log.isDebugEnabled()) m_Log.debug("entro en consultar");
     if(form == null){//si no hay form
    form = new BuzonForm();//lo creo
    if ("request".equals(mapping.getScope()))//si estoy en el request
       request.setAttribute(mapping.getAttribute(), form);//lo pongo aqui
    else//si no
       session.setAttribute(mapping.getAttribute(), form);//lo pongo aqui
     }
     //a continuacion cojo los datos q me hacen falta para pasar al manager
     // y relleno el vector a partir de ellos
     Vector v = BuzonManager.getInstance().rellenarBuzon(uvo.getOrgCod(),
       uvo.getOrgCod(), uvo.getDepCod(), uvo.getUnidadOrgCod(),parametros);
     BuzonForm bf = (BuzonForm)form;
     bf.setBuzon(v);//ya tengo toda la informacion en el form
     if(v.isEmpty())
    bf.setFilas(false);
     else
    bf.setFilas(true);
     if(m_Log.isDebugEnabled()) m_Log.debug("salgo del consultar");
  }
  else if(acceso.equalsIgnoreCase("procesar")){
     //traigo los códigos del request
     Integer aux = new Integer(request.getParameter("eje"));
     int eje = aux.intValue();
     if(m_Log.isDebugEnabled()) m_Log.debug("eje:"+eje);
     aux = new Integer(request.getParameter("num"));
     Long num = new Long(aux);
     if(m_Log.isDebugEnabled()) m_Log.debug("num:"+num);
     String tip = request.getParameter("tip");
     if(m_Log.isDebugEnabled()) m_Log.debug("tip:"+tip);
     //aux = new Integer(request.getParameter("orgcod"));
     //int org = aux.intValue();
     String org = (String) request.getParameter("orgcod");
     if(m_Log.isDebugEnabled()) m_Log.debug("orgcod:"+org);
     aux = new Integer(request.getParameter("entcod"));
     int ent = aux.intValue();
     if(m_Log.isDebugEnabled()) m_Log.debug("entcod:"+ent);
     aux = new Integer(request.getParameter("depcod"));
     int dep = aux.intValue();
     if(m_Log.isDebugEnabled()) m_Log.debug("depcod:"+dep);
     aux = new Integer(request.getParameter("uorcod"));
     int uor = aux.intValue();
     if(m_Log.isDebugEnabled()) m_Log.debug("uorcod:"+uor);
     
     //creo un nuevo RegistroValueObject
     RegistroValueObject rvo = new RegistroValueObject();
     //lo inicializo
     rvo.setAnoReg(eje);
     rvo.setNumReg(num);
     rvo.setTipoReg(tip);
     rvo.setIdentDepart(dep);
     rvo.setUnidadOrgan(uor);
     //lo relleno
     AnotacionRegistroManager.getInstance().getByPrimaryKey(rvo,parametros);
     //relleno los campos de origen
     rvo.setOrganizacionOrigen(org);
     rvo.setNomOrganizacionOrigen(request.getParameter("orgdes"));
     rvo.setOrgEntOrigen(ent);
     rvo.setNomEntidadOrigen(request.getParameter("entnom"));
     rvo.setIdDepOrigen(dep);
     rvo.setNomDptoOrigen(request.getParameter("depnom"));
     String uorOrigen = Integer.toString(uor);
     rvo.setIdUndRegOrigen(uorOrigen);
     rvo.setNomUniRegOrigen(request.getParameter("uornom"));
     String ejercicio = Integer.toString(eje);
     rvo.setEjeOrigen(ejercicio);
     String numeroOrigen = Long.toString(num);
     rvo.setNumOrigen(numeroOrigen);
     rvo.setTipoRegOrigen(rvo.getTipoReg());
     //inicializo las listas de tipos
     rvo.setListaTiposDocumentos(new Vector());
     rvo.setListaTiposRemitentes(new Vector());
     rvo.setListaTiposTransportes(new Vector());
     rvo.setListaActuaciones(new Vector());
     rvo.setListaTemas(new Vector());
     //marco q he pasado por el buzon
     rvo.setHayBuzon(true);
     //dejo en blanco la PK
     rvo.setAnoReg(-1);
     rvo.setNumReg(-1L);
     rvo.setTipoReg(null);
     rvo.setIdentDepart(-1);
     rvo.setUnidadOrgan(-1);
     MantAnotacionRegistroForm registroForm = (MantAnotacionRegistroForm)session.getAttribute("MantAnotacionRegistroForm");
     if (registroForm == null){
    registroForm = new MantAnotacionRegistroForm();
    session.setAttribute("MantAnotacionRegistroForm",registroForm);
     }
     registroForm.setRegistro(rvo);
     //if(m_Log.isInfoEnabled()) m_Log.info("ya tengo el form");
     //completo la informacion del tercero
     TercerosValueObject tvo = new TercerosValueObject();
     tvo.setIdDomicilio(String.valueOf(rvo.getDomicInter()));
     tvo.setIdentificador(String.valueOf(rvo.getCodInter()));
     tvo.setVersion(String.valueOf(rvo.getNumModInfInt()));
     Vector tv = TercerosManager.getInstance().getByHistorico(tvo,parametros);
     BusquedaTercerosForm BTForm = new BusquedaTercerosForm();
     BTForm.setListaTerceros(tv);
     session.setAttribute("BusquedaTercerosForm",BTForm);
     if(m_Log.isDebugEnabled()){
         m_Log.debug("Origen:");
         m_Log.debug("organizacion: "+rvo.getOrganizacionOrigen());
         m_Log.debug(rvo.getNomOrganizacionOrigen());
         m_Log.debug("entidad: "+rvo.getOrgEntOrigen());
         m_Log.debug(rvo.getNomEntidadOrigen());
         m_Log.debug("departamento: "+rvo.getIdDepOrigen());
         m_Log.debug(rvo.getNomDptoOrigen());
         m_Log.debug("unidad: "+rvo.getIdUndRegOrigen());
         m_Log.debug(rvo.getNomUniRegOrigen());
         m_Log.debug("ejercicio: "+rvo.getEjeOrigen());
         m_Log.debug("numero: "+rvo.getNumOrigen());
         m_Log.debug("tipo: "+rvo.getTipoRegOrigen());
     }

  }
  return (mapping.findForward(acceso));//redirijo al jsp
   }
}
