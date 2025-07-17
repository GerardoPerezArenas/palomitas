// NOMBRE DEL PAQUETE d
package es.altia.agora.interfaces.user.web.terceros;

// PAQUETES IMPORTADOS
import es.altia.agora.business.escritorio.*;
import es.altia.agora.business.terceros.*;
import es.altia.agora.business.terceros.persistence.*;
import es.altia.agora.business.terceros.mantenimiento.persistence.*;
import es.altia.agora.business.terceros.persistence.manual.DatosSuplementariosTerceroManager;
import es.altia.agora.business.util.*;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.util.FormateadorTercero;
import es.altia.agora.technical.CamposFormulario;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.technical.EstructuraCampo;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.terceros.integracion.externa.excepciones.ErrorTransaccionalTerceroExternoException;
import es.altia.flexia.terceros.integracion.externa.factoria.AltaTerceroExternoFactoria;
import es.altia.util.conexion.BDException;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase BusquedaTercerosAction</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class BusquedaTercerosAction extends ActionSession {

    String codPais = "108"; //Pais por defecto: Espana
    String codProvincia = "99"; //Provincia por defecto: desconocida
    String codMunicipio = "999"; //Municipio por defecto: desconocido
    protected static Config m_ConfigTerceros = 
          ConfigServiceHelper.getConfig("Terceros");

    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        m_Log.debug("=================== BusquedaTercerosAction =======================>");
        HttpSession session = request.getSession();

        String opcion = request.getParameter("opcion");
        if (m_Log.isInfoEnabled()) m_Log.info("la opcion en el action es " + opcion);

        // Rellenamos el form de BusquedaTerceros
        if (form == null) {
            if(m_Log.isDebugEnabled()) m_Log.debug("Rellenamos el form de BusquedaTerceros");
            form = new BusquedaTercerosForm();
            if ("request".equals(mapping.getScope())){
            	if(m_Log.isDebugEnabled()) m_Log.debug("El scope es request");
                request.setAttribute(mapping.getAttribute(), form);
            }else{
            	if(m_Log.isDebugEnabled()) m_Log.debug("El scope es session");
                session.setAttribute(mapping.getAttribute(), form);
            }
        }
        UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        ParametrosTerceroValueObject paramsTercero = (ParametrosTerceroValueObject)session.getAttribute("parametrosTercero");
        if(paramsTercero != null){
            codPais = paramsTercero.getPais();
            codProvincia = paramsTercero.getProvincia();
            codMunicipio = paramsTercero.getMunicipio();
        }
        String[] params = usuario.getParamsCon();
        BusquedaTercerosForm bTercerosForm = (BusquedaTercerosForm)form;
        bTercerosForm.setVentana(request.getParameter("ventana"));
        if(m_Log.isDebugEnabled()) 
        	m_Log.debug("Valor de BusquedaTercerosForm.ventana : " + bTercerosForm.getVentana());
        TercerosValueObject terVO = new TercerosValueObject();

        /**
         * Para el control de las vias de la base de datos del padron, se consiguen
         * instancias de varias fachadas.
         */
        ViasManager viaManager = ViasManager.getInstance();
        ProvinciasManager provinciasManager = ProvinciasManager.getInstance();
        MunicipiosManager municipiosManager = MunicipiosManager.getInstance();
        CodPostalesManager codPostalesManager = CodPostalesManager.getInstance();
        //TiposViasManager tiposViasManager = TiposViasManager.getInstance();
        TipoOcupacionManager tipoOcupManager = TipoOcupacionManager.getInstance();
        TipoDocumentosManager tipoDocumManager = TipoDocumentosManager.getInstance();
        TercerosManager terMan = TercerosManager.getInstance();        
        /* anadir ECO/ESI */
        EcosManager ecosManager = EcosManager.getInstance();
        EntidadesSingularesManager esisManager = EntidadesSingularesManager.getInstance();

        Vector resultado = new Vector();



       Config configCommon = ConfigServiceHelper.getConfig("common");
       String ALTA_TERCERO_SIN_DOMICILIO = configCommon.getString(usuario.getOrgCod() + ConstantesDatos.ALTA_TERCERO_SIN_DOMICILIO);

       
        /***** SE COMPRUEBA SI ES NECESARIO CARGAR LA PESTAÑA DE DATOS SUPLEMENTARIOS DE TERCERO *********/
        Config configTerceros = ConfigServiceHelper.getConfig("Terceros");
        String ACTIVAR_PANTALLA_CAMPOSSUP_TERCERO = configTerceros.getString(ConstantesDatos.CAMPOS_SUPLEMENTARIOS_TERCERO + usuario.getOrgCod() + ConstantesDatos.ACTIVAR);
        m_Log.debug("ACTIVAR_PANTALLA_CAMPOSSUP_TERCERO: " + ACTIVAR_PANTALLA_CAMPOSSUP_TERCERO);

        bTercerosForm.setPantallaDatosSuplementariosTerceroActivada("NO");
        if(ACTIVAR_PANTALLA_CAMPOSSUP_TERCERO!=null && "SI".equalsIgnoreCase(ACTIVAR_PANTALLA_CAMPOSSUP_TERCERO)){
            bTercerosForm.setPantallaDatosSuplementariosTerceroActivada("SI");
        }
        /************************************************************************************************/

        if (opcion.equals("buscarDomicilio")){

            GeneralValueObject domicilioVO = recogerParametrosDomicilio(request);
            String codECO = request.getParameter("codECO");
            String codESI = request.getParameter("codESI");
            domicilioVO.setAtributo("codECO",codECO);
            domicilioVO.setAtributo("codESI",codESI);
            /* fin anadir ECO/ESI */
            Vector domicilios = terMan.getListaDomicilios(params, domicilioVO);
            bTercerosForm.setListaDomicilios(domicilios);
        }else if (opcion.equals("cargarMunicipios")){
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codPais",request.getParameter("codPais"));
            gVO.setAtributo("codProvincia",request.getParameter("codProvincia"));
            Vector muns = municipiosManager.getListaMunicipios(gVO,params);
            bTercerosForm.setListaMunicipios(muns);
            /* anadir ECO/ESI */
        }else if(opcion.equals("cargarListas")){
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codPais",request.getParameter("codPais"));
            gVO.setAtributo("codProvincia",request.getParameter("codProvincia"));
            gVO.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
            gVO.setAtributo("codECO",request.getParameter("codECO"));
            gVO.setAtributo("codEntidadColectiva",request.getParameter("codECO"));
            gVO.setAtributo("codESI",request.getParameter("codESI"));

            Vector esis = esisManager.getListaEntidadesSingulares(gVO,params);
            bTercerosForm.setListaESIs(esis);

        }else if (opcion.equals("cargarCodPostales")){
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codPais",request.getParameter("codPais"));
            gVO.setAtributo("codProvincia",request.getParameter("codProvincia"));
            gVO.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
            Vector cps = codPostalesManager.getListaCodPostales(params,gVO);
            bTercerosForm.setListaCodPostales(cps);

        }else if (opcion.equals("cargarMunicipiosYCodPostales")){
            // Opcion usada al modificar un tercero para cargar los combos
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codPais",request.getParameter("codPais"));
            gVO.setAtributo("codProvincia",request.getParameter("codProvincia"));
            gVO.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
            Vector muns = municipiosManager.getListaMunicipios(gVO,params);
            bTercerosForm.setListaMunicipios(muns);
            Vector cps = codPostalesManager.getListaCodPostales(params,gVO);
            bTercerosForm.setListaCodPostales(cps);

        }else if ((opcion.equals("cargarVias"))||(opcion.equals("cargarViasDomicilios"))){
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codPais",request.getParameter("codPais"));
            gVO.setAtributo("codProvincia",request.getParameter("codProvincia"));
            gVO.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
            Vector vias = terMan.getListaVias(params,gVO);
            bTercerosForm.setListaVias(vias);
        } else if (opcion.equals("descripcionVias") || opcion.equals("descripcionViasDomicilios")){
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codPais",request.getParameter("codPais"));
            gVO.setAtributo("codProvincia",request.getParameter("codProvincia"));
            gVO.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
            gVO.setAtributo("codVia",request.getParameter("txtCodVia"));
            Vector vias = terMan.getListaVias(params,gVO);
            bTercerosForm.setListaVias(vias);
        } else if (opcion.equals("inicializar") || opcion.equals("inicializarBusquedaDoms")||
                opcion.equals("inicializarBusquedaTerc")){
            
            // #228743: OBTENEMOS LA PROPIEDAD QUE INDICA SI SE ACTUALIZA EL DOM O SE DA DE ALTA UNO NUEVO, PARA PASARSELA A LA JSP
            String modDom = m_ConfigTerceros.getString(usuario.getOrgCod() + "/ActualizarDomIntRegistroExpediente");
            request.setAttribute("actualizarDom", modDom);

             if("si".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO)){
                // Si está activada el alta de terceros sin necesidad de dar de alta un domicilio
                bTercerosForm.setPermitirAltaTerceroSinDomicilio("SI");
            }else
            if("no".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO) || "".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO)){
                // Si está activada el alta de terceros sin necesidad de dar de alta un domicilio
                bTercerosForm.setPermitirAltaTerceroSinDomicilio("NO");
            }else // En cualquier otro caso no se permite
                bTercerosForm.setPermitirAltaTerceroSinDomicilio("NO");

            String destino = request.getParameter("destino");
            if("altaDesdeBuscar".equals(destino)) {
                session.setAttribute("inicioAltaDesdeBuscar","si");
            } else if ("seleccionDesdeInformes".equals(destino)){
                session.setAttribute("seleccionDesdeInformes","si");
            }
            session.setAttribute("origen","otra");
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codPais",codPais);
            gVO.setAtributo("codProvincia",codProvincia);
            gVO.setAtributo("codMunicipio",codMunicipio);
            Vector tiposDocs = tipoDocumManager.getListaTipoDocumentos(params);
            Vector provs = provinciasManager.getListaProvincias(gVO,params);
            Vector listaM = municipiosManager.getListaMunicipios(gVO,params);
            Vector listaCP = codPostalesManager.getListaCodPostales(params,gVO);
            Vector usos = tipoOcupManager.getListaTipoOcupaciones(params);

            bTercerosForm.setListaTipoDocs(tiposDocs);
            bTercerosForm.setListaProvincias(provs);
            bTercerosForm.setListaMunicipios(listaM);
            bTercerosForm.setListaUsoViviendas(usos);
            bTercerosForm.setListaCodPostales(listaCP);
            
            /* anadir ECO/ESI */ /* En los tres casos */

            Vector listaECOs = ecosManager.getListaEcos(gVO,params);
            Vector listaESIs = esisManager.getListaEntidadesSingulares(gVO,params);
            bTercerosForm.setListaECOs(listaECOs);
            bTercerosForm.setListaESIs(listaESIs);
            if(destino!=null && destino.equals("registroAltaRapida")) {
                opcion="registroAltaRapida";
            }


            /***** SE RECUPERA ESTRUCTURA CAMPOS SUPLEMENTARIOS *******/

            try{
                String codOrganizacion = Integer.toString(usuario.getOrgCod());
                Hashtable<String,Vector> salida = DatosSuplementariosTerceroManager.getInstance().cargarEstructuraValoresDatosSuplementarios(codOrganizacion, null,params);

                Vector<EstructuraCampo> estructura = (Vector<EstructuraCampo>) salida.get("ESTRUCTURA_CAMPOS_TERCERO");
                Vector valores = (Vector)salida.get("VALORES_CAMPOS_TERCERO");
                bTercerosForm.setEstructuraCamposSuplementariosTercero(estructura);
                bTercerosForm.setValoresCamposSuplementariosTercero(valores);
            }catch(Exception e){
                e.printStackTrace();
            }


            /************************************************************/



             // Se comprueba cual es el navegador utlizado por el usuario
            String navegador = request.getParameter("navegador");
            m_Log.debug(" =======> navegador:: " + navegador);
            if(navegador!=null && navegador.equals("FF"))
                opcion="inicializarFirefox";
            
        }
        else if (opcion.equals("inicializarTerc")) {

            if("si".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO)){
                // Si está activada el alta de terceros sin necesidad de dar de alta un domicilio
                bTercerosForm.setPermitirAltaTerceroSinDomicilio("SI");
            }else
            if("no".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO) || "".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO)){
                // Si está activada el alta de terceros sin necesidad de dar de alta un domicilio
                bTercerosForm.setPermitirAltaTerceroSinDomicilio("NO");
            }else // En cualquier otro caso no se permite
                bTercerosForm.setPermitirAltaTerceroSinDomicilio("NO");


            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codPais",codPais);
            gVO.setAtributo("codProvincia",codProvincia);
            gVO.setAtributo("codMunicipio",codMunicipio);
            Vector tiposDocs = tipoDocumManager.getListaTipoDocumentos(params);
            Vector provs = provinciasManager.getListaProvincias(gVO,params);
            Vector listaM = municipiosManager.getListaMunicipios(gVO,params);
            Vector listaCP = codPostalesManager.getListaCodPostales(params,gVO);
            Vector usos = tipoOcupManager.getListaTipoOcupaciones(params);

            bTercerosForm.setListaTipoDocs(tiposDocs);
            bTercerosForm.setListaProvincias(provs);
            bTercerosForm.setListaMunicipios(listaM);
            bTercerosForm.setListaUsoViviendas(usos);
            bTercerosForm.setListaCodPostales(listaCP);
            /* anadir ECO/ESI */
            Vector listaECOs = ecosManager.getListaEcos(gVO,params);
            Vector listaESIs = esisManager.getListaEntidadesSingulares(gVO,params);
            //listaVias = viasManager.getListaViasSolas(params,gVO);
            bTercerosForm.setListaECOs(listaECOs);
            bTercerosForm.setListaESIs(listaESIs);

            /** INICIO: Se recuperan los campos suplementarios de tercero **/
            try{
                Hashtable<String,Vector> salida = DatosSuplementariosTerceroManager.getInstance().cargarEstructuraValoresDatosSuplementarios(Integer.toString(usuario.getOrgCod()),null, params);
                Vector<EstructuraCampo>  estructura = salida.get("ESTRUCTURA_CAMPOS_TERCERO");
                Vector valores = salida.get("VALORES_CAMPOS_TERCERO");

                if(estructura==null)
                    m_Log.debug("La estructura es nula");
                else
                    m_Log.debug("Número de elementos de la estructura: " + estructura.size());
                bTercerosForm.setEstructuraCamposSuplementariosTercero(estructura);
                // Al entrar en la página por primera vez, no hay valores de datos suplementarios para el tercero, hasta que no se haga una búsqueda por uno en concreto
                bTercerosForm.setValoresCamposSuplementariosTercero(valores);
                
            }catch(Exception e){
                e.printStackTrace();
                m_Log.error("Excepción Estructura campo: " + e.getMessage());
            }


            /** FIN: Se recuperan los campos suplementarios de tercero **/


            //bTercerosForm.setListaVias(listaVias);
            /* fin anadir ECO/ESI */
            session.setAttribute("inicioTercero","si");
            opcion = "inicializarBusquedaTerc";
        }else if ((opcion.equals("inicializarDesdeTercero"))){

            String tipo = request.getParameter("destino");
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codPais",codPais);
            gVO.setAtributo("codProvincia",codProvincia);
            gVO.setAtributo("codMunicipio",codMunicipio);

            Vector tiposDocs = tipoDocumManager.getListaTipoDocumentos(params);
            Vector provs = provinciasManager.getListaProvincias(gVO,params);
            Vector listaM = municipiosManager.getListaMunicipios(gVO,params);
            Vector listaCP = codPostalesManager.getListaCodPostales(params,gVO);
            Vector usos = tipoOcupManager.getListaTipoOcupaciones(params);
            //tiposVias = tiposViasManager.getListaTiposVias(params);
            bTercerosForm.setListaTipoDocs(tiposDocs);
            bTercerosForm.setListaProvincias(provs);
            bTercerosForm.setListaMunicipios(listaM);
            bTercerosForm.setListaUsoViviendas(usos);
            //bTercerosForm.setListaTipoVias(tiposVias);
            bTercerosForm.setListaCodPostales(listaCP);
            if(m_Log.isDebugEnabled()) m_Log.debug("el tipo en el action es : " + tipo);
            if(tipo.equals("alta")) {
                session.setAttribute("inicioAlta","si");
            } else if(tipo.equals("modificarTercero")) {
                session.setAttribute("inicioModificar","si");
            }
            /* anadir ECO/ESI */
            Vector listaECOs = ecosManager.getListaEcos(gVO,params);
            Vector listaESIs = esisManager.getListaEntidadesSingulares(gVO,params);
            bTercerosForm.setListaECOs(listaECOs);
            bTercerosForm.setListaESIs(listaESIs);
            //bTercerosForm.setListaVias(listaVias);
            /* fin anadir ECO/ESI */

            opcion = "inicializar";
        }else if ((opcion.equals("cargarTercero"))){
            /* Esta opcion solo se alcanza desde la funcion terceroSinDomicilio en listaBusquedaTerceros.js,
             * sirve para recargar la pagina de mantenimiento de terceros con un tercero en particular
             * cuando se ha cerrado sin asignar un domicilio al tercero.*/            
            
            // Codigo común para inicializar combos
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codPais",codPais);
            gVO.setAtributo("codProvincia",codProvincia);
            gVO.setAtributo("codMunicipio",codMunicipio);
            Vector tiposDocs = tipoDocumManager.getListaTipoDocumentos(params);
            Vector provs = provinciasManager.getListaProvincias(gVO,params);
            Vector listaM = municipiosManager.getListaMunicipios(gVO,params);
            Vector listaCP = codPostalesManager.getListaCodPostales(params,gVO);
            Vector usos = tipoOcupManager.getListaTipoOcupaciones(params);
            //tiposVias = tiposViasManager.getListaTiposVias(params);
            bTercerosForm.setListaTipoDocs(tiposDocs);
            bTercerosForm.setListaProvincias(provs);
            bTercerosForm.setListaMunicipios(listaM);
            bTercerosForm.setListaUsoViviendas(usos);
            //bTercerosForm.setListaTipoVias(tiposVias);
            bTercerosForm.setListaCodPostales(listaCP);            
            /* anadir ECO/ESI */
            Vector listaECOs = ecosManager.getListaEcos(gVO,params);
            Vector listaESIs = esisManager.getListaEntidadesSingulares(gVO,params);
            bTercerosForm.setListaECOs(listaECOs);
            bTercerosForm.setListaESIs(listaESIs);            
            /* fin anadir ECO/ESI */
            
            // Recuperamos el tercero
            String codTerc = request.getParameter("codTerc");
            m_Log.debug("Buscando tercero de id = " + codTerc);
            terVO = terMan.getDatosTercero(codTerc,params);            
            resultado.add(terVO);            
            bTercerosForm.setListaTerceros(resultado);            
            // Este valor sirve para que en inicializar() en mantTerceros.jsp sepa que debe
            // cargar el tercero que se pasa en el form.
            session.setAttribute("cargarTercero","si");            

            opcion = "inicializar"; // Redireccion a mantTerceros.jsp
        } else if (opcion.equals("buscar") || "buscar_tercero".equals(opcion)) {
            m_Log.debug("BusquedaTercerosAction opcion = buscar o buscar_tercero");
            bTercerosForm.setLineasPagina(Integer.parseInt(request.getParameter("lineasPagina")));
            bTercerosForm.setPagina(Integer.parseInt(request.getParameter("pagina")));

            CondicionesBusquedaTerceroVO condsBusq = new CondicionesBusquedaTerceroVO();
            condsBusq.setCodOrganizacion(usuario.getOrgCod());
            String strTipoDoc = request.getParameter("codTipoDoc");
            m_Log.debug("BusquedaTerceros strTipoDoc: " + strTipoDoc);

            if (strTipoDoc == null) strTipoDoc = request.getParameter("cbTipoDoc");
            if (strTipoDoc != null && !"".equals(strTipoDoc)) condsBusq.setTipoDocumento(Integer.parseInt(strTipoDoc));
            condsBusq.setDocumento(request.getParameter("txtDNI"));
            m_Log.debug("BusquedaTerceros txtDNI busqueda: " + request.getParameter("txtDNI"));

            // Llamar al manager.
            HashMap mapaResultados = terMan.getTercero(condsBusq, params);
            opcion="buscar";
            
            /* Asignamos el ValueObject al formulario*/
            try{
                Hashtable<String,Vector> salida = DatosSuplementariosTerceroManager.getInstance().cargarEstructuraValoresDatosSuplementarios(Integer.toString(usuario.getOrgCod()), terVO.getIdentificador(), params);
                Vector<EstructuraCampo> campos = (Vector<EstructuraCampo>)salida.get("ESTRUCTURA_CAMPOS_TERCERO");
                

                bTercerosForm.setEstructuraCamposSuplementariosTercero(campos);
               

            }catch(BDException e){
                e.printStackTrace();
            }
            bTercerosForm.setListaTerceros((Vector)mapaResultados.get("resultados"));
            request.setAttribute("errores", mapaResultados.get("errores"));
            

        } else if (opcion.equals("buscar_por_id") || opcion.equals("buscar_por_id_doms")) {

            terVO.setIdentificador(request.getParameter("codTerc"));
            String cargarTerceroExp = request.getParameter("cargarTerceroExp");
            m_Log.debug("BusquedaTercerosAction cargarTerceroExp: " + cargarTerceroExp);

            /******************************/
            Vector<EstructuraCampo> estructura = null;
            try{
                
                Hashtable<String,Vector> salida = DatosSuplementariosTerceroManager.getInstance().cargarEstructuraValoresDatosSuplementarios(Integer.toString(usuario.getOrgCod()),terVO.getIdentificador(), params);                
                estructura = (Vector<EstructuraCampo>)salida.get("ESTRUCTURA_CAMPOS_TERCERO");
                Vector valores = (Vector)salida.get("VALORES_CAMPOS_TERCERO");
                bTercerosForm.setEstructuraCamposSuplementariosTercero(estructura);
                bTercerosForm.setValoresCamposSuplementariosTercero(valores);
                
            }catch(BDException e){
                e.printStackTrace();
            }catch(TechnicalException e){
                e.printStackTrace();
            }
            /**********************************/


            m_Log.debug("el codigo del tercero es : " + terVO.getIdentificador());
            resultado = terMan.getByIdTercero(terVO,params);

            // Se cargan los valores de los campos suplementarios de cada tercero
            Vector otro = new Vector();
            for(int i=0;resultado!=null && i<resultado.size();i++){
                TercerosValueObject aux = (TercerosValueObject)resultado.get(i);
                aux.setValoresCamposSuplementarios(DatosSuplementariosTerceroManager.getInstance().cargaValoresDatosSuplementarios(Integer.toString(usuario.getOrgCod()),terVO.getIdentificador(),estructura, params));
                otro.add(aux);

            }
            bTercerosForm.setListaTerceros(otro);
            

            /* Asignamos el ValueObject al formulario*/
            //bTercerosForm.setListaTerceros(resultado);

            if("si".equals(cargarTerceroExp)){ m_Log.debug("redirigiendo a ocultoBusquedaTercero opcion cargarTerceroExpedientes");
                    return new ActionForward("/jsp/terceros/ocultoBusquedaTerceros.jsp?opcion=cargarTerceroExpedientes");
                }

        }else if (opcion.equals("recargaBusquedaTerceros")){
            bTercerosForm.setLineasPagina(Integer.parseInt(request.getParameter("lineasPagina")));
            bTercerosForm.setPagina(Integer.parseInt(request.getParameter("pagina")));
            opcion="buscar";

        } else if (opcion.equals("buscarTerceros")){
            // Para realizar búsqueda desde expedientes
            m_Log.debug("BusquedaTercerosAction opcion =buscarTerceros");
            String lineas = request.getParameter("lineasPagina");
            String pagina = request.getParameter("pagina");
            String selTercMiUnidad = request.getParameter("selTercMiUnidad");
            String soloTerceroFlexia = "no";
           /* if (session.getAttribute("soloTerceroFlexia")!=null)
                soloTerceroFlexia = (String) session.getAttribute("soloTerceroFlexia");
*/
            soloTerceroFlexia = request.getParameter("soloTerceroFlexia");
            
            m_Log.debug("BusquedaTercerosAction parametro lineas: " + lineas);
            m_Log.debug("BusquedaTercerosAction parametro pagina: " + pagina);
            m_Log.debug("BusquedaTercerosAction parametro codTipoDoc: " + request.getParameter("codTipoDoc"));

            int nLineas = 10;
            int nPagina = 1;
            try{
                nLineas = Integer.parseInt(lineas);
                nPagina = Integer.parseInt(pagina);
            }
            catch(NumberFormatException e){
                e.printStackTrace();
                m_Log.error(e.getMessage());
            }

            bTercerosForm.setLineasPagina(nLineas);
            bTercerosForm.setPagina(nPagina);
            
            
            // Creamos la estructura con los datos necesarios para la busqueda.
            CondicionesBusquedaTerceroVO condsBusq = recogerParametrosBusqueda(request);
            condsBusq.setCodOrganizacion(usuario.getOrgCod());
            condsBusq.setIdUsuario(usuario.getIdUsuario());
            condsBusq.setCodOrganizacion(usuario.getOrgCod());
            condsBusq.setCodEnt(usuario.getEntCod());


            // Llamar al manager.
            HashMap mapaResultados=new HashMap();
            if (selTercMiUnidad.equals("si"))   {
                


                Vector r = terMan.getTerceroExpMiUnidad(condsBusq, params);
                /***************** PARA LOS TERCEROS RECUPERADOS, SE RECUPERAN LOS VALORES DE SUS CAMPOS SUPLEMENTARIOS ***********/
                DatosSuplementariosTerceroManager camposManager = DatosSuplementariosTerceroManager.getInstance();

                try{
                    Vector<EstructuraCampo> estructura = camposManager.cargaEstructuraDatosSuplementariosTercero(Integer.toString(usuario.getOrgCod()), params);

                    Vector aux = new Vector();
                    for(int i=0;r!=null && i<r.size();i++){
                        TercerosValueObject tercero = (TercerosValueObject)r.get(i);
                        tercero.setValoresCamposSuplementarios(camposManager.cargaValoresDatosSuplementarios(Integer.toString(usuario.getOrgCod()),tercero.getIdentificador(),estructura, params));
                        aux.add(tercero);
                    }

                    r = null;
                    r = aux;

                    bTercerosForm.setListaTerceros(r);
                    request.setAttribute("errores", null);

                }catch(Exception e){
                    e.printStackTrace();
                }
                /******************************************************************************************************************/

            }else if (soloTerceroFlexia.equals("si"))
            {
                Vector r = terMan.getTerceroExpFlexia(condsBusq, params);
                /***************** PARA LOS TERCEROS RECUPERADOS, SE RECUPERAN LOS VALORES DE SUS CAMPOS SUPLEMENTARIOS ***********/
                DatosSuplementariosTerceroManager camposManager = DatosSuplementariosTerceroManager.getInstance();

                try{
                    Vector<EstructuraCampo> estructura = camposManager.cargaEstructuraDatosSuplementariosTercero(Integer.toString(usuario.getOrgCod()), params);

                    Vector aux = new Vector();
                    for(int i=0;r!=null && i<r.size();i++){
                        TercerosValueObject tercero = (TercerosValueObject)r.get(i);
                        tercero.setValoresCamposSuplementarios(camposManager.cargaValoresDatosSuplementarios(Integer.toString(usuario.getOrgCod()),tercero.getIdentificador(),estructura, params));
                        aux.add(tercero);
                    }

                    r = null;
                    r = aux;

                    bTercerosForm.setListaTerceros(r);
                    request.setAttribute("errores", null);

                }catch(Exception e){
                    e.printStackTrace();
                }
                
            }
                
            
            else{
               mapaResultados = terMan.getTercero(condsBusq, params);
                /***************** PARA LOS TERCEROS RECUPERADOS, SE RECUPERAN LOS VALORES DE SUS CAMPOS SUPLEMENTARIOS ***********/
                DatosSuplementariosTerceroManager camposManager = DatosSuplementariosTerceroManager.getInstance();
                 TercerosValueObject tercero = new TercerosValueObject();
                try{
                    
		    Vector aux2 = new Vector();
                    Vector salida2 = (Vector)mapaResultados.get("resultados");
                    
                        for(int i=0;salida2!=null && i<salida2.size();i++){
                            tercero = (TercerosValueObject)salida2.get(i);                        
                            aux2.add(tercero);
                   	}

                    salida2 = null;
                    salida2 = aux2;

                    bTercerosForm.setListaTerceros(salida2);
                    
                     if(m_Log.isDebugEnabled()) m_Log.debug("ERRORES "+mapaResultados.get("errores"));
                     
                     Collection<String> errores = new ArrayList<String>();
                     errores=(Collection<String>)  mapaResultados.get("errores");
                     
                     if(errores.contains("DEMASIADOS_RESULTADOS")) {
                             if(m_Log.isDebugEnabled()) m_Log.debug("se detecta el error de muchos resultados");
                             return mapping.findForward("demasiadosResultadosBusquedaTerceros");
                     }

                    
                    request.setAttribute("errores", mapaResultados.get("errores"));

                }catch(Exception e){
                    e.printStackTrace();
                }
               
            }// else
            

            session.removeAttribute("soloTerceroFlexia");

           // Se comprueba cual es el navegador web utilizado por el usuario
           String navegador = request.getParameter("navegador");
           if(navegador==null || (navegador!=null && navegador.equals("IE"))){
               // Internet Explorer
            opcion="buscar";
           }
           else
           if(navegador!=null && navegador.equals("FF")){
               // Firefox u otro navegador
               request.setAttribute("recargaGestionTerceros","cargarBusqueda");
               opcion = "ocultoNuevaGestionTerceros";
           }

        } else if (opcion.equals("verTercero")){

            Vector tiposDocs = tipoDocumManager.getListaTipoDocumentos(params);
            bTercerosForm.setListaTipoDocs(tiposDocs);
            String codTerc = request.getParameter("nCS");
            String versTerc = request.getParameter("codMun");
            String codDom = request.getParameter("codProc");
            GeneralValueObject ter1VO = new GeneralValueObject();
            ter1VO.setAtributo("codTerc",codTerc);
            ter1VO.setAtributo("versTerc",versTerc);
            ter1VO.setAtributo("codDom",codDom);
            ter1VO.setAtributo("tipoDocumento","");
            ter1VO.setAtributo("documento","");
            ter1VO.setAtributo("nombre","");
            ter1VO.setAtributo("apellido1","");
            ter1VO.setAtributo("apellido2","");
            ter1VO.setAtributo("codPais","");
            ter1VO.setAtributo("codProvincia","");
            ter1VO.setAtributo("codMunicipio","");
            ter1VO.setAtributo("codVia","");
            ter1VO.setAtributo("domicilio","");
            ter1VO.setAtributo("numDesde","");
            ter1VO.setAtributo("letraDesde","");
            ter1VO.setAtributo("numHasta","");
            ter1VO.setAtributo("letraHasta","");
            resultado = terMan.getTerceroHistorico(ter1VO,params);
            opcion="verTercero";
            if(m_Log.isDebugEnabled()) m_Log.debug("el tamaño del vector de resultado es : " + resultado.size());
            /* Asignamos el ValueObject al formulario*/
            bTercerosForm.setListaTerceros(resultado);
       
        }else if (opcion.equals("grabarTerceroRapido") || opcion.equals("grabarTerceroRapidoConfirmado")){
                
            m_Log.debug("ALTA DE TERCERO RÁPIDA");
            terVO.setIdentificador(request.getParameter("txtIdTercero"));
            terVO.setTipoDocumento(request.getParameter("codTipoDoc"));
            terVO.setDocumento(request.getParameter("txtDNI"));
            terVO.setNombre(request.getParameter("txtInteresado"));
            terVO.setApellido1(request.getParameter("txtApell1"));
            terVO.setApellido2(request.getParameter("txtApell2"));
            terVO.setPartApellido1(request.getParameter("txtPart"));
            terVO.setPartApellido2(request.getParameter("txtPart2"));
            terVO.setNombreCompleto(concatenaNombreTercero(terVO));
            terVO.setNormalizado("1");
            terVO.setTelefono(request.getParameter("txtTelefono"));
            terVO.setEmail(request.getParameter("txtCorreo"));
            terVO.setSituacion('A');
            terVO.setUsuarioAlta(String.valueOf(usuario.getIdUsuario()));
            terVO.setModuloAlta(String.valueOf(usuario.getAppCod()));
            terVO.setDomicilios(new Vector());
            terVO.setOrigen("SGE");
            
            //RECOGEMOS LOS DATOS DEL DOMICILIO 
            DomicilioSimpleValueObject domicilio = new DomicilioSimpleValueObject();
            domicilio.setIdPais(request.getParameter("codPais"));
            domicilio.setProvincia(request.getParameter("descProvincia"));
            domicilio.setMunicipio(request.getParameter("descMunicipio"));
            domicilio.setIdProvincia(request.getParameter("codProvincia"));
            domicilio.setIdMunicipio(request.getParameter("codMunicipio"));
            domicilio.setDomicilio(request.getParameter("txtDomicilio"));
            domicilio.setNormalizado("2");
            domicilio.setEnPadron("1");
            Vector<DomicilioSimpleValueObject> dom = new Vector<DomicilioSimpleValueObject>();
            dom.add(domicilio);
            terVO.setDomicilios(dom);
            m_Log.debug("******* TERCERO "+request.getParameter("codTipoDoc")+","+request.getParameter("txtDNI")+","+terVO.getNombreCompleto());
            m_Log.debug("******* IDENTIFICADOR "+terVO.getIdentificador());
            m_Log.debug("******* DOMICILIO "+request.getParameter("codPais")+","+request.getParameter("descProvincia")+","+request.getParameter("codProvincia")+","+request.getParameter("descMunicipio")+","+request.getParameter("codMunicipio")+","+request.getParameter("txtDomicilio"));
            terVO.setOrigen("SGE");
            int id=-1;
            boolean existeTercero = terMan.existeTercero(terVO,params);
            // Opcion grabarTerceroRapidoConfirmado= existe un tercero con mismo 
            // doc pero el usuario ha decidido darlo de alta de todas formas.
            if (!existeTercero || opcion.equals("grabarTerceroRapidoConfirmado")) {
                opcion = "grabarTerceroRapido";
                id = terMan.setTercero(terVO,params);
                terVO.setIdentificador(String.valueOf(id));
                resultado.add(terVO);
                /* Asignamos el ValueObject al formulario*/
                bTercerosForm.setListaTerceros(resultado);
               if(m_Log.isDebugEnabled()) m_Log.debug("El valor del Id del usuario es : " + id);
                int idDom;
                //cOMPROBAMOS QUE EL TERCERO FUE INSERTADO CORRECTAMENTE
                if(id!=-1){
                    idDom = terMan.setDomicilioTerceroRapido(terVO,params);
                     domicilio.setIdDomicilio(String.valueOf(idDom));
                        dom.set(0,domicilio);
                        terVO.setDomicilios(dom);
                        resultado.add(terVO);
                    /* Asignamos el ValueObject al formulario*/
                    bTercerosForm.setListaTerceros(resultado);
                }else{
                     m_Log.debug("** Tercero grabado off***");
                }
            } else if (!(terVO.getTipoDocumento().equals("0"))) {
                opcion="terceroYaExiste";
            } 
                   
        }else if (opcion.equals("grabarTercero") || opcion.equals("grabarTerceroConfirmado")){
            terVO.setTipoDocumento(request.getParameter("codTipoDoc"));
            terVO.setDocumento(request.getParameter("txtDNI"));
            terVO.setNombre(request.getParameter("txtInteresado"));
            terVO.setApellido1(request.getParameter("txtApell1"));
            terVO.setApellido2(request.getParameter("txtApell2"));
            terVO.setPartApellido1(request.getParameter("txtPart"));
            terVO.setPartApellido2(request.getParameter("txtPart2"));
            terVO.setNombreCompleto(concatenaNombreTercero(terVO));
            terVO.setNormalizado("1");
            terVO.setTelefono(request.getParameter("txtTelefono"));
            terVO.setEmail(request.getParameter("txtCorreo"));
            terVO.setSituacion('A');
            terVO.setUsuarioAlta(String.valueOf(usuario.getIdUsuario()));
            terVO.setModuloAlta(String.valueOf(usuario.getAppCod()));
            terVO.setDomicilios(new Vector());
            terVO.setOrigen("SGE");
            int id;
            String grabarDirecto = request.getParameter("grabarDirecto");
            boolean existeTercero = terMan.existeTercero(terVO,params);
            // Opcion grabarTerceroConfirmado = existe un tercero con mismo doc
            // pero el usuario ha decidido darlo de alta de todas formas.
            if ((!existeTercero) || grabarDirecto.equals("si") || opcion.equals("grabarTerceroConfirmado")) {
                opcion = "grabarTercero";
                id = terMan.setTercero(terVO,params);
                terVO.setIdentificador(String.valueOf(id));
                resultado.add(terVO);
                /* Asignamos el ValueObject al formulario*/
                bTercerosForm.setListaTerceros(resultado);
                if(m_Log.isDebugEnabled()) m_Log.debug("El valor del Id del usuario es : " + id);
            } else if (!(terVO.getTipoDocumento().equals("0"))) {
                opcion="terceroYaExiste";
            } else {
                opcion="existeTerceroSinDoc";
            }
        } else if (opcion.equals("grabarTerceroExt")){

            terVO.setTipoDocumento(request.getParameter("codTipoDoc"));
            terVO.setTipoDocDesc(request.getParameter("descTipoDoc"));
            terVO.setDocumento(request.getParameter("txtDNI"));
            terVO.setNombre(request.getParameter("txtInteresado"));
            terVO.setApellido1(request.getParameter("txtApell1"));
            terVO.setApellido2(request.getParameter("txtApell2"));
            terVO.setPartApellido1(request.getParameter("txtPart"));
            terVO.setPartApellido2(request.getParameter("txtPart2"));
            terVO.setNombreCompleto(concatenaNombreTercero(terVO));
            terVO.setNormalizado("1");
            terVO.setTelefono(request.getParameter("txtTelefono"));
            terVO.setEmail(request.getParameter("txtCorreo"));
            terVO.setSituacion('A');
            terVO.setUsuarioAlta(String.valueOf(usuario.getIdUsuario()));
            terVO.setModuloAlta(String.valueOf(usuario.getAppCod()));
            terVO.setDomicilios(new Vector());
            terVO.setCodTerceroOrigen(request.getParameter("codTerceroOrigen"));
            int id;

            CondicionesBusquedaTerceroVO conds = new CondicionesBusquedaTerceroVO();
            conds.setTipoDocumento(Integer.parseInt(terVO.getTipoDocumento()));
            if (terVO.getTipoDocumento().equals("0")) {
                conds.setNombre(terVO.getNombre());
                conds.setApellido1(terVO.getApellido1());
                conds.setApellido2(terVO.getApellido2());
            } else {
                conds.setDocumento(terVO.getDocumento());
            }
            Vector tercerosReps = terMan.getTerceroInterno(conds, params);
            if (tercerosReps.size() > 0) {
                Vector listaTiposDocs = tipoDocumManager.getListaTipoDocumentos(params);
                HashMap<String, String> tipoDocsMap = new HashMap<String, String>();
                for (Object tipoDocObj : listaTiposDocs) {
                    GeneralValueObject tipoDatoGVO = (GeneralValueObject) tipoDocObj;
                    tipoDocsMap.put((String) tipoDatoGVO.getAtributo("codTipoDoc"), (String) tipoDatoGVO.getAtributo("descTipoDoc"));
                }

                for (Object objTercero: tercerosReps) {
                    TercerosValueObject terceroVO = (TercerosValueObject)objTercero;
                    terceroVO.setTipoDocDesc(tipoDocsMap.get(terceroVO.getTipoDocumento()));
                }

                request.setAttribute("tercerosRepetidos", tercerosReps);
                request.setAttribute("terceroNuevo", terVO);
                m_Log.debug("<================ BusquedaTercerosAction =======================");
                return mapping.findForward("grabarTerceroExtExiste");
            } else{
                id = terMan.setTercero(terVO,params);
            }
            if(m_Log.isDebugEnabled()) m_Log.debug("El valor del Id del usuario es : " + id);
            terVO.setIdentificador(String.valueOf(id));
            resultado.add(terVO);
            /* Asignamos el ValueObject al formulario*/
            bTercerosForm.setListaTerceros(resultado);

        } else if (opcion.equals("forzarGrabarTercero")) {
            terVO.setTipoDocumento(request.getParameter("codTipoDoc"));
            terVO.setTipoDocDesc(request.getParameter("descTipoDoc"));
            terVO.setDocumento(request.getParameter("txtDNI"));
            terVO.setNombre(request.getParameter("txtInteresado"));
            terVO.setApellido1(request.getParameter("txtApell1"));
            terVO.setApellido2(request.getParameter("txtApell2"));
            terVO.setNombreCompleto(concatenaNombreTercero(terVO));
            terVO.setNormalizado("1");
            terVO.setTelefono(request.getParameter("txtTelefono"));
            terVO.setEmail(request.getParameter("txtCorreo"));
            terVO.setSituacion('A');
            terVO.setUsuarioAlta(String.valueOf(usuario.getIdUsuario()));
            terVO.setModuloAlta(String.valueOf(usuario.getAppCod()));
            terVO.setDomicilios(new Vector());

            int id = terMan.setTercero(terVO, params);
            if (m_Log.isDebugEnabled()) m_Log.debug("El valor del Id del usuario es : " + id);

            terVO.setIdentificador(Integer.toString(id));
            terVO.setVersion("1");

            request.setAttribute("tercero", terVO);
            request.setAttribute("opcion", opcion);
            m_Log.debug("<================ BusquedaTercerosAction =======================");
            return mapping.findForward(opcion);

        } else if (opcion.equals("updateTercAntiguo")) {
            terVO.setIdentificador(request.getParameter("oldIdentificador"));
            terVO.setVersion(request.getParameter("oldNumVersion"));
            terVO.setTipoDocumento(request.getParameter("codTipoDoc"));
            terVO.setTipoDocDesc(request.getParameter("descTipoDoc"));
            terVO.setDocumento(request.getParameter("txtDNI"));
            terVO.setNombre(request.getParameter("txtInteresado"));
            terVO.setApellido1(request.getParameter("txtApell1"));
            terVO.setApellido2(request.getParameter("txtApell2"));
            terVO.setNombreCompleto(concatenaNombreTercero(terVO));
            terVO.setNormalizado("1");
            terVO.setTelefono(request.getParameter("txtTelefono"));
            terVO.setEmail(request.getParameter("txtCorreo"));
            terVO.setSituacion('A');
            terVO.setUsuarioAlta(String.valueOf(usuario.getIdUsuario()));
            terVO.setModuloAlta(String.valueOf(usuario.getAppCod()));
            terVO.setDomicilios(new Vector());

            int codigoTercero = terMan.updateTercero(terVO, params);
            int numVersion = Integer.parseInt(terVO.getVersion()) + 1;
            terVO.setIdentificador(Integer.toString(codigoTercero));
            terVO.setVersion(Integer.toString(numVersion));

            request.setAttribute("tercero", terVO);
            request.setAttribute("opcion", opcion);
            m_Log.debug("<================ BusquedaTercerosAction =======================");
            return mapping.findForward("forzarGrabarTercero");


        }else if (opcion.equals("grabarTerceroDuplicado")){
            terVO.setTipoDocumento(request.getParameter("codTipoDoc"));
            terVO.setDocumento(request.getParameter("txtDNI"));
            terVO.setNombre(request.getParameter("txtInteresado"));
            terVO.setApellido1(request.getParameter("txtApell1"));
            terVO.setApellido2(request.getParameter("txtApell2"));
            terVO.setPartApellido1(request.getParameter("txtPart"));
            terVO.setPartApellido2(request.getParameter("txtPart2"));
            terVO.setNombreCompleto(concatenaNombreTercero(terVO));
            terVO.setNormalizado("1");
            terVO.setTelefono(request.getParameter("txtTelefono"));
            terVO.setEmail(request.getParameter("txtCorreo"));
            terVO.setSituacion('A');
            terVO.setUsuarioAlta(String.valueOf(usuario.getIdUsuario()));
            terVO.setModuloAlta(String.valueOf(usuario.getAppCod()));
            terVO.setDomicilios(new Vector());
            int id = terMan.setTerceroDuplicado(terVO,params);
            terVO.setIdentificador(String.valueOf(id));
            resultado.add(terVO);
            /* Asignamos el ValueObject al formulario*/
            bTercerosForm.setListaTerceros(resultado);
            opcion = "grabarTercero";
        }else if (opcion.equals("modificarTercero")){
            String insertarDoc = request.getParameter("insertarDoc");
            terVO.setIdentificador(request.getParameter("txtIdTercero"));
            terVO.setVersion(request.getParameter("txtVersion"));
            terVO.setTipoDocumento(request.getParameter("codTipoDoc"));
            terVO.setDocumento(request.getParameter("txtDNI"));
            terVO.setNombre(request.getParameter("txtInteresado"));
            terVO.setApellido1(request.getParameter("txtApell1"));
            terVO.setApellido2(request.getParameter("txtApell2"));
            terVO.setPartApellido1(request.getParameter("txtPart"));
            terVO.setPartApellido2(request.getParameter("txtPart2"));
            terVO.setNombreCompleto(concatenaNombreTercero(terVO));
            terVO.setNormalizado("1");
            terVO.setTelefono(request.getParameter("txtTelefono"));
            terVO.setEmail(request.getParameter("txtCorreo"));
            terVO.setSituacion('A');
            terVO.setUsuarioAlta(String.valueOf(usuario.getIdUsuario()));
            terVO.setModuloAlta(String.valueOf(usuario.getAppCod()));
            terVO.setDomicilios(new Vector());
            terVO.setOrigen("SGE");
            Vector terceros = bTercerosForm.getListaTerceros();
            if ((insertarDoc.equals("no")) || (insertarDoc.equals("si") && !(terMan.existeTercero(terVO,params)))) {
                int id = terMan.updateTercero(terVO,params);
                terVO.setIdentificador(String.valueOf(id));
                resultado.add(terVO);
                bTercerosForm.setListaTerceros(terceros);
            } else {
                opcion="docYaExiste";
            }
        }else if (opcion.equals("cambiaSituacionTercero")) { // bajaTercero
            terVO.setIdentificador(request.getParameter("txtIdTercero"));
            char situacion = request.getParameter("situacion").charAt(0);
            terVO.setSituacion(situacion);
            terVO.setUsuarioBaja(String.valueOf(usuario.getIdUsuario()));
            terMan.cambiaSituacionTercero(terVO,params);
        }
        else if ((opcion.equals("grabarDomicilio")) || (opcion.equals("grabarAltaDomicilio")) ||
               (opcion.equals("grabarDomicilioExt")) || (opcion.equals("grabarDomicilioExtTercAgora")))
            // grabarAltaDomicilio: opcion para nueva interfaz de terceros
        {
            /**
             * AL AÑADIR LA POSIBILIDAD DE OBTENER VIAS DEL OTRAS BASES DE DATOS DEBEMOS COMPROBAR SI EXISTE EN AGORA
             * SI NO EXISTE INSERTARLA.
             */

            GeneralValueObject via = new GeneralValueObject();
            via.setAtributo("codPais",request.getParameter("codPais"));
            via.setAtributo("codProvincia",request.getParameter("codProvincia"));
            via.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
            via.setAtributo("descVia",request.getParameter("descVia"));
            via.setAtributo("codTipoVia",request.getParameter("codTVia"));

            // Si se ha encontrado la via de un servicio de busqueda externo, no se esta grabando el nombre
            // corto de la vía. Si el nombre largo es mayor que 25 (valor de tamaño de la BBDD para la columna nombre corto) daría un fallo. Se hace substring
            String nombreCortoVia = request.getParameter("descVia");
            if (nombreCortoVia.length()<=25)     via.setAtributo("nombreCorto", nombreCortoVia);
            else via.setAtributo("nombreCorto", nombreCortoVia.substring(0, 24));

            via.setAtributo("usuario",String.valueOf(usuario.getIdUsuario()));
           int id = -1;
           if (!"".equals(request.getParameter("descVia"))&&request.getParameter("descVia")!=null){
               id = viaManager.altaViaNoRepetido(via,params);
               if(m_Log.isDebugEnabled()) m_Log.debug("La via tienen el codigo "+ id);
           }
           String idVia = String.valueOf(id);
           
            String domActual = request.getParameter("domActual");
             // Datos del domicilio
            DomicilioSimpleValueObject domicilio = recogerParametrosDomicilioVO(request);
            if (!domicilio.getCodigoVia().equals("")){
                domicilio.setIdVia(idVia);
            }
            Vector<DomicilioSimpleValueObject> dom = new Vector<DomicilioSimpleValueObject>();
            dom.add(domicilio);
            terVO.setDomPrincipal(request.getParameter("nuevoDomPrincipal"));
            terVO.setIdentificador(request.getParameter("txtIdTercero"));
            terVO.setDomicilios(dom);
            terVO.setUsuarioAlta(String.valueOf(usuario.getIdUsuario()));
            int idDom;
            /**
             * COMPROBAMOS SI EL DOMICILIO A INSERTAR ESTA YA EN LA BASE DE DATOS
             */

            String txtNormalizado = request.getParameter("txtNormalizado");
            int d = terMan.existeDomicilio(terVO,params);
            if (d >0)
            {
               if(m_Log.isDebugEnabled()) m_Log.debug("EXISTE EL DOMICILIO");
                txtNormalizado = "1";
                domicilio.setNormalizado(txtNormalizado);
                terVO.setIdDomicilio(String.valueOf(d));
                idDom = terMan.setDomicilioTercero(terVO,params);
            }
            else
            {
                if(m_Log.isDebugEnabled()) m_Log.debug("NO EXISTE EL DOMICILIO");
                if(txtNormalizado.equals("2")){
                    idDom = terMan.setDomicilioTercero(terVO,params);
                }else{
                    terVO.setIdDomicilio(request.getParameter("txtIdDomicilio"));
                    idDom = terMan.setDomicilioTercero(terVO,params);
                }
                if(m_Log.isDebugEnabled()) m_Log.debug("SE INSERTA EL COD POSTAL EN CASO" +
                        "DE QUE NO EXISTA");
                
                GeneralValueObject gVO = obtenerDatosCodPostal(terVO);

                String codigo =(String) gVO.getAtributo("descPostal");
                if (codigo!=null && !"".equals(codigo) && !codPostalesManager.existeCodPostal(gVO, params)){
                    bTercerosForm.setListaCodPostales(
                        codPostalesManager.altaCodPostal(gVO, params));
                }
            }


            /************* SE RECUPERAN LOS CAMPOS SUPLEMENTARIOS ************/
            try{
                Hashtable<String,Vector> salida = DatosSuplementariosTerceroManager.getInstance().cargarEstructuraValoresDatosSuplementarios(Integer.toString(usuario.getOrgCod()), terVO.getIdentificador(), params);
                Vector<EstructuraCampo> campos = (Vector<EstructuraCampo>)salida.get("ESTRUCTURA_CAMPOS_TERCERO");
                Vector valores = (Vector)salida.get("VALORES_CAMPOS_TERCERO");

                bTercerosForm.setEstructuraCamposSuplementariosTercero(campos);
                terVO.setValoresCamposSuplementarios(valores);

            }catch(BDException e){
                e.printStackTrace();
            }

            domicilio.setIdDomicilio(String.valueOf(idDom));
            domicilio.setDomActual(domActual);
            dom.set(0,domicilio);
            terVO.setDomicilios(dom);
            resultado.add(terVO);
            /* Asignamos el ValueObject al formulario*/
            if ("grabarAltaDomicilio".equals(opcion)){
                bTercerosForm.setListaTercerosModificados(resultado);
            }else {
                bTercerosForm.setListaTerceros(resultado);
            }
        }else if (opcion.equals("modificarDomicilio")){
            Vector<DomicilioSimpleValueObject> dom = new Vector<DomicilioSimpleValueObject>();
            String idVia;
            String txtNormalizado = request.getParameter("txtNormalizado");
            String codUso = request.getParameter("codUso");
            String codTVia = request.getParameter("codTVia");            
            String txtCodVia = request.getParameter("txtCodVia");
            String txtNumDesde = request.getParameter("txtNumDesde");
            String txtNumHasta = request.getParameter("txtNumHasta");                                            
            String codESI = request.getParameter("codESI");
            
            /* opcionDomicilioCompartido (definido en mantTerceros.jsp)nos indica si debemos 
             * comprobar si hay mas de un tercero compartiendo el domicilio que vamos a modificar. El 
             * valor por defecto, 'alertar' indica que aun no hemos comprobado ni alertado al usuario. 
             * Una vez comprobado se alerta al usuario si es el caso (está compartido) */
            String opcionDomicilioCompartido = request.getParameter("opcionDomicilioCompartido");
            m_Log.debug("Valor de opcionDomicilioCompartido : " + opcionDomicilioCompartido);
            
            // Comprobacion de numero de terceros que comparten el domicilio.
            int numTerceros = 0;
            if (opcionDomicilioCompartido.equals("alertar")) { 
	            DomicilioSimpleValueObject domVO = new DomicilioSimpleValueObject();
	            domVO.setIdDomicilio(request.getParameter("txtIdDomicilio"));
	            numTerceros = terMan.getNumeroTercerosByDomicilio(domVO,params);
	            m_Log.debug("Numero de terceros con este domicilio : " + numTerceros);
            } 
            
            // Si mas de un tercero comparte el domicilio debemos alertar al usuario antes de hacer cq cambio.
            if (numTerceros > 1) {
            	opcion = "alertarDomicilioCompartido";
            	m_Log.debug("Se procede a alertar al usuario");
            } else {
            	m_Log.debug("Se procede a modificar el domicilio");
	            GeneralValueObject via = new GeneralValueObject();
	            via.setAtributo("codPais",request.getParameter("codPais"));
	            via.setAtributo("codProvincia",request.getParameter("codProvincia"));
	            via.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
	            via.setAtributo("descVia",request.getParameter("descVia"));
	            via.setAtributo("codTipoVia",request.getParameter("codTVia"));
	            via.setAtributo("usuario",String.valueOf(usuario.getIdUsuario()));
	            // Se intenta añadir vía en caso de que ya exista sólo se consigue el idVia.            
	            int id = -1;
	            if (!"".equals(request.getParameter("descVia"))&&request.getParameter("descVia")!=null){
	                id = viaManager.altaViaNoRepetido(via,params);
	                if(m_Log.isDebugEnabled()) m_Log.debug("La via tienen el codigo "+ id);
	            }
	            idVia = String.valueOf(id);
	                        
	            DomicilioSimpleValueObject domicilio = new DomicilioSimpleValueObject();
	            domicilio.setIdDomicilio(request.getParameter("txtIdDomicilio"));
	            domicilio.setNormalizado(txtNormalizado);
	            domicilio.setCodTipoUso(codUso);
	            domicilio.setDescTipoUso(request.getParameter("descUso"));
	            domicilio.setIdTipoVia(codTVia);
	            domicilio.setTipoVia(request.getParameter("descTVia"));
	            domicilio.setCodigoVia(txtCodVia);
	            domicilio.setIdPais(request.getParameter("codPais"));
	            domicilio.setIdProvincia(request.getParameter("codProvincia"));
	            domicilio.setIdMunicipio(request.getParameter("codMunicipio"));
	            if (!domicilio.getCodigoVia().equals("")){
	                domicilio.setIdPaisVia(request.getParameter("codPais"));
	                domicilio.setIdProvinciaVia(request.getParameter("codProvincia"));
	                domicilio.setIdMunicipioVia(request.getParameter("codMunicipio"));
	                domicilio.setIdVia(idVia);
	            }
	            domicilio.setCodigoPostal(request.getParameter("descPostal"));
	            domicilio.setBarriada(request.getParameter("txtBarriada"));
	            domicilio.setDomicilio(request.getParameter("txtDomicilio"));
	            domicilio.setNumDesde(txtNumDesde);
	            domicilio.setLetraDesde(request.getParameter("txtLetraDesde"));
	            domicilio.setNumHasta(txtNumHasta);
	            domicilio.setLetraHasta(request.getParameter("txtLetraHasta"));
	            domicilio.setBloque(request.getParameter("txtBloque"));
	            domicilio.setPortal(request.getParameter("txtPortal"));
	            domicilio.setEscalera(request.getParameter("txtEsc"));
	            domicilio.setPlanta(request.getParameter("txtPlta"));
	            domicilio.setPuerta(request.getParameter("txtPta"));
	            /* anadir ECOESI */
	            if (codESI!= null){
	                if (!"".equals(codESI)){
	                    domicilio.setCodESI(codESI);
	                }
	            }
	            /* fin anadir ECOESI */
	            domicilio.setModificado("NO");
	            
	            dom.add(domicilio);
	            
	            terVO.setIdentificador(request.getParameter("txtIdTercero"));
	            terVO.setDomicilios(dom);
	            terVO.setUsuarioAlta(String.valueOf(usuario.getIdUsuario()));
	            
	            terMan.updateDomicilioTercero(terVO,params);
	            if ("SI".equals(domicilio.getModificado())){            	
	                bTercerosForm.setResOp("domicilioModificado");
                        GeneralValueObject gVO = obtenerDatosCodPostal(terVO);
                        if (!codPostalesManager.existeCodPostal(gVO, params)){
                             bTercerosForm.setListaCodPostales(
                             codPostalesManager.altaCodPostal(gVO, params));
                        }
                    }
	            else bTercerosForm.setResOp("domicilioNoModificado");
            }

            
        }else if (opcion.equals("borrarDomicilio")){
            terVO.setIdentificador(request.getParameter("txtIdTercero"));
            terVO.setIdDomicilio(request.getParameter("txtIdDomicilio"));
            terVO.setDomPrincipal(request.getParameter("nuevoDomPrincipal"));
            terVO.setUsuarioBaja(String.valueOf(usuario.getIdUsuario()));
            terMan.deleteDomicilioTercero(terVO,params);
        }else if (opcion.equals("eliminarTercero")){
            String sPosElemento = (String)request.getParameter("posElemento");
            if(sPosElemento != null && !"".equals(sPosElemento.trim())){
                int posElemento = Integer.parseInt(sPosElemento);
                BusquedaTercerosForm bForm =(BusquedaTercerosForm)session.getAttribute("BusquedaTercerosForm");
                Vector listaTerceros = bForm.getListaTerceros();
                if(m_Log.isDebugEnabled()){
                    m_Log.debug("Tamaño Lista Terceros:" + listaTerceros.size());
                    m_Log.debug("Posicion a eliminar:" + posElemento);
                }
                listaTerceros.removeElementAt(posElemento);
                bForm.setListaTerceros(listaTerceros);
            }

        }else if (opcion.substring(0,8).equals("registro")){
            String codTipoDoc;
            codTipoDoc = request.getParameter("codTipoDoc");
            terVO.setTipoDocumento(codTipoDoc);
            //terVO..setDescTipoDoc(request.getParameter("descTipoDoc"));
            terVO.setIdentificador(request.getParameter("txtIdTercero"));
            terVO.setVersion(request.getParameter("txtVersion"));
            terVO.setDocumento(request.getParameter("txtDNI"));
            terVO.setNombre(request.getParameter("txtInteresado"));
            terVO.setApellido1(request.getParameter("txtApell1"));
            terVO.setApellido2(request.getParameter("txtApell2"));
            terVO.setPartApellido1(request.getParameter("txtPart"));
            terVO.setPartApellido2(request.getParameter("txtPart2"));
            terVO.setTelefono(request.getParameter("txtTelefono"));
            terVO.setEmail(request.getParameter("txtCorreo"));
            terVO.setIdDomicilio(request.getParameter("txtIdDomicilio"));

            DomicilioSimpleValueObject dom= new DomicilioSimpleValueObject();
            dom.setIdDomicilio(request.getParameter("txtIdDomicilio"));
            dom.setPais(request.getParameter("descPais"));
            dom.setProvincia(request.getParameter("descProvincia"));
            dom.setMunicipio(request.getParameter("descMunicipio"));
            dom.setCodigoPostal(request.getParameter("descPostal"));
            dom.setBarriada(request.getParameter("txtBarriada"));

            String descTVia = request.getParameter("descTVia");
            String numDesde = request.getParameter("txtNumDesde");
            String letraDesde = request.getParameter("txtLetraDesde");
            String numHasta = request.getParameter("txtNumHasta");
            String letraHasta = request.getParameter("txtLetraHasta");
            String bloque = request.getParameter("txtBloque");
            String portal = request.getParameter("txtPortal");
            String escal = request.getParameter("txtEsc");
            String planta = request.getParameter("txtPlta");
            String puerta = request.getParameter("txtPta");
            String domicilio ="";
            domicilio = (!descTVia.equals("")) ? domicilio+descTVia+" ":domicilio;
            domicilio += request.getParameter("txtDomicilio");
            domicilio = (!numDesde.equals("")) ? domicilio+" "+numDesde:domicilio;
            domicilio = (!letraDesde.equals("")) ? domicilio+" "+letraDesde+" ":domicilio;
            domicilio = (!numHasta.equals("")) ? domicilio+" "+numHasta:domicilio;
            domicilio = (!letraHasta.equals("")) ? domicilio+" "+letraHasta:domicilio;
            domicilio = (!bloque.equals("")) ? domicilio+" Bl. "+bloque:domicilio;
            domicilio = (!portal.equals("")) ? domicilio+" Portal "+portal:domicilio;
            domicilio = (!escal.equals("")) ? domicilio+" Esc. "+escal:domicilio;
            domicilio = (!planta.equals("")) ? domicilio+" "+planta+"º ":domicilio;
            domicilio = (!puerta.equals("")) ? domicilio+puerta:domicilio;

            dom.setDomicilio(domicilio);
            Vector<DomicilioSimpleValueObject> doms = new Vector<DomicilioSimpleValueObject>();
            doms.add(dom);
            terVO.setDomicilios(doms);
            resultado.add(terVO);
            bTercerosForm.setVentana("false");
            opcion="buscar";
            /* Asignamos el ValueObject al formulario*/
            bTercerosForm.setListaTerceros(resultado);

        } else if (opcion.equals("grabarTercDomExterno")) {
            m_Log.debug("*******************************************************************************************");
            m_Log.debug("QUEREMOS GRABAR TODA LA INFORMACION DE TERCERO DIRECTAMENTE");
            m_Log.debug("POSTERC "+request.getParameter("posTerc"));
            m_Log.debug("POSDOM "+request.getParameter("posDom"));

            terVO.setIdentificador(request.getParameter("txtIdTercero"));
            terVO.setTipoDocumento(request.getParameter("codTipoDoc"));
            terVO.setTipoDocDesc(request.getParameter("descTipoDoc"));
            terVO.setDocumento(request.getParameter("txtDNI"));
            terVO.setNombre(request.getParameter("txtInteresado"));
            terVO.setApellido1(request.getParameter("txtApell1"));
            terVO.setApellido2(request.getParameter("txtApell2"));
            terVO.setPartApellido1(request.getParameter("txtPart"));
            terVO.setPartApellido2(request.getParameter("txtPart2"));
            terVO.setNombreCompleto(concatenaNombreTercero(terVO));
            terVO.setNormalizado("1");
            terVO.setTelefono(request.getParameter("txtTelefono"));
            terVO.setEmail(request.getParameter("txtCorreo"));
            terVO.setSituacion('A');
            terVO.setUsuarioAlta(String.valueOf(usuario.getIdUsuario()));
            terVO.setModuloAlta(String.valueOf(usuario.getAppCod()));
            terVO.setCodTerceroOrigen(request.getParameter("codTerceroOrigen"));                     
            
            if (terVO.getIdentificador().equals("0")) {
                int idTercero;
                CondicionesBusquedaTerceroVO conds = new CondicionesBusquedaTerceroVO();
                conds.setTipoDocumento(Integer.parseInt(terVO.getTipoDocumento()));
                if (terVO.getTipoDocumento().equals("0")) {
                    conds.setNombre(terVO.getNombre());
                    conds.setApellido1(terVO.getApellido1());
                    conds.setApellido2(terVO.getApellido2());                    
                } else {
                    conds.setNombre(terVO.getNombre());
                    conds.setApellido1(terVO.getApellido1());
                    conds.setApellido2(terVO.getApellido2());
                    conds.setDocumento(terVO.getDocumento());
                }
                Vector tercerosReps = terMan.getTerceroInterno(conds, params);
                
                String documento=null;
                if (!terVO.getTipoDocumento().equals("0")) {
                    documento=terVO.getDocumento(); 
                } 
            }

            Vector<DomicilioSimpleValueObject> dom = new Vector<DomicilioSimpleValueObject>();
            String txtNormalizado;
            String codUso;
            String idVia;
            String codTVia;
            String txtCodVia;
            String txtNumDesde;
            String txtNumHasta;
            String domActual;

            /**
             * AL AÑADIR LA POSIBILIDAD DE OBTENER VIAS DEL OTRAS BASES DE DATOS DEBEMOS COMPROBAR SI EXISTE EN AGORA
             * SI NO EXISTE INSERTARLA.
             */
            GeneralValueObject via = new GeneralValueObject();
            via.setAtributo("codPais", request.getParameter("codPais"));
            via.setAtributo("codProvincia", request.getParameter("codProvincia"));
            via.setAtributo("codMunicipio", request.getParameter("codMunicipio"));
            via.setAtributo("descVia", request.getParameter("descVia"));
            via.setAtributo("codTipoVia", request.getParameter("codTVia"));

            // Si se ha encontrado la via de un servicio de busqueda externo, no se esta grabando el nombre
            // corto de la vía. Si el nombre largo es mayor que 25 (valor de tamaño de la BBDD para la columna nombre corto) daría un fallo. Se hace substring
            String nombreCortoVia = request.getParameter("descVia");          
            if (nombreCortoVia.length()<=25)     via.setAtributo("nombreCorto", nombreCortoVia);
            else via.setAtributo("nombreCorto", nombreCortoVia.substring(0, 24));

            m_Log.debug("******************************* codTipoVia "+request.getParameter("codTVia"));
            m_Log.debug("******************************* descTVia "+request.getParameter("descTVia"));
            m_Log.debug("******************************* descVia "+request.getParameter("descVia"));
            via.setAtributo("usuario", String.valueOf(usuario.getIdUsuario()));
            int id = -1;
            String emplazamiento="";
            boolean sinDireccion=false;
			boolean altaVia = false;
            if (!"".equals(request.getParameter("descVia")) && request.getParameter("descVia") != null) {
                id = viaManager.altaViaNoRepetido(via, params);
				altaVia = true;
                if (m_Log.isDebugEnabled()) m_Log.debug("La via tienen el codigo " + id);
                emplazamiento=request.getParameter("txtDomicilio");
            }else {
                 
                if (!"".equals(request.getParameter("txtDomicilio")) && request.getParameter("txtDomicilio") != null) {
                    emplazamiento=request.getParameter("txtDomicilio");
                }else{
                    emplazamiento=configCommon.getString(usuario.getOrgCod() + ConstantesDatos.DESCRIPCION_VIA_DESCONOCIDA); 
                    sinDireccion=true;
                }
            }

            txtNormalizado = request.getParameter("txtNormalizado");
            codUso = request.getParameter("codUso");
            codTVia = request.getParameter("codTVia");
            idVia = String.valueOf(id);
            txtCodVia = request.getParameter("txtCodVia");
            txtNumDesde = request.getParameter("txtNumDesde");
            txtNumHasta = request.getParameter("txtNumHasta");
            domActual = request.getParameter("domActual");
            DomicilioSimpleValueObject domicilio = new DomicilioSimpleValueObject();
            domicilio.setRevNormalizar("1");
            domicilio.setNormalizado(txtNormalizado);
            domicilio.setCodTipoUso(codUso);
            domicilio.setDescTipoUso(request.getParameter("descUso"));
            if(sinDireccion)
            {
                domicilio.setIdTipoVia(null);
                domicilio.setTipoVia(null);
            }
            else{
                domicilio.setIdTipoVia(codTVia);
                domicilio.setTipoVia(request.getParameter("descTVia"));
            }
            
            domicilio.setCodigoVia(txtCodVia);
            domicilio.setIdPais(request.getParameter("codPais"));
            domicilio.setProvincia(request.getParameter("descProvincia"));
            domicilio.setMunicipio(request.getParameter("descMunicipio"));
            domicilio.setIdProvincia(request.getParameter("codProvincia"));
            domicilio.setIdMunicipio(request.getParameter("codMunicipio"));
            domicilio.setCodigoVia(idVia);
            domicilio.setIdPaisVia(request.getParameter("codPais"));
            domicilio.setIdProvinciaVia(request.getParameter("codProvincia"));
            domicilio.setIdMunicipioVia(request.getParameter("codMunicipio"));
            domicilio.setIdVia(idVia);
            domicilio.setDescVia(request.getParameter("descVia"));
            domicilio.setCodigoPostal(request.getParameter("descPostal"));
            domicilio.setBarriada(request.getParameter("txtBarriada"));
            domicilio.setDomicilio(emplazamiento);
            domicilio.setNumDesde(txtNumDesde);
            domicilio.setLetraDesde(request.getParameter("txtLetraDesde"));
            domicilio.setNumHasta(txtNumHasta);
            domicilio.setLetraHasta(request.getParameter("txtLetraHasta"));
            domicilio.setBloque(request.getParameter("txtBloque"));
            domicilio.setPortal(request.getParameter("txtPortal"));
            domicilio.setEscalera(request.getParameter("txtEsc"));
            domicilio.setPlanta(request.getParameter("txtPlta"));
            domicilio.setPuerta(request.getParameter("txtPta"));
            domicilio.setEnPadron("1");
            domicilio.setEsDomPrincipal("true");
            /* anadir ECOESI */
            String codESI = request.getParameter("codESI");
            if (codESI != null) {
                if (!"".equals(codESI)) {
                    domicilio.setCodESI(codESI);
                }
            }
            /* fin anadir ECOESI */
            dom.add(domicilio);

            terVO.setDomicilios(dom);
            terVO.setUsuarioAlta(String.valueOf(usuario.getIdUsuario()));
            int idDom;
            /**
             * COMPROBAMOS SI EL DOMICILIO A INSERTAR ESTA YA EN LA BASE DE DATOS
             */

            int d = terMan.existeDomicilio(terVO, params);
            if (d > 0) {
                if (m_Log.isDebugEnabled()) m_Log.debug("EXISTE EL DOMICIALIO");
                txtNormalizado = "1";
                domicilio.setNormalizado(txtNormalizado);
                terVO.setIdDomicilio(String.valueOf(d));
                
            } else {
                if (m_Log.isDebugEnabled()) m_Log.debug("NO EXISTE EL DOMICILIO");
                domicilio.setNormalizado("2");
               
                //insetamos el CP
                GeneralValueObject gVO = obtenerDatosCodPostal(terVO);
              
            }
           
            
            
			String txtIdDomicilio = request.getParameter("txtIdDomicilio");
            GeneralValueObject gCodPostalVO = obtenerDatosCodPostal(terVO);	


            /**** CAMPOS SUPLEMENTARIOS DEL TERCERO ******/
            
            Vector estructuraDatosSuplementarios = new Vector();
            estructuraDatosSuplementarios = bTercerosForm.getEstructuraCamposSuplementariosTercero();
            Vector valoresDatosSuplementarios = new Vector();
            GeneralValueObject listaFicheros = null;
            listaFicheros = bTercerosForm.getListaFicheros();
            GeneralValueObject listaTiposFicheros = null;
            listaTiposFicheros = bTercerosForm.getListaTiposFicheros();
            Config m_Conf = ConfigServiceHelper.getConfig("common");
            String campo = "E_PLT.CodigoPlantillaFichero";
            String tipoDatoFichero = m_Conf.getString(campo);
            campo = "E_PLT.CodigoCampoDesplegable";
            String tipoDatoDesplegable = m_Conf.getString(campo);

            GeneralValueObject gVO = new GeneralValueObject();

            String camposSuplementarios = request.getParameter("CAMPOS_SUPLEMENTARIOS");            
            HashMap<String,String> valoresCamposSuplementarios = this.tratarCamposSuplementariosAltaTerceroExterno(camposSuplementarios);
            
            for (int i = 0; estructuraDatosSuplementarios!=null && i < estructuraDatosSuplementarios.size(); i++) {
                EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementarios.elementAt(i);
                m_Log.debug("CODIGO ... " + eC.getCodCampo());
                m_Log.debug("NOMBRE ... " + eC.getDescCampo());
                m_Log.debug("TIPO DATO ... " + eC.getCodTipoDato());

                gVO.setAtributo(eC.getCodCampo(),valoresCamposSuplementarios.get(eC.getCodCampo()));                    
                valoresDatosSuplementarios.addElement(gVO);
            }


            /*********************************************************************************************************************/
            /*********************************************************************************************************************/
            /*********************************************************************************************************************/

            SalidaTratamientoTerceroExterno salida = null;
            boolean error = false;
            boolean errorTransaccional = false;
            try{

                m_Log.debug("***** ANTES DE LLAMAR A setTerceroExterno() appCod: " + usuario.getAppCod());
                //SE INSERTA EN BD EL TERCERO
                salida  = terMan.setTerceroExterno(usuario.getOrgCod(),terVO, txtNormalizado, via, altaVia,gCodPostalVO,txtIdDomicilio,usuario.getAppCod(),estructuraDatosSuplementarios,valoresDatosSuplementarios,params);

                terVO.setIdentificador(Integer.toString(salida.getIdTercero()));
                terVO.setVersion(salida.getVerTercero());

            }catch(TechnicalException e){
                // HA OCURRIDO UN ERROR TÉCNICO DURANTE EL ALTA DEL TERCERO EN FLEXIA
                m_Log.error("ERROR TÉCNICO AL DAR DE ALTA EL TERCERO EN FLEXIA: " + e.getMessage());
                error = true;
            } catch(ErrorTransaccionalTerceroExternoException e){
                m_Log.error("NO SE HA PODIDO DAR DE ALTA EL TERCERO EN ALGUNO O TODOS LOS SISTEMAS EXTERNOS DE TERCEROS: " + e.getMessage());
                error = true;
                errorTransaccional = true;
            }

        resultado.add(terVO);
        /* Asignamos el ValueObject al formulario*/
        bTercerosForm.setListaTerceros(resultado);
        opcion = "seleccionar";

    // --------------------------
    // GRABAR ALTA de un tercero y un domicilio (nueva interfaz de terceros)
    // --------------------------
    }
        else if (opcion.equals("grabarAltaTercero")) {

            terVO = recogerParametrosTercero(request);
            terVO.setUsuarioAlta(String.valueOf(usuario.getIdUsuario()));
            terVO.setModuloAlta(String.valueOf(usuario.getAppCod()));
            terVO.setDomicilios(new Vector());
            terVO.setOrigen("SGE");
            terVO.setCodOrganizacion(Integer.toString(usuario.getOrgCod()));
            terVO.setCodIdioma(Integer.toString(usuario.getIdioma()));

            int idTercero = 0;
            String grabarDirecto = request.getParameter("grabarDirecto");
            boolean existeTercero = terMan.existeTercero(terVO,params);
            // Opcion grabarDirecto = hay confirmacion del usuario
            if ((!existeTercero) || grabarDirecto.equals("si")) {
                // SE DA DE ALTA EL TERCERO JUNTO CON EL DOMICILIO, PERO PREVIAMENTE SE RECUPERA LA INFORMACIÓN NECESARIA
                // PARA PASAR AL MÉTODO DE LA FACHADA
                
                GeneralValueObject via = new GeneralValueObject();
                String rCodPais        = request.getParameter("codPais");
                String rCodProvincia = request.getParameter("codProvincia");
                String rCodMunicipio = request.getParameter("codMunicipio");
                String rDescVia        = request.getParameter("descVia");
                String rCodTipoVia    = request.getParameter("codTVia");

                m_Log.debug(" codPais: " + rCodPais);
                m_Log.debug(" codProvincia: " + rCodProvincia);
                m_Log.debug(" codMunicipio: " + rCodMunicipio);
                m_Log.debug(" descVia: " + rDescVia);
                m_Log.debug(" codTipoVia: " + rCodTipoVia);

                if("".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO) || "no".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO)){
                    // Se cargan los datos de la vía que llegan del formulario
                    via.setAtributo("codPais",request.getParameter("codPais"));
                    via.setAtributo("codProvincia",request.getParameter("codProvincia"));
                    via.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
                    via.setAtributo("descVia",request.getParameter("descVia"));
                    via.setAtributo("codTipoVia",request.getParameter("codTVia"));
                }
                else
                if("si".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO)){

                    if("".equals(rCodProvincia) && "".equals(rCodMunicipio) && "".equals(rCodTipoVia) && "".equals(rDescVia)){
                        // Se cargan los datos de la vía por defecto con dirección desconocida
                        via.setAtributo("codPais",request.getParameter("codPais"));
                        via.setAtributo("codProvincia",configCommon.getString(usuario.getOrgCod() + ConstantesDatos.CODIGO_PROVINCIA_DESCONOCIDO));
                        via.setAtributo("codMunicipio",configCommon.getString(usuario.getOrgCod() + ConstantesDatos.CODIGO_MUNICIPIO_DESCONOCIDO));
                        via.setAtributo("descVia",configCommon.getString(usuario.getOrgCod() + ConstantesDatos.DESCRIPCION_VIA_DESCONOCIDA));
                        via.setAtributo("codTipoVia",configCommon.getString(usuario.getOrgCod() + ConstantesDatos.CODIGO_VIA_DESCONOCIDO));

                    }else{
                        // Se cargan los datos de la vía que llegan del formulario
                        via.setAtributo("codPais",request.getParameter("codPais"));
                        via.setAtributo("codProvincia",request.getParameter("codProvincia"));
                        via.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
                        via.setAtributo("descVia",request.getParameter("descVia"));
                        via.setAtributo("codTipoVia",request.getParameter("codTVia"));
                    }
                }

                // Si se ha encontrado la via de un servicio de busqueda externo, no se esta grabando el nombre
                // corto de la vía.  Si el nombre largo es mayor que 25 (valor de tamaño de la BBDD para la columna nombre corto) daría un fallo. Se hace substring
                String nombreCortoVia = request.getParameter("descVia");
                if (nombreCortoVia.length()<=25)     via.setAtributo("nombreCorto", nombreCortoVia);
                else via.setAtributo("nombreCorto", nombreCortoVia.substring(0, 24));
                via.setAtributo("usuario",String.valueOf(usuario.getIdUsuario()));                
                boolean altaVia = false;
                if (!"".equals(request.getParameter("descVia")) && request.getParameter("descVia")!=null) {
                    altaVia = true;
                }
                                
                String txtNormalizado = request.getParameter("txtNormalizado");

                // Datos del domicilio
                DomicilioSimpleValueObject domicilio = recogerParametrosDomicilioVO(request);
                terVO.setUsuarioAlta(String.valueOf(usuario.getIdUsuario()));
                Vector<DomicilioSimpleValueObject> doms = new Vector<DomicilioSimpleValueObject>();
                doms.add(domicilio);
                terVO.setDomicilios(doms);

                String txtIdDomicilio = request.getParameter("txtIdDomicilio");
                GeneralValueObject gCodPostalVO = obtenerDatosCodPostal(terVO);

                /*********************************************************************************************************************/
                /*************************** RECUPERAR CAMPOS SUPLEMENTARIOS DE TERCERO Y SUS VALORES ********************************/
                /*********************************************************************************************************************/

                Vector estructuraDatosSuplementarios = new Vector();
                estructuraDatosSuplementarios = bTercerosForm.getEstructuraCamposSuplementariosTercero();
                Vector valoresDatosSuplementarios = new Vector();
                GeneralValueObject listaFicheros = null;
                listaFicheros = bTercerosForm.getListaFicheros();
                GeneralValueObject listaTiposFicheros = null;
                listaTiposFicheros = bTercerosForm.getListaTiposFicheros();
                Config m_Conf = ConfigServiceHelper.getConfig("common");
                String campo = "E_PLT.CodigoPlantillaFichero";
                String tipoDatoFichero = m_Conf.getString(campo);
                campo = "E_PLT.CodigoCampoDesplegable";
                String tipoDatoDesplegable = m_Conf.getString(campo);

                GeneralValueObject gVO = new GeneralValueObject();

                for (int i = 0; estructuraDatosSuplementarios!=null && i < estructuraDatosSuplementarios.size(); i++) {
                    EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementarios.elementAt(i);
                    m_Log.debug("CODIGO ... " + eC.getCodCampo());
                    m_Log.debug("NOMBRE ... " + eC.getDescCampo());
                    m_Log.debug("TIPO DATO ... " + eC.getCodTipoDato());

                    
                    if (eC.getCodTipoDato().equals(tipoDatoFichero)) { // 5 = FICHERO
                        m_Log.debug("Procesado FICHERO ... ");
                        if (listaFicheros.getAtributo(eC.getCodCampo()) != "") {
                            m_Log.debug("FICHERO ........................ NO VACIO ");
                            byte[] fichero = (byte[]) listaFicheros.getAtributo(eC.getCodCampo());
                            String tipoFichero = (String) listaTiposFicheros.getAtributo(eC.getCodCampo());
                            gVO.setAtributo(eC.getCodCampo(), fichero);
                            if (fichero != null) {
                                String aux = request.getParameter(eC.getCodCampo());
                                String[] matriz = aux.split("/");
                                aux = matriz[matriz.length - 1];
                                gVO.setAtributo(eC.getCodCampo() + "_NOMBRE", aux);
                                gVO.setAtributo(eC.getCodCampo() + "_TIPO", tipoFichero);
                            }
                        } else {
                            m_Log.debug("FICHERO ........................ VACIO ");
                            gVO.setAtributo(eC.getCodCampo(), null);
                            gVO.setAtributo(eC.getCodCampo() + "_NOMBRE", "");
                            gVO.setAtributo(eC.getCodCampo() + "_TIPO", "");
                        }
                        m_Log.debug("lista FICHEROs ... " + listaFicheros);
                    } else
                     
                     if (eC.getCodTipoDato().equals(tipoDatoDesplegable)) {
                        m_Log.debug("Procesado DESPLEGABLE ... ");
                        gVO.setAtributo(eC.getCodCampo(), request.getParameter("cod" + eC.getCodCampo()));
                        m_Log.debug("DESPLEGABLE ........................ " + request.getParameter("cod" + eC.getCodCampo()));
                    } else {
                        gVO.setAtributo(eC.getCodCampo(), request.getParameter(eC.getCodCampo()));
                    }
                    valoresDatosSuplementarios.addElement(gVO);
                }
        

                /*********************************************************************************************************************/
                /*********************************************************************************************************************/
                /*********************************************************************************************************************/

                SalidaTratamientoTerceroExterno salida = null;
                boolean error = false;
                boolean errorTransaccional = false;
                try{

                    m_Log.debug("***** ANTES DE LLAMAR A setTerceroExterno() appCod: " + usuario.getAppCod());
                    salida  = terMan.setTerceroExterno(usuario.getOrgCod(),terVO, txtNormalizado, via, altaVia,gCodPostalVO,txtIdDomicilio,usuario.getAppCod(),estructuraDatosSuplementarios,valoresDatosSuplementarios,params);

                    terVO.setIdentificador(Integer.toString(salida.getIdTercero()));
                    terVO.setVersion("1");
                    if(m_Log.isDebugEnabled()) m_Log.debug("El valor del Id del usuario es : " + idTercero);

                }catch(TechnicalException e){
                    // HA OCURRIDO UN ERROR TÉCNICO DURANTE EL ALTA DEL TERCERO EN FLEXIA
                    m_Log.error("ERROR TÉCNICO AL DAR DE ALTA EL TERCERO EN FLEXIA: " + e.getMessage());
                    error = true;
                } catch(ErrorTransaccionalTerceroExternoException e){
                    m_Log.error("NO SE HA PODIDO DAR DE ALTA EL TERCERO EN ALGUNO O TODOS LOS SISTEMAS EXTERNOS DE TERCEROS: " + e.getMessage());
                    error = true;
                    errorTransaccional = true;
                }

                if(!error){
                    // Si no se ha lanzado nin una TechnicalException ni un ErrorTransaccionalTerceroExternoException,
                    // se comprueba si los sistemas externos han lanzado algún error.
                    
                    if(salida!=null && salida.getErroresEjecucionServicio()!=null && salida.getErroresEjecucionServicio().size()>0){
                        m_Log.debug("Hay errores en la ejecución de los sistemas externos");
                        bTercerosForm.setErroresSistemaExterno(salida.getErroresEjecucionServicio());
                        if(AltaTerceroExternoFactoria.altaTerceroExternoRequiereTransaccionalidad(String.valueOf(usuario.getOrgCod()))){
                            // Si la operación requiere de transaccionalidad, al haber errores no se ha dado de alta el tercero
                            opcion = "errorAltaTerceroTransaccionErroresSistemasExternos";
                        }else
                            // La operación no requiere de transaccionalidad y hay errores en el tratamiento de los sistemas externos de terceros.
                            // Se informa de que el tercero ha sido dado de alta en Flexia y se muestran los errores producidos
                            opcion = "altaTerceroFlexiaOKErrorSistemasExternos";
                    }
                    /**** TODO: VER QUE OCURREN EN EL CASO DEL ELSE ******/
                } else{ // Error técnico durante el alta del tercero en BBDD de Flexia
                    if(errorTransaccional)
                        opcion = "errorTransaccionAltaTercero";
                    else
                        opcion = "errorTecnicoAltaTercero";
                }



                terVO.setValoresCamposSuplementarios(DatosSuplementariosTerceroManager.getInstance().cargaValoresDatosSuplementarios(Integer.toString(usuario.getOrgCod()), terVO.getIdentificador(), estructuraDatosSuplementarios, params));

                m_Log.debug("Documento::: " +  terVO.getDocumento());


                resultado.add(terVO);
                // Asignamos el ValueObject al formulario
                bTercerosForm.setListaTerceros(resultado);
                
                m_Log.debug("================= opcion: " + opcion);
                m_Log.debug("<================ BusquedaTercerosAction =======================");

                return (mapping.findForward(opcion));

            }else if (!(terVO.getTipoDocumento().equals("0"))) {
                opcion="terceroYaExiste";
            } else {
                opcion="existeTerceroSinDoc";
            }

        // ----------------------
        // DAR DE BAJA UN TERCERO
        // ----------------------

        } else if (opcion.equals("borrarTercero")) {
            terVO.setIdentificador(request.getParameter("txtIdTercero"));
            terVO.setSituacion('B');
            terVO.setUsuarioBaja(String.valueOf(usuario.getIdUsuario()));
            terMan.cambiaSituacionTercero(terVO,params);
            Vector lTerceros = bTercerosForm.getListaTerceros();
            lTerceros = borraDeListaTerceros(terVO, lTerceros);
            bTercerosForm.setListaTerceros(lTerceros);
        
        // ----------------------
        // GRABAR MODIFICACION de un tercero y un domicilio
        // ----------------------
        }
        else if (opcion.equals("grabarModificacion")) {

            // PARTE DEL TERCERO
            terVO = recogerParametrosTercero(request);
            terVO.setVersion(request.getParameter("txtVersion"));
            terVO.setUsuarioAlta(String.valueOf(usuario.getIdUsuario()));
            terVO.setModuloAlta(String.valueOf(usuario.getAppCod()));
            terVO.setDomicilios(new Vector());
            terVO.setDomPrincipal(request.getParameter("nuevoDomPrincipal"));
            terVO.setOrigen("SGE");

            
            
            m_Log.debug("=========> ID DOMICILIO A MODIFICAR: " + request.getParameter("nuevoDomPrincipal"));
            
            int id = terMan.updateTercero(terVO,params);
            

            GeneralValueObject via = new GeneralValueObject();
            String rCodPais        = request.getParameter("codPais");
            String rCodProvincia = request.getParameter("codProvincia");
            String rCodMunicipio = request.getParameter("codMunicipio");
            String rDescVia        = request.getParameter("descVia");
            String rCodTipoVia    = request.getParameter("codTVia");

            m_Log.debug(" codPais: " + rCodPais);
            m_Log.debug(" codProvincia: " + rCodProvincia);
            m_Log.debug(" codMunicipio: " + rCodMunicipio);
            m_Log.debug(" descVia: " + rDescVia);
            m_Log.debug(" codTipoVia: " + rCodTipoVia);

            if("".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO) || "no".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO)){
                // Se cargan los datos de la vía que llegan del formulario
                via.setAtributo("codPais",request.getParameter("codPais"));
                via.setAtributo("codProvincia",request.getParameter("codProvincia"));
                via.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
                via.setAtributo("descVia",request.getParameter("descVia"));
                via.setAtributo("codTipoVia",request.getParameter("codTVia"));
            }

            if("si".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO)){

                if("".equals(rCodProvincia) && "".equals(rCodMunicipio) && "".equals(rCodTipoVia) && "".equals(rDescVia)){
                    // Se cargan los datos de la vía por defecto con dirección desconocida
                    via.setAtributo("codPais",request.getParameter("codPais"));
                    via.setAtributo("codProvincia",configCommon.getString(usuario.getOrgCod() + ConstantesDatos.CODIGO_PROVINCIA_DESCONOCIDO));
                    via.setAtributo("codMunicipio",configCommon.getString(usuario.getOrgCod() + ConstantesDatos.CODIGO_MUNICIPIO_DESCONOCIDO));
                    via.setAtributo("descVia",configCommon.getString(usuario.getOrgCod() + ConstantesDatos.DESCRIPCION_VIA_DESCONOCIDA));
                    via.setAtributo("codTipoVia",configCommon.getString(usuario.getOrgCod() + ConstantesDatos.CODIGO_VIA_DESCONOCIDO));

                }else{
                    // Se cargan los datos de la vía que llegan del formulario
                    via.setAtributo("codPais",request.getParameter("codPais"));
                    via.setAtributo("codProvincia",request.getParameter("codProvincia"));
                    via.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
                    via.setAtributo("descVia",request.getParameter("descVia"));
                    via.setAtributo("codTipoVia",request.getParameter("codTVia"));
                }
            }
            via.setAtributo("usuario",String.valueOf(usuario.getIdUsuario()));
           
            // Se intenta añadir vía en caso de que ya exista sólo se consigue el idVia.
            id = -1;
            if (!"".equals(request.getParameter("descVia"))&&request.getParameter("descVia")!=null){
                id = viaManager.altaViaNoRepetido(via,params);
                if(m_Log.isDebugEnabled()) m_Log.debug("La via tienen el codigo "+ id);
            }
            String idVia = String.valueOf(id);

            DomicilioSimpleValueObject domicilio = recogerParametrosDomicilioVO(request);
            if (!domicilio.getCodigoVia().equals("") && domicilio.isCambiarDomicilioVia()){
                domicilio.setIdPaisVia(request.getParameter("codPais"));
                domicilio.setIdProvinciaVia(request.getParameter("codProvincia"));
                domicilio.setIdMunicipioVia(request.getParameter("codMunicipio"));                
            }

            if(!"-1".equals(idVia)) domicilio.setIdVia(idVia);            
            domicilio.setModificado("NO");
            
            Vector<DomicilioSimpleValueObject> dom =
                    new Vector<DomicilioSimpleValueObject>();
            dom.add(domicilio);
            terVO.setDomicilios(dom);            
            terVO.setUsuarioAlta(String.valueOf(usuario.getIdUsuario()));

            GeneralValueObject gVO = obtenerDatosCodPostal(terVO);
            
            if(domicilio.getCodigoPostal()!=null && !"".equals(domicilio.getCodigoPostal()) && !codPostalesManager.existeCodPostal(gVO, params)){                
                 codPostalesManager.altaCodPostal(gVO, params);
            }

            
            /*** ORIGINAL 
            terMan.updateDomicilioTercero(terVO,params);
            ***/
            
            String CODIGO_DOMICILIO_ORIGINAL_MODIFICADO = domicilio.getIdDomicilio();            
            boolean DOMICILIO_ASIGNADO_MAS_UN_TERCERO = terMan.estaAsignadoDomicilioVariosTerceros(domicilio.getIdDomicilio(), params);
            m_Log.debug("DOMICILIO_ASIGNADO_MAS_UN_TERCERO: " + DOMICILIO_ASIGNADO_MAS_UN_TERCERO);                        
            
            // #228743: comprobar propiedad que indica que hacer con el domicilio si existe en algún registro o expediente
            boolean DOMICILIO_ASIGNADO_REGEXP = terMan.existeDomEnRegistroExpediente(domicilio.getIdDomicilio(), params);
            String modDom = request.getParameter("actualizarDom");
            if(modDom==null)
                modDom = m_ConfigTerceros.getString(usuario.getOrgCod() + "/ActualizarDomIntRegistroExpediente");
            
            /* ORIGINAL
            if(!DOMICILIO_ASIGNADO_MAS_UN_TERCERO){                          
                // Si el domicilio no está asignado a más de un tercero, se puede asignar sin más
                terMan.updateDomicilioTercero(terVO,params);
            } else {
                terVO.setUsuarioBaja(Integer.toString(usuario.getIdUsuario()));
                int nuevoCodigoDomicilio=terMan.modificarDomicilioCreandoNuevo(terVO,CODIGO_DOMICILIO_ORIGINAL_MODIFICADO,params);                
                if((nuevoCodigoDomicilio!=-1)){
                    domicilio.setIdDomicilio(Integer.toString(nuevoCodigoDomicilio));
                }
               
            }   
            */
            if(DOMICILIO_ASIGNADO_MAS_UN_TERCERO || (DOMICILIO_ASIGNADO_REGEXP && modDom.equalsIgnoreCase("no"))){
                // Si el domicilio está asignado a más de un tercero o  a un registro o expediente con la propiedad que indica si actualizar a no, se procede:
                // baja del domicilio del tercero, alta de nuevo domicilio, y se mantiene el anterior para otros (terceros, registros o expedientes)
                terVO.setUsuarioBaja(Integer.toString(usuario.getIdUsuario()));
                int nuevoCodigoDomicilio=terMan.modificarDomicilioCreandoNuevo(terVO,CODIGO_DOMICILIO_ORIGINAL_MODIFICADO,params);                
                if((nuevoCodigoDomicilio!=-1)){
                    domicilio.setIdDomicilio(Integer.toString(nuevoCodigoDomicilio));
                }
            } else {
                // Si el domicilio no está asignado a más de un tercero, registro o expediente, independientemente de la propiedad, se actualiza sin más
                terMan.updateDomicilioTercero(terVO,params);
            } 
            /*******************************************/
            
             
            
            //*********************************************************************************************************************
            //*************************** RECUPERAR CAMPOS SUPLEMENTARIOS DE TERCERO Y SUS VALORES ********************************
            //*********************************************************************************************************************

            Vector estructuraDatosSuplementarios = new Vector();
            estructuraDatosSuplementarios = bTercerosForm.getEstructuraCamposSuplementariosTercero();
            Vector valoresDatosSuplementarios = new Vector();
            GeneralValueObject listaFicheros = null;
            listaFicheros = bTercerosForm.getListaFicheros();
            GeneralValueObject listaTiposFicheros = null;
            listaTiposFicheros = bTercerosForm.getListaTiposFicheros();
            Config m_Conf = ConfigServiceHelper.getConfig("common");
            String campo = "E_PLT.CodigoPlantillaFichero";
            String tipoDatoFichero = m_Conf.getString(campo);
            campo = "E_PLT.CodigoCampoDesplegable";
            String tipoDatoDesplegable = m_Conf.getString(campo);

            GeneralValueObject valorCampo = new GeneralValueObject();

            for (int i = 0; estructuraDatosSuplementarios!=null && i < estructuraDatosSuplementarios.size(); i++) {
                EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementarios.elementAt(i);
                m_Log.debug("CODIGO ... " + eC.getCodCampo());
                m_Log.debug("NOMBRE ... " + eC.getDescCampo());
                m_Log.debug("TIPO DATO ... " + eC.getCodTipoDato());


                if (eC.getCodTipoDato().equals(tipoDatoFichero)) { // 5 = FICHERO
                    m_Log.debug("Procesado FICHERO ... ");
                    if (listaFicheros.getAtributo(eC.getCodCampo()) != "") {
                        m_Log.debug("FICHERO ........................ NO VACIO ");
                        byte[] fichero = null;
                        String tipoFichero = null;
                        String aux = null;

                        // ORIGINAL
                        fichero = (byte[]) listaFicheros.getAtributo(eC.getCodCampo());
                        tipoFichero = (String) listaTiposFicheros.getAtributo(eC.getCodCampo());
                        if(fichero!=null && !"".equals(fichero) && tipoFichero!=null && !"".equals(tipoFichero)){
                            // Si llega un fichero de formulario
                            aux = request.getParameter(eC.getCodCampo());
                            String[] matriz = aux.split("/");
                            aux = matriz[matriz.length - 1];

                        }else{
                            // Sino llega un fichero de formulario, a lo mejor es que ya tenía un fichero anteriormente, se recupera
                            Hashtable<String,Object> campoFichero = DatosSuplementariosTerceroManager.getInstance().getContenidoCampoFichero(Integer.toString(usuario.getOrgCod()),eC.getCodCampo(), terVO.getIdentificador(), params);
                            tipoFichero = (String)campoFichero.get("TIPO_MIME");
                            fichero = (byte[])campoFichero.get("CONTENIDO");
                            aux = (String)campoFichero.get("NOMBRE_FICHERO");
                        }

                        // Se recupera el tipo de fichero y su nombre directamente de base de datos si tuviese valor
                        valorCampo.setAtributo(eC.getCodCampo() + "_NOMBRE", aux);
                        valorCampo.setAtributo(eC.getCodCampo() + "_TIPO", tipoFichero);
                        valorCampo.setAtributo(eC.getCodCampo(), fichero);

                    } else {
                        m_Log.debug("FICHERO ........................ VACIO ");
                        valorCampo.setAtributo(eC.getCodCampo(), null);
                        valorCampo.setAtributo(eC.getCodCampo() + "_NOMBRE", "");
                        valorCampo.setAtributo(eC.getCodCampo() + "_TIPO", "");
                    }
                    m_Log.debug("lista FICHEROs ... " + listaFicheros);
                } else

                 if (eC.getCodTipoDato().equals(tipoDatoDesplegable)) {
                    m_Log.debug("Procesado DESPLEGABLE ... ");
                    valorCampo.setAtributo(eC.getCodCampo(), request.getParameter("cod" + eC.getCodCampo()));
                    m_Log.debug("DESPLEGABLE ........................ " + request.getParameter("cod" + eC.getCodCampo()));
                } else {
                    valorCampo.setAtributo(eC.getCodCampo(), request.getParameter(eC.getCodCampo()));
                }
                valoresDatosSuplementarios.addElement(valorCampo);
            }
            
            try{
                DatosSuplementariosTerceroManager.getInstance().grabarDatosSuplementarios(Integer.toString(usuario.getOrgCod()),terVO.getIdentificador(), estructuraDatosSuplementarios, valoresDatosSuplementarios, params);
                bTercerosForm.setEstructuraCamposSuplementariosTercero(estructuraDatosSuplementarios);
                terVO.setValoresCamposSuplementarios(DatosSuplementariosTerceroManager.getInstance().cargaValoresDatosSuplementarios(String.valueOf(usuario.getOrgCod()), terVO.getIdentificador(),estructuraDatosSuplementarios, params));
            }catch(Exception e){
                
                e.printStackTrace();
            }

            /*******/
            if ("SI".equals(domicilio.getModificado())) bTercerosForm.setResOp("domicilioModificado");
            else bTercerosForm.setResOp("domicilioNoModificado");

            resultado.add(terVO);
            bTercerosForm.setListaTercerosModificados(resultado);
        }    
        else if (opcion.equals("getTerceroUOR")) {
            // Se busca la UOR y se devuelve en un TerceroVO con identificador=0
            // para que sea tratada como un tercero externo
            CondicionesBusquedaTerceroVO condsVO = new CondicionesBusquedaTerceroVO();
            condsVO.setTipoDocumento(Integer.parseInt(request.getParameter("tipoDocUOR")));
            condsVO.setDocumento(request.getParameter("docUOR"));
            condsVO.setNombre(request.getParameter("nombreUOR"));
            Vector <TercerosValueObject> uor = terMan.getTerceroUOR(condsVO, params);
            bTercerosForm.setListaTerceros(uor);
            opcion = "buscar";            
        }else if("ocultoAltaTerceroSinDomicilio".equals(opcion)){
            opcion = "ocultoAltaTerceroSinDomicilio";
        }
       /** Óscar **/
        /* Redirigimos al JSP de salida*/
        m_Log.debug("<================ BusquedaTercerosAction =======================");
        return (mapping.findForward(opcion));
    }

    private String concatenaNombreTercero(TercerosValueObject terVO){        
        return FormateadorTercero.getDescTercero(terVO.getNombre(), terVO.getApellido1(), terVO.getApellido2(), false);
    }

    private CondicionesBusquedaTerceroVO recogerParametrosBusqueda(HttpServletRequest request) {
        CondicionesBusquedaTerceroVO condsBusq = new CondicionesBusquedaTerceroVO();

        String strTipoDoc = request.getParameter("codTipoDoc");
        m_Log.debug("BusquedaTercerosAction recogerParametrosBusqueda strTipoDoc = " + strTipoDoc);
         m_Log.debug("BusquedaTercerosAction recogerParametrosBusqueda txtInteresado = " + request.getParameter("txtInteresado"));
        m_Log.debug("BusquedaTercerosAction recogerParametrosBusqueda txtApell1 = " + request.getParameter("txtApell1"));
        m_Log.debug("BusquedaTercerosAction recogerParametrosBusqueda txtApell2 = " + request.getParameter("txtApell2"));
        m_Log.debug("BusquedaTercerosAction recogerParametrosBusqueda txtTelefono = " + request.getParameter("txtTelefono"));
        m_Log.debug("BusquedaTercerosAction recogerParametrosBusqueda txtCorreo = " + request.getParameter("txtCorreo"));
        m_Log.debug("BusquedaTercerosAction recogerParametrosBusqueda codPais = " + request.getParameter("codPais"));
        m_Log.debug("BusquedaTercerosAction recogerParametrosBusqueda codProvincia = " + request.getParameter("codProvincia"));
        m_Log.debug("BusquedaTercerosAction recogerParametrosBusqueda codMunicipio = " + request.getParameter("codMunicipio"));
        m_Log.debug("BusquedaTercerosAction recogerParametrosBusqueda txtCodVia = " + request.getParameter("txtCodVia"));
        m_Log.debug("BusquedaTercerosAction recogerParametrosBusqueda txtNumHasta = " + request.getParameter("txtNumHasta"));
        m_Log.debug("BusquedaTercerosAction recogerParametrosBusqueda txtLetraDesde = " + request.getParameter("txtLetraDesde"));
        m_Log.debug("BusquedaTercerosAction recogerParametrosBusqueda txtLetraHasta = " + request.getParameter("txtLetraHasta"));
        m_Log.debug("BusquedaTercerosAction recogerParametrosBusqueda txtLetraHasta = " + request.getParameter("txtLetraHasta"));
        m_Log.debug("BusquedaTercerosAction recogerParametrosBusqueda txtDomicilio = " + request.getParameter("txtTxtDomicilio"));

        if (strTipoDoc != null && !"".equals(strTipoDoc)) {
            condsBusq.setTipoDocumento(Integer.parseInt(strTipoDoc));
        }
        condsBusq.setDocumento(request.getParameter("txtDNI"));
        m_Log.debug("BusquedaTercerosAction recogerParametrosBusqueda txtDNI = " + request.getParameter("txtDNI"));

        // Si se ha especificado busqueda abierta se trata la cadena de busqueda.
        String busquedaAbierta = m_ConfigTerceros.getString("BusquedaAbierta");
        m_Log.debug("BusquedaTercerosAction recogerParametrosBusqueda busquedaAbierta = " + busquedaAbierta);
        if (busquedaAbierta == null) {
            busquedaAbierta = "no";
        }
        if (busquedaAbierta.equals("si")) {
            condsBusq.setNombre(tratarBusquedaAbierta(request.getParameter("txtInteresado")));
            condsBusq.setApellido1(tratarBusquedaAbierta(request.getParameter("txtApell1")));
            condsBusq.setApellido2(tratarBusquedaAbierta(request.getParameter("txtApell2")));
        } else {
            condsBusq.setNombre(request.getParameter("txtInteresado"));
            condsBusq.setApellido1(request.getParameter("txtApell1"));
            condsBusq.setApellido2(request.getParameter("txtApell2"));
        }

        condsBusq.setTelefono(request.getParameter("txtTelefono"));
        condsBusq.setEmail(request.getParameter("txtCorreo"));
        String strCodPais = request.getParameter("codPais");
        if (strCodPais != null && !"".equals(strCodPais)) {
            condsBusq.setCodigoPais(Integer.parseInt(strCodPais));
        }
        String strCodProvincia = request.getParameter("codProvincia");
        if (strCodProvincia != null && !"".equals(strCodProvincia)) {
            condsBusq.setCodigoProvincia(Integer.parseInt(strCodProvincia));
        }
        String strCodMunicipio = request.getParameter("codMunicipio");
        if (strCodMunicipio != null && !"".equals(strCodMunicipio)) {
            condsBusq.setCodigoMunicipio(Integer.parseInt(strCodMunicipio));
        }
        String strCodVia = request.getParameter("txtCodVia");
        if (strCodVia != null && !"".equals(strCodVia) && !"0".equals(strCodVia)) {
            condsBusq.setCodigoVia(Integer.parseInt(strCodVia));
        }
        condsBusq.setNombreVia(request.getParameter("descVia"));
        String strNumDesde = request.getParameter("txtNumDesde");
        if (strNumDesde != null && !"".equals(strNumDesde)) {
            condsBusq.setNumeroDesde(Integer.parseInt(strNumDesde));
        }
        String strNumHasta = request.getParameter("txtNumHasta");
        if (strNumHasta != null && !"".equals(strNumHasta)) {
            condsBusq.setNumeroHasta(Integer.parseInt(strNumHasta));
        }
        condsBusq.setLetraDesde(request.getParameter("txtLetraDesde"));
        condsBusq.setLetraHasta(request.getParameter("txtLetraHasta"));

        condsBusq.setBloque(request.getParameter("txtBloque"));
        condsBusq.setPortal(request.getParameter("txtPortal"));
        condsBusq.setEscalera(request.getParameter("txtEsc"));
        condsBusq.setPlanta(request.getParameter("txtPlta"));
        condsBusq.setPuerta(request.getParameter("txtPta"));
        condsBusq.setCodPostal(request.getParameter("descPostal"));
        condsBusq.setDomicilio(request.getParameter("txtDomicilio"));
        condsBusq.setLugar(request.getParameter("txtBarriada"));
        String strCodEco = request.getParameter("codECO");
        if (strCodEco != null && !"".equals(strCodEco)) {
            condsBusq.setCodigoEco(Integer.parseInt(strCodEco));
        }
        String strCodEsi = request.getParameter("codESI");
        if (strCodEsi != null && !"".equals(strCodEsi)) {
            condsBusq.setCodigoEsi(Integer.parseInt(strCodEsi));
        }

        return condsBusq;
    }

    private TercerosValueObject recogerParametrosTercero(HttpServletRequest request) {

        TercerosValueObject terVO = new TercerosValueObject();
        terVO.setIdentificador(request.getParameter("txtIdTercero"));
        terVO.setTipoDocumento(request.getParameter("codTipoDoc"));
        terVO.setDocumento(request.getParameter("txtDNI"));
        terVO.setNombre(request.getParameter("txtInteresado"));
        terVO.setApellido1(request.getParameter("txtApell1"));
        terVO.setApellido2(request.getParameter("txtApell2"));
        terVO.setPartApellido1(request.getParameter("txtPart"));
        terVO.setPartApellido2(request.getParameter("txtPart2"));
        terVO.setNombreCompleto(concatenaNombreTercero(terVO));
        terVO.setNormalizado("1");
        terVO.setTelefono(request.getParameter("txtTelefono"));
        terVO.setEmail(request.getParameter("txtCorreo"));
        terVO.setSituacion('A');

        return terVO;
    }

    private DomicilioSimpleValueObject recogerParametrosDomicilioVO(HttpServletRequest request) {

        UsuarioValueObject usuario = (UsuarioValueObject)request.getSession().getAttribute("usuario");
        Config configCommon = ConfigServiceHelper.getConfig("common");        
        String ALTA_TERCERO_SIN_DOMICILIO = configCommon.getString(usuario.getOrgCod() + ConstantesDatos.ALTA_TERCERO_SIN_DOMICILIO);

        DomicilioSimpleValueObject domicilio = new DomicilioSimpleValueObject();
        domicilio.setIdDomicilio(request.getParameter("txtIdDomicilio"));
        domicilio.setRevNormalizar("1");
        domicilio.setNormalizado(request.getParameter("txtNormalizado"));
        domicilio.setCodTipoUso(request.getParameter("codUso"));
        domicilio.setDescTipoUso(request.getParameter("descUso"));
        
        if("".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO)  || "no".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO))
            domicilio.setIdTipoVia(request.getParameter("codTVia"));
        else
        if("si".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO) && "".equals(request.getParameter("codTVia")))
             domicilio.setIdTipoVia(configCommon.getString(usuario.getOrgCod() + ConstantesDatos.CODIGO_TIPO_VIA_DESCONOCIDA));
        else
            domicilio.setIdTipoVia(request.getParameter("codTVia"));

        domicilio.setTipoVia(request.getParameter("descTVia"));

        if("".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO)  || "no".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO))
            domicilio.setCodigoVia(request.getParameter("txtCodVia"));
        else
        if("si".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO) && "".equals(request.getParameter("txtCodVia")) && "".equals(request.getParameter("txtDomicilio"))){
            domicilio.setCodigoVia(configCommon.getString(usuario.getOrgCod() + ConstantesDatos.CODIGO_VIA_DESCONOCIDO));
            domicilio.setIdVia(configCommon.getString(usuario.getOrgCod() + ConstantesDatos.CODIGO_VIA_DESCONOCIDO));
            domicilio.setCambiarCodVia(true);
        }
        else
            domicilio.setCodigoVia(request.getParameter("txtCodVia"));

        domicilio.setIdPais(request.getParameter("codPais"));

        if("".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO)  || "no".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO))
            domicilio.setIdProvincia(request.getParameter("codProvincia"));
        else
        if("si".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO) && "".equals(request.getParameter("codProvincia")))
            domicilio.setIdProvincia(configCommon.getString(usuario.getOrgCod() + ConstantesDatos.CODIGO_PROVINCIA_DESCONOCIDO));
        else
            domicilio.setIdProvincia(request.getParameter("codProvincia"));

        if("".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO)  || "no".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO))
            domicilio.setIdMunicipio(request.getParameter("codMunicipio"));
        else
        if("si".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO) && "".equals(request.getParameter("codMunicipio")))
            domicilio.setIdMunicipio(configCommon.getString(usuario.getOrgCod() + ConstantesDatos.CODIGO_MUNICIPIO_DESCONOCIDO));
        else
            domicilio.setIdMunicipio(request.getParameter("codMunicipio"));


        if("".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO)  || "no".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO))
            domicilio.setProvincia(request.getParameter("descProvincia"));
        else
        if("si".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO) && "".equals(request.getParameter("descProvincia")))
            domicilio.setProvincia(configCommon.getString(usuario.getOrgCod() + ConstantesDatos.DESCRIPCION_PROVINCIA_DESCONOCIDA));
        else
            domicilio.setProvincia(request.getParameter("descProvincia"));


        if("".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO)  || "no".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO))
            domicilio.setMunicipio(request.getParameter("descMunicipio"));
        else
            if("si".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO) && "".equals(request.getParameter("descMunicipio")))
            domicilio.setMunicipio(configCommon.getString(usuario.getOrgCod() + ConstantesDatos.DESCRIPCION_MUNICIPIO_DESCONOCIDO));
        else
            domicilio.setMunicipio(request.getParameter("descMunicipio"));


        if (!domicilio.getCodigoVia().equals("")){
            domicilio.setIdPaisVia(request.getParameter("codPais"));
            
            if("si".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO))
                domicilio.setCambiarDomicilioVia(false);
            
            if("".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO)  || "no".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO))
                domicilio.setIdProvinciaVia(request.getParameter("codProvincia"));
            else
            if("si".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO) && "".equals(request.getParameter("codProvincia")))
                domicilio.setIdProvinciaVia(configCommon.getString(usuario.getOrgCod() + ConstantesDatos.CODIGO_PROVINCIA_DESCONOCIDO));
            else
                domicilio.setIdProvinciaVia(request.getParameter("codProvincia"));


            if("".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO)  || "no".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO))
                domicilio.setIdMunicipioVia(request.getParameter("codMunicipio"));
            else
            if("si".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO) && "".equals(request.getParameter("codMunicipio")))
                domicilio.setIdMunicipioVia(configCommon.getString(usuario.getOrgCod() + ConstantesDatos.CODIGO_MUNICIPIO_DESCONOCIDO));
            else
                domicilio.setIdMunicipioVia(request.getParameter("codMunicipio"));

            if("".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO)  || "no".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO))
                domicilio.setDescVia(request.getParameter("descVia"));
            else
            if("si".equalsIgnoreCase(ALTA_TERCERO_SIN_DOMICILIO) && "".equals(request.getParameter("descVia")) && "".equals(request.getParameter("txtDomicilio")))
                domicilio.setDescVia(configCommon.getString(usuario.getOrgCod() + ConstantesDatos.DESCRIPCION_VIA_DESCONOCIDA));
            else
                domicilio.setDescVia(request.getParameter("descVia"));
        }
        
        domicilio.setCodigoPostal(request.getParameter("descPostal"));
        domicilio.setBarriada(request.getParameter("txtBarriada"));
        domicilio.setDomicilio(request.getParameter("txtDomicilio"));
        domicilio.setNumDesde(request.getParameter("txtNumDesde"));
        domicilio.setLetraDesde(request.getParameter("txtLetraDesde"));
        domicilio.setNumHasta(request.getParameter("txtNumHasta"));
        domicilio.setLetraHasta(request.getParameter("txtLetraHasta"));
        domicilio.setBloque(request.getParameter("txtBloque"));
        domicilio.setPortal(request.getParameter("txtPortal"));
        domicilio.setEscalera(request.getParameter("txtEsc"));
        domicilio.setPlanta(request.getParameter("txtPlta"));
        domicilio.setPuerta(request.getParameter("txtPta"));
        domicilio.setEnPadron("1");
        domicilio.setCodECO(request.getParameter("codECO"));
        String codESI=request.getParameter("codESI");
        if (codESI!= null && !"".equals(codESI)){
            domicilio.setCodESI(codESI);
        }
        // Indica si este domicilio debe guardarse como principal del tercero
        domicilio.setEsDomPrincipal(request.getParameter("esDomPrincipal"));
        return domicilio;
    }
    
    
     /*
      private DomicilioSimpleValueObject recogerParametrosDomicilioVO(HttpServletRequest request) {

            DomicilioSimpleValueObject domicilio = new DomicilioSimpleValueObject();
            domicilio.setIdDomicilio(request.getParameter("txtIdDomicilio"));
            domicilio.setRevNormalizar("1");
            domicilio.setNormalizado(request.getParameter("txtNormalizado"));
            domicilio.setCodTipoUso(request.getParameter("codUso"));
            domicilio.setDescTipoUso(request.getParameter("descUso"));
            domicilio.setIdTipoVia(request.getParameter("codTVia"));
            domicilio.setTipoVia(request.getParameter("descTVia"));
            domicilio.setCodigoVia(request.getParameter("txtCodVia"));
            domicilio.setIdPais(request.getParameter("codPais"));
            domicilio.setIdProvincia(request.getParameter("codProvincia"));
            domicilio.setIdMunicipio(request.getParameter("codMunicipio"));
            domicilio.setProvincia(request.getParameter("descProvincia"));
            domicilio.setMunicipio(request.getParameter("descMunicipio"));
            if (!domicilio.getCodigoVia().equals("")){
                domicilio.setIdPaisVia(request.getParameter("codPais"));
                domicilio.setIdProvinciaVia(request.getParameter("codProvincia"));
                domicilio.setIdMunicipioVia(request.getParameter("codMunicipio"));
                domicilio.setDescVia(request.getParameter("descVia"));
            }
            domicilio.setCodigoPostal(request.getParameter("descPostal"));
            domicilio.setBarriada(request.getParameter("txtBarriada"));
            domicilio.setDomicilio(request.getParameter("txtDomicilio"));
            domicilio.setNumDesde(request.getParameter("txtNumDesde"));
            domicilio.setLetraDesde(request.getParameter("txtLetraDesde"));
            domicilio.setNumHasta(request.getParameter("txtNumHasta"));
            domicilio.setLetraHasta(request.getParameter("txtLetraHasta"));
            domicilio.setBloque(request.getParameter("txtBloque"));
            domicilio.setPortal(request.getParameter("txtPortal"));
            domicilio.setEscalera(request.getParameter("txtEsc"));
            domicilio.setPlanta(request.getParameter("txtPlta"));
            domicilio.setPuerta(request.getParameter("txtPta"));
            domicilio.setEnPadron("1");
            domicilio.setCodECO(request.getParameter("codECO"));
            String codESI=request.getParameter("codESI");
            if (codESI!= null && !"".equals(codESI)){
                domicilio.setCodESI(codESI);
            }
            // Indica si este domicilio debe guardarse como principal del tercero
            domicilio.setEsDomPrincipal(request.getParameter("esDomPrincipal"));
            return domicilio;
    } */



    private GeneralValueObject recogerParametrosDomicilio(HttpServletRequest request)
    {
        GeneralValueObject domicilioVO = new GeneralValueObject();
        domicilioVO.setAtributo("documento",request.getParameter("txtDocumentos"));
        domicilioVO.setAtributo("codPais",request.getParameter("codPais"));
        domicilioVO.setAtributo("codProvincia",request.getParameter("codProvincia"));
        domicilioVO.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
        domicilioVO.setAtributo("codVia",request.getParameter("txtCodVia"));
        domicilioVO.setAtributo("descVia",request.getParameter("txtDomicilio"));
        domicilioVO.setAtributo("codTipoVia",request.getParameter("codTVia"));
        domicilioVO.setAtributo("descTipoVia",request.getParameter("descTVia"));
        domicilioVO.setAtributo("numDesde",request.getParameter("txtNumDesde"));
        domicilioVO.setAtributo("letraDesde",request.getParameter("txtLetraDesde"));
        domicilioVO.setAtributo("numHasta",request.getParameter("txtNumHasta"));
        domicilioVO.setAtributo("letraHasta",request.getParameter("txtLetraHasta"));
        domicilioVO.setAtributo("bloque",request.getParameter("txtBloque"));
        domicilioVO.setAtributo("portal",request.getParameter("txtPortal"));
        domicilioVO.setAtributo("escalera",request.getParameter("txtEsc"));
        domicilioVO.setAtributo("planta",request.getParameter("txtPlta"));
        domicilioVO.setAtributo("puerta",request.getParameter("txtPta"));
        domicilioVO.setAtributo("km",request.getParameter("txtKm"));
        domicilioVO.setAtributo("hm",request.getParameter("txtHm"));
        /* anadir ECO/ESI */
        domicilioVO.setAtributo("codECO",request.getParameter("codECO"));
        domicilioVO.setAtributo("codESI",request.getParameter("codESI"));
        /* fin anadir ECO/ESI */

        return domicilioVO;
    }
    private GeneralValueObject obtenerDatosCodPostal(TercerosValueObject terVO){
        
        GeneralValueObject gVO = new GeneralValueObject();
        DomicilioSimpleValueObject domVO = (DomicilioSimpleValueObject)terVO.getDomicilios().get(0);
        gVO.setAtributo("codPais",domVO.getIdPais());
        gVO.setAtributo("codProvincia", domVO.getIdProvincia());
        gVO.setAtributo("codMunicipio", domVO.getIdMunicipio());
        gVO.setAtributo("descPostal",domVO.getCodigoPostal());
        gVO.setAtributo("defecto", 0);
        return gVO;
    }
    
    /**
     * Para realizar busqueda abierta, rodea el string con "*" si no contiene
     * ninguno de los comodines "*", "&" o "|".
     * @param str String a tratar
     * @return String rodeado de "*", si es el caso
     */
    private String tratarBusquedaAbierta(String str){
        if (str != null && !str.equals("")) {
            if (!str.contains("*") && !str.contains("&") && !str.contains("|")) {
                str = "*" + str + "*";   
            }
        }            
        return str;    
    }

    private Vector borraDeListaTerceros (TercerosValueObject terVO, Vector listaTerceros)    {
        int i = 0;
        for (Iterator it = listaTerceros.iterator();it.hasNext();){
            TercerosValueObject terAux = (TercerosValueObject)it.next();
            if (terAux.getIdentificador().equals(terVO.getIdentificador())) {
                listaTerceros.removeElementAt(i);
                break;
            } else i++;
        }
        return listaTerceros;
    }
    
    
    private HashMap<String,String> tratarCamposSuplementariosAltaTerceroExterno(String campos){
        HashMap<String,String> salida = new HashMap<String,String>();
        if(campos!=null && !"".equals(campos)){
            String[] datos = campos.split("§¥");
            for(int i=0;datos!=null && i<datos.length;i++){
                
                String[] dato = datos[i].split(";");
                if(dato!=null && dato.length==2) salida.put(dato[0],dato[1]);
                
                
            }//for
        }//if
         
        
        return salida;
        
    }
}
