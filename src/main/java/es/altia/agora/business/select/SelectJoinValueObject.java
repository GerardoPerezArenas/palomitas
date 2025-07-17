package es.altia.agora.business.select;

import java.util.Vector;
import es.altia.technical.ValueObject;
import es.altia.technical.Messages;
import es.altia.technical.Message;
import es.altia.technical.ValidationException;
import java.io.Serializable;

public class SelectJoinValueObject implements Serializable, ValueObject {
    /** Construye una nueva Select por defecto. */
    public SelectJoinValueObject() {
        super();
    }

    /** Construye una nueva Select que devuelve una única descripcion, y cuyo estado esta definido en los parametros */
    public SelectJoinValueObject(String col_cod1_tabla1, String col_cod2_tabla1, String nom_tabla1, String col_cod_tabla2, String col_desc_tabla2, String nom_tabla2, String valor_where) {
            this.col_cod1_tabla1  = col_cod1_tabla1;
            this.col_cod2_tabla1 = col_cod2_tabla1;
            this.nom_tabla1 = nom_tabla1;
            this.col_cod_tabla2 = col_cod_tabla2;
            this.col_desc_tabla2 = col_desc_tabla2;
            this.nom_tabla2 = nom_tabla2;
            this.valor_where = valor_where;
    }


     public SelectJoinValueObject(String col_cod1_tabla1, String col_cod2_tabla1, String col_cod3_tabla1, String nom_tabla1, String col_cod_tabla2, String col_cod2_tabla2, String col_desc_tabla2, String nom_tabla2, String valor_where) {
            this.col_cod1_tabla1  = col_cod1_tabla1;
            this.col_cod2_tabla1 = col_cod2_tabla1;
            this.col_cod3_tabla1 = col_cod3_tabla1;
            this.nom_tabla1 = nom_tabla1;
            this.col_cod_tabla2 = col_cod_tabla2;
            this.col_cod2_tabla2 = col_cod2_tabla2;
            this.col_desc_tabla2 = col_desc_tabla2;
            this.nom_tabla2 = nom_tabla2;
            this.valor_where = valor_where;
    }


    public String getCol_cod1_tabla1() { return col_cod1_tabla1; }

    public void setCol_cod1_tabla1(String col_cod1_tabla1) { this.col_cod1_tabla1 = col_cod1_tabla1; }

    public String getCol_cod2_tabla1() { return col_cod2_tabla1; }

    public void setCol_cod2_tabla1(String col_cod2_tabla1) { this.col_cod2_tabla1 = col_cod2_tabla1; }

    public String getCol_cod3_tabla1() { return col_cod3_tabla1; }

    public void setCol_cod3_tabla1(String col_cod3_tabla1) { this.col_cod3_tabla1 = col_cod3_tabla1; }

    public String getNom_tabla1() { return nom_tabla1; }

    public void setNom_tabla1(String nom_tabla1) { this.nom_tabla1 = nom_tabla1; }

    public String getCol_cod_tabla2() { return col_cod_tabla2; }

    public void setCol_cod_tabla2(String col_cod_tabla2) { this.col_cod_tabla2 = col_cod_tabla2; }

    public String getCol_cod2_tabla2() { return col_cod2_tabla2; }

    public void setCol_cod2_tabla2(String col_cod2_tabla2) { this.col_cod2_tabla2 = col_cod2_tabla2; }

    public String getCol_desc_tabla2() { return col_desc_tabla2; }

    public void setCol_desc_tabla2(String col_desc_tabla2) { this.col_desc_tabla2 = col_desc_tabla2; }

    public String getNom_tabla2() { return nom_tabla2; }

    public void setNom_tabla2(String nom_tabla2) { this.nom_tabla2 = nom_tabla2; }

    public String getValor_where() { return valor_where; }

    public void setValor_where(String valor_where ) { this.valor_where = valor_where; }


    // Resultado de la select
    public Vector getLista_resultado_join() { return lista_resultado_join; }

    public void setLista_resultado_join(Vector lista_resultado_join) { this.lista_resultado_join = lista_resultado_join; }

    // Nombres de los Textbox
    public String getInput_cod_join() { return input_cod_join; }

    public void setInput_cod_join(String input_cod_join) { this.input_cod_join = input_cod_join; }

    public String getInput_desc_join() { return input_desc_join; }

    public void setInput_desc_join(String input_desc_join) { this.input_desc_join = input_desc_join; }


    public void copy(SelectJoinValueObject other) {
        this.col_cod1_tabla1  = other.col_cod1_tabla1;
        this.col_cod2_tabla1 = other.col_cod2_tabla1;
        this.nom_tabla1 = other.nom_tabla1;
        this.col_cod_tabla2 = other.col_cod_tabla2;
        this.col_desc_tabla2 = other.col_desc_tabla2;
        this.nom_tabla2 = other.nom_tabla2;
        this.valor_where = other.valor_where;
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

        if ((nom_tabla1 == null) || (nom_tabla1.length() < 1))
                errors.add(new Message("formulario", "select.error.nom_tabla.required", sufijo));
        if (!errors.empty())
            throw new ValidationException(errors);
        isValid = true;
    }

    /** Devuelve un booleano que representa si el estado de esta BolsaEmpleo es válido. */
    public boolean IsValid() { return isValid; }

    private String col_cod1_tabla1;
    private String col_cod2_tabla1;
    private String col_cod3_tabla1;
    private String nom_tabla1;
    private String col_cod_tabla2;
    private String col_cod2_tabla2;
    private String col_desc_tabla2;
    private String nom_tabla2;
    private String valor_where;
    private String input_cod_join;
    private String input_desc_join;
    private Vector lista_resultado_join;
       /** Variable booleana que indica si el estado de la instancia de BolsaEmpleo es válido o no */
    private boolean isValid;
}
