package es.altia.agora.interfaces.user.web.escritorio;

import es.altia.agora.business.administracion.mantenimiento.persistence.IdiomasManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.UORsManager;
import es.altia.agora.business.escritorio.RegistroUsuarioValueObject;
import es.altia.agora.business.escritorio.UsuarioEscritorioValueObject;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.escritorio.persistence.UsuarioManager;
import es.altia.agora.business.terceros.ParametrosTerceroValueObject;
import es.altia.agora.business.terceros.mantenimiento.persistence.ParametrosTercerosManager;
import es.altia.agora.business.terceros.mantenimiento.persistence.ViasManager;
import es.altia.agora.business.util.LabelValueTO;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.util.CargaMenu;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.exception.TechnicalException;
import es.altia.common.exception.CriticalException;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

public final class EntradaAction extends ActionSession {

    public ActionForward performSession(ActionMapping mapping,
                                        ActionForm form,
                                        HttpServletRequest request,
                                        HttpServletResponse response)throws IOException, ServletException {

        if(m_Log.isDebugEnabled())m_Log.debug("Entro en EntradaAction");
        HttpSession session = request.getSession();
        //Borramos el LogonForm que existe en la session.
        if ((session.getAttribute("LogonForm") != null))
            session.removeAttribute("LogonForm");
        
        String opcion = request.getParameter("opcion");
        String cod = request.getParameter("codigo");
        request.setAttribute("codigo",cod);
        String app = request.getParameter("aplicacion");
        request.setAttribute("aplicacion",app);
        
        if (m_Log.isInfoEnabled()) m_Log.info("la opcion en el action es " + opcion);
        if(m_Log.isDebugEnabled())m_Log.debug("EntradaAction. cod " + cod);
        if(m_Log.isDebugEnabled())m_Log.debug("EntradaAction. app " + app);
        UsuarioManager usuarioManager = UsuarioManager.getInstance();
        ViasManager viasManager = ViasManager.getInstance();
        UsuarioValueObject usuarioVO = null;
        
        if ("abrirApp".equals(opcion) || "busquedaOrgs".equals(opcion)){
            if ("abrirApp".equals(opcion)) {
                if(session.getAttribute("usuario") != null) session.removeAttribute("usuario");
                if(session.getAttribute("url") != null) session.removeAttribute("url");
                UsuarioEscritorioValueObject usuarioEscritorioVO = (UsuarioEscritorioValueObject)session.getAttribute("usuarioEscritorio");

                usuarioVO = new UsuarioValueObject(usuarioEscritorioVO);
                usuarioVO.setDepCod(ConstantesDatos.REG_COD_DEP_DEFECTO);
                usuarioVO.setDep("DEPARTAMENTO POR DEFECTO (1)");
                usuarioVO.setAppCod(Integer.parseInt(cod));
                usuarioVO.setApp(app);
                usuarioVO.setCaminoContexto(request.getContextPath());
                Iterator iter = (usuarioEscritorioVO.getIconos()).iterator();
                int i = 0;
                while(iter.hasNext()){
                    String codApp = (String)iter.next();
                    iter.next();
                    String icoApp = (String)iter.next();
                    if (cod.equals(codApp)){
                        usuarioVO.setAppIco(icoApp);
                    }
                }
                
                session.setAttribute("usuario", usuarioVO);
            } else {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
            }
            
            // #291976: no borramos de la sesion la propiedad: el servicio de digitalizacion para el usuario de la sesion no varia durante la sesion
//            if(session.getAttribute("servicioDigitalizacionActivo") != null){
//                session.removeAttribute("servicioDigitalizacionActivo");             
//            }
            
            Vector org_ent = usuarioManager.buscaOrg(usuarioVO,"busquedaOrgs".equals(opcion)?"busqueda":"");
            
            // Vemos el número de entidades recuperadas. Si es cero y ha habido búsqueda se muestran todas y un mensaje de error.
            if (org_ent.size() == 0 && "busquedaOrgs".equals(opcion)) {
                org_ent = usuarioManager.buscaOrg(usuarioVO, "");
                request.setAttribute("noResultadosBusqueda", true);
            }
            
            // Recuperamos de configuracion el numero minimo de resultados para que aparezca la barra de busqueda 
            Config m_Conf = ConfigServiceHelper.getConfig("common");
            int minResultadosBusqueda;
            try {
              minResultadosBusqueda = Integer.parseInt(m_Conf.getString("Escritorio.minOrgsBusqueda"));
            } catch (CriticalException e) {                
                minResultadosBusqueda = 10; // Valor por defecto si no se encuentra la clave en common.properties 
            }
            
            if ((org_ent.size()>7 && usuarioVO.getAppCod() != ConstantesDatos.APP_ADMINISTRADOR_GENERAL) 
                 || "busquedaOrgs".equals(opcion)) {// Varias Entidades o busqueda con filtro realizada.
                
                if ((session.getAttribute("org_ent") != null)) session.removeAttribute("org_ent");                
                session.setAttribute("org_ent", org_ent);

                // Comprobamos si hay que dibujar la barra de busqueda
                // El vector tiene 7 elementos por Organizacion-Entidad
                int numOrgs = org_ent.size() / 7;
                
                // Si hay mas orgs que el minimo o venimos de buscar se dibuja la barra de busqueda
                if ((numOrgs >= minResultadosBusqueda) || "busquedaOrgs".equals(opcion)) {
                    request.setAttribute("mostrarBarraBusqueda","si");
                }
             
                if ("busquedaOrg".equals(opcion)) {
                    request.setAttribute("busqueda","busqueda");
                    return mapping.findForward("busqOrg");
               } else {
                    String json = "{\"selector\":\"listaOrg\"";
                    // Si hay mas orgs que el minimo o venimos de buscar se dibuja la barra de busqueda
                    if ((numOrgs >= minResultadosBusqueda) || "busquedaOrgs".equals(opcion)) {
                        json += ",\"mostrarBarraBusqueda\":\"si\"";
                    }
                    json +="}";
                    retornarJSON(json, response);
                    return null;
                }
            }else if(org_ent.size()>0){// Para una unica Entidad.
                int oc = -1;
                if(org_ent.elementAt(0)!=null && !"".equals(org_ent.elementAt(0))) oc = Integer.parseInt((String)org_ent.elementAt(0));
                usuarioVO.setOrgCod(oc);

                usuarioVO.setOrg((String)org_ent.elementAt(1));

                int ec = -1;
                if(org_ent.elementAt(2)!=null && !"".equals(org_ent.elementAt(2))) ec = Integer.parseInt((String)org_ent.elementAt(2));
                usuarioVO.setEntCod(ec);

                usuarioVO.setEnt((String)org_ent.elementAt(3));
                usuarioVO.setDtr((String)org_ent.elementAt(4));

                String ico = "";
                if(org_ent.elementAt(5)!=null) ico = (String)org_ent.elementAt(5);
                usuarioVO.setOrgIco(ico);

                int ine = -1;
                if(org_ent.elementAt(6)!=null && !"".equals(org_ent.elementAt(6))) ine = Integer.parseInt((String)org_ent.elementAt(6));
                usuarioVO.setOrgCodINE(ine);

                usuarioVO = UsuarioManager.getInstance().buscaCss(usuarioVO);
                
                // PARA SABER SI ES UN USUARIO DE SOLO SONSULTA EN EXPEDIENTES
                String soloConsultaExpedientes;
                if(usuarioVO.getAppCod() == ConstantesDatos.APP_GESTION_EXPEDIENTES) {
                    soloConsultaExpedientes = UsuarioManager.getInstance().soloConsultarExpedientes(usuarioVO);
                    if (m_Log.isDebugEnabled())m_Log.debug("el resultado es : " + soloConsultaExpedientes );
                    usuarioVO.setSoloConsultarExp(soloConsultaExpedientes);
                }

                // PARA SABER SI ES UN USUARIO DE SOLO SONSULTA EN PADRON
                String soloConsultaPadron;
                if(usuarioVO.getAppCod() == ConstantesDatos.APP_PADRON) {
                    soloConsultaPadron =UsuarioManager.getInstance().soloConsultarPadron(usuarioVO);
                    usuarioVO.setSoloConsultarPad(soloConsultaPadron);
                }

                cargarParametrosConexion(usuarioVO,session);

                boolean existeTramero = viasManager.existeTramero(usuarioVO.getParamsCon());
                if (existeTramero) {
                    usuarioVO.setExisteTramero("si");
                } else {
                    usuarioVO.setExisteTramero("no");
                }
                if (m_Log.isDebugEnabled()) m_Log.debug("existeTramero: "+usuarioVO.getExisteTramero());

                if(usuarioVO.getAppCod()==1 || usuarioVO.getAppCod()==997 || usuarioVO.getAppCod()==998){
                    Vector vecUOR = UsuarioManager.getInstance().buscaUnidadOrg(usuarioVO);
                    if(vecUOR.size() > 2){// Para varias UOR que registran.
                        if ((session.getAttribute("vecUOR") != null)) session.removeAttribute("vecUOR");
                        session.setAttribute("vecUOR", vecUOR);
                        String json = "{\"selector\":\"listaUor\"}";
                        retornarJSON(json, response);
                        return null;
                    }else if(vecUOR.size()>0){// Si solo existe una UOR que registra o no hay ninguna.
                        if(m_Log.isDebugEnabled())m_Log.debug("EntradaAction: unidadOrgCod " + vecUOR.elementAt(0));
                        if(m_Log.isDebugEnabled())m_Log.debug("EntradaAction: unidadOrg " + vecUOR.elementAt(1));

                        int uoc = -1;
                        if(vecUOR.elementAt(0)!=null && !"".equals(vecUOR.elementAt(0))) uoc = Integer.parseInt((String)vecUOR.elementAt(0));
                        usuarioVO.setUnidadOrgCod(uoc);
                        usuarioVO.setUnidadOrg((String)vecUOR.elementAt(1));

                        cargarParametros(usuarioVO,session);
                        String json = "{\"selector\":\"selectApp\",\"url\":\""+ usuarioManager.buscaURL(usuarioVO) + "\"}";
                        retornarJSON(json, response);
                        return null;
                    }else{
                        String json = "{\"selector\":\"noApp\"}";
                        retornarJSON(json, response);
                        return null;
                    }
                }else{
                    cargarParametros(usuarioVO,session);
                    String json = "{\"selector\":\"selectApp\",\"url\":\""+ usuarioManager.buscaURL(usuarioVO) + "\"}";
                    retornarJSON(json, response);
                    return null;
                }
            } else if(app.equals("1")) {
                Vector vecUOR = UsuarioManager.getInstance().buscaUnidadOrg(usuarioVO);
                //vecUOR contiene un codigo y un nombre por cada UOR que devuelve la función
                
                if(vecUOR.size() > 2){// Para varias UOR que registran.
                    if ((session.getAttribute("vecUOR") != null)) session.removeAttribute("vecUOR");
                    session.setAttribute("vecUOR", vecUOR);
                    String json = "{\"selector\":\"listaUor\"}";
                    retornarJSON(json, response);
                    return null;
                }else if(vecUOR.size()>0){// Si solo existe una UOR que registra

                    int uoc = -1;
                    if(vecUOR.elementAt(0)!=null && !"".equals(vecUOR.elementAt(0))) uoc = Integer.parseInt((String)vecUOR.elementAt(0));
                    usuarioVO.setUnidadOrgCod(uoc);

                    usuarioVO.setUnidadOrg((String)vecUOR.elementAt(1));

                    cargarParametros(usuarioVO,session);
                    String json = "{\"selector\":\"selectApp\",\"url\":\""+ usuarioManager.buscaURL(usuarioVO) + "\"}";
                    retornarJSON(json, response);
                    return null;
                } else {
                    String json = "{\"selector\":\"noApp\"}";
                    retornarJSON(json, response);
                    return null;
                }
            } else {
                String json = "{\"selector\":\"noApp\"}";
                retornarJSON(json, response);
                return null;
            }
        } else if ("uorSelec".equals(opcion)) {
            m_Log.info("******************** LISTA UOR" );
           usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
           int uoc = -1;
           if(m_Log.isDebugEnabled())m_Log.debug("EntradaAction: unidadOrgCod " + request.getParameter("unidadOrgCod"));
           if(m_Log.isDebugEnabled())m_Log.debug("EntradaAction: unidadOrg " + request.getParameter("unidadOrg"));

           if(request.getParameter("unidadOrgCod")!=null && !"".equals(request.getParameter("unidadOrgCod")))
               uoc = Integer.parseInt(request.getParameter("unidadOrgCod"));
            m_Log.info("******************** unidad organica" + uoc);
            usuarioVO.setUnidadOrgCod(uoc);
            usuarioVO.setUnidadOrg(request.getParameter("unidadOrg"));
            cargarParametros(usuarioVO,session);
            String json = "{\"selector\":\"selectApp\",\"url\":\""+ usuarioManager.buscaURL(usuarioVO) + "\"}";
            retornarJSON(json, response);
            return null;
        } else if ("orgSelec".equals(opcion)) {
           m_Log.info("******************** LISTA ORG" );
           usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
           if ((session.getAttribute("org_ent") != null)) session.removeAttribute("org_ent");

           //if (m_Log.isDebugEnabled())m_Log.debug("EntradaAction. orgCod" + request.getParameter("orgCod"));
           //if (m_Log.isDebugEnabled())m_Log.debug("EntradaAction. entCod" + request.getParameter("entCod"));

           int oc = -1;
           if(request.getParameter("orgCod")!=null && !"".equals(request.getParameter("orgCod"))) oc = Integer.parseInt(request.getParameter("orgCod"));
           usuarioVO.setOrgCod(oc);
           usuarioVO.setOrg(request.getParameter("org"));
           m_Log.info("******************** orgG"+usuarioVO.getOrg());
           m_Log.info("******************** orgG"+usuarioVO.getOrgCod());
           usuarioVO = UsuarioManager.getInstance().buscaCss(usuarioVO);
              m_Log.info("******************** orgG"+usuarioVO.getCss());
           //buscar css

           int ec = -1;
           if(request.getParameter("entCod")!=null && !"".equals(request.getParameter("entCod"))) ec = Integer.parseInt(request.getParameter("entCod"));
           usuarioVO.setEntCod(ec);
           usuarioVO.setEnt(request.getParameter("ent"));
           usuarioVO.setDtr(request.getParameter("dtr"));
           String ico = "";
           if(request.getParameter("ico")!=null) ico = request.getParameter("ico");
           usuarioVO.setOrgIco(ico);

           int ine = -1;
           if(request.getParameter("ine")!=null && !"".equals(request.getParameter("ine"))
                   && !"null".equals(request.getParameter("ine"))) ine = Integer.parseInt(request.getParameter("ine"));
           usuarioVO.setOrgCodINE(ine);


           // PARA SABER SI ES UN USUARIO DE SOLO SONSULTA EN EXPEDIENTES
                       String soloConsultaExpedientes;
                           if(usuarioVO.getAppCod() == ConstantesDatos.APP_GESTION_EXPEDIENTES) {
                                   soloConsultaExpedientes = UsuarioManager.getInstance().soloConsultarExpedientes(usuarioVO);
                                   if(m_Log.isDebugEnabled())m_Log.debug("el resultado es : " + soloConsultaExpedientes );
                                   usuarioVO.setSoloConsultarExp(soloConsultaExpedientes);
                           }

           if(m_Log.isDebugEnabled())m_Log.debug("EntradaAction. 7");
           // PARA SABER SI ES UN USUARIO DE SOLO SONSULTA EN PADRON
           /*
               String soloConsultaPadron = "no";
               if(usuarioVO.getAppCod() == 2) {
                   soloConsultaPadron = UsuarioManager.getInstance().soloConsultarPadron(usuarioVO);
                   usuarioVO.setSoloConsultarPad(soloConsultaPadron);
               }
               */


           cargarParametrosConexion(usuarioVO,session);

           boolean existeTramero = viasManager.existeTramero(usuarioVO.getParamsCon());
           if (existeTramero) {
               usuarioVO.setExisteTramero("si");
           } else {
               usuarioVO.setExisteTramero("no");
           }
           if (m_Log.isDebugEnabled()) m_Log.debug("existeTramero: "+usuarioVO.getExisteTramero());

           Config common = ConfigServiceHelper.getConfig("common");

           int APP_REGISTRO_SALIDA = common.getInt("APP_REGISTRO_SALIDA");
           int APP_REGISTRO_ENTRADA = common.getInt("APP_REGISTRO_ENTRADA");

                           if((usuarioVO.getAppCod()==ConstantesDatos.APP_REGISTRO_ENTRADA_SALIDA)
                   ||(usuarioVO.getAppCod() == APP_REGISTRO_ENTRADA)
                   ||(usuarioVO.getAppCod() == APP_REGISTRO_SALIDA)){
               Vector vecUOR = UsuarioManager.getInstance().buscaUnidadOrg(usuarioVO);

               if(vecUOR.size()>2){// Para varias UOR que registran.
                   if ((session.getAttribute("vecUOR") != null)) session.removeAttribute("vecUOR");
                   session.setAttribute("vecUOR", vecUOR);
                    String json = "{\"selector\":\"listaUor\"}";
                    retornarJSON(json, response);
                    return null;
               }else if(vecUOR.size()>0){// Si solo existe una UOR que registra o no hay ninguna.

                   if(m_Log.isDebugEnabled())m_Log.debug("EntradaAction: unidadOrgCod " + vecUOR.elementAt(0));
                   if(m_Log.isDebugEnabled())m_Log.debug("EntradaAction: unidadOrg " + vecUOR.elementAt(1));

                   usuarioVO.setUnidadOrgCod(Integer.parseInt((String)vecUOR.elementAt(0)));
                   usuarioVO.setUnidadOrg((String)vecUOR.elementAt(1));

                   cargarParametros(usuarioVO,session);
                    String json = "{\"selector\":\"selectApp\",\"url\":\""+ usuarioManager.buscaURL(usuarioVO) + "\"}";
                    retornarJSON(json, response);
                    return null;
               }else {
                    String json = "{\"selector\":\"noApp\"}";
                    retornarJSON(json, response);
                    return null;
               }

           }else{
               cargarParametros(usuarioVO,session);
                String json = "{\"selector\":\"selectApp\",\"url\":\""+ usuarioManager.buscaURL(usuarioVO) + "\"}";
                retornarJSON(json, response);
                return null;
           }
        }
        String json = "{\"selector\":\"noApp\"}";
        retornarJSON(json, response);
        return null;
    }

    /**
     * Metodo utilizado para introducir en la sesión en número de idiomas existentes para que en caso de que exista
     *  más de uno pueda seleccionarse dentro de la aplicación el correcto
     * @param usuarioVO Datos del usuario en donde se encuentran los parametros de conexion
     */
    private void numeroIdiomas(UsuarioValueObject usuarioVO) {
        
        ////// OSCAR
        m_Log.debug(">>>>>>>>> numeroIdiomas");
        ////// OSCAR
        
        String[] params = usuarioVO.getParamsCon();
        IdiomasManager idiomasManager = IdiomasManager.getInstance();
        Vector listaIdiomas = idiomasManager.getListaIdiomas(params);
        //IdiomasManagerAdapter idiomasManagerAdapter = IdiomasManagerAdapter.getInstance(getServlet());
        //Vector listaIdiomas = idiomasManagerAdapter.getListaIdiomas(params);
        if (listaIdiomas.size() > 1) {
            usuarioVO.setMultipleIdioma(true);
        } else {
            usuarioVO.setMultipleIdioma(false);
        }
    }

    private void cargarParametrosConexion(UsuarioValueObject usuarioVO, HttpSession session) throws TechnicalException {
        
        ////// OSCAR
        m_Log.debug(">>>>>>>>> cargarParametrosConexion");
        ////// OSCAR
        
        if(session.getAttribute("conexion") != null) session.removeAttribute("conexion");
        UsuarioManager usuarioManager = UsuarioManager.getInstance();
        String[] conexion = usuarioManager.loadParametrosConexion(usuarioVO);
        usuarioVO.setParamsCon(conexion);
    }

    private void cargarParametros(UsuarioValueObject usuarioVO, HttpSession session){
        
        m_Log.debug(">>>>>>>>> cargarParametros");
        
        UsuarioManager usuarioManager = UsuarioManager.getInstance();
        CargaMenu cargaMenu = new CargaMenu();
        session.setAttribute("menuAppUsu",cargaMenu.getMenu(usuarioVO));

        ////////////////////// SE RECUPERA LA LISTA DE IDIOMAS Y SE GUARDA EN LA SESIÓN SI NO LO ESTÁ /////////
        if(session.getAttribute(ConstantesDatos.LISTA_IDIOMAS)==null){
            String[] paramsConexion = usuarioVO.getParamsCon();                                               
            IdiomasManager idiomasManager = IdiomasManager.getInstance();
            ArrayList<LabelValueTO> listaIdiomas = idiomasManager.getListIdiomasLabel(paramsConexion,usuarioVO.getIdioma());             
            session.setAttribute(ConstantesDatos.LISTA_IDIOMAS,listaIdiomas);
        }
        
        if(usuarioVO.getAppCod()!=ConstantesDatos.APP_WEB_CIUDADANO){
            ParametrosTercerosManager parametrosTercerosManager = ParametrosTercerosManager.getInstance();
            ParametrosTerceroValueObject ptVO = parametrosTercerosManager.getParametrosTerceros(usuarioVO.getOrgCod(), usuarioVO.getParamsCon());

            Config common = ConfigServiceHelper.getConfig("common");

            int salida = common.getInt("APP_REGISTRO_SALIDA");
            int entrada = common.getInt("APP_REGISTRO_ENTRADA");

            if ((salida == usuarioVO.getAppCod())
                    || (entrada == usuarioVO.getAppCod())) {
                usuarioVO = usuarioManager.buscaMantenimiento(usuarioVO);
                registroDep(usuarioVO, session);

            } else {
                switch (usuarioVO.getAppCod()){
                    case ConstantesDatos.APP_REGISTRO_ENTRADA_SALIDA:
                        usuarioVO = usuarioManager.buscaMantenimiento(usuarioVO);
                        registroDep(usuarioVO, session);
                        break;
                }
            }

            if(session.getAttribute("parametrosTercero") != null) session.removeAttribute("parametrosTercero");
            session.setAttribute("parametrosTercero",ptVO);
        }
        numeroIdiomas(usuarioVO);
        
        usuarioManager.auditarAccesoModulo(usuarioVO.getOrgCod(), usuarioVO.getAppCod(), usuarioVO.getIdUsuario());
    }
    
    private void registroDep(UsuarioValueObject usuarioVO, HttpSession session) {
        if (session.getAttribute("registroUsuario") != null) session.removeAttribute("registroUsuario");
        RegistroUsuarioValueObject registroUsuarioVO;
        boolean uorDigitalizacion = false;
        if(m_Log.isDebugEnabled()) m_Log.debug("Recuperamos el código de la oficina de registro");
        Integer codOficinaRegistro = UsuarioManager.getInstance().getCodOficinaRegistro(usuarioVO.getIdUsuario(), usuarioVO.getUnidadOrgCod(), usuarioVO.getParamsCon());
        //Cargamos el numero de permisos en oficinas de registro que tiene el usuario.
        if(m_Log.isDebugEnabled()) m_Log.debug("Recuperamos el número de oficinas de registro en las cuales tenemos permisos");
        Integer numPermisos = UsuarioManager.getInstance().getNumOficinasRegistroPermiso(usuarioVO.getOrgCod(),usuarioVO.getIdUsuario(), usuarioVO.getUnidadOrgCod(), usuarioVO.getParamsCon());
        session.setAttribute("numPermisosOficinaRegistro", numPermisos);
        if (usuarioVO.getDepCod() != -1 && usuarioVO.getUnidadOrgCod() != -1 && (usuarioVO.getUnidadOrg() != null && !usuarioVO.getUnidadOrg().equals(""))) {
            int ofi = -1;
            if(codOficinaRegistro!=null)
                ofi = codOficinaRegistro.intValue();
                
            uorDigitalizacion = UORsManager.getInstance().comprobarUorPermiteDigitalizacion(ofi, usuarioVO.getParamsCon());
            if(m_Log.isDebugEnabled()) m_Log.debug("Recuperamos si la oficina admite digitalización : "+uorDigitalizacion);
            
            registroUsuarioVO = new RegistroUsuarioValueObject(usuarioVO.getDepCod(), usuarioVO.getUnidadOrgCod(), usuarioVO.getUnidadOrg(), ofi, uorDigitalizacion);
        } else {
            String depCod = m_ConfigTechnical.getString("PAR.RegistroGeneral.depCod");
            String unidadOrgCod = m_ConfigTechnical.getString("PAR.RegistroGeneral.unidadOrgCod");
            String unidadOrg = "";
            if (usuarioVO.getIdioma() == 1) unidadOrg = m_ConfigTechnical.getString("PAR.RegistroGeneral.unidadOrg_1");
            else if (usuarioVO.getIdioma() == 2) unidadOrg = m_ConfigTechnical.getString("PAR.RegistroGeneral.unidadOrg_2");
            registroUsuarioVO = new RegistroUsuarioValueObject(Integer.parseInt(depCod), Integer.parseInt(unidadOrgCod), unidadOrg, codOficinaRegistro);
        }//if (usuarioVO.getDepCod() != -1 && usuarioVO.getUnidadOrgCod() != -1 && (usuarioVO.getUnidadOrg() != null && !usuarioVO.getUnidadOrg().equals(""))) 
        session.setAttribute("registroUsuario", registroUsuarioVO);
    }//registroDep
    
      /**
     * Método llamado para devolver un String en formato JSON al cliente que ha realiza la petición 
     * a alguna de las operaciones de este action
     * @param json: String que contiene el JSON a devolver
     * @param response: Objeto de tipo HttpServletResponse a través del cual se devuelve la salida
     * al cliente que ha realizado la solicitud
     */
    private void retornarJSON(String json,HttpServletResponse response){
        
        try{
            if(json!=null){
                response.setCharacterEncoding("ISO-8859-15");                
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
                out.close();
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
}//class
