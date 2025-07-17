package es.altia.util.conexion;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que extiende a AdaptadorSQLBD para dar soporte a Oracle
 * <p>Título: </p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: Altia Consultaros & AytosCPD</p>
 * @author Manuel Vera Silvestre
 * @version 1.0
 */
public class AdaptadorSQLOracle extends AdaptadorSQLBD{

   public String convertir(String dato, int tipo, String fmt){
    String convertir = "";
    String auxTipo = "";
    String nulo = "";
    /* Determinamos el valor para nulo */
    switch(tipo){
       case (CONVERTIR_COLUMNA_NUMERO):
                auxTipo = "TO_NUMBER(" + dato;
                if(fmt != null)
                    auxTipo += ",'" + fmt + "'";
                auxTipo += ")";
        nulo = "0";
        break;
       case(CONVERTIR_COLUMNA_FECHA):
                auxTipo = "TO_DATE(" + dato ;
                if(fmt != null)
                    auxTipo += ",'" + fmt + "'";
                auxTipo += ")";
        nulo = "''";
        break;
       case(CONVERTIR_COLUMNA_TEXTO):
        auxTipo = "TO_CHAR(" + dato ;
        if(fmt != null)
           auxTipo += ",'" + fmt + "'";
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
           fechaHora = " TO_DATE('"+valor+"','DD/MM/YYYY') ";
        break;
       case(FECHAHORA_CAMPO_HORA):
           s = new SimpleDateFormat("HH:mm:ss");
           d = s.parse(valor);
           //s.applyPattern("HH:mm:ss");
           //fechaHora = " {t '" + s.format(d) + "'}";
           fechaHora = " TO_DATE('"+valor+"','HH24:MI:SS') ";
        break;
       case(FECHAHORA_CAMPO_FECHA_HORA):
           s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
           d = s.parse(valor);
           //s.applyPattern("yyyy-MM-dd HH:mm:ss");
           //fechaHora = " {ts '" + s.format(d) + "'}";
           fechaHora = " TO_DATE('"+valor+"','DD/MM/YYYY HH24:MI:SS') ";
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
        funcionCadena = " CHR(" + parametros[0] + ") ";
        break;
       case(FUNCIONCADENA_CONCAT):
        funcionCadena = " NVL(" + parametros[0] + ",'')";
            for (int i=1;i<parametros.length;i++){
           funcionCadena = funcionCadena + " || " + " NVL(" + parametros[i] + ",'')";
            }
        break;
       case(FUNCIONCADENA_LOWER):
        funcionCadena = " LOWER(" + parametros[0] + ") ";
        break;
       case(FUNCIONCADENA_LENGTH):
        funcionCadena = " LENGTH(" + parametros[0] + ") ";
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
        funcionCadena = " SUBSTR(" + parametros[0] + "," +
                    parametros[1] + "," + parametros[2] + ") ";
        break;
       case(FUNCIONCADENA_UPPER):
        funcionCadena = " UPPER(" + parametros[0] + ") ";
        break;
       case(FUNCIONCADENA_INITCAP):
         funcionCadena = " INITCAP(" + parametros[0] + ") ";
         break;
            case(FUNCIONCADENA_LPAD):
        funcionCadena = "LPAD( "+parametros[0]+","+parametros[1]+")";
                break;
            case(FUNCIONCADENA_TRIM):
         funcionCadena = "TRIM( "+parametros[0]+")";
         break;
        case(FUNCIONCADENA_TRANSLATE):
         funcionCadena = "TRANSLATE( "+parametros[0]+","+parametros[1]+","+parametros[2]+")";
                break;
            case (FUNCIONCADENA_IGNORAR_ACENTOS_MAYÚSCULAS):
                funcionCadena = parametros[0];
                break;
    }
    return funcionCadena;
   }

   public String funcionFecha(int funcion, String[] parametros){
    String funcionFecha = "";
    switch(funcion){
       case(FUNCIONFECHA_SYSDATE):
        funcionFecha = " SYSDATE ";
        break;
       case(FUNCIONFECHA_TRUNCAR_HORA):
           funcionFecha = String.format(" TRUNC(%s) ", parametros[0]);
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
        funcionMatematica = " CEIL(" + parametros[0] + ") ";
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
        funcionMatematica = " LOG(" + parametros[0] + "," +
                      parametros[1] + ") ";
        break;
       case(FUNCIONMATEMATICA_LN):
        funcionMatematica = " LN(" + parametros[0] + ") ";
        break;
       case(FUNCIONMATEMATICA_MOD):
        funcionMatematica = " MOD(" + parametros[0] + "," +
                      parametros[1] + ") ";
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
            case(FUNCIONMATEMATICA_AVG):
                funcionMatematica = " AVG(" + parametros[0] + ") ";
                break;
            case(FUNCIONMATEMATICA_MIN):
                funcionMatematica = " MIN(" + parametros[0] + ") ";
                break;
    }
    return funcionMatematica;
   }

   public String funcionSistema(int funcion, String[] parametros){
    String funcionSistema = "";
    switch(funcion){
       case(FUNCIONSISTEMA_USER):
        funcionSistema = " USER ";
        break;
       case(FUNCIONSISTEMA_NVL):
        funcionSistema = " NVL(" + parametros[0] + "," +
                     parametros[1] + ") ";
        break;
    }
    return funcionSistema;
   }

    public String getSymbolConcat(){
        return "||";
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
        group = " GROUP BY ROLLUP(" + group.substring(1) + ")";//quito la primera ","
        return group;
    }

/**
 * @deprecated 
 * @param campoIzqda
 * @param campoDrcha
 * @return
 */

   public String joinLeft(String campoIzqda, String campoDrcha){
       return (campoIzqda + " = " + campoDrcha + " (+) ");
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
       return (campoIzqda + " (+) = " + campoDrcha);
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
    //String from ="SELECT " + partefrom + " FROM ";
	   String from="";
        String where = " WHERE ";
    String aux = parametros[0];
    String[] tablas;
    String[] condiciones;
    String[] joins;
    int dimension;
    String join = "";

    /*comprobamos que los parámetros se suministren correctamente*/
    if((parametros.length-2) % 3 == 0){
       dimension = (parametros.length-2) / 3;
       condiciones = new String[dimension];
       joins = new String[dimension];
       tablas = new String[dimension+1];
       tablas[0] = parametros[0];
       //Si no se anida
       if(!(parametros[parametros.length-1].equalsIgnoreCase("true"))){
    	   from= "SELECT " + partefrom + " FROM ";
        for(int i = 0; i < dimension; i++){
           joins[i] = parametros[3*i+1];//guardo los tipos de join
           condiciones[i] = parametros[3*i+3];//guardo las condiciones
           tablas[i+1] = parametros[3*i+2];//guardo las tablas
           if(aux.indexOf(tablas[i+1]) == -1)//si no se ha añadido ya esta tabla al from
            aux += "," + tablas[i+1];//la añado
        }
        from += aux;//concateno los nombres de las tablas
                if (partewhere!=null && partewhere.length() != 0) {
                    where = where + partewhere;
                }
        for(int i = 0; i < dimension; i++){
           if(joins[i].equalsIgnoreCase("left")){
                        if(i == 0 && (partewhere == null || partewhere.length() == 0))//si es la primera condicion y no hay parte where
               where = where + trataCondicionesLeft(condiciones[i]);
            else
               where = where + " AND " + trataCondicionesLeft(condiciones[i]);
                    } else if(joins[i].equalsIgnoreCase("right")){
                        if(i == 0 && (partewhere == null || partewhere.length() == 0))//si es la primera condicion y no hay parte where
                            where = where + trataCondicionesRight(condiciones[i]);
                        else
                            where = where + " AND " + trataCondicionesRight(condiciones[i]);
           }
           else{
                        if(i == 0 && (partewhere == null || partewhere.length() == 0))//si es la primera condición y no hay parte where
               where = where + condiciones[i];
            else
               where = where + " AND " + condiciones[i];
           }
        }
        join = from + where;
       }
       else{
        /*código para realizar la anidación*/
    	   
    	   from = " SELECT * FROM ";
           for(int i = 0; i < dimension; i++){
               joins[i] = parametros[3*i+1];//guardo los tipos de join
               condiciones[i] = parametros[3*i+3];//guardo las condiciones
               tablas[i+1] = parametros[3*i+2];//guardo las tablas
               if(aux.indexOf(tablas[i+1]) == -1)//si no se ha añadido ya esta tabla al from
                aux += "," + tablas[i+1];//la añado
            }
            from += aux;//concateno los nombres de las tablas
                    if (partewhere!=null && partewhere.length() != 0) {
                        where = where + partewhere;
                    }
            for(int i = 0; i < dimension; i++){
               if(joins[i].equalsIgnoreCase("left")){
                            if(i == 0 && (partewhere == null || partewhere.length() == 0))//si es la primera condicion y no hay parte where
                   where = where + trataCondicionesLeft(condiciones[i]);
                else
                   where = where + " AND " + trataCondicionesLeft(condiciones[i]);
                        } else if(joins[i].equalsIgnoreCase("right")){
                            if(i == 0 && (partewhere == null || partewhere.length() == 0))//si es la primera condicion y no hay parte where
                                where = where + trataCondicionesRight(condiciones[i]);
                            else
                                where = where + " AND " + trataCondicionesRight(condiciones[i]);
               }
               else{
                            if(i == 0 && (partewhere == null || partewhere.length() == 0))//si es la primera condición y no hay parte where
                   where = where + condiciones[i];
                else
                   where = where + " AND " + condiciones[i];
               }
            }
            join = " (" + from + where + ")";    	   
    	   
       }
    }
    else
       throw new BDException("Error al construir el join. El número de " +
                      "parámetros pasados en el tercer argumento " +
                      "no eran correctos");
    return join;
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
            m_Log.info("AdaptadorSQLOracle.devolverNextValSecuencia");
            oad = new AdaptadorSQLBD(parametros);
            con = oad.getConnection();
            PreparedStatement ps =
                    con.prepareStatement(
                            " select " + secuencia + ".nextval from dual");
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                resultado = rs.getLong(1);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
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
       r += aux.substring(ini,hasta);//meto la subcadena hasta el "="
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
       r += aux.substring(ini,hasta) + "(+)";//le añado el operador de outer join
       //m_Log.debug("rd: " + r);
       ini = hasta;//ahora estoy en el caracter en blanco detrás del nombre del campo
       hasta = aux.indexOf("=",ini);
       hastaAnd = aux.indexOf(" AND ",hasta);
       hastaOr = aux.indexOf(" OR ",hasta);
    }
    //m_Log.debug("rfa:" + r);
    r += aux.substring(ini,aux.length()) + "(+) ";
    //m_Log.debug("rfd:" + r);
    return r;
   }

    /**
     * Función auxiliar encargada de colocar el operador (+) de oracle el el RIGHT
     * @param condiciones las condiciones a tratar
     * @return la cadena con los (+) en su lugar correspondiente
     */
    private String trataCondicionesRight(String condiciones){
        String r = "";
        String join = "(+) = ";
        java.util.StringTokenizer st = new java.util.StringTokenizer(condiciones, "=");
        while (st.hasMoreElements()) {
            r = r + (String) st.nextElement() + join;
        }
        return r.substring(0,r.length()-join.length());
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
        return " MINUS " + condicionSelect;
    }


    public String intersect(String sentencia, String condicionSelect){
        return " INTERSECT " + condicionSelect;
    }
    
    public String valorJoin() {
        return "RIGHT";
    }

    public String consultaSinAcentos (Connection conexion, String[] params){
//        try {
//            if (conexion!=null){
//            String alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
//            String alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";
//            PreparedStatement pStatement = conexion.prepareStatement(alter1);
//            pStatement.executeQuery();
//            m_Log.debug(alter1);
//            pStatement = conexion.prepareStatement(alter2);
//            pStatement.executeQuery();     
//            m_Log.debug(alter2);
//            }
//            else {
//            m_Log.debug("No se modifica la sesión de Oracle para ser insensible a acentos (no se posee la conexión activa).");
//            }

//        } catch (SQLException ex) {
//            Logger.getLogger(AdaptadorSQLOracle.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return this.funcionCadena(FUNCIONCADENA_IGNORAR_ACENTOS_MAYÚSCULAS, params);        
    }
    public String whereEsNumero(String cadena) {
        return ("replace(translate(" + cadena + ",'N01234567890','XNNNNNNNNNN'),'N',null) is null");        
    }
    public String whereNoEsNumero(String cadena) {
        return ("replace(translate(" + cadena + ",'N01234567890','XNNNNNNNNNN'),'N',null) is not null");
    }

    public String consultaSinTabla (){
        return " FROM DUAL";
    }
    
     public String tamanoTexto(String dato){
       return "LENGTH ( "+dato+")";
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
        sb.append("SUBSTR(").append(value_expression).append(",").append(start_expression).append(",").append(length_expression).append(")");
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
   public String charIndex(String buscar,String contenedor,String posicion, String ocurrencia){
       StringBuffer sb = new StringBuffer();
       //INSTR(PROCEDIMIENTO,  '§¥', 1, 1)
       sb.append("INSTR(").append(contenedor).append(",").append(buscar).append(",").append(posicion).append(",").append(ocurrencia).append(")");
       return sb.toString();
   }
   
   public String diferenciaMeses(String fechaI, String fechaF)
   {
      
    String funcionFecha = "";
         
        funcionFecha = " MONTHS_BETWEEN("+fechaI+","+fechaF+") ";
     
    return funcionFecha;
   
   }

}
