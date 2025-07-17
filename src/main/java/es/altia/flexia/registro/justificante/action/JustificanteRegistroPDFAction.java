package es.altia.flexia.registro.justificante.action;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.technical.ConstantesDatos;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Action encargado de cargar  un justificante de registro personalizado diseñado con Jasper Reports
 * y devolver la salida en PDF
 */
public class JustificanteRegistroPDFAction extends ActionSession{

    private Logger log = Logger.getLogger(JustificanteRegistroPDFAction.class);


    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {


        log.debug(this.getClass().getName() + " =================================>");
        String fichero = request.getParameter("fichero");
        log.debug("fichero: " + fichero);
        
        response.setContentType("application/pdf");
        
        String rutaFinal = null;
        String directorio = null;
        if(!fichero.contains(File.separator)){
        try{
            HttpSession session = request.getSession();
            UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");

            ResourceBundle configRegistro = ResourceBundle.getBundle("Registro");
            directorio = configRegistro.getString(usuario.getOrgCod() + ConstantesDatos.RUTA_PLANTILLAS_JUSTIFICANTE);

        }catch(Exception e){
            log.error("Error al rec " + e.getMessage());
        }

        if(directorio!=null && !"".equals(directorio)){

                rutaFinal = directorio + File.separator + fichero;
                
            }// if
        } else {
            rutaFinal = fichero;
            File file = new File(rutaFinal);
            fichero = file.getName();
        }
        
            File fDestino = new File(rutaFinal);
            if(fDestino.exists()){

                ServletOutputStream out = response.getOutputStream();
            	response.setContentType("application/pdf");
                response.setHeader("Content-Transfer-Encoding","binary");
                response.setHeader("Content-Disposition","inline;filename=\"" + fichero + "\"");

                FileInputStream fis = new FileInputStream(fDestino);
                byte buffer[] = new byte[2048];
                int bytesLeidos = -1;
                while(-1 != (bytesLeidos = fis.read(buffer, 0, buffer.length))){
                    out.write(buffer, 0, bytesLeidos);
                }
                               
                fis.close();
                out.close();
                out.flush();
                
            String sinBorrado = request.getParameter("sinBorrado");
            if(sinBorrado == null || sinBorrado.equals("off")){
                log.debug("Se requiere borrado de ficheros temporales");
                // Se borra el pdf junto con el fichero .jrprint y .pdf
                String[] datos = fichero.split("[.]");                                
                if(directorio!=null && datos!=null && datos.length==2){
                    borrarFicherosTemporales(directorio,datos[0]);
                }
            } else log.debug("No se requiere borrado de ficheros temporales");
        }// if

        return null;
    }    
    
    
    private void borrarFicherosTemporales(String directorio,String prefijoFichero){        
        final String EXTENSION_PDF = ".pdf";
        final String EXTENSION_JRPRINT = ".jrprint";
        
        try{
            
            // Se procede a eliminar el fichero en formato pdf
            String ruta_pdf = directorio + File.separator + prefijoFichero + EXTENSION_PDF;
            File pdf = new File(ruta_pdf);
            boolean pdf_borrado = pdf.delete();
            log.debug("El fichero " + ruta_pdf + ", ha sido eliminado? " + pdf_borrado);
            
            // Se procede a eliminar el fichero en formato jrprint
            String ruta_jrprint = directorio + File.separator + prefijoFichero + EXTENSION_JRPRINT;
            File jrprint = new File(ruta_jrprint);
            boolean jrprint_borrado = jrprint.delete();
            log.debug("El fichero " + ruta_jrprint + ", ha sido eliminado? " + jrprint_borrado);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
