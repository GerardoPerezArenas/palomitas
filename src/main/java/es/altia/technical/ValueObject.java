package es.altia.technical;

/**
 * Interfaz usado para de finir el estado de los objectos de negocio (sus atributos).
 * Asimismo es el responsable de validar los valores de los atributos del objeto.
 * Es posible usarlo en la capa de negocio y en la capa cliente.
 * Supuestamente ha de ser mas compleja en el futuro, definiendo mas metodos.
 */
public interface ValueObject {
    public void validate(String idioma) throws ValidationException;

}
