package es.altia.agora.interfaces.user.web.administracion.mantenimiento;

import es.altia.agora.business.escritorio.*;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.helper.*;
import es.altia.agora.business.administracion.mantenimiento.persistence.*;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.util.*;
import es.altia.arboles.impl.ArbolImpl;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase UORsAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: ALTIA CONSULTORES</p>
 * @version 1.0
 */


public class CargosAction extends ActionSession  {

    public ActionForward performSession(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        m_Log.debug("perform");
        ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));
        HttpSession session = request.getSession();

        // Validaremos los parametros del request especificados
        ActionErrors errors = new ActionErrors();

        // decidir que acción tomar
        String opcion = request.getParameter("opcion");

        if (m_Log.isInfoEnabled()) {
            m_Log.info("la opción en el action es: " + opcion);
        }

        // Rellenamos el form de MantenimientosAdminForm
        if (form == null) {
            m_Log.debug("Rellenamos el form de MantenimientosAdminForm");
            form = new MantenimientosAdminForm();
            if ("request".equals(mapping.getScope())){
                request.setAttribute(mapping.getAttribute(), form);
            }
            else{
                session.setAttribute(mapping.getAttribute(), form);
            }
        }


        UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        String[] params = usuario.getParamsCon();
        MantenimientosAdminForm mantForm = (MantenimientosAdminForm)form;

        CargosManager uorManager = CargosManager.getInstance();
        GeneralValueObject gVO = recogerParametros(request);


        //m_Log.info("=============================================>");
        if ("cargar".equalsIgnoreCase(opcion)) {
            // =======================
            // nuevas UORs
            m_Log.debug("Recargando datos de los cargos en la sesión");
            Vector nuevasUOR = CargosManager.getInstance().getListaUORs(params);
            /*for(int i=0; i<nuevasUOR.size(); i++) {
                m_Log.info(((UORDTO)nuevasUOR.get(i)).toString());
            }*/
            m_Log.debug("Cargadas " + nuevasUOR.size() + " UORs");
            mantForm.setListaNuevasUORs(nuevasUOR);
            // arbol de jerarquía de uors
            ArbolImpl arbol = CargosManager.getInstance().getArbolUORs(false, params);
            m_Log.debug("Cargado árbol:" + arbol.contarNodos());
            //m_Log.debug(arbol.toString());
            mantForm.setArbol(arbol);
            // =======================

            mantForm.setOtrosDatos(null);
            opcion = "cargar";
        }
        else if("eliminar".equalsIgnoreCase(opcion)) {
            int codigo = Integer.parseInt(request.getParameter("codUOR"));
            //m_Log.debug("cod recuperado: " + codigo);
            
            
            //No importa los parámetros que pasemos al eliminar, solo el codigo.
			int usuariosCatalogo = CargosManager.getInstance().usuariosCatalogoAfectados(codigo, params,"","");
			if (usuariosCatalogo==0){
	            int eliminada = CargosManager.getInstance().eliminarUOR(codigo, params);
	            // si no hay problema
	            if (eliminada > 0)
	                mantForm.setOtrosDatos(null);
	            // si hay registros asociados al cod
	            else if (eliminada == -1) {
	                m_Log.debug("problema con eliminar: " + eliminada);
	                GeneralValueObject gVO1 = new GeneralValueObject();
	                gVO1.setAtributo("respuesta","hayRegistros");
	                mantForm.setOtrosDatos(gVO1);
	            }
	            // problema
	            else {
	                m_Log.debug("problema con eliminar: " + eliminada);
	                GeneralValueObject gVO1 = new GeneralValueObject();
	                gVO1.setAtributo("respuesta","noEliminada");
	                mantForm.setOtrosDatos(gVO1);
	            }
			
			}else{
			    m_Log.debug("problema con eliminar: existen usuarios de formularios");
			    GeneralValueObject gVO1 = new GeneralValueObject();
			    gVO1.setAtributo("respuesta","formularios");
			    mantForm.setOtrosDatos(gVO1);
			}

            // recargar datos en la sesión
            m_Log.debug("Recargando datos de las UORs en la sesión");
            Vector nuevasUOR = CargosManager.getInstance().getListaUORs(params);
            m_Log.debug("Cargadas " + nuevasUOR.size() + " UORs");
            mantForm.setListaNuevasUORs(nuevasUOR);
            // arbol de jerarquía de uors
            ArbolImpl arbol = CargosManager.getInstance().getArbolUORs(false, params);
            m_Log.debug("Cargado árbol:" + arbol.contarNodos());
            mantForm.setArbol(arbol);
            request.setAttribute("operacion_realizada", "eliminar");
            opcion = "vuelveCargar";

        }
        else if("modificar".equals(opcion)){
            request.setAttribute("operacion_realizada", "modificar");
            // recuperar los datos del UOR de la sesión y rellenar el dto
            UORDTO dto = new UORDTO();
            // uor cod
            dto.setUor_cod(request.getParameter("codUOR"));
            m_Log.debug("request.getParameter(\"codUOR\"): " + request.getParameter("codUOR"));
            // uor pad
            dto.setUor_pad(request.getParameter("codPad"));
            m_Log.debug("request.getParameter(\"codPad\"): " + request.getParameter("codPad"));
            // uor cod visible
            dto.setUor_cod_vis(request.getParameter("codigo_visible"));
            m_Log.debug("request.getParameter(\"codigo_visible\"): " + request.getParameter("codigo_visible"));
            // estado
            dto.setUor_estado(request.getParameter("estado"));
            m_Log.debug("request.getParameter(\"estado\"): " + request.getParameter("estado"));
            // nombre/descripción
            dto.setUor_nom(request.getParameter("nombre"));
            m_Log.debug("request.getParameter(\"nombre\"): " + request.getParameter("nombre"));
            // fecha alta
            dto.setUor_fecha_alta(request.getParameter("fecha_alta"));
            m_Log.debug("request.getParameter(\"fecha_alta\"): " + request.getParameter("fecha_alta"));
            // fecha baja
            dto.setUor_fecha_baja(request.getParameter("fecha_baja"));
            m_Log.debug("request.getParameter(\"fecha_baja\"): " + request.getParameter("fecha_baja"));



            int usuariosCatalogo = CargosManager.getInstance().usuariosCatalogoAfectados(
            		Integer.parseInt(request.getParameter("codUOR")),params,request.getParameter("estado"), 
            		request.getParameter("codigo_visible"));
            if (usuariosCatalogo==0){
            int actualizada = CargosManager.getInstance().modificarUOR(dto, params);
	            if (actualizada ==-1) {
	                GeneralValueObject gVO1 = new GeneralValueObject();
	                gVO1.setAtributo("respuesta","modificar_existe");
	                mantForm.setOtrosDatos(gVO1);
	            } else mantForm.setOtrosDatos(null);
            }
            else{
                m_Log.debug("problema con modificar: existen usuarios de formularios");
                GeneralValueObject gVO1 = new GeneralValueObject();
                gVO1.setAtributo("respuesta","formularios");
                mantForm.setOtrosDatos(gVO1);
            }
                     
            opcion ="vuelveCargar";
            // recargar datos en la sesión
            m_Log.debug("Recargando datos de las UORs en la sesión");
            Vector nuevasUOR = CargosManager.getInstance().getListaUORs(params);
            m_Log.debug("Cargadas " + nuevasUOR.size() + " UORs");
            mantForm.setListaNuevasUORs(nuevasUOR);
            // arbol de jerarquía de uors
            ArbolImpl arbol =CargosManager.getInstance().getArbolUORs(false, params);
            m_Log.debug("Cargado árbol:" + arbol.contarNodos());
            mantForm.setArbol(arbol);
        }
        else if("alta".equals(opcion)){

            UORDTO dto = new UORDTO();
            // uor cod
            //dto.setUor_cod(request.getParameter("codUOR"));
            //m_Log.debug("request.getParameter(\"codUOR\"): " + request.getParameter("codUOR"));
            // uor pad
            m_Log.debug("request.getParameter(\"codPad\"): " + request.getParameter("codPad"));
            dto.setUor_pad(request.getParameter("codPad"));
            // uor cod visible
            m_Log.debug("request.getParameter(\"codigo_visible\"): " + request.getParameter("codigo_visible"));
            dto.setUor_cod_vis(request.getParameter("codigo_visible"));
            // estado
            m_Log.debug("request.getParameter(\"estado\"): " + request.getParameter("estado"));
            dto.setUor_estado(request.getParameter("estado"));
            // nombre/descripción
            m_Log.debug("request.getParameter(\"nombre\"): " + request.getParameter("nombre"));
            dto.setUor_nom(request.getParameter("nombre"));
            // fecha alta
            m_Log.debug("request.getParameter(\"fecha_alta\"): " + request.getParameter("fecha_alta"));
            dto.setUor_fecha_alta(request.getParameter("fecha_alta"));
            // fecha baja
            m_Log.debug("request.getParameter(\"fecha_baja\"): " + request.getParameter("fecha_baja"));
            dto.setUor_fecha_baja(request.getParameter("fecha_baja"));

            //int insertada = uorManager.altaUOR(gVO, params);
            int insertada = CargosManager.getInstance().altaUOR(dto, params);
            if (insertada ==-1) {
                m_Log.debug("Problema con el alta");
                GeneralValueObject gVO1 = new GeneralValueObject();
                gVO1.setAtributo("respuesta","alta_existe");
                mantForm.setOtrosDatos(gVO1);
            }
            else mantForm.setOtrosDatos(null);
            /*Vector lista = uorManager.getListaUORs(params);
            mantForm.setListaUORs(lista);*/
            //opcion = "vuelveCargar";
            opcion = "cargar";

            // recargar datos en la sesión
            m_Log.debug("Recargando datos de las UORs en la sesión");
            Vector nuevasUOR = CargosManager.getInstance().getListaUORs(params);
            m_Log.debug("Cargadas " + nuevasUOR.size() + " UORs");
            mantForm.setListaNuevasUORs(nuevasUOR);
            // arbol de jerarquía de uors
            ArbolImpl arbol = CargosManager.getInstance().getArbolUORs(false, params);
            m_Log.debug("Cargado árbol:" + arbol.contarNodos());
            mantForm.setArbol(arbol);
        }
        else if("salir".equals(opcion)){
            if ((session.getAttribute(mapping.getAttribute()) != null))
                session.removeAttribute(mapping.getAttribute());
        }
        else{
            opcion = mapping.getInput();
            m_Log.debug("opcion en else: " + opcion);
        }
        
        //m_Log.info("<=============================================");
        return (mapping.findForward(opcion));
    }

    /**
     * Coge los nombres de las columnas de una UOR de la sesión y los escribe en el GeneralValueObject
     * @param request La request
     * @return GeneralValueObject con los nombres de las columnas
     */
    private GeneralValueObject recogerParametros(HttpServletRequest request){
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codDepartamento",request.getParameter("codigoDepartamento"));
        gVO.setAtributo("codUOR",request.getParameter("codigo"));
        gVO.setAtributo("descUOR",request.getParameter("descripcion"));
        gVO.setAtributo("tipoUOR",request.getParameter("tipo"));
        gVO.setAtributo("codUORAntiguo",request.getParameter("codUORAntiguo"));
        if (m_Log.isDebugEnabled())
            m_Log.debug("VALORES " + gVO.getAtributo("codDepartamento")+"//"+
            gVO.getAtributo("codUOR")+"//"+gVO.getAtributo("descUOR")+"//"+
            gVO.getAtributo("tipoUOR")+"//"+gVO.getAtributo("codUORAntiguo"));

        return gVO;
    }
}