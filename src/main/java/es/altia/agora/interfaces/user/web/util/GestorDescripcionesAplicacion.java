/*
 * GestorDescripcionesAplicacion.java
 *
 * Created on 20 de noviembre de 2002, 03:21 PM
 */

package es.altia.agora.interfaces.user.web.util;

import java.util.Hashtable;

/**
 *
 * @author  manuel
 * @version
 */
public class GestorDescripcionesAplicacion {

    private  static GestorDescripcionesAplicacion gestorDescripcionesAplicacion;
    private Hashtable descripcionesAplicacion;

    /** Creates new GestorDescripcionesAplicacion */
    private GestorDescripcionesAplicacion() {
       descripcionesAplicacion = new Hashtable();
       this.inicializarGestor();
    }


    private static synchronized void constructor(){
       gestorDescripcionesAplicacion = new GestorDescripcionesAplicacion();
    }

    /*****************************************************************
     *permite obtener una instancia de la clase GestorDescripcionesAplicacion
     *
     *
     */
    public static GestorDescripcionesAplicacion newInstance(){
         if(gestorDescripcionesAplicacion == null){
            constructor();

        }
        return gestorDescripcionesAplicacion;

    }

    /***********************************************************
     *Permite obtener un objeto que encapsula las descripciones de
     *las aplicaciones correspondientes
     *@apl_cod: identificador de la aplicacion
     *
     */
    public DescripcionesAplicacion obtenerAplicacion(int apl_cod, int idi_cod){
        DescripcionesAplicacion aplicacionActual = null;
        aplicacionActual = (DescripcionesAplicacion)descripcionesAplicacion.get(""+apl_cod);
        if(aplicacionActual == null){
            aplicacionActual=new DescripcionesAplicacion(apl_cod, idi_cod);
            descripcionesAplicacion.put(""+apl_cod,aplicacionActual);
        }
        else { // Comprobar el idioma.

           if (aplicacionActual.getIdi_cod() != idi_cod) {
			   // Cargar las descripciones de ese idioma.
			   aplicacionActual=new DescripcionesAplicacion(apl_cod, idi_cod);
			   descripcionesAplicacion.put(""+apl_cod,aplicacionActual);
		   }
   		}
        return aplicacionActual;
    }



    private void inicializarGestor(){

        descripcionesAplicacion.put("1"	,new DescripcionesAplicacion(1,1));

    }



}
