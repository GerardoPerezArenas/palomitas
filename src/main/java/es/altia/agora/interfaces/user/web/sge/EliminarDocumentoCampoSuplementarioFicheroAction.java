package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.flexia.registro.justificante.util.FileOperations;
import es.altia.util.commons.StringOperations;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Este action recibe peticiones correspondiente a la eliminación de un documento
 * correspondiente a un campo suplementario, sea a nivel de expediente o de trámite.
 * 
 * Se utiliza para cambiar el estado del documento en la lista de estados que se 
 * mantiene en los ActionForm correspondientes
 * @author oscar
 * 
 */
public class EliminarDocumentoCampoSuplementarioFicheroAction extends ActionSession{

    private Logger log = Logger.getLogger(EliminarDocumentoCampoSuplementarioFicheroAction.class);
    
    public ActionForward performSession(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response) throws IOException {
        
        // Código del campo suplementario
        String codigoCampo  = request.getParameter("codigoCampo");
        // Parámetro que indica si el campo a eliminar este de expediente o de trámite
        String origen       = request.getParameter("origen");
        HttpSession session = null;
        String salida = "";
        
        if(StringOperations.stringNoNuloNoVacio(codigoCampo) && StringOperations.stringNoNuloNoVacio(origen)){
            
            session = request.getSession();
            
            if(origen.equals(ConstantesDatos.ORIGEN_ELIMINAR_DOCUMENTO_EXPEDIENTE)){
                // Se elimina un fichero de un campo suplementario de tipo fichero
                FichaExpedienteForm fichaExpedienteForm = (FichaExpedienteForm)session.getAttribute("FichaExpedienteForm");
                if(fichaExpedienteForm!=null){
                    // Se recupera el formulario FichaExpedienteForm de la sesión, para obtener
                    // la lista de estados
                    
                    GeneralValueObject estados = fichaExpedienteForm.getListaEstadoFicheros();
                    
                    Integer estadoAnterior=(Integer)estados.getAtributo(codigoCampo);
                    Integer estadoNuevo = null;
                    if(estadoAnterior!=null && estadoAnterior.equals(new Integer(ConstantesDatos.ESTADO_DOCUMENTO_NUEVO))){
                        // Si el estado de documento anteriormente era nuevo, y a ahora se pretende eliminar, es que no 
                        // se ha grabado en base de datos/gestor documental. Por tanto, se pone el estado VACIO y se elimina
                        // de disco, para que no se invoque al plugin, ya que si el plugin es de un gestor documental se produce
                        // un error al eliminar, ya que en dicho gestor no existe.                      
                        estadoNuevo = new Integer(ConstantesDatos.ESTADO_DOCUMENTO_VACIO);     
                        // Se procede a eliminar los ficheros nuevos de la anotación que residen en el disco del servidor
                        // ya que se trata de un documento con estado NUEVO. 
                        
                        GeneralValueObject rutas = fichaExpedienteForm.getListaRutaFicherosDisco();                        
                        String rutaFichero = (String)rutas.getAtributo(codigoCampo);
                        log.debug(" =================> El archivo a eliminar se encuentra en disco en la ruta " + rutaFichero);
                        if(rutaFichero!=null && !"".equals(rutaFichero)){
                            try{
                                FileOperations.deleteFile(rutaFichero);

                            }catch(Exception e){                        
                                m_Log.error("Error al borrar el archivo " + rutaFichero + ": " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                        
                    }else
                        estadoNuevo = new Integer(ConstantesDatos.ESTADO_DOCUMENTO_ELIMINADO);     
                    
                    estados.setAtributo(codigoCampo,estadoNuevo);                    
                    fichaExpedienteForm.setListaEstadoFicheros(estados);
                    
                     // Se recupera la lista de longitudes de los ficheros almacenados en el objeto TramitacionExpedientesForm. Sólo
                    // almacena los que se encuentra a nivel de trámite
                    GeneralValueObject longitudesFichero = fichaExpedienteForm.getListaLongitudFicherosDisco();
                    if(longitudesFichero!=null){
                        longitudesFichero.setAtributo(codigoCampo,new Integer(0));
                        fichaExpedienteForm.setListaLongitudFicherosDisco(longitudesFichero);
                    }
                    
                    salida = "exito=1";
                }                
            }else
            if(origen.equals(ConstantesDatos.ORIGEN_ELIMINAR_DOCUMENTO_TRAMITE)){    
                // Se elimina un fichero de un campo suplementario de tipo fichero
                TramitacionExpedientesForm tramForm = (TramitacionExpedientesForm)session.getAttribute("TramitacionExpedientesForm");
                FichaExpedienteForm fichaExpedienteForm = (FichaExpedienteForm)session.getAttribute("FichaExpedienteForm");
                
                if(tramForm!=null && fichaExpedienteForm!=null){
                    // Se recupera el formulario FichaExpedienteForm de la sesión, para obtener
                    // la lista de estados                    
                    GeneralValueObject estados = tramForm.getListaEstadoFicheros();
                    
                    // Se recupera la lista de longitudes de los ficheros almacenados en el objeto TramitacionExpedientesForm. Sólo
                    // almacena los que se encuentra a nivel de trámite
                    GeneralValueObject longitudesTramiteForm = tramForm.getListaLongitudFicheros();
                    if(longitudesTramiteForm!=null){
                        longitudesTramiteForm.setAtributo(codigoCampo,new Integer(0));
                        tramForm.setListaLongitudFicheros(longitudesTramiteForm);
                    }                    
                    
                    
                    // Se recupera lia lista con las longitudes de los ficheros que se mantienen en el objeto FichaExpedienteForm, que 
                    // contiene la lista de ficheros de campos a nivel de expediente más los de trámite
                    GeneralValueObject longitudesFicheroExpedienteForm = fichaExpedienteForm.getListaLongitudFicherosDisco();
                    if(longitudesFicheroExpedienteForm!=null){
                        longitudesFicheroExpedienteForm.setAtributo(codigoCampo,new Integer(0));
                        fichaExpedienteForm.setListaLongitudFicherosDisco(longitudesFicheroExpedienteForm);
                    }
                    
                    
                    Integer estadoAnterior = (Integer)estados.getAtributo(codigoCampo);
                    Integer estadoNuevo = null;
                    if(estadoAnterior!=null && estadoAnterior.equals(new Integer(ConstantesDatos.ESTADO_DOCUMENTO_NUEVO))){
                        // Si el estado de documento anteriormente era nuevo, y a ahora se pretende eliminar, es que no 
                        // se ha grabado en base de datos/gestor documental. Por tanto, se pone el estado VACIO y se elimina
                        // de disco, para que no se invoque al plugin, ya que si el plugin es de un gestor documental se produce
                        // un error al eliminar, ya que en dicho gestor no existe.                      
                        estadoNuevo = new Integer(ConstantesDatos.ESTADO_DOCUMENTO_VACIO);     
                        // Se procede a eliminar los ficheros nuevos de la anotación que residen en el disco del servidor
                        // ya que se trata de un documento con estado NUEVO. 
                        
                        GeneralValueObject rutas = tramForm.getListaRutaFicherosDisco();                        
                        String rutaFichero = (String)rutas.getAtributo(codigoCampo);
                        log.debug(" =================> El archivo a eliminar se encuentra en disco en la ruta " + rutaFichero);
                        if(rutaFichero!=null && !"".equals(rutaFichero)){
                            try{
                                FileOperations.deleteFile(rutaFichero);

                            }catch(Exception e){                        
                                m_Log.error("Error al borrar el archivo " + rutaFichero + ": " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                        
                    }else
                        estadoNuevo = new Integer(ConstantesDatos.ESTADO_DOCUMENTO_ELIMINADO);                        
                    
                    // Se cambia al estado ELIMINADO para el documento correspondiente a un determinado
                    // campo suplementario de tipo fichero, al que se hace referencia a través de la variable codigoCampo                    
                    estados.setAtributo(codigoCampo,estadoNuevo);                    
                    tramForm.setListaEstadoFicheros(estados);
                    
                    
                    /**
                     * ORIGINAL
                    // Se cambia al estado ELIMINADO para el documento correspondiente a un determinado
                    // campo suplementario de tipo fichero, al que se hace referencia a través de la variable codigoCampo
                    estados.setAtributo(codigoCampo,new Integer(ConstantesDatos.ESTADO_DOCUMENTO_ELIMINADO));                    
                    tramForm.setListaEstadoFicheros(estados);
                    
                    */ 
                    salida = "exito=1";
                }                                
            }
        }else
            salida = "exito=0";
        
        
        
        try{
            PrintWriter out = response.getWriter();                
            response.setContentType("text/html");            
            out.println(salida);  
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
            
        
        
        
        return null;
    }
    
}
