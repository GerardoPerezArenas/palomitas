/*
 * DescripcionesAplicacion.java
 *
 * Created on 20 de noviembre de 2002, 03:22 PM
 */

package es.altia.agora.interfaces.user.web.util;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Hashtable;
import java.sql.ResultSet;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;

/**
 *
 * @author  manuel
 * @version
 */
public class DescripcionesAplicacion {


    private Hashtable descripcionesAG;
    private int apl_cod;
    private int idi_cod;
    private GestorConexion gestorConexion;
    private static final String nombreTabla="A_TEX";
    private static Log log =
            LogFactory.getLog(DescripcionesAplicacion.class.getName());

     /** Creates new DescripcionesAplicacion */
    public DescripcionesAplicacion(int apl_cod, int idi_cod) {

        this.descripcionesAG = new Hashtable();
        this.apl_cod = apl_cod;
        this.idi_cod = idi_cod;
        try{
           Config m_ConfigTech = ConfigServiceHelper.getConfig("techserver");
           String sJndi = m_ConfigTech.getString("CON.jndi");
           this.gestorConexion = new GestorConexion(sJndi);
           this.inicializar();
        }catch( Exception e){
            if(this.gestorConexion!=null)this.gestorConexion.desconectar();
            this.setDefaultValues();
        }
    }

    public int getIdi_cod(){
        return this.idi_cod;
    }


    public int getApl_cod(){
        return this.apl_cod;
    }


    public synchronized String getDescripcion(String txt_cod, int idi_cod){
       String descripcion = null;
       try{
          //if(idi_cod == null || txt_cod==null) return null;
          descripcion = (String)descripcionesAG.get(txt_cod);
          return descripcion;
       }catch(Exception e){
          gestorConexion.desconectar();
       }
       return null;
    }

    private void inicializar(){
       ResultSet descripciones;
       String cod="";
       String des="";
       descripciones= this.obtenerDescripciones();
       try{
          if(descripciones!=null){
             while(descripciones.next()){
                cod = descripciones.getString("TEX_COD");
                des = descripciones.getString("TEX_DES");
                descripcionesAG.put(cod.trim(),des.trim());
        }
        this.gestorConexion.desconectar();
        }else this.setDefaultValues();
        }catch(Exception e){
            setDefaultValues();
        }

    }


    private void setDefaultValues(){
       descripcionesAG.put("botonAceptar","Aceptar");
    }

    private ResultSet obtenerDescripciones(){
       String consulta="";
       ResultSet resultado=null;

       consulta = "select * " +
                  "  from "+ nombreTabla +
                  " where TEX_APL = " + this.apl_cod +
                  "   and TEX_IDI = " + this.idi_cod;
        if (log.isDebugEnabled()) log.debug("SQL: "+consulta);
       try{
          this.gestorConexion.conectar();
          resultado=this.gestorConexion.ejecutarQuery(consulta);
       }catch(Exception e){
          gestorConexion.desconectar();
       }
       return resultado;
    }

}
