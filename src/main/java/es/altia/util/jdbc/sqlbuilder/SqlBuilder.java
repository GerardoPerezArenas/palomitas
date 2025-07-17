package es.altia.util.jdbc.sqlbuilder;

import es.altia.util.StringUtils;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * @version $\Revision$ $\Date$
 *
 * La intencion de esta clase es utilizarla en conjunto con PreparedStatement y
 * JdbcOperations, usando las wildcards "?" donde después se introduciran los
 * valores.
 *
 * Es prerrequisito para todos los metodos de esta clase que las entradas no
 * sean null.
 *
 * SqlBuilder expone, ademas de las constantes de operadores, otras constantes
 * que utiliza internamente para que pueda ser más flexible y facilitar mas la
 * construccion de consultas SQL.
 * 
 * SqlBuilder utiliza la clase {@link GestorBaseDatos} para definir el gestor 
 * que se esta utilizando en la ejecucion actual de Flexia. Dependiendo del 
 * gestor la generacion de la consulta puede variar en ciertos casos, estando
 * esto descrito en los JavaDocs de los metodos afectados.
 */
public class SqlBuilder {

	protected static final GestorBaseDatos gestor = GestorBaseDatos.getGestorBaseDatos();

	// Palabras clave
	public static final String SELECT = "SELECT ";
	public static final String DISTINCT = "DISTINCT ";
	public static final String DELETE = "DELETE ";
	public static final String INSERT_INTO = "INSERT INTO ";
	public static final String VALUES = " VALUES ";
	public static final String UPDATE = "UPDATE ";
	public static final String SET = " SET ";
	public static final String FROM = " FROM ";
	public static final String WHERE = " WHERE ";
	public static final String IN = " IN ";
    public static final String NOT_IN = " NOT IN ";
	public static final String AND = " AND ";
	public static final String OR = " OR ";
	public static final String JOIN = " JOIN ";
	public static final String INNER_JOIN = " INNER JOIN ";
	public static final String LEFT_JOIN = " LEFT JOIN ";
	public static final String RIGHT_JOIN = " RIGHT JOIN ";
	public static final String ON = " ON ";
	public static final String GROUP_BY = " GROUP BY ";
	public static final String ORDER_BY = " ORDER BY ";
	public static final String ASC = " ASC ";
	public static final String DESC = " DESC ";
	public static final String AS = " AS ";
	public static final String SYSDATE = (gestor == GestorBaseDatos.ORACLE) ? " SYSDATE " : " GETDATE() ";

	// Utilidades
	public static final String COMA = ", ";
	public static final String PARAMETRO = " ? ";
	public static final String ABRIR_PARENTESIS = " ( ";
	public static final String CERRAR_PARENTESIS = " ) ";
    public static final String TODO = " * ";
	
	// Operaciones
	public static final String COALESCE = " COALESCE ";
	public static final String COUNT = " COUNT ";
	public static final String MAX = " MAX ";
	public static final String NEXTVAL = ".NEXTVAL";		// Oracle
	public static final String TO_TIMESTAMP = "TO_TIMESTAMP";
	public static final String TO_CHAR = "TO_CHAR";

	// Operadores
	public static final String COMODIN = "%";
	public static final String DISTINTO = " <> ";
	public static final String IGUAL = " = ";
	public static final String LIKE = " LIKE ";
	public static final String MAYOR_IGUAL = " >= ";
	public static final String MAYOR_QUE = " > ";
	public static final String MENOR_IGUAL = " <= ";
	public static final String MENOR_QUE = " > ";
	public static final String SUMA = " + ";
	public static final String TODOS = " * ";

	private StringBuilder consulta = new StringBuilder();
	
	public SqlBuilder() {
	}

	/**
	 * Comienza una consulta con la cadena provista.
	 * 
	 * @param inicio Cadena con la que comenzar la consulta. No debe ser nula.
	 */
	public SqlBuilder(String inicio) {
		consulta = new StringBuilder(inicio);
	}

	/**
	 * Utiliza un {@link SqlBuilder} para copiar su consulta a una nueva instancia.
	 * 
	 * @param sqlBuilder {@link SqlBuilder} cuya consulta se va a copiar.
	 */
	public SqlBuilder(SqlBuilder sqlBuilder) {
		consulta = new StringBuilder(sqlBuilder.toString());
	}

	private GestorBaseDatos getGestorBaseDatos() {
		return gestor;
	}
	
	/**
	 * Permite añadir un número n de campos a la sentencia select. Es decir, una
	 * llamada tal que sql.select(campoA).select(campoB) siendo el primer select
	 * la primera vez que se llama crearía la cadena "SELECT campoA, campoB".
	 * Por lo tanto, es posible concatenar selecciones, por ejemplo, en bucles.
	 * Añade la palabra clave SELECT al principio o sólo añade los campos
	 * dependiendo del tamaño del String actual del SqlBuilder, por lo que hay
	 * que ser consciente de no hacer algo como
	 * sql.select(campoA).from(tablaA).select(campoA) ya que la cadena creada
	 * sería "SELECT campoA FROM tablaA, campoB". En caso de que campos venga
	 * vacío o null creará la cadena "SELECT * ".
	 *
	 *
	 * @param campos
	 * @return El propio SqlBuilder
	 */
	public SqlBuilder select(String... campos) {
		if ((campos == null) || (campos.length == 0)) {
			campos = new String[]{TODOS};
		}
		return iniciarOSeguirConsulta(SELECT, COMA).anadirCampos(campos);
	}

	/**
	 * Inicia o reinicia la consulta empezando por "SELECT DISTINCT campo1,
	 * campo2, ... , campoN".
	 *
	 * @param campos
	 * @return
	 */
	public SqlBuilder selectDistinct(String... campos) {
		return iniciarConsulta(SELECT).append(DISTINCT).anadirCampos(campos);
	}

	/**
	 * Añade " COUNT (campo1, campo2, ... , campoN) " a la cadena si está vacía
	 * o tiene sólo "SELECT " y ", COUNT(campo1, campo2, ... , campoN) " en otro
	 * caso.
	 *
	 * @param campos
	 * @return
	 */
	public static String count(String... campos) {
		return String.format("%s%s%s%s", COUNT, ABRIR_PARENTESIS, anadirArgumentos(campos), CERRAR_PARENTESIS);
	}

	/**
	 * Añade " COUNT (campo1, campo2, ... , campoN) AS alias" a la cadena si
	 * está vacía o tiene sólo "SELECT " y ", COUNT(campo1, campo2, ... ,
	 * campoN) AS alias" en otro caso.
	 *
	 * @param alias
	 * @param campos
	 * @return
	 */
	public static String countAs(String alias, String... campos) {
		return String.format("%s%s%s", count(campos), AS, alias);
	}

	/**
	 * Añade " COUNT (*) " a la cadena si está vacía o tiene sólo "SELECT " y ",
	 * COUNT(*) " en otro caso.
	 *
	 * @return
	 */
	public static String countAll() {
		return count(TODO);
	}

	/**
	 * Añade " COUNT (*) AS alias" a la cadena si está vacía o tiene sólo
	 * "SELECT " y ", COUNT(*) AS alias" en otro caso.
	 *
	 * @param alias
	 * @return
	 */
	public static String countAllAs(String alias) {
		return String.format("%s%s%s", count(TODO), AS, alias);
	}

	/**
	 * Añade " FROM tabla1, tabla2, ... ,tablaN" a la consulta.
	 *
	 * @param tablas
	 * @return El propio SqlBuilder
	 */
	public SqlBuilder from(String... tablas) {
		return this.append(FROM).anadirCampos(tablas);
	}

	/**
	 * Añade " AS alias" a la consulta.
	 *
	 * @param alias
	 * @return
	 */
	public SqlBuilder as(String alias) {
		return this.append(AS).append(alias);
	}

	/**
	 * Anexa la cláusula where con una condición provista como la comparación
	 * del campoA y campoB usando el operador designado. Se proveen unas
	 * constantes de operadores con la clase para mayor comodidad.
	 *
	 * Un ejemplo de uso sería sqlBuilder.where("A", SqlBuilder.MAYOR_QUE, "B")
	 * que se traduciría en " WHERE A > B"
	 *
	 * @param campoA
	 * @param operador
	 * @param campoB
	 * @return El propio SqlBuilder
	 */
	public SqlBuilder where(String campoA, String operador, String campoB) {
		return anadirComparacion(WHERE, campoA, operador, campoB);
	}

	/**
	 * Anexa la clausula where de la forma " WHERE campoA LIKE 'campoB%'."
	 * 
	 * @param campoA
	 * @param campoB
	 * @return 
	 */
	public SqlBuilder whereLikeFinal(String campoA, String campoB) {
		return anadirComparacion(WHERE, campoA, LIKE, String.format("'%s%s'", campoB, COMODIN));
	}

	/**
	 * Anexa la clausula where de la forma " WHERE campoA LIKE '%campoB'."
	 *
	 * @param campoA
	 * @param campoB
	 * @return
	 */
	public SqlBuilder whereLikeInicial(String campoA, String campoB) {
		return anadirComparacion(WHERE, campoA, LIKE, String.format("%s%s", COMODIN, campoB));
	}

	/**
	 * Anexa la clausula where de la forma " WHERE campoA LIKE '%campoB%'."
	 *
	 * @param campoA
	 * @param campoB
	 * @return
	 */
	public SqlBuilder whereLikeInicialFinal(String campoA, String campoB) {
		return anadirComparacion(WHERE, campoA, LIKE, String.format("%s%s%s", COMODIN, campoB, COMODIN));
	}

	/**
	 * Anexa la cláusula where con una condición provista como la comparación
	 * del campoA y campoB usando el operador de igualdad. Se proveen unas
	 * constantes de operadores con la clase para mayor comodidad.
	 *
	 * Un ejemplo de uso sería sqlBuilder.where("A", SqlBuilder.MAYOR_QUE, "B")
	 * que se traduciría en " WHERE A > B"
	 *
	 * @param campoA
	 * @param campoB
	 * @return El propio SqlBuilder
	 */
	public SqlBuilder whereEquals(String campoA, String campoB) {
		return anadirComparacion(WHERE, campoA, IGUAL, campoB);
	}

	/**
	 * Anexa la cláusula where con una condición provista como la comparación
	 * del campoA y campoB usando el operador de igualdad. Se proveen unas
	 * constantes de operadores con la clase para mayor comodidad.
	 *
	 * Un ejemplo de uso sería sqlBuilder.where("A", SqlBuilder.MAYOR_QUE, "B")
	 * que se traduciría en " WHERE A > B"
	 *
	 * @param campo
	 * @param subconsulta
	 * @return El propio SqlBuilder
	 */
	public SqlBuilder whereEquals(String campo, SqlBuilder subconsulta) {
		return anadirComparacion(WHERE, campo, IGUAL, prepararSubconsulta(subconsulta));
	}

	/**
	 * Anexa la cláusula where con una condición provista como la comparación
	 * del campoA y un comodín usando el operador designado. Se proveen unas
	 * constantes de operadores con la clase para mayor comodidad.
	 *
	 * Un ejemplo de uso sería sqlBuilder.where("A", SqlBuilder.MAYOR_QUE, "B")
	 * que se traduciría en " WHERE A > B"
	 *
	 * @param campoA
	 * @param operador
	 * @return El propio SqlBuilder
	 */
	public SqlBuilder whereParametrizado(String campoA, String operador) {
		return anadirComparacion(WHERE, campoA, operador, PARAMETRO);
	}

	/**
	 * Anexa la cláusula where con una condición provista como la comparación
	 * del campoA y un comodín usando el operador de igualdad. Se proveen unas
	 * constantes de operadores con la clase para mayor comodidad.
	 *
	 * Un ejemplo de uso sería sqlBuilder.where("A", SqlBuilder.MAYOR_QUE, "B")
	 * que se traduciría en " WHERE A > B"
	 *
	 * @param campoA
	 * @return El propio SqlBuilder
	 */
	public SqlBuilder whereEqualsParametrizado(String campoA) {
		return anadirComparacion(WHERE, campoA, IGUAL, PARAMETRO);
    }
	
	/**
	 * Anexa la cláusula where con una condición provista como la comparación de los campos y un comodín usando el 
	 * operador de igualdad. Se proveen unas constantes de operadores con la clase para mayor comodidad.
	 *
	 * Un ejemplo de uso sería sqlBuilder.where("A") que se traduciría en " WHERE campo1 = ? AND campo2 = ? ... AND
	 * campoN = ? "
	 * 
	 * @param campos
	 * @return 
	 */
	public SqlBuilder whereEqualsParametrizado(String... campos) {
		String sufijo = String.format("%s%s", IGUAL, PARAMETRO);
		return this.whereEqualsParametrizado(campos[0]).append(AND)
				.anadirCamposConSeparadorYSufijo(sufijo, AND, Arrays.copyOfRange(campos, 1, campos.length));
	}

	/**
	 * Añade " WHERE campo IN ('valor1', 'valor2', ... , 'valorN')" a la
	 * sentencia que se esté construyendo.
	 *
	 * @param campo Campo que comparar a los valores del IN.
	 * @param valores Valores a introducir en el IN.
	 * @return El propio SqlBuilder
	 */
	public SqlBuilder whereIn(String campo, String... valores) {
		return whereIn(campo, Arrays.asList(valores));
	}

	/**
	 * 
	 * Añade " WHERE campo NOT IN ( subconsulta )" a la sentencia que se esté construyendo.
	 * 
	 * @param campo
	 * @param subconsulta
	 * @return 
	 */
	public SqlBuilder whereNotIn(String campo, SqlBuilder subconsulta) {
		return this.where(campo, NOT_IN, prepararSubconsulta(subconsulta));
	}
	
	/**
	 * Añade " WHERE campo IN ('valor1', 'valor2', ... , 'valorN')" a la
	 * sentencia que se esté construyendo.
	 *
	 * @param campo
	 * @param valores
	 * @return El propio SqlBuilder
	 */
	public SqlBuilder whereIn(String campo, List<String> valores) {

		int numParametros = valores.size();
		this.append(WHERE).append(campo).append(IN).abrir();
		for (int i = 0; i < numParametros; i++) {
			this.append(String.format("'%s'", valores.get(i)));
			if (i < numParametros - 1) {
				this.append(COMA);
			}
		}
		this.cerrar();
		return this;
	}
	
	/**
	 * 
	 * Añade " WHERE campo IN ( subconsulta )" a la sentencia que se esté construyendo.
	 * 
	 * @param campo
	 * @param subconsulta
	 * @return 
	 */
	public SqlBuilder whereIn(String campo, SqlBuilder subconsulta) {
		return this.where(campo, IN, prepararSubconsulta(subconsulta));
	}

	/**
	 * Añade " WHERE campo IN (?1, ?2, ... , ?numParametros)" a la sentencia que
	 * se esté construyendo.
	 *
	 * @param campo
	 * @param numParametros
	 * @return El propio SqlBuilder
	 */
	public SqlBuilder whereInParametrizado(String campo, int numParametros) {

		this.append(WHERE).append(campo).append(IN).abrir();
		for (int i = 0; i < numParametros; i++) {
			this.append(PARAMETRO);
			if (i < numParametros - 1) {
				this.append(COMA);
			}
		}

		this.cerrar();

		return this;

	}

	/**
	 * Añade " WHERE campo IN (?1, ?2, ... , ?valores.size())" a la sentencia
	 * que se esté construyendo. Los valores deben de ser de un tamaño mayor que
	 * 0. De otro modo quedará " WHERE campo IN ()".
	 *
	 * @param campo
	 * @param valores
	 * @return El propio SqlBuilder
	 */
	public SqlBuilder whereInParametrizado(String campo, List<?> valores) {
		int numParametros = valores.size();
		this.append(WHERE).append(campo).append(IN).abrir();
		for (int i = 0; i < numParametros; i++) {
			this.append(PARAMETRO);
			if (i < numParametros - 1) {
				this.append(COMA);
			}
		}

		this.cerrar();

		return this;

	}

	/**
	 * Añade " WHERE campo IN (?1, ?2, ... , ?valores.length)" a la sentencia
	 * que se esté construyendo. Los valores deben de ser de un tamaño mayor que
	 * 0. De otro modo quedará " WHERE campo IN ()".
	 *
	 * @param campo
	 * @param valores
	 * @return El propio SqlBuilder
	 */
	public SqlBuilder whereInParametrizado(String campo, Object... valores) {
		return whereInParametrizado(campo, Arrays.asList(valores));
	}

	/**
	 * Añade una cláusula AND COMPARACION, por ejemplo, " AND A > B". Se puede
	 * anexar, por ejemplo, a una cláusula JOIN o WHERE.
	 *
	 * @param campoA
	 * @param operador
	 * @param campoB
	 * @return El propio SqlBuilder
	 */
	public SqlBuilder and(String campoA, String operador, String campoB) {
		return anadirComparacion(AND, campoA, operador, campoB);
	}

	/**
	 * Añade una cláusula AND COMPARACION, por ejemplo, " AND A = B". Se puede
	 * anexar, por ejemplo, a una cláusula JOIN o WHERE.
	 *
	 * @param campoA
	 * @param campoB
	 * @return El propio SqlBuilder
	 */
	public SqlBuilder andEquals(String campoA, String campoB) {
		return anadirComparacion(AND, campoA, IGUAL, campoB);
	}

	/**
	 * Añade una cláusula " AND campo = (subconsulta)". Se puede anexar, por
	 * ejemplo, a una cláusula JOIN o WHERE.
	 *
	 * @param campo
	 * @param subconsulta
	 * @return
	 */
	public SqlBuilder andEquals(String campo, SqlBuilder subconsulta) {
		return anadirComparacion(AND, campo, IGUAL, prepararSubconsulta(subconsulta));
	}

	/**
	 * Añade una cláusula AND COMPARACION, por ejemplo, " AND A > ?". Se puede
	 * anexar, por ejemplo, a una cláusula JOIN o WHERE.
	 *
	 * @param campoA
	 * @param operador
	 * @return El propio SqlBuilder
	 */
	public SqlBuilder andParametrizado(String campoA, String operador) {
		return anadirComparacion(AND, campoA, operador, PARAMETRO);
	}

	/**
	 * Añade una cláusula AND COMPARACION, por ejemplo, " AND A = ?". Se puede
	 * anexar, por ejemplo, a una cláusula JOIN o WHERE.
	 *
	 * @param campoA
	 * @return El propio SqlBuilder
	 */
	public SqlBuilder andEqualsParametrizado(String campoA) {
		return anadirComparacion(AND, campoA, IGUAL, PARAMETRO);
	}

	/**
	 * Añade una cláusula OR COMPARACION, por ejemplo, " AND A > B". Se puede
	 * anexar, por ejemplo, a una cláusula JOIN o WHERE.
	 *
	 * @param campoA
	 * @param operador
	 * @param campoB
	 * @return El propio SqlBuilder
	 */
	public SqlBuilder or(String campoA, String operador, String campoB) {
		return anadirComparacion(OR, campoA, operador, campoB);
	}

	/**
	 * Añade una cláusula OR COMPARACION, por ejemplo, " AND A = B". Se puede
	 * anexar, por ejemplo, a una cláusula JOIN o WHERE.
	 *
	 * @param campoA
	 * @param campoB
	 * @return El propio SqlBuilder
	 */
	public SqlBuilder orEquals(String campoA, String campoB) {
		return anadirComparacion(OR, campoA, IGUAL, campoB);
	}

	/**
	 * Añade una cláusula OR COMPARACION, por ejemplo, " AND A > ?". Se puede
	 * anexar, por ejemplo, a una cláusula JOIN o WHERE.
	 *
	 * @param campoA
	 * @param operador
	 * @return El propio SqlBuilder
	 */
	public SqlBuilder orParametrizado(String campoA, String operador) {
		return anadirComparacion(OR, campoA, operador, PARAMETRO);
	}

	/**
	 * Añade una cláusula OR COMPARACION, por ejemplo, " AND A = ?". Se puede
	 * anexar, por ejemplo, a una cláusula JOIN o WHERE.
	 *
	 * @param campoA
	 * @return El propio SqlBuilder
	 */
	public SqlBuilder orEqualsParametrizado(String campoA) {
		return anadirComparacion(OR, campoA, IGUAL, PARAMETRO);
	}

	/**
	 * Añade una cláusula INNER JOIN a la consulta tal que " INNER JOIN tabla ON
	 * campoA operador camopB".
	 *
	 * @param tabla
	 * @param campoA
	 * @param operador
	 * @param campoB
	 * @return El propio SqlBuilder
	 */
	public SqlBuilder innerJoin(String tabla, String campoA, String operador, String campoB) {
		return join(INNER_JOIN, tabla, campoA, operador, campoB);
	}

	/**
	 * Añade una cláusula INNER JOIN a la consulta tal que " INNER JOIN tabla ON
	 * campoA = camopB".
	 *
	 * @param tabla
	 * @param campoA
	 * @param campoB
	 * @return El propio SqlBuilder
	 */
	public SqlBuilder innerEquiJoin(String tabla, String campoA, String campoB) {
		return join(INNER_JOIN, tabla, campoA, IGUAL, campoB);
	}

	/**
	 * Añade una cláusula LEFT JOIN a la consulta tal que " LEFT JOIN tabla ON
	 * campoA operador camopB".
	 *
	 * @param tabla
	 * @param campoA
	 * @param operador
	 * @param campoB
	 * @return El propio SqlBuilder
	 */
	public SqlBuilder leftJoin(String tabla, String campoA, String operador, String campoB) {
		return join(LEFT_JOIN, tabla, campoA, operador, campoB);
	}

	/**
	 * Añade una cláusula LEFT JOIN a la consulta tal que " LEFT JOIN tabla ON
	 * campoA = camopB".
	 *
	 * @param tabla
	 * @param campoA
	 * @param campoB
	 * @return El propio SqlBuilder
	 */
	public SqlBuilder leftEquiJoin(String tabla, String campoA, String campoB) {
		return join(LEFT_JOIN, tabla, campoA, IGUAL, campoB);
	}

	/**
	 * Añade una cláusula RIGHT JOIN a la consulta tal que " RIGHT JOIN tabla ON
	 * campoA operador camopB".
	 *
	 * @param tabla
	 * @param campoA
	 * @param operador
	 * @param campoB
	 * @return El propio SqlBuilder
	 */
	public SqlBuilder rightJoin(String tabla, String campoA, String operador, String campoB) {
		return join(RIGHT_JOIN, tabla, campoA, operador, campoB);
	}

	/**
	 * Añade una cláusula RIGHT JOIN a la consulta tal que " RIGHT JOIN tabla ON
	 * campoA = camopB".
	 *
	 * @param tabla
	 * @param campoA
	 * @param campoB
	 * @return El propio SqlBuilder
	 */
	public SqlBuilder rightJoin(String tabla, String campoA, String campoB) {
		return join(RIGHT_JOIN, tabla, campoA, IGUAL, campoB);
	}

	/**
	 * Añade la cláusula " GROUP BY campo1, campo2, ... , campoN" a la consulta.
	 *
	 * @param campos
	 * @return
	 */
	public SqlBuilder groupBy(String... campos) {
		return this.append(GROUP_BY).anadirCampos(campos);
	}

	/**
	 * Añade a la cláusula " ORDER BY campo1, campo2, ... , campoN" a la
	 * consulta.
	 *
	 * @param campos
	 * @return
	 */
	public SqlBuilder orderBy(String... campos) {
		return this.append(ORDER_BY).anadirCampos(campos);
	}
	
	/**
	 * Devuelve el {@link String} formado " campo ASC "
	 *
	 * @param campo
	 * @return
	 */
	public static String asc(String campo) {
		return String.format(" %s%s",campo, ASC);
	}

	/**
	 * Devuelve el {@link String} formado " campo DESC "
	 *
	 * @param campo
	 * @return
	 */
	public static String desc(String campo) {
		return String.format("%s%s",campo, DESC);
	}

	/**
	 * Es posible, como con StringBuilder, añadir cadenas utilizando append. No
	 * tiene la misma funcionalidad que StringBuilder dado que sólo acepta un
	 * parámetro String... .
	 *
	 * @param cadenas
	 * @return El propio SqlBuilder
	 */
	public SqlBuilder append(String... cadenas) {
		for (String cadena : cadenas) {
			consulta.append(cadena);
		}
		return this;
	}

	/**
	 * Devuelve "tabla.atributo" independientemente de si el parametro tabla termina con un punto o no.
	 *
	 * @param tabla
	 * @param atributo
	 * @return El propio SqlBuilder
	 */
	public static String columnaConTabla(String tabla, String atributo) {
		if (StringUtils.endsWith(tabla, ".")) {
			return String.format("%s%s", tabla, atributo);
		} else {
			return String.format("%s.%s", tabla, atributo);
		}
	}

	/**
	 * Devuelve "atributo AS alias".
	 *
	 * @param atributo
	 * @param alias
	 * @return El propio SqlBuilder
	 */
	public static String columnaConAlias(String atributo, String alias) {
		return String.format("%s%s%s", atributo, AS, alias);
	}

	/**
	 * Devuelve "tabla alias".
	 *
	 * @param tabla
	 * @param alias
	 * @return
	 */
	public static String tablaConAlias(String tabla, String alias) {
		return String.format("%s %s", tabla, alias);
	}

	/**
	 * Reinicia la consulta como "DELETE ".
	 *
	 * @return El propio SqlBuilder
	 */
	public SqlBuilder delete() {
		return iniciarConsulta(DELETE);
	}

	/**
	 * Prepara una consulta "INSERT INTO tabla (campo1, campo2, ... , campoN)
	 * VALUES (?1, ?2, ... , ?N) ".
	 *
	 * @param tabla
	 * @param campos
	 * @return
	 */
	public SqlBuilder insertIntoParametrizado(String tabla, List<String> campos) {
		iniciarConsulta(INSERT_INTO).append(tabla).abrir();
		StringBuilder values = new StringBuilder(VALUES).append(ABRIR_PARENTESIS);

		int restante = campos.size();
		for (String campo : campos) {
			this.append(campo);
			values.append(PARAMETRO);
			if (restante > 1) {
				this.append(COMA);
				values.append(COMA);
				restante--;
			}
		}
		values.append(CERRAR_PARENTESIS);
		this.cerrar().append(values.toString());

		return this;

	}

	/**
	 * Prepara la siguiente consulta:
	 * <ul>
	 * <li><b>ORACLE:</b> "INSERT INTO tabla (PK, campo1, campo2, ... ,  campoN) VALUES (nombreSecuencia.NEXTVAL, ?1,
	 * ?2, ... , ?N) ".
	 * <li><b>SQLSERVER:</b> "INSERT INTO tabla (campo1, campo2, ... , campoN) VALUES (?1, ?2, ... , ?N) ".</li>
	 * </ul>
	 * 
	 * @param tabla Tabla donde se ejecuta la insercion.
	 * @param nombreSecuencia Nombre de la secuencia sobre la que se hace NEXTVAL.
	 * @param pk Clave primaria a la que se le aplicara la secuencia.
	 * @param campos Campos cuyos valores seran insertados.
	 * @return
	 */
	public SqlBuilder insertIntoNextValParametrizado(String tabla, String nombreSecuencia, String pk, String... campos) {
		iniciarConsulta(INSERT_INTO).append(tabla).abrir();
		StringBuilder values = new StringBuilder(VALUES).append(ABRIR_PARENTESIS);

		int restante = campos.length;
		if (gestor == GestorBaseDatos.ORACLE) {
			this.append(pk);
			values.append(nextVal(nombreSecuencia));

			if (restante > 0) {
				this.append(COMA);
				values.append(COMA);
			}
		}
		
		for (String campo : campos) {
			this.append(campo);
			values.append(PARAMETRO);
			if (restante > 1) {
				this.append(COMA);
				values.append(COMA);
				restante--;
			}
		}
		values.append(CERRAR_PARENTESIS);
		this.cerrar().append(values.toString());

		return this;
	}

	/**
	 * Prepara una consulta "INSERT INTO tabla (campo1, campo2, ... , campoN)
	 * VALUES (?1, ?2, ... , ?N) ".
	 *
	 * @param tabla
	 * @param campos
	 * @return
	 */
	public SqlBuilder insertIntoParametrizado(String tabla, String... campos) {
		return insertIntoParametrizado(tabla, Arrays.asList(campos));
	}

	/**
	 * Prepara una consulta "INSERT INTO tabla (campo1, campo2, ... , campoN) "
	 * continuable con {@link SqlBuilder#values(java.lang.String...) }
	 *
	 * @param tabla
	 * @param campos
	 * @return
	 */
	public SqlBuilder insertInto(String tabla, String... campos) {
		return iniciarConsulta(INSERT_INTO).append(tabla)
				.abrir().anadirCampos(campos).cerrar();
	}

	/**
	 * Continúa una consulta con " VALUES (valor1, valor2, ... , valorN) "
	 *
	 * @param valores
	 * @return
	 */
	public SqlBuilder values(String... valores) {
		return this.append(VALUES)
				.abrir().anadirCampos(valores).cerrar();
	}

	/**
	 * Prepara una consulta "UPDATE tabla SET campo1 = ?, campo2 = ?, ... ,
	 * campoN = ? ".
	 *
	 * @param tabla
	 * @param campos
	 * @return
	 */
	public SqlBuilder updateParametrizado(String tabla, String... campos) {
		String sufijo = String.format("%s%s", IGUAL, PARAMETRO);
		return this.iniciarConsulta(UPDATE).append(tabla).setParametrizado(campos);
	}

	/**
	 * Prepara una consulta "UPDATE tabla".
	 * 
	 * @param tabla
	 * @return 
	 */
	public SqlBuilder update(String tabla) {
		return this.iniciarConsulta(UPDATE).append(tabla);
	}
	
	/** Añade a una consulta " SET campo = valor".
	 * 
	 * @param campo
	 * @param valor
	 * @return 
	 */
	public SqlBuilder set(String campo, String valor) {
		if (this.toString().indexOf(SET) > 0) {
			this.append(COMA);
		} else {
			this.append(SET);
		}
		return this.append(campo).append(IGUAL).append(valor);
	}
	
	/**
	 * Añade a la consulta " SET campo1 = ?, campo2 = ?, ... , campoN = ? ".
	 * 
	 * @param campos
	 * @return 
	 */
	public SqlBuilder setParametrizado(String... campos) {
		String sufijo = String.format("%s%s", IGUAL, PARAMETRO);
		if (this.toString().indexOf(SET) > 0) {
			this.append(COMA);
		} else {
			this.append(SET);
		}
		return this.anadirCamposConSeparadorYSufijo(sufijo, COMA, campos);
	}
	
	/**
	 * Devuelve un String de la forma " ( subconsulta ) ".
	 *
	 * @param subconsulta
	 * @return
	 */
	public static String prepararSubconsulta(SqlBuilder subconsulta) {
		return String.format("%s%s%s", ABRIR_PARENTESIS, subconsulta.toString(), CERRAR_PARENTESIS);
	}

	/**
	 * Devuelve un String de la forma " TO_CHAR ( campo ) ".
	 *
	 * @param campo
	 * @return
	 */
	public static String toChar(String campo) {
		return String.format("%s%s%s%s", TO_CHAR, ABRIR_PARENTESIS, campo, CERRAR_PARENTESIS);
	}

	/**
	 * Devuelve un String de la forma " TO_TIMESTAMP ( campo, 'formatoFecha' ) ".
	 *
	 * @param campo
	 * @param formatoFecha
	 * @return
	 */
	public static String toTimestamp(String campo, String formatoFecha) {
		return String.format("%s%s%s,'%s'%s", TO_TIMESTAMP, ABRIR_PARENTESIS, campo, formatoFecha, CERRAR_PARENTESIS);
	}
	
	/**
	 * Devuelve un String:
	 * <ul>
	 * <li><b>ORACLE:</b> " SYSDATE "</li>
	 * <li><b>SQLSERVER:</b> " GETDATE() "</li>
	 * </ul>.
	 * @return 
	 */
	public static String sysdate(){
		return SYSDATE;
	}
	
	/**
	 * Devuelve un String de la forma " MAX ( columna ) "
	 *
	 * @param columna
	 * @return
	 */
	public static String max(String columna) {
		return String.format("%s%s%s%s", MAX, ABRIR_PARENTESIS, columna, CERRAR_PARENTESIS);
	}

	/**
	 * Devuelve un String de la forma " COALESCE ( campo1, campo2, ... , campoN ) ".
	 * Hay que tener en cuenta que estos campos son Strings.
	 * 
	 * @param campos
	 * @return 
	 */
	public static String coalesce(String... campos) {
		return String.format("%s%s%s%s", COALESCE, ABRIR_PARENTESIS, anadirArgumentos(campos), CERRAR_PARENTESIS);
	}

	/**
	 * Devuelve la longitud de la cadena interna.
	 *
	 * @return
	 */
	public int length() {
		return consulta.length();
	}

	/**
	 * Muestra si el SqlBuilder está vacío.
	 *
	 * @return
	 */
	public boolean isEmpty() {
		return length() == 0;
    }

    /**
     * Crea un log a nivel DEBUG si éste está habilitado de la sentencia SQL y después devuelve el objeto propio.
     *
     * @param log
     */
    public SqlBuilder logSqlDebug(Logger log) {
        if (log.isDebugEnabled()) {
            log.debug(this.toString());
        }
		return this;
    }

    private SqlBuilder join(String tipoJoin, String tabla, String campoA, String operador, String campoB) {
		return this.append(tipoJoin).append(tabla).append(ON).anadirComparacion(" ", campoA, operador, campoB);
	}

	private SqlBuilder anadirComparacion(String palabraClave, String campoA, String operador, String campoB) {
		this.append(palabraClave).append(campoA).append(operador).append(campoB);
		return this;
	}

	private SqlBuilder iniciarOSeguirConsulta(String inicio, String continuacion) {
		if (!isEmpty()) {
			return this.append(continuacion);
		} else {
			return iniciarConsulta(inicio);
		}
	}

	/**
	 * Añade campos separados por comas de la forma "Campo1, Campo2, ... ,
	 * CampoN ".
	 *
	 * @param campos
	 * @return
	 */
	private static String anadirArgumentos(String... campos) {
		return concatenarCamposConSeparador(COMA, campos);
	}

	/**
	 * Añade campos separados por comas tal que " Campo1, Campo2, ... , CampoN "
	 *
	 * @param campos
	 * @return
	 */
	private SqlBuilder anadirCampos(String... campos) {
		return anadirCamposConSeparador(COMA, campos);
	}

	/**
	 * Añade campos separados por un separador establecido como parámetro tal
	 * que " Campo1separador Campo2separador ... separador CampoN ". Separador
	 * no tiene espacio después de la palabra previa para poder separar, por
	 * ejemplo, con comas.
	 *
	 * @param separador
	 * @param campos
	 * @return
	 */
	private SqlBuilder anadirCamposConSeparador(String separador, String... campos) {
		return this.append(concatenarCamposConSeparador(separador, campos));
	}

	/**
	 * Añade campos separados por un separador establecido como parámetro tal
	 * que " Campo1sufijoseparador Campo2sufijoseparador ... separador
	 * CampoNsufijo ". Separador no tiene espacio después de la palabra previa
	 * para poder separar, por ejemplo, con comas.
	 *
	 * @param separador
	 * @param campos
	 * @return
	 */
	private SqlBuilder anadirCamposConSeparadorYSufijo(String sufijo, String separador, String... campos) {

		int restante = campos.length;

		for (String campo : campos) {
			this.append(String.format("%s%s", campo, sufijo));
			if (restante > 1) {
				this.append(separador);
				restante--;
			}
		}

		return this;
	}

	private static String concatenarCamposConSeparador(String separador, String... campos) {
		int restante = campos.length;
		StringBuilder consulta = new StringBuilder();
		for (String campo : campos) {
			consulta.append(campo);
			if (restante > 1) {
				consulta.append(separador);
				restante--;
			}
		}
		return consulta.toString();
	}

	private SqlBuilder iniciarConsulta(String inicio) {
		consulta = new StringBuilder(inicio);
		return this;
	}

	/**
	 * Añade " ( " a la consulta.
	 *
	 * @return
	 */
	private SqlBuilder abrir() {
		return this.append(ABRIR_PARENTESIS);
	}

	/**
	 * Añade " ) " a la consulta.
	 *
	 * @return
	 */
	private SqlBuilder cerrar() {
		return this.append(CERRAR_PARENTESIS);
	}

	/**
	 * Crea la representación como String de la consulta.
	 *
	 * @return El String que representa la consulta.
	 */
	@Override
	public String toString() {
		return consulta.toString();
	}

	/**
	 * Solo debe ser utilizado tras una comprobacion del {@link GestorBaseDatos} que demuestre que es 
	 * {@link GestorBaseDatos#ORACLE}.
	 * 
	 * @param nombreSecuencia Nombre de la secuencia a la que aplicar la funcion NEXTVAL
	 * @return String de la forma "nombresecuencia.NEXTVAL"
	 */
	protected static String nextVal(String nombreSecuencia) {
		//adaptadorSqls
		return String.format("%s%s", nombreSecuencia, NEXTVAL);
	}
}
