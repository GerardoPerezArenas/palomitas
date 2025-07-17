package es.altia.agora.interfaces.user.web.util;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

public class Descarga  {
  public Descarga(){
  }

  public void descargarFichero(HttpServletResponse response,String fichero){
    try{
      if(fichero!=null){
      File file = new File(fichero);
      FileInputStream fIS = new FileInputStream(file);
      BufferedReader bufferR = new BufferedReader(new InputStreamReader(fIS));
      String linea = new String();
      String nombre = fichero.substring(fichero.lastIndexOf("/") + 1,fichero.length());
      ServletOutputStream out = response.getOutputStream();
      response.setHeader ("Content-Disposition", "attachment;filename=\""+nombre+"\"");
      response.setHeader("Content-Transfer-Encoding","binary");
      //response.setContentType("text/plain");
	  //response.setContentType("APPLICATION/STREAM");
	  response.setContentType("text/stream");	  
      while((linea = bufferR.readLine())!=null){
        out.write(linea.getBytes());
        out.write("\n".getBytes());
      }
      fIS.close();
      out.close();
      file.delete();
      }
    }catch(Exception e){
      e.printStackTrace();
    }
  }
 
}