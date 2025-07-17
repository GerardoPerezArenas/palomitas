/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.agora.interfaces.user.web.administracion.mantenimiento;

// PAQUETES IMPORTADOS
import es.altia.agora.business.escritorio.*;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.helper.*;
import es.altia.agora.business.administracion.mantenimiento.persistence.*;
import es.altia.agora.business.util.*;
import java.io.*; 
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;

/**
 *
 * @author paz.rodriguez
 */
public class CamposDesplegablesExternoAction extends ActionSession  {

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
      m_Log.debug("Rellenamos el form de MantenimientosAdminForm");
      form = new MantenimientosAdminForm();
      if ("request".equals(mapping.getScope())){
        request.setAttribute(mapping.getAttribute(), form);
      }else{
        session.setAttribute(mapping.getAttribute(), form);
      }
    }
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    String[] params = usuario.getParamsCon();
    MantenimientosAdminForm mantForm = (MantenimientosAdminForm)form;
    CamposDesplegablesExternoManager mantManager = CamposDesplegablesExternoManager.getInstance();

    if ("cargar".equalsIgnoreCase(opcion)){
      Vector lista = mantManager.getListaCampoDesplegableExterno(params);
      mantForm.setListaCamposDesplegables(lista);

   }else if("alta".equals(opcion)){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codigo",request.getParameter("Codigo"));
      gVO.setAtributo("descripcion",request.getParameter("Descripcion"));
      Vector lista = mantManager.altaCampoDesplegableExterno(gVO,params);
      mantForm.setListaCamposDesplegables(lista);
      opcion = "vuelveCargar"; 

    }else if("valores".equals(opcion)){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("campo",request.getParameter("Codigo"));
      gVO.setAtributo("descCampo",request.getParameter("Descripcion"));
      Vector lista = mantManager.getListaValoresCampoDesplegableExterno(gVO,params);
      mantForm.setListaValoresCamposDesplegables(lista);
     //campos suplementarios
      Vector lista2 = mantManager.getListaCampoSupExterno(gVO,params);
      mantForm.setListaCamposDesplegables(lista2);
      mantForm.setOtrosDatos(gVO);
      opcion = "valores";
      
    }else if("modifica".equals(opcion)){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codigo",request.getParameter("codigo"));
      gVO.setAtributo("descripcion",request.getParameter("descripcion"));
      gVO.setAtributo("descDriver_JDBC",request.getParameter("descDriver_JDBC"));
      gVO.setAtributo("urlDriver",request.getParameter("urlDriver"));
      gVO.setAtributo("usuario",request.getParameter("usuario"));
      gVO.setAtributo("contrasena",request.getParameter("contrasena"));
      gVO.setAtributo("tabla",request.getParameter("tabla"));
      gVO.setAtributo("campoCodigo",request.getParameter("campoCodigo"));
      gVO.setAtributo("campoValor",request.getParameter("campoValor"));
      gVO.setAtributo("campoValorId2",request.getParameter("campoValorId2"));
      int rdo = mantManager.modificaGuardaCampoDesplegableExterno(gVO,params);
      gVO.setAtributo("errores", (String.valueOf(rdo)));
      mantForm.setOtrosDatos(gVO);
      opcion = "modifica";

    }else if("probarConexion".equals(opcion)){
       GeneralValueObject gVO = new GeneralValueObject();
       gVO.setAtributo("descDriver_JDBC",request.getParameter("descDriver_JDBC"));
       gVO.setAtributo("urlDriver",request.getParameter("urlDriver"));
       gVO.setAtributo("usuario",request.getParameter("usuario"));
       gVO.setAtributo("contrasena",request.getParameter("contrasena"));
       gVO.setAtributo("tabla",request.getParameter("tabla"));
       gVO.setAtributo("codigo",request.getParameter("codigo"));
       gVO.setAtributo("campoCodigo",request.getParameter("campoCodigo"));
       gVO.setAtributo("campoValor",request.getParameter("campoValor"));
       gVO.setAtributo("campoValorId2",request.getParameter("campoValorId2"));
       Vector rdo = mantManager.probarConexion(gVO,params);
       if (rdo == null || rdo.isEmpty()){
            gVO.setAtributo("conexion", (String.valueOf(0)));
       }else{
            gVO.setAtributo("conexion", (String.valueOf(1)));
       }

       mantForm.setOtrosDatos(gVO);
       opcion = "probarConexion";

    }else if("importar".equals(opcion)){
       GeneralValueObject gVO = new GeneralValueObject();
       gVO.setAtributo("codigo",request.getParameter("codigo"));
       gVO.setAtributo("descDriver_JDBC",request.getParameter("descDriver_JDBC"));
       gVO.setAtributo("urlDriver",request.getParameter("urlDriver"));
       gVO.setAtributo("usuario",request.getParameter("usuario"));
       gVO.setAtributo("contrasena",request.getParameter("contrasena"));
       gVO.setAtributo("tabla",request.getParameter("tabla"));
       gVO.setAtributo("campoCodigo",request.getParameter("campoCodigo"));
       gVO.setAtributo("campoValor",request.getParameter("campoValor"));      
       Vector lista=mantManager.insertaCampoDesplegableExterno(gVO,params);
       mantForm.setOtrosDatos(gVO);
       mantForm.setListaCamposDesplegables(lista);
       opcion = "vuelveCargar";
       

    }else if("modificar".equals(opcion)){
       GeneralValueObject gVO = new GeneralValueObject();
       gVO.setAtributo("codigoAntiguo",request.getParameter("identificador"));
       gVO.setAtributo("codigo",request.getParameter("Codigo"));
       gVO.setAtributo("descripcion",request.getParameter("Descripcion"));
       Vector lista = mantManager.modificarCampoDesplegableExterno(gVO,params);
       mantForm.setListaCamposDesplegables(lista);
       opcion = "vuelveCargar";
       
    }else
    if("comprobarEliminacionCampo".equals(opcion)){
        
        String identificador = request.getParameter("identificador");        
        int salida = mantManager.comprobarEliminacionCampoDesplegableExterno(identificador, params);
        m_Log.debug("salida:: " + salida);
        
        //  0 -> Se puede eliminar el campo desplegable externo
        //  1 -> El campo está asignado como campo a nivel de procedimiento
        //  2 -> El campo está asignado como campo a nivel de trámite
        // -1 -> No se ha podido obtener una conexión a la BBDD
        // -2 -> Se ha producido un error técnico al eliminar el desplegable                          
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println(salida); 
        
    }    
    else    
    if("eliminar".equals(opcion)){
        String identificador = request.getParameter("identificador");        
        
        boolean exito = mantManager.eliminarCampoDesplegableExterno(identificador, params);
        if(exito){
            // Se ha eliminado el campo desplegable => Entonces se recuperan de nuevo la lista del campos para actualizar la vista
             Vector lista = mantManager.getListaCampoDesplegableExterno(params);
             
             StringBuffer salida = new StringBuffer();
             salida.append("<RESPUESTA>");
             salida.append("<CAMPOS>");
             
             for(int i=0;lista!=null && i<lista.size();i++){                 
                 GeneralValueObject gvo = (GeneralValueObject)lista.get(i);
                 salida.append("<CAMPO>");
                 salida.append("<CODIGO>");
                    salida.append((String)gvo.getAtributo("codigo"));                 
                 salida.append("</CODIGO>");
                 salida.append("<DESCRIPCION>");
                    salida.append((String)gvo.getAtributo("descripcion"));                                  
                 salida.append("</DESCRIPCION>");
                 salida.append("</CAMPO>");
             }// for
            salida.append("</CAMPOS>");
            salida.append("</RESPUESTA>");
            
            
            PrintWriter out = response.getWriter();
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");            
            out.print(salida.toString());
            out.flush();
            out.close();
            
            
        }// if
        
        
    }
    else
    {
      opcion = mapping.getInput();
    }
    m_Log.debug("perform");
    return (mapping.findForward(opcion));
  }
}