// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.administracion.mantenimiento;

// PAQUETES IMPORTADOS
import es.altia.agora.business.administracion.mantenimiento.persistence.EstilosManager;
import es.altia.agora.business.escritorio.*;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.helper.*;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.util.struts.StrutsFileValidation;
import es.altia.agora.business.util.*;
import java.io.*;
import java.text.NumberFormat;
import java.util.Vector;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;
import sun.misc.BASE64Decoder;


/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase ProcedimientosAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: ALTIA CONSULTORES & AYTOS CPD</p>
 * @author Jorge Hombre Tuñas
 * @version 1.0
 */
public class DocumentoCssAction extends ActionSession  {

  public ActionForward performSession(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {

    try
    {
  
    m_Log.debug("perform");
    ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));
    HttpSession session = request.getSession();

    // Validaremos los parametros del request especificados
    ActionErrors errors = new ActionErrors();
    String opcion = request.getParameter("opcion");
    if (m_Log.isInfoEnabled()) m_Log.info("CH   la opcion en el action es " + opcion);
    
    
    m_Log.debug("DocumentoRegistroAction - opcion: " + opcion);    
    if (form == null) {
      m_Log.debug("Rellenamos el form de fichero");
      form = new DocumentoCssForm();
      if ("request".equals(mapping.getScope())){
          m_Log.debug("El ambito es la request");
        request.setAttribute(mapping.getAttribute(), form);
      }else{
          m_Log.debug("El ámbito es la sesión");
          session.setAttribute(mapping.getAttribute(), form);
      }
    }
    //DocumentoCssForm fichaForm = (DocumentoCssForm)form;
      MantenimientosAdminForm fichaForm = (MantenimientosAdminForm)form;
    /////
    if(fichaForm!=null)
    {
        m_Log.debug("comprobación fichero <> formulario !=null");
        if(fichaForm.getFichero()!=null){
            m_Log.debug(">>>> comprobación fichero <> formulario.getFichero !=null");
            m_Log.debug(">>>> FileSize: " + fichaForm.getFichero().getFileSize());
        }
        else 
             m_Log.debug("comprobación fichero <> formulario.getFichero =null");
        
    } else  m_Log.debug("comprobación fichero <> formulario ==null");    
    /////   
    
    FormFile fichero = null;
    EstilosManager mantManager = EstilosManager.getInstance(); 
    if(fichaForm!=null && fichaForm.getFichero()!=null)
        fichero = fichaForm.getFichero();
        
    m_Log.debug("DocumentoRegistroAction - DocumentoRegistroForm es: " + fichaForm);
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    String[] params = usuario.getParamsCon();
    if("documentoAlta".equals(opcion))
    {     
        String descripcion=request.getParameter("descripcion");
        String titulo = fichaForm.getTituloFichero();
        String nomficheroSubir= fichero.getFileName();
         try{
              m_Log.debug("************   descripcion  "+descripcion); 
           
            byte[] ficheroSubir= fichero.getFileData();
            
            ServletContext sc = request.getSession().getServletContext(); 
            String path="css";
            m_Log.debug("************   nomficheroSubir "+nomficheroSubir);    
            String pathReal = sc.getRealPath("css") + File.separator+nomficheroSubir;
            m_Log.debug("************      Real path x "+pathReal);       
            FileOutputStream fos=new FileOutputStream(pathReal);
             m_Log.debug("************   tamaño  ficheroSubir "+fichero.getFileSize());    
            fos.write(ficheroSubir);
            fos.close();
            
             //GUARDAMOS EL DOCUMENTO EN LA BASE DE DATOS
             GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("descripcion",descripcion);
            String nombrefichero="/css/"+nomficheroSubir;
            gVO.setAtributo("nombreFichero",nombrefichero);
            
         
            Vector lista = mantManager.insertarCss(gVO,params);
            
            
            Vector lista2 = mantManager.getListaOrganizaciones(params);
            Vector listaCss = mantManager.getListaCss(params);
            fichaForm.setListaCss(listaCss);
            fichaForm.setListaOrganizaciones(lista2);
            
       
            for(int i=0;i<fichaForm.getListaCss().size();i++){
             m_Log.debug("************  listar "+fichaForm.getListaCss().elementAt(i));     
            }
            
            
         }catch (FileNotFoundException e) {
          //la excepción provendria de no encontrar original.txt
          // originada en la linea FileInputStream fis = new FileInputStream(inputFile);
          // java exige recoger la excepcion al usar este canal ( try{..} catch{..} )
          // el fichero de salida no genera excepción , ya que se va a crear o sobreescribir
            System.err.println("FileStreamsTest: " + e);

         }
           
              
          
         
      opcion = "vuelveCargar"; // Volvemos al formulario de entrada  
        
    } else if("descargarCss".equals(opcion)){
        
       // try{
           String cod=request.getParameter("cod");
            String desc=request.getParameter("descrCss");
             m_Log.debug("************  desc "+desc);
           ServletContext sc = request.getSession().getServletContext(); 
           String pathReal = sc.getRealPath("css");
           
           m_Log.debug("************  path "+pathReal);
           
      /*     String nomFile = desc;
            FileInputStream archivo = new FileInputStream("C:\\"+nomFile);
            int longitud = archivo.available();
            byte[] datos = new byte[longitud];
            archivo.read(datos);
            archivo.close();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition","attachment;filename="+nomFile);
            ServletOutputStream ouputStream = response.getOutputStream();
            ouputStream.write(datos);
            ouputStream.flush();
            ouputStream.close();
            }catch(Exception e){ e.printStackTrace(); } */

        
        
        
        
      //String codigo = request.getParameter("codigoDocumento");
      //fichaForm.setCodigo(codigo);
      //opcion = "documentoEliminar";
    }else if("salir".equals(opcion)){
      if ((session.getAttribute(mapping.getAttribute()) != null))
        session.removeAttribute(mapping.getAttribute());
    }else{
      opcion = mapping.getInput();
    }   
    
    m_Log.debug("perform");        
    
    
    // Cada vez que se excede el tamaño máximo del fichero a subir por POST el parámetro opcion
    // llega vacío
    //if(opcion==null && fichaForm!=null && (fichaForm.getFichero()==null || (fichaForm.getFichero()!=null && fichaForm.getFichero().getFileSize()==0))){
    if(opcion==null){
        // Se indica que hay error y se vuelve a la página que
        // permite dar de alta un nuevo documento
        //m_Log.debug("***********El fichero excede el limite maximo establecido");
        //int num = Math.round(StrutsFileValidation.TAM_MAX_FILE/ConstantesDatos.DIVISOR_BYTES);                   
        //request.setAttribute(ConstantesDatos.TAM_MAX_FILE_BYTE,num);
       // request.setAttribute("ERROR_FILESIZE_UPLOAD","si");
     //   opcion = "documentoNuevo";        
    }
            
    return (mapping.findForward(opcion));
  }
  catch(Exception e){
      // Si ocurre algún error
      e.printStackTrace();
      m_Log.error("Se ha producido el siguiente error: " + e.getMessage());
      return null;
  }
     
  }
}