package es.altia.flexia.expedientes.relacionados.plugin.action;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.persistence.TramitacionManager;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.flexia.expedientes.relacionados.plugin.PluginExpedientesRelacionados;
import es.altia.flexia.expedientes.relacionados.plugin.factoria.PluginExpedientesRelacionadosFactoria;
import es.altia.flexia.expedientes.relacionados.plugin.vo.ExpedienteRelacionadoVO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import es.altia.agora.business.administracion.mantenimiento.persistence.UsuariosGruposManager;

/**
 * Este Action se encarga de verificar la existencia de un expediente relacionado en el módulo de registro
 * @author oscar.rodriguez
 */
public class ValidarExistenciaExpRelacionadoRegistroAction extends ActionSession{
    private Logger log = Logger.getLogger(ValidarExistenciaExpRelacionadoRegistroAction.class);
  
    public ActionForward performSession(ActionMapping mapping,
                                        ActionForm form,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws IOException, ServletException {

        log.debug("=========> ValidarExistenciaExpRelacionadoRegistroAction - init <=======================");
        UsuarioValueObject usuarioVO = (UsuarioValueObject) request.getSession().getAttribute("usuario");

        String codExp = request.getParameter("codExp");  // Código del exp. relacionado a una anotación del registro de entrada
        log.debug("Código expediente relacionado: " + codExp);
    
        String[] params = usuarioVO.getParamsCon();

         // buscamos directiva para poder o no eliminar un tercero.
        Boolean permisos = UsuariosGruposManager.getInstance().tienePermisoDirectiva("BLOQUEAR_CREACION_TRAMITES_MANUALES", usuarioVO.getIdUsuario(), params);
        request.setAttribute("permisosDirectiva", permisos);
        m_Log.debug("BLOQUEAR_CREACION_TRAMITES_MANUALES : " + permisos);
        
        // SE CREA EL OBJETO ExpedienteRelacionadoVO
        ExpedienteRelacionadoVO expediente = new ExpedienteRelacionadoVO();
        expediente.setNumExpedienteRelacionado(codExp);
        expediente.setParams(params);
        expediente.setCodigoUsuario(Integer.toString(usuarioVO.getIdUsuario()));
        expediente.setCodigoOrganizacion(Integer.toString(usuarioVO.getOrgCod()));
        expediente.setCerrarConexionFlexia(true);
       
        PluginExpedientesRelacionados plugin = PluginExpedientesRelacionadosFactoria.getImplClass(Integer.toString(usuarioVO.getOrgCod()));
        List listadoParametros = PluginExpedientesRelacionadosFactoria.getListadoParametros(Integer.toString(usuarioVO.getOrgCod()));        
        Hashtable<String,String> valoresParametros = this.getListaCodigoValorParametros(request, listadoParametros);
        int codigoSalida = plugin.existeExpediente(expediente);
        boolean puedeVer = codigoSalida ==0||codigoSalida ==1||codigoSalida ==2;

        String codMunicipio = "";        
        if(puedeVer&& PluginExpedientesRelacionadosFactoria.recuperarCodigoMunicipioExpediente(Integer.toString(usuarioVO.getOrgCod()))) {
            // Se recupera el código de municipio del expediente relacionado si se ha configuración el plugin para ello
            codMunicipio = TramitacionManager.getInstance().getCodMunicipio(codExp,params);            
            valoresParametros.put("codMunicipio",codMunicipio);
        }

        
        String urlPuntoEntrada = PluginExpedientesRelacionadosFactoria.getURLPantallaPuntoEntrada(Integer.toString(usuarioVO.getOrgCod()));
        //String URL_DESTINO = this.rellenarURLconParametros(valoresParametros, urlPuntoEntrada,request.getContextPath());
        String URL_DESTINO    = plugin.cargar(valoresParametros, urlPuntoEntrada,request.getContextPath());
        log.debug("============> urlPuntoEntrada: " + urlPuntoEntrada);
        log.debug(" ============> La url de destino::  " + URL_DESTINO);
                
        if(puedeVer){
            // El usuario tiene permiso sobre el expediente relacionado => Se pasa el control a la ficha de expediente correspondiente para el plugin
            // Se recupera el código de municipio del expediente necesario para pasarlo a FichaExpediente
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("destino#" + URL_DESTINO);                    
        }else
        if(codigoSalida==4){
            // No existe el expediente
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("permiso#noexiste");
        }else
        if(codigoSalida==6){
            // El procedimiento al que pertenece el expediente está restringido y el usuario no tiene permiso para visualizarlo. Por tanto se le impide
            // su visualización
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("permiso#restringido");
        }
        else{
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("permiso#no");
        }
        return null;
      
  }


    private Hashtable<String,String> getListaCodigoValorParametros(HttpServletRequest request,List listadoParametros){
        Hashtable<String,String> valoresParametros = new Hashtable<String, String>();

        for(int i=0;i<listadoParametros.size();i++){
            String parametro = (String)listadoParametros.get(i);
            String valor         = request.getParameter(parametro);
            valoresParametros.put(parametro,valor);
        }

        return valoresParametros;
    }
   
}
