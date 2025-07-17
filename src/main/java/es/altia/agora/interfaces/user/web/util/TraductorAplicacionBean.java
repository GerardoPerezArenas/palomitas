/*
 * TraductorAplicacionBean.java
 *
 * Created on 20 de noviembre de 2002, 10:31 AM
 */

package es.altia.agora.interfaces.user.web.util;

/**
 *
 * @author  manuel
 * @version
 */
public class TraductorAplicacionBean {

    private GestorDescripcionesAplicacion gestorDescripcionesAplicacion;
    private int apl_cod;
    private int idi_cod;
    private String txt_cod;
    private String txt_des;


    /** Creates new TraductorAplicacionBean */
    public TraductorAplicacionBean() {
        this.idi_cod=1;
        this.apl_cod=1;
        this.gestorDescripcionesAplicacion = GestorDescripcionesAplicacion.newInstance();
    }


    /*****************************************************************
     *método que permite recuperar la descripción en un idioma
     *de cualquier item utilizado en una aplicacion
     *
     ****************************************************************/
    public String getDescripcion(){
       DescripcionesAplicacion descripcionesAplicacion;
       descripcionesAplicacion = gestorDescripcionesAplicacion.obtenerAplicacion(apl_cod,this.idi_cod);
       if(descripcionesAplicacion!=null){
          this.txt_des = descripcionesAplicacion.getDescripcion(this.txt_cod,this.idi_cod);
       }
       return this.txt_des;
    }


    public String getDescripcion(String txt_cod){
       return getDescripcion(this.apl_cod, this.idi_cod, txt_cod);
    }

    public String getDescripcion(int apl_cod, String txt_cod){
       return getDescripcion(apl_cod, this.idi_cod, txt_cod);
    }
    public String getDescripcion(int apl_cod, int idi_cod, String txt_cod) {
       DescripcionesAplicacion descripcionesAplicacion;
       descripcionesAplicacion = gestorDescripcionesAplicacion.obtenerAplicacion(apl_cod, idi_cod);
       if(descripcionesAplicacion != null){
          this.txt_des = descripcionesAplicacion.getDescripcion(txt_cod, idi_cod);
       }
       return this.txt_des;
    }

    public void setApl_cod(int apl_cod){
       this.apl_cod = apl_cod;
    }


    public int getApl_cod(){
       return this.apl_cod;
    }


    public void setTxt_cod(String txt_cod){
       this.txt_cod = txt_cod;
    }


    public String getTxt_cod(){
       return this.txt_cod;
    }


    public void setIdi_cod(int idi_cod){
         this.idi_cod = idi_cod;
    }


    public int getIdi_cod(){
        return this.idi_cod;
    }


}
