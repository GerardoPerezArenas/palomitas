package es.altia.agora.interfaces.user.web.usuariosforms;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.altia.agora.business.usuariosforms.UsuariosFormsValueObject;
import es.altia.agora.business.usuariosforms.UsuariosFormsPermisosVO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.usuariosforms.persistence.UsuariosFormsManager;
import es.altia.agora.interfaces.user.web.usuariosforms.UsuariosFormsForm;
import es.altia.agora.business.administracion.mantenimiento.UsuariosGruposValueObject;
import es.altia.agora.business.administracion.mantenimiento.persistence.CargosManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.UORsManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.UsuariosGruposManager;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.usuariosforms.persistence.UsuariosFormsPermisosManager;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import java.util.StringTokenizer;


public final class UsuariosFormsAction extends ActionSession {


	public ActionForward performSession(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {

        m_Log.debug("perform");

        //Validaremos los parametros del request especificados
        HttpSession session = request.getSession();
        String opcion = "";

        if ((session.getAttribute("usuario") != null)) {
            UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
            String[] params = usuario.getParamsCon();
            int codOrg;
            int codEnt;
            codOrg = usuario.getOrgCod();
            codEnt = usuario.getEntCod();
            String cOrg = Integer.toString(codOrg);
            String cEnt = Integer.toString(codEnt);
            String descOrg = usuario.getOrg();
            String descEnt = usuario.getEnt();

            // Si usuario en sesion es nulo --> error.

            UsuariosFormsValueObject ufVO = new UsuariosFormsValueObject();
            UsuariosFormsForm ufForm = null;

            if (form == null) {
                m_Log.debug("Rellenamos el form de UsuariosForms");
                form = new UsuariosFormsForm();
                if ("request".equals(mapping.getScope())) {
                    request.setAttribute(mapping.getAttribute(), form);
                } else {
                    session.setAttribute(mapping.getAttribute(), form);
                }
            }

            ufForm = (UsuariosFormsForm) form;

            opcion = request.getParameter("opcion");
            if (m_Log.isInfoEnabled()) {
                m_Log.info("la opcion en el action es " + opcion);
            }

            
            if ("inicio".equals(opcion)) {
                Vector consulta = new Vector();
                consulta = UsuariosFormsManager.getInstance().getUsuariosForms(cOrg, params);
                session.setAttribute("RelacionUsuarios", consulta);
                opcion = "inicio";

            } else if ("cargar_pagina".equals(opcion)) {
                ufVO = ufForm.getUsuariosForms();
                ufForm.setUsuariosForms(ufVO);
                opcion = "cargar_pagina";

            } else if ("grabar_modificacion".equals(opcion)) {
                ufVO = ufForm.getUsuariosForms(); //Recojemos los datos del usuario
                
                GeneralValueObject genVO = obtenerPermisosUsuario(ufVO.getLogin(),request);
                ufVO.setListaUors((Vector)genVO.getAtributo("listaPermisos"));

                int resultado = UsuariosFormsManager.getInstance().modificarUsuario(ufVO, params);

                if (resultado != -1) {
                    ufVO.setRespOpcion("grabar_modificacion");
                }
                ufForm.setUsuariosForms(ufVO);
                opcion = "verOculto";

            } else if ("alta".equals(opcion)) {
                ufVO = ufForm.getUsuariosForms();
                String modo = request.getParameter("segundo");
                if ("alta".equals(modo)) {
                    session.setAttribute("modo", "alta");
                    ufVO = new UsuariosFormsValueObject();
                } else {
                    session.setAttribute("modo", "modificar");
                }
                String codUsuario = request.getParameter("primero");
                ufVO = UsuariosFormsManager.getInstance().getDatosUsuarios(ufVO, codUsuario, cOrg, params);
                UsuariosFormsValueObject u = new UsuariosFormsValueObject();

                // obtenemos los permisos de los usuarios
                Vector listaPermisosUsuario = UsuariosFormsPermisosManager.getInstance().obtenerListaPermisosUsuario(codUsuario, params);
                ufForm.setListaUsuariosFormsPermisos(listaPermisosUsuario);

                Vector listaPerfiles = UsuariosFormsManager.getInstance().getListaPerfiles(params);
                ufVO.setListaPerfiles(listaPerfiles);
                Vector listaCatProfesionales = UsuariosFormsManager.getInstance().getListaCatProfesionales(params);
                ufVO.setListaCatProfesionales(listaCatProfesionales);
                Vector listaUors = UsuariosFormsManager.getInstance().getListaUors(params);
                ufVO.setListaUors(listaUors);
                Vector consulta = new Vector();
                consulta = UsuariosFormsManager.getInstance().getUsuariosForms(cOrg, params);
                ufVO.setListaTodosUsuarios(consulta);
                ufForm.setUsuariosForms(ufVO);
                opcion = "alta";

            } else if ("modificar".equals(opcion)) {
                ufVO = ufForm.getUsuariosForms();
                String modo = request.getParameter("segundo");
                if ("alta".equals(modo)) {
                    session.setAttribute("modo", "alta");
                    ufVO = new UsuariosFormsValueObject();
                } else {
                    session.setAttribute("modo", "modificar");
                }
                String codUsuario = request.getParameter("primero");
                ufVO.setLogin(codUsuario);
                ufVO = UsuariosFormsManager.getInstance().getDatosUsuarios(ufVO, codUsuario, cOrg, params);
                
                // obtenemos los permisos de los usuarios
                Vector listaPermisosUsuario = UsuariosFormsPermisosManager.getInstance().obtenerListaPermisosUsuario(codUsuario, params);
                ufForm.setListaUsuariosFormsPermisos(listaPermisosUsuario);

                Vector listaPerfiles = UsuariosFormsManager.getInstance().getListaPerfiles(params);
                ufVO.setListaPerfiles(listaPerfiles);
                Vector listaCatProfesionales = UsuariosFormsManager.getInstance().getListaCatProfesionales(params);
                ufVO.setListaCatProfesionales(listaCatProfesionales);
                Vector listaUors = UsuariosFormsManager.getInstance().getListaUors(params);
                ufVO.setListaUors(listaUors);
                Vector consulta = new Vector();
                consulta = UsuariosFormsManager.getInstance().getUsuariosForms(cOrg, params);
                ufForm.setUsuariosForms(ufVO);
                opcion = "modificar";

            } else if ("insertarUsuario".equals(opcion)) {
                ufVO = ufForm.getUsuariosForms();

                GeneralValueObject genVO = obtenerPermisosUsuario(ufVO.getLogin(),request);
                ufVO.setListaUors((Vector)genVO.getAtributo("listaPermisos"));
                
                int resultado = UsuariosFormsManager.getInstance().insertarUsuario(ufVO, params);

                if (resultado != -1) {
                    ufVO.setRespOpcion("insertarUsuario");
                }
                opcion = "verOculto";

            } else if ("eliminarUsuario".equals(opcion)) {
                ufVO = ufForm.getUsuariosForms();
                String codUsuario = request.getParameter("codUsuario");

                int resultado = UsuariosFormsManager.getInstance().eliminarUsuario(codUsuario, params);
                if (resultado != -1) {
                    ufVO.setRespOpcion("eliminarUsuario");
                }
                ufForm.setUsuariosForms(ufVO);
                opcion = "verOculto";
            }
        } else { // No hay usuario.
            m_Log.debug("MantAnotacionRegistroAction --> no hay usuario");
            opcion = "no_usuario";
        }
        /* Redirigimos al JSP de salida*/
        return (mapping.findForward(opcion));

    }

      private GeneralValueObject obtenerPermisosUsuario(String loginUsuario, HttpServletRequest request) {
        GeneralValueObject generalVO = new GeneralValueObject();

        // Obtener los datos de las distintas listas que se crean en la jsp 
        String listaCodUnidadesOrganicas = request.getParameter("listaCodUnidadesOrganicas");
        String listaCodCargos = request.getParameter("listaCodCargos");

        Vector listaPermisos = new Vector();
        StringTokenizer lCodUnidadesOrganicasUsuario = null;
        StringTokenizer lCodCargosUsuario = null;

        lCodUnidadesOrganicasUsuario = new StringTokenizer(listaCodUnidadesOrganicas, "зе", false);
        lCodCargosUsuario = new StringTokenizer(listaCodCargos, "зе", false);

        while (lCodUnidadesOrganicasUsuario.hasMoreTokens()) {
            String codUOR = lCodUnidadesOrganicasUsuario.nextToken();
            String codCargo = lCodCargosUsuario.nextToken();

            UsuariosFormsPermisosVO usuariosFomsPermisosVO = new UsuariosFormsPermisosVO();
            usuariosFomsPermisosVO.setLoginUsuarioPermiso(loginUsuario);
            usuariosFomsPermisosVO.setCodUnidadOrganicaPermiso(codUOR);
            usuariosFomsPermisosVO.setCodCargoPermiso(codCargo);

            listaPermisos.addElement(usuariosFomsPermisosVO);

        }
        generalVO.setAtributo("listaPermisos", listaPermisos);
        return generalVO;
    }

}
