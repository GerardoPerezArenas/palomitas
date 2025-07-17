package es.altia.flexia.registro.cargamasiva.lanbide.action;


import com.google.gson.Gson;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.flexia.registro.cargamasiva.lanbide.exception.MigracionSolicitudesAyudaException;
import es.altia.flexia.registro.cargamasiva.lanbide.persistence.MigracionSolicitudesAyudaManager;
import es.altia.flexia.registro.cargamasiva.lanbide.vo.MigracionSolicitudesAyudaRespuesta;
import es.altia.flexia.registro.cargamasiva.lanbide.vo.SolicitudVO;
import es.altia.util.ajax.respuesta.RespuestaAjaxUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public class MigracionSolicitudesAyudaAction extends DispatchAction{
	private Logger log = Logger.getLogger(MigracionSolicitudesAyudaAction.class);
	
	public ActionForward importarSolicitudes(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.info("MigracionSolicitudesAyudaAction.importarSolicitudes()");
		
		HttpSession session = request.getSession();
		UsuarioValueObject usuario = null;
		String[] params = null;
		List<SolicitudVO> solicitudes = null;
		MigracionSolicitudesAyudaRespuesta respuesta = new MigracionSolicitudesAyudaRespuesta();
		List<String> mensajesResultadoOperaciones = null;
                String codProc = request.getParameter("codProc");
                
		if(session.getAttribute("usuario")!=null && codProc != null && !"".equals(codProc.trim())){
			usuario = (UsuarioValueObject) session.getAttribute("usuario");
			params = usuario.getParamsCon();
			
			try {
				solicitudes = MigracionSolicitudesAyudaManager.getInstance().obtenerSolicitudes(params, codProc.trim());
				mensajesResultadoOperaciones = MigracionSolicitudesAyudaManager.getInstance().insercionRegistroFlexia(solicitudes, params, codProc.trim());
				
				respuesta.setCodResultado(0);
				respuesta.setMensajeResultado("OK");
				respuesta.setMensajes(mensajesResultadoOperaciones);
			} catch (MigracionSolicitudesAyudaException msae) {
				log.error("Ha ocurrido un error al llamar a la operación setRegistroES de WSRegistroES", msae);
				
				respuesta.setCodResultado(msae.getCodError());
				respuesta.setMensajeResultado(msae.getMensaje());
			}
			
			RespuestaAjaxUtils.retornarJSON(new Gson().toJson(respuesta), response);
			
			
		}
		
		return null;
	}
        
        public ActionForward cargarProcdimientos(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception{
            log.debug("MigracionSolicitudesAyudaAction.cargarProcdimientos()[INICIO] ");
            HttpSession session = request.getSession();
            
            List<GeneralValueObject> lista = null;
            
            //Se obtienen datos del usuario
            UsuarioValueObject usuario = null;
            if(session.getAttribute("usuario")!=null){
			usuario = (UsuarioValueObject) session.getAttribute("usuario");
            }     
                        
            if (usuario != null){
                
                //Se obtienen datos de conexion e idioma
                String[] params = usuario.getParamsCon();
                int idioma = usuario.getIdioma();
             
                //Se obtienen los tipos de procedimientos del fichero properties Expediente.properties
                ResourceBundle expProp = ResourceBundle.getBundle("Expediente");
                String todosProc = null;
                String propiedad = usuario.getOrgCod()+"/PROCEDIMIENTOS_PERMITIDOS_CARGA_MASIVA";
                String[] listaCodProcedimientos = null;

                try{
                    log.debug("Código de organización: "+usuario.getOrgCod());
                    todosProc = expProp.getString(propiedad);
                  }catch(Exception e) { 
                        log.error("No existe la propiedad "+propiedad+" en Expediente.properties");
                        GeneralValueObject objError = new GeneralValueObject();
                        objError.setAtributo("error", "No se h localizado la propiedad "+propiedad);
                        RespuestaAjaxUtils.retornarJSON(new Gson().toJson(objError), response);
                  }
                
                //Se genera el filtro el cual indica los tipos de procedimientos
                String tipoProcedimientos = "";

                if(todosProc!=null){
                  listaCodProcedimientos = todosProc.split(";");
                  for(int i=0;i<listaCodProcedimientos.length;i++){
                     tipoProcedimientos = tipoProcedimientos + "'" + listaCodProcedimientos[i] + "'";
                     if(listaCodProcedimientos.length - i>1){
                        tipoProcedimientos = tipoProcedimientos + ",";
                     }
                  }
                }
                
                log.debug("idioma vale: "+idioma + " y tipoProcedimientos vale: "+ tipoProcedimientos);
                
                //Se comprueba que existan tipos de procedimientos e idioma para realizar la consulta
                if (!"".equals(tipoProcedimientos) && idioma != 0) {
                    
                    try {
                        lista = new ArrayList<GeneralValueObject>(MigracionSolicitudesAyudaManager.getInstance().
                                obtenerTipoProcedimientos(params, tipoProcedimientos, idioma));
                        
                        if (lista!= null) {
                            RespuestaAjaxUtils.retornarJSON(new Gson().toJson(lista), response);
                        }

                    } catch (MigracionSolicitudesAyudaException msae) {
                        log.error("Ha ocurrido un error al obtener los procedimientos", msae);
                        GeneralValueObject objError = new GeneralValueObject();
                        objError.setAtributo("error", "Ha ocurrido un error al obtener los procedimientos");
                        RespuestaAjaxUtils.retornarJSON(new Gson().toJson(objError), response);
                    }
                    
                } else {
                    log.debug ("MigracionSolicitudesAyudaAction.cargarProcdimientos() No se ha obtenido idioma o tipo de procedimentos");
                }
            } else {
                 log.debug ("MigracionSolicitudesAyudaAction.cargarProcdimientos() No se ha obtenido datos usuario");                               
            }

            log.debug("MigracionSolicitudesAyudaAction.cargarProcdimientos() [FIN] ");
            
            return null;
            
        }
        
}
