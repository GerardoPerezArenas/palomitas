package es.altia.util.conexion;

import java.text.*;
import java.util.Locale;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;


/**
 * Clase que extiende a AdaptadorSQLBD para dar soporte a Sql Server
 * <p>Título: </p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: Altia Consultaros & AytosCPD</p>
 * @author Manuel Vera Silvestre
 * @version 1.0
 */
public class AdaptadorSQLSqlServer extends AdaptadorSQLBD{

   //private static String url;
   //private static String ficheroLog;
   //private static Driver oraDriver;
   //private static Properties p;

   public String convertir(String dato, int tipo, String fmt){
    String convertir = "";
    String auxTipo = "";
    String nulo = "";
    /* Determinamos el valor para nulo */
    switch(tipo){
       case (CONVERTIR_COLUMNA_NUMERO):
                if (dato.indexOf("'")!=-1){
                try {
                        dato = dato.substring(1, dato.length()-1);//Eliminamos comillas simple 'dato'

                        /****De la vista viene con formato entero,decimal y la base de datos solo acepta entero.decimal-->convertir***/
                    NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("es", "ES"));
                    numberFormat.setMinimumFractionDigits(2);
                    Number numero = numberFormat.parse(dato);
                        DecimalFormat decimalFormat = new DecimalFormat("###.##", new DecimalFormatSymbols(
                                new Locale("en", "EN")));
                        dato = decimalFormat.format(numero.doubleValue());
                        /********************************************************************************************/
                        auxTipo = dato;//No añadimos el CONVERT porque SQLSERVER se come la parte decimal
                } catch(ParseException e) {
                    e.printStackTrace();
                    return "";
                }
                }else{
                    auxTipo = "CONVERT(numeric, " + dato + ")";
                }
        nulo = "0";
        break;
       case(CONVERTIR_COLUMNA_FECHA):
           auxTipo = "CONVERT(datetime, " + dato ;
                if ((fmt!=null)) {
                    if (fmt.equalsIgnoreCase("DD/MM/YYYY")) {
                        auxTipo += ", 103";
                    } else if (fmt.equalsIgnoreCase("DD/MM/YYYY HH24:MI:SS")) {
                        auxTipo += "";//No se mete ningun estilo, pq el adaptador convierte bien la fecha de todas formas
                    } else if (fmt.equalsIgnoreCase("YYYY-MM-DD")) {
                        auxTipo += ", 120";
                    }
                }
           
                auxTipo += ")";
        nulo = "''";
        break;
       case(CONVERTIR_COLUMNA_TEXTO):
        auxTipo = "CONVERT(varchar, " + dato ;
                if ((fmt!=null)) {
                    if (fmt.equalsIgnoreCase("DD/MM/YYYY")) {
           auxTipo += ", 103";
                    } else if (fmt.equalsIgnoreCase("DD/MM/YYYY HH24:MI:SS")) {
                        auxTipo += ", 103) + ' ' + CONVERT(varchar, " + dato + ", 108";
                    } else if (fmt.equalsIgnoreCase("YYYY-MM-DD")) {
                        auxTipo += ", 120";
                        auxTipo = "SUBSTRING(" + auxTipo + "), 1, 10";
                    } else if (fmt.equalsIgnoreCase("YYYYMMDD")) {
                        auxTipo += ", 120";
                        auxTipo = "REPLACE(SUBSTRING(" + auxTipo + "), 1, 10), '-',''";
                    } else if (fmt.equalsIgnoreCase("YYYY")) {
                        auxTipo += ", 103";
                        auxTipo = "SUBSTRING(" + auxTipo + "), 7, 4";
                    } else if (fmt.equalsIgnoreCase("MM/YYYY")) {
                        auxTipo += ", 103";
                        auxTipo = "SUBSTRING(" + auxTipo + "), 4, 7";
                    } else if (fmt.equalsIgnoreCase("MM")) {
                        auxTipo += ", 103";
                        auxTipo = "SUBSTRING(" + auxTipo + "), 4, 2";
                    } else if (fmt.equalsIgnoreCase("HH24:MI")) {
                        auxTipo += ", 114";
                        auxTipo = "SUBSTRING(" + auxTipo + "), 1, 5";
                    } else if (fmt.equalsIgnoreCase("HH24:MI:SS")) {
                    	auxTipo += ", 108";
                    }
        }
        auxTipo += ")";
        nulo = "''";
        break;
    }
    String[] aux = {auxTipo, nulo};
    convertir = funcionSistema(AdaptadorSQL.FUNCIONSISTEMA_NVL, aux);
    return convertir;
   }

   public String fechaHora(String valor, int tipoCampo)
        throws BDException{
    String fechaHora = "";
    SimpleDateFormat s;
    java.util.Date d;
    try{
       switch(tipoCampo){
        case(FECHAHORA_CAMPO_FECHA):
           s = new SimpleDateFormat("dd/MM/yyyy");
           d = s.parse(valor);
           //s.applyPattern("yyyy-MM-dd");
           //fechaHora = " {d '" + s.format(d) + "'}";
           fechaHora = " CONVERT('"+valor+"','DD/MM/YYYY') ";
        break;
       case(FECHAHORA_CAMPO_HORA):
           s = new SimpleDateFormat("HH:mm:ss");
           d = s.parse(valor);
           //s.applyPattern("HH:mm:ss");
           //fechaHora = " {t '" + s.format(d) + "'}";
           fechaHora = " CONVERT('"+valor+"','HH24:MI:SS') ";
        break;
       case(FECHAHORA_CAMPO_FECHA_HORA):
           s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
           d = s.parse(valor);
           //s.applyPattern("yyyy-MM-dd HH:mm:ss");
           //fechaHora = " {ts '" + s.format(d) + "'}";
           fechaHora = " CONVERT('"+valor+"','DD/MM/YYYY HH24:MI:SS') ";
        break;
       default:
        fechaHora = "";
        break;
       }
    }
    catch(ParseException pe){
       m_Log.error("*** AdaptadorSQLBD." + pe.toString());
       throw new BDException(999,"Error al tratar de dar formato en la " +
                     "función fechaHora", pe.toString());
    }
    catch(Exception e){
       m_Log.error("*** AdaptadorSQLBD." + e.toString());
       throw new BDException(999,"Error en la función fechaHora",
                     e.toString());
    }
    return fechaHora;

   }

   public String funcionCadena(int funcion, String[] parametros){
    String funcionCadena = "";
    switch(funcion){
       case(FUNCIONCADENA_ASCII):
        funcionCadena = " ASCII(" + parametros[0] + ") ";
        break;
       case(FUNCIONCADENA_CHR):
        funcionCadena = " CHAR(" + parametros[0] + ") ";
        break;
       case(FUNCIONCADENA_CONCAT):
                funcionCadena = " ISNULL(" + parametros[0] + ",'')";
        for (int i=1;i<parametros.length;i++){
                    funcionCadena = funcionCadena + " + " + " ISNULL(" + parametros[i] + ",'')";
        }	               
        break;
       case(FUNCIONCADENA_LOWER):
        funcionCadena = " LOWER(" + parametros[0] + ") ";
        break;
       case(FUNCIONCADENA_LENGTH):
        funcionCadena = " LEN(" + parametros[0] + ") ";
        break;
       case(FUNCIONCADENA_LTRIM):
        funcionCadena = " LTRIM(" + parametros[0] + ") ";
        break;
       case(FUNCIONCADENA_REPLACE):
        funcionCadena = " REPLACE(" + parametros[0] + "," +
                    parametros[1] + "," + parametros[2] + ") ";
        break;
       case(FUNCIONCADENA_RTRIM):
        funcionCadena = " RTRIM(" + parametros[0] + ") ";
        break;
       case(FUNCIONCADENA_SOUNDEX):
        funcionCadena = " SOUNDEX(" + parametros[0] + ") ";
        break;
       case(FUNCIONCADENA_SUBSTR):
        funcionCadena = " SUBSTRING(" + parametros[0] + "," +
                    parametros[1] + "," + parametros[2] + ") ";
        break;
       case(FUNCIONCADENA_UPPER):
        funcionCadena = " UPPER(" + parametros[0] + ") ";
        break;
        case(FUNCIONCADENA_INITCAP):
         funcionCadena = parametros[0]; // No existe esta función en SQL Server
         break;
           case(FUNCIONCADENA_LPAD):
                String ceros = "";
                for (int i=0;i<Integer.parseInt(parametros[1]);i++){
                 ceros = ceros + "0";
                }
                String s = "'" + ceros + "'" + "+" +parametros[0];
                funcionCadena = "SUBSTRING("+s+","+"LEN("+s+")-"+parametros[1]+",LEN("+s+"))";
                break;
            case(FUNCIONCADENA_TRIM):
                funcionCadena = "RTRIM(LTRIM("+parametros[0]+"))";
                break;
                
            case(FUNCIONCADENA_TRANSLATE):
            	funcionCadena="REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(" + parametros[0];
            	for (int i=1; i<parametros[1].length()-1; i++){
            		funcionCadena+= "," +"SUBSTRING(" + parametros[1] +"," + String.valueOf(i) 
            		+ ",1)" +"," +"SUBSTRING(" + parametros[2] +"," + String.valueOf(i) + ",1)" + ")";
            	}
                break;
            case (FUNCIONCADENA_IGNORAR_ACENTOS_MAYÚSCULAS):
                funcionCadena = parametros[0] + " COLLATE Latin1_General_CI_AI ";
                break;
                 
    }
    return funcionCadena;
   }

   public String funcionFecha(int funcion, String[] parametros){
    String funcionFecha = "";
    switch(funcion){
       case(FUNCIONFECHA_SYSDATE):
        funcionFecha = " GETDATE() ";
        break;
        case (FUNCIONFECHA_TRUNCAR_HORA):
            funcionFecha = String.format(" DATEADD(%s, 0, DATEDIFF(%s, 0, GETDATE())) ", parametros[0], parametros[0]);
            break;
    }
    return funcionFecha;
   }

   public String funcionMatematica(int funcion, String[] parametros){
    String funcionMatematica = "";
    switch(funcion){
       case(FUNCIONMATEMATICA_ABS):
        funcionMatematica = " ABS(" + parametros[0] + ") ";
        break;
       case(FUNCIONMATEMATICA_CEIL):
        funcionMatematica = " CEILING(" + parametros[0] + ") ";
        break;
       case(FUNCIONMATEMATICA_COS):
        funcionMatematica = " COS(" + parametros[0] + ") ";
        break;
       case(FUNCIONMATEMATICA_EXP):
        funcionMatematica = " EXP(" + parametros[0] + ") ";
        break;
       case(FUNCIONMATEMATICA_FLOOR):
        funcionMatematica = " FLOOR(" + parametros[0] + ") ";
        break;
       case(FUNCIONMATEMATICA_LOG):
        funcionMatematica = " LOG10(" + parametros[0] + "," +
                      parametros[1] + ") ";
        break;
       case(FUNCIONMATEMATICA_LN):
        funcionMatematica = " LN(" + parametros[0] + ") ";
        break;
       case(FUNCIONMATEMATICA_MOD):
        funcionMatematica = parametros[0] + " % " +
                      parametros[1];
        break;
       case(FUNCIONMATEMATICA_POWER):
        funcionMatematica = " POWER(" + parametros[0] + "," +
                      parametros[1] + ") ";
        break;
       case(FUNCIONMATEMATICA_ROUND):
        funcionMatematica = " ROUND(" + parametros[0] + "," +
                      parametros[1] + ") ";
        break;
       case(FUNCIONMATEMATICA_SIGN):
        funcionMatematica = " SIGN(" + parametros[0] + ") ";
        break;
       case(FUNCIONMATEMATICA_SIN):
        funcionMatematica = " SIN(" + parametros[0] + ") ";
        break;
       case(FUNCIONMATEMATICA_SQRT):
        funcionMatematica = " SQRT(" + parametros[0] + ") ";
        break;
       case(FUNCIONMATEMATICA_TAN):
        funcionMatematica = " TAN(" + parametros[0] + ") ";
        break;
       case(FUNCIONMATEMATICA_TRUNC):
        funcionMatematica = " TRUNC(" + parametros[0] + "," +
                      parametros[1] + ") ";
        break;
            case(FUNCIONMATEMATICA_MAX):
                funcionMatematica = " MAX(" + parametros[0] + ") ";
                break;
            case(FUNCIONMATEMATICA_MIN):
                funcionMatematica = " MIN(" + parametros[0] + ") ";
                break;
            case(FUNCIONMATEMATICA_AVG):
                funcionMatematica = " AVG(" + parametros[0] + ") ";
                break;
    }
    return funcionMatematica;
   }

   public String funcionSistema(int funcion, String[] parametros){
    String funcionSistema = "";
    switch(funcion){
       case(FUNCIONSISTEMA_USER):
        funcionSistema = " USER_NAME ";
        break;
       case(FUNCIONSISTEMA_NVL):
        funcionSistema = " ISNULL(" + parametros[0] + "," +
                     parametros[1] + ") ";
        break;
    }
    return funcionSistema;
   }

    public String getSymbolConcat(){
        return "+";
    }

   public String group(String[] camposAgrupacion){
    String group = "";
    //nos saltamos las posiciones porque no hacen falta en ORACLE
    for (int i = 0; i < camposAgrupacion.length; i = i + 2)
       if(camposAgrupacion[i] != null)
        group = group + "," + camposAgrupacion[i];
    group = " GROUP BY " + group.substring(1);//quito la primera ","
    return group;
   }

    public String rollup(String[] camposAgrupacion){
        String group = "";
        //nos saltamos las posiciones porque no hacen falta en ORACLE
        for (int i = 0; i < camposAgrupacion.length; i++)
            if(camposAgrupacion[i] != null)
                group = group + "," + camposAgrupacion[i];
        group = " GROUP BY " + group.substring(1) + " WITH ROLLUP";//quito la primera ","
        return group;
    }
/**
 * @deprecated 
 * @param campoIzqda
 * @param campoDrcha
 * @return
 */
   public String joinLeft(String campoIzqda, String campoDrcha){
	   return (campoIzqda + " *= " + campoDrcha);
   }
/**
 * @deprecated 
 * @param camposIzqda
 * @param camposDrcha
 * @return
 */
   public String joinLeft(String camposIzqda[], String camposDrcha[]){
       String res = "";
       for (int i=0;i<camposIzqda.length;i++){
               res = res + joinLeft(camposIzqda[i],camposDrcha[i]);
               if (i<(camposIzqda.length-1)) res = res + " AND ";
       }
       return res;
   }
/**
 * @deprecated 
 * @param campoIzqda
 * @param campoDrcha
 * @return
 */
   public String joinRight(String campoIzqda, String campoDrcha){
	   return (campoIzqda + " =* " + campoDrcha);
   }
/**
 * @deprecated 
 * @param camposIzqda
 * @param camposDrcha
 * @return
 */
   public String joinRight(String camposIzqda[], String camposDrcha[]){
       String res = "";
       for (int i=0;i<camposIzqda.length;i++){
               res = res + joinRight(camposIzqda[i],camposDrcha[i]);
               if (i<(camposIzqda.length-1)) res = res + " AND ";
       }
       return res;
   }

   public String join(String partefrom, String partewhere, String[] parametros)
       throws BDException{
    String from ="SELECT " + partefrom + " FROM ";

        String where = "";
        if ((partewhere!=null) && (!partewhere.equals(""))) { 
            where = " WHERE " + partewhere;
        }
        
    int dimension;
    String join = "";
    String aux="";

    /*comprobamos que los parámetros se suministren correctamente*/
    if((parametros.length-2) % 3 == 0){
            join = parametros[0];
            int j=1;
            while (j<(parametros.length-1)){
                join = " " + join + " " + parametros[j] + " JOIN " + parametros[j+1] + " ON ("+
                 parametros[j+2]+ ")";
                j = j+3;
       }
    }
    else
       throw new BDException("Error al construir el join. El número de " +
                      "parámetros pasados en el tercer argumento " +
                      "no eran correctos");
    if ("".equals(partefrom)) return(join + where);
    else return (from +join + where);
   }

   
   public String like(String campo, String valor, String caracter){
    String escape = "\\";
    if (caracter != null)
       escape = caracter;
    String like = campo + " LIKE '" + valor + "'";
    if((valor.indexOf(escape + "%") != -1) || (valor.indexOf(escape + "_") != -1))
       like = like + " {escape '\\'}";
    return like;
   }

   public String orderUnion(String[] parametros){

    String orderUnion = " ORDER BY ";
    //nos saltamos las posiciones porque no hacen falta en ORACLE
    for(int i = 0; i < parametros.length; i = i + 2)
       if(parametros[i] != null)
        orderUnion = orderUnion + (String)parametros[i] + ", ";
    orderUnion = orderUnion.substring(0, orderUnion.length() - 2);
    return orderUnion;
   }

   /**
     * Método que se encarga de devolver el numero correspondiente de la secuencia, aumentandolo en 1 en la bd
     * @param parametros los parametros de conexion a la BD
     * @param secuencia el nombre de la secuencia
     * @throws BDException
     */
    public long devolverNextValSecuencia(String[] parametros, String secuencia) throws BDException {

        long resultado = -1;

        Connection con=null;
        AdaptadorSQLBD oad = null;
        try {
            m_Log.info("AdaptadorSQLSqlServer.devolverNextValSecuencia");
            oad = new AdaptadorSQLBD(parametros);
            con = oad.getConnection();
            con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            oad.inicioTransaccion(con);
            PreparedStatement ps =
                    con.prepareStatement(
                            "SELECT VALOR FROM SECUENCIAS WHERE NOMBRE=?");
            ps.setString(1, secuencia);
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                resultado = rs.getLong(1);
            }
            rs.close();
            ps.close();
            ps = con.prepareStatement("UPDATE SECUENCIAS SET VALOR=? WHERE NOMBRE=?");
            ps.setLong(1, resultado+1);
            ps.setString(2, secuencia);
            ps.executeUpdate();
            ps.close();
            oad.finTransaccion(con);
        } catch (SQLException e) {
            e.printStackTrace();
            oad.rollBack(con);
            m_Log.error(e.getMessage());
            if (e.getErrorCode() == 1)
                throw new BDException(e.getMessage());
            else if (e.getErrorCode() == 2291)
                throw new BDException(e.getMessage());
            else
                throw new BDException(e.getMessage());
        } finally {
            oad.devolverConexion(con);
        }
        return resultado;
    }

    /**
    * Función auxiliar encargada de colocar el operador (+) de oracle el el LEFT
    * @param condiciones las condiciones a tratar
    * @return la cadena con los (+) en su lugar correspondiente
    */
   private String trataCondicionesLeft(String condiciones){
    String r = "";
    String aux = condiciones.toUpperCase();
    int ini = 0;
    int hasta = aux.indexOf("=",ini);
    int hastaAnd = aux.indexOf(" AND ",hasta);
    int hastaOr = aux.indexOf(" OR ",hasta);
    while(!(hastaAnd == hastaOr)){//hay al menos dos condiciones separadas por un and o un or
       hasta++;
       //m_Log.debug("ra: " + r);
        r += aux.substring(ini,hasta-1);//meto la subcadena hasta el "="
        r += "*=";
       //m_Log.debug("rd:" + r);
       ini = hasta;//ahora estoy situado justo en el caracter detrás del "="
       hasta++;//un caracter detras de ini
       //m_Log.debug("entro en el bucle");
       while(" ".equals(aux.substring(ini,hasta))){//me salto los blancos detras del igual
        ini = hasta;
        hasta++;
       }
       //m_Log.debug("salgo del bucle");
       //al salir del bucle ini está en el primer caracter del nombre de la columna
       hasta = aux.indexOf(" ",ini);//busco el caracter en blanco detrás del nombre del campo
       //m_Log.debug("ra: " + r);
        r += aux.substring(ini,hasta);
       //m_Log.debug("rd: " + r);
       ini = hasta;//ahora estoy en el caracter en blanco detrás del nombre del campo
       hasta = aux.indexOf("=",ini);
       hastaAnd = aux.indexOf(" AND ",hasta);
       hastaOr = aux.indexOf(" OR ",hasta);
    }
    //m_Log.debug("rfa:" + r);

    hasta = aux.indexOf("=",ini);
    r += aux.substring(ini,hasta);
    r += "*=";
    r += aux.substring(hasta+1,aux.length());
    //m_Log.debug("rfd:" + r);
    return r;
   }

    /**
     * Metodo que introduce las comillas simples a un campo que es de tipo String y además comprueba que no tenga otras
     * comillas simples por el medio
     * @param dato El valor sin las comillas simples
     * @return El valor con las comillas simples
     */
    public String addString(String dato) {
        if ((dato != null) && (!dato.equalsIgnoreCase(""))) {
            int e = 0;
            int s = 0;
            StringBuffer result = new StringBuffer();
            while ((e = dato.indexOf("'", s)) >= 0) {
                result.append(dato.substring(s, e));
                result.append("''");
                s = e + 1;
            }
            result.append(dato.substring(s));
            dato = "'" + result.toString() + "'";
            return dato;
        } else {
            return null;
        }
    }

    public String minus(String sentencia, String condicionSelect){

        int numDivsSelects = sentencia.split("SELECT").length;
        int numDivsWheres = sentencia.split("WHERE").length;
        if ( numDivsSelects <=  numDivsWheres ){
            return " AND NOT EXISTS (" + condicionSelect + ") ";
        }else return " WHERE NOT EXISTS (" + condicionSelect + ") ";
    }

    public String intersect(String sentencia, String condicionSelect){
        String buscaWhere = sentencia.toUpperCase();
        if (buscaWhere.indexOf("WHERE")!=-1){
            return " AND EXISTS (" + condicionSelect + ") ";
        }else return " WHERE EXISTS (" + condicionSelect + ") ";
    }
    
    public String valorJoin() {
        return "LEFT";
    }

    public String consultaSinAcentos(Connection conexion, String[] params) {
        return this.funcionCadena(FUNCIONCADENA_IGNORAR_ACENTOS_MAYÚSCULAS, params);
   }
    
    public String whereEsNumero(String cadena) {
        return "isnumeric("+cadena+")=1";
	}
    
    public String whereNoEsNumero(String cadena) {
        return "isnumeric("+cadena+")!=1";
	}

    /**
     * Da la vuelta a una fecha
     * @param fecha
     * @return
     */
    private String vueltaFecha(String dato){
        dato = dato.replaceAll("'","").trim();
        String[] dFechaHora = dato.split(" ");
        StringBuffer aux = new StringBuffer();
        if(dFechaHora!=null && dFechaHora.length==2)
        {
            String[] dFecha = dFechaHora[0].split("/");
            if(dFecha!=null && dFecha.length==3){
                aux.append("'");
                aux.append(dFecha[2]);
                aux.append("/");
                aux.append(dFecha[1]);
                aux.append("/");
                aux.append(dFecha[0]);
                aux.append(" ");
                aux.append(dFechaHora[1]);
                aux.append("'");
            }
        }
        return aux.toString();
    }
    
    /** Esta funci?n obtener una fecha y transformarla en un varchar. Se puede utilizar para comparar fechas */
    public String castFecha(String dato){
        StringBuffer aux = new StringBuffer();
        aux.append("CAST(ISNULL(CONVERT(datetime,");
        aux.append(dato);
        aux.append(",103),");
        aux.append("''");
        aux.append(") AS VARCHAR)");
        return aux.toString();
    }

       public String consultaSinTabla(){
           return " ";
       }
       
   public String tamanoTexto(String dato){
       return "LEN ( "+dato+")";
   }

           /**
        * Recupera un substring dentro de otro
        * @param value_expression: String sobre el que se realiza la búsqueda
        * @param start_expression: Posición a partir de la cual se comienza a buscar
        * @param length_expression: Posición hasta la que se busca
        * @return
        */
   public String substr(String value_expression, String start_expression, String length_expression ){
        StringBuffer sb = new StringBuffer();
        //substring(PROCEDIMIENTO,1,CHARINDEX('§¥',PROCEDIMIENTO, 1)-1))
        sb.append("SUBSTRING(").append(value_expression).append(",").append(start_expression).append(",").append(length_expression).append(")");
        return sb.toString();
   }

     /**
     *  Posición de una determinada subcadena dentro de otra cadena
     * @param buscar: Cadena a buscar
     * @param contenedor: String que contiene la cadena buscada y referencia por el parámetro buscar
     * @param posicion: Posició a partir de la cual se comienza a buscar
     * @param ocurrencia: Primera ocurrencia a buscar
     * @return String
     */
   public String charIndex(String buscar,String contenedor,String posicion,String ocurrencia){
       StringBuffer sb = new StringBuffer();
       sb.append("CHARINDEX(").append(buscar).append(",").append(contenedor).append(",").append(posicion).append(")");
       return sb.toString();
   }
   
   public String diferenciaMeses(String fechaI, String fechaF)
   {
      
    String funcionFecha = ""; 
   
        funcionFecha = " DATEDIFF(month,"+fechaF+","+fechaI+")";
       
    return funcionFecha;
   
   }
}
