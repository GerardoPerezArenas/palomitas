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
 * Este action recibe todas las peticiones en lo que a proceso de m�dulos de integraci�n externos se refiere
 */
public class PeticionModuloIntegracionAction extends Action{

    private Logger log = Logger.getLogger(PeticionModuloIntegracionAction.class);


     public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

         log.debug(" ==================> PeticionModuloIntegracionAction init");
        String tarea     = request.getParameter("tarea"); // PUEDE TOMAR DOS VALORES (preparar, procesar)
        String operacion = request.getParameter("operacion"); // Operaci�n a ejecutar (Lo proporciona flexia)
        String nombreModulo = request.getParameter("modulo"); // Nombre del m�dulo (Lo proporciona flexia)
        String codOrganizacion = request.getParameter("codOrganizacion"); // C�digo de la organizaci�n
        String codTramite = request.getParameter("codTramite"); // C�digo del tr�mite
        String ocuTramite = request.getParameter("ocuTramite"); // Ocurrencia del tr�mite
        String numero = request.getParameter("numero"); // N�mero del expediente
        String tipo   = request.getParameter("tipo"); // Tipo de operacion 0 -> Expediente, 1-> Tr�mite
        String codProcedimiento = request.getParameter("codProcedimiento"); // C�digo del procedimiento

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
                // Si se procesa un formulario que se carga en la vista de expediente, no hay tr�mite ni ocurrencia.
                codTramite = "-1";
                ocuTramite = "-1";
            }

            Class[] tipoParametros      = null;
            Object[] valoresParametros = null;
            

            if("2".equals(tipo)){
                Class[] tipoParametrosPantallaDefProcedimientos = {int.class,String.class,HttpServletRequest.class,HttpServletResponse.class};
                
                // La petici�n procede de una pantalla a nivel de definici�n de procedimiento, entonces los par�metros de la operaci�n/m�todo cambian
                Object[] valoresParametros1  = {Integer.parseInt(codOrganizacion),codProcedimiento,request,response};
                valoresParametros = valoresParametros1;
                tipoParametros = tipoParametrosPantallaDefProcedimientos;
                
            }else{
                Class[] tipoParametrosPantallaExpTramite = {int.class,int.class,int.class,String.class,HttpServletRequest.class,HttpServletResponse.class};
                // La petici�n procede de una pantalla a nivel de ficha de expediente o // de ficha de tr�mite
                Object[] valoresParametros2 = {Integer.parseInt(codOrganizacion),Integer.parseInt(codTramite),Integer.parseInt(ocuTramite),numero,request,response};
                valoresParametros = valoresParametros2;
                tipoParametros = tipoParametrosPantallaExpTramite;
            }


            log.debug("Antes de llamar a la operaci�n " + operacion + " del m�dulo: " + nombreModulo);
            if(tarea!=null && "preparar".equals(tarea) && operacion!=null && !"".equals(operacion)){
                redireccion = (String)factoria.ejecutarMetodo(clase, operacion, tipoParametros, valoresParametros);
            }else
                factoria.ejecutarMetodo(clase, operacion, tipoParametros, valoresParametros);

            log.debug("Despu�s de llamar a la operaci�n " + operacion + " del m�dulo: " + nombreModulo);
        }

        log.debug(" <================== PeticionModuloIntegracionAction");
        if(redireccion!=null && !"".equals(redireccion))
            return new ActionForward(redireccion);
        else return null;

     }
   
}