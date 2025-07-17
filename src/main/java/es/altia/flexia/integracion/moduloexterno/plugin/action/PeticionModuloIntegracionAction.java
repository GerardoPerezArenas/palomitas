package es.altia.flexia.integracion.moduloexterno.plugin.action;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoFactoria;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Este action recibe todas las peticiones en lo que a proceso de módulos de integración externos se refiere
 */
public class PeticionModuloIntegracionAction extends Action{

    private Logger log = Logger.getLogger(PeticionModuloIntegracionAction.class);


     public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

         log.debug(" ==================> PeticionModuloIntegracionAction init");
        String tarea     = request.getParameter("tarea"); // PUEDE TOMAR DOS VALORES (preparar, procesar)
        String operacion = request.getParameter("operacion"); // Operación a ejecutar (Lo proporciona flexia)
        String nombreModulo = request.getParameter("modulo"); // Nombre del módulo (Lo proporciona flexia)
        String codOrganizacion = request.getParameter("codOrganizacion"); // Código de la organización
        String codTramite = request.getParameter("codTramite"); // Código del trámite
        String ocuTramite = request.getParameter("ocuTramite"); // Ocurrencia del trámite
        String numero = request.getParameter("numero"); // Número del expediente
        String tipo   = request.getParameter("tipo"); // Tipo de operacion 0 -> Expediente, 1-> Trámite
        String codProcedimiento = request.getParameter("codProcedimiento"); // Código del procedimiento

        log.debug("===> tarea = " + tarea);
        log.debug("===> operacion = " + operacion);
        log.debug("===> nombreModulo = " + nombreModulo);
        log.debug("===> codOrganizacion = " + codOrganizacion);
        log.debug("===> tipo = " + tipo);
        log.debug("===> codProcedimeinto = " + codProcedimiento);

        ModuloIntegracionExternoFactoria factoria = ModuloIntegracionExternoFactoria.getInstance();
        UsuarioValueObject usuario = (UsuarioValueObject)request.getSession().getAttribute("usuario");
        if(codOrganizacion==null || "".equals(codOrganizacion))
            codOrganizacion = Integer.toString(usuario.getOrgCod());
		
        String redireccion = null;
        ModuloIntegracionExterno clase = factoria.getImplClass(Integer.parseInt(codOrganizacion), nombreModulo);

        if(clase!=null){
            clase.setNombreModulo(nombreModulo);
            if(tipo!=null && "0".equals(tipo)){
                // Si se procesa un formulario que se carga en la vista de expediente, no hay trámite ni ocurrencia.
                codTramite = "-1";
                ocuTramite = "-1";
            }

            Class[] tipoParametros      = null;
            Object[] valoresParametros = null;
            

            if("2".equals(tipo)){
                Class[] tipoParametrosPantallaDefProcedimientos = {int.class,String.class,HttpServletRequest.class,HttpServletResponse.class};
                
                // La petición procede de una pantalla a nivel de definición de procedimiento, entonces los parámetros de la operación/método cambian
                Object[] valoresParametros1  = {Integer.parseInt(codOrganizacion),codProcedimiento,request,response};
                valoresParametros = valoresParametros1;
                tipoParametros = tipoParametrosPantallaDefProcedimientos;
                
            }else{
                Class[] tipoParametrosPantallaExpTramite = {int.class,int.class,int.class,String.class,HttpServletRequest.class,HttpServletResponse.class};
                // La petición procede de una pantalla a nivel de ficha de expediente o // de ficha de trámite
                Object[] valoresParametros2 = {Integer.parseInt(codOrganizacion),Integer.parseInt(codTramite),Integer.parseInt(ocuTramite),numero,request,response};
                valoresParametros = valoresParametros2;
                tipoParametros = tipoParametrosPantallaExpTramite;
            }


            log.debug("Antes de llamar a la operación " + operacion + " del módulo: " + nombreModulo);
            if(tarea!=null && "preparar".equals(tarea) && operacion!=null && !"".equals(operacion)){
                redireccion = (String)factoria.ejecutarMetodo(clase, operacion, tipoParametros, valoresParametros);
            }else
                factoria.ejecutarMetodo(clase, operacion, tipoParametros, valoresParametros);

            log.debug("Después de llamar a la operación " + operacion + " del módulo: " + nombreModulo);
        }

        log.debug(" <================== PeticionModuloIntegracionAction");
        if(redireccion!=null && !"".equals(redireccion))
            return new ActionForward(redireccion);
        else return null;

     }
   
}