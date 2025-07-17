
package es.altia.agora.interfaces.user.web.sge;

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

import es.altia.agora.business.administracion.mantenimiento.persistence.CargosManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.UORsManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.integracionsw.AvanzarRetrocederSWVO;
import es.altia.agora.business.integracionsw.ComprobacionEstructuraVO;
import es.altia.agora.business.integracionsw.ParametroConfigurableVO;
import es.altia.agora.business.integracionsw.persistence.DefinicionOperacionesSWManager;
import es.altia.agora.business.integracionsw.persistence.DefinicionSWTramitacionManager;
import es.altia.agora.business.sge.DefinicionCampoValueObject;
import es.altia.agora.business.sge.DefinicionProcedimientosValueObject;
import es.altia.agora.business.sge.DefinicionTramitesValueObject;
import es.altia.agora.business.sge.TablasIntercambiadorasValueObject;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.persistence.DefinicionTramitesManager;
import es.altia.agora.business.sge.persistence.TramitacionManager;
import es.altia.agora.business.util.ElementoListaValueObject;
import es.altia.agora.interfaces.user.web.helper.ActionHelper;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.notificaciones.NotificacionesUtil;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.escritorio.UsuarioEscritorioValueObject;
import es.altia.agora.business.escritorio.persistence.UsuarioManager;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.ModuloIntegracionExternoManager;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.OperacionModuloIntegracionExternoVO;
import es.altia.flexia.notificacion.persistence.NotificacionManager;
import es.altia.flexia.tramitacion.externa.plugin.TramitacionExternaBase;
import es.altia.flexia.tramitacion.externa.plugin.TramitacionExternaCargador;
import java.io.PrintWriter;
import java.util.ArrayList;

public final class DefinicionTramitesAction	extends ActionSession {

  public ActionForward performSession(ActionMapping mapping,
              ActionForm form,
              HttpServletRequest request,
              HttpServletResponse response) throws IOException, ServletException {

    m_Log.debug("================ DefinicionTramitesAction ==================>");
    ActionHelper myActionHelper =	new ActionHelper(getLocale(request), getResources(request));
    Config confExpediente = ConfigServiceHelper.getConfig("Expediente");

    // Validaremos los parametros	del request	especificados
    HttpSession	session = request.getSession();
    String opcion ="";
    String plazoFin = confExpediente.getString("plazo.finalizacion");
    
    
     m_Log.debug("**************  "+plazoFin);
    if ((session.getAttribute("usuario") != null)) {
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
      String[] params	= usuario.getParamsCon();
      int	cod_dep;
      int	cod_uni;
      cod_dep= usuario.getDepCod();
      cod_uni= usuario.getUnidadOrgCod();

      // Si usuario en sesion es nulo --> error.

      ActionErrors errors =	new ActionErrors();

      DefinicionTramitesValueObject defTramVO	= new	DefinicionTramitesValueObject();
      DefinicionTramitesForm defTramForm = null;
      
      if (form == null) {
        m_Log.debug("Rellenamos el	form de Definicion de Tramites");
        form = new DefinicionTramitesForm();
        if ("request".equals(mapping.getScope()))
        request.setAttribute(mapping.getAttribute(), form);
        else
        session.setAttribute(mapping.getAttribute(), form);
      }

      defTramForm = (DefinicionTramitesForm)form;
      session.setAttribute("configurandoSW", "0");
      opcion = request.getParameter("opcion");
      if (m_Log.isInfoEnabled()) m_Log.info("la opcion en el action es	" + opcion);

      String posicion	= "";
      String[] paramsDiputacion =	new String[7];
  
        if (opcion.equals("inicio")) {
            defTramVO = defTramForm.getDefinicionTramites();     
            if (m_Log.isInfoEnabled()) m_Log.info("la opcion en el action es	" + opcion);
            String enviar = request.getParameter("enviar");
            session.setAttribute("enviar", enviar);
            String codMunicipio = request.getParameter("codMunicipio");
            defTramVO.setCodMunicipio(codMunicipio);
            int plazo=0;
            plazo=Integer.parseInt(plazoFin);
            defTramVO.setPlazoFin(plazo);
            //Listas
            defTramVO.setListaClasifTramite(DefinicionTramitesManager.getInstance().getListaClasifTramites(params));
            defTramVO.setListaTramites(DefinicionTramitesManager.getInstance().getListaTramites(defTramVO, params));
            defTramVO.setListaExpRel(TramitacionManager.getInstance().getListaProcedimientos(usuario, params));
            defTramVO.setListaCargos(CargosManager.getInstance().getListaUORs(params));
            defTramForm.setListaDocsPresentados(DefinicionProcedimientosManager.getInstance().getListaDocumentos(defTramVO, params));
            m_Log.debug("Cargadas " + UORsManager.getInstance().getListaUORs(false,params).size() + " UORs");
            defTramVO.setListaUnidadesTramitadoras(UORsManager.getInstance().getListaUORs(false,params));
            m_Log.debug("\n\n\n NOTIFICACIONESSSSS "+NotificacionesUtil.getTiposNotificacion() );
            defTramForm.setListaTiposNotificacionElectronica(NotificacionesUtil.getTiposNotificacion());

            m_Log.debug("Cargar Lista de Servicios Web Disponibles");
            Vector listaSW = new Vector();
            try {
                listaSW = DefinicionOperacionesSWManager.getInstance().getAllPublishedOperations(params);
            } catch (InternalErrorException e) {
                m_Log.error("SE HA PRODUCIDO UN ERROR A LA HORA DE CARGAR LA LISTA DE SERVICIOS WEB PUBLICADOS");
                e.printStackTrace();
            }

           
            // SE COMPRUEBA SI EL PROCEDIMIENTO TIENE ACTIVADO UN MÓDULO EXTERNO Y SI LO TIENE, SE RECUPERAN LAS OPERACIONES
            // DISPONIBLES DEL MISMO
            try{
                ModuloIntegracionExternoFactoria factoriaModulos = ModuloIntegracionExternoFactoria.getInstance();
                if(factoriaModulos.tieneModulosIntegracionActivados(usuario.getOrgCod())){
                    m_Log.debug(" ******* el procedimiento tiene activado un módulo de integración externo");
                    // Si el procedimiento tiene activado un módulo externo
                    ArrayList<ModuloIntegracionExterno> modulos = factoriaModulos.getImplClass(usuario.getOrgCod());
                    for(int i=0;modulos!=null && i<modulos.size();i++){
                        ModuloIntegracionExterno modulo = modulos.get(i);
                        m_Log.debug(" ******* Nombre del módulo: " + modulo.getNombreModulo());
                        m_Log.debug(" ******* Descripción del módulo: " + modulo.getDescripcionModulo());
                        ArrayList<OperacionModuloIntegracionExternoVO> operaciones = modulo.getListaOperacionesDisponibles();
                        m_Log.debug(" ******* Número de operaciones recuperadas del módulo: " + operaciones.size());
                        listaSW = this.operacionesSW_Modulo(listaSW, operaciones);
                    }// for

                }//if
            }catch(Exception e){
                e.printStackTrace();
            }

           
           
            defTramVO.setListaWebServices(listaSW);
            defTramVO.setListasCondEntrada(new Vector());
            defTramVO.setListaDocumentos(new Vector());
            defTramVO.setListaEnlaces(new Vector());
            defTramVO.setListaCampos(new Vector());
            defTramVO.setListaCamposTramitesCondEntrada(new Vector());
            defTramVO.setListaConfSW(new Vector());
            defTramVO.setListaAgrupaciones(new Vector());

            defTramVO.setTramiteEliminado("");
            defTramVO.setInstrucciones("");

            defTramForm.setDefinicionTramites(defTramVO);
            // Se vacía la lista de plugin con pantalla de tramitación externa
            
             /*** SE RECUPERAN LOS PLUGIN DE TRAMITACION EXTERNA ACTIVOS PARA EL TRÁMITE ***/
            TramitacionExternaCargador cargador = TramitacionExternaCargador.getInstance();
            //ArrayList<TramitacionExternaBase>  plugins = cargador.getPluginDisponibles(usuario.getOrgCod(), Integer.parseInt(defTramVO.getCodigoTramite()), defTramVO.getTxtCodigo());
            ArrayList<TramitacionExternaBase>  plugins = cargador.getPluginDisponibles(usuario.getOrgCod());
            m_Log.debug(" ********************* Número de plugins a cargar: " + plugins.size() + " **********************************");
            defTramVO.setPluginPantallasTramitacionExterna(plugins);
            defTramVO.setCodPluginPantallaTramitacionExterna(null);
            defTramVO.setCodUrlPluginPantallaTramitacionExterna(null);
            defTramVO.setUrlPluginPantallaTramitacionExterna(null);
            defTramVO.setImplClassPluginPantallaTramitacionExterna(null);

            defTramForm.setNotificacionElectronicaObligatoria("off");
            defTramForm.setCertificadoOrganismoFirmaNotificacion("off");
            defTramForm.setListaDepartamentosNotificacion(DefinicionTramitesManager.getInstance().getDepartamentosNotificacionSNE(params));


            /******************************************************************************/
            opcion = "inicio";

        } else if (opcion.equals("enviar")) {
            defTramVO = defTramForm.getDefinicionTramites();
            /** PRUEBA **/
            String codMunicipioAux = request.getParameter("codMunicipio");
            defTramVO.setCodMunicipio(Integer.toString(usuario.getOrgCod()));
            /** PRUEBA **/
            String enviar = request.getParameter("enviar");
            session.setAttribute("enviar", enviar);
            String importar = request.getParameter("importar");
            if (m_Log.isDebugEnabled()) m_Log.debug("IMPORTAR	" + importar);
            if ("si".equals(importar)) {
                DefinicionProcedimientosValueObject defProcVO = new DefinicionProcedimientosValueObject();
                defProcVO.setCodMunicipio("0");
                defProcVO.setCodAplicacion(defTramVO.getCodAplicacion());
                String jndi = DefinicionProcedimientosManager.getInstance().obtenerJndi(defProcVO, params);
                paramsDiputacion[0] = params[0]; // "oracle";
                paramsDiputacion[6] = jndi;
                //Listas
                defTramVO.setListaClasifTramite(DefinicionTramitesManager.getInstance().getListaClasifTramites(paramsDiputacion));
                defTramVO.setListaExpRel(TramitacionManager.getInstance().getListaProcedimientos(usuario, paramsDiputacion));
                defTramVO.setListaCargos(CargosManager.getInstance().getListaUORs(params));
                defTramVO.setListaUnidadesTramitadoras(UORsManager.getInstance().getListaUORs(false,paramsDiputacion));
                defTramVO.setListaTramites(DefinicionTramitesManager.getInstance().getListaTramites(defTramVO, paramsDiputacion));
                defTramVO.setListaEnlaces(DefinicionTramitesManager.getInstance().getListaEnlaces(defTramVO, paramsDiputacion));

                defTramVO.setListaConfSW(DefinicionSWTramitacionManager.getInstance().getListaConfSW(
                		Integer.parseInt(defTramVO.getCodMunicipio()),
	  					defTramVO.getTxtCodigo(),Integer.parseInt(defTramVO.getCodigoTramite()),paramsDiputacion));
                
                defTramForm.setListaTiposNotificacionElectronica(NotificacionesUtil.getTiposNotificacion());

                m_Log.debug("Cargar Lista de Servicios Web Disponibles");
                Vector listaSW = new Vector();
                try {
                    listaSW = DefinicionOperacionesSWManager.getInstance().getAllPublishedOperations(paramsDiputacion);
                } catch (InternalErrorException e) {
                    m_Log.error("SE HA PRODUCIDO UN ERROR A LA HORA DE CARGAR LA LISTA DE SERVICIOS WEB PUBLICADOS");
                    e.printStackTrace();
                }
                defTramVO.setListaWebServices(listaSW);
                
                if (m_Log.isDebugEnabled()) m_Log.debug("siTRAMITAR	" + defTramVO.getListaWebServices());

                String codMunicipio = "0";
                String txtCodigo = request.getParameter("txtCodigo");

                defTramVO.setTxtCodigo(txtCodigo);

                Vector listaCodTramites = DefinicionTramitesManager.getInstance().getListaCodTramites(defTramVO, codMunicipio, paramsDiputacion);
                String codigoTramiteActual = defTramVO.getCodigoTramite();
                int i = 0;
                if (listaCodTramites.size() > 0) {
                    while (i < listaCodTramites.size() && !listaCodTramites.elementAt(i).equals(codigoTramiteActual))
                        i++;
                }
                i = (i == listaCodTramites.size()) ? 0 : i + 1;
                defTramVO.setTramiteActual(Integer.toString(i));
                defTramVO.setNumeroTramites(Integer.toString(listaCodTramites.size()));
                defTramVO = DefinicionTramitesManager.getInstance().getTramite(defTramVO, codMunicipio, paramsDiputacion);

                try{
                UsuarioManager mgr = UsuarioManager.getInstance();
                UsuarioEscritorioValueObject usuarioEscritorioVO = mgr.buscaUsuario(Integer.parseInt(defTramVO.getCodigoOtroUsuarioFirma()));
                defTramVO.setNombreOtroUsuarioFirma(usuarioEscritorioVO.getLogin());
                }catch(NumberFormatException e)
                {
                    m_Log.error("SE HA PRODUCIDO UN ERROR en el parseInt");
                    // e.printStackTrace();
                     defTramVO.setNombreOtroUsuarioFirma(null);
                }
                
                
            } else {
                //Listas
                defTramVO.setListaClasifTramite(DefinicionTramitesManager.getInstance().getListaClasifTramites(params));
                defTramVO.setListaExpRel(TramitacionManager.getInstance().getListaProcedimientos(usuario, params));
                defTramVO.setListaCargos(CargosManager.getInstance().getListaUORs(params));
                defTramVO.setListaUnidadesTramitadoras(UORsManager.getInstance().getListaUORs(false,params));
                defTramVO.setListaTramites(DefinicionTramitesManager.getInstance().getListaTramites(defTramVO, params));
                defTramVO.setListaEnlaces(DefinicionTramitesManager.getInstance().getListaEnlaces(defTramVO, params));


                defTramVO.setListaConfSW(DefinicionSWTramitacionManager.getInstance().getListaConfSW(
                		Integer.parseInt(defTramVO.getCodMunicipio()),
	  					defTramVO.getTxtCodigo(),Integer.parseInt(defTramVO.getCodigoTramite()),params));

                String codProcedimiento = defTramVO.getTxtCodigo();
                m_Log.debug(" ******* código del procedimiento: " + codProcedimiento);
                // Se recupera la lista de operaciones disponibles del módulo externo si es que está configurado
                                

                defTramForm.setListaTiposNotificacionElectronica(NotificacionesUtil.getTiposNotificacion());
                m_Log.debug("Cargar Lista de Servicios Web Disponibles");
                Vector listaSW = new Vector();
                try {
                    listaSW = DefinicionOperacionesSWManager.getInstance().getAllPublishedOperations(params);
                    
                } catch (InternalErrorException e) {
                    m_Log.error("SE HA PRODUCIDO UN ERROR A LA HORA DE CARGAR LA LISTA DE SERVICIOS WEB PUBLICADOS");
                    e.printStackTrace();
                }



                // SE COMPRUEBA SI EL PROCEDIMIENTO TIENE ACTIVADO UN MÓDULO EXTERNO Y SI LO TIENE, SE RECUPERAN LAS OPERACIONES
                // DISPONIBLES DEL MISMO
                try{
                    ModuloIntegracionExternoFactoria factoriaModulos = ModuloIntegracionExternoFactoria.getInstance();
                    if(factoriaModulos.tieneModulosIntegracionActivados(usuario.getOrgCod())){
                        m_Log.debug(" ******* el procedimiento tiene activado un módulo de integración externo");
                        // Si el procedimiento tiene activado un módulo externo
                        ArrayList<ModuloIntegracionExterno> modulos = factoriaModulos.getImplClass(usuario.getOrgCod());
                        for(int i=0;modulos!=null && i<modulos.size();i++){
                            ModuloIntegracionExterno modulo = modulos.get(i);
                            m_Log.debug(" ******* Nombre del módulo: " + modulo.getNombreModulo());
                            m_Log.debug(" ******* Descripción del módulo: " + modulo.getDescripcionModulo());
                            ArrayList<OperacionModuloIntegracionExternoVO> operaciones = modulo.getListaOperacionesDisponibles();
                            m_Log.debug(" ******* Número de operaciones recuperadas del módulo: " + operaciones.size());
                            listaSW = this.operacionesSW_Modulo(listaSW, operaciones);
                        }// for

                    }//if
                }catch(Exception e){
                    e.printStackTrace();
                }
                


                defTramVO.setListaWebServices(listaSW);
                if (m_Log.isDebugEnabled()) m_Log.debug("noTRAMITAR	" + defTramVO.getListaWebServices());

                String codMunicipio = request.getParameter("codMunicipio");
                String txtCodigo = request.getParameter("txtCodigo");

                defTramVO.setTxtCodigo(txtCodigo);

                Vector listaCodTramites = DefinicionTramitesManager.getInstance().getListaCodTramites(defTramVO, codMunicipio, params);
                String codigoTramiteActual = defTramVO.getCodigoTramite();
                int i = 0;
                if (listaCodTramites.size() > 0) {
                    while (i < listaCodTramites.size() && !listaCodTramites.elementAt(i).equals(codigoTramiteActual))
                        i++;
                }
                i = (i == listaCodTramites.size()) ? 0 : i + 1;
                defTramVO.setTramiteActual(Integer.toString(i));
                defTramVO.setNumeroTramites(Integer.toString(listaCodTramites.size()));
                defTramVO = DefinicionTramitesManager.getInstance().getTramite(defTramVO, codMunicipio, params);
                try{
                UsuarioManager mgr = UsuarioManager.getInstance();
                UsuarioEscritorioValueObject usuarioEscritorioVO = mgr.buscaUsuario(Integer.parseInt(defTramVO.getCodigoOtroUsuarioFirma()));
                defTramVO.setNombreOtroUsuarioFirma(usuarioEscritorioVO.getLogin());
                }catch(NumberFormatException e){
                    m_Log.error("SE HA PRODUCIDO UN ERROR en el parseInt");
                   // e.printStackTrace();
                     defTramVO.setNombreOtroUsuarioFirma(null);
                }                
            }

            /*** SE RECUPERAN LOS PLUGIN DE TRAMITACION EXTERNA ACTIVOS PARA EL TRÁMITE ***/
            TramitacionExternaCargador cargador = TramitacionExternaCargador.getInstance();
            ArrayList<TramitacionExternaBase>  plugins = cargador.getPluginDisponibles(usuario.getOrgCod());
            m_Log.debug(" ********************* Número de plugins a cargar: " + plugins.size() + " **********************************");            
            defTramVO.setPluginPantallasTramitacionExterna(plugins);
            /******************************************************************************/
            
            //ini Campos de la ventana popUp de condiciones de entrada
            Vector camposCondEntrada = new Vector();
            Vector tramites = defTramVO.getListaTramites();
            for (int i = 0; i < tramites.size(); i++) {
                ElementoListaValueObject elemListVO = (ElementoListaValueObject) tramites.get(i);
                String codTramite = elemListVO.getIdentificador();
                m_Log.debug("TRAMITE " + codTramite + " --> " + elemListVO.getDescripcion());
                DefinicionTramitesValueObject defTramVOAux = new DefinicionTramitesValueObject();
                String codMunicipio = defTramVO.getCodMunicipio();
                defTramVOAux.setCodMunicipio(codMunicipio);
                defTramVOAux.setTxtCodigo(defTramVO.getTxtCodigo());
                defTramVOAux.setCodigoTramite(codTramite);
                defTramVOAux = DefinicionTramitesManager.getInstance().getTramite(defTramVOAux, codMunicipio, params);
                Vector listaCodCampos = defTramVOAux.getListaCodCampos();
                Vector listaCampos = defTramVOAux.getListaCampos();
                for (int j = 0; j < listaCampos.size(); j++) {
                    DefinicionCampoValueObject dCVO = (DefinicionCampoValueObject) listaCampos.elementAt(j);
                    m_Log.debug("CAMPO : " + dCVO.getCodCampo() + " ... " + dCVO.getDescCampo());
                    camposCondEntrada.add(listaCampos.elementAt(j));
                }
            }
            defTramVO.setListaCamposTramitesCondEntrada(camposCondEntrada);
            //fin Campos de la ventana popUp de condiciones de entrada
            defTramForm.setListaDocsPresentados(DefinicionProcedimientosManager.getInstance().getListaDocumentos(defTramVO, params));
            defTramForm.setDefinicionTramites(defTramVO);
            
            defTramForm.setNotificacionElectronicaObligatoria((defTramVO.getNotificacionElectronicaObligatoria())?"on":"off");
            defTramForm.setCertificadoOrganismoFirmaNotificacion((defTramVO.getCertificadoOrganismoFirmaNotificacion())?"on":"off");
            defTramForm.setNotificacionElectronicaObligatoriaValue((defTramVO.getNotificacionElectronicaObligatoria())?"on":"off");
            defTramForm.setCertificadoOrganismoFirmaNotificacionValue((defTramVO.getCertificadoOrganismoFirmaNotificacion())?"on":"off");
            
            defTramForm.setListaDepartamentosNotificacion(DefinicionTramitesManager.getInstance().getDepartamentosNotificacionSNE(params));
            
            try {
                String valor = confExpediente.getString(usuario.getOrgCod()+"/MOSTRAR_MARCA_TRAMITE_NOTIFICADO");
                if(valor!=null && !valor.equals("") && valor.equalsIgnoreCase("si")){
                    defTramForm.setMostrarTramiteNotificado(valor);
                }
                m_Log.debug("Propiedad '" + usuario.getOrgCod()+ "/MOSTRAR_MARCA_TRAMITE_NOTIFICADO' de Expediente.properties recuperada. Valor = " + valor);
            } catch (Exception ex){
                m_Log.error("Error al recuperar la propiedad '" + usuario.getOrgCod()+ "/MOSTRAR_MARCA_TRAMITE_NOTIFICADO' de Expediente.properties");
            }
            
            opcion = "enviar";

        } else if(opcion.equals("deCatalogo")) {
        defTramVO	= defTramForm.getDefinicionTramites();
        String enviar =	"si";
        session.setAttribute("enviar",enviar);
        String codMunicipio =	request.getParameter("codMun");
        String txtCodigo = request.getParameter("codProc");
        String codigoTramite = request.getParameter("codTram");
        String numeroTramite = request.getParameter("nCS");
        defTramVO.setCodMunicipio(codMunicipio);
        defTramVO.setTxtCodigo(txtCodigo);
        defTramVO.setCodigoTramite(codigoTramite);
        defTramVO.setNumeroTramite(numeroTramite);
        String noModificar = "si";
        defTramVO.setNoModificar(noModificar);
        String deCatalogo = "si";
        defTramVO.setDeCatalogo(deCatalogo);
        //Listas
        defTramVO.setListaClasifTramite(DefinicionTramitesManager.getInstance().getListaClasifTramites(params));
                defTramVO.setListaCargos(CargosManager.getInstance().getListaUORs(params));
        defTramVO.setListaUnidadesTramitadoras(UORsManager.getInstance().getListaUORs(false,params));
        defTramVO.setListaTramites(DefinicionTramitesManager.getInstance().getListaTramites(defTramVO,params));
        
        defTramVO.setListaCampos(new Vector());
        defTramVO.setListaDocumentos(new Vector());
        defTramVO.setListaConfSW(DefinicionSWTramitacionManager.getInstance().getListaConfSW(
                Integer.parseInt(defTramVO.getCodMunicipio()),
                defTramVO.getTxtCodigo(),Integer.parseInt(defTramVO.getCodigoTramite()),params));
        //  Lista exp rel
        
        defTramVO.setListaExpRel(TramitacionManager.getInstance().getListaProcedimientos(usuario, params));

        Vector listaCodTramites = DefinicionTramitesManager.getInstance().getListaCodTramites(defTramVO,codMunicipio,params);
        String codigoTramiteActual = defTramVO.getCodigoTramite();
        int	i=0;
        if (listaCodTramites.size()>0){
          while	(i<listaCodTramites.size() &&	!listaCodTramites.elementAt(i).equals(codigoTramiteActual))
          i++;
        }
        i=(i==listaCodTramites.size())?0:i+1;
        defTramVO.setTramiteActual(Integer.toString(i));
        defTramVO.setNumeroTramites(Integer.toString(listaCodTramites.size()));
        defTramVO=DefinicionTramitesManager.getInstance().getTramite(defTramVO,codMunicipio,params);
        defTramForm.setDefinicionTramites(defTramVO);
        defTramForm.setListaTiposNotificacionElectronica(NotificacionesUtil.getTiposNotificacion());
        defTramForm.setListaDocsPresentados(DefinicionProcedimientosManager.getInstance().getListaDocumentos(defTramVO, params));
        defTramForm.setListaDepartamentosNotificacion(DefinicionTramitesManager.getInstance().getDepartamentosNotificacionSNE(params));

        /*** SE RECUPERAN LOS PLUGIN DE TRAMITACION EXTERNA ACTIVOS PARA EL TRÁMITE ***/
        TramitacionExternaCargador cargador = TramitacionExternaCargador.getInstance();
        //ArrayList<TramitacionExternaBase>  plugins = cargador.getPluginDisponibles(usuario.getOrgCod(), Integer.parseInt(defTramVO.getCodigoTramite()), defTramVO.getTxtCodigo());
        ArrayList<TramitacionExternaBase>  plugins = cargador.getPluginDisponibles(usuario.getOrgCod());
        m_Log.debug(" ********************* Número de plugins a cargar: " + plugins.size() + " **********************************");
        defTramVO.setPluginPantallasTramitacionExterna(plugins);
        opcion = "enviar";
 
      } else if(opcion.equals("enlaceTramite"))	{
        String[] paramsAUtilizar = params;
        defTramVO	= defTramForm.getDefinicionTramites();
        String codMunicipio =	request.getParameter("codMunicipio");
        String importar	= request.getParameter("importar");
        String strEnlace = request.getParameter("enlace");
        String txtCodigo = request.getParameter("codigoProcedimiento");
        defTramVO.setTxtCodigo(txtCodigo);

        //String codigoTramite = request.getParameter("codigoTramite");
        if (strEnlace == null) strEnlace = "0";
        int	enlace = Integer.parseInt(strEnlace)-1;
        if("si".equals(importar)) {
          DefinicionProcedimientosValueObject defProcVO = new DefinicionProcedimientosValueObject();
          defProcVO.setCodMunicipio("0");
          defProcVO.setCodAplicacion(defTramVO.getCodAplicacion());
          String jndi =	DefinicionProcedimientosManager.getInstance().obtenerJndi(defProcVO,params);
          paramsDiputacion[0]	= params[0]; // "oracle";
          paramsDiputacion[6]	= jndi;
          paramsAUtilizar = paramsDiputacion;
        }

        String codigoTramiteActual = defTramVO.getCodigoTramite();
        Vector listaCodTramites = DefinicionTramitesManager.getInstance().getListaCodTramites(defTramVO,codMunicipio,paramsAUtilizar);

        if ( (enlace>=0) && (enlace<listaCodTramites.size())){
          codigoTramiteActual =	(String)listaCodTramites.elementAt(enlace);
        } else {
          int i=0;
          if (listaCodTramites.size()>0){
            while	(i<listaCodTramites.size() &&	!listaCodTramites.elementAt(i).equals(codigoTramiteActual))
            i++;
          }
          enlace = (i==listaCodTramites.size())?0:i+1;
        }

        defTramVO.setCodigoTramite(codigoTramiteActual);
        defTramVO.setCodigoInternoTramite(codigoTramiteActual);
        //Listas
        defTramVO.setListaTramites(DefinicionTramitesManager.getInstance().getListaTramites(defTramVO,paramsAUtilizar));
        defTramVO.setListaClasifTramite(DefinicionTramitesManager.getInstance().getListaClasifTramites(paramsAUtilizar));
                defTramVO.setListaExpRel(TramitacionManager.getInstance().getListaProcedimientos(usuario, params));
                defTramVO.setListaCargos(CargosManager.getInstance().getListaUORs(params));
        defTramVO.setListaUnidadesTramitadoras(UORsManager.getInstance().getListaUORs(false,paramsAUtilizar));
        defTramVO.setListaEnlaces(DefinicionTramitesManager.getInstance().getListaEnlaces(defTramVO,paramsAUtilizar));

        defTramVO.setListaConfSW(DefinicionSWTramitacionManager.getInstance().getListaConfSW(
        Integer.parseInt(defTramVO.getCodMunicipio()),
            defTramVO.getTxtCodigo(),Integer.parseInt(defTramVO.getCodigoTramite()),params));
       


        defTramVO.setPosicion(posicion);
        defTramVO.setTramiteEliminado("");
        defTramVO.setTramiteActual(Integer.toString(enlace+1));
        defTramVO.setNumeroTramites(Integer.toString(listaCodTramites.size()));
        defTramVO=DefinicionTramitesManager.getInstance().getTramite(defTramVO,codMunicipio,paramsAUtilizar);
        
        defTramForm.setCertificadoOrganismoFirmaNotificacion((defTramVO.getCertificadoOrganismoFirmaNotificacion())?"on":"off");
        defTramForm.setNotificacionElectronicaObligatoria((defTramVO.getNotificacionElectronicaObligatoria())?"on":"off");

        // Se recupera la lista de departamentos válidos para la notificación del SNE
        defTramForm.setListaDepartamentosNotificacion(DefinicionTramitesManager.getInstance().getDepartamentosNotificacionSNE(params));
        

        //Campos de la ventana popUp de condiciones de entrada
        Vector camposCondEntrada = new Vector();
        Vector tramites = defTramVO.getListaTramites();
        for (int i = 0; i < tramites.size(); i++) {
            ElementoListaValueObject elemListVO = (ElementoListaValueObject) tramites.get(i);
            String codTramite = elemListVO.getIdentificador();
            m_Log.debug("TRAMITE " + codTramite + " --> " + elemListVO.getDescripcion());
            DefinicionTramitesValueObject defTramVOAux = new DefinicionTramitesValueObject();
            defTramVOAux.setCodMunicipio(codMunicipio);
            defTramVOAux.setTxtCodigo(defTramVO.getTxtCodigo());
            defTramVOAux.setCodigoTramite(codTramite);
            defTramVOAux = DefinicionTramitesManager.getInstance().getTramite(defTramVOAux, codMunicipio, params);

            try{
                UsuarioManager mgr = UsuarioManager.getInstance();
                UsuarioEscritorioValueObject usuarioEscritorioVO = mgr.buscaUsuario(Integer.parseInt(defTramVO.getCodigoOtroUsuarioFirma()));
                defTramVO.setNombreOtroUsuarioFirma(usuarioEscritorioVO.getLogin());
            }catch(NumberFormatException e)
            {
                m_Log.error("SE HA PRODUCIDO UN EERROR en el parseInt");
                //e.printStackTrace();
                defTramVO.setNombreOtroUsuarioFirma(null);
            }
            Vector listaCampos = defTramVOAux.getListaCampos();
            for (int j = 0; j < listaCampos.size(); j++) {
                DefinicionCampoValueObject dCVO = (DefinicionCampoValueObject) listaCampos.elementAt(j);
                m_Log.debug("CAMPO : " + dCVO.getCodCampo() + " ... " + dCVO.getDescCampo());
                camposCondEntrada.add(listaCampos.elementAt(j));
            }
        }
        
        /********** PLUGIN DE PANTALLAS DE TRAMITACIÓN EXTERNA ******************/
        defTramVO.setPluginPantallasTramitacionExterna(new ArrayList());
        /*** SE RECUPERAN LOS PLUGIN DE TRAMITACION EXTERNA ACTIVOS PARA EL TRÁMITE ***/
        TramitacionExternaCargador cargador = TramitacionExternaCargador.getInstance();
        ArrayList<TramitacionExternaBase>  plugins = cargador.getPluginDisponibles(usuario.getOrgCod());
        m_Log.debug(" ********************* Número de plugins a cargar: " + plugins.size() + " **********************************");        
        defTramVO.setPluginPantallasTramitacionExterna(plugins);
        /************************************************************************/


        defTramVO.setListaCamposTramitesCondEntrada(camposCondEntrada);
        defTramForm.setListaDocsPresentados(DefinicionProcedimientosManager.getInstance().getListaDocumentos(defTramVO, params));
        defTramForm.setDefinicionTramites(defTramVO);
        opcion = "tramite";

      } else if(opcion.equals("pintaTramiteActual")) {
        String[] paramsAUtilizar = params;
        defTramVO	= defTramForm.getDefinicionTramites();
        String codMunicipio =	request.getParameter("codMunicipio");
        String importar	= request.getParameter("importar");
        String txtCodigo = request.getParameter("codigoProcedimiento");
        defTramVO.setTxtCodigo(txtCodigo);

        if("si".equals(importar)) {
          DefinicionProcedimientosValueObject defProcVO = new DefinicionProcedimientosValueObject();
          defProcVO.setCodMunicipio("0");
          defProcVO.setCodAplicacion(defTramVO.getCodAplicacion());
          String jndi =	DefinicionProcedimientosManager.getInstance().obtenerJndi(defProcVO,params);
          paramsDiputacion[0]	= params[0]; // "oracle";
          paramsDiputacion[6]	= jndi;
          paramsAUtilizar = paramsDiputacion;
        }

        String codigoTramiteActual = defTramVO.getCodigoTramite();
        Vector listaCodTramites = DefinicionTramitesManager.getInstance().getListaCodTramites(defTramVO,codMunicipio,paramsAUtilizar);

        int i=0;
        if (listaCodTramites.size()>0){
            while	(i<listaCodTramites.size() &&	!listaCodTramites.elementAt(i).equals(codigoTramiteActual))
                i++;
        }
        int enlace = (i==listaCodTramites.size())?0:i+1;
        //Listas
        defTramVO.setListaTramites(DefinicionTramitesManager.getInstance().getListaTramites(defTramVO,paramsAUtilizar));
        defTramVO.setListaClasifTramite(DefinicionTramitesManager.getInstance().getListaClasifTramites(paramsAUtilizar));
                defTramVO.setListaCargos(CargosManager.getInstance().getListaUORs(params));
        defTramVO.setListaUnidadesTramitadoras(UORsManager.getInstance().getListaUORs(false,paramsAUtilizar));
        defTramVO.setListaEnlaces(DefinicionTramitesManager.getInstance().getListaEnlaces(defTramVO,paramsAUtilizar));

        defTramVO.setPosicion(Integer.toString(enlace));
        defTramVO.setTramiteEliminado("");
        defTramVO.setTramiteActual(Integer.toString(enlace));
        defTramVO.setNumeroTramites(Integer.toString(listaCodTramites.size()));
        defTramVO=DefinicionTramitesManager.getInstance().getTramite(defTramVO,codMunicipio,paramsAUtilizar);
        m_Log.debug("** GENERAR PLAZOS............."+defTramVO.isGenerarPlazos());
        defTramForm.setDefinicionTramites(defTramVO);
        // #274437: se establece la propiedad notificacionElectronicaObligatoria de defTramForm seg?n el valor recuperado de bbdd que est?	
        // en la propiedad notificacionElectronicaObligatoria de defTramVO	
        String notifObligOnOff = defTramVO.getNotificacionElectronicaObligatoria()?"on":"off";	
        defTramForm.setNotificacionElectronicaObligatoria(notifObligOnOff);
         m_Log.debug("** plazo fin............."+defTramVO.getPlazoFin());

        if(defTramVO.getCertificadoOrganismoFirmaNotificacion())
            defTramForm.setCertificadoOrganismoFirmaNotificacion("on");
        else
            defTramForm.setCertificadoOrganismoFirmaNotificacion("off");



        opcion = "tramite";

      }else	if(opcion.equals("actualizar")) {
        m_Log.debug("ACTUALIZAR");
        defTramVO	= defTramForm.getDefinicionTramites();

        m_Log.debug("EXPEDIENTE RELACIONADO ............."+defTramVO.getCodExpRel());
        m_Log.debug("CARGO RELACIONADO ............."+defTramVO.getCodCargo());
        m_Log.debug("GENERAR PLAZOS ............."+defTramVO.isGenerarPlazos());
        // Recuperar listas de la pestaña de condiciones de entrada
        String listaCodTramitesTabla = request.getParameter("listaCodTramitesTabla");
        String listaTramitesTabla =	request.getParameter("listaTramitesTabla");
        String listaEstadosTabla = request.getParameter("listaEstadosTabla");
        String listaTiposTabla = request.getParameter("listaTiposTabla");
        String listaExpresionesTabla = request.getParameter("listaExpresionesTabla");
        String listaCodigosDocTabla = request.getParameter("listaCodigosDocTabla");       
        m_Log.debug("LISTA COD TRAMITES : "+listaCodTramitesTabla);
        m_Log.debug("LISTA TRAMITES     : "+listaTramitesTabla);
        m_Log.debug("LISTA ESTADOS      : "+listaEstadosTabla);
        m_Log.debug("LISTA TIPOS        : "+listaTiposTabla);
        m_Log.debug("LISTA EXPRESIONES        : "+listaExpresionesTabla);
        m_Log.debug("LISTA CODIGOS DOC        : "+listaCodigosDocTabla);
        defTramVO.setListaCodTramitesTabla(listaTemasSeleccionados(listaCodTramitesTabla));
        defTramVO.setListaTramitesTabla(listaTemasSeleccionados(listaTramitesTabla));
        defTramVO.setListaEstadosTabla(listaTemasSeleccionados(listaEstadosTabla));
        defTramVO.setListaTiposTabla(listaTemasSeleccionados(listaTiposTabla));
        defTramVO.setListaExpresionesTabla(listaTemasSeleccionados(listaExpresionesTabla));
        defTramVO.setListaCodigosDocTabla(listaTemasSeleccionados(listaCodigosDocTabla));
         
        // Recuperar valores de unidad de tramite
        m_Log.debug("OPCION UNIDAD TRAMITE="+defTramVO.getCodUnidadTramite());
        m_Log.debug("LISTA UNIDAD TRAMITE="+defTramForm.getTxtUnidadesTramitadoras());
        defTramVO.setUnidadesTramitadoras(getListaUTRSeleccionadas(defTramForm.getTxtUnidadesTramitadoras()));
        // Recuperar listas de la pestaña	de documentos
        String listaCodigosDoc = request.getParameter("listaCodigosDoc");
        String listaNombresDoc = request.getParameter("listaNombresDoc");
        String listaVisibleDoc = request.getParameter("listaVisibleDoc");
        String listaPlantillaDoc = request.getParameter("listaPlantillaDoc");
        String listaCodPlantilla = request.getParameter("listaCodPlantilla");
        String listaDocActivos = request.getParameter("listaDocActivos");
        String listaFirmaPlantilla = request.getParameter("listaFirmaPlantilla");
        Vector vListaFirmaPlantilla = listaFirmaPlantilla(listaFirmaPlantilla);
        if (listaFirmaPlantilla!=null) defTramVO.setListaFirmaDoc(vListaFirmaPlantilla);
        defTramVO.setListaCodigosDoc(listaTemasSeleccionados(listaCodigosDoc));
        defTramVO.setListaNombresDoc(listaTemasSeleccionados(listaNombresDoc));
        defTramVO.setListaVisibleDoc(listaTemasSeleccionados(listaVisibleDoc));
        defTramVO.setListaPlantillaDoc(listaTemasSeleccionados(listaPlantillaDoc));
        defTramVO.setListaCodPlantilla(listaTemasSeleccionados(listaCodPlantilla));
        defTramVO.setListaDocActivos(listaTemasSeleccionados(listaDocActivos));


        // Recuperar listas de la pestaña de enlaces
        String listaCodigoEnlaces = request.getParameter("listaCodigoEnlaces");
        String listaDescripcionEnlaces = request.getParameter("listaDescripcionEnlaces");
        String listaUrlEnlaces = request.getParameter("listaUrlEnlaces");
        String listaEstadoEnlaces = request.getParameter("listaEstadoEnlaces");
        defTramVO.setListaCodigoEnlaces(listaTemasSeleccionados(listaCodigoEnlaces));
        defTramVO.setListaDescripcionEnlaces(listaTemasSeleccionados(listaDescripcionEnlaces));
        defTramVO.setListaUrlEnlaces(listaTemasSeleccionados(listaUrlEnlaces));
        defTramVO.setListaEstadoEnlaces(listaTemasSeleccionados(listaEstadoEnlaces));

        // Recuperar listas de la pestaña de Campos
        String listaCodCampos = request.getParameter("listaCodCampos");
        String listaDescCampos = request.getParameter("listaDescCampos");
        String listaCodPlantill = request.getParameter("listaCodPlantill");
        String listaCodTipoDato = request.getParameter("listaCodTipoDato");
        String listaTamano = request.getParameter("listaTamano");
        String listaMascara = request.getParameter("listaMascara");
        String listaObligatorio = request.getParameter("listaObligatorio");
        String listaOrden = request.getParameter("listaOrden");
        String listaRotulo = request.getParameter("listaRotulo");
        String listaVisible = request.getParameter("listaVisible");
        String listaActivo = request.getParameter("listaActivo");
        String listaOcultos = request.getParameter("listaOcultos");
        String listaBloqueados = request.getParameter("listaBloqueados");
        //,PLAZO_AVISO, PERIODO_PLAZO
        String listaPlazoFecha = request.getParameter("listaPlazoFecha");
        String listaCheckPlazoFecha = request.getParameter("listaCheckPlazoFecha");
        String listaValidacion = request.getParameter("listaValidacion");
        String listaOperacion = request.getParameter("listaOperacion");
        String listaCodAgrupacionCampo = request.getParameter("listaCodAgrupacion");
        String listaPosicionesX = request.getParameter("listaPosicionesX");
        String listaPosicionesY = request.getParameter("listaPosicionesY");

        defTramVO.setListaCodCampos(listaTemasSeleccionados(listaCodCampos));
        defTramVO.setListaDescCampos(listaTemasSeleccionados(listaDescCampos));
        defTramVO.setListaCodPlantill(listaTemasSeleccionados(listaCodPlantill));
        defTramVO.setListaCodTipoDato(listaTemasSeleccionados(listaCodTipoDato));
        defTramVO.setListaTamano(listaTemasSeleccionados(listaTamano));
        defTramVO.setListaMascara(listaTemasSeleccionados(listaMascara));
        defTramVO.setListaObligatorio(listaTemasSeleccionados(listaObligatorio));
        defTramVO.setListaOrden(listaTemasSeleccionados(listaOrden));
        defTramVO.setListaRotulo(listaTemasSeleccionados(listaRotulo));
        defTramVO.setListaVisible(listaTemasSeleccionados(listaVisible));
        defTramVO.setListaActivo(listaTemasSeleccionados(listaActivo));
        defTramVO.setListaOcultos(listaTemasSeleccionados(listaOcultos));
        defTramVO.setListaBloqueados(listaTemasSeleccionados(listaBloqueados));
        //,PLAZO_AVISO, PERIODO_PLAZO
        defTramVO.setListaPlazoFecha(listaTemasSeleccionados(listaPlazoFecha));
        defTramVO.setListaCheckPlazoFecha(listaTemasSeleccionados(listaCheckPlazoFecha));
        defTramVO.setListaValidacion(listaTemasSeleccionados(listaValidacion));
        defTramVO.setListaOperacion(listaTemasSeleccionados(listaOperacion));
        defTramVO.setListaCodAgrupacionCampo(listaTemasSeleccionados(listaCodAgrupacionCampo));
        defTramVO.setListaPosX(listaTemasSeleccionados(listaPosicionesX));
        defTramVO.setListaPosY(listaTemasSeleccionados(listaPosicionesY));
        
        //Recuperamos la lista de agrupaciones de campo
        String listaCodAgrupacion = request.getParameter("listaCodAgrupaciones");
        String listaDescAgrupacion = request.getParameter("listaDescAgrupaciones");
        String listaOrdenAgrupacion = request.getParameter("listaOrdenAgrupaciones");
        String listaAgrupacionesActivas = request.getParameter("listaAgrupacionesActivas");
        defTramVO.setListaCodAgrupacion(listaTemasSeleccionados(listaCodAgrupacion));
        defTramVO.setListaDescAgrupacion(listaTemasSeleccionados(listaDescAgrupacion));
        defTramVO.setListaOrdenAgrupacion(listaTemasSeleccionados(listaOrdenAgrupacion));
        defTramVO.setListaAgrupacionActiva(listaTemasSeleccionados(listaAgrupacionesActivas));

        String tipoCondicion = request.getParameter("tipoCondicion");
        String tipoFavorableSI = request.getParameter("tipoFavorableSI");
        String tipoFavorableNO = request.getParameter("tipoFavorableNO");
        defTramVO.setTipoCondicion(tipoCondicion);
        defTramVO.setTipoFavorableSI(tipoFavorableSI);
        defTramVO.setTipoFavorableNO(tipoFavorableNO);
        String numeroCondicionSalida = request.getParameter("numeroCondicionSalida");
        String numeroCondicionSalidaSiFavorable = request.getParameter("numeroCondicionSalidaSiFavorable");
        String numeroCondicionSalidaNoFavorable	= request.getParameter("numeroCondicionSalidaNoFavorable");
        if (numeroCondicionSalida.equals("") && numeroCondicionSalidaSiFavorable.equals("")
            && numeroCondicionSalidaNoFavorable.equals("")) { //Sin condicion de Salida ó Finalizacion
                numeroCondicionSalida = "3";
        }
        String listaCodTramitesFlujoSalida = "";
        String listaNumerosSecuenciaFlujoSalida	= "";
        String listaCodTramitesFlujoSalidaSiFavorable = "";
        String listaNumerosSecuenciaFlujoSalidaSiFavorable	= "";
        String listaCodTramitesFlujoSalidaNoFavorable = "";
        String listaNumerosSecuenciaFlujoSalidaNoFavorable	= "";
        if(numeroCondicionSalida !=	null && !numeroCondicionSalida.equals("") && !numeroCondicionSalida.equals("3")) {
          listaCodTramitesFlujoSalida = request.getParameter("listaCodTramitesFlujoSalida");
          listaNumerosSecuenciaFlujoSalida = request.getParameter("listaNumerosSecuenciaFlujoSalida");
        }
        if(numeroCondicionSalidaSiFavorable !=	null && !numeroCondicionSalidaSiFavorable.equals("")) {
            listaCodTramitesFlujoSalidaSiFavorable = request.getParameter("listaCodTramitesFlujoSalidaSiFavorable");
            listaNumerosSecuenciaFlujoSalidaSiFavorable = request.getParameter("listaCodTramitesFlujoSalidaSiFavorable");
        }
        if(numeroCondicionSalidaNoFavorable != null && !numeroCondicionSalidaNoFavorable.equals("")) {
          listaCodTramitesFlujoSalidaNoFavorable = request.getParameter("listaCodTramitesFlujoSalidaNoFavorable");
          listaNumerosSecuenciaFlujoSalidaNoFavorable = request.getParameter("listaNumerosSecuenciaFlujoSalidaNoFavorable");
        }


               // NOTIFICACIONES automaticas de tramite
        boolean notificarCercaFinPlazo=defTramForm.getNotificarCercaFinPlazo();
        boolean notificarFueraDePlazo=defTramForm.getNotificarFueraDePlazo();

        String codNotCercaFinPlazo= request.getParameter("codNotCercaFinPlazo");
        String codNotFueraDePlazo= request.getParameter("codNotFueraDePlazo");

        String admiteNotificacionElectronica= request.getParameter("admiteNotificacionElectronica");
        String codAdmiteNotifElect= request.getParameter("codAdmiteNotifElect");
        String tipoUsuarioFirma= request.getParameter("tipoUsuarioFirma");
        String codigoOtroUsuarioFirma= request.getParameter("codigoOtroUsuarioFirma");
        
        m_Log.debug("---notificarCercaFinPlazo: "+notificarCercaFinPlazo);
        m_Log.debug("---codNotCercaFinPlazo: "+codNotCercaFinPlazo);
        m_Log.debug("--notificarFueraDePlazo : "+notificarFueraDePlazo);
        m_Log.debug("---codNotFueraDePlazo: "+codNotFueraDePlazo);

        m_Log.debug("---admiteNotificacionElectronica: "+admiteNotificacionElectronica);
        m_Log.debug("---codAdmiteNotifElect: "+codAdmiteNotifElect);
        m_Log.debug("--tipoUsuarioFirma : "+tipoUsuarioFirma);
        m_Log.debug("---codigoOtroUsuarioFirma: "+codigoOtroUsuarioFirma);

        if(codNotCercaFinPlazo!=null && !"".equals(codNotCercaFinPlazo))

        defTramVO.setNotificarCercaFinPlazo(notificarCercaFinPlazo);
        defTramVO.setNotificarFueraDePlazo(notificarFueraDePlazo);

        if(codNotCercaFinPlazo!=null && !"".equals(codNotCercaFinPlazo)){
             defTramVO.setTipoNotCercaFinPlazo(Integer.parseInt(codNotCercaFinPlazo));
        }
        if(codNotFueraDePlazo!=null && !"".equals(codNotFueraDePlazo)){
             defTramVO.setTipoNotFueraDePlazo(Integer.parseInt(codNotFueraDePlazo));
        }


        // NOTIFICACIONES
        String notUnidadTramitIni= request.getParameter("checkUdadTramitIni");
        if(notUnidadTramitIni	== null) {
            defTramVO.setNotUnidadTramitIni("N");
        } else defTramVO.setNotUnidadTramitIni("S");
            String notUnidadTramitFin= request.getParameter("checkUdadTramitFin");
        if(notUnidadTramitFin	== null) {
            defTramVO.setNotUnidadTramitFin("N");
        } else defTramVO.setNotUnidadTramitFin("S");
            String notUsuUnidadTramitIni= request.getParameter("checkUsuUdadTramitIni");
        if(notUsuUnidadTramitIni == null) {
            defTramVO.setNotUsuUnidadTramitIni("N");
        } else defTramVO.setNotUsuUnidadTramitIni("S");
            String notUsuUnidadTramitFin= request.getParameter("checkUsuUdadTramitFin");
        if(notUsuUnidadTramitFin == null) {
            defTramVO.setNotUsuUnidadTramitFin("N");
        } else defTramVO.setNotUsuUnidadTramitFin("S");
            String notInteresadosIni = request.getParameter("checkInteresadosIni");
        if(notInteresadosIni == null) {
            defTramVO.setNotInteresadosIni("N");
        } else defTramVO.setNotInteresadosIni("S");
            String notInteresadosFin= request.getParameter("checkInteresadosFin");
        if(notInteresadosFin == null) {
            defTramVO.setNotInteresadosFin("N");
        } else defTramVO.setNotInteresadosFin("S");

        String notUsuInicioTramiteIni = request.getParameter("checkUsuarioTramiteIni");
        if(notUsuInicioTramiteIni == null){
            defTramVO.setNotUsuInicioTramiteIni("N");
        } else{
            defTramVO.setNotUsuInicioTramiteIni("S");
        }

        String notUsuInicioTramiteFin = request.getParameter("checkUsuarioTramiteFin");
        if(notUsuInicioTramiteFin == null){
            defTramVO.setNotUsuInicioTramiteFin("N");
        } else {
            defTramVO.setNotUsuInicioTramiteFin("S");
        }

        String notUsuInicioExpedIni = request.getParameter("checkUsuarioExpedIni");
        if(notUsuInicioExpedIni == null){
            defTramVO.setNotUsuInicioExpedIni("N");
        } else {
            defTramVO.setNotUsuInicioExpedIni("S");
        }

        String notUsuInicioExpedFin = request.getParameter("checkUsuarioExpedFin");
        if(notUsuInicioExpedFin == null){
            defTramVO.setNotUsuInicioExpedFin("N");
        } else {
            defTramVO.setNotUsuInicioExpedFin("S");
        }
        
        String notUsuTraFinPlazo = request.getParameter("checkNotUsuTraFinPlazo");	
        if (notUsuTraFinPlazo == null){	
            defTramVO.setNotUsuTraFinPlazo("N");	
        }else{	
            defTramVO.setNotUsuTraFinPlazo("S");	
        }	
        String notUsuExpFinPlazo = request.getParameter("checkNotUsuExpFinPlazo");	
        if(notUsuExpFinPlazo == null){	
            defTramVO.setNotUsuExpFinPlazo("N");	
        }else{	
            defTramVO.setNotUsuExpFinPlazo("S");	
        }	
        String notUORFinPlazo = request.getParameter("checkNotUORFinPlazo");	
        if(notUORFinPlazo == null){	
            defTramVO.setNotUORFinPlazo("N");	
        }else{	
            defTramVO.setNotUORFinPlazo("S");	
        }
        
        if(admiteNotificacionElectronica!=null && !"".equals(admiteNotificacionElectronica))
        {
            defTramVO.setAdmiteNotificacionElectronica("1");
        }else defTramVO.setAdmiteNotificacionElectronica("0");

        if(codAdmiteNotifElect!=null && !"".equals(codAdmiteNotifElect))
        {
            defTramVO.setCodigoTipoNotificacionElectronica(codAdmiteNotifElect);
        }

            /*
        if(tipoUsuarioFirma!=null && !"".equals(tipoUsuarioFirma)&&!"-1".equals(tipoUsuarioFirma))
        {
            defTramVO.setTipoUsuarioFirma(tipoUsuarioFirma);
        }

        if(codigoOtroUsuarioFirma!=null && !"".equals(codigoOtroUsuarioFirma))
        {
            defTramVO.setCodigoOtroUsuarioFirma(codigoOtroUsuarioFirma);
        }*/

        String certificadoOrganismoFirmaNotificacion = request.getParameter("certificadoOrganismoFirmaNotificacion");
        String certificadoOrganismoFirmaNotificacionValue = request.getParameter("certificadoOrganismoFirmaNotificacionValue");
        m_Log.debug("******** certificadoOrganismoFirmaNotificacion: " + certificadoOrganismoFirmaNotificacion);
        m_Log.debug("******** certificadoOrganismoFirmaNotificacionValue: " + certificadoOrganismoFirmaNotificacionValue);

        defTramVO.setCertificadoOrganismoFirmaNotificacion(false);
        if(certificadoOrganismoFirmaNotificacionValue!=null && "on".equalsIgnoreCase(certificadoOrganismoFirmaNotificacionValue)){
            defTramVO.setCertificadoOrganismoFirmaNotificacion(true);
        }

        // SE COMPRUEBA SI HA SIDO SELECCIONADO ALGÚN PLUGIN DE PANTALLAS DE TRAMITACIÓN EXTERNA Y ALGUNA URL PARA EL MISMO
        String codPluginPantallaTramitacionExterna  = request.getParameter("codPluginPantallaTramitacionExterna");        
        String urlPantallaTramitacionExterna        = request.getParameter("descUrlPluginPantallaTramitacionExterna");
        String codUrlPluginPantallaTramitacionExterna  = request.getParameter("codUrlPluginPantallaTramitacionExterna");
        String implClassPluginPantallaTramExterna   = request.getParameter("implClassPluginPantallaTramExterna");
        String codDepartamentoNotificacion          = request.getParameter("codDepartamentoNotificacion");
        m_Log.debug(" ************ codPluginPantallaTramitacionExterna: " + codPluginPantallaTramitacionExterna);
        m_Log.debug(" ************ codUrlPluginPantallaTramitacionExterna: " + codUrlPluginPantallaTramitacionExterna);
        m_Log.debug(" ************ urlPantallaTramitacionExterna: " + urlPantallaTramitacionExterna);
        m_Log.debug(" ************ implClassPluginPantallaTramExterna: " + implClassPluginPantallaTramExterna);
        m_Log.debug(" ************ codDepartamentoNotificacion: " + codDepartamentoNotificacion);
        
        defTramVO.setCodPluginPantallaTramitacionExterna(codPluginPantallaTramitacionExterna);
        defTramVO.setImplClassPluginPantallaTramitacionExterna(implClassPluginPantallaTramExterna);
        defTramVO.setCodUrlPluginPantallaTramitacionExterna(codUrlPluginPantallaTramitacionExterna);
        defTramVO.setUrlPluginPantallaTramitacionExterna(urlPantallaTramitacionExterna);
        defTramVO.setCodDepartamentoNotificacion(codDepartamentoNotificacion);
        //NOTIFICACIONES

            //DATOS INICIALES
            // Capturamos el dato del fin de plazo.
            String plaz = request.getParameter("plazoFin");
            m_Log.debug("______________________________________________________________ plazo: " + plaz);
            int plazo = 0;
            if (plaz != null && !"".equals(plaz)) {
                plazo = Integer.parseInt(plaz);
            }
            defTramVO.setPlazoFin(plazo);                               
        
        String disponible =	request.getParameter("disponible");
        if(disponible	== null) {
            defTramVO.setDisponible("0");
        } else defTramVO.setDisponible("1");
        String tramiteInicio = request.getParameter("tramiteInicio");
        if(tramiteInicio ==	null)	{
            defTramVO.setTramiteInicio("0");
        } else defTramVO.setTramiteInicio("1");
        // APROVECHAMOS LO DE TRAMITE PREGUNTA QUE NO SE UTILIZA PARA ACTUALIZAR EL
        // CHECK BOX DE SOLO ESTA
        String soloEsta = request.getParameter("soloEsta");
        if(soloEsta == null) {
            defTramVO.setTramitePregunta("0");
        } else defTramVO.setTramitePregunta("1");

            // Gestion de los datos de los servicios web.
            String strCodWSAvanzar = request.getParameter("codOpSWAvz");
            if ("".equals(strCodWSAvanzar) || strCodWSAvanzar == null) defTramVO.setInfoSWAvanzar(null);
            String strCodWSRetroceder = request.getParameter("codOpSWRtr");
            if ("".equals(strCodWSRetroceder) || strCodWSRetroceder == null) defTramVO.setInfoSWRetroceder(null);

         defTramVO.setNotificacionElectronicaObligatoria(defTramForm.getNotificacionElectronicaObligatoriaValue().toUpperCase().equals("ON"));
         defTramVO.setCertificadoOrganismoFirmaNotificacion(defTramForm.getCertificadoOrganismoFirmaNotificacionValue().toUpperCase().equals("ON"));
         defTramForm.setCertificadoOrganismoFirmaNotificacion(defTramForm.getCertificadoOrganismoFirmaNotificacionValue());
         defTramForm.setNotificacionElectronicaObligatoria(defTramForm.getNotificacionElectronicaObligatoriaValue());
            
        int i =	DefinicionTramitesManager.getInstance().modify(defTramVO,params);
        if(i>0 && !numeroCondicionSalida.equals("")) {
            TablasIntercambiadorasValueObject tabInterVO = new TablasIntercambiadorasValueObject();
            if ((listaCodTramitesFlujoSalida !=	null) && !("".equals(listaCodTramitesFlujoSalida))) {
                tabInterVO.setListaCodTramitesFlujoSalida(listaTemasSeleccionados(listaCodTramitesFlujoSalida));
                tabInterVO.setListaNumerosSecuenciaFlujoSalida(listaTemasSeleccionados(listaNumerosSecuenciaFlujoSalida));
            } else {
                tabInterVO.setListaCodTramitesFlujoSalida(new Vector());
                tabInterVO.setListaNumerosSecuenciaFlujoSalida(new Vector());
            }
            String obligatorio = request.getParameter("oblig");
            tabInterVO.setObligatorio(obligatorio);
            tabInterVO.setNumeroCondicionSalida(numeroCondicionSalida);
            tabInterVO.setCodMunicipio(defTramVO.getCodMunicipio());
            tabInterVO.setCodProcedimiento(defTramVO.getTxtCodigo());
            tabInterVO.setCodTramite(defTramVO.getCodigoTramite());
            int j	= DefinicionTramitesManager.getInstance().grabarFlujoSalida(tabInterVO,params);
        }
        if(i>0 && !numeroCondicionSalidaSiFavorable.equals(""))	{
            TablasIntercambiadorasValueObject tabInterVO = new TablasIntercambiadorasValueObject();
            tabInterVO.setListaCodTramitesFlujoSalida(listaTemasSeleccionados(listaCodTramitesFlujoSalidaSiFavorable));
            tabInterVO.setListaNumerosSecuenciaFlujoSalida(listaTemasSeleccionados(listaNumerosSecuenciaFlujoSalidaSiFavorable));
            String obligatorio = request.getParameter("obligSiFavorable");
            tabInterVO.setObligatorio(obligatorio);
            tabInterVO.setNumeroCondicionSalida(numeroCondicionSalidaSiFavorable);
            tabInterVO.setCodMunicipio(defTramVO.getCodMunicipio());
            tabInterVO.setCodProcedimiento(defTramVO.getTxtCodigo());
            tabInterVO.setCodTramite(defTramVO.getCodigoTramite());
            int j	= DefinicionTramitesManager.getInstance().grabarFlujoSalida(tabInterVO,params);
        }
        if(i>0 && !numeroCondicionSalidaNoFavorable.equals(""))	{
             TablasIntercambiadorasValueObject tabInterVO = new TablasIntercambiadorasValueObject();
             tabInterVO.setListaCodTramitesFlujoSalida(listaTemasSeleccionados(listaCodTramitesFlujoSalidaNoFavorable));
             tabInterVO.setListaNumerosSecuenciaFlujoSalida(listaTemasSeleccionados(listaNumerosSecuenciaFlujoSalidaNoFavorable));
             String obligatorio = request.getParameter("obligNoFavorable");
             tabInterVO.setObligatorio(obligatorio);
             tabInterVO.setNumeroCondicionSalida(numeroCondicionSalidaNoFavorable);
             tabInterVO.setCodMunicipio(defTramVO.getCodMunicipio());
             tabInterVO.setCodProcedimiento(defTramVO.getTxtCodigo());
             tabInterVO.setCodTramite(defTramVO.getCodigoTramite());
             int j	= DefinicionTramitesManager.getInstance().grabarFlujoSalida(tabInterVO,params);
        }
       
        ComprobacionEstructuraVO estructuraCorrecta = esEstructuraCorrecta(defTramVO.getListaConfSW(),params);
        m_Log.debug("La estructura es correcta? : " + estructuraCorrecta);

                	
        
        
        if(i>0&&estructuraCorrecta.isCorrecta())
          opcion = "actualizacionRealizada";
        else {
          opcion = "actualizacionNoRealizada";
          request.setAttribute("estructuraCorrecta", estructuraCorrecta);
        }
      } else if(opcion.equals("insertar")) {
        defTramVO	= defTramForm.getDefinicionTramites();

        // Recuperar listas de la pestaña de condiciones de entrada
        String listaCodTramitesTabla = request.getParameter("listaCodTramitesTabla");
        String listaTramitesTabla =	request.getParameter("listaTramitesTabla");
        String listaEstadosTabla = request.getParameter("listaEstadosTabla");
          String listaTiposTabla = request.getParameter("listaTiposTabla");
          String listaExpresionesTabla = request.getParameter("listaExpresionesTabla");
          m_Log.debug("LISTA COD TRAMITES : "+listaCodTramitesTabla);
          m_Log.debug("LISTA TRAMITES     : "+listaTramitesTabla);
          m_Log.debug("LISTA ESTADOS      : "+listaEstadosTabla);
          m_Log.debug("LISTA TIPOS        : "+listaTiposTabla);
          m_Log.debug("LISTA EXPRESIONES        : "+listaExpresionesTabla);
        defTramVO.setListaCodTramitesTabla(listaTemasSeleccionados(listaCodTramitesTabla));
        defTramVO.setListaTramitesTabla(listaTemasSeleccionados(listaTramitesTabla));
        defTramVO.setListaEstadosTabla(listaTemasSeleccionados(listaEstadosTabla));
          defTramVO.setListaTiposTabla(listaTemasSeleccionados(listaTiposTabla));
          defTramVO.setListaExpresionesTabla(listaTemasSeleccionados(listaExpresionesTabla));


        // Recuperar listas de la pestaña de documentos
        String listaCodigosDoc = request.getParameter("listaCodigosDoc");
        String listaNombresDoc = request.getParameter("listaNombresDoc");
        String listaVisibleDoc = request.getParameter("listaVisibleDoc");
        String listaPlantillaDoc = request.getParameter("listaPlantillaDoc");
        String listaCodPlantilla = request.getParameter("listaCodPlantilla");
        String listaDocActivos = request.getParameter("listaDocActivos");
        String listaFirmaPlantilla = request.getParameter("listaFirmaPlantilla");
        if (listaFirmaPlantilla!=null) defTramVO.setListaFirmaDoc(listaTemasSeleccionados(listaFirmaPlantilla));
        defTramVO.setListaCodigosDoc(listaTemasSeleccionados(listaCodigosDoc));
        defTramVO.setListaNombresDoc(listaTemasSeleccionados(listaNombresDoc));
        defTramVO.setListaVisibleDoc(listaTemasSeleccionados(listaVisibleDoc));
        defTramVO.setListaPlantillaDoc(listaTemasSeleccionados(listaPlantillaDoc));
        defTramVO.setListaCodPlantilla(listaTemasSeleccionados(listaCodPlantilla));
        defTramVO.setListaDocActivos(listaTemasSeleccionados(listaDocActivos));

        // Recuperar listas de la pestaña de enlaces
        String listaCodigoEnlaces = request.getParameter("listaCodigoEnlaces");
        String listaDescripcionEnlaces = request.getParameter("listaDescripcionEnlaces");
        String listaUrlEnlaces = request.getParameter("listaUrlEnlaces");
        String listaEstadoEnlaces = request.getParameter("listaEstadoEnlaces");
        defTramVO.setListaCodigoEnlaces(listaTemasSeleccionados(listaCodigoEnlaces));
        defTramVO.setListaDescripcionEnlaces(listaTemasSeleccionados(listaDescripcionEnlaces));
        defTramVO.setListaUrlEnlaces(listaTemasSeleccionados(listaUrlEnlaces));
        defTramVO.setListaEstadoEnlaces(listaTemasSeleccionados(listaEstadoEnlaces));

        // Recuperar listas de la pestaña de Campos
        String listaCodCampos = request.getParameter("listaCodCampos");
        String listaDescCampos = request.getParameter("listaDescCampos");
        String listaCodPlantill = request.getParameter("listaCodPlantill");
        String listaCodTipoDato = request.getParameter("listaCodTipoDato");
        String listaTamano = request.getParameter("listaTamano");
        String listaMascara = request.getParameter("listaMascara");
        String listaObligatorio = request.getParameter("listaObligatorio");
        String listaOrden = request.getParameter("listaOrden");
        String listaRotulo = request.getParameter("listaRotulo");
        String listaVisible = request.getParameter("listaVisible");
        String listaActivo = request.getParameter("listaActivo");
        String listaOcultos = request.getParameter("listaOcultos");
        String listaBloqueados = request.getParameter("listaBloqueados");
        String listaPlazoFecha = request.getParameter("listaPlazoFecha");
        String listaCheckPlazoFecha = request.getParameter("listaCheckPlazoFecha");                
        String listaValidacion = request.getParameter("listaValidacion");
        String listaOperacion = request.getParameter("listaOperacion");
        String listaCodAgrupacionCampo = request.getParameter("listaCodAgrupacion");
        String listaPosicionesX = request.getParameter("listaPosicionesX");
        String listaPosicionesY = request.getParameter("listaPosicionesY");
        
        defTramVO.setListaCodCampos(listaTemasSeleccionados(listaCodCampos));
        defTramVO.setListaDescCampos(listaTemasSeleccionados(listaDescCampos));
        defTramVO.setListaCodPlantill(listaTemasSeleccionados(listaCodPlantill));
        defTramVO.setListaCodTipoDato(listaTemasSeleccionados(listaCodTipoDato));
        defTramVO.setListaTamano(listaTemasSeleccionados(listaTamano));
        defTramVO.setListaMascara(listaTemasSeleccionados(listaMascara));
        defTramVO.setListaObligatorio(listaTemasSeleccionados(listaObligatorio));
        defTramVO.setListaOrden(listaTemasSeleccionados(listaOrden));
        defTramVO.setListaRotulo(listaTemasSeleccionados(listaRotulo));
        defTramVO.setListaVisible(listaTemasSeleccionados(listaVisible));
        defTramVO.setListaActivo(listaTemasSeleccionados(listaActivo));
        defTramVO.setListaOcultos(listaTemasSeleccionados(listaOcultos));
        defTramVO.setListaBloqueados(listaTemasSeleccionados(listaBloqueados));
        //,PLAZO_AVISO, PERIODO_PLAZO
        defTramVO.setListaPlazoFecha(listaTemasSeleccionados(listaPlazoFecha));
        defTramVO.setListaCheckPlazoFecha(listaTemasSeleccionados(listaCheckPlazoFecha));
        defTramVO.setListaValidacion(listaTemasSeleccionados(listaValidacion));
        defTramVO.setListaOperacion(listaTemasSeleccionados(listaOperacion));
        defTramVO.setListaCodAgrupacionCampo(listaTemasSeleccionados(listaCodAgrupacionCampo));
        defTramVO.setListaPosX(listaTemasSeleccionados(listaPosicionesX));
        defTramVO.setListaPosY(listaTemasSeleccionados(listaPosicionesY));
        
        //Recuperamos la lista de agrupaciones de campo
        String listaCodAgrupacion = request.getParameter("listaCodAgrupaciones");
        String listaDescAgrupacion = request.getParameter("listaDescAgrupaciones");
        String listaOrdenAgrupacion = request.getParameter("listaOrdenAgrupaciones");
        String listaAgrupacionesActivas = request.getParameter("listaAgrupacionesActivas");
        defTramVO.setListaCodAgrupacion(listaTemasSeleccionados(listaCodAgrupacion));
        defTramVO.setListaDescAgrupacion(listaTemasSeleccionados(listaDescAgrupacion));
        defTramVO.setListaOrdenAgrupacion(listaTemasSeleccionados(listaOrdenAgrupacion));
        defTramVO.setListaAgrupacionActiva(listaTemasSeleccionados(listaAgrupacionesActivas));

        String tipoCondicion = request.getParameter("tipoCondicion");
        String tipoFavorableSI = request.getParameter("tipoFavorableSI");
        String tipoFavorableNO = request.getParameter("tipoFavorableNO");
        defTramVO.setTipoCondicion(tipoCondicion);
        defTramVO.setTipoFavorableSI(tipoFavorableSI);
        defTramVO.setTipoFavorableNO(tipoFavorableNO);

        // Recuperar valores de unidad de tramite
        m_Log.debug("OPCION UNIDAD TRAMITE="+defTramVO.getCodUnidadTramite());
        m_Log.debug("LISTA UNIDAD TRAMITE="+defTramForm.getTxtUnidadesTramitadoras());
        defTramVO.setUnidadesTramitadoras(getListaUTRSeleccionadas(defTramForm.getTxtUnidadesTramitadoras()));

        // NOTIFICACIONES automaticas de tramite
        boolean notificarCercaFinPlazo=defTramForm.getNotificarCercaFinPlazo();
        boolean notificarFueraDePlazo=defTramForm.getNotificarFueraDePlazo();

        String codNotCercaFinPlazo= request.getParameter("codNotCercaFinPlazo");
        String codNotFueraDePlazo= request.getParameter("codNotFueraDePlazo");

        String admiteNotificacionElectronica= request.getParameter("admiteNotificacionElectronica");
        String codAdmiteNotifElect= request.getParameter("codAdmiteNotifElect");
        String tipoUsuarioFirma= request.getParameter("tipoUsuarioFirma");
        String codigoOtroUsuarioFirma= request.getParameter("codigoOtroUsuarioFirma");
        
        m_Log.debug("---notificarCercaFinPlazo: "+notificarCercaFinPlazo);
        m_Log.debug("---codNotCercaFinPlazo: "+codNotCercaFinPlazo);
        m_Log.debug("--notificarFueraDePlazo : "+notificarFueraDePlazo);
        m_Log.debug("---codNotFueraDePlazo: "+codNotFueraDePlazo);

        m_Log.debug("---admiteNotificacionElectronica: "+admiteNotificacionElectronica);
        m_Log.debug("---codAdmiteNotifElect: "+codAdmiteNotifElect);
        m_Log.debug("--tipoUsuarioFirma : "+tipoUsuarioFirma);
        m_Log.debug("---codigoOtroUsuarioFirma: "+codigoOtroUsuarioFirma);


        defTramVO.setNotificarCercaFinPlazo(notificarCercaFinPlazo);
        defTramVO.setNotificarFueraDePlazo(notificarFueraDePlazo);
        
        if(codNotCercaFinPlazo!=null){
             defTramVO.setTipoNotCercaFinPlazo(Integer.parseInt(codNotCercaFinPlazo));
        }
        if(codNotFueraDePlazo!=null){
             defTramVO.setTipoNotFueraDePlazo(Integer.parseInt(codNotFueraDePlazo));
        }

        // NOTIFICACIONES
        String notUnidadTramitIni= request.getParameter("checkUdadTramitIni");
        if(notUnidadTramitIni	== null) {
            defTramVO.setNotUnidadTramitIni("N");
        } else defTramVO.setNotUnidadTramitIni("S");
            String notUnidadTramitFin= request.getParameter("checkUdadTramitFin");
        if(notUnidadTramitFin	== null) {
            defTramVO.setNotUnidadTramitFin("N");
        } else defTramVO.setNotUnidadTramitFin("S");
            String notUsuUnidadTramitIni= request.getParameter("checkUsuUdadTramitIni");
        if(notUsuUnidadTramitIni == null) {
            defTramVO.setNotUsuUnidadTramitIni("N");
        } else defTramVO.setNotUsuUnidadTramitIni("S");
            String notUsuUnidadTramitFin= request.getParameter("checkUsuUdadTramitFin");
        if(notUsuUnidadTramitFin == null) {
            defTramVO.setNotUsuUnidadTramitFin("N");
        } else defTramVO.setNotUsuUnidadTramitFin("S");
            String notInteresadosIni = request.getParameter("checkInteresadosIni");
        if(notInteresadosIni == null) {
            defTramVO.setNotInteresadosIni("N");
        } else defTramVO.setNotInteresadosIni("S");
            String notInteresadosFin= request.getParameter("checkInteresadosFin");
        if(notInteresadosFin == null) {
            defTramVO.setNotInteresadosFin("N");
        } else defTramVO.setNotInteresadosFin("S");

        String notUsuInicioTramiteIni = request.getParameter("checkUsuarioTramiteIni");
        if(notUsuInicioTramiteIni == null){
            defTramVO.setNotUsuInicioTramiteIni("N");
        } else{
            defTramVO.setNotUsuInicioTramiteIni("S");
        }

        String notUsuInicioTramiteFin = request.getParameter("checkUsuarioTramiteFin");
        if(notUsuInicioTramiteFin == null){
            defTramVO.setNotUsuInicioTramiteFin("N");
        } else {
            defTramVO.setNotUsuInicioTramiteFin("S");
        }

        String notUsuInicioExpedIni = request.getParameter("checkUsuarioExpedIni");
        if(notUsuInicioExpedIni == null){
            defTramVO.setNotUsuInicioExpedIni("N");
        } else {
            defTramVO.setNotUsuInicioExpedIni("S");
        }

        String notUsuInicioExpedFin = request.getParameter("checkUsuarioExpedFin");
        if(notUsuInicioExpedFin == null){
            defTramVO.setNotUsuInicioExpedFin("N");
        } else {
            defTramVO.setNotUsuInicioExpedFin("S");
        }
         String notUsuTraFinPlazo = request.getParameter("checkNotUsuTraFinPlazo");	
        if (notUsuTraFinPlazo == null){	
            defTramVO.setNotUsuTraFinPlazo("N");	
        }else{	
            defTramVO.setNotUsuTraFinPlazo("S");	
        }	
        String notUsuExpFinPlazo = request.getParameter("checkNotUsuExpFinPlazo");	
        if(notUsuExpFinPlazo == null){	
            defTramVO.setNotUsuExpFinPlazo("N");	
        }else{	
            defTramVO.setNotUsuExpFinPlazo("S");	
        }	
        String notUORFinPlazo = request.getParameter("checkNotUORFinPlazo");	
        if(notUORFinPlazo == null){	
            defTramVO.setNotUORFinPlazo("N");	
        }else{	
            defTramVO.setNotUORFinPlazo("S");	
        }
        
        if(admiteNotificacionElectronica!=null && !"".equals(admiteNotificacionElectronica))
        {
            defTramVO.setAdmiteNotificacionElectronica("1");
        }else defTramVO.setAdmiteNotificacionElectronica("0");

        if(codAdmiteNotifElect!=null && !"".equals(codAdmiteNotifElect))
        {
            defTramVO.setCodigoTipoNotificacionElectronica(codAdmiteNotifElect);
        }

        if(tipoUsuarioFirma!=null && !"".equals(tipoUsuarioFirma)&&!"-1".equals(tipoUsuarioFirma))
        {
            defTramVO.setTipoUsuarioFirma(tipoUsuarioFirma);
        }

        if(codigoOtroUsuarioFirma!=null && !"".equals(codigoOtroUsuarioFirma))
        {
            defTramVO.setCodigoOtroUsuarioFirma(codigoOtroUsuarioFirma);
        }

        //NOTIFICACIONES
            
            //DATOS INICIALES
            // Capturamos el dato del fin de plazo.
            String plaz = request.getParameter("plazoFin");
            m_Log.debug("______________________________________________________________ plazo: " + plaz);
            int plazo = 0;
            if (plaz != null && !"".equals(plaz)) {
                plazo = Integer.parseInt(plaz);
            }
            defTramVO.setPlazoFin(plazo);        
                                        
        String unidadesPlazo = request.getParameter("unidadesPlazo");
        String codMunicipio =	request.getParameter("codMunicipio");
        String disponible = request.getParameter("disponible");
        if(disponible == null) {
          defTramVO.setDisponible("0");
        } else defTramVO.setDisponible("1");
        String tramiteInicio = request.getParameter("tramiteInicio");
        if(tramiteInicio == null) {
          defTramVO.setTramiteInicio("0");
        } else defTramVO.setTramiteInicio("1");
        String soloEsta = request.getParameter("soloEsta");
        if(soloEsta ==	null)	{
          defTramVO.setTramitePregunta("0");
        } else defTramVO.setTramitePregunta("1");

            // Gestion de los datos de los servicios web.
            String strCodWSAvanzar = request.getParameter("codOpSWAvz");
            if ("".equals(strCodWSAvanzar) || strCodWSAvanzar == null) defTramVO.setInfoSWAvanzar(null);
            String strCodWSRetroceder = request.getParameter("codOpSWRtr");
            if ("".equals(strCodWSRetroceder) || strCodWSRetroceder == null) defTramVO.setInfoSWRetroceder(null);

        /************** PARÁMETROS NECESARIOS PARA SABER SI SE HA SELECCIONADO UN PLUGIN DE PANTALLA DE TRAMITACIÓN EXTERNA PARA EL TRÁMITE ********/
        String codPluginPantallaTramitacionExterna  = request.getParameter("codPluginPantallaTramitacionExterna");
        String codUrlPantallaTramitacionExterna     = request.getParameter("codUrlPluginPantallaTramitacionExterna");
        String urlPantallaTramitacionExterna        = request.getParameter("descUrlPluginPantallaTramitacionExterna");
        String implClassPluginPantallaTramExterna   = request.getParameter("implClassPluginPantallaTramExterna");
        m_Log.debug(" ************ codPluginPantallaTramitacionExterna: " + codPluginPantallaTramitacionExterna);
        m_Log.debug(" ************ codUrlPantallaTramitacionExterna: " + codUrlPantallaTramitacionExterna);
        m_Log.debug(" ************ urlPantallaTramitacionExterna: " + urlPantallaTramitacionExterna);
        m_Log.debug(" ************ implClassPluginPantallaTramExterna: " + implClassPluginPantallaTramExterna);
        
        defTramVO.setCodPluginPantallaTramitacionExterna(codPluginPantallaTramitacionExterna);
        defTramVO.setImplClassPluginPantallaTramitacionExterna(implClassPluginPantallaTramExterna);
        defTramVO.setUrlPluginPantallaTramitacionExterna(urlPantallaTramitacionExterna);
        defTramVO.setCodUrlPluginPantallaTramitacionExterna(codUrlPantallaTramitacionExterna);
        /**********************************************************************************/
        
        defTramVO.setNotificacionElectronicaObligatoria(defTramForm.getNotificacionElectronicaObligatoriaValue().toUpperCase().equals("ON"));
        defTramVO.setCertificadoOrganismoFirmaNotificacion(defTramForm.getCertificadoOrganismoFirmaNotificacionValue().toUpperCase().equals("ON"));
        defTramForm.setCertificadoOrganismoFirmaNotificacion(defTramForm.getCertificadoOrganismoFirmaNotificacionValue());
        defTramForm.setNotificacionElectronicaObligatoria(defTramForm.getNotificacionElectronicaObligatoriaValue());
            

        int	i = DefinicionTramitesManager.getInstance().insert(defTramVO,params);
        String numeroCondicionSalida = request.getParameter("numeroCondicionSalida");
        String numeroCondicionSalidaSiFavorable	= request.getParameter("numeroCondicionSalidaSiFavorable");
        String numeroCondicionSalidaNoFavorable	= request.getParameter("numeroCondicionSalidaNoFavorable");
        if(i>0 &&	!numeroCondicionSalida.equals("")) {
          TablasIntercambiadorasValueObject tabInterVO = new TablasIntercambiadorasValueObject();
          String listaCodTramitesFlujoSalida = request.getParameter("listaCodTramitesFlujoSalida");
          String listaNumerosSecuenciaFlujoSalida = request.getParameter("listaNumerosSecuenciaFlujoSalida");
          tabInterVO.setListaCodTramitesFlujoSalida(listaTemasSeleccionados(listaCodTramitesFlujoSalida));
          tabInterVO.setListaNumerosSecuenciaFlujoSalida(listaTemasSeleccionados(listaNumerosSecuenciaFlujoSalida));
          String obligatorio = request.getParameter("oblig");
          tabInterVO.setObligatorio(obligatorio);
          tabInterVO.setNumeroCondicionSalida(numeroCondicionSalida);
          tabInterVO.setCodMunicipio(defTramVO.getCodMunicipio());
          tabInterVO.setCodProcedimiento(defTramVO.getTxtCodigo());
          tabInterVO.setCodTramite(defTramVO.getCodigoTramite());
          int j =	DefinicionTramitesManager.getInstance().grabarFlujoSalida(tabInterVO,params);
        }
        if(i>0 &&	!numeroCondicionSalidaSiFavorable.equals("")) {
          TablasIntercambiadorasValueObject tabInterVO = new TablasIntercambiadorasValueObject();
          String listaCodTramitesFlujoSalida = request.getParameter("listaCodTramitesFlujoSalidaSiFavorable");
          String listaNumerosSecuenciaFlujoSalida = request.getParameter("listaNumerosSecuenciaFlujoSalidaSiFavorable");
          tabInterVO.setListaCodTramitesFlujoSalida(listaTemasSeleccionados(listaCodTramitesFlujoSalida));
          tabInterVO.setListaNumerosSecuenciaFlujoSalida(listaTemasSeleccionados(listaNumerosSecuenciaFlujoSalida));
          String obligatorio = request.getParameter("obligSiFavorable");
          tabInterVO.setObligatorio(obligatorio);
          tabInterVO.setNumeroCondicionSalida(numeroCondicionSalidaSiFavorable);
          tabInterVO.setCodMunicipio(defTramVO.getCodMunicipio());
          tabInterVO.setCodProcedimiento(defTramVO.getTxtCodigo());
          tabInterVO.setCodTramite(defTramVO.getCodigoTramite());
          int j = DefinicionTramitesManager.getInstance().grabarFlujoSalida(tabInterVO,params);
        }
        if(i>0 &&	!numeroCondicionSalidaNoFavorable.equals("")) {
          TablasIntercambiadorasValueObject tabInterVO = new TablasIntercambiadorasValueObject();
          String listaCodTramitesFlujoSalida = request.getParameter("listaCodTramitesFlujoSalidaNoFavorable");
          String listaNumerosSecuenciaFlujoSalida = request.getParameter("listaNumerosSecuenciaFlujoSalidaNoFavorable");
          tabInterVO.setListaCodTramitesFlujoSalida(listaTemasSeleccionados(listaCodTramitesFlujoSalida));
          tabInterVO.setListaNumerosSecuenciaFlujoSalida(listaTemasSeleccionados(listaNumerosSecuenciaFlujoSalida));
          String obligatorio = request.getParameter("obligNoFavorable");
          tabInterVO.setObligatorio(obligatorio);
          tabInterVO.setNumeroCondicionSalida(numeroCondicionSalidaNoFavorable);
          tabInterVO.setCodMunicipio(defTramVO.getCodMunicipio());
          tabInterVO.setCodProcedimiento(defTramVO.getTxtCodigo());
          tabInterVO.setCodTramite(defTramVO.getCodigoTramite());
          int j = DefinicionTramitesManager.getInstance().grabarFlujoSalida(tabInterVO,params);
        }
        Vector listaCodTramites = DefinicionTramitesManager.getInstance().getListaCodTramites(defTramVO,codMunicipio,params);
                
                /******Si se ordenan los tramites por tra_cod lo de abajo vale si no no
        String codigoTramiteIntroducido =	(String)listaCodTramites.lastElement();
        defTramVO.setCodigoTramite(codigoTramiteIntroducido);
                 ******************/


        String codigoTramiteActual = defTramVO.getCodigoTramite();
        int	p=0;
        if (listaCodTramites.size()>0){
          while	(p<listaCodTramites.size() &&	!listaCodTramites.elementAt(p).equals(codigoTramiteActual))
          p++;
        }
        p=(p==listaCodTramites.size())?0:p+1;
        defTramVO.setTramiteActual(Integer.toString(p));
        defTramVO.setNumeroTramites(Integer.toString(listaCodTramites.size()));

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("El codigo del tramite introducido es: " + defTramVO.getCodigoTramite());
                    m_Log.debug("El tramite actual es: " + defTramVO.getTramiteActual());
                }
        defTramForm.setDefinicionTramites(defTramVO);
        if(i>0) {
          opcion = "insercionRealizada";
        } else if(i<0) {
          opcion = "yaExiste";
        } else
        opcion = "insercionNoRealizada";

      } else if(opcion.equals("eliminar")) {
        defTramVO	= defTramForm.getDefinicionTramites();
        String codMunicipio =	request.getParameter("codMunicipio");
        int	j =0;
        Vector listaCodTramites = DefinicionTramitesManager.getInstance().getListaCodTramites(defTramVO,codMunicipio,params);
        String codigoTramiteActual = defTramVO.getCodigoTramite();
        for(int l=0; l < listaCodTramites.size(); l++){
          if (codigoTramiteActual.equals(listaCodTramites.elementAt(l))){
            j = l;
          }
        }
        int	i = DefinicionTramitesManager.getInstance().eliminar(defTramVO,params);

        int	posTramiteActual=j+1;
        int	numTramites=listaCodTramites.size();

        if(i != 0) {
          defTramVO.setTramiteEliminado("eliminado");
        } else {
          defTramVO.setTramiteEliminado("noEliminado");
        }
        if(listaCodTramites.size() ==1) {
          //Listas
          defTramVO.setListaTramites(DefinicionTramitesManager.getInstance().getListaTramites(defTramVO,params));
          defTramVO.setListaClasifTramite(DefinicionTramitesManager.getInstance().getListaClasifTramites(params));
                    defTramVO.setListaCargos(CargosManager.getInstance().getListaUORs(params));
          defTramVO.setListaUnidadesTramitadoras(UORsManager.getInstance().getListaUORs(false,params));
          defTramVO.setListaEnlaces(DefinicionTramitesManager.getInstance().getListaEnlaces(defTramVO,params));
          session.setAttribute("enviar","no");
          if ("eliminado".equals(defTramVO.getTramiteEliminado())){
            posTramiteActual = 0;
            numTramites	= 0;
          }
          defTramVO.setTramiteActual(Integer.toString(posTramiteActual));
          defTramVO.setNumeroTramites(Integer.toString(numTramites));

          defTramForm.setDefinicionTramites(defTramVO);
          opcion = "eliminado";

        } else {
          // j posicion	tramite eliminado
          if ("eliminado".equals(defTramVO.getTramiteEliminado())){
            posTramiteActual=j;
            numTramites	= listaCodTramites.size()-1;
            if (j==(listaCodTramites.size()-1))
            j--;
            else {
              j++;
              posTramiteActual +=1;
            }
          }

          codigoTramiteActual	= (String) listaCodTramites.elementAt(j);
          defTramVO.setCodigoTramite(codigoTramiteActual);

          //Listas
          defTramVO.setListaTramites(DefinicionTramitesManager.getInstance().getListaTramites(defTramVO,params));
          defTramVO.setListaClasifTramite(DefinicionTramitesManager.getInstance().getListaClasifTramites(params));
                    defTramVO.setListaCargos(CargosManager.getInstance().getListaUORs(params));
          defTramVO.setListaUnidadesTramitadoras(UORsManager.getInstance().getListaUORs(false,params));
          defTramVO.setListaEnlaces(DefinicionTramitesManager.getInstance().getListaEnlaces(defTramVO,params));

          defTramVO.setPosicion(posicion);
          defTramVO.setTramiteActual(Integer.toString(posTramiteActual));
          defTramVO.setNumeroTramites(Integer.toString(numTramites));

          defTramVO=DefinicionTramitesManager.getInstance().getTramite(defTramVO,codMunicipio,params);
          defTramForm.setDefinicionTramites(defTramVO);
          opcion = "tramite";
        }
      } else if (opcion.equals("listaPlantillas")){
        defTramVO =	defTramForm.getDefinicionTramites();
        defTramVO.setCodClasifTramite((String) request.getParameter("codClasifTram"));
        Vector listaPlantillas = new Vector();
        String codTramite = defTramVO.getCodigoTramite();
        if(codTramite == null || "".equals(codTramite)) {
          listaPlantillas = new Vector();
        } else {
          listaPlantillas = DefinicionTramitesManager.getInstance().getListaPlantillas(defTramVO,params);
        }
        defTramVO.setListaPlantillas(listaPlantillas);
      } else if (opcion.equals("actualizarComboCondEnt")){
        defTramVO =	defTramForm.getDefinicionTramites();
        defTramVO.setListaTramites(DefinicionTramitesManager.getInstance().getListaTramites(defTramVO,params));

          //ini Campos de la ventana popUp de condiciones de entrada
          Vector camposCondEntrada = new Vector();
          Vector tramites = defTramVO.getListaTramites();
          for (int i=0; i< tramites.size(); i++) {
              ElementoListaValueObject elemListVO = (ElementoListaValueObject)tramites.get(i);
              String codTramite = elemListVO.getIdentificador();
              m_Log.debug("TRAMITE "+codTramite+" --> "+ elemListVO.getDescripcion());
              DefinicionTramitesValueObject defTramVOAux = new DefinicionTramitesValueObject();
              String codMunicipio=defTramVO.getCodMunicipio();
              defTramVOAux.setCodMunicipio(codMunicipio);
              defTramVOAux.setTxtCodigo(defTramVO.getTxtCodigo());
              defTramVOAux.setCodigoTramite(codTramite);
              defTramVOAux=DefinicionTramitesManager.getInstance().getTramite(defTramVOAux,codMunicipio,params);
              Vector listaCodCampos = defTramVOAux.getListaCodCampos();
              Vector listaCampos = defTramVOAux.getListaCampos();
              for(int j=0;j<listaCampos.size();j++) {
                  DefinicionCampoValueObject dCVO = (DefinicionCampoValueObject)listaCampos.elementAt(j);
                  m_Log.debug("CAMPO : "+dCVO.getCodCampo()+" ... "+dCVO.getDescCampo());
                  camposCondEntrada.add(listaCampos.elementAt(j));
              }
          }
          defTramVO.setListaCamposTramitesCondEntrada(camposCondEntrada);
          //fin Campos de la ventana popUp de condiciones de entrada
        defTramForm.setListaDocsPresentados(DefinicionProcedimientosManager.getInstance().getListaDocumentos(defTramVO, params));
        defTramForm.setDefinicionTramites(defTramVO);
        opcion = "actualizarComboCondEnt";
        } else if (opcion.equals("altaSW")) {
            /* ORIGINAL */
            defTramVO = defTramForm.getDefinicionTramites();

            int maxOrd = Integer.parseInt(request.getParameter("maxOrdEjec"));
            int codMunicipio = Integer.parseInt(defTramVO.getCodMunicipio());
            String codProcedimiento = defTramVO.getTxtCodigo();
            int codTramite = Integer.parseInt(defTramVO.getCodigoTramite());
            int codAvanzarSW = Integer.parseInt(request.getParameter("codAvanzarSW"));
            int codRetrocederSW = Integer.parseInt(request.getParameter("codRetrocederSW"));
            int codIniciarSW = Integer.parseInt(request.getParameter("codIniciarSW"));

            // Tipo de origen de la operación de avanzar (SW o Módulo)
            String tipoOrigenAvanzar      = request.getParameter("tipoOrigenAvanzar");
            // Tipo de origen de la operación de retroceder (SW o Módulo)
            String tipoOrigenRetroceder   = request.getParameter("tipoOrigenRetroceder");

            // Nombre de la operación de avanzar
            String tituloOperacionAvanzar     = request.getParameter("tituloOperacionAvanzar");
            // Nombre de la operación de retroceder
            String tituloOperacionRetroceder  = request.getParameter("tituloOperacionRetroceder");

            String nombreModuloAvanzar    = request.getParameter("nombreModuloAvanzar");
            String nombreModuloRetroceder = request.getParameter("nombreModuloRetroceder");

            String tipoOrigenIniciar    = request.getParameter("tipoOrigenIniciar");
            String tituloOperacionIniciar = request.getParameter("tituloOperacionIniciar");
            String nombreModuloIniciar  = request.getParameter("nombreModuloIniciar");
            
            // Código del tipo de retroceso
            String codTipoRetrocesoSW = request.getParameter("codTipoRetrocesoSW");
            

            m_Log.debug("tipoOrigenAvanzarSW: " + tipoOrigenAvanzar);
            m_Log.debug("tipoOrigenRetrocederSW: " + tipoOrigenRetroceder);            
            m_Log.debug("tituloOperacionAvanzarSW: " + tituloOperacionAvanzar);
            m_Log.debug("tituloOperacionRetrocederSW: " + tituloOperacionRetroceder);
            m_Log.debug("nombreModuloAvanzar: " + nombreModuloAvanzar);
            m_Log.debug("nombreModuloRetroceder: " + nombreModuloRetroceder);
            m_Log.debug("tipoOrigenIniciar: " + tipoOrigenIniciar);
            m_Log.debug("tipoOperacionIniciar: " + tituloOperacionIniciar);
            m_Log.debug("nombreModuloIniciar: " + nombreModuloIniciar);
            m_Log.debug("codTipoRetrocesoSW: " + codTipoRetrocesoSW);
            
            /*
            public long insertWS(int codMunicipio, String codProcedimiento, int codTramite,
    								int codSWAv,int codSWRet,int codSWInicio,int ord,String tipoOrigenAvanzar,String tipoOrigenRetroceder,String tipoOrigenIniciar,
                                    String tituloOperacionAvanzar,String tituloOperacionRetroceder,String tituloOperacionIniciar,String nombreModuloAvanzar,String nombreModuloRetroceder,String nombreModuloIniciar,String[] params) throws AnotacionRegistroException {
            */
            DefinicionTramitesManager.getInstance().insertWS(codMunicipio, codProcedimiento, codTramite,
                    codAvanzarSW, codRetrocederSW, codIniciarSW, maxOrd + 1,tipoOrigenAvanzar,tipoOrigenRetroceder,tipoOrigenIniciar,tituloOperacionAvanzar,tituloOperacionRetroceder,tituloOperacionIniciar,nombreModuloAvanzar,nombreModuloRetroceder,nombreModuloIniciar,Integer.parseInt(codTipoRetrocesoSW),params);

            /****/
        
            defTramVO.setListaConfSW(DefinicionSWTramitacionManager.getInstance().getListaConfSW(
                    Integer.parseInt(defTramVO.getCodMunicipio()),
                    defTramVO.getTxtCodigo(), Integer.parseInt(defTramVO.getCodigoTramite()),params));

            // Escapamos el campo instrucciones del value object para que no de problemas.
            defTramVO.setInstrucciones(AdaptadorSQLBD.js_escape(defTramVO.getInstrucciones()));
            defTramForm.setDefinicionTramites(defTramVO);

            session.setAttribute("configurandoSW", "1");
            opcion = "tramite";
           
        } else if (opcion.equals("eliminarSW")) {
            defTramVO = defTramForm.getDefinicionTramites();
            int codMunicipio = Integer.parseInt(defTramVO.getCodMunicipio());
            String codProcedimiento = defTramVO.getTxtCodigo();
            int codTramite = Integer.parseInt(defTramVO.getCodigoTramite());
            int orden = Integer.parseInt(request.getParameter("codOrdenEjec")) + 1;
            m_Log.debug("El orden es " + orden);

            DefinicionSWTramitacionManager.getInstance().removeWSyOrdena(codMunicipio, codProcedimiento, codTramite, orden, params);
            
            defTramVO.setListaConfSW(DefinicionSWTramitacionManager.getInstance().getListaConfSW(
                    Integer.parseInt(defTramVO.getCodMunicipio()),
                    defTramVO.getTxtCodigo(), Integer.parseInt(defTramVO.getCodigoTramite()), params));

            // Escapamos el campo instrucciones del value object para que no de problemas.
            defTramVO.setInstrucciones(AdaptadorSQLBD.js_escape(defTramVO.getInstrucciones()));
            defTramForm.setDefinicionTramites(defTramVO);

            session.setAttribute("configurandoSW", "1");
            opcion = "tramite";

        }else
        if (opcion.equals("comprobarExistenciaTareasPendientesInicio")) {    
            defTramVO = defTramForm.getDefinicionTramites();
            int codMunicipio = Integer.parseInt(defTramVO.getCodMunicipio());
            String codProcedimiento = defTramVO.getTxtCodigo();
            int codTramite = Integer.parseInt(defTramVO.getCodigoTramite());
            int orden = Integer.parseInt(request.getParameter("orden"))+1;
                        
            String dato = "NO";            
            boolean exito = ModuloIntegracionExternoManager.getInstance().tieneOperacionTareasPendientesAsociadas(codMunicipio, codTramite, orden, codProcedimiento, params);
            if(exito) dato="SI";
            else dato = "NO";
            
            StringBuffer salida = new StringBuffer();
            salida.append("<VERIFICACION_TAREAS_PENDIENTES_TRAMITE>");
            salida.append("<TIENE_TAREAS_PENDIENTES>");
            salida.append(dato);
            salida.append("</TIENE_TAREAS_PENDIENTES>");
            salida.append("</VERIFICACION_TAREAS_PENDIENTES_TRAMITE>");

            m_Log.debug("Tiene el trámite " + codTramite + "");;
            try{
                // Se envía la salida
                response.setContentType("text/xml");
                PrintWriter out = response.getWriter();
                out.print(salida.toString());
                out.flush();
                out.close();

            }catch(Exception e){
                e.printStackTrace();
            }

        }
        else if (opcion.equals("grabarSW")) {
            defTramVO = defTramForm.getDefinicionTramites();
            int codMunicipio = Integer.parseInt(defTramVO.getCodMunicipio());
            String codProcedimiento = defTramVO.getTxtCodigo();
            int codTramite = Integer.parseInt(defTramVO.getCodigoTramite());
            int orden = Integer.parseInt(request.getParameter("codOrdenEjec")) + 1;
            long cfoAv = Long.parseLong(request.getParameter("cfoAvanzar"));
            long cfoRet = Long.parseLong(request.getParameter("cfoRetroceder"));
            // oscar
            long cfoIniciar = Integer.parseInt(request.getParameter("cfoIniciar"));
            // oscar
            m_Log.debug("cfoAv: " + cfoAv);
            m_Log.debug("cfoRet:" + cfoRet);
            m_Log.debug("cfoIniciar:" + cfoIniciar);
            m_Log.debug("El orden es " + orden);

            int codAvanzarSW = -1;
            int codRetrocederSW = -1;
            int codIniciarSW = -1;

            if(request.getParameter("codAvanzarSW")!=null && !"".equals(request.getParameter("codAvanzarSW")))
                codAvanzarSW = Integer.parseInt(request.getParameter("codAvanzarSW"));

            if(request.getParameter("codRetrocederSW")!=null && !"".equals(request.getParameter("codRetrocederSW")))
                codRetrocederSW = Integer.parseInt(request.getParameter("codRetrocederSW"));

            if(request.getParameter("codIniciarSW")!=null && !"".equals(request.getParameter("codIniciarSW")))
                codIniciarSW = Integer.parseInt(request.getParameter("codIniciarSW"));

            String tipoOrigenAvanzarSW    = request.getParameter("tipoOrigenAvanzar");
            String tipoOrigenRetrocederSW = request.getParameter("tipoOrigenRetroceder");
            String tipoOrigenIniciar    = request.getParameter("tipoOrigenIniciar");

            // Nombre de la operación de avanzar
            String tituloOperacionAvanzar     = request.getParameter("tituloOperacionAvanzar");
            // Nombre de la operación de retroceder
            String tituloOperacionRetroceder  = request.getParameter("tituloOperacionRetroceder");
            // Nombre de la operación de iniciar
            String tituloOperacionIniciar  = request.getParameter("tituloOperacionIniciar");

            m_Log.debug("tituloOperacionAvanzarSW: " + tituloOperacionAvanzar);
            m_Log.debug("tituloOperacionRetrocederSW: " + tituloOperacionRetroceder);

            String nombreModuloAvanzar    = request.getParameter("nombreModuloAvanzar");
            String nombreModuloRetroceder = request.getParameter("nombreModuloRetroceder");
            String nombreModuloIniciar    = request.getParameter("nombreModuloIniciar");
            
            // Código del tipo de retroceso
            String codTipoRetrocesoSW = request.getParameter("codTipoRetrocesoSW");

            m_Log.debug("tipoOrigenAvanzarSW: " + tipoOrigenAvanzarSW);
            m_Log.debug("tipoOrigenRetrocederSW: " + tipoOrigenRetrocederSW);
            m_Log.debug("tipoOrigenIniciarSW: " + tipoOrigenIniciar);
            
            m_Log.debug("nombreModuloAvanzar: " + nombreModuloAvanzar);
            m_Log.debug("nombreModuloRetroceder: " + nombreModuloRetroceder);
            m_Log.debug("nombreModuloIniciar: " + nombreModuloIniciar);
            m_Log.debug("codTipoRetrocesoSW: " + codTipoRetrocesoSW);

            DefinicionSWTramitacionManager.getInstance().updateWS(cfoAv, cfoRet,cfoIniciar, codMunicipio, codProcedimiento, codTramite, orden,codAvanzarSW, codRetrocederSW, codIniciarSW,tipoOrigenAvanzarSW,tipoOrigenRetrocederSW,tipoOrigenIniciar,tituloOperacionAvanzar,tituloOperacionRetroceder,tituloOperacionIniciar,nombreModuloAvanzar,nombreModuloRetroceder,nombreModuloIniciar,Integer.parseInt(codTipoRetrocesoSW),params);

            defTramVO.setListaConfSW(DefinicionSWTramitacionManager.getInstance().getListaConfSW(
                    Integer.parseInt(defTramVO.getCodMunicipio()),
                    defTramVO.getTxtCodigo(), Integer.parseInt(defTramVO.getCodigoTramite()),params));


            // Escapamos el campo instrucciones del value object para que no de problemas.
            defTramVO.setInstrucciones(AdaptadorSQLBD.js_escape(defTramVO.getInstrucciones()));
            defTramForm.setDefinicionTramites(defTramVO);
            session.setAttribute("configurandoSW", "1");
            opcion = "tramite";
        }else if (opcion.equals("enviarTramiteRealImportar")) {
            m_Log.debug(" ====================> enviarTramiteRealImportar");
            // SE QUIERE VISUALIZAR UN TRÁMITE DE UN PROCEDIMIENTO DE UN ENTORNO REAL DESDE UN ENTORNO DE PRUEBAS, CON LA POSIBLE
            // INTENCIÓN DEL USUARIO DE IMPORTAR EL PROCEDIMIENTO
            defTramVO = defTramForm.getDefinicionTramites();
            String enviar = request.getParameter("enviar");
            session.setAttribute("enviar", enviar);
            String importar = request.getParameter("importar");
            if (m_Log.isDebugEnabled()) m_Log.debug("IMPORTAR	" + importar);
            if ("si".equals(importar)) {
                String codMunicipioProcedimientoImportar = request.getParameter("codMunicipio");
                m_Log.debug(" =========> codMunicipioProcedimientoImportar: " + codMunicipioProcedimientoImportar);
                DefinicionProcedimientosValueObject defProcVO = new DefinicionProcedimientosValueObject();
                //defProcVO.setCodMunicipio("0");
                defProcVO.setCodMunicipio(codMunicipioProcedimientoImportar);
                defProcVO.setCodAplicacion(defTramVO.getCodAplicacion());
                String jndi = DefinicionProcedimientosManager.getInstance().obtenerJndi(defProcVO, params);
                paramsDiputacion[0] = params[0]; // "oracle";
                paramsDiputacion[6] = jndi;
                //Listas
                defTramVO.setListaClasifTramite(DefinicionTramitesManager.getInstance().getListaClasifTramites(paramsDiputacion));
                defTramVO.setListaExpRel(TramitacionManager.getInstance().getListaProcedimientos(usuario, paramsDiputacion));
                defTramVO.setListaCargos(CargosManager.getInstance().getListaUORs(params));
                defTramVO.setListaUnidadesTramitadoras(UORsManager.getInstance().getListaUORs(false,paramsDiputacion));
                defTramVO.setListaTramites(DefinicionTramitesManager.getInstance().getListaTramites(defTramVO, paramsDiputacion));
                defTramVO.setListaEnlaces(DefinicionTramitesManager.getInstance().getListaEnlaces(defTramVO, paramsDiputacion));


                defTramVO.setListaConfSW(DefinicionSWTramitacionManager.getInstance().getListaConfSW(
                		Integer.parseInt(defTramVO.getCodMunicipio()),
	  					defTramVO.getTxtCodigo(),Integer.parseInt(defTramVO.getCodigoTramite()),paramsDiputacion));

                m_Log.debug("Cargar Lista de Servicios Web Disponibles");
                Vector listaSW = new Vector();
                try {
                    listaSW = DefinicionOperacionesSWManager.getInstance().getAllPublishedOperations(paramsDiputacion);
                } catch (InternalErrorException e) {
                    m_Log.error("SE HA PRODUCIDO UN ERROR A LA HORA DE CARGAR LA LISTA DE SERVICIOS WEB PUBLICADOS");
                    e.printStackTrace();
                }
                defTramVO.setListaWebServices(listaSW);

                if (m_Log.isDebugEnabled()) m_Log.debug("siTRAMITAR	" + defTramVO.getListaWebServices());

                //String codMunicipio = "0";
                String codMunicipio = codMunicipioProcedimientoImportar;
                String txtCodigo = request.getParameter("txtCodigo");

                defTramVO.setTxtCodigo(txtCodigo);

                Vector listaCodTramites = DefinicionTramitesManager.getInstance().getListaCodTramites(defTramVO, codMunicipio, paramsDiputacion);
                String codigoTramiteActual = defTramVO.getCodigoTramite();
                int i = 0;
                if (listaCodTramites.size() > 0) {
                    while (i < listaCodTramites.size() && !listaCodTramites.elementAt(i).equals(codigoTramiteActual))
                        i++;
                }
                i = (i == listaCodTramites.size()) ? 0 : i + 1;
                defTramVO.setTramiteActual(Integer.toString(i));
                defTramVO.setNumeroTramites(Integer.toString(listaCodTramites.size()));
                defTramVO = DefinicionTramitesManager.getInstance().getTramite(defTramVO, codMunicipio, paramsDiputacion);
            } else {
                //Listas
                defTramVO.setListaClasifTramite(DefinicionTramitesManager.getInstance().getListaClasifTramites(params));
                defTramVO.setListaExpRel(TramitacionManager.getInstance().getListaProcedimientos(usuario, params));
                defTramVO.setListaCargos(CargosManager.getInstance().getListaUORs(params));
                defTramVO.setListaUnidadesTramitadoras(UORsManager.getInstance().getListaUORs(false,params));
                defTramVO.setListaTramites(DefinicionTramitesManager.getInstance().getListaTramites(defTramVO, params));
                defTramVO.setListaEnlaces(DefinicionTramitesManager.getInstance().getListaEnlaces(defTramVO, params));
                defTramVO.setListaConfSW(DefinicionSWTramitacionManager.getInstance().getListaConfSW(
                		Integer.parseInt(defTramVO.getCodMunicipio()),
	  					defTramVO.getTxtCodigo(),Integer.parseInt(defTramVO.getCodigoTramite()),params));
                m_Log.debug("Cargar Lista de Servicios Web Disponibles");
                Vector listaSW = new Vector();
                try {
                    listaSW = DefinicionOperacionesSWManager.getInstance().getAllPublishedOperations(params);
                } catch (InternalErrorException e) {
                    m_Log.error("SE HA PRODUCIDO UN ERROR A LA HORA DE CARGAR LA LISTA DE SERVICIOS WEB PUBLICADOS");
                    e.printStackTrace();
                }
                defTramVO.setListaWebServices(listaSW);

                if (m_Log.isDebugEnabled()) m_Log.debug("noTRAMITAR	" + defTramVO.getListaWebServices());

                String codMunicipio = request.getParameter("codMunicipio");
                String txtCodigo = request.getParameter("txtCodigo");

                defTramVO.setTxtCodigo(txtCodigo);

                Vector listaCodTramites = DefinicionTramitesManager.getInstance().getListaCodTramites(defTramVO, codMunicipio, params);
                String codigoTramiteActual = defTramVO.getCodigoTramite();
                int i = 0;
                if (listaCodTramites.size() > 0) {
                    while (i < listaCodTramites.size() && !listaCodTramites.elementAt(i).equals(codigoTramiteActual))
                        i++;
                }
                i = (i == listaCodTramites.size()) ? 0 : i + 1;
                defTramVO.setTramiteActual(Integer.toString(i));
                defTramVO.setNumeroTramites(Integer.toString(listaCodTramites.size()));
                defTramVO = DefinicionTramitesManager.getInstance().getTramite(defTramVO, codMunicipio, params);
            }

            //ini Campos de la ventana popUp de condiciones de entrada
            Vector camposCondEntrada = new Vector();
            Vector tramites = defTramVO.getListaTramites();
            for (int i = 0; i < tramites.size(); i++) {
                ElementoListaValueObject elemListVO = (ElementoListaValueObject) tramites.get(i);
                String codTramite = elemListVO.getIdentificador();
                m_Log.debug("TRAMITE " + codTramite + " --> " + elemListVO.getDescripcion());
                DefinicionTramitesValueObject defTramVOAux = new DefinicionTramitesValueObject();
                String codMunicipio = defTramVO.getCodMunicipio();
                defTramVOAux.setCodMunicipio(codMunicipio);
                defTramVOAux.setTxtCodigo(defTramVO.getTxtCodigo());
                defTramVOAux.setCodigoTramite(codTramite);
                defTramVOAux = DefinicionTramitesManager.getInstance().getTramite(defTramVOAux, codMunicipio, params);
                Vector listaCodCampos = defTramVOAux.getListaCodCampos();
                Vector listaCampos = defTramVOAux.getListaCampos();
                for (int j = 0; j < listaCampos.size(); j++) {
                    DefinicionCampoValueObject dCVO = (DefinicionCampoValueObject) listaCampos.elementAt(j);
                    m_Log.debug("CAMPO : " + dCVO.getCodCampo() + " ... " + dCVO.getDescCampo());
                    camposCondEntrada.add(listaCampos.elementAt(j));
                }
            }
            defTramVO.setListaCamposTramitesCondEntrada(camposCondEntrada);
            //fin Campos de la ventana popUp de condiciones de entrada
            defTramForm.setListaDocsPresentados(DefinicionProcedimientosManager.getInstance().getListaDocumentos(defTramVO, params));
            defTramForm.setDefinicionTramites(defTramVO);
            opcion = "enviar";

        }


    else if (opcion.equals("usuarioFirmaNotifElect")) {
        try {
        defTramVO=defTramForm.getDefinicionTramites();

        String tipoUsuarioFirma=request.getParameter("tipoUsuarioFirma");
        defTramVO.setTipoUsuarioFirma(tipoUsuarioFirma);

        String codigoOtroUsuarioFirma=request.getParameter("codigoOtroUsuarioFirma");
        defTramVO.setCodigoOtroUsuarioFirma(codigoOtroUsuarioFirma);
       
        int codigoOtroUsuFirma = Integer.parseInt(codigoOtroUsuarioFirma);
        UsuarioManager mgr = UsuarioManager.getInstance();
        UsuarioEscritorioValueObject usuarioEscritorioVO = mgr.buscaUsuario(codigoOtroUsuFirma);
        defTramVO.setNombreOtroUsuarioFirma(usuarioEscritorioVO.getLogin());

       
        }catch(NumberFormatException e)
        {
           m_Log.error("SE HA PRODUCIDO UN ERROR en el parseInt");
           //e.printStackTrace();
           defTramVO.setNombreOtroUsuarioFirma(null);
           opcion = "no_usuario";
        }
        defTramForm.setDefinicionTramites(defTramVO);

        opcion="usuarioFirmaNotifElect";
    }
    }
    else {	// No	hay usuario.
      m_Log.debug("MantAnotacionRegistroAction -->	no hay usuario");
      opcion = "no_usuario";
    }

    /* Redirigimos al JSP de salida*/
    m_Log.debug("<================ DefinicionTramitesAction ==================");
    return (mapping.findForward(opcion));

  }

  // Obtener la lista de temas seleccionados de	un input cuyo value es una variable
  // declarada como un array javascript.
  // RegistroValueObject tiene un vector que contiene	la lista de	códigos de temas.

  private Vector listaTemasSeleccionados(String	listSelecc)	{
    Vector lista = new Vector();
    StringTokenizer codigos =	null;

    if (listSelecc != null) {
      codigos = new StringTokenizer(listSelecc,"§¥",false);

      while	(codigos.hasMoreTokens()) {
        String cod = codigos.nextToken();
        lista.addElement(cod);
      }

    }
    return lista;
  }

  private Vector<UORDTO> getListaUTRSeleccionadas(String txtUnidades) {
    Vector<UORDTO> lista = new Vector();
    StringTokenizer codigos =	null;

    if (txtUnidades != null && !"".equals(txtUnidades)) {
      codigos = new StringTokenizer(txtUnidades,"§¥",false);

      while	(codigos.hasMoreTokens()) {
        String cod = codigos.nextToken();
        UORDTO uor = new UORDTO();
        uor.setUor_cod(cod);
        lista.addElement(uor);
      }

    }
    return lista;
  }

    private Vector listaFirmaPlantilla(String listSelecc) {
        Vector lista = new Vector();
        StringTokenizer codigos = null;
        if (listSelecc != null) {
            codigos = new StringTokenizer(listSelecc, "§¥", false);
            while (codigos.hasMoreTokens()) {
                String cod = codigos.nextToken();
                if ( (cod!=null) && (cod.equals("N")) )
                    lista.addElement(null);
                else
                    lista.addElement(cod);
            }//while
        }//if
        return lista;
    }//listaFirmaPlantilla

    /*
    private ComprobacionEstructuraVO esEstructuraCorrecta(Vector listaConfSW, String[] params) {
    	int ord = 1;
    	int codOp = -1;
    	ComprobacionEstructuraVO comp = new ComprobacionEstructuraVO();
    	comp.setCorrecta(true);
    	for (Iterator it = listaConfSW.iterator();it.hasNext();) {    		
    		AvanzarRetrocederSWVO avRetVO = (AvanzarRetrocederSWVO) it.next();
    		boolean paramsAvCorrect = paramsCorrectos (avRetVO.getCfoAvanzar(),avRetVO.getCodAvanzar(), params);
    		boolean paramsRetCorrect = paramsCorrectos (avRetVO.getCfoRetroceder(),avRetVO.getCodRetroceder(), params);
    		if (!(paramsAvCorrect&&paramsRetCorrect)) {    			
    			if (!paramsAvCorrect) {
    				comp.setAvanzar(true);
    				codOp = avRetVO.getCodAvanzar();
    			}
    			else if (!paramsRetCorrect) {
    				comp.setAvanzar(false);
    				codOp = avRetVO.getCodRetroceder();
    			}
    			comp.setOrden(ord);
    			comp.setCorrecta(false);
    			comp.setTitulo(DefinicionSWTramitacionManager.getInstance().getTitulo(codOp, params));
    		}
    		ord++;
    	}    	
    	return comp;
    }*/

    private ComprobacionEstructuraVO esEstructuraCorrecta(Vector listaConfSW, String[] params) {
    	int ord = 1;
    	int codOp = -1;
    	ComprobacionEstructuraVO comp = new ComprobacionEstructuraVO();
    	comp.setCorrecta(true);
    	for (Iterator it = listaConfSW.iterator();it.hasNext();) {
    		AvanzarRetrocederSWVO avRetVO = (AvanzarRetrocederSWVO) it.next();

            boolean paramsIniCorrect = paramsCorrectos (avRetVO.getCfoIniciar(),avRetVO.getCodIniciar(), params);
    		boolean paramsAvCorrect = paramsCorrectos (avRetVO.getCfoAvanzar(),avRetVO.getCodAvanzar(), params);
    		boolean paramsRetCorrect = paramsCorrectos (avRetVO.getCfoRetroceder(),avRetVO.getCodRetroceder(), params);

    		if (!(paramsIniCorrect&&paramsAvCorrect&&paramsRetCorrect)) {

                if (!paramsIniCorrect) {
                    comp.setIniciar(true);
    				comp.setAvanzar(false);
    				codOp = avRetVO.getCodIniciar();
    			}else
                if (!paramsAvCorrect) {
    				comp.setAvanzar(true);
    				codOp = avRetVO.getCodAvanzar();
    			}
    			else if (!paramsRetCorrect) {
    				comp.setAvanzar(false);
    				codOp = avRetVO.getCodRetroceder();
    			}
    			comp.setOrden(ord);
    			comp.setCorrecta(false);
    			comp.setTitulo(DefinicionSWTramitacionManager.getInstance().getTitulo(codOp, params));
    		}
    		ord++;
    	}
    	return comp;
    }

    private boolean paramsCorrectos(long cfo,int codOp, String[] params) {
    	if (cfo==-1) return true;
    	else {
    		Vector paramsEntrada = new Vector();
    		try {
				paramsEntrada = DefinicionOperacionesSWManager.getInstance().getParamsEntrada(cfo, codOp, params);
			} catch (InternalErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (Iterator it = paramsEntrada.iterator();it.hasNext();) {
				ParametroConfigurableVO parametro = (ParametroConfigurableVO) it.next();				
				boolean vacio = "".equals(parametro.getValorConstante()) && "".equals(parametro.getCodCampoExp());
				if ((parametro.getObligatorio()==1)&& vacio) {											
					return false;
				}
			}
    	}
    	return true;
    }


    /**
     * Combina en una única colección, las operaciones públicas de los servicios web y las operaciones públicas del módulo de integración
     * @param operacionesWS: Colección con las operaciones públicas de los servicios web
     * @param operacionesModulo: Colección con las operaciones públicas del módulo de integración
     * @return Vector
     *
    private Vector operacionesSW_Modulo(Vector operacionesWS,ArrayList<GeneralValueObject> operacionesModulo){
        Vector salida = new Vector();
        // Se incorporan a la salida las operaciones
        salida.addAll(operacionesWS);

        if(operacionesWS!=null && operacionesWS.size()>0 && operacionesModulo!=null && operacionesModulo.size()>0){
            GeneralValueObject aux = (GeneralValueObject)operacionesWS.get(0);
            int codMayor = Integer.parseInt((String)aux.getAtributo("codigoOp"));
            
            // Se busca el mayor de los códigos de las operaciones públicas del servicio web
            for(int i=1;operacionesWS!=null && i<operacionesWS.size();i++){
                GeneralValueObject gVO = (GeneralValueObject)operacionesWS.get(i);
                String codigoSW = (String)gVO.getAtributo("codigoOp");
                if(codigoSW!=null && !"".equals(codigoSW)){

                    int codActual = Integer.parseInt(codigoSW);
                    if(codActual>codMayor)
                        codMayor = codActual;
                }//if
            }// for

            int codigo = codMayor + 1;
            for(int i=0;operacionesModulo!=null && i<operacionesModulo.size();i++){
                GeneralValueObject opModulo = operacionesModulo.get(i);
                Integer integer = new Integer(codigo++);
                opModulo.setAtributo("codigoOp", integer.toString());
                opModulo.setAtributo("tipoOrigen","MI");
                salida.add(opModulo);
                
            }// for
            
        }//if
        else{
            int codigo = 1;
            for(int i=0;operacionesModulo!=null && i<operacionesModulo.size();i++){
                GeneralValueObject opModulo = operacionesModulo.get(i);
                Integer integer = new Integer(codigo);
                opModulo.setAtributo("codigoOp", integer.toString());
                opModulo.setAtributo("tipoOrigen","MI");
                codigo++;
                salida.add(opModulo);
            }// for
            
        }// else

        return salida;
    } */


      private Vector operacionesSW_Modulo(Vector operacionesWS,ArrayList<OperacionModuloIntegracionExternoVO> operacionesModulo){
        Vector salida = new Vector();
        // Se incorporan a la salida las operaciones
        salida.addAll(operacionesWS);

        if(operacionesWS!=null && operacionesWS.size()>0 && operacionesModulo!=null && operacionesModulo.size()>0){
            GeneralValueObject aux = (GeneralValueObject)operacionesWS.get(0);
            int codMayor = Integer.parseInt((String)aux.getAtributo("codigoOp"));

            // Se busca el mayor de los códigos de las operaciones públicas del servicio web
            for(int i=1;operacionesWS!=null && i<operacionesWS.size();i++){
                GeneralValueObject gVO = (GeneralValueObject)operacionesWS.get(i);
                String codigoSW = (String)gVO.getAtributo("codigoOp");
                if(codigoSW!=null && !"".equals(codigoSW)){

                    int codActual = Integer.parseInt(codigoSW);
                    if(codActual>codMayor)
                        codMayor = codActual;
                }//if
            }// for

            int codigo = codMayor + 1;
            for(int i=0;operacionesModulo!=null && i<operacionesModulo.size();i++){
                OperacionModuloIntegracionExternoVO operacion = operacionesModulo.get(i);
                Integer integer = new Integer(codigo++);
                GeneralValueObject opModulo = new GeneralValueObject();
                opModulo.setAtributo("codigoOp", integer.toString());
                opModulo.setAtributo("tipoOrigen","MI");
                opModulo.setAtributo("tituloOp",operacion.getTituloOperacion());
                opModulo.setAtributo("tituloSW",operacion.getDescripcionModulo());
                opModulo.setAtributo("nombreModulo",operacionesModulo.get(i).getNombreModulo());
                opModulo.setAtributo("altoPantalla",operacionesModulo.get(i).getAltoPantallaConfiguracion());
                opModulo.setAtributo("anchoPantalla",operacionesModulo.get(i).getAnchoPantallaConfiguracion());
                opModulo.setAtributo("urlPantallaConfiguracion",operacionesModulo.get(i).getUrlPantallaConfiguracion());

                salida.add(opModulo);

            }// for

        }//if
        else{
            int codigo = 1;
            for(int i=0;operacionesModulo!=null && i<operacionesModulo.size();i++){
                //GeneralValueObject opModulo = operacionesModulo.get(i);                
                GeneralValueObject opModulo = new GeneralValueObject();
                Integer integer = new Integer(codigo);
                opModulo.setAtributo("codigoOp", integer.toString());
                opModulo.setAtributo("tipoOrigen","MI");
                opModulo.setAtributo("tituloOp",operacionesModulo.get(i).getTituloOperacion());
                opModulo.setAtributo("tituloSW",operacionesModulo.get(i).getDescripcionModulo());
                opModulo.setAtributo("nombreModulo",operacionesModulo.get(i).getNombreModulo());
                opModulo.setAtributo("altoPantalla",operacionesModulo.get(i).getAltoPantallaConfiguracion());
                opModulo.setAtributo("anchoPantalla",operacionesModulo.get(i).getAnchoPantallaConfiguracion());
                opModulo.setAtributo("urlPantallaConfiguracion",operacionesModulo.get(i).getUrlPantallaConfiguracion());
                
                codigo++;
                salida.add(opModulo);
            }// for

        }// else

        return salida;
    }
}
