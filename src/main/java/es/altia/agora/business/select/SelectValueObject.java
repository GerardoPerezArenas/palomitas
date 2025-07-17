package es.altia.agora.business.select;

import java.util.Vector;
import es.altia.technical.ValueObject;
import es.altia.technical.Messages;
import es.altia.technical.Message;
import es.altia.technical.ValidationException;
import java.io.Serializable;

public class SelectValueObject implements Serializable, ValueObject {
    /** Construye una nueva Select por defecto. */
    public SelectValueObject() {
        super();
    }

    /** Construye una nueva Select ...??? */
    public SelectValueObject(String col_cod, String col_desc_c, String nom_tabla, Vector c_v_where) {
            this.col_cod  = col_cod;
            this.col_desc_c = col_desc_c;
            this.nom_tabla = nom_tabla;
            this.col_valor_where = c_v_where;
    }


    /** Construye una nueva Select que devuelve una única descripcion, y cuyo estado esta definido en los parametros */
    public SelectValueObject(String col_cod, String col_desc_c, String nom_tabla, String col_where, String valor_where, String col_where2, String valor_where2) {
            this.col_cod  = col_cod;
            this.col_desc_c = col_desc_c;
            this.nom_tabla = nom_tabla;
            this.col_where = col_where;
            this.valor_where = valor_where;
            this.col_where2 = col_where2;
            this.valor_where2 = valor_where2;
    }

    /** Construye una nueva Select que devuelve las descripciones en 2 idiomas, y cuyo estado esta definido en los parametros */
    public SelectValueObject(String col_cod, String col_desc_c, String col_desc_e, String nom_tabla, String col_where, String valor_where, String col_where2, String valor_where2) {
            this.col_cod  = col_cod;
            this.col_desc_c = col_desc_c;
            this.col_desc_e = col_desc_e;
            this.nom_tabla = nom_tabla;
            this.col_where = col_where;
            this.valor_where = valor_where;
            this.col_where2 = col_where2;
            this.valor_where2 = valor_where2;
    }

    public SelectValueObject(String nom_tabla, Vector columnas, String col_where, String valor_where, String col_where2, String valor_where2) {
            this.columnas  = new Vector();
            this.columnas =columnas;
            this.nom_tabla = nom_tabla;
            this.col_where = col_where;
            this.valor_where = valor_where;
            this.col_where2 = col_where2;
            this.valor_where2 = valor_where2;
    }

    public String getCol_cod() { return col_cod; }

    public void setCol_cod(String col_cod) { this.col_cod = col_cod; }

    public String getCol_desc_c() { return col_desc_c; }

    public void setCol_desc_c(String col_desc_c) { this.col_desc_c = col_desc_c; }

    public String getCol_desc_e() { return col_desc_e; }

    public void setCol_desc_e(String col_desc_e) { this.col_desc_e = col_desc_e; }

    public String getNom_tabla() { return nom_tabla; }

    public void setNom_tabla(String nom_tabla) { this.nom_tabla = nom_tabla; }

    public String getCol_where() { return col_where; }

    public void setCol_where(String col_where) { this.col_where = col_where; }

    public String getValor_where() { return valor_where; }

    public void setValor_where(String valor_where) { this.valor_where = valor_where; }

    public String getCol_where2() { return col_where2; }

    public void setCol_where2(String col_where2) { this.col_where2 = col_where2; }

    public String getValor_where2() { return valor_where2; }

    public void setValor_where2(String valor_where2) { this.valor_where2 = valor_where2; }

    // Resultado de la select
    public Vector getLista_resultado() { return lista_resultado; }

    public void setLista_resultado(Vector lista_resultado) { this.lista_resultado = lista_resultado; }

    public String getValor_desc() { return valor_desc; }

    public void setValor_desc(String valor_desc) { this.valor_desc = valor_desc; }

    // Nombres de los Textbox
    public String getInput_cod() { return input_cod; }

    public void setInput_cod(String input_cod) { this.input_cod = input_cod; }

    public String getInput_desc() { return input_desc; }

    public void setInput_desc(String input_desc) { this.input_desc = input_desc; }

    public Vector getColumnas() { return columnas; }

    public void setColumnas(Vector columnas) { this.columnas = columnas; }

    public Vector getColumn_valor_where() { return col_valor_where; }

    public void setColumn_valor_where(Vector columnas) { this.col_valor_where = columnas; }

    public void setTarget1(String target) { this.target1 = target; }

    public String getTarget1() {
    		return this.target1;
    }

    public String getParteWhereCompleja() { return parteWhereCompleja; }
    public void setParteWhereCompleja(String condicion) { this.parteWhereCompleja = condicion; }

    public void copy(SelectValueObject other) {
        this.col_cod   = other.col_cod;
        this.col_desc_c  = other.col_desc_c;
        this.col_desc_e  = other.col_desc_e;
        this.nom_tabla = other.nom_tabla;
        this.col_where = other.col_where;
        this.valor_where = other.valor_where;
        this.col_where2 = other.col_where2;
        this.valor_where2 = other.valor_where2;
        this.lista_resultado = other.lista_resultado;
        this.valor_desc  = other.valor_desc;
        this.input_cod   = other.input_cod;
        this.input_desc  = other.input_desc;
        this.target1 = other.target1;
    }

    /**
     * Valida el estado de esta Select
     * Puede ser invocado desde la capa cliente o desde la capa de negocio
     * @exception ValidationException si el estado no es válido
     */
    public void validate(String idioma) throws ValidationException {
        String sufijo = "";
        if ("euskera".equals(idioma)) sufijo="_eu";
		boolean correcto = true;
        Messages errors = new Messages();

	if ((col_desc_c == null) || (col_desc_c.length() < 1))
                errors.add(new Message("formulario", "select.error.col_desc_c.required", sufijo));
        if ((nom_tabla == null) || (nom_tabla.length() < 1))
                errors.add(new Message("formulario", "select.error.nom_tabla.required", sufijo));
        if (!errors.empty())
            throw new ValidationException(errors);
        isValid = true;
    }

    /** Devuelve un booleano que representa si el estado de esta BolsaEmpleo es válido. */
    public boolean IsValid() { return isValid; }

    private String col_cod = "";
    private String col_desc_c = "";
    private String col_desc_e = "";
    private String nom_tabla = "";
    private String col_where = "";
    private String valor_where = "";
    private String col_where2 = "";
    private String valor_where2 = "";
    private Vector lista_resultado = null;
    private String valor_desc = "";
    private String input_cod = "";
    private String input_desc = "";
    private Vector columnas = null;
    private Vector col_valor_where= null;
    private String target1=null;
	private String parteWhereCompleja="";


    /** Variable booleana que indica si el estado de la instancia de BolsaEmpleo es válido o no */
    private boolean isValid;
}
