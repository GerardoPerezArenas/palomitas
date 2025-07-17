package es.altia.agora.interfaces.user.web.registro;

import es.altia.agora.interfaces.user.web.util.ActionSession;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.registro.justificante.form.JustificanteRegistroPersonalizadoForm;
import es.altia.flexia.registro.justificante.persistence.bd.JustificanteRegistroPersonalizadoManager;
import es.altia.flexia.registro.justificante.vo.JustificanteRegistroPersonalizadoVO;
import es.altia.util.commons.ZipJustificanteRegistroUtilities;
import es.altia.util.conexion.BDException;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;	
import java.util.Map;
import java.util.ResourceBundle;
import org.apache.struts.upload.FormFile;

public final class MantJustificanteRegistroAction extends ActionSession {

    private Logger log = Logger.getLogger(MantJustificanteRegistroAction.class);
    private final String PANTALLA_JUSTIFICANTES = "inicio";
    private final String RECARGAR_PANTALLA_JUSTIFICANTES = "recargar";

    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        log.debug("perform");
        log.debug("================= MantJustificanteRegistroAction ======================>");
        
        String opcion = request.getParameter("opcion");
        String redireccion = "";

        HttpSession session = request.getSession();
        
         ResourceBundle configRegistro = ResourceBundle.getBundle("Registro");

        UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        String[] params = usuario.getParamsCon();
            log.debug("opcion= " + opcion);

        JustificanteRegistroPersonalizadoForm formJustificante = (JustificanteRegistroPersonalizadoForm)session.getAttribute("JustificanteRegistroPersonalizadoForm");
        ArrayList<GeneralValueObject> tiposInforme = getTiposJustificantes(usuario.getAppCod(),usuario.getIdioma(),"Justificante","Modelo");
        request.setAttribute("tiposJustif",tiposInforme);

         // Determina si se activa el modelo de peticion de respuesta	
        Boolean modeloPeticionRespuesta = false;	
        if (ConstantesDatos.SI.equalsIgnoreCase(configRegistro.getString(usuario.getOrgCod() + ConstantesDatos.MODELO_PETICION_RESPUESTA))) {	
            modeloPeticionRespuesta = true;	
        }
        
        request.setAttribute("modeloPeticionRespuesta", modeloPeticionRespuesta);
        
        if(opcion!=null && opcion.equals("inicio")){
            
            JustificanteRegistroPersonalizadoManager manager = JustificanteRegistroPersonalizadoManager.getInstance();
            ArrayList<JustificanteRegistroPersonalizadoVO> justificantes= null;
            ArrayList<JustificanteRegistroPersonalizadoVO> infJustif= new ArrayList<JustificanteRegistroPersonalizadoVO>();
            ArrayList<JustificanteRegistroPersonalizadoVO> infPeticion= new ArrayList<JustificanteRegistroPersonalizadoVO>();

            Map<String, Boolean> tipoJustificantes = new HashMap<String, Boolean>();
            try{
                 if (modeloPeticionRespuesta) {	
                    tipoJustificantes.put(ConstantesDatos.TIPO_JUSTIFICANTE_MODELO_PETICION_RESPUESTA, Boolean.TRUE);	
                } else {	
                    tipoJustificantes.put(ConstantesDatos.TIPO_JUSTIFICANTE_MODELO_PETICION_RESPUESTA, Boolean.FALSE);	
                }	
                	
                justificantes = manager.getJustificantes(params, tipoJustificantes);
                for(JustificanteRegistroPersonalizadoVO inf : justificantes){
                    if(inf.getTipoJustificante()==1) infPeticion.add(inf);
                    else infJustif.add(inf);
                }
                
                
                formJustificante.setJustificantes(infJustif);
                formJustificante.setModelos(infPeticion);
                
            }catch(BDException e){
                // No se ha podido recuperar una conexión a la BBDD
                request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_CONNECTION_GETJUSTIFICANTES);

            }catch(TechnicalException e){
                // Error técnico al recuperar los justificantes
                request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_TECNICO_EJECUCION_GETJUSTIFICANTES);
            }

            redireccion = PANTALLA_JUSTIFICANTES;
        }else
        if(opcion!=null && opcion.equals("activar")){
            log.debug("activar un justificante");
            String justificante = request.getParameter("justificante");
            String tipoJustif = request.getParameter("tipoJustif");
            if(justificante!=null && !"".equals(justificante)){
                JustificanteRegistroPersonalizadoVO just = new JustificanteRegistroPersonalizadoVO();
                just.setNombreJustificante(justificante);
                just.setTipoJustificante(Integer.parseInt(tipoJustif));
                /*
                  *     0 --> OK
                  *     1 --> No se ha podido marcar el justificante como activado
                  *     2 --> Error al obtener una conexión con la BBDD
                  *     3 --> Error técnico durante la ejecución de la operación
                  */
                int salida = JustificanteRegistroPersonalizadoManager.getInstance().activarJustificante(just, params);
                if(salida==0){
                    // Operación correcta
                    request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.JUSTIFICANTE_ACTIVADO_CORRECTAMENTE);
                    redireccion = RECARGAR_PANTALLA_JUSTIFICANTES;
                }else
                if(salida==1){
                    // No se ha podido marcar el justificante como activo o por defecto
                    request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.JUSTIFICANTE_ACTIVADO_INCORRECTAMENTE);
                    redireccion = PANTALLA_JUSTIFICANTES;
                }else
                if(salida==2){
                    // No se ha podido obtener una conexión a la BBDD                    
                    request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.JUSTIFICANTE_ACTIVADO_CONEXION_BD_INCORRECTA);
                    redireccion = PANTALLA_JUSTIFICANTES;
                }else
                if(salida==3){
                    // Error técnico durante la ejecución de la operación
                    request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_TECNICO_EJECUCION_ACTIVAR_JUSTIFICANTE);
                    redireccion = PANTALLA_JUSTIFICANTES;
                }else
                if(salida==4){
                    // Error técnico durante la ejecución de la operación
                    request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_EXISTE_JUSTIFICANTE_ACTIVADO);
                    redireccion = PANTALLA_JUSTIFICANTES;
                }


            }else{
                // Justificante a activar desconocido
                request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.JUSTIFICANTE_ACTIVAR_DESCONOCIDO);
                redireccion = PANTALLA_JUSTIFICANTES;
            }


        }else
        if(opcion!=null && opcion.equals("desactivar")){
            log.debug("desactivar un justificante");
            String justificante = request.getParameter("justificante");
            String tipoJustif = request.getParameter("tipoJustif");
            if(justificante!=null && !"".equals(justificante)){
                JustificanteRegistroPersonalizadoVO just = new JustificanteRegistroPersonalizadoVO();
                just.setNombreJustificante(justificante);
                just.setTipoJustificante(Integer.parseInt(tipoJustif));
               /**
                  * Operación encargada de marcar un justificante de registro personalizado como desactivado
                  * @param nombreJustificante: Nombre del justificante
                  * @param params: Parámetros de conexión a la BBDD
                  * @return int que puede tomar los siguientes valores:
                  *     0 --> OK
                  *     1 --> No se ha podido marcar el justificante como desactivado
                  *     2 --> Error al obtener una conexión con la BBDD
                  *     3 --> Error técnico durante la ejecución de la operación
                  */
                int salida = JustificanteRegistroPersonalizadoManager.getInstance().desactivarJustificante(just, params);
                if(salida==0){
                    // Operación correcta
                    request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.JUSTIFICANTE_DESACTIVADO_CORRECTAMENTE);
                    redireccion = RECARGAR_PANTALLA_JUSTIFICANTES;
                }else
                if(salida==1){
                    // No se ha podido marcar el justificante como desactivado
                    request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.JUSTIFICANTE_DESACTIVADO_INCORRECTAMENTE);
                    redireccion = PANTALLA_JUSTIFICANTES;
                }else
                if(salida==2){
                    // No se ha podido obtener una conexión a la BBDD
                    request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_CONNECTION_DESACTIVAR_JUSTIFICANTE);
                    redireccion = PANTALLA_JUSTIFICANTES;
                }else
                if(salida==3){
                    // Error técnico durante la ejecución de la operación
                    request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_TECNICO_EJECUCION_DESACTIVAR_JUSTIFICANTE);
                    redireccion = PANTALLA_JUSTIFICANTES;
                }


            }else{
                // Justificante a activar desconocido
                request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_JUSTIFICANTE_DESACTIVAR_DESCONOCIDO);
                redireccion = PANTALLA_JUSTIFICANTES;
            }


        }else
        if(opcion!=null && opcion.equals("eliminar")){
            // Se recupera el nombre del justificante a eliminar
            String justificante = request.getParameter("justificante");
            String tipoJustif = request.getParameter("tipoJustif");
            if(justificante!=null && !"".equals(justificante)){
                String directorioPlantillas = null;
                try{
                   
                    directorioPlantillas = configRegistro.getString(usuario.getOrgCod() + ConstantesDatos.RUTA_PLANTILLAS_JUSTIFICANTE);
                }catch(Exception e){
                    log.error("NO SE HA PODIDO RECUPERAR LA RUTA EN LA QUE SE ALOJAN LAS PLANTILLAS DE REGISTRO");
                }


                if(directorioPlantillas==null || "".equals(directorioPlantillas)){
                    request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_PROPIEDAD_RUTA_DIRECTORIO_JUSTIFICANTES);
                    redireccion = PANTALLA_JUSTIFICANTES;
                }else{


                    File dir = new File(directorioPlantillas);
                    if(dir==null || !dir.exists()){
                        log.debug("NO EXISTE EL DIRECTORIO QUE ALOJA LAS PLANTILLAS DE JUSTIFICANTE DE REGISTRO");
                        request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_ELIMINAR_DIRECTORIO_JUSTIFICANTE_DESCONOCIDO);
                        redireccion = PANTALLA_JUSTIFICANTES;
                     }else{
                        // EXISTE EL DIRECTORIO QUE ALOJA LAS PLANTILLAS DE REGISTRO

                        String rutaFichero = directorioPlantillas + File.separator + justificante + ConstantesDatos.EXTENSION_JUSTIFICANTE_REGISTRO_JASPER;
                        File fichero = new File(rutaFichero);
                        if(fichero==null || !fichero.exists()){
                            // El justificante no existe en disco => ERROR: SE INFORMA AL USUARIO
                            request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_ELIMINAR_JUSTIFICANTE_NO_EXISTE_DISCO);
                            redireccion = PANTALLA_JUSTIFICANTES;
                        }else{
                            // Existe el justificante en disco => Se procede a realizar la eliminación en BBDD primero y después en disco

                            JustificanteRegistroPersonalizadoVO just = new JustificanteRegistroPersonalizadoVO();
                            just.setNombreJustificante(justificante);
                            just.setExtensionJustificante(ConstantesDatos.EXTENSION_JUSTIFICANTE_REGISTRO_JASPER);
                            just.setTipoJustificante(Integer.parseInt(tipoJustif));
                            int salida = JustificanteRegistroPersonalizadoManager.getInstance().eliminarJustificante(just,directorioPlantillas, params);

                            if(salida==0){
                                // SI SE HA PODIDO ELIMINAR EL JUSTIFICANTE TANTO DE BBDD COMO DEL SERVIDOR
                                request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.JUSTIFICANTE_ELIMNADO_CORRECTAMENTE);
                                redireccion = RECARGAR_PANTALLA_JUSTIFICANTES;
                            }else
                            if(salida==1){
                                // ERROR AL ELIMINAR EL JUSTIFICANTE DE LA BBDD
                                request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_ELIMINAR_JUSTIFICANTE_BBDD);
                                redireccion = PANTALLA_JUSTIFICANTES;
                            }else
                            if(salida==2){
                                // ERROR AL ELIMINAR EL JUSTIFICANTE DEL SERVIDOR
                                request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_ELIMINAR_JUSTIFICANTE_SERVIDOR);
                                redireccion = PANTALLA_JUSTIFICANTES;
                            }else
                            if(salida==3){
                                // ERROR AL OBTENER UNA CONEXIÓN A LA BBDD A LA HORA DE ELIMINAR EL JUSTIFICANTE
                                request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_OBTENER_CONEXION_BBDD);
                                redireccion = PANTALLA_JUSTIFICANTES;
                            }else
                            if(salida==4){
                                // ERROR TÉCNICO DURANTE LA ELIMINACIÓN DEL JUSTIFICANTE
                                request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_TECNICO_EJECUCION_ELIMINAR_JUSTIFICANTE);
                                redireccion = PANTALLA_JUSTIFICANTES;
                            }
                        }
                     }// else
                }
            }else{
                request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_JUSTIFICANTE_ELIMINAR_DESCONOCIDO);
                redireccion = PANTALLA_JUSTIFICANTES;
            }


        }else
        /*
        if(opcion!=null && opcion.equals("alta")){
            log.debug(" =========> Alta justificante de registro");
            String descripcion = formJustificante.getDescripcionJustificante();
            FormFile fichero   = formJustificante.getFichero();
            
            if(fichero!=null && descripcion!=null && !"".equals(descripcion)){
                // Si se ha subido un fichero
                String nombreFichero = fichero.getFileName();
                
                if(nombreFichero!=null && !"".equals(nombreFichero)){
                    String extension = nombreFichero.substring( nombreFichero.lastIndexOf( "." ) );
                    String nombre    = nombreFichero.substring(0,nombreFichero.lastIndexOf( "." ));

                    if(extension!=null){

                        if(!ConstantesDatos.EXTENSION_JUSTIFICANTE_REGISTRO_JASPER.equalsIgnoreCase(extension)){
                           request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_JUSTIFICANTE_REGISTRO_EXTENSION_INCORRECTA);                           
                           redireccion = PANTALLA_JUSTIFICANTES;
                        }else{

                            String propiedad_tam_max = usuario.getOrgCod() + ConstantesDatos.TAM_MAX_JUSTIFICANTE_REGISTRO;
                            String valorTam_configRegistro = null;
                            try{
                                ResourceBundle configRegistro = ResourceBundle.getBundle("Registro");
                                valorTam_configRegistro = configRegistro.getString(propiedad_tam_max);

                            }catch(Exception e){
                                log.error("Error al recuperar la propiedad que contiene el tamaño máximo del justificante de registro a subir: " + e.getMessage());
                            }

                            if(valorTam_configRegistro==null || "".equals(valorTam_configRegistro)){
                                // NO SE HA PODIDO RECUPERAR EL TÁMAÑO MÁXIMO DEL JUSTICANTE DE REGISTRO DEL FICHERO DE CONFIGURACIÓN
                                request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_PROPIEDAD_TAM_MAX_JUSTIFICANTE);
                                redireccion = PANTALLA_JUSTIFICANTES;
                            }else{

                                int TAM_MAXIMO = Integer.parseInt(valorTam_configRegistro);
                                if(fichero.getFileSize()>TAM_MAXIMO){
                                    // SI EL TAMAÑO DEL FICHERO ES MAYOR QUE EL TAMAÑO MÁXIMO FIJADO POR CONFIGURACIÓN => ERROR Y SE INFORMA AL USUARIO
                                    request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_TAM_MAX_JUSTIFICANTE_EXCEDIDO);
                                    request.setAttribute("TAM_MAX_JUSTIFICANTE",Integer.toString(TAM_MAXIMO));
                                    redireccion = PANTALLA_JUSTIFICANTES;
                                }else{
                                    // EL TAMAÑO DEL JUSTIFICANTE ES CORRECTO => SE PROCEDE A DARLO DE ALTA
                                    
                                    JustificanteRegistroPersonalizadoVO justificante = new JustificanteRegistroPersonalizadoVO();
                                    justificante.setDescripcionJustificante(descripcion);
                                    justificante.setExtensionJustificante(extension);
                                    justificante.setNombreJustificante(nombre);
                                    justificante.setRutaDiscoJustificante(extension);
                                    justificante.setTamanhoJustificante(fichero.getFileSize());

                                String directorioPlantillas = null;
                                try{
                                    ResourceBundle configRegistro = ResourceBundle.getBundle("Registro");
                                    directorioPlantillas = configRegistro.getString(usuario.getOrgCod() + ConstantesDatos.RUTA_PLANTILLAS_JUSTIFICANTE);
                                }catch(Exception e){
                                    log.error("NO SE HA PODIDO RECUPERAR LA RUTA EN LA QUE SE ALOJAN LAS PLANTILLAS DE REGISTRO");
                                }

                                if(directorioPlantillas==null || "".equals(directorioPlantillas)){
                                    request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_PROPIEDAD_RUTA_DIRECTORIO_JUSTIFICANTES);                                    
                                    redireccion = PANTALLA_JUSTIFICANTES;                                    
                                }else{

                                    File directorio = new File(directorioPlantillas);
                                    if(directorio!=null && !directorio.exists()){
                                        log.debug("NO EXISTE EL DIRECTORIO PARA ALOJAR LAS PLANTILLAS DE JUSTIFICANTE DE REGISTRO");
                                        request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_NO_EXISTE_DIRECTORIO_PLANTILLAS_JUSTIFICANTE);
                                        redireccion = PANTALLA_JUSTIFICANTES;
                                    }else{

                                        boolean existeJustificante = false;
                                        try{
                                            existeJustificante = JustificanteRegistroPersonalizadoManager.getInstance().existeJustificante(nombre, params);

                                        }catch(BDException e){
                                            request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_CONNECTION_JUSTIFICANTE_REGISTRO);

                                        }catch(TechnicalException e){
                                            request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_TECNICO_ALTA_JUSTIFICANTE_REGISTRO);
                                        }

                                        if(existeJustificante){
                                            request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_BBDD_EXISTE_JUSTIFICANTE_REGISTRO);
                                            redireccion = PANTALLA_JUSTIFICANTES;
                                        }else{

                                            // NO EXISTE EN BASE DE DATOS OTRO FICHERO CON EL MISMO NOMBRE
                                            log.debug("EXISTE EL DIRECTORIO QUE ALOJA LAS PLANTILLAS DE JUSTIFICANTE DE REGISTRO");
                                            String ruta = directorio + File.separator + nombreFichero;
                                            log.debug("ruta: " + ruta);

                                            // Se recupera el inputStream del fichero recién subido
                                            InputStream is = fichero.getInputStream();
                                            boolean copiado = FileOperations.copy(is,ruta);
                                            if(copiado){
                                                // Se ha subido el fichero a su destino => Se procede a darlo de alta en base de datos
                                                justificante.setRutaDiscoJustificante(ruta);
                                                try{

                                                    boolean exito = JustificanteRegistroPersonalizadoManager.getInstance().altaJustificante(justificante, params);
                                                    if(!exito){
                                                        request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_TECNICO_ALTA_JUSTIFICANTE_REGISTRO);
                                                        redireccion = PANTALLA_JUSTIFICANTES;
                                                    }else{
                                                        request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.JUSTIFICANTE_ALTA_CORRECTAMENTE);
                                                        redireccion = RECARGAR_PANTALLA_JUSTIFICANTES;
                                                    }

                                                }catch(BDException e){
                                                    request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_CONNECTION_JUSTIFICANTE_REGISTRO);
                                                    redireccion = PANTALLA_JUSTIFICANTES;

                                                }catch(TechnicalException e){
                                                    request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_TECNICO_ALTA_JUSTIFICANTE_REGISTRO);
                                                    redireccion = PANTALLA_JUSTIFICANTES;
                                                }
                                            }// if
                                            else{
                                                request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_COPIAR_JUSTIFICANTE_REGISTRO);
                                                redireccion = PANTALLA_JUSTIFICANTES;
                                            }

                                        } //else

                                    }// else
                                 }

                                    
                                }
                            }

                  
                        }// else
                    }//if
                }// if
            }// if
            */
            if(opcion!=null && opcion.equals("alta")){
            log.debug(" =========> Alta justificante de registro en formato zip");
            String descripcion = formJustificante.getDescripcionJustificante();
            FormFile fichero   = formJustificante.getFichero();

            if(fichero!=null && descripcion!=null && !"".equals(descripcion)){
                // Si se ha subido un fichero
                String nombreFichero = fichero.getFileName();

                if(nombreFichero!=null && !"".equals(nombreFichero)){
                    String extension = nombreFichero.substring( nombreFichero.lastIndexOf( "." ) );
                    String nombre    = nombreFichero.substring(0,nombreFichero.lastIndexOf( "." ));

                    if(extension!=null){

                        if(!ConstantesDatos.EXTENSION_JUSTIFICANTE_REGISTRO_ZIP.equalsIgnoreCase(extension)){
                           request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_JUSTIFICANTE_REGISTRO_EXTENSION_INCORRECTA);
                           redireccion = PANTALLA_JUSTIFICANTES;
                        }else{

                            String propiedad_tam_max = usuario.getOrgCod() + ConstantesDatos.TAM_MAX_JUSTIFICANTE_REGISTRO;
                            String valorTam_configRegistro = null;
                            try{
                              
                                valorTam_configRegistro = configRegistro.getString(propiedad_tam_max);

                            }catch(Exception e){
                                log.error("Error al recuperar la propiedad que contiene el tamaño máximo del justificante de registro a subir: " + e.getMessage());
                            }

                            if(valorTam_configRegistro==null || "".equals(valorTam_configRegistro)){
                                // NO SE HA PODIDO RECUPERAR EL TÁMAÑO MÁXIMO DEL JUSTICANTE DE REGISTRO DEL FICHERO DE CONFIGURACIÓN
                                request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_PROPIEDAD_TAM_MAX_JUSTIFICANTE);
                                redireccion = PANTALLA_JUSTIFICANTES;
                            }else{

                                int TAM_MAXIMO = Integer.parseInt(valorTam_configRegistro);
                                if(fichero.getFileSize()>TAM_MAXIMO){
                                    // SI EL TAMAÑO DEL FICHERO ES MAYOR QUE EL TAMAÑO MÁXIMO FIJADO POR CONFIGURACIÓN => ERROR Y SE INFORMA AL USUARIO
                                    request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_TAM_MAX_JUSTIFICANTE_EXCEDIDO);
                                    request.setAttribute("TAM_MAX_JUSTIFICANTE",Integer.toString(TAM_MAXIMO));
                                    redireccion = PANTALLA_JUSTIFICANTES;
                                }else{

                                    // EL TAMAÑO DEL JUSTIFICANTE ES CORRECTO => SE PROCEDE A DARLO DE ALTA
                                    /*************************************************/
                                   

                                String directorioPlantillas = null;
                                try{
                                  
                                    directorioPlantillas = configRegistro.getString(usuario.getOrgCod() + ConstantesDatos.RUTA_PLANTILLAS_JUSTIFICANTE);
                                }catch(Exception e){
                                    log.error("NO SE HA PODIDO RECUPERAR LA RUTA EN LA QUE SE ALOJAN LAS PLANTILLAS DE REGISTRO");
                                }

                                if(directorioPlantillas==null || "".equals(directorioPlantillas)){
                                    request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_PROPIEDAD_RUTA_DIRECTORIO_JUSTIFICANTES);
                                    redireccion = PANTALLA_JUSTIFICANTES;
                                }else{

                                    File directorio = new File(directorioPlantillas);
                                    if(directorio!=null && !directorio.exists()){
                                        log.debug("NO EXISTE EL DIRECTORIO PARA ALOJAR LAS PLANTILLAS DE JUSTIFICANTE DE REGISTRO");
                                        request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_NO_EXISTE_DIRECTORIO_PLANTILLAS_JUSTIFICANTE);
                                        redireccion = PANTALLA_JUSTIFICANTES;
                                    }else{
                         
                                        long start = System.currentTimeMillis();
                                        
                                        File temporal = File.createTempFile("prueba" + start,"zip");
                                        log.debug(">>>>>>>>>>>>>>>>> FICHERO TEMPORAL: " + temporal.getPath());
                                        FileOutputStream fos = new FileOutputStream(temporal);
                                        fos.write(fichero.getFileData());
                                        fos.flush();
                                        fos.close();

                                        String origenZip = temporal.getPath();

                                        boolean hayErrores = false;
                                        if(!ZipJustificanteRegistroUtilities.validarFormatoZipJustificante(origenZip)){
                                            // El formato del zip es incorrecto
                                            request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.FORMATO_ZIP_JUSTIFICANTE_NO_VALIDO);
                                            redireccion = PANTALLA_JUSTIFICANTES;
                                            hayErrores = true;
                                        }else{
                                            log.debug("El fichero subido se trata de un fichero zip con el formato válido para justificantes de registro");
                                            try{                                                
                                                ArrayList<String> errores = ZipJustificanteRegistroUtilities.validarFicherosDestino(origenZip,directorioPlantillas);
                                                if(errores!=null && errores.size()>0){
                                                    StringBuffer sb = new StringBuffer();
                                                    for(int i=0;i<errores.size();i++){
                                                        sb.append(errores.get(i));
                                                        if(errores.size()-i>1)
                                                            sb.append(",</br>");
                                                    }
                                                    hayErrores = true;
                                                    request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.FICHEROS_JUSTIFICANTE_EXISTEN_DESTINO);
                                                    request.setAttribute("FICHEROS_EXISTENTES",sb.toString());
                                                    redireccion = PANTALLA_JUSTIFICANTES;
                                                }

                                            }catch(Exception e){
                                                request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_TECNICO_VALIDACION_FICHEROS_DIRECTORIO_DESTINO);
                                                redireccion = PANTALLA_JUSTIFICANTES;
                                            }                                            

                                        }// else


                                        if(!hayErrores){
                                            log.debug(" ====================> NO HAY ERRORES");
                                            
                                            try{
                                                JustificanteRegistroPersonalizadoVO justificante = ZipJustificanteRegistroUtilities.unzip(origenZip, directorioPlantillas);
                                                String rutaDiscoJasper = directorioPlantillas + File.separator + justificante.getNombreJustificante() + ".jasper";                                                
                                                String tipoJustif = request.getParameter("codTipoJustif");
                                                justificante.setDescripcionJustificante(descripcion);
                                                justificante.setRutaDiscoJustificante(rutaDiscoJasper);
                                                justificante.setTipoJustificante(Integer.parseInt(tipoJustif));
                                                
                                                try{

                                                    boolean exito = JustificanteRegistroPersonalizadoManager.getInstance().altaJustificante(justificante, params);
                                                    if(!exito){
                                                        request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_TECNICO_ALTA_JUSTIFICANTE_REGISTRO);
                                                        redireccion = PANTALLA_JUSTIFICANTES;
                                                    }else{
                                                        request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.JUSTIFICANTE_ALTA_CORRECTAMENTE);
                                                        redireccion = RECARGAR_PANTALLA_JUSTIFICANTES;
                                                    }

                                                }catch(BDException e){
                                                    request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_CONNECTION_JUSTIFICANTE_REGISTRO);
                                                    redireccion = PANTALLA_JUSTIFICANTES;

                                                }catch(TechnicalException e){
                                                    request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_TECNICO_ALTA_JUSTIFICANTE_REGISTRO);
                                                    redireccion = PANTALLA_JUSTIFICANTES;
                                                }

                                                
                                            }catch(Exception e){
                                                request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_TECNICO_DESCOMPRESION_ZIP);
                                                redireccion = PANTALLA_JUSTIFICANTES;
                                            }
                                            

                                        }
                                        


                                            /*

                                            // NO EXISTE EN BASE DE DATOS OTRO FICHERO CON EL MISMO NOMBRE
                                            log.debug("EXISTE EL DIRECTORIO QUE ALOJA LAS PLANTILLAS DE JUSTIFICANTE DE REGISTRO");
                                            String ruta = directorio + File.separator + nombreFichero;
                                            log.debug("ruta: " + ruta);

                                            // Se recupera el inputStream del fichero recién subido
                                            InputStream is = fichero.getInputStream();
                                            boolean copiado = FileOperations.copy(is,ruta);
                                            if(copiado){
                                                // Se ha subido el fichero a su destino => Se procede a darlo de alta en base de datos
                                                justificante.setRutaDiscoJustificante(ruta);
                                                try{

                                                    boolean exito = JustificanteRegistroPersonalizadoManager.getInstance().altaJustificante(justificante, params);
                                                    if(!exito){
                                                        request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_TECNICO_ALTA_JUSTIFICANTE_REGISTRO);
                                                        redireccion = PANTALLA_JUSTIFICANTES;
                                                    }else{
                                                        request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.JUSTIFICANTE_ALTA_CORRECTAMENTE);
                                                        redireccion = RECARGAR_PANTALLA_JUSTIFICANTES;
                                                    }

                                                }catch(BDException e){
                                                    request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_CONNECTION_JUSTIFICANTE_REGISTRO);
                                                    redireccion = PANTALLA_JUSTIFICANTES;

                                                }catch(TechnicalException e){
                                                    request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_TECNICO_ALTA_JUSTIFICANTE_REGISTRO);
                                                    redireccion = PANTALLA_JUSTIFICANTES;
                                                }
                                            }// if
                                            else{
                                                request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_COPIAR_JUSTIFICANTE_REGISTRO);
                                                redireccion = PANTALLA_JUSTIFICANTES;
                                            }
                                            */
                                        //} //else

                                    }// else
                                 }

                                    /****************************************************/
                                }
                            }


                        }// else
                    }//if
                }// if
            }// if

            else{
                // SE DESCONOCE EL NOMBRE DEL FICHERO Y SU DESCRIPCIÓN
                request.setAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE",ConstantesDatos.ERROR_ALTA_JUSTIFICANTE_DESCONOCIDO);
                redireccion = PANTALLA_JUSTIFICANTES;
            }

            
        }

        return mapping.findForward(redireccion);
    }
    
    private ArrayList<GeneralValueObject> getTiposJustificantes(int aplicacion, int idioma, String...tipos){
        ArrayList<GeneralValueObject> lista = new ArrayList<GeneralValueObject>();
        TraductorAplicacionBean descriptor = new TraductorAplicacionBean();
        descriptor.setApl_cod(aplicacion);
        descriptor.setIdi_cod(idioma);
        
        if(tipos.length>0){
            for(int i=0; i<tipos.length; i++){
                GeneralValueObject tipo = new GeneralValueObject();
                tipo.setAtributo("codigo", i);
                tipo.setAtributo("descripcion", descriptor.getDescripcion("etiq_TiposJustif"+tipos[i]));
                lista.add(tipo);
            }
        }
        return lista;
    }


}