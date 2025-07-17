package es.altia.agora.interfaces.user.web.administracion.mantenimiento;

import es.altia.agora.business.administracion.exception.LoginDuplicadoException;
import es.altia.agora.business.administracion.mantenimiento.DirectivaVO;
import es.altia.agora.business.administracion.mantenimiento.PermisoProcedimientosRestringidosVO;
import es.altia.agora.business.administracion.mantenimiento.UsuariosGruposValueObject;
import es.altia.agora.business.administracion.mantenimiento.persistence.*;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.escritorio.exception.UsuarioNoValidadoException;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.persistence.PermisoProcRestringidoManager;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.util.CambioIdiomaAction;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.technical.data.Licencia;
import es.altia.agora.technical.data.LicenciaMaximoUsuarios;
import es.altia.agora.technical.xmldata.LicenseFactory;
import es.altia.agora.util.Constantes;
import es.altia.arboles.impl.ArbolImpl;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.LoadLicenseException;
import org.apache.struts.action.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;

  public final class UsuariosGruposAction extends ActionSession {

      public ActionForward performSession(	ActionMapping mapping,
                  ActionForm form,
                  HttpServletRequest request,
                  HttpServletResponse response) throws IOException, ServletException {

    m_Log.info("==================== UsuariosGruposAction ==================>");
    // Se intenta busca el fichero de licencias
    Integer maxUsuariosLicencia = -1;                
    m_Log.debug("@@@@@@ Recuperando LicenseFactory del ServletContext");
    LicenseFactory factoria  = (LicenseFactory)getServlet().getServletContext().getAttribute(ConstantesDatos.APPLICATION_LICENSE);
    if(factoria!=null){        
        m_Log.debug("@@@@@@ Recuperando licencias NumMaxUsuarios");
        
        ArrayList<Licencia> licencias = new ArrayList<Licencia>();
        try{
            licencias = factoria.getLicenses(Constantes.LICENCIA_MAXIMO_USUARIOS);
        }
        catch(LoadLicenseException e){
            m_Log.error(e.getMessage());
        }
        catch(InternalErrorException e){
            m_Log.error(e.getMessage());
        }
                
        int num=0;
        if(licencias.size()>0){
            LicenciaMaximoUsuarios maxUsers = (LicenciaMaximoUsuarios)licencias.get(0);
            // Se acumula el número de usuarios si la firma de la licencia es válida
            if(factoria.verifyEncryption(maxUsers)) 
            {
               // Se comprueba si es numérico o no el nº de usuarios
                if(!maxUsers.getNumeroMaximoUsuarios().equals(ConstantesDatos.NUNCA_MAX_USUARIO_LICENCIA) && !maxUsers.getNumeroMaximoUsuarios().equals(ConstantesDatos.NUNCA_MAX_USUARIO_LICENCIA.toLowerCase()) && isInteger(maxUsers.getNumeroMaximoUsuarios())){
                    m_Log.debug("El número de usuarios es numérico");
                    num = num + Integer.parseInt(maxUsers.getNumeroMaximoUsuarios());
                    m_Log.debug("num usuarios: " + num);
                }                
                
                // No hay límite para dar de alta usuarios
                if(maxUsers.getNumeroMaximoUsuarios().equals(ConstantesDatos.NUNCA_MAX_USUARIO_LICENCIA) || maxUsers.getNumeroMaximoUsuarios().equals(ConstantesDatos.NUNCA_MAX_USUARIO_LICENCIA.toLowerCase())){
                    maxUsuariosLicencia = -1;
                }
                   
            }else{
                m_Log.debug("La encriptación del número maximo de usuarios no es correcta");
            }
        }
      
        // Si maxUsuariosLicencia =-1 no se pueden dar de alta usuarios y si el valor es positivo, ese será el nº máximo de usuarios.
        if(num>0) maxUsuariosLicencia = num;
     }else{
        m_Log.debug("No hay límite para los usuarios");
     }        
    
    m_Log.debug("@@@@@@ maxUsers " + maxUsuariosLicencia);        
    request.getSession().setAttribute("MAX-USERS-LICENSE",maxUsuariosLicencia);                  

    HttpSession session = request.getSession();
    String opcion ="";

          ActionErrors errors = new ActionErrors();

    if ((session.getAttribute("usuario") != null)) {
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
      String[] params = usuario.getParamsCon();
      int codOrg;
      int codEnt;
      codOrg = usuario.getOrgCod();
      codEnt = usuario.getEntCod();
      String cOrg = Integer.toString(codOrg);
      String cEnt = Integer.toString(codEnt);
      String descOrg = usuario.getOrg();
      String descEnt = usuario.getEnt();

      UsuariosGruposValueObject ugVO = new UsuariosGruposValueObject();
      UsuariosGruposForm ugForm = null;

      if (form == null) {
        m_Log.debug("Rellenamos el form de UsuariosGrupos");
        form = new UsuariosGruposForm();
        if ("request".equals(mapping.getScope()))
          request.setAttribute(mapping.getAttribute(), form);
        else
          session.setAttribute(mapping.getAttribute(), form);
      }

      ugForm = (UsuariosGruposForm) form;

      opcion = request.getParameter("opcion");
      if (m_Log.isInfoEnabled()) m_Log.info("la opcion en el action es " + opcion);

      if (opcion.equals("inicio")){
        Vector consulta = new Vector();
        Vector consultaG = new Vector();
        consulta = UsuariosGruposManager.getInstance().getUsuarios(params);
        consultaG = UsuariosGruposManager.getInstance().getGrupos(params);
        session.setAttribute("RelacionUsuarios",consulta);
        session.setAttribute("RelacionGrupos",consultaG);
        Vector lista = OrganizacionesManager.getInstance().getListaOrganizaciones(params);
        ugVO.setListaOrganizaciones(lista);
        ugForm.setUsuariosGrupos(ugVO);
        opcion="inicio";
      } else if ( "inicioAdmLocal".equals(opcion) ) {
        Vector consulta = new Vector();
        String nombreT = request.getParameter("nombreT");
        String loginT  = request.getParameter("loginT");
        m_Log.debug("loginT: " + loginT + ", nombreT: " + nombreT);
        consulta = UsuariosGruposManager.getInstance().getUsuariosLocalConFiltroBusqueda(cOrg,loginT,nombreT,params);
        session.setAttribute("RelacionUsuarios",consulta);        

        int totalUsuarios = UsuariosGruposManager.getInstance().countNumUsers(loginT,nombreT,params);     
        session.setAttribute(ConstantesDatos.NUM_TOTAL_USUARIOS,totalUsuarios);
        m_Log.debug("Recuperando usuarios local: " + totalUsuarios);
        opcion="inicioAdmLocal";
      } else if ( "cargar_pagina".equals(opcion) ) {
        ugVO = ugForm.getUsuariosGrupos();
        ugForm.setUsuariosGrupos(ugVO);
        opcion = "cargar_pagina";
            } else if ( "ficheroFirma".equals(opcion) ) {
                opcion = "ficheroFirma";
            } else if ( "cargarFicheroFirma".equals(opcion) ) {
                opcion = "cargarFicheroFirma";
            } else if ( "eliminarFicheroFirma".equals(opcion) ) {
                opcion = "eliminarFicheroFirma";
      } else if ( "cargar_paginaAdmLocal".equals(opcion) ) {
	        ugVO = ugForm.getUsuariosGrupos();
	        ugForm.setUsuariosGrupos(ugVO);
	        opcion = "cargar_paginaAdmLocal";
      } else if ("modalUOR".equals(opcion)){                
                String codOrganizacion = request.getParameter("codOrganizacion");
                String codEntidad = request.getParameter("codEntidad");
                String tipoESRegistro = request.getParameter("tipoESRegistro");
                String directivaSalidaUorUsuario = (String )session.getAttribute("directiva_salidas_uor_usuario");
                
                if(("S".equals(tipoESRegistro))&&("SI".equals(directivaSalidaUorUsuario)))
                {
                    session.setAttribute("tipoAnotacion","S");
                }

                if (null==codOrganizacion) {
                    codOrganizacion = ugForm.getUsuariosGrupos().getCodOrganizacion();
                }
                if (null==codEntidad) {
                    codEntidad=ugForm.getUsuariosGrupos().getCodEntidad();
                }
                
                // seleccionar el jndi para el esquema apropiado
              String jndi = UsuariosGruposManager.getInstance().obtenerJNDI(
                  codEntidad, codOrganizacion, String.valueOf(usuario.getAppCod()), params);
          String[] paramsNuevos = new String[7];
          paramsNuevos[0] = params[0];
          paramsNuevos[6] = jndi;
          
          Vector nuevasUOR=new Vector();
          ArbolImpl arbol=new ArbolImpl();
          
          if("SI".equals(directivaSalidaUorUsuario))
          {
              nuevasUOR = UORsManager.getInstance().getListaUORsPermisoUsuarioNuevo(usuario,paramsNuevos);
              arbol = UORsManager.getInstance().getArbolUORsPermisoUsuario(false, false,usuario, paramsNuevos);
          }else
          {
              nuevasUOR = UORsManager.getInstance().getListaUORs(true,paramsNuevos);
              arbol = UORsManager.getInstance().getArbolUORs(false,false, false, paramsNuevos);
          }
          
          m_Log.debug("Cargadas " + nuevasUOR.size() + " UORs");
          //arbol de jerarquía de uors
          
          m_Log.debug("Cargadas " + (arbol.contarNodos() - 1) + " UORs en el árbol");

          request.setAttribute("codOrganizacion", codOrganizacion);
          request.setAttribute("codEntidad", codEntidad);
          request.setAttribute("arbolUORs", arbol);
          request.setAttribute("listaUORs", nuevasUOR);
}else if ("cargarNombre".equals(opcion)){
          String codOrganizacion = request.getParameter("codOrganizacion");
                String codEntidad = request.getParameter("codEntidad");
                String directivaSalidaUorUsuario = (String )session.getAttribute("directiva_salidas_uor_usuario");
                

                if (null==codOrganizacion) {
                    codOrganizacion = ugForm.getUsuariosGrupos().getCodOrganizacion();
                }
                if (null==codEntidad) {
                    codEntidad=ugForm.getUsuariosGrupos().getCodEntidad();
                }

                // seleccionar el jndi para el esquema apropiado
               String jndi = UsuariosGruposManager.getInstance().obtenerJNDI(
                  codEntidad, codOrganizacion, String.valueOf(usuario.getAppCod()), params);
          String[] paramsNuevos = new String[7];
          paramsNuevos[0] = params[0];
          paramsNuevos[6] = jndi;
          
          Vector nuevasUOR=new Vector();
          ArbolImpl arbol=new ArbolImpl();
          if("SI".equals(directivaSalidaUorUsuario))
          {
              nuevasUOR = UORsManager.getInstance().getListaUORsPorNombrePermisoUsuario(usuario,paramsNuevos);
              //arbol = UORsManager.getInstance().getArbolUORsPermisoUsuario(false, false,usuario, paramsNuevos);
              arbol = UORsManager.getInstance().getArbolUORsPermisoUsuario(false, true,usuario, paramsNuevos);
          }else
          {
              nuevasUOR = UORsManager.getInstance().getListaUORsPorNombre(true,paramsNuevos);
              //arbol = UORsManager.getInstance().getArbolUORs(false,false, false, paramsNuevos);
              arbol = UORsManager.getInstance().getArbolUORs(false,false,true, paramsNuevos);
          }
          
          m_Log.debug("Cargadas " + (arbol.contarNodos() - 1) + " UORs en el árbol");

          request.setAttribute("codOrganizacion", codOrganizacion);
          request.setAttribute("codEntidad", codEntidad);
          request.setAttribute("arbolUORs", arbol);
          request.setAttribute("listaUORs", nuevasUOR);

            opcion = "cargarNombre";
       } else if ("modalCargo".equals(opcion)){
                String codOrganizacion = request.getParameter("codOrganizacion");
                String codEntidad = request.getParameter("codEntidad");
                // seleccionar el jndi para el esquema apropiado
                String jndi = UsuariosGruposManager.getInstance().obtenerJNDI(
                        codEntidad, codOrganizacion, String.valueOf(usuario.getAppCod()), params);
                String[] paramsNuevos = new String[7];
                paramsNuevos[0] = params[0]; 
                paramsNuevos[6] = jndi;
                Vector nuevosCargos = CargosManager.getInstance().getListaUORs(paramsNuevos);
                m_Log.debug("Cargados " + nuevosCargos.size() + " Cargos");
                ArbolImpl arbol = CargosManager.getInstance().getArbolUORs(false, paramsNuevos);
                m_Log.debug("Cargados " + (arbol.contarNodos() -1) + " Cargos en el árbol");

                request.setAttribute("arbolCargos", arbol);
                request.setAttribute("listaCargos", nuevosCargos);
      } else if ( "altaDatosUsuariosAdmLocal".equals(opcion) ) {
                 //Se obtienen los tipos de procedimientos del fichero properties Portafirmas.properties
                ResourceBundle expPortafirmas = ResourceBundle.getBundle("Portafirmas");
                 String propiedad = usuario.getOrgCod()+"/Portafirmas";
                String portafirmas = expPortafirmas.getString(propiedad);
                
	      	ugVO = ugForm.getUsuariosGrupos();
	      	String modo = request.getParameter("segundo");
	      	if("alta".equals(modo)) {
	      		session.setAttribute("modo","alta");
	      		ugVO = new UsuariosGruposValueObject();
	      	} else {
	      	  session.setAttribute("modo","modificar");	
	      	}
	      	String codUsuario = request.getParameter("primero");
	      	ugVO.setCodUsuario(codUsuario);
	      	ugVO = UsuariosGruposManager.getInstance().getDatosUsuarios(ugVO,codUsuario,params, portafirmas);
	      	Vector listaOrganizaciones = new Vector();
	      	UsuariosGruposValueObject u = new UsuariosGruposValueObject();
	      	u.setCodOrganizacion(cOrg);
	        u.setNombreOrganizacion(descOrg);
	        u.setAutorizacion("si");
	        listaOrganizaciones.addElement(u);
	        ugVO.setListaOrganizaciones(listaOrganizaciones);
	      	Vector listaIdiomas = IdiomasManager.getInstance().getListaIdiomas(params);
	      	ugVO.setListaIdiomas(listaIdiomas);
	      	ugVO.setCodEntidad(cEnt);
	      	Vector consulta = new Vector();
	        consulta = UsuariosGruposManager.getInstance().getUsuarios(params);
	        ugVO.setListaUsuariosTodos(consulta);
                // Construir lista de aplicaciones y conseguir mensajes para las directivas.
                procesarDirectivas(ugForm, ugVO, usuario);
                                
                try{
                    ResourceBundle CONFIG_COMMON = ResourceBundle.getBundle("common");
                    String valorPropiedad = CONFIG_COMMON.getString(usuario.getOrgCod() + "/obligatorio_cambio_password");

                    if(valorPropiedad!=null && "SI".equalsIgnoreCase(valorPropiedad))
                        request.setAttribute("OBLIGATORIO_CAMBIO_PASSWORD","SI");
                    else
                    if(valorPropiedad!=null && "NO".equalsIgnoreCase(valorPropiedad))
                        request.setAttribute("OBLIGATORIO_CAMBIO_PASSWORD","NO");
                
                }catch(Exception e){
                    e.printStackTrace();
                }
                
                String RESTRICCION_LONGITUD_PASSWORD = "NO";
                 try{
                    ResourceBundle CONFIG_COMMON = ResourceBundle.getBundle("common");
                    
                    RESTRICCION_LONGITUD_PASSWORD = CONFIG_COMMON.getString("password_longitud");

                     if(RESTRICCION_LONGITUD_PASSWORD!=null && !"".equals(RESTRICCION_LONGITUD_PASSWORD)){
                          int num = Integer.parseInt(RESTRICCION_LONGITUD_PASSWORD);
                          // Si no salta ninguna excepci?n, es porque el valor de la propiedad es num?rico
                     }
                
                }catch(Exception e){
                   
                    // Si se ha producido alg?n error al recuperar el valor de la propiedad, no se tiene 
                    // en cuenta una longitud m?nima de password
                    RESTRICCION_LONGITUD_PASSWORD = "NO";
                    
                }
                 request.setAttribute("RESTRICCION_LONGITUD_PASSWORD",RESTRICCION_LONGITUD_PASSWORD);
                 
                
                
                

	      	ugForm.setUsuariosGrupos(ugVO);
	      	opcion = "altaDatosUsuariosAdmLocal";
      } else if ( "datosUsuarios".equals(opcion) ) {
                
        //Se obtienen los tipos de procedimientos del fichero properties Portafirmas.properties
        ResourceBundle expPortafirmas = ResourceBundle.getBundle("Portafirmas");
        String propiedad = usuario.getOrgCod()+"/Portafirmas";
        String portafirmas = expPortafirmas.getString(propiedad);
          
        ugVO = ugForm.getUsuariosGrupos();
        String modo = request.getParameter("segundo");
        if("alta".equals(modo)) {
                session.setAttribute("modo","alta");
                ugVO = new UsuariosGruposValueObject();
        } else {
          session.setAttribute("modo","modificar");	
        }
        String codUsuario = request.getParameter("primero");
        ugVO.setCodUsuario(codUsuario);
        ugVO = UsuariosGruposManager.getInstance().getDatosUsuarios(ugVO,codUsuario,params, portafirmas);
                
          Vector listaOrganizaciones = new Vector();
          listaOrganizaciones = UsuariosGruposManager.getInstance().getListaOrganizaciones(codUsuario, params);
          ugVO.setListaOrganizaciones(listaOrganizaciones);

          Vector unidades = new Vector();
          Vector listaUnidadesOrganicasUsuario = new Vector();
          for (int k = 0; k < listaOrganizaciones.size(); k++) {
              if("si".equalsIgnoreCase(((UsuariosGruposValueObject) listaOrganizaciones.elementAt(k)).getAutorizacion())){              
                  UsuariosGruposValueObject u = new UsuariosGruposValueObject();
                  u = (UsuariosGruposValueObject) listaOrganizaciones.elementAt(k);
                  String jndi = UsuariosGruposManager.getInstance().obtenerJNDI(
                          u.getCodEntidad(), u.getCodOrganizacion(), u.getCodAplicacion(), params);
                  String[] paramsNuevo = new String[7];
                  paramsNuevo[0] = params[0];
                  paramsNuevo[6] = jndi;
                  u.setCodUsuario(codUsuario);
                  listaUnidadesOrganicasUsuario = UsuariosGruposManager.getInstance().
                          getListaUnidadesOrganicasUsuario(u, paramsNuevo);
                  for (int j = 0; j < listaUnidadesOrganicasUsuario.size(); j++) {
                      UsuariosGruposValueObject uG = new UsuariosGruposValueObject();
                      uG = (UsuariosGruposValueObject) listaUnidadesOrganicasUsuario.elementAt(j);
                      unidades.addElement(uG);
                  }
              }
          }
          ugVO.setListaUnidOrganicas(unidades);
               
	      	
	      	Vector listaIdiomas = IdiomasManager.getInstance().getListaIdiomas(params);
	      	ugVO.setListaIdiomas(listaIdiomas);

            // Construir lista de aplicaciones y conseguir mensajes para las directivas.
            procesarDirectivas(ugForm, ugVO, usuario);

            // Se procesan los permisos del usuario sobre los procedimientos restringidos
            ArrayList<PermisoProcedimientosRestringidosVO> permisos = ugVO.getProcedimientosRestringidos();

            StringBuffer aux = new StringBuffer();
            for(int i=0;permisos!=null && i<permisos.size();i++){
                PermisoProcedimientosRestringidosVO permiso = permisos.get(i);

                aux.append(permiso.getCodMunicipio() + ";" + permiso.getDescOrganizacion() + ";" +
                        permiso.getCodEntidad() + ";" + permiso.getDescEntidad() + ";" + permiso.getCodProcedimiento() +
                        ";" + permiso.getDescProcedimiento());

                if(permisos.size()-i>1)
                    aux.append(ConstantesDatos.SEPARADOR);
            }

            ugForm.setListaProcedimientosRestringidos(aux.toString());
	    ugForm.setUsuariosGrupos(ugVO);
			
            try{
                ResourceBundle CONFIG_COMMON = ResourceBundle.getBundle("common");
                String valorPropiedad = CONFIG_COMMON.getString(usuario.getOrgCod() + "/obligatorio_cambio_password");
                
                if(valorPropiedad!=null && "SI".equalsIgnoreCase(valorPropiedad))
                    request.setAttribute("OBLIGATORIO_CAMBIO_PASSWORD","SI");
                else
                if(valorPropiedad!=null && "NO".equalsIgnoreCase(valorPropiedad))
                    request.setAttribute("OBLIGATORIO_CAMBIO_PASSWORD","NO");
                
            }catch(Exception e){
                e.printStackTrace();
            }
            
            
             String RESTRICCION_LONGITUD_PASSWORD = "NO"; 
                try {
                    ResourceBundle CONFIG_COMMON = ResourceBundle.getBundle("common");

                    RESTRICCION_LONGITUD_PASSWORD = CONFIG_COMMON.getString("password_longitud");

                    if (RESTRICCION_LONGITUD_PASSWORD != null && !"".equals(RESTRICCION_LONGITUD_PASSWORD)) {
                        int num = Integer.parseInt(RESTRICCION_LONGITUD_PASSWORD);
                        // Si no salta ninguna excepci?n, es porque el valor de la propiedad es num?rico
                    }

                } catch (Exception e) {

                    // Si se ha producido alg?n error al recuperar el valor de la propiedad, no se tiene 
                    // en cuenta una longitud m?nima de password
                    RESTRICCION_LONGITUD_PASSWORD = "NO";

                }
                request.setAttribute("RESTRICCION_LONGITUD_PASSWORD", RESTRICCION_LONGITUD_PASSWORD);
            
            
      } else if ( "cargarParametrosUsuario".equals(opcion) ) {
          
          //Se obtienen los tipos de procedimientos del fichero properties Portafirmas.properties
            ResourceBundle expPortafirmas = ResourceBundle.getBundle("Portafirmas");
            String propiedad = usuario.getOrgCod()+"/Portafirmas";
            String portafirmas = expPortafirmas.getString(propiedad);
    	    // Opcion para editar los datos básicos con acceso para cualquier usuario
	      	ugVO = ugForm.getUsuariosGrupos();
	      	String modo = request.getParameter("segundo");
	      	if("alta".equals(modo)) {
	      		session.setAttribute("modo","alta");
	      		ugVO = new UsuariosGruposValueObject();
	      	} else {
	      	  session.setAttribute("modo","modificar");	
	      	}

	      	String codUsuario = Integer.toString(usuario.getIdUsuario());
	      	m_Log.debug("El codigo de usuario es: " + codUsuario);
	      	ugVO.setCodUsuario(codUsuario);
	      	ugVO = UsuariosGruposManager.getInstance().getDatosUsuariosLocal(ugVO,codUsuario,cOrg,cEnt,params, portafirmas);
	      	Vector listaIdiomas = IdiomasManager.getInstance().getListaIdiomas(params);
	      	ugVO.setListaIdiomas(listaIdiomas);
	      	Vector consulta = new Vector();
	        consulta = UsuariosGruposManager.getInstance().getUsuarios(params);
	        ugVO.setListaUsuariosTodos(consulta);
	      	ugForm.setUsuariosGrupos(ugVO);
      } else if ( "datosUsuariosAdmLocal".equals(opcion) ) {
            //Se obtienen los tipos de procedimientos del fichero properties Portafirmas.properties
            ResourceBundle expPortafirmas = ResourceBundle.getBundle("Portafirmas");
            String propiedad = usuario.getOrgCod()+"/Portafirmas";
            String portafirmas = expPortafirmas.getString(propiedad);
            ugVO = ugForm.getUsuariosGrupos();
            String modo = request.getParameter("segundo");
            if("alta".equals(modo)) {
                    session.setAttribute("modo","alta");
                    ugVO = new UsuariosGruposValueObject();
            } else {
              session.setAttribute("modo","modificar");	
            }
            String codUsuario = request.getParameter("primero");
            ugVO.setCodUsuario(codUsuario);
            ugVO = UsuariosGruposManager.getInstance().getDatosUsuariosLocal(ugVO,codUsuario,cOrg,cEnt,params, portafirmas);
            Vector listaOrganizaciones = new Vector();
            listaOrganizaciones = UsuariosGruposManager.getInstance().getListaOrganizacionesLocal(cOrg,descOrg,codUsuario,params);
            ugVO.setListaOrganizaciones(listaOrganizaciones);
            Vector listaIdiomas = IdiomasManager.getInstance().getListaIdiomas(params);
            ugVO.setListaIdiomas(listaIdiomas);
            Vector consulta = new Vector();
            consulta = UsuariosGruposManager.getInstance().getUsuarios(params);
            ugVO.setListaUsuariosTodos(consulta);
            // Construir lista de aplicaciones y conseguir mensajes para las directivas.
            procesarDirectivas(ugForm, ugVO, usuario);

            // Se procesan los permisos del usuario sobre los procedimientos restringidos
            ArrayList<PermisoProcedimientosRestringidosVO> permisos = ugVO.getProcedimientosRestringidos();

            StringBuffer aux = new StringBuffer();
            for(int i=0;permisos!=null && i<permisos.size();i++){
                PermisoProcedimientosRestringidosVO permiso = permisos.get(i);
                aux.append(permiso.getCodMunicipio() + ";" + permiso.getDescOrganizacion() + ";" +
                        permiso.getCodEntidad() + ";" + permiso.getDescEntidad() + ";" + permiso.getCodProcedimiento() +
                        ";" + permiso.getDescProcedimiento());

                if(permisos.size()-i>1)
                    aux.append(ConstantesDatos.SEPARADOR);
            }

            ugForm.setListaProcedimientosRestringidos(aux.toString());
            
            
            try{
              ResourceBundle CONFIG_COMMON = ResourceBundle.getBundle("common");
              String valorPropiedad = CONFIG_COMMON.getString(usuario.getOrgCod() + "/obligatorio_cambio_password");

              if(valorPropiedad!=null && "SI".equalsIgnoreCase(valorPropiedad))
                  request.setAttribute("OBLIGATORIO_CAMBIO_PASSWORD","SI");
              else
              if(valorPropiedad!=null && "NO".equalsIgnoreCase(valorPropiedad))
                  request.setAttribute("OBLIGATORIO_CAMBIO_PASSWORD","NO");
                
            }catch(Exception e){
                e.printStackTrace();
            }
            
            
             String RESTRICCION_LONGITUD_PASSWORD = "NO"; 
                try {
                    ResourceBundle CONFIG_COMMON = ResourceBundle.getBundle("common");

                    RESTRICCION_LONGITUD_PASSWORD = CONFIG_COMMON.getString("password_longitud");

                    if (RESTRICCION_LONGITUD_PASSWORD != null && !"".equals(RESTRICCION_LONGITUD_PASSWORD)) {
                        int num = Integer.parseInt(RESTRICCION_LONGITUD_PASSWORD);
                        // Si no salta ninguna excepci?n, es porque el valor de la propiedad es num?rico
                    }

                } catch (Exception e) {

                    // Si se ha producido alg?n error al recuperar el valor de la propiedad, no se tiene 
                    // en cuenta una longitud m?nima de password
                    RESTRICCION_LONGITUD_PASSWORD = "NO";

                }
                request.setAttribute("RESTRICCION_LONGITUD_PASSWORD", RESTRICCION_LONGITUD_PASSWORD);


          	ugForm.setUsuariosGrupos(ugVO);
          	opcion = "altaDatosUsuariosAdmLocal";
        }else if ( "datosGrupos".equals(opcion) ) {
	      	ugVO = ugForm.getUsuariosGrupos();
	      	String codGrupo = request.getParameter("primero");
	      	ugVO.setCodGrupo(codGrupo);
	      	String nombreGrupo = request.getParameter("segundo");
	      	if(nombreGrupo ==null || "null".equals(nombreGrupo)) {
	      		ugVO.setNombreGrupo("");
	      	} else {
	      	  ugVO.setNombreGrupo(nombreGrupo);
	        }
	      	String modo = request.getParameter("tercero");
	      	if("alta".equals(modo)) {
	      		session.setAttribute("modo","alta");
	      	} else {
	      	  session.setAttribute("modo","modificar");	
	      	}
	      	Vector listaUsuariosGrupos = new Vector();
	      	if(codGrupo !=null && !"".equals(codGrupo)) {
	      	  listaUsuariosGrupos = UsuariosGruposManager.getInstance().getListaUsuariosGrupos(codGrupo,params);
	        }
	      	ugVO.setListaUsuariosGrupos(listaUsuariosGrupos);
	      	ugForm.setUsuariosGrupos(ugVO);
      } else if ("insertarUsuario".equals(opcion)) {
	          ugVO = ugForm.getUsuariosGrupos();
	          String listaOrgan = request.getParameter("listaOrgan");
	          ugVO.setListaOrganizaciones(listaTemasSeleccionados(listaOrgan));
	          String listaOrganizacionesGrupos = request.getParameter("lOrganizaciones");
	          String listaEntidadesGrupos = request.getParameter("lEntidades");
	          String listaAplicacionesGrupos = request.getParameter("lAplicaciones");
	          String listaGrupos = request.getParameter("lGrupos");
	          ugVO.setListaOrganizacionesGrupos(listaTemasSeleccionados(listaOrganizacionesGrupos));
	          ugVO.setListaEntidadesGrupos(listaTemasSeleccionados(listaEntidadesGrupos));
	          ugVO.setListaAplicacionesGrupos(listaTemasSeleccionados(listaAplicacionesGrupos));
	          ugVO.setListaGrupos(listaTemasSeleccionados(listaGrupos));
	          String listaOrganizacionesUOR = request.getParameter("lOrganizacionesUOR");
	          String listaEntidadesUOR = request.getParameter("lEntidadesUOR");
	          String listaUnidadesOrganicas = request.getParameter("lUnidOrganicas");
	          String listaCargosUOR = request.getParameter("lCargosUOR");
                  if (m_Log.isDebugEnabled()) m_Log.debug("LCARGOS = " + listaCargosUOR);
	          ugVO.setListaOrganizacionesUOR(listaTemasSeleccionados(listaOrganizacionesUOR));
	          ugVO.setListaEntidadesUOR(listaTemasSeleccionados(listaEntidadesUOR));
	          ugVO.setListaUnidadesOrganicas(listaTemasSeleccionados(listaUnidadesOrganicas));
	          ugVO.setListaCargosUOR(listaTemasSeleccionados(listaCargosUOR));

              String listaProcedimientosRestringidos = request.getParameter("listaProcedimientosRestringidos");
              m_Log.debug(" =====> listaProcedimientosRestringidos: "  + listaProcedimientosRestringidos);
              Vector listaProcsRestringidos = listaTemasSeleccionados(listaProcedimientosRestringidos);
              m_Log.debug(" =============> modificarUsuario listaProcsRestringidos: " + listaProcsRestringidos);
              // Se establecen los procedimientos restringidos sobre los que el usuario tiene permiso, si los hubiera
              ugVO.setProcedimientosRestringidos(this.tratarPermisosSobreProcedimientosRestringidos(listaProcsRestringidos, ugVO.getCodUsuario()));
              
              int resultado = -1;
               try {
                        //Se obtienen los tipos de procedimientos del fichero properties Portafirmas.properties
                         ResourceBundle expPortafirmas = ResourceBundle.getBundle("Portafirmas");
                         String propiedad = usuario.getOrgCod()+"/Portafirmas";
                         String portafirmas = expPortafirmas.getString(propiedad);
                      resultado = UsuariosGruposManager.getInstance().insertarUsuario(ugVO, params, portafirmas);
                   if (resultado >= 0) {
                       ugVO.setRespOpcion("insertarUsuario");
                   } else if(resultado == -1)  {
                       ugVO.setRespOpcion("errorTecnico");
                   }

                } catch (LoginDuplicadoException lde) {
                    ugVO.setRespOpcion("loginDuplicado");
                }/* catch (UsuarioNoValidadoException unve) {
                    ugVO.setRespOpcion("errorValidacion");
                    errors.add("errorValidacion", new ActionError(unve.getMessage()));
                }*/
	          ugVO.setListaUnidadesOrganicas(new Vector());
	          ugForm.setUsuariosGrupos(ugVO);
	          opcion = "verOculto";
      } else if ( "eliminarUsuario".equals(opcion) ) {
	        ugVO = ugForm.getUsuariosGrupos();
	        String codUsuario = request.getParameter("codUsuario");
	        int resultado = UsuariosGruposManager.getInstance().eliminarUsuario(codUsuario,params);
                ugVO.setRespOpcion("noEliminado");
                if(resultado !=-1 && resultado!=-99) {
	      	  ugVO.setRespOpcion("eliminarUsuario");
	      	}
                else if (resultado==-99) ugVO.setRespOpcion("errorTieneTramitacionRegistro");
	      	ugVO.setListaUnidadesOrganicas(new Vector());
	      	ugForm.setUsuariosGrupos(ugVO);
	      	opcion = "verOculto";
      } else if ("modificarUsuario".equals(opcion)) {
	          ugVO = ugForm.getUsuariosGrupos();
	          boolean huboErrores = false;
	          if (ugVO.getFirmante() == 0) {
	              int codigoUsuarioAsInt = Integer.parseInt(ugVO.getCodUsuario());
	              int cuentaPendientes = UsuariosGruposManager.getInstance().cuentaDelegacionesHaciaUsuario(codigoUsuarioAsInt, params);
	              if (cuentaPendientes == 0) {
	                  cuentaPendientes = UsuariosGruposManager.getInstance().cuentaDocumentosPendientesDeFirmarPorUsuario(codigoUsuarioAsInt, params);
	                  if (cuentaPendientes == 0) {
	                      cuentaPendientes = UsuariosGruposManager.getInstance().cuentaPlantillasParaFirmarPorUsuario(codigoUsuarioAsInt, params);
	                      if (cuentaPendientes == 0) {
	                          /* Nada pendiente... se puede quitar la capacidad de firma */
	                      } else {
	                          /* Hay pendientes plantillas */
	                          ugVO.setRespOpcion("errorNoSePuedeQuitarCapacidadFirmaPlantillas");
	                          huboErrores = true;
	                      }
	                  } else {
	                      ugVO.setRespOpcion("errorNoSePuedeQuitarCapacidadFirmaDocumentos");
	                      huboErrores = true;
	                  }
	              } else {
	                  ugVO.setRespOpcion("errorNoSePuedeQuitarCapacidadFirmaDelegaciones");
	                  huboErrores = true;
	              }
	          }
	          if (!huboErrores) {
	        	 	        	  
	        	  /**
	        	   * Comprobamos si el nombre de usuario que se cambia es el del usuario
	        	   * en sesion. Si es así, hay que cambiar el nombre en la sesion, y recargar
	        	   * la JSP para que coja el nombre nuevo.
	        	   **/	      
	        	  if ((ugVO.getCodUsuario().equals(String.valueOf(((UsuarioValueObject)session.getAttribute("usuario")).getIdUsuario()))) 
	        			  && (!ugVO.getNombreUsuario().equals(String.valueOf(((UsuarioValueObject)session.getAttribute("usuario")).getNombreUsu())))){
	        		  UsuarioValueObject usuSesion = (UsuarioValueObject) session.getAttribute("usuario");
	        		  usuSesion.setNombreUsu(ugVO.getNombreUsuario());
	        		  session.setAttribute("usuario", usuSesion);
	        	  }
	        	  
	              String listaOrgan = request.getParameter("listaOrgan");
	              ugVO.setListaOrganizaciones(listaTemasSeleccionados(listaOrgan));
	              String listaOrganizacionesGrupos = request.getParameter("lOrganizaciones");
	              String listaEntidadesGrupos = request.getParameter("lEntidades");
	              String listaAplicacionesGrupos = request.getParameter("lAplicaciones");
	              String listaGrupos = request.getParameter("lGrupos");
	              ugVO.setListaOrganizacionesGrupos(listaTemasSeleccionados(listaOrganizacionesGrupos));
	              ugVO.setListaEntidadesGrupos(listaTemasSeleccionados(listaEntidadesGrupos));
	              ugVO.setListaAplicacionesGrupos(listaTemasSeleccionados(listaAplicacionesGrupos));
	              ugVO.setListaGrupos(listaTemasSeleccionados(listaGrupos));
	              String listaOrganizacionesUOR = request.getParameter("lOrganizacionesUOR");
	              String listaEntidadesUOR = request.getParameter("lEntidadesUOR");
	              String listaUnidadesOrganicas = request.getParameter("lUnidOrganicas");
	              String listaCargosUOR = request.getParameter("lCargosUOR");
	              ugVO.setListaOrganizacionesUOR(listaTemasSeleccionados(listaOrganizacionesUOR));
	              ugVO.setListaEntidadesUOR(listaTemasSeleccionados(listaEntidadesUOR));
	              ugVO.setListaUnidadesOrganicas(listaTemasSeleccionados(listaUnidadesOrganicas));
	              ugVO.setListaCargosUOR(listaTemasSeleccionados(listaCargosUOR));
                  String listaProcedimientosRestringidos = request.getParameter("listaProcedimientosRestringidos");
                  m_Log.debug(" =====> listaProcedimientosRestringidos: "  + listaProcedimientosRestringidos);
                  Vector listaProcsRestringidos = listaTemasSeleccionados(listaProcedimientosRestringidos);
                  m_Log.debug(" =============> modificarUsuario listaProcsRestringidos: " + listaProcsRestringidos);
                  // Se establecen los procedimientos restringidos sobre los que el usuario tiene permiso, si los hubiera
                  ugVO.setProcedimientosRestringidos(this.tratarPermisosSobreProcedimientosRestringidos(listaProcsRestringidos, ugVO.getCodUsuario()));


	              int resultado;
	              try {
                           //Se obtienen los tipos de procedimientos del fichero properties Portafirmas.properties
                            ResourceBundle expPortafirmas = ResourceBundle.getBundle("Portafirmas");
                            String propiedad = usuario.getOrgCod()+"/Portafirmas";
                            String portafirmas = expPortafirmas.getString(propiedad);
	                  resultado = UsuariosGruposManager.getInstance().modificarUsuario(ugVO, params, portafirmas);
	                  if (resultado != -1) ugVO.setRespOpcion("modificarUsuario");
	
	              } catch (UsuarioNoValidadoException unve) {
	                  ugVO.setRespOpcion("errorValidacion");
	                  errors.add("errorValidacion", new ActionError(unve.getMessage()));
	              }
	              ugVO.setListaUnidadesOrganicas(new Vector());
	
	          } else {
	              if (m_Log.isDebugEnabled()) m_Log.debug("Hubo errores. RespOpcion=" + ugVO.getRespOpcion());
	              ugVO.setListaUnidadesOrganicas(new Vector());
	          }
	          ugForm.setUsuariosGrupos(ugVO);
	          opcion = "verOculto";
	          
      } else if ("modificarUsuarioLocal".equals(opcion)) {
	          ugVO = ugForm.getUsuariosGrupos();
	          boolean huboErrores = false;
	          if (ugVO.getFirmante() == 0) {
	              int codigoUsuarioAsInt = Integer.parseInt(ugVO.getCodUsuario());
	              int cuentaPendientes = UsuariosGruposManager.getInstance().cuentaDelegacionesHaciaUsuario(codigoUsuarioAsInt, params);
	              if (cuentaPendientes == 0) {
	                  cuentaPendientes = UsuariosGruposManager.getInstance().cuentaDocumentosPendientesDeFirmarPorUsuario(codigoUsuarioAsInt, params);
	                  if (cuentaPendientes == 0) {
	                      cuentaPendientes = UsuariosGruposManager.getInstance().cuentaPlantillasParaFirmarPorUsuario(codigoUsuarioAsInt, params);
	                      if (cuentaPendientes == 0) {
	                          /* Nada pendiente... se puede quitar la capacidad de firma */
	                      } else {
	                          /* Hay pendientes plantillas */
	                          ugVO.setRespOpcion("errorNoSePuedeQuitarCapacidadFirmaPlantillas");
	                          huboErrores = true;
	                      }
	                  } else {
	                      ugVO.setRespOpcion("errorNoSePuedeQuitarCapacidadFirmaDocumentos");
	                      huboErrores = true;
	                  }
	              } else {
	                  ugVO.setRespOpcion("errorNoSePuedeQuitarCapacidadFirmaDelegaciones");
	                  huboErrores = true;
	              }
	          }
	          if (!huboErrores) {
                    String listaOrgan = request.getParameter("listaOrgan");
                    ugVO.setListaOrganizaciones(listaTemasSeleccionados(listaOrgan));
                    String listaOrganizacionesGrupos = request.getParameter("lOrganizaciones");
                    String listaEntidadesGrupos = request.getParameter("lEntidades");
                    String listaAplicacionesGrupos = request.getParameter("lAplicaciones");
                    String listaGrupos = request.getParameter("lGrupos");
                    ugVO.setListaOrganizacionesGrupos(listaTemasSeleccionados(listaOrganizacionesGrupos));
                    ugVO.setListaEntidadesGrupos(listaTemasSeleccionados(listaEntidadesGrupos));
                    ugVO.setListaAplicacionesGrupos(listaTemasSeleccionados(listaAplicacionesGrupos));
                    ugVO.setListaGrupos(listaTemasSeleccionados(listaGrupos));
                    String listaOrganizacionesUOR = request.getParameter("lOrganizacionesUOR");
                    String listaEntidadesUOR = request.getParameter("lEntidadesUOR");
                    String listaUnidadesOrganicas = request.getParameter("lUnidOrganicas");
                    String listaCargosUOR = request.getParameter("lCargosUOR");
                    ugVO.setListaOrganizacionesUOR(listaTemasSeleccionados(listaOrganizacionesUOR));
                    ugVO.setListaEntidadesUOR(listaTemasSeleccionados(listaEntidadesUOR));
                    ugVO.setListaUnidadesOrganicas(listaTemasSeleccionados(listaUnidadesOrganicas));
                    ugVO.setListaCargosUOR(listaTemasSeleccionados(listaCargosUOR));
				  
                    String listaProcedimientosRestringidos = request.getParameter("listaProcedimientosRestringidos");
                    m_Log.debug(" =====> listaProcedimientosRestringidos: "  + listaProcedimientosRestringidos);
                    Vector listaProcsRestringidos = listaTemasSeleccionados(listaProcedimientosRestringidos);
                    m_Log.debug(" =============> modificarUsuario listaProcsRestringidos: " + listaProcsRestringidos);
                    // Se establecen los procedimientos restringidos sobre los que el usuario tiene permiso, si los hubiera
                    ugVO.setProcedimientosRestringidos(this.tratarPermisosSobreProcedimientosRestringidos(listaProcsRestringidos, ugVO.getCodUsuario()));

                    int resultado;
                    try {

                         //Se obtienen los tipos de procedimientos del fichero properties Portafirmas.properties
                          ResourceBundle expPortafirmas = ResourceBundle.getBundle("Portafirmas");
                           String propiedad = usuario.getOrgCod()+"/Portafirmas";
                          String portafirmas = expPortafirmas.getString(propiedad);

                        resultado = UsuariosGruposManager.getInstance().modificarUsuarioLocal(ugVO, cOrg, cEnt, params, portafirmas);
                        if (resultado != -1) ugVO.setRespOpcion("modificarUsuario");

                    } catch (UsuarioNoValidadoException unve) {
                        ugVO.setRespOpcion("errorValidacion");
                        errors.add("errorValidacion", new ActionError(unve.getMessage()));
                    }
                    ugVO.setListaUnidadesOrganicas(new Vector());
	          } else {
	              ugVO.setListaUnidadesOrganicas(new Vector());
	          }
	
	          ugForm.setUsuariosGrupos(ugVO);
	          opcion = "verOculto";
      }
      else if("verificarAsignacionPermisosCorrectaAdmLocal".equals(opcion)){          
          String listaUnidadesOrganicas = request.getParameter("listaUnidades");
          String listaOrganizaciones = request.getParameter("listaOrganizaciones");
          
          m_Log.debug(" ============== listaOrganizaciones: " + listaOrganizaciones);
          m_Log.debug(" ============== listaUnidadesOrganicas: " + listaUnidadesOrganicas);
          Vector vectorUnidadesOrganicas = listaTemasSeleccionados(listaUnidadesOrganicas);
          
           
          
          String errores = UORsManager.getInstance().verificarAsignacionPermisosOficinasRegistro(vectorUnidadesOrganicas, params);
          String salida= mensajeVerificarAsignacionPermisosOficinasRegistro(errores);
           
          response.setContentType("text/xml");
          response.setCharacterEncoding("utf-8");
          PrintWriter out = response.getWriter();
          out.println(salida);
          out.flush();
          out.close();
          
      }
      
       else if("verificarAsignacionPermisosCorrecta".equals(opcion)){          
          String listaUnidadesOrganicas = request.getParameter("listaUnidades");
          String listaOrganizaciones = request.getParameter("listaOrganizaciones");
          String entidades = request.getParameter("listaEntidades");
          
          m_Log.debug(" ============== listaOrganizaciones: " + listaOrganizaciones);
          m_Log.debug(" ============== listaUnidadesOrganicas: " + listaUnidadesOrganicas);
          Vector vectorUnidadesOrganicas = listaTemasSeleccionados(listaUnidadesOrganicas);
          Vector vectorOrganizaciones = listaTemasSeleccionados(listaOrganizaciones);
          Vector vectorEntidades = listaTemasSeleccionados(entidades);
          
          Vector vectorOrganizacionesDistintas= listaDistintaTemasSeleccionados(listaOrganizaciones);
                 
                    
          Hashtable<String,Vector> unidadesOrganicasPorOrganizacion = new Hashtable<String,Vector>();
          
           for(int i=0;vectorOrganizacionesDistintas!=null && i<vectorOrganizacionesDistintas.size();i++)
            {
                
                Vector unidadesOrganizacion=new Vector();
                String codOrganizacion=(String) vectorOrganizacionesDistintas.get(i);
                
                m_Log.debug(" ============== organizacion: "+codOrganizacion);
                for(int j=0;vectorUnidadesOrganicas!=null && j<vectorUnidadesOrganicas.size();j++)
                {
                    String organizacion=(String)vectorOrganizaciones.get(j);
                    if(organizacion.equals(codOrganizacion)) unidadesOrganizacion.add(vectorUnidadesOrganicas.get(j));
                }
                
                unidadesOrganicasPorOrganizacion.put(codOrganizacion, unidadesOrganizacion);
                
                
            }
          
          String errores="";
          
          for(int i=0;vectorOrganizacionesDistintas!=null && i<vectorOrganizacionesDistintas.size();i++)
            {
     
                String codOrganizacion=(String) vectorOrganizacionesDistintas.get(i);
                Vector unidadesOrganizacion=unidadesOrganicasPorOrganizacion.get(codOrganizacion);
                m_Log.debug(" ============== obtener unidades por organizacion: "+codOrganizacion);
                m_Log.debug(" ============== unidades por organizacion: "+codOrganizacion+" -->"+unidadesOrganizacion.toString() );
                
                String jndi = UsuariosGruposManager.getInstance().obtenerJNDI(
                        "1", codOrganizacion, String.valueOf(usuario.getAppCod()), params);
                String[] paramsNuevos = new String[7];
                paramsNuevos[0] = params[0];
                paramsNuevos[6] = jndi;
                
                errores = errores+ UORsManager.getInstance().verificarAsignacionPermisosOficinasRegistro(unidadesOrganizacion, paramsNuevos);
          
                
            }
          
          
          
          String salida= mensajeVerificarAsignacionPermisosOficinasRegistro(errores);
           
          response.setContentType("text/xml");
          response.setCharacterEncoding("utf-8");
          PrintWriter out = response.getWriter();
          out.println(salida);
          out.flush();
          out.close();
          
      }
       
      else if ("modificarDatosBasicosUsuario".equals(opcion)) {
	    	  // Modificación de datos básicos con acceso para cualquier usuario
	    	  ugVO = ugForm.getUsuariosGrupos();
	          int resultado;
	          try {
	              resultado = UsuariosGruposManager.getInstance().modificarDatosBasicosUsuario(ugVO, params);
	              if (resultado != -1) ugVO.setRespOpcion("modificarUsuario");
	              // LLamamos a la accion de cambiar idioma
	              CambioIdiomaAction cambioIdiomaAction = new CambioIdiomaAction();
	              cambioIdiomaAction.performSession(mapping,form,request,response);
	              // Cambiamos el nombre de usuario en la sesion
	              UsuarioValueObject elUsuario = new UsuarioValueObject();
	              if (session.getAttribute("usuario") != null)
	                  elUsuario = (UsuarioValueObject)session.getAttribute("usuario");
	              elUsuario.setNombreUsu(ugForm.getNombreUsuario());	              
	              session.setAttribute("usuario",elUsuario);
	              m_Log.debug("Cambiado nombre de usuario a " + ((UsuarioValueObject) session.getAttribute("usuario")).getNombreUsu());
	
	          } catch (UsuarioNoValidadoException unve) {
	              ugVO.setRespOpcion("errorValidacion");
	              errors.add("errorValidacion", new ActionError(unve.getMessage()));
	          }
	          ugForm.setUsuariosGrupos(ugVO);
	          opcion = "verOcultoDatosBasicos";
      } else if ("insertarGrupo".equals(opcion)) {
	      	ugVO = ugForm.getUsuariosGrupos();
	      	int resultado = UsuariosGruposManager.getInstance().insertarGrupo(ugVO,params);
	      	if(resultado != -1) {
			      ugVO.setListaUnidadesOrganicas(new Vector());
	      	  ugVO.setRespOpcion("insertarGrupo");
	      	}
	      	ugForm.setUsuariosGrupos(ugVO);
	      	opcion = "verOculto";
      } else if ( "eliminarGrupo".equals(opcion) ) {
	        ugVO = ugForm.getUsuariosGrupos();
	        String codGrupo = request.getParameter("codGrupo");
	        int resultado = UsuariosGruposManager.getInstance().eliminarGrupo(codGrupo,params);
	        if(resultado != -1) {
			  ugVO.setListaUnidadesOrganicas(new Vector());
	      	  ugVO.setRespOpcion("eliminarGrupo");
	      	}
	      	ugForm.setUsuariosGrupos(ugVO);
	      	opcion = "verOculto";	
      } else if ( "modificarGrupo".equals(opcion) ) {
	      	ugVO = ugForm.getUsuariosGrupos();
	      	int resultado = UsuariosGruposManager.getInstance().modificarGrupo(ugVO,params);
	      	if(resultado !=-1) {
			  ugVO.setListaUnidadesOrganicas(new Vector());
	      	  ugVO.setRespOpcion("modificarGrupo");
	      	}
	      	ugForm.setUsuariosGrupos(ugVO);
	      	opcion = "verOculto";
      } else if ( "verGrupos".equals(opcion) ) {
      	session.setAttribute("modo","verGrupos");
      	String listaOrganizaciones = request.getParameter("primero");
      	ugVO.setListaOrg(listaTemasSeleccionados(listaOrganizaciones));
      	ugVO.setListaOrganizaciones(OrganizacionesManager.getInstance().getListaOrganizaciones(params));
      	ugVO.setListaGrupos(UsuariosGruposManager.getInstance().getGrupos(params));
      	ugForm.setUsuariosGrupos(ugVO);
      	opcion = "verGrupos";
      } else if ( "verGruposAdmLocal".equals(opcion) ) {
      	session.setAttribute("modo","verGrupos");
      	String listaOrganizaciones = request.getParameter("primero");
      	ugVO.setListaOrg(listaTemasSeleccionados(listaOrganizaciones));
      	ugVO.setListaOrganizaciones(OrganizacionesManager.getInstance().getListaOrganizaciones(params));
      	ugVO.setListaAplicaciones(AplicacionesManager.getInstance().getListaAplicaciones(params));
      	ugVO.setListaGrupos(UsuariosGruposManager.getInstance().getGrupos(params));
      	ugVO.setCodEntidad(cEnt);
      	ugVO.setNombreEntidad(descEnt);
      	ugForm.setUsuariosGrupos(ugVO);
      	opcion = "verGrupos";
      } else if ( "verUnidOrganicas".equals(opcion) ) {
      	String listaOrganizaciones = request.getParameter("primero");
      	ugVO.setListaOrg(listaTemasSeleccionados(listaOrganizaciones));
      	ugVO.setListaOrganizaciones(OrganizacionesManager.getInstance().getListaOrganizaciones(params));
       	ugForm.setUsuariosGrupos(ugVO);

     	opcion = "verUnidOrganicas";
      } else if ( "verUnidOrganicasAdmLocal".equals(opcion) ) {
      	String listaOrganizaciones = request.getParameter("primero");
      	ugVO.setListaOrg(listaTemasSeleccionados(listaOrganizaciones));
      	ugVO.setListaOrganizaciones(OrganizacionesManager.getInstance().getListaOrganizaciones(params));
      	ugVO.setCodEntidad(cEnt);
      	ugVO.setNombreEntidad(descEnt);
      	ugForm.setUsuariosGrupos(ugVO);

                // seleccionar el jndi para el esquema apropiado
                String jndi = UsuariosGruposManager.getInstance().obtenerJNDI(
                        cEnt, cOrg, String.valueOf(usuario.getAppCod()), params);
                String[] paramsNuevos = new String[7];
                paramsNuevos[0] = params[0];
                paramsNuevos[6] = jndi;
                Vector nuevasUOR = UORsManager.getInstance().getListaUORs(true,paramsNuevos);
                m_Log.debug("Cargadas " + nuevasUOR.size() + " UORs");
                request.setAttribute("listaUORs", nuevasUOR);
                Vector nuevosCargos = CargosManager.getInstance().getListaUORs(paramsNuevos);
                m_Log.debug("Cargados " + nuevosCargos.size() + " Cargos");
                request.setAttribute("listaCargos", nuevosCargos);

      	opcion = "verUnidOrganicas";
                
            } else if ("cargarUORsCargos".equals(opcion)) {
                String codOrganizacion = request.getParameter("organizacion");
                String codEntidad = request.getParameter("entidad");
                
                ugVO.setCodOrganizacion(codOrganizacion);
                ugVO.setCodEntidad(codEntidad);
                ugVO.setCodAplicacion(String.valueOf(usuario.getAppCod()));
                
                String jndi = UsuariosGruposManager.getInstance().obtenerJNDI(
                        codEntidad, codOrganizacion, String.valueOf(usuario.getAppCod()), params);
                
                String[] paramsNuevos = new String[7];
                paramsNuevos[0] = params[0];
                paramsNuevos[6] = jndi;
                
                Vector nuevasUOR = UORsManager.getInstance().getListaUOROrdenPorDesc(paramsNuevos);
                m_Log.debug("Cargadas " + nuevasUOR.size() + " UORs");
                request.setAttribute("listaUORs", nuevasUOR);
                
                Vector nuevosCargos = CargosManager.getInstance().getListaUOROrdenPorDesc(paramsNuevos);
                m_Log.debug("Cargados " + nuevosCargos.size() + " Cargos");
                request.setAttribute("listaCargos", nuevosCargos);
                               
      } else if ( "buscarUsuariosOrganizacion".equals(opcion) ) {
         
            String nombre = request.getParameter("nombreT");
            String login = request.getParameter("loginT");
            m_Log.debug("nombre " + nombre + ",login: " + login);

            String codOrganizacion = request.getParameter("codOrganizacion");
            Vector consulta = new Vector();

            if(codOrganizacion != null && !"".equals(codOrganizacion)) {
                consulta = UsuariosGruposManager.getInstance().getUsuariosLocalConFiltroBusqueda(codOrganizacion,login,nombre,params);
            } else {
                consulta = UsuariosGruposManager.getInstance().getUsuariosFiltroBusqueda(login,nombre,params);
            }

            session.setAttribute("RelacionUsuarios",consulta);
            opcion = "cargar_pagina";                          
      }
       else if ( "cargarSeleccionProcedimientosRestringidos".equals(opcion) ) {

           /** Se carga la página de selección de de procedimientos restringidos para poder ser asignados al usuario
             * que tenga la directiva adecuada */
           // Se recuperan las organizaciones para que el usuario seleccione aquella con la que trabajar
           Vector organizaciones = OrganizacionesManager.getInstance().getListaOrganizaciones(params);
           m_Log.debug(" ===========> El número de organizaciones recuperadas es : " + organizaciones.size());
           request.setAttribute("organizaciones_seleccion_procedimiento",organizaciones);

           // Se recuperan los permisos que ya pueda tener concedido el usuario sobre algún procedimiento
           ugVO = ugForm.getUsuariosGrupos();
           m_Log.debug(" *********************************** USUARIO A MODIFICAR : " + ugVO.getCodUsuario() + " *************************");
           ArrayList<PermisoProcedimientosRestringidosVO> procedimientos = PermisoProcRestringidoManager.getInstance().getProcedimientosRestringidosPermisoUsuario(ugVO.getCodUsuario(), String.valueOf(usuario.getAppCod()), params);

            request.setAttribute("procedimientos_restringidos",procedimientos);
            
           // Se pasa el control a la jsp correspondiente
           opcion = "cargarSeleccionProcedimientosRestringidos";
      }else if("cargarListadoEntidades".equals(opcion)){
            m_Log.debug(" ==========> Se recuperan las entidades correspondientes a una determinada organización");

            // Se recuperan las lista de entidades de una determinada organización
            String codOrganizacion = request.getParameter("codOrganizacion");
            es.altia.agora.business.util.GeneralValueObject gVO = new es.altia.agora.business.util.GeneralValueObject();
            gVO.setAtributo("codOrganizacion",codOrganizacion);
            Vector entidades = EntidadesManager.getInstance().getListaEntidades(gVO,params);
            request.setAttribute("entidades_seleccion_procedimientos_restringidos",entidades);
            request.setAttribute("opcion_oculto","cargarEntidades");
            
      }
       else if ( "cargarProcedimientosRestringidos".equals(opcion) ) {
          
           String codOrganizacion = request.getParameter("codOrganizacion");
           String codEntidad        = request.getParameter("codEntidad");
           m_Log.debug(" ======> codOrganizacion " + codOrganizacion + ", codEntidad: " + codEntidad);

           ugVO = ugForm.getUsuariosGrupos();
           m_Log.debug(" *********************************** USUARIO QUE SE ESTÁ MODIFICANDO : " + ugVO.getCodUsuario() + " *************************");

           String codAplicacion = null;
           if(ugVO.getCodAplicacion()!=null && !"".equals(ugVO.getCodAplicacion()) && !"null".equals(ugVO.getCodAplicacion()))
               codAplicacion = ugVO.getCodAplicacion();
           else
               codAplicacion = String.valueOf(usuario.getAppCod());

           ArrayList<PermisoProcedimientosRestringidosVO> procs = DefinicionProcedimientosManager.getInstance().getProcedimientosRestringidos(codAplicacion,codOrganizacion,codEntidad,params);
           request.setAttribute("procedimientos_restringidos_combo",procs);
           request.setAttribute("opcion_oculto","cargarProcedimientos");
      }else if("buscarTodasUOR".equalsIgnoreCase(opcion)){
        if(m_Log.isDebugEnabled()) m_Log.debug("buscarTodasUOR : BEGIN");
        if(m_Log.isDebugEnabled()) m_Log.debug("Recuperamos los parámetros de código de entidad, código de la organizacion y cargo");
        String codOrganizacion = (String) request.getParameter("codOrg");
        String descOrganizacion = (String) request.getParameter("descOrg");
        String codEntidad = (String) request.getParameter("codEnt");
        String descEntidad = (String) request.getParameter("descEnt");
        String codCargo = (String) request.getParameter("codCargo");
        String descCargo = (String) request.getParameter("descCargo");
        String listaUorsCascada = (String) request.getParameter("listaUorsCascada");
        String infoListasUors = (String) request.getParameter("infoListasUors");
        String permisoCascadaChecked = (String) request.getParameter("permisoCascadaChecked");
        String tieneDescendencia = (String) request.getParameter("tieneDescendencia");
        String infoPadreListasUors = (String) request.getParameter("infoPadreListasUors");
        
        if(m_Log.isDebugEnabled()){
            m_Log.debug("codOrganizacion = " + codOrganizacion);
            m_Log.debug("descOrganizacion = " + descOrganizacion);
            m_Log.debug("codEntidad = " + codEntidad);
            m_Log.debug("descEntidad = " + descEntidad);
            m_Log.debug("codCargo = " + codCargo);
            m_Log.debug("descCargo = " + descCargo);
            m_Log.debug("listaUorsCascada = " + listaUorsCascada);
            m_Log.debug("infoListasUors = " + infoListasUors);
            m_Log.debug("permisoCascadaChecked = " + permisoCascadaChecked);
            m_Log.debug("tieneDescendencia = " + tieneDescendencia);
            m_Log.debug("infoPadreListasUors = " + infoPadreListasUors);
        }//if(m_Log.isDebugEnabled())
                
        String jndi = UsuariosGruposManager.getInstance().obtenerJNDI(
        codEntidad, codOrganizacion, String.valueOf(usuario.getAppCod()), params);
                
        String[] paramsNuevos = new String[7];
        paramsNuevos[0] = params[0];
        paramsNuevos[6] = jndi;
                
        Vector nuevasUOR = UORsManager.getInstance().getListaUOROrdenPorDesc(paramsNuevos);
        m_Log.debug("Cargadas " + nuevasUOR.size() + " UORs");
        ugForm.setRespOpcion("cargarTodasLasUor");
        request.setAttribute("listaUORs", nuevasUOR);

        request.setAttribute("codOrganizacion", codOrganizacion);
        request.setAttribute("descOrganizacion", descOrganizacion);
        request.setAttribute("codEntidad", codEntidad);
        request.setAttribute("descEntidad", descEntidad);
        request.setAttribute("codCargo", codCargo);
        request.setAttribute("descCargo", descCargo);
        request.setAttribute("listaUorsCascada", listaUorsCascada);
        request.setAttribute("infoListasUors", infoListasUors);
        request.setAttribute("permisoCascadaChecked", permisoCascadaChecked);
        request.setAttribute("tieneDescendencia", tieneDescendencia);
        request.setAttribute("infoPadreListasUors", infoPadreListasUors);

        if(m_Log.isDebugEnabled()) m_Log.debug("buscarTodasUOR : END");
        opcion = "cargarTodasLasUOR";
      }//if(opcion)
    } else { 
      m_Log.debug("MantAnotacionRegistroAction --> no hay usuario");
      opcion = "no_usuario";
    }
          /* Redirigimos al JSP de salida*/
          if (!errors.isEmpty()) saveErrors(request, errors);
          m_Log.info("<=================== UsuariosGruposAction ===================");
          return (mapping.findForward(opcion));

      }

private Vector listaTemasSeleccionados(String listTemasSelecc) {
  Vector lista = new Vector();
  StringTokenizer valores = null;
  if (listTemasSelecc != null) {
    valores = new StringTokenizer(listTemasSelecc,"§¥,",false);
    while (valores.hasMoreTokens()) {
      String valor = valores.nextToken();
      lista.addElement(valor);
      if (m_Log.isDebugEnabled()) m_Log.debug("--> " + valor);
    }
  }
  return lista;
}

private Vector listaDistintaTemasSeleccionados(String listTemasSelecc) {
  Vector lista = new Vector();
  StringTokenizer valores = null;
  if (listTemasSelecc != null) {
    valores = new StringTokenizer(listTemasSelecc,"§¥,",false);
    while (valores.hasMoreTokens()) {
      String valor = valores.nextToken();
      if(!lista.contains(valor)) lista.addElement(valor);
      if (m_Log.isDebugEnabled()) m_Log.debug("--> " + valor);
    }
  }
  return lista;
}

/**
 * A partir de la lista de directivas contenida en el VO construye una lista de
 * codigos y una lista de descripciones de las aplicaciones que contienen
 * directivas y las asigna al form para usarlas en la jsp. Tambien obtiene los
 * mensajes correspondientes a las directivas.
 * @param ugForm form de esta action
 * @param ugVO VO devuelto por el DAO al buscar los datos de un usuario
 * @param usuario VO del usuario actual
 */
private void procesarDirectivas(UsuariosGruposForm ugForm, UsuariosGruposValueObject ugVO, UsuarioValueObject usuario) {
    // Procesamiento de las directivas, obtenemos los mensajes
    // correspondientes y listado de las aplicaciones que tienen directivas.
    TraductorAplicacionBean traductor = new TraductorAplicacionBean();
    traductor.setApl_cod(usuario.getAppCod());
    traductor.setIdi_cod(usuario.getIdioma());
    Vector<String> codAplDirectivas = new Vector<String>();
    Vector<String> descAplDirectivas = new Vector<String>();
    String aplAnterior = "";
	
    for (DirectivaVO dir : ugVO.getDirectivas()) {
        dir.setMensaje(traductor.getDescripcion(dir.getMensaje()));
        if (!dir.getAplCod().equals(aplAnterior)) {
            aplAnterior = dir.getAplCod();
            codAplDirectivas.add(dir.getAplCod());
            descAplDirectivas.add(dir.getAplDesc());
        }
        m_Log.debug("DIRECTIVA: " + dir);
    }
    ugForm.setCodAplDirectivas(codAplDirectivas);
    ugForm.setDescAplDirectivas(descAplDirectivas);
}


/**
 * Comprueba si un String contiene dígitos numéricos
 * @param cadena
 * @return boolean
 */
private boolean isInteger(String cadena){
    boolean exito = false;
    try{
        Integer.parseInt(cadena);
        exito = true;
    }
    catch(NumberFormatException e){
        m_Log.error(e.getMessage());
    }
    
    return exito;
}



/**
 * Mapea un vector de String que contiene los permisos de un usuario sobre procedimientos restringidos, en un ArrayList<PermisoProcedimientosRestringidosVO>,
 * para que sea entendido por la capa modelo
 * @param permisos: Vector de String con cadena de tipo [COD_MUNICIPIO]-[COD_PROCEDIMIENTO]
 * @param codUsuario: Código del usuario del sistema que se da de alta o se modifica
 * @return  ArrayList<PermisoProcedimientosRestringidosVO>
 */
private ArrayList<PermisoProcedimientosRestringidosVO> tratarPermisosSobreProcedimientosRestringidos(Vector permisos,String codUsuario){
    ArrayList<PermisoProcedimientosRestringidosVO> salida = new ArrayList<PermisoProcedimientosRestringidosVO>();

    for(int i=0;permisos!=null && i<permisos.size();i++){
        String permiso = (String)permisos.get(i);
        String[] datos  = permiso.split(";");

        if(datos!=null && datos.length>=5){
            PermisoProcedimientosRestringidosVO p = new PermisoProcedimientosRestringidosVO();
            p.setCodMunicipio(datos[0]);
            p.setDescOrganizacion(datos[1]);
            p.setCodEntidad(datos[2]);
            p.setDescEntidad(datos[3]);
            p.setCodProcedimiento(datos[4]);            
            p.setCodUsuario(codUsuario);
            salida.add(p);
        }
    }// for
    return salida;
}




 public String mensajeVerificarAsignacionPermisosOficinasRegistro (String errores){
    
    String salida="";
    
    salida = "<SALIDA_CUENTA_USUARIO>";
            if(!"".equals(errores)){
                salida = salida + "<CODIGO_ERROR>NO_OK</CODIGO_ERROR>" ;
                salida = salida + "<ERRORES>";
                salida = salida + errores;
                salida = salida + "</ERRORES>";
            }else
                salida = salida + "<CODIGO_ERROR>0K</CODIGO_ERROR>" ;
            
    salida = salida + "</SALIDA_CUENTA_USUARIO>";
    return salida;
     
    }
    
   

}