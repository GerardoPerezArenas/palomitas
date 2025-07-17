package es.altia.agora.interfaces.user.web.gestionInformes;

import java.io.IOException;
import java.util.Iterator;
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

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.gestionInformes.AmbitoListaValueObject;
import es.altia.agora.business.gestionInformes.persistence.GestionInformesManager;
import es.altia.agora.business.gestionInformes.persistence.MantenimientoInformesManager;
import es.altia.agora.business.util.ElementoListaValueObject;
import es.altia.agora.interfaces.user.web.helper.ActionHelper;
import es.altia.agora.interfaces.user.web.util.ActionSession;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase ProcedimientosAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: ALTIA CONSULTORES & AYTOS CPD</p>
 * @author Jorge Hombre Tuñas
 * @version 1.0
 */


public class MantenimientoInformesAction extends ActionSession {

  public ActionForward performSession(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {

    m_Log.debug("en MantenimientoInformesAction");
    ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));
    HttpSession session = request.getSession();

    // Validaremos los parametros del request especificados
    ActionErrors errors = new ActionErrors();
    String opcion = request.getParameter("opcion");
    String codigoAmbit="";
    if (m_Log.isInfoEnabled()) m_Log.info("la opcion en MantenimientoInformesAction es " + opcion);

    // Rellenamos el form de BusquedaTerceros
    if (form == null) {
      m_Log.debug("Rellenamos el form de solicitudes");
      form = new MantenimientoInformesForm();
      if ("request".equals(mapping.getScope())){
        request.setAttribute(mapping.getAttribute(), form);
      }else{
        session.setAttribute(mapping.getAttribute(), form);
      }
    }
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    String[] params = usuario.getParamsCon();
    MantenimientoInformesForm solForm = (MantenimientoInformesForm)form;

   if ("administracion".equalsIgnoreCase(opcion)) {
	   m_Log.debug("Entramos en administracion");
	  
       Vector listaAmbitos = GestionInformesManager.getInstance().getListaAmbitos(params);
        
        for (Iterator it=listaAmbitos.iterator();it.hasNext();){
        	ElementoListaValueObject elvo = (ElementoListaValueObject)it.next();
        	m_Log.debug(elvo);
        }
        solForm.setListaAmbitos(listaAmbitos);
        Vector listaModoAmbitos = GestionInformesManager.getInstance().getListaModoAmbitos(params);
        
        for (Iterator it=listaModoAmbitos.iterator();it.hasNext();){
     	   ElementoListaValueObject elvo = (ElementoListaValueObject)it.next();
         	m_Log.debug(elvo);
         }
         solForm.setListaModoAmbitos(listaModoAmbitos);
    }else if ("administracionModo".equalsIgnoreCase(opcion)) {
    	 m_Log.debug("Entramos en administracon modo registro");
        Vector listaModoAmbitos = GestionInformesManager.getInstance().getListaModoAmbitos(params);
        
        Vector listaAmbitos = GestionInformesManager.getInstance().getListaAmbitos(params);
        
       for (Iterator it=listaModoAmbitos.iterator();it.hasNext();){
    	   ElementoListaValueObject elvo = (ElementoListaValueObject)it.next();
        	m_Log.debug(elvo);
        }

        solForm.setListaModoAmbitos(listaModoAmbitos);
        solForm.setListaAmbitos(listaAmbitos);
      //  solForm.setOrigen("");*/
    } else if ("administracionCampos".equalsIgnoreCase(opcion)) {
    	
   	 m_Log.debug("Entramos en administracon modo registro");
   	 String codigoAmbito = request.getParameter("codigoAmbito");
   	codigoAmbit=codigoAmbito;
	   m_Log.debug("Entramos en administracion "+codigoAmbito);
       Vector listaCampos = GestionInformesManager.getInstance().getListaCampos(codigoAmbito,params);
       solForm.setListaCampos(listaCampos);
       for (Iterator it=listaCampos.iterator();it.hasNext();){
	    	 AmbitoListaValueObject elvo1 = (AmbitoListaValueObject)it.next();
	        	m_Log.debug("datos    -"+elvo1.getCri());
	        }
       Vector listaAmbitos = GestionInformesManager.getInstance().getListaAmbitos(params);
       solForm.setListaAmbitos(listaAmbitos);
    	
   }else if ("altaCampo".equalsIgnoreCase(opcion)) {
   	 m_Log.debug("Entramos en administracon altaCampo");
	 String codigo = request.getParameter("codParamNew");
	 Integer cod;
	 String nome= request.getParameter("nomeParamNew");
	 String campo = request.getParameter("campoParamNew");
	 String tipo = request.getParameter("tipoParamNew");
	 String lonxitude = request.getParameter("lonxitudeParamNew");
	 Integer lon=Integer.parseInt(lonxitude);
	 String desc= request.getParameter("descParamNew");
	 Integer origen=Integer.parseInt(desc);
	 String nomeas = request.getParameter("nomeasParamNew");
	 String criterio = request.getParameter("criterioParamNew");
	 Integer cri=Integer.parseInt(criterio);
	
	 AmbitoListaValueObject elvo = new AmbitoListaValueObject();
    
     elvo.setNome(nome);
     elvo.setCampo(campo);
     elvo.setTipo(tipo);
     elvo.setLonxitude(lon);
     elvo.setOrigen(origen);
     elvo.setNomeas(nomeas);
     elvo.setCriterio(cri);
     try {
		cod=MantenimientoInformesManager.getInstance().insertarCampo(elvo, params);
		 elvo.setCodigo(cod);
	     m_Log.debug("********    -"+desc);
		 Vector listaCampos = GestionInformesManager.getInstance().getListaCampos(desc,params);
		 solForm.setListaCampos(listaCampos);
	     solForm.setOrigen("");
	
	     for (Iterator it=listaCampos.iterator();it.hasNext();){
	    	 AmbitoListaValueObject elvo1 = (AmbitoListaValueObject)it.next();
	        	m_Log.debug("datos    -"+elvo1.getNome());
	        }
	  
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  
   }else if ("eliminarCampo".equalsIgnoreCase(opcion)) {
   	String codigo = request.getParameter("codParamNew");
    String desc= request.getParameter("descParamNew");
    String nom= request.getParameter("nomParamNew");
    String resultado=null;
	Integer codigo1;
	codigo1=Integer.valueOf(codigo);
	m_Log.debug("******************    datos    -> "+codigo);
	m_Log.debug("******************    desc    -> "+desc);
	m_Log.debug("******************    nom    -> "+nom);
	
    try {
    	
		 resultado=MantenimientoInformesManager.getInstance().eliminarCampo(desc,nom,codigo1, params);
		m_Log.debug("******************    resultado    -> "+resultado);
		 Vector listaCampos = GestionInformesManager.getInstance().getListaCampos(desc,params);
		 solForm.setListaCampos(listaCampos);
		 solForm.setEliminado(resultado);
	     solForm.setOrigen("");
	     m_Log.debug("******************    LO QUE ENVIO A JSP    -> "+solForm.getEliminado());
	   /*  for (Iterator it=listaModoAmbitos.iterator();it.hasNext();){
	        	ElementoListaValueObject elvo1 = (ElementoListaValueObject)it.next();
	        	m_Log.debug("datos    -"+elvo1.getDescripcion());
	        }
	     */
	     // opcion = "modificar";
		  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     
     
    }else if ("modificarCampo".equalsIgnoreCase(opcion)) {
    	 m_Log.debug("Entramos en administracon altaCampo");
    	 String codigo = request.getParameter("codParamNew");
    	 Integer cod=Integer.parseInt(codigo);
    	 String nome= request.getParameter("nomeParamNew");
    	 String campo = request.getParameter("campoParamNew");
    	 String tipo = request.getParameter("tipoParamNew");
    	 String lonxitude = request.getParameter("lonxitudeParamNew");
    	 Integer lon=Integer.parseInt(lonxitude);
    	 String desc= request.getParameter("descParamNew");
    	 Integer origen=Integer.parseInt(desc);
    	 String nomeas = request.getParameter("nomeasParamNew");
    	 String criterio = request.getParameter("criterioParamNew");
    	 Integer cri=Integer.parseInt(criterio);
    	 
    	 m_Log.debug("******************    datos    -> "+origen);
    	 AmbitoListaValueObject elvo = new AmbitoListaValueObject();
         elvo.setCodigo(cod);
         elvo.setNome(nome);
         elvo.setCampo(campo);
         elvo.setTipo(tipo);
         elvo.setLonxitude(lon);
         elvo.setOrigen(origen);
         elvo.setNomeas(nomeas);
         elvo.setCriterio(cri);
         try {
    			MantenimientoInformesManager.getInstance().modificarCampo(elvo, params);
    			 Vector ListaCampos = GestionInformesManager.getInstance().getListaCampos(desc,params);
    			 solForm.setListaCampos(ListaCampos);
    		     solForm.setOrigen("");
    		   
    		/*     for (Iterator it=ListaCampos.iterator();it.hasNext();){
    		        	ElementoListaValueObject elvo1 = (ElementoListaValueObject)it.next();
    		        	m_Log.debug("datos    -"+elvo1.getDescripcion());
    		        }*/
    		  
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
         
    }else if ("altaModoAmbito".equalsIgnoreCase(opcion)) {
   	 m_Log.debug("Entramos en alta modo ambito");
   	 String codigo = request.getParameter("codParamNew");
     String descripcion = request.getParameter("descripcionParamNew");
     m_Log.debug("Entramos en alta modo ambito"+codigo);
     m_Log.debug("Entramos en alta modo ambito"+descripcion);
     ElementoListaValueObject elvo = new ElementoListaValueObject();
     elvo.setCodigo(codigo);
     elvo.setDescripcion(descripcion);
     try {
			MantenimientoInformesManager.getInstance().insertarModoOrigen(elvo, params);
			 Vector listaModoAmbitos = GestionInformesManager.getInstance().getListaModoAmbitos(params);
			 solForm.setListaModoAmbitos(listaModoAmbitos);
		     solForm.setOrigen("");
		   
		/*     for (Iterator it=listaModoAmbitos.iterator();it.hasNext();){
		        	ElementoListaValueObject elvo1 = (ElementoListaValueObject)it.next();
		        	m_Log.debug("datos    -"+elvo1.getDescripcion());
		        }
		  */
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     
     
    }else if ("modificarModoAmbito".equalsIgnoreCase(opcion)) {
      	 m_Log.debug("Entramos en modificarModoAmbito");
       	 String codigo = request.getParameter("codParamNew");
         String descripcion = request.getParameter("descripcionParamNew");
         m_Log.debug("Entramos modificarModoAmbito"+codigo);
         m_Log.debug("Entramos modificarModoAmbito"+descripcion);
         ElementoListaValueObject elvo = new ElementoListaValueObject();
         elvo.setCodigo(codigo);
         elvo.setDescripcion(descripcion);
         try {
    			MantenimientoInformesManager.getInstance().modificarModoOrigen(elvo, params);
    			 Vector listaModoAmbitos = GestionInformesManager.getInstance().getListaModoAmbitos(params);
    			 solForm.setListaModoAmbitos(listaModoAmbitos);
    		     solForm.setOrigen("");
    		   
    		     for (Iterator it=listaModoAmbitos.iterator();it.hasNext();){
    		        	ElementoListaValueObject elvo1 = (ElementoListaValueObject)it.next();
    		        	m_Log.debug("datos    -"+elvo1.getDescripcion());
    		        }
    		  
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
         
         
    }else if ("eliminarModoAmbito".equalsIgnoreCase(opcion)) {
    	String codigo = request.getParameter("codParamNew");
    	Integer codigo1;
    	codigo1=Integer.valueOf(codigo);
    	m_Log.debug("datos    -> "+codigo);
        try {
        	
			MantenimientoInformesManager.getInstance().eliminarModoOrigen(codigo1, params);
			 Vector listaModoAmbitos = GestionInformesManager.getInstance().getListaModoAmbitos(params);
			 solForm.setListaModoAmbitos(listaModoAmbitos);
		     solForm.setOrigen("");
		   
		     for (Iterator it=listaModoAmbitos.iterator();it.hasNext();){
		        	ElementoListaValueObject elvo1 = (ElementoListaValueObject)it.next();
		        	m_Log.debug("datos    -"+elvo1.getDescripcion());
		        }
		     
		     // opcion = "modificar";
    		  
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
         
         
        }else if("modificar".equalsIgnoreCase(opcion)) {
    	String codigo = request.getParameter("codParamNew");
        String descripcion = request.getParameter("descripcionParamNew");
        String tab = request.getParameter("tabParamNew");
        String modo=request.getParameter("modoParamNew");
        
        ElementoListaValueObject elvo = new ElementoListaValueObject();
        elvo.setCodigo(codigo);
        elvo.setDescripcion(descripcion);
        elvo.setTab(tab);
        elvo.setModo(modo);
        try {
			MantenimientoInformesManager.getInstance().modificarOrigen(elvo, params);
			 Vector listaAmbitos = GestionInformesManager.getInstance().getListaAmbitos(params);
			 solForm.setListaAmbitos(listaAmbitos);
		     solForm.setOrigen("");
		   
		     for (Iterator it=listaAmbitos.iterator();it.hasNext();){
		        	ElementoListaValueObject elvo1 = (ElementoListaValueObject)it.next();
		        	m_Log.debug("datos    -"+elvo1.getDescripcion());
		        }
		     
		      opcion = "modificar";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  m_Log.debug("***** opcion modificar"+opcion);
    }else if("altaAmbito".equalsIgnoreCase(opcion)){
    	String codigo = request.getParameter("codParamNew");
        String descripcion = request.getParameter("descripcionParamNew");
        String tab = request.getParameter("tabParamNew");
        String modo=request.getParameter("modoParamNew");
        ElementoListaValueObject elvo = new ElementoListaValueObject();
        elvo.setCodigo(codigo);
        elvo.setDescripcion(descripcion);
        elvo.setTab(tab);
        elvo.setModo(modo);
        try {
			MantenimientoInformesManager.getInstance().insertarOrigen(elvo, params);
			 Vector listaAmbitos = GestionInformesManager.getInstance().getListaAmbitos(params);
			 solForm.setListaAmbitos(listaAmbitos);
		     solForm.setOrigen("");
		   
		     for (Iterator it=listaAmbitos.iterator();it.hasNext();){
		        	ElementoListaValueObject elvo1 = (ElementoListaValueObject)it.next();
		        	m_Log.debug("datos    -"+elvo1.getDescripcion());
		        }
		     
		     // opcion = "modificar";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }else if("eliminarAmbito".equalsIgnoreCase(opcion)){
    	String codigo = request.getParameter("codParamNew");
    	Integer codigo1;
    	codigo1=Integer.valueOf(codigo);
    	m_Log.debug("datos    -> "+codigo);
        try {
        	
			MantenimientoInformesManager.getInstance().eliminarOrigen(codigo1, params);
			 Vector listaAmbitos = GestionInformesManager.getInstance().getListaAmbitos(params);
			 solForm.setListaAmbitos(listaAmbitos);
		     solForm.setOrigen("");
		   
		     for (Iterator it=listaAmbitos.iterator();it.hasNext();){
		        	ElementoListaValueObject elvo1 = (ElementoListaValueObject)it.next();
		        	m_Log.debug("datos    -"+elvo1.getDescripcion());
		        }
		     
		     // opcion = "modificar";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
}