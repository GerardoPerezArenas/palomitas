package es.altia.agora.business.administracion.mantenimiento.persistence.manual;

import java.io.Serializable;
import org.apache.commons.lang.StringEscapeUtils;

public class UORDTO implements Serializable
{
   
    private String uor_nom;
    private String uor_cod;
    private String uor_pad;
    private String uor_tipo;
    private String uor_fecha_alta;
    private String uor_fecha_baja;
    private String uor_ordenar;
    private String uor_estado;
    private String uorOculta;
    private String uor_cod_vis;
    private String uor_email;
    private char uor_no_registro;
    private String uor_cod_accede;
    private String uor_rexistro_xeral;
    private String uor_nom_pad;
    private boolean oficinaRegistro;
    private String jndi;

    public UORDTO() {
        
    }

    /**
     *
     * @param uor_cod Código del departamento
     * @param uor_pad Código del padre
     * @param uor_nom Nombre del departamento
     * @param uor_tipo Tipo
     * @param uor_fecha_alta Fecha de alta
     * @param uor_fecha_baja Fecha de baja
     * @param uor_estado Estado (Alta/Baja)
     * @param uor_cod_vis Código visible (el que se muestra en el árbol p/e)
     * @param uor_cod_accede Código visible de accede (si la constante del common.properties está a si)
     */
    

    public UORDTO(String uor_nom, String uor_pad, String uor_cod, String uor_tipo, String uor_fecha_alta,
                  String uor_fecha_baja, String uor_estado, String uor_cod_vis, char uor_no_registro, 
                  String uor_rexistro_xeral, String uorOculta) {
       
       this.uor_nom = uor_nom;
        this.uor_cod = uor_cod;
        this.uor_pad = uor_pad;
        
        this.uor_tipo = uor_tipo;
        this.uor_fecha_alta = uor_fecha_alta;
        this.uor_fecha_baja = uor_fecha_baja;
        this.uor_estado = uor_estado;
        this.uor_cod_vis = uor_cod_vis;
        this.uor_no_registro = uor_no_registro;
         this.uor_rexistro_xeral = uor_rexistro_xeral;
         this.uorOculta = uorOculta;
    }

    public String getUor_cod() {
        return uor_cod;
    }

    public void setUor_cod(String uor_cod) {
        this.uor_cod = uor_cod;
    }

    public String getUor_pad() {
        return uor_pad;
    }

    public void setUor_pad(String uor_pad) {
        this.uor_pad = uor_pad;
    }

    public String getUor_nom() {
        return uor_nom;
    }

    public void setUor_nom(String uor_nom) {
        this.uor_nom = uor_nom;
    }

    public String getUor_tipo() {
        return uor_tipo;
    }

    public void setUor_tipo(String uor_tipo) {
        this.uor_tipo = uor_tipo;
    }

    public String getUor_fecha_alta() {
        return uor_fecha_alta;
    }

    public void setUor_fecha_alta(String uor_fecha_alta) {
        this.uor_fecha_alta = uor_fecha_alta;
    }

    public String getUor_fecha_baja() {
        return uor_fecha_baja;
    }

    public void setUor_fecha_baja(String uor_fecha_baja) {
        this.uor_fecha_baja = uor_fecha_baja;
    }

    public String getUor_estado() {
        return uor_estado;
    }

    public void setUor_estado(String uor_estado) {
        this.uor_estado = uor_estado;
    }

    public String getUorOculta() {
        return uorOculta;
    }

    public void setUorOculta(String uorOculta) {
        this.uorOculta = uorOculta;
    }

    public String getUor_cod_vis() {
        return uor_cod_vis;
    }

    public void setUor_cod_vis(String uor_cod_vis) {
        this.uor_cod_vis = uor_cod_vis;
    }

    public String getUor_email() {
        return uor_email;
    }

    public void setUor_email(String uor_email) {
        this.uor_email = uor_email;
    }

    public char getUor_no_registro() {
        return uor_no_registro;
    }

    public void setUor_no_registro(char uor_no_registro) {
        this.uor_no_registro = uor_no_registro;
    }
    public String getUor_rexistro_xeral() {
        return uor_rexistro_xeral;
    }

    public void setUor_rexistro_xeral(String uor_rexistro_xeral) {
        this.uor_rexistro_xeral = uor_rexistro_xeral;
    }
    public String getUor_cod_accede() {
        return uor_cod_accede;
    }

    public void setUor_cod_accede(String uor_cod_accede) {
        this.uor_cod_accede = uor_cod_accede;
    }

    public void setUor_ordenar(String uor_ordenar) {
        this.uor_ordenar = uor_ordenar;
    }

   

    /**
     * Devuelve los atributos de este objeto como un String en formato CSV (valores separados por comas).
     * Si un valor es null, se inserta la cadena vacía ",'' ,"
     * @return Cadena con los valores separados por comas
     */
    public String toJavascriptArgs() {
        StringBuffer s = new StringBuffer("(");

        if(uor_cod != null) {
            s.append("'").append(uor_cod).append("'");
        }
        else {
            s.append("''");
        }

        if(uor_cod_vis != null) {
            s.append(",").append("'").append(uor_cod_vis).append("'");
        }
        else {
            s.append(",''");
        }

        if(uor_estado != null) {
            s.append(",'").append(uor_estado).append("'");
        }
        else {
            s.append(",''");
        }

        if(uor_fecha_alta != null) {
            s.append(",'").append(uor_fecha_alta).append("'");
        }
        else {
            s.append(",''");
        }

        if(uor_fecha_baja != null) {
            s.append(",'").append(uor_fecha_baja).append("'");
        }
        else {
            s.append(",''");
        }

        if(uor_nom != null) {
            s.append(",'").append(StringEscapeUtils.escapeJavaScript(uor_nom)).append("'");
        }
        else {
            s.append(",''");
        }

        if(uor_pad != null) {
            s.append(",'").append(uor_pad).append("'");
        }
        else {
            s.append(",''");
        }

        if(uor_tipo != null) {
            s.append(",'").append(uor_tipo).append("'");
        }
        else {
            s.append(",''");
        }

        if(uor_email != null) {
            s.append(",'").append(uor_email).append("'");
        }
        else {
            s.append(",''");
        }

        if((uor_no_registro == '0')||(uor_no_registro == '1')) {
            s.append(",'").append(uor_no_registro).append("'");
        }
        else { // el campo uor_no_registro es erróneo
            s.append(",'0'");
        }

        if(uor_cod_accede != null) {
            s.append(",").append("'").append(uor_cod_accede).append("'");
        }
        else {
            s.append(",''");
        }
          if(uor_rexistro_xeral != null) {
            s.append(",").append("'").append(uor_rexistro_xeral).append("'");
        }
        else {
            s.append(",''");
        }
          
        /**** PRUEBA ****/  
        if(oficinaRegistro)
            s.append(",").append("'").append("1").append("'");
        else
            s.append(",").append("'").append("0").append("'");
                    
          
          /**** PRUEBA ****/

        if ("N".equals(uorOculta) || "S".equals(uorOculta)) 
            s.append(",'").append(uorOculta).append("'");
        else 
            s.append(",'N'");

        s.append(")");

        return s.toString();
    }


    public boolean equals(Object obj) {
        if((obj == null)||(obj.getClass().equals(this.getClass()) == false)) {
            return false;
        }
        else {
            UORDTO dto = (UORDTO)obj;
            return (
                    (this.getUor_cod().equals(dto.getUor_cod())) &&
                    (this.getUor_cod_vis().equals(dto.getUor_cod_vis())) &&
                    //(this.getUor_cod_accede().equals(dto.getUor_cod_accede())) &&
                    (this.getUor_estado().equals(dto.getUor_estado())) &&
                    (this.getUor_fecha_alta().equals(dto.getUor_fecha_alta())) &&
                    (this.getUor_fecha_baja().equals(dto.getUor_fecha_baja())) &&
                    (this.getUor_nom().equals(dto.getUor_nom())) &&
                    (this.getUor_pad().equals(dto.getUor_pad())) &&
                    (this.getUor_tipo().equals(dto.getUor_tipo())) &&
                    (this.getUor_email().equals(dto.getUor_email())) &&
                    (this.getUor_no_registro() == dto.getUor_no_registro() &&
                    (this.isOficinaRegistro() == dto.isOficinaRegistro()))
            );
        }
    }

    public String toString() {
    	StringBuffer s = new StringBuffer();

    	s.append("[");
    	s.append(uor_nom);
    	s.append(", ");
    	s.append(uor_cod_vis);
    	s.append(", ");
    	s.append(uor_estado);
    	s.append(", ");
    	s.append(uor_fecha_alta);
    	s.append(", ");
    	s.append(uor_fecha_baja);
    	s.append(", ");
    	s.append(uor_cod);
    	s.append(", ");
    	s.append(uor_pad);
    	s.append(", ");
    	s.append(uor_tipo);
        s.append(", ");
        s.append(uor_email);
        s.append(", ");
        s.append(uor_no_registro);
        s.append(", ");
        s.append(uor_cod_accede);
        s.append(", ");
        s.append(uor_rexistro_xeral);
        s.append(", ");
        if(oficinaRegistro) s.append("1");
        else
            s.append("0");
        s.append(", ");
        s.append(uorOculta);
                
        s.append("]");

    	return s.toString();
    }

    public String getUor_nom_pad() {
        return uor_nom_pad;
    }

    public void setUor_nom_pad(String uor_nom_pad) {
        this.uor_nom_pad = uor_nom_pad;
    }

    /**
     * @return the oficinaRegistro
     */
    public boolean isOficinaRegistro() {
        return oficinaRegistro;
    }

    /**
     * @param oficinaRegistro the oficinaRegistro to set
     */
    public void setOficinaRegistro(boolean oficinaRegistro) {
        this.oficinaRegistro = oficinaRegistro;
    }

    public String getJndi() {
        return jndi;
    }

    public void setJndi(String jndi) {
        this.jndi = jndi;
    }
    
}