package es.altia.agora.business.registro.sir.vo;

/**
 * BD: SIR_LOCALIDADES
 * @author INGDGC
 *
 */
public class SirLocalidades {

    private String codEntidad;          //COD_ENTIDAD	VARCHAR2(2 CHAR)	Codigo Entidad....
    private String codProvincia;        //COD_PROVINCIA	VARCHAR2(2 CHAR)	Codigo de provinicia - Siempre dos caracteres rellenar 0 Izq
    private String codLocalidad;        //COD_LOCALIDAD	VARCHAR2(4 CHAR)	Codigo Localidad SIR  - 4 CARACTERES rellenar 0 Izq.
    private String denominacion;        //DENOMINACION	VARCHAR2(50 CHAR)	Nombre/Denominacion Localidad
    private String codIsla;             //COD_ISLA	VARCHAR2(2 CHAR)	Codigo Isla
    private String codLocalidadFlexia;  //COD_LOCALIDAD_FLEXIA	VARCHAR2(3 CHAR)	Codigo municipio en Flexia - Fexia es Entero, aqui 3 Caracteres, rellenar con 0 Izq.
    private String sufijoDir;                  //SUFIJO_DIR	VARCHAR2(1 CHAR)	Sufijo del codigo de Sir, ultimo digito del codigo de la localidad en sir, no existente en flexia


    public SirLocalidades(String codEntidad, String codProvincia, String codLocalidad, String denominacion, String codIsla, String codLocalidadFlexia, String sufijoDir) {
        this.codEntidad = codEntidad;
        this.codProvincia = codProvincia;
        this.codLocalidad = codLocalidad;
        this.denominacion = denominacion;
        this.codIsla = codIsla;
        this.codLocalidadFlexia = codLocalidadFlexia;
        this.sufijoDir = sufijoDir;
    }

    public String getCodEntidad() {
        return codEntidad;
    }

    public void setCodEntidad(String codEntidad) {
        this.codEntidad = codEntidad;
    }

    public String getCodProvincia() {
        return codProvincia;
    }

    public void setCodProvincia(String codProvincia) {
        this.codProvincia = codProvincia;
    }

    public String getCodLocalidad() {
        return codLocalidad;
    }

    public void setCodLocalidad(String codLocalidad) {
        this.codLocalidad = codLocalidad;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public String getCodIsla() {
        return codIsla;
    }

    public void setCodIsla(String codIsla) {
        this.codIsla = codIsla;
    }

    public String getCodLocalidadFlexia() {
        return codLocalidadFlexia;
    }

    public void setCodLocalidadFlexia(String codLocalidadFlexia) {
        this.codLocalidadFlexia = codLocalidadFlexia;
    }

    public String getSufijoDir() {
        return sufijoDir;
    }

    public void setSufijoDir(String sufijoDir) {
        this.sufijoDir = sufijoDir;
    }

    @Override
    public String toString() {
        return "SirLocalidades{" +
                "codEntidad='" + codEntidad + '\'' +
                ", codProvincia='" + codProvincia + '\'' +
                ", codLocalidad='" + codLocalidad + '\'' +
                ", denominacion='" + denominacion + '\'' +
                ", codIsla='" + codIsla + '\'' +
                ", codLocalidadFlexia='" + codLocalidadFlexia + '\'' +
                ", sufijoDir='" + sufijoDir + '\'' +
                '}';
    }

}
