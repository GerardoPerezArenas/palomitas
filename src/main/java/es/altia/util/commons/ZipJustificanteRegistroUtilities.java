package es.altia.util.commons;

import es.altia.flexia.registro.justificante.vo.JustificanteRegistroPersonalizadoVO;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class ZipJustificanteRegistroUtilities {

    private static int BUFFER_SIZE = 2048;

    /**
     * Descomprime un fichero zip en el directorio de destino que se le indique. Si en el
     * directorio destino ya existen los ficheros, los sobreescribe
     * @param zipOrigen: Fichero zip de origen
     * @param destino: Directorio de destino en el que se descomprime el fichero
     * @throws java.lang.Exception
     */
    public static JustificanteRegistroPersonalizadoVO unzip(String zipOrigen, String destino) throws Exception {
		BufferedOutputStream bos = null;
		FileInputStream fis = null;
		ZipInputStream zipis = null;
		FileOutputStream fos = null;
        String jasper = null;
        boolean existeZip = true;

        JustificanteRegistroPersonalizadoVO just = null;
                
        try{
            fis = new FileInputStream(zipOrigen);

        }catch(FileNotFoundException e){
            existeZip = false;
        }

		try {
            if(existeZip){

                zipis = new ZipInputStream(new BufferedInputStream(fis));

                ZipEntry entrada = null;
                while((entrada = zipis.getNextEntry())!=null){

                    String nombreEntrada = entrada.getName();
                    if(entrada.isDirectory()){
                        // Se comprueba si existe el directorio en destino y sino existe se crea
                        File directorio = new File(destino + File.separator + entrada.getName());
                        if(!directorio.exists() ){
                           directorio.mkdir();
                        }

                    }else{
                        String rutaDestino = destino  + File.separator + nombreEntrada;
                        String extension = nombreEntrada.substring(nombreEntrada.lastIndexOf("."));
                        if(".jasper".equals(extension))
                            jasper = nombreEntrada.substring(0,nombreEntrada.lastIndexOf("."));
                        
                        if(!nombreEntrada.contains("Thumbs.db")){
                            int len = 0;
                            byte[] buffer = new byte[BUFFER_SIZE];
                            fos = new FileOutputStream(rutaDestino);
                            bos = new BufferedOutputStream(fos, BUFFER_SIZE);

                            while  ((len = zipis.read(buffer, 0, BUFFER_SIZE)) != -1)
                                bos.write(buffer, 0, len);

                            bos.flush();
                        }

                    }// else
                }//if
			}
		} catch (Exception e) {
			throw e;
		} finally {

			if(bos!=null) bos.close();
			if(zipis!=null) zipis.close();
			if(fos!=null) fos.close();
			if(fis!=null) fis.close();
		}

        just = new JustificanteRegistroPersonalizadoVO();
        just.setNombreJustificante(jasper);
        just.setExtensionJustificante(".jasper");        
        return just;
	}



   /**
     * Comprueba si en los ficheros comprimidos en el fichero zip, existen en destino. Se supone que se trata de un fichero zip válido
     * @param zipOrigen: Fichero zip de origen
     * @param ArrayList<String>: Que contiene la lista de documentos que forman parte del zip que existen en el directorio
     * al que hace referencia el parámetro destino
     * @throws java.lang.Exception
     * @throws Exception
     */
    public static ArrayList<String> validarFicherosDestino(String zipOrigen, String destino) throws Exception {
		FileInputStream fis = null;
		ZipInputStream zipis = null;
        ArrayList<String> errores= new ArrayList<String>();

        boolean existeZip = true;
        try{
            fis = new FileInputStream(zipOrigen);

        }catch(FileNotFoundException e){
            existeZip = false;
        }

		try {
            if(existeZip){
                zipis = new ZipInputStream(new BufferedInputStream(fis));
                ZipEntry entrada = null;

                while((entrada = zipis.getNextEntry())!=null){
                    String nombreEntrada = entrada.getName();
                    if(!entrada.isDirectory()){

                        String rutaDestino = destino  + File.separator + nombreEntrada;
                        File fDestino = new File(rutaDestino);
                        if(fDestino.exists() && !rutaDestino.contains("Thumbs.db"))
                            errores.add(nombreEntrada);

                    }// else
                }//if
			}

		} catch (Exception e) {
			throw e;
		} finally {
			if(zipis!=null) zipis.close();
			if(fis!=null) fis.close();
		} // end try

        return errores;
	}


   

     /**
     * Valida que el zip contenga un formato correcto
     * @param input: InputStream con el contenido del fichero zip
     * @return True si es correcto o false en otro caso     
     * @throws descomprimirficheroszip.UnzipException
     */
    public static boolean validarFormatoZipJustificante(String zipOrigen) {
        boolean exito = false;
		FileInputStream fis = null;
		ZipInputStream zipis = null;
        int contadorJasper = 0;
        int contadorDirectorioImagenes = 0;
        int contadorCarpetas = 0;
        int contadorFicheros = 0;

       boolean existeZip = true;
        try{
            fis = new FileInputStream(zipOrigen);

        }catch(FileNotFoundException e){
            existeZip = false;
        }


        try {
            if(existeZip){
                zipis = new ZipInputStream(new BufferedInputStream(fis));
                ZipEntry entrada = null;

                while((entrada = zipis.getNextEntry())!=null){

                    String nombreEntrada = entrada.getName();
                    if(entrada.isDirectory()){
                        contadorCarpetas++;
                        if(nombreEntrada.equals("imagenes/")){
                            contadorDirectorioImagenes++;
                        }
                    }else{
                        String extension = nombreEntrada.substring(nombreEntrada.lastIndexOf("."));
                        if(!nombreEntrada.contains("imagenes/") && !".jasper".equals(extension)){
                            contadorFicheros++;
                        }else
                        if(".jasper".equals(extension)){
                            contadorJasper++;
                            contadorFicheros++;
                        }

                    }// else
                }// while

                if(contadorFicheros==1 && contadorJasper==1 && ((contadorCarpetas==1 && contadorDirectorioImagenes==1)
                        || (contadorCarpetas==0 && contadorDirectorioImagenes==0)))
                   exito= true;
            }
		} catch (Exception e) {
            exito = false;
			e.printStackTrace();
		} finally {
            try{                
                if(fis!=null) fis.close();
                if(zipis!=null) zipis.close();
            }catch(IOException e){
            }
		} // end try

        return exito;
	}

}