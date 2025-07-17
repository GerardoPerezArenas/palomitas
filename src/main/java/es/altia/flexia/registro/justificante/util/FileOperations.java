package es.altia.flexia.registro.justificante.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.Logger;

public class FileOperations {
    private static Logger log = Logger.getLogger(FileOperations.class);

    /**
     * Copia el contenido de un fichero de f1 a f2
     * @param f1: String con el nombe del fichero origen
     * @param f2: String con el nombre del fichero destino
     */
    public static void copy(String ficheroOrigen,String ficheroDestino){

        log.debug("copy ficheroOrigen: " + ficheroOrigen + ", ficheroDestino: " + ficheroDestino);
        if(ficheroOrigen!=null && ficheroDestino!=null){
            try {
                File inFile = new File(ficheroOrigen);
                File outFile = new File(ficheroDestino);

                FileInputStream in = new FileInputStream(inFile);
                FileOutputStream out = new FileOutputStream(outFile);

                int c;
                while( (c = in.read() ) != -1)
                    out.write(c);

                in.close();
                out.close();
            } catch(IOException e) {
                e.printStackTrace();
                log.error("Hubo un error de entrada/salida !!!");
            }

        }// if
    }

    
    public static boolean copy(InputStream is,String ficheroDestino){
        boolean exito = false;
        if(is!=null && ficheroDestino!=null){
            
            try {                
                File outFile = new File(ficheroDestino);
                FileOutputStream out = new FileOutputStream(outFile);

                int c;
                while( (c = is.read() ) != -1)
                    out.write(c);

                is.close();
                out.close();
                exito = true;
                
            } catch(IOException e) {
                log.error("Hubo un error de entrada/salida: " + e.getMessage());
                exito = false;
            } catch(Exception e){
                log.error("Hubo un error de entrada/salida: " + e.getMessage());
                exito = false;
            }

        }// if

        return exito;
    }
    
    
    
    /**
     * Almacena un array de bytes en una determinada ruta indicada por la variable path
     * @param path: String ruta en la que se aloja el fichero
     * @param content: Contenido binario del fichero
     * @throws Exception 
     */
    public static void writeFile(String path, byte[] content) throws Exception {

        FileOutputStream out;
        try {
                // Se comprueba si existe el fichero => En ese caso se elimina para 
                // sobreescribirlo
                File f = new File(path);
                if(f.exists()) f.delete();
            
                // Se almacena el fichero
                out = new FileOutputStream(path);                
                out.write(content);
                out.close();
                log.debug("FICHERO CREADO: " + path);
        } catch (FileNotFoundException fnfex) {
                log.error("ERROR FileOperations.writeFile:  " + fnfex.getMessage());
                throw fnfex;
        } catch (IOException ioex) {
                log.error("ERROR FileOperations.writeFile:  " + ioex.getMessage());
                throw ioex;
        }
    }
    
    
    /**
     * Comprueba si la ruta enviada es un directorio, y sino existe, lo crea
     * @param path: Ruta al directorio
     * @throws Exception 
     */
    public static boolean createDirectory(String path) throws Exception {

        boolean exito = false;
        try {
            File directorio = new File(path);
            if(!directorio.exists()){
                exito = directorio.mkdir();                
            }else exito = true;
                
        } catch (Exception e) {
            log.error("ERROR FileOperations.createDirectory: Error al crear un directorio: " + e.getMessage());
            e.printStackTrace();
            exito = false;
        }
        
        return exito;
    }
    
    
    
    
    /**
     * Lee el contenido de un fichero que se encuentra en una ruta determinada     
     * @param file: Objeto de tipo File          
     * @throws Exception: si ocurre algún error
     */
    public static byte[] readFile(File file) throws Exception {

        FileInputStream in;
        byte[] buffer;
        try {
            in = new FileInputStream(file);
            buffer = new byte[safeLongToInt(file.length())];
            in.read(buffer);
            in.close();	

        } catch (FileNotFoundException fnfex) {
            log.error("FileOperations.readFile() error al leer el contenido de un archivo: " + fnfex.getMessage());
            throw fnfex;
        } catch (IOException ioex) {
            log.error("FileOperations.readFile() error al leer el contenido de un archivo: " + ioex.getMessage());
            throw ioex;
        }
        return buffer;
    }

    
    public static boolean deleteFile(String path) throws Exception {

        boolean exito = false;
        
        try {
            File f = new File(path);
            if(f.exists()){
                f.delete();
                exito = true;
            }
            
        } catch (Exception ioex) {
            log.error("FileOperations.deleteFile() error al eliminar un archivo del disco del servidor: " + ioex.getMessage());
            throw ioex;
        }
        
        return exito;
    }
        
        
    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }
    
    
    /**
     * Devuelve la extensión de un archivo. 
     * @param fileName: Nombre del archivo. Puede incluir la ruta completa del archivo
     * @return String con la extensión o un null sino se ha podido recuperar
     */
    public static String getExtension(String fileName){
        String extension = null;
        try{
             int index = fileName.lastIndexOf(".");             
             extension = fileName.substring(index + 1);
             
        }catch(Exception e){
            e.printStackTrace();
        }        
        return extension;        
    }
    
    
    
    /**
     * Recupera el nombre del documento quitanto la extensión del mismo
     * @param fileName: Nombre del archivo. No debe incluir la ruta completa del archivo en disco, sólo el nombre y la extensión
     * @return String con el nombre del documento o null sino se ha podido recuperar
     */
    public static String getNombreArchivo(String fileName){
        String nombre = null;
        try{
             int index = fileName.lastIndexOf(".");             
             nombre = fileName.substring(0,index);
             
        }catch(Exception e){
            e.printStackTrace();
        }        
        return nombre;        
    }
    
}
