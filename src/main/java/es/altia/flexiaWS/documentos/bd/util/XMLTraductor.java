/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexiaWS.documentos.bd.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;



/**
 *
 * @author mjesus.lopez
 */
public class XMLTraductor {

    protected static Log m_Log = LogFactory.getLog(XMLTraductor.class.getName());
    
    public static HashMap traduccionXmlToHashMap (String xmlCampos){

        // Creamos el builder basado en SAX
        SAXBuilder builder = new SAXBuilder();
        HashMap <String, String> datos = new HashMap<String, String>();
         // m_Log.debug("\nxmlCampos: "+xmlCampos);
        String nombre ="";
        String texto="";
        try {
            // Construimos el arbol DOM a partir del fichero xml
            Document doc = builder.build(new StringReader(xmlCampos));
            // Obtenemos la etiqueta raíz
            Element raiz = doc.getRootElement();
             m_Log.debug("\nraiz: "+raiz.toString());
            // Recorremos los hijos de la etiqueta raíz
            List<Element> hijosRaiz = raiz.getChildren();
            for(Element hijo: hijosRaiz){
                 texto="";
                 // Obtenemos el atributo tit si lo hubiera
                String tit = hijo.getAttributeValue("tit");
                if(tit!=null){
                   m_Log.debug("\tit: "+tit);
                   texto=tit+"|";
                }
                 // Obtenemos el atributo tit si lo hubiera
                String tip = hijo.getAttributeValue("tip");
                if(tip!=null){
                   m_Log.debug("\tip: "+tip);
                   texto=texto+tip+"|";
                }

                // Obtenemos el nombre y su contenido de tipo texto
                nombre = hijo.getName();
                String valor = hijo.getValue();
                texto=texto+valor;
                datos.put(nombre, texto);

                //m_Log.debug("\nEtiqueta: "+nombre+". Texto: "+texto);
                
         }

        } catch (JDOMException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

     return datos;
    }


public static String traduccionHashMapToXml(HashMap hashDatos){
    // Vamos a crear un XML desde cero
    // Lo primero es crear el Document
    Document docNuevo = new Document();
    // Vamos a generar la etiqueta raiz
    Element root = new Element("suplementarios");
    String docStr =null;

    java.util.Iterator it = hashDatos.keySet().iterator();
    while(it.hasNext()){
        String cod = (String)it.next();
        String valor = (String)hashDatos.get(cod);
        StringTokenizer tokens = new StringTokenizer(valor,"|");
        int numTokes=tokens.countTokens();
        Element item1=new Element(cod);
        if (numTokes==1){
            item1.setText(valor);
        }else{//numTokes=3
            String[] datos=new String[numTokes];
            int i=0;
            while(tokens.hasMoreTokens()){
                String str=tokens.nextToken();
                datos[i]=str;
                i++;
            }
            item1.setAttribute("tit", datos[0]);
            item1.setAttribute("tip", datos[1]);
            item1.setText(datos[2]);
        }
        root.addContent(item1);
       
    }  //fin elementos tabla

    //Vamos a almacenarlo en un fichero y ademas lo sacaremos por pantalla
    try{
        Document doc=new Document(root);//Creamos el documento
        Format format = Format.getPrettyFormat();
        // Creamos el serializador con el formato deseado
        XMLOutputter xmloutputter = new XMLOutputter(format);
        // Serializamos el document parseado
         docStr = xmloutputter.outputString(doc);
        // Serializamos nuestro nuevo document
        String docNuevoStr = xmloutputter.outputString(docNuevo);

        
    }catch(Exception e){e.printStackTrace();}
    return docStr;

   
    
}

}

