package es.altia.agora.interfaces.user.web.registro.mantenimiento;

import es.altia.agora.business.escritorio.RegistroUsuarioValueObject;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.mantenimiento.MantClasifAsuntosValueObject;
import es.altia.agora.business.registro.mantenimiento.persistence.MantClasifAsuntosManager;
import es.altia.agora.interfaces.user.web.registro.mantenimiento.exceptions.EliminarClasificacionAsuntoException;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;



public class MantClasifAsuntosAction  extends ActionSession {
    
    protected static Log m_Log =
        LogFactory.getLog(MantClasifAsuntosAction.class.getName());
    
    public ActionForward performSession(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        m_Log.debug("================= MantClasifAsuntosAction ======================>");
                
        HttpSession session = request.getSession();        
        UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        RegistroUsuarioValueObject regUsuarioVO = (RegistroUsuarioValueObject)session.getAttribute("registroUsuario");
        MantClasifAsuntosForm clasifAsuntosForm = (MantClasifAsuntosForm) form;
        MantClasifAsuntosValueObject clasifAsuntoVO = null;
        String formName = mapping.getAttribute();
        
        if (clasifAsuntosForm==null){
             m_Log.debug("El formulario es null");
        }
        
         m_Log.debug("El form name es:"+formName);
          // Parametros propios de esta accion        
        String opcion = clasifAsuntosForm.getOpcion();
      
        // Cogemos el codigo de unidad de registro del usuario para recuperar
        // la clasificacion de asuntos de esta unidad. 
         int  codUnidadRegistroGeneral = regUsuarioVO.getUnidadOrgCod();
      
        m_Log.debug("Opcion=" + opcion );                        
        m_Log.debug("Unidad de registro con codigo=" + codUnidadRegistroGeneral + " y descripcion= " + regUsuarioVO.getUnidadOrg());
        m_Log.debug("Usamos el form : " + formName);        
                
        // Recuperamos los parametros de conexión                
        String[] params = usuario.getParamsCon();
        
        
           
        // ---- OPCION CARGAR ----        
        if (opcion.equals("cargar")){
        // Carga la página de clasificacion asuntos (página con el listado de clasificaciones de asuntos).
            m_Log.debug("Opción:cargar----------------------->"); 
            ArrayList<MantClasifAsuntosValueObject> clasifAsuntos =            
                MantClasifAsuntosManager.getInstance().cargarClasifAsuntos(codUnidadRegistroGeneral,params);                        
            clasifAsuntosForm.setClasifAsuntos(clasifAsuntos);                    
             m_Log.debug("Saímos de Opción:cargar----------------------->"); 
               
        } else if(opcion.equals("alta")) {
            // Carga la página de definición de clasificacion de asuntos 
            //con una clasificación en blanco
            
            m_Log.debug("Opción:alta----------------------->"); 
            clasifAsuntosForm.setDescripcion("");
            clasifAsuntosForm.setCodigo(null);
            clasifAsuntosForm.setUnidadRegistro(codUnidadRegistroGeneral);
            request.setAttribute("modificar",false);   
      
        //---- OPCION GRABAR_ALTA ----
        } else if(opcion.equals("grabar_alta")) { 
            // Guarda el alta de un nuevo asunto.
            String descripcion = request.getParameter("descripcion");
            String codigo = request.getParameter("codAsunto");
            
            clasifAsuntoVO= new MantClasifAsuntosValueObject();
            clasifAsuntoVO.setDescripcion(descripcion);
            clasifAsuntoVO.setCodigo(Integer.parseInt(codigo));
            clasifAsuntoVO.setUnidadRegistro(codUnidadRegistroGeneral);
            m_Log.debug("Grabando......: " + clasifAsuntoVO.toString());            
            MantClasifAsuntosManager.getInstance().grabarAlta(clasifAsuntoVO,params);
            
            // Recargamos la pagina de asuntos
            ArrayList<MantClasifAsuntosValueObject> clasifAsuntos = MantClasifAsuntosManager.getInstance().cargarClasifAsuntos(codUnidadRegistroGeneral,params);                        
            clasifAsuntosForm.setClasifAsuntos(clasifAsuntos);
             
            opcion = "ocultoActualizarVista";                                               
            
        }
        else if(opcion.equals("grabar_modificar")) {
        // Guarda las modificaciones realizadas en un asunto existente.
            
            String codigo = request.getParameter("codigo");
            String descripcion = request.getParameter("descripcion");
            //String sUnidadRegistro = request.getParameter("unidadRegistro");
            
            //m_Log.info("Estamos en la opcion grabar_modificar, para la clasificacion con codigo: "+codigo+""
            //        + " y descripcion: "+descripcion + "y unidadRegristro:"+ sUnidadRegistro);
            
            MantClasifAsuntosManager.getInstance().grabarModificacion(descripcion,Integer.parseInt(codigo),codUnidadRegistroGeneral, params);
            
            // Recargamos la pagina de asuntos
            ArrayList<MantClasifAsuntosValueObject> clasifAsuntos =          
                MantClasifAsuntosManager.getInstance().cargarClasifAsuntos(codUnidadRegistroGeneral, params);                        
            clasifAsuntosForm.setClasifAsuntos(clasifAsuntos);
                        
            //opcion = "cargar";
            opcion = "ocultoActualizarVista";
            
        //---- OPCION ELIMINAR ----        
            
        } else if(opcion.equals("eliminar")) {
        // Elimina la clasificación seleccionada 
            m_Log.info("Estamos en la opción: Eliminar");
            m_Log.debug("Estamos en la opción: Eliminar. Codigo de la clasificacion"+clasifAsuntosForm.getCodigo());
            m_Log.debug("Estamos en la opción: Eliminar. Codigo de la unidad de registro"+clasifAsuntosForm.getUnidadRegistro());
            Integer codigo=clasifAsuntosForm.getCodigo();
            //Integer unidadRegistro=clasifAsuntosForm.getUnidadRegistro();
         
           try {
             //Saltará la excepción si intentamos eliminar alguna clasificación, para la
            // cual haya asuntos con esa clasificacion 
             MantClasifAsuntosManager.getInstance().eliminarClasifAsunto(codigo,codUnidadRegistroGeneral, params);
           } catch (EliminarClasificacionAsuntoException e ) {
              request.setAttribute("errorEliminar","error"); 
              return (mapping.findForward("error"));
           }
            
            
            
            // Recargamos la pagina de asuntos
            ArrayList<MantClasifAsuntosValueObject> clasifAsuntos =            
                MantClasifAsuntosManager.getInstance().cargarClasifAsuntos(codUnidadRegistroGeneral,params);                        
            clasifAsuntosForm.setClasifAsuntos(clasifAsuntos);
            
            opcion ="ocultoActualizarVista";
                                    
        //---- OPCION SALIR ----       
        } else if(opcion.equals("salir")) {
        // Abandona la página de asuntos.
            
            if ((session.getAttribute(formName) != null))
                session.removeAttribute(formName);
        
        //---- OPCION LISTAPROCEDIMIENTOS ----                  
        }
     
          m_Log.debug("<================= MantClasifAsuntosAction ======================");
          return (mapping.findForward(opcion));
        
    }
}
    
    
