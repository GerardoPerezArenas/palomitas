// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.terceros.mantenimiento;

// PAQUETES IMPORTADOS
import es.altia.agora.business.escritorio.*;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.helper.*;
import es.altia.agora.business.terceros.mantenimiento.persistence.*;
import es.altia.agora.business.util.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase TipoDocumentosAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Fernando Rueda Rueda
 * @version 1.0
 */


public class TipoDocumentosAction extends ActionSession  {
  protected static Log m_Log =
          LogFactory.getLog(TipoDocumentosAction.class.getName());
  String codPais = "";
  String codProvincia = "";
  String codMunicipio = "";

  public ActionForward performSession(ActionMapping mapping, ActionForm form,
                      HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {

    m_Log.debug("perform");
    ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));
    HttpSession session = request.getSession();

    // Validaremos los parametros del request especificados
    ActionErrors errors = new ActionErrors();
    String opcion = request.getParameter("opcion");
    if (m_Log.isInfoEnabled()) m_Log.info("la opcion en el action es " + opcion);

    // Rellenamos el form de BusquedaTerceros
    if (form == null) {
      m_Log.debug("Rellenamos el form de MantenimientosTerceros");
      form = new MantenimientosTercerosForm();
      if ("request".equals(mapping.getScope())){
        request.setAttribute(mapping.getAttribute(), form);
      }else{
        session.setAttribute(mapping.getAttribute(), form);
      }
    }
    //Cogemos los parametros contenidos en la sesión
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    String[] params = usuario.getParamsCon();
    //Instanciamos el Form de Terceros que usaremos como contenedor de
    //datos
    MantenimientosTercerosForm mantForm = (MantenimientosTercerosForm)form;
    //Recogemos los parámetros del request, principalmente vienen de la jsp
    GeneralValueObject gVO = recogerParametros(request);
    //Instaciamos las clases necesarias para cargar la lista de datos
    //que será la de los tipos de Documentos
    TipoDocumentosManager tipoDocumentosManager = TipoDocumentosManager.getInstance();
    //Añadimos el tipo de ventana en el Form
    mantForm.setVentana(request.getParameter("ventana"));
    if ("cargar".equalsIgnoreCase(opcion))
    {
      //Cargamos la lista de Tipos de Documentos
      Vector listaTiposDocs = new Vector();
      listaTiposDocs = tipoDocumentosManager.getListaTipoDocumentos(params);
      mantForm.setListaTipoDocs(listaTiposDocs);
    }
    else if("eliminar".equalsIgnoreCase(opcion))
    {
      Vector listaTipoDocumentos = new Vector();
      String codigo = (String)gVO.getAtributo("codTipoDoc");
      listaTipoDocumentos = tipoDocumentosManager.eliminarTipoDocumento(codigo,params);
      if (listaTipoDocumentos!=null && listaTipoDocumentos.size()!=0) {
        gVO = (GeneralValueObject) listaTipoDocumentos.firstElement();
      }
      String puedeEliminar = (String) gVO.getAtributo("puedeEliminar");
      if(puedeEliminar != null && !puedeEliminar.equals("")) {
      	opcion = "noPuedeEliminar";
      } else {
	      mantForm.setListaTipoDocs(listaTipoDocumentos);
	      opcion = "cargarTiposDocs";
      }
    }
    else if("modificar".equals(opcion))
    {
      Vector listaTipoDocumentos = new Vector();
      listaTipoDocumentos = tipoDocumentosManager.modificarTipoDocumento(gVO,params);
      mantForm.setListaTipoDocs(listaTipoDocumentos);
      opcion = "cargarTiposDocs";
    }
    else if("alta".equals(opcion))
    {
      Vector listaTipoDocumentos = new Vector();
      listaTipoDocumentos = tipoDocumentosManager.altaTipoDocumento(gVO,params);
      mantForm.setListaTipoDocs(listaTipoDocumentos);
      opcion = "cargarTiposDocs";
    }
    else if("salir".equals(opcion))
    {
      if ((session.getAttribute(mapping.getAttribute()) != null))
        session.removeAttribute(mapping.getAttribute());
    }
    else
    {
      opcion = mapping.getInput();
    }
    return (mapping.findForward(opcion));
  }

  private GeneralValueObject recogerParametros(HttpServletRequest request){
    GeneralValueObject gVO = new GeneralValueObject();
    //Recogemos y asignamos al gVO los parametros con los nombres que les damos
    //en la jsp
    gVO.setAtributo("codTipoDocAntiguo",request.getParameter("codTipoDocAntiguo"));
    gVO.setAtributo("codTipoDoc",request.getParameter("codTipoDoc"));
    gVO.setAtributo("codigoINE",request.getParameter("codigoINE"));
    gVO.setAtributo("codigoAccede",request.getParameter("codigoAccede"));
    if (request.getParameter("duplicado")!=null)
      gVO.setAtributo("duplicado",request.getParameter("duplicado"));
    else //No está chequeado, entonces asigno un 0
      gVO.setAtributo("duplicado","0");
    if (request.getParameter("persFJ")!=null)
      gVO.setAtributo("persFJ",request.getParameter("persFJ"));
    else//No está chequeado, entonces asigno un 0
      gVO.setAtributo("persFJ","0");
    if (request.getParameter("normalizado")!=null)
      gVO.setAtributo("normalizado",request.getParameter("normalizado"));
    else//No está chequeado, entonces asigno un 0
      gVO.setAtributo("normalizado","0");
    gVO.setAtributo("descTipoDoc",request.getParameter("descTipoDoc"));
    gVO.setAtributo("grupo1",request.getParameter("grupo1"));
    gVO.setAtributo("tipo1",request.getParameter("tipo1"));
    if (request.getParameter("grupo2")==null || request.getParameter("grupo2").equals("")) {
        gVO.setAtributo("grupo2","null");
    } else {
        gVO.setAtributo("grupo2",request.getParameter("grupo2"));
    }
    gVO.setAtributo("tipo2",request.getParameter("tipo2"));
    if (request.getParameter("grupo3")==null || request.getParameter("grupo3").equals("")) {
        gVO.setAtributo("grupo3","null");
    } else {
        gVO.setAtributo("grupo3",request.getParameter("grupo3"));
    }
    gVO.setAtributo("tipo3",request.getParameter("tipo3"));
    if (request.getParameter("grupo4")==null || request.getParameter("grupo4").equals("")) {
        gVO.setAtributo("grupo4","null");
    } else {
        gVO.setAtributo("grupo4",request.getParameter("grupo4"));
    }
    gVO.setAtributo("tipo4",request.getParameter("tipo4"));
    if (request.getParameter("grupo5")==null || request.getParameter("grupo5").equals("")) {
        gVO.setAtributo("grupo5","null");
    } else {
        gVO.setAtributo("grupo5",request.getParameter("grupo5"));
    }
    gVO.setAtributo("tipo5",request.getParameter("tipo5"));
    if (request.getParameter("longitudMaxima")==null || request.getParameter("longitudMaxima").equals("")) {
        gVO.setAtributo("longitudMaxima","null");
    } else {
        gVO.setAtributo("longitudMaxima",request.getParameter("longitudMaxima"));
    }
    gVO.setAtributo("validacion",request.getParameter("validacion"));
    return gVO;
  }
}