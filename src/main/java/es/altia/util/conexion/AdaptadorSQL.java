package es.altia.util.conexion;
import java.sql.Connection;

/**
 * <p>T�tulo: @gora</p>
 * <p>Descripci�n: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author sin atribuir
 * @version 1.0
 */

public interface AdaptadorSQL {

   /*Constantes para la funci�n CONVERT*/
   /**
    * Para convertir a texto
    */
   public static final int CONVERTIR_COLUMNA_TEXTO = 1;
   /**
    * Para convertir a n�mero
    */
   public static final int CONVERTIR_COLUMNA_NUMERO = 2;
   /**
    * La fecha que se pase debe estar en formato DD/MM/YYYY
    */
   public static final int CONVERTIR_COLUMNA_FECHA = 4;
   /*Constantes para las cadenas de fecha y hora*/
   /**
    * Se pasan fechas
    */
   public static final int FECHAHORA_CAMPO_FECHA = 1;
   /**
    * Se pasan horas
    */
   public static final int FECHAHORA_CAMPO_HORA = 2;
   /**
    * Se pasan fechas y horas
    */
   public static final int FECHAHORA_CAMPO_FECHA_HORA = 3;
   /*Constantes para las funciones de cadena*/
   /**
    * Hay que pasar un car�cter como par�metro
    * Devuelve la representaci�n decimal del caracter
    */
   public static final int FUNCIONCADENA_ASCII = 1;
   /**
    * Hay que pasar un entero como par�metro
    * Devuelve el car�cter correspondiente al n�mero decimal pasado como par�metro
    */
   public static final int FUNCIONCADENA_CHR = 2;

   /**
    * Hay que pasar una cadena como par�metro
    * Devuelve la cadena en min�sculas
    */
   public static final int FUNCIONCADENA_LOWER = 3;
   /**
    * Hay que pasar una cadena como par�metro
    * Devuelve la longitud de la cadena
    */
   public static final int FUNCIONCADENA_LENGTH = 4;
   /**
    * Hay que pasar una cadena como par�metro
    * Devuelve la cadena sin espacios en blanco por la izquierda
    */
   public static final int FUNCIONCADENA_LTRIM = 5;
   /**
    * Hay que pasar tres cadenas como par�metros
    * Devuelve la primera cadena con las ocurrencias de la segunda sustituidas
    * por la tercera.
    */
   public static final int FUNCIONCADENA_REPLACE = 6;
   /**
    * Hay que pasar una cadena como par�metro
    * Devuelve la cedena sin espacios en blanco por la derecha
    */
   public static final int FUNCIONCADENA_RTRIM = 7;
   /**
    * Hay que pasar una cadena como par�metro
    * Devuelve la representaci�n fon�tica de la cadena (en ingl�s)
    */
   public static final int FUNCIONCADENA_SOUNDEX = 8;
   /**
    * Hay que pasar una cadena como primer par�metro y un n�mero como segundo y
    * tercer par�metros
    * Devuelve la subcadena contenida en el primer par�metro, a partir de la
    * posici�n indicada por el segundo, con el n�mero de caracteres indicados
    * por el tercero
    */
   public static final int FUNCIONCADENA_SUBSTR = 9;
   /**
    * Hay que pasar una cadena como par�metro
    * Devuelve la cadena en may�sculas
    */
   public static final int FUNCIONCADENA_UPPER = 10;
   /**
    * Hay que pasar las cadenas como par�metros, devolv�endose como resultado la
    * concatenaci�n de todas ellas
    */
   public static final int FUNCIONCADENA_CONCAT = 11;
    /**
    * Hay que pasar una cadena como par�metro
    * y el tama�o de la cadena resultante
    * Devuelve la cadena de tama�o deseado con los ceros
    * a la izqda que sean necesarios
    */
   public static final int FUNCIONCADENA_LPAD = 12;
   /**
    * Hay que pasar una cadena como par�metro
    * Devuelve la cedena sin espacios en blanco por la derecha ni por la izquierda
    */
   public static final int FUNCIONCADENA_TRIM = 13;
    /**
     * Hay que pasar tres cadenas como par�metro
     * Devuelve la primera cadena sustituyendo los caracteres de la segunda por los de la tercera
     */
    public static final int FUNCIONCADENA_TRANSLATE = 14;
   /*Constantes para las funciones de fecha y hora*/
   /**
    * Sin par�metros
    * Devuelve la fecha y hora actuales
    */
   public static final int FUNCIONFECHA_SYSDATE = 1;
   /*Constantes para las funciones de fecha y hora*/
   /**
    * Recibe la columna o valor a truncar
    * Devuelve la fecha truncando la hora
    */
   public static final int FUNCIONFECHA_TRUNCAR_HORA = 2;
   /*Constantes para funciones matem�ticas*/
   /**
    * Recibe un n�mero como par�metro
    * Devuelve el valor absoluto del n�mero
    */
   public static final int FUNCIONMATEMATICA_ABS = 1;
   /**
    * Hay que pasar un n�mero como par�metro
    * Devuelve el menor entero que es mayor o igual que el par�metro
    */
   public static final int FUNCIONMATEMATICA_CEIL = 2;
   /**
    * Hay que pasar un n�mero como par�metro
    * Devuelve el coseno del par�metro
    */
   public static final int FUNCIONMATEMATICA_COS = 3;
   /**
    * Hay que pasar un n�mero como par�metro
    * Devuelve e elevado a la potencia indicada por el par�metro
    */
   public static final int FUNCIONMATEMATICA_EXP = 4;
   /**
    * Hay que pasar un n�mero como par�metro
    * Devuelve el mayor entero que menor o igual que el par�metro
    */
   public static final int FUNCIONMATEMATICA_FLOOR = 5;
   /**
    * Hay qur pasar un n�mero como par�metro
    * Devuelve el logaritmo neperiano del par�metro
    */
   public static final int FUNCIONMATEMATICA_LN = 6;
   /**
    * Hay que pasar dos n�meros como par�metros
    * Devuelve el logaritmo, en la base del primer par�metro, del segundo
    */
   public static final int FUNCIONMATEMATICA_LOG = 7;
   /**
    * Hay que pasar dos n�meros como par�metros
    * Devuelve el resto de dividir el primer par�metro entre el segundo
    */
   public static final int FUNCIONMATEMATICA_MOD = 8;
   /**
    * Hay que pasar dos n�meros como par�metros
    * Devuelve el primer par�metro elevado a la potencia indicada por el segundo
    */
   public static final int FUNCIONMATEMATICA_POWER = 9;
   /**
    * Hay que pasar un n�mero como primer par�metro y un entero como segundo
    * par�metro
    * Devuelve el primer par�metro redondeado a las posiciones indicadas por el
    * segundo
    */
   public static final int FUNCIONMATEMATICA_ROUND = 10;
   /**
    * Hay que pasar un n�mero como par�metro
    * Devuelve -1 si el par�metro es negativo, 1 si es positivo o 0 si era 0
    */
   public static final int FUNCIONMATEMATICA_SIGN = 11;
   /**
    * Hay que pasar un n�mero como par�metro
    * Devuelve el seno del par�metro
    */
   public static final int FUNCIONMATEMATICA_SIN = 12;
   /**
    * Hay que pasar un n�mero positivo como par�metro
    * Devuelve la ra�z cuadrada del n�mero
    */
   public static final int FUNCIONMATEMATICA_SQRT = 13;
   /**
    * Hay que pasar un n�mero como par�metro
    * Devuelve la tangente del par�metro
    */
   public static final int FUNCIONMATEMATICA_TAN = 14;
   /**
    * Hay que pasar dos n�mero como par�metros
    * Devuelve el primer par�metro truncado las posiciones a la derecha (si es
    * positivo el segundo) o a la izquierda (si es negativo el segundo) de la
    * coma decimal que indique el segundo par�metro
    */
   public static final int FUNCIONMATEMATICA_TRUNC = 15;
   /**
     * Hay que pasar un n�mero como par�metro
     * Devuelve el maximo del par�metro
     */
    public static final int FUNCIONMATEMATICA_MAX = 16;
   /**
     * Hay que pasar un n�mero como par�metro
     * Devuelve el minimo del par�metro
     */
    public static final int FUNCIONMATEMATICA_MIN = 17;
    /**
     * Hay que pasar un n�mero como par�metro
     * Devuelve la media del par�metro
     */
   public static final int FUNCIONMATEMATICA_AVG = 18;
   /**
    * Hay que pasar un String como parametro
    * Devuelve el String capitalizado
    */
   public static final int FUNCIONCADENA_INITCAP = 16;
   /*Constantes para funciones de sistema*/
   /**
    * Hay que pasar dos expresiones como par�meros
    * Si la primera expresion es NULL se sustituye por la segunda
    */
   public static final int FUNCIONSISTEMA_NVL = 1;
   /**
    * No hay que pasar par�metros
    * Devuelve el usuario actual
    */
   public static final int FUNCIONSISTEMA_USER = 2;
   /**
    * No se admiten transacciones 
    */
   public static final int CAMBIARNIVELTRANSACCION_NINGUNA = 99;
   /**
    * Permite realizar consultas en campos de texto de la base de datos sin tener en cuenta
    * ni may�sculas ni acentos.
    */
   public static final int FUNCIONCADENA_IGNORAR_ACENTOS_MAY�SCULAS = 15;
   /**
    * Permite realizar consultas en campos de texto de la base de datos sin tener en cuenta
    * ni may�sculas ni acentos a nivel de campo.
    */
   public static final int FUNCIONCADENA_IGNORAR_ACENTOS_MAYUSCULAS_POR_CAMPO = 17;
   /**
    * Este nivel permite que una fila que ha sido cambiada por una transacci�n
    * sea leida por otra antes que se haya hecho un commit de la misma (lectura
    * sucia: si la primera transacci�n ejecuta un rollback, la segunda recibir�
    * datos err�neos).
    */
   public static final int CAMBIARNIVELTRANSACCION_COMMIT_LECTURA = Connection.TRANSACTION_READ_COMMITTED;
   /**
    * Se proh�be leer una fila con cambios a los cuales todav�a no se les ha
    * hecho commit (lectura sucia).
    */
   public static final int CAMBIARNIVELTRANSACCION_NO_COMMIT_LECTURA = Connection.TRANSACTION_READ_UNCOMMITTED;
   /**
    * Adem�s de la restricci�n del nivel anterior, se impide que una transacci�n
    * lea una fila, otra la altere y la primera vuelva a leerla, con lo que
    * obtendr�a datos distintos entre las dos lecturas (lectura no repetida).
    */
   public static final int CAMBIARNIVELTRANSACCION_LECTURA_REPETIDA = Connection.TRANSACTION_REPEATABLE_READ;
   /**
    * Adem�s de todas las restricciobes anteriores evita la situacion en la que
    * una transacci�n lee todas las filas que satisfagan una condici�n where,
    * luego una segunda transacci�n inserta una fila que satisface esa condici�n
    * y de nuevo la primera transacci�n vuelve a leer filas afectadas por la
    * misma condici�n where, recuperando tambi�n la fila insertada por la
    * segunda transacci�n (lectura fantasma).
    */
   public static final int CAMBIARNIVELTRANSACCION_SERIALIZADA = Connection.TRANSACTION_SERIALIZABLE;

   /**
    * Funcion para la adapter de datos
    * @param columna La constante o campo de la base de datos a convertir
    * @param tipo El tipo de datos al que hay que convertir el primer par�metro�
    * @param fmt El formato con el que se mostrar�n los datos
    * @return El String adecuado para realizar la conversi�n
    */
   public String convertir(String columna, int tipo, String fmt);

   /**
    * Funci�n para el tratamiento de fechas
    * @param valor La cadena representando el valor de la fecha o el campo de
    * la base de datos a tratar en formato dd/MM/yyyy para fecha, dd/MM/yyyy
    * HH:mm:ss para fecha-hora y HH:mm:ss para hora
    * @param tipoCampo El tipo de valor fecha de que se trata
    * @return El String adecuado para tratar la fecha
    * @throws BDException
    */
   public String fechaHora(String valor, int tipoCampo) throws BDException;

   /**
    * Funci�n encargada del tratamiento de cadenas
    * @param funcion El tipo de funci�n de cadena a aplicar
    * @param parametros Los elementos que se necesitan para aplicar la funci�n
    * seleccionada en el primer argumento
    * @return El String adecuado para llevar a cabo la funci�n
    */
   public String funcionCadena(int funcion, String[] parametros);

   /**
    * Se encarga de tratar las funciones de fecha
    * @param funcion El tipo de funci�n de fecha a aplicar
    * @param parametros Los elementos que se necesitan para aplicar la funci�n
    * seleccionada en el primer argumento
    * @return El String adecuado para ejecutar la funcion
    */
   public String funcionFecha(int funcion, String[] parametros);

   /**
    * Se encarga de tratar las funciones matem�ticas
    * @param funcion El tipo de funci�n matem�tica:
    * @param parametros Los elementos que se necesitan para aplicar la funci�n
    * seleccionada en el primer argumento
    * @return El String adecuado para ejecutar la funci�n
    */
   public String funcionMatematica(int funcion, String[] parametros);

   /**
    * Se encarga del tratamientode las funciones del sistema
    * @param funcion El tipo de funci�n de sistema:
    * @param parametros Los elementos que se necesitan para aplicar la funci�n
    * seleccionada en el primer argumento
    * @return El String adecuado para ejecutar la funci�n
    */
   public String funcionSistema(int funcion, String[] parametros);

   /**
     * Devuelve el simbolo empleado por la BD para representar la concatenacion de cadenas
     * @return El simbolo de concatenacion
     */
    public String getSymbolConcat();

   /**
    * Funci�n encargadada del tratamiento de agrupaciones
    * @param camposAgrupacion vector con parejas NOMBRE (del campo), POSICION
    * (dentro de la select)
    * @return El String adecuado para realizar la agrupaci�n
    */
   public String group(String[] camposAgrupacion);

   /**
    * Funci�n encargadada del tratamiento de los rollup en la agrupaciones
    * @param camposAgrupacion vector con los NOMBRES (del campo)
    * @return El String adecuado para realizar la agrupaci�n
    */
   public String rollup(String[] camposAgrupacion);

   /**
    * Funci�n encargada de realizar los joins sobre tablas. De momento no
    * soporta joins anidados.
    * @param parteFrom String con la parte from de una sentencia (solo el nombre
    * de las tablas o los alias empleados)
    * @param parteWhere String con la parte where de una sentencia (solo la de
    * la sentencia, sin las condiciones de los joins)
    * @param parametros Corresponde a la siguiente secuencia de informaci�n:
    * Tabla principal, tipo de uni�n 1 ("Left" o "Inner"), tabla
    * secundaria 1, condici�n de uni�n 1, tipo de uni�n 2, tabla secundaria 2,
    * condici�n de uni�n 2, ... Deben ir OBLIGATORIAMENTE en ese orden. El
    * �ltimo par�metro tendr� el valor "true" si se anida y "false" en caso
    * contrario
    * @return El string adecuado para llevar a cabo el join
    * @throws BDException
    */
   public String join(String parteFrom, String parteWhere, String[] parametros)
	   throws BDException;

   /**
    * Funci�n encargada de tratamiento de la cl�usula like
    * @param campo El campo de base de datos o valor constante a comparar
    * @param valor Patr�n de comparaci�n. Puede contener los caracteres comod�n
    * _ (para un solo caracter) y % (para 0 o m�s caracteres)
    * @param caracter El caracter de escape que queramos especificar
    * @return El String adecuado para agregar la cl�usula like
    */
   public String like(String campo, String valor, String caracter);

   /**
    * Esta funci�n resuelve las ordenaciones de sentencias
    * @param parametros vector con parejas NOMBRE (del campo), POSICION
    * (dentro de la select)
    * @return El String adecuado para la cl�usula ORDERBY
    */
   public String orderUnion(String[] parametros);

    /**
     * M�todo que se encarga de devolver el numero correspondiente de la secuencia, aumentandolo en 1 en la bd
     * @param parametros los parametros de conexion a la BD
     * @param secuencia el nombre de la secuencia
     * @throws BDException
     */
    public long devolverNextValSecuencia(String[] parametros, String secuencia) throws BDException;

    /**
     * Metodo que introduce las comillas simples a un campo que es de tipo String y adem�s comprueba que no tenga otras
     * comillas simples por el medio
     * @param dato El valor sin las comillas simples
     * @return El valor con las comillas simples
     */
    public String addString(String dato);

   /**
    * Funci�n que nos devuelve una conexi�n a la base de datos
    * @return El Connection correspondiente
    * @throws BDException
    */
   public Connection getConnection() throws BDException;

   /**
    * M�todo que se encarga de liberar la conexi�n con la base de datos
    * @param conexion la conexi�n a liberar
    * @throws BDException
    */
   public void devolverConexion(Connection conexion) throws BDException;

   /**
    * M�todo que ha de llamarse antes de ejecutar cualquier transacci�n
    * @param conexion La conexion sobre la que vamos a operar
    * @throws BDException
    */
   public void inicioTransaccion(Connection conexion) throws BDException;

   /**
    * M�todo que hay que llamar al finalizar la transacci�n ya que salva los
    * cambios realizados en la base de datos
    * @param conexion La conexi�n utilizada
    * @throws BDException
    */
   public void finTransaccion(Connection conexion) throws BDException;

   /**
    * M�todo que nos permite cambiar el nivel de transacci�n con el que
    * accedemos a la base de datos si �sta lo permite
    * @param conexion La conexi�n que queremos alterar
    * @param nivel El nivel con que queremos acceder
    * @throws BDException
    */
   public void cambiarNivelTransaccion(Connection conexion, int nivel) throws BDException;

   /**
    * Cancela la transacci�n actual.
    * @param conexion La conexi�n utilizada
    * @throws BDException
    */
   public void rollBack(Connection conexion) throws BDException;

   /**
    * Devuelve "LEFT" o "RIGHT" seg�n la BD. Necesario para ciertos casos. 
    */
   public String valorJoin();
   
   /**
    * Devuelve la parte FROM de una query cuando no se realiza sobre ninguna tabla (por ejemplo,
    * SELECT SYSDATE", que no consulta ninguna tabla espec�fica del SIGP.
    * @return "FROM tabla"
    */
   public String consultaSinTabla();
   
   /*
    Alterar la sesi�n de la base de datos para que no distinga may�sculas/min�sculas y acentos
    */
   public String consultaSinAcentos(Connection conexion, String[] params);
   
   public String whereEsNumero(String cadena);
   
   public String whereNoEsNumero(String cadena);
   
   public String castFecha(String dato);

   public String[] getParametros();
   
    public String getTipoGestor();

}
