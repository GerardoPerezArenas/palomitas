// NOMBRE DEL PAQUETE
package es.altia.agora.business.util;

// PAQUETES	IMPORTADOS
import es.altia.util.conexion.AdaptadorSQL;
import java.sql.*;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.conexion.AdaptadorSQL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.StringTokenizer;
import java.util.Vector;

public class TransformacionAtributoSelect	{
    protected static char caracterEscape='·';
    protected static Log m_Log =
            LogFactory.getLog(TransformacionAtributoSelect.class.getName());

    private AdaptadorSQLBD oad;
    private Connection conexion;
 
    //Eliminadas referencias a este constructor
    //public TransformacionAtributoSelect() {
    //}

    public TransformacionAtributoSelect(AdaptadorSQLBD oad) {
        this.oad = oad;
    }

    public TransformacionAtributoSelect(AdaptadorSQLBD oad, Connection conexion) {
        this.oad = oad;
        this.conexion = conexion;
    }
 
    // El	caracter de	escape es \.
    public String tratarComodines(String s){
        String s_result= replace(s,"%",caracterEscape+"%");
        s_result= s_result.replace('*','%');
        return s_result;
    }

    public void	setCaracterEscape(char c){
        TransformacionAtributoSelect.caracterEscape = c;
    }

    public char	getCaracterEscape(){
        return TransformacionAtributoSelect.caracterEscape;
    }

    // Quite parentesis por problemas con busqueda de poblacion que los
    // incluye en su descripcion
    public String construirCondicionWhereConOperadores(String nombreCampo, String	strCondicion, boolean conOpIntervalo, boolean campoAlfanumerico){
        
        
        
        String delimitadorValor=""; // Por defecto ninguno.
        String operadores	= "&|";
        if (conOpIntervalo) operadores = "&|:><!=";

        String condicion="";
        if (campoAlfanumerico) delimitadorValor="'";

        if((strCondicion!=null)&&(!strCondicion.equals(""))){
            strCondicion = tratarComodines(strCondicion);
            StringTokenizer elementos = new StringTokenizer(strCondicion,operadores,true);
            Vector v = new Vector();

            while	(elementos.hasMoreTokens()) {

                String elemento =	elementos.nextToken();
                if ("|".equals(elemento))
                    v.addElement("OR");
                else if ("&".equals(elemento))
                    v.addElement("AND");
                else if ("!".equals(elemento))
                    v.addElement("<>");
                else  v.addElement(elemento);
            }

            String s="";
            if(m_Log.isDebugEnabled()) m_Log.debug(v);
            if (v.size() > 0)	{
                int i=0;
                condicion =	"(";
                while(i<v.size()){
                    s = (String) v.elementAt(i);
                    if ("AND".equals(s) || "OR".equals(s) || "(".equals(s) ||")".equals(s)){
                        condicion += " "+s+ " ";
                        i++;
                    } else if (!"".equals(s.trim())){
                        if ( ("<".equals(s)) ||	(">".equals(s)) || ("<>".equals(s))	){
                            if (i+1<v.size())	{
                                String valor = (String)	v.elementAt(i+1);
                                if ("=".equals(valor.trim()))	{
                                    if ((i+2) <v.size()) {
                                        valor	= (String) v.elementAt(i+2);
                                        valor = replace(valor,"'", "''");
                                        valor = "'" + valor + "'";
                                        valor = oad.funcionCadena(AdaptadorSQL.FUNCIONCADENA_UPPER, new String[]{valor});
                                        condicion += "( " + oad.consultaSinAcentos(conexion, new String[]{nombreCampo}) + " " + s.trim() + "= " + delimitadorValor + valor + delimitadorValor + ")";
                                        i+=3;
                                    } else i+=2; // ignoramos.
                                } else {
                                    valor = replace(valor,"'", "''");
                                    valor = "'" + valor + "'";
                                    valor = oad.funcionCadena(AdaptadorSQL.FUNCIONCADENA_UPPER, new String[]{valor});
                                    condicion += "( " + oad.consultaSinAcentos(conexion, new String[]{nombreCampo}) + " " + s.trim() + " " + delimitadorValor + valor + delimitadorValor + ")";
                                    i+=2;
                                }
                            } else i+=1; //ignoramos
                        } else {
                            String valor = s.trim();
                            
                            valor = replace(valor,"'","''");
                            valor = "'%" + valor + "%'";
                            valor = oad.funcionCadena(AdaptadorSQL.FUNCIONCADENA_UPPER, new String[]{valor});

                            if(i+1<v.size()){
                                String oper	= (String)v.elementAt(i+1);
                                if(m_Log.isDebugEnabled()) m_Log.debug(oper);
                                if(oper.trim().equals(":")){
                                    String op =	(String)v.elementAt(i+2);
                                    condicion += "( " + oad.consultaSinAcentos(conexion, new String[]{nombreCampo}) + " BETWEEN " + delimitadorValor + s.trim() + delimitadorValor + "	AND " + delimitadorValor + op.trim() + delimitadorValor + "	)";
                                    i+=3;
                                }else{
                                    nombreCampo = oad.funcionCadena(AdaptadorSQL.FUNCIONCADENA_UPPER, new String[]{nombreCampo});
                                    condicion += "( " + oad.consultaSinAcentos(conexion, new String[]{nombreCampo}) + " LIKE " + valor + ")";
                                    i++;
                                }
                            }else{
                                nombreCampo = oad.funcionCadena(AdaptadorSQL.FUNCIONCADENA_UPPER, new String[]{nombreCampo});
                                condicion += "( " + oad.consultaSinAcentos(conexion, new String[]{nombreCampo}) + " LIKE " + valor + ")";
                                i++;
                            }
                        }
                    } else condicion +="";
                }
                condicion +=")";
            } else {
                condicion = "";
            }
        }
                
        return condicion;
    }
    
    //Hace lo mismo que la funcion anterior, pero devuelve un vector con 2 objetos: la consulta sql con las bind variables en String
    // y un vector con los distintos valores para sustituir en las bind variables
     public Vector construirCondicionWhereConOperadoresBindVariables(String nombreCampo, String	strCondicion, boolean conOpIntervalo, boolean campoAlfanumerico){
        
        
        Vector filtros= new Vector();
        Vector <Object>retorno=new Vector();
        
        String delimitadorValor=""; // Por defecto ninguno.
        String operadores	= "&|";
        if (conOpIntervalo) operadores = "&|:><!="; 

        String condicion="";
        if (campoAlfanumerico) delimitadorValor="'";

        if((strCondicion!=null)&&(!strCondicion.equals(""))){
            strCondicion = tratarComodines(strCondicion);
            StringTokenizer elementos = new StringTokenizer(strCondicion,operadores,true);
            Vector v = new Vector();

            while	(elementos.hasMoreTokens()) {

                String elemento =	elementos.nextToken();
                if ("|".equals(elemento))
                    v.addElement("OR");
                else if ("&".equals(elemento))
                    v.addElement("AND");
                else if ("!".equals(elemento))
                    v.addElement("<>");
                else  v.addElement(elemento);
            }

            String s="";
            if(m_Log.isDebugEnabled()) m_Log.debug(v);
            if (v.size() > 0)	{
                int i=0;
                condicion =	"(";
                while(i<v.size()){
                    s = (String) v.elementAt(i);
                    if ("AND".equals(s) || "OR".equals(s) || "(".equals(s) ||")".equals(s)){
                        condicion += " "+s+ " ";
                        i++;
                    } else if (!"".equals(s.trim())){
                        if ( ("<".equals(s)) ||	(">".equals(s)) || ("<>".equals(s))	){
                            if (i+1<v.size())	{
                                String valor = (String)	v.elementAt(i+1);
                                if ("=".equals(valor.trim()))	{
                                    if ((i+2) <v.size()) {
                                        valor	= (String) v.elementAt(i+2);
                                        valor = replace(valor,"'", "''");
                                        filtros.add(valor);
                                        valor = " ? ";   
                                        valor = oad.funcionCadena(AdaptadorSQL.FUNCIONCADENA_UPPER, new String[]{valor});
                                        condicion += "( " + oad.consultaSinAcentos(conexion, new String[]{nombreCampo}) + " " + s.trim() + "= " +  valor +  ")";
                                        i+=3;
                                    } else i+=2; // ignoramos.
                                } else {
                                    valor = replace(valor,"'", "''");
                                   filtros.add(valor);
                                        valor = " ? ";   
                                    valor = oad.funcionCadena(AdaptadorSQL.FUNCIONCADENA_UPPER, new String[]{valor});
                                    condicion += "( " + oad.consultaSinAcentos(conexion, new String[]{nombreCampo}) + " " + s.trim() + " " +  valor +  ")";
                                    i+=2;
                                }
                            } else i+=1; //ignoramos
                        } else {
                            

                            if(i+1<v.size()){
                                String oper	= (String)v.elementAt(i+1);
                                if(m_Log.isDebugEnabled()) m_Log.debug(oper);
                                if(oper.trim().equals(":")){
                                    String op =	(String)v.elementAt(i+2);
                                    condicion += "( " + oad.consultaSinAcentos(conexion, new String[]{nombreCampo}) + " BETWEEN  ? AND ?)";
                                    String val1 = replace(s.trim(),"%","");
                                    String val2 = replace(op.trim(),"%","");
                                    filtros.add(val1);
                                    filtros.add(val2);
                                    i+=3;
                                }else{
                                    String valor = s.trim();
                                    
                                    valor = replace(valor,"'","''");                                    
                                    filtros.add(valor);
                                    valor = " ? ";     
                                   
                                    valor = oad.funcionCadena(AdaptadorSQL.FUNCIONCADENA_UPPER, new String[]{valor});
                                    nombreCampo = oad.funcionCadena(AdaptadorSQL.FUNCIONCADENA_UPPER, new String[]{nombreCampo});
                                    condicion += "( " + oad.consultaSinAcentos(conexion, new String[]{nombreCampo}) + " LIKE " + valor + ")";
                                    
                                    i++;
                                }
                            }else{
                                String valor = s.trim();
                                
                                valor = replace(valor,"'","''");
                               
                                filtros.add(valor);
                                valor = " ? ";                                
                                valor = oad.funcionCadena(AdaptadorSQL.FUNCIONCADENA_UPPER, new String[]{valor});
                                nombreCampo = oad.funcionCadena(AdaptadorSQL.FUNCIONCADENA_UPPER, new String[]{nombreCampo});
                                condicion += "( " + oad.consultaSinAcentos(conexion, new String[]{nombreCampo}) + " LIKE " + valor + ")";
                               
                                i++;
                            }
                        }
                    } else condicion +="";
                }
                condicion +=")";
            } else {
                condicion = "";
            }
        }
                
        retorno.add(condicion);
        retorno.add(filtros);
        return retorno;
    }

    public String construirCondicionWhereConOperadores(String	nombreCampo, String strCondicion, boolean	conOpIntervalo){
        return construirCondicionWhereConOperadores( nombreCampo, strCondicion, conOpIntervalo, true);
    }
    
    
    //Hace lo mismo que la funcion anterior, pero devuelve un vector con 2 objetos: la consulta sql con las bind variables en String
    // y un vector con los distintos valores para sustituir en las bind variables
    public Vector construirCondicionWhereConOperadoresBindVariables(String	nombreCampo, String strCondicion, boolean	conOpIntervalo){
        return construirCondicionWhereConOperadoresBindVariables( nombreCampo, strCondicion, conOpIntervalo, true);
    }

    public String construirCondicionWhereConOperadores(String	nombreCampo, String strCondicion){
        return construirCondicionWhereConOperadores(nombreCampo,strCondicion, true);
    }
    
    //Hace lo mismo que la funcion anterior, pero devuelve un vector con 2 objetos: la consulta sql con las bind variables en String
    // y un vector con los distintos valores para sustituir en las bind variables
     public Vector construirCondicionWhereConOperadoresBindVariables(String	nombreCampo, String strCondicion){
        return construirCondicionWhereConOperadoresBindVariables(nombreCampo,strCondicion, true);
    }

    public  String construirCondicionWhereConOperadoresCampoFecha(String nombreCampoFecha, String	strCondicion)
            throws BDException
    {
        String condicion="";
        if((strCondicion!=null)&&(!strCondicion.equals(""))){
            StringTokenizer elementos = new StringTokenizer(strCondicion,"&|():<>=!",true);
            Vector v = new Vector();
            while	(elementos.hasMoreTokens()) {
                String elemento	= elementos.nextToken();
                if ("|".equals(elemento))
                    v.addElement("OR");
                else if ("&".equals(elemento))
                    v.addElement("AND");
                else if ("!".equals(elemento))
                    v.addElement("<>");
                else v.addElement(elemento);
            }
            String s="";
            if (v.size() > 0)	{
                int	i=0;
                condicion	= "(";
                while(i<v.size()){
                    s = (String) v.elementAt(i);
                    if ("AND".equals(s)	|| "OR".equals(s)	|| "(".equals(s) ||")".equals(s)){
                        condicion += " "+s+ " ";
                        i++;
                    } else if	(!"".equals(s.trim())){
                        if ( ("<".equals(s)) ||	(">".equals(s)) || ("<>".equals(s))	){
                            if (i+1<v.size()){
                                String op = (String)v.elementAt(i+1);
								if ("=".equals(op.trim())) {           //opcion ">=" o "<="
                                    if (i+2<v.size())	{
                                        op = (String) v.elementAt(i+2);
                                        if (op.length()==4) op=	"01/01/" + op;
                                        else if (op.length()==7)	op= "01/" +op;
                                        else if (op.length()==10) op=	op;

                                        condicion += "(("+ oad.convertir(oad.convertir(nombreCampoFecha,
                                            AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY"),
												AdaptadorSQL.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")
												+ s +"="+ oad.convertir("'" + op.trim() + "'" ,
												AdaptadorSQL.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+"))";
                                        i+=3;
                                    } else i +=	2; //	ignoramos.
                                } else {
                                    if (op.length()==4) op=	"01/01/" + op;
                                    else if (op.length()==7)	op= "01/" +op;
                                    else if (op.length()==10) op=	op;

                                    condicion += "(("+ oad.convertir(oad.convertir(nombreCampoFecha,
											AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY"),
											AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")
											+ s + oad.convertir("'" + op.trim() + "'",
											AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+"))";
                                    i+=2;
                                }
                            }else i+=1; //ignoramos
                        } else {
                            if(i+1<v.size()){
                                String oper = (String)v.elementAt(i+1);
                                if(oper.trim().equals(":")){
                                    if (s.length()==4) s= "01/01/" + s;
                                    else if (s.length()==7)	s= "01/" +s;
                                    else if (s.length()==10) s= s;

                                    String op = (String)v.elementAt(i+2);

                                    if (op.length()==4) op= "01/01/" + op;
                                    else if (op.length()==7) op= "01/" +op;
                                    else if (op.length()==10) op= op;

									condicion += "( (" + oad.convertir(oad.convertir(nombreCampoFecha,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY"),AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+" BETWEEN "+oad.convertir("'"+	s.trim() + "'",
                                        AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + " AND " +
                                            oad.convertir("'"+	op.trim() + "'",
                                        AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY")+"))";
                                    i+=3;
                                }else{
                                    if (s.length()==4)
                                        condicion += "( (" + oad.convertir(oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,new String[]{"'01/01/'",oad.convertir(nombreCampoFecha,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"YYYY")}),AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+ " = " +
                                                    oad.convertir("'01/01/" + s.trim()+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+"))";
                                    else if (s.length()==7)
                                        condicion += "( (" + oad.convertir(oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,new String[]{"'01/'",oad.convertir(nombreCampoFecha,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"MM/YYYY")}),AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+ " = " +
                                                    oad.convertir("'01/" + s.trim()+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+"))";
                                    else if (s.length()==10)
                                        condicion += "( (" + oad.convertir(oad.convertir(nombreCampoFecha,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY"),AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+ " = " +
                                                    oad.convertir("'"+s.trim()+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+"))";
                                    i++;
                                }
                            }else{
                                    if (s.length()==4)
                                        condicion += "( (" + oad.convertir(oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,new String[]{"'01/01/'",oad.convertir(nombreCampoFecha,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"YYYY")}),AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+ " = " +
                                                    oad.convertir("'01/01/" + s.trim()+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+"))";
                                    else if (s.length()==7)
                                        condicion += "( (" + oad.convertir(oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,new String[]{"'01/'",oad.convertir(nombreCampoFecha,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"MM/YYYY")}),AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+ " = " +
                                                    oad.convertir("'01/" + s.trim()+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+"))";
                                    else if (s.length()==10)
                                        condicion += "( (" + oad.convertir(oad.convertir(nombreCampoFecha,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY"),AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+ " = " +
                                                    oad.convertir("'"+s.trim()+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+"))";
                                i++;
                            }
                        }
                    }else condicion +="";
                }
                condicion +=")";
            } else {
                condicion = "";
            }
        }

		m_Log.debug("********************"+condicion+"**********************");
        return condicion;
    }
    
    //Hace lo mismo que la funcion anterior, pero devuelve un vector con 2 objetos: la consulta sql con las bind variables en String
    // y un vector con los distintos valores para sustituir en las bind variables
    
    public  Vector construirCondicionWhereConOperadoresCampoFechaBindVariables(String nombreCampoFecha, String	strCondicion)
            throws BDException
    {
        
        Vector filtros= new Vector();
        Vector <Object>retorno=new Vector();
        
        String condicion="";
        if((strCondicion!=null)&&(!strCondicion.equals(""))){
            StringTokenizer elementos = new StringTokenizer(strCondicion,"&|():<>=!",true);
            Vector v = new Vector();
            while	(elementos.hasMoreTokens()) {
                String elemento	= elementos.nextToken();
                if ("|".equals(elemento))
                    v.addElement("OR");
                else if ("&".equals(elemento))
                    v.addElement("AND");
                else if ("!".equals(elemento))
                    v.addElement("<>");
                else v.addElement(elemento);
            }
            String s="";
            if (v.size() > 0)	{
                int	i=0;
                condicion	= "(";
                while(i<v.size()){
                    s = (String) v.elementAt(i);
                    if ("AND".equals(s)	|| "OR".equals(s)	|| "(".equals(s) ||")".equals(s)){
                        condicion += " "+s+ " ";
                        i++;
                    } else if	(!"".equals(s.trim())){
                        if ( ("<".equals(s)) ||	(">".equals(s)) || ("<>".equals(s))	){
                            if (i+1<v.size()){
                                String op = (String)v.elementAt(i+1);
								if ("=".equals(op.trim())) {           //opcion ">=" o "<="
                                    if (i+2<v.size())	{
                                        op = (String) v.elementAt(i+2);
                                        if (op.length()==4) op=	"01/01/" + op;
                                        else if (op.length()==7)	op= "01/" +op;
                                        else if (op.length()==10) op=	op;

                                        condicion += "(("+ oad.convertir(oad.convertir(nombreCampoFecha,
                                            AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY"),
												AdaptadorSQL.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")
												+ s +"="+ oad.convertir("?" ,
												AdaptadorSQL.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+"))";
                                        filtros.add(op.trim());
                                        i+=3;
                                    } else i +=	2; //	ignoramos.
                                } else {
                                    if (op.length()==4) op=	"01/01/" + op;
                                    else if (op.length()==7)	op= "01/" +op;
                                    else if (op.length()==10) op=	op;

                                    condicion += "(("+ oad.convertir(oad.convertir(nombreCampoFecha,
											AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY"),
											AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")
											+ s + oad.convertir("?",
											AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+"))";
                                    filtros.add(op.trim());
                                    i+=2;
                                }
                            }else i+=1; //ignoramos
                            
                        } else {
                            if(i+1<v.size()){
                                String oper = (String)v.elementAt(i+1);
                                if(oper.trim().equals(":")){
                                    if (s.length()==4) s= "01/01/" + s;
                                    else if (s.length()==7)	s= "01/" +s;
                                    else if (s.length()==10) s= s;

                                    String op = (String)v.elementAt(i+2);

                                    if (op.length()==4) op= "01/01/" + op;
                                    else if (op.length()==7) op= "01/" +op;
                                    else if (op.length()==10) op= op;

									condicion += " "+nombreCampoFecha+" BETWEEN "+oad.convertir("?",
                                        AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + " AND " +
                                            oad.convertir("?",
                                        AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY");
                                        filtros.add(s.trim());
                                        filtros.add(op.trim());
                                    i+=3;
                                }else{
                                    if (s.length()==4){
                                        condicion += "( (" + oad.convertir(oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,new String[]{"'01/01/'",oad.convertir(nombreCampoFecha,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"YYYY")}),AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+ " = " +
                                                    oad.convertir("?",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+"))";
                                        String fecha="01/01/"+ s.trim();
                                        filtros.add(fecha);  
                                    }
                                    else if (s.length()==7){
                                        condicion += "( (" + oad.convertir(oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,new String[]{"'01/'",oad.convertir(nombreCampoFecha,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"MM/YYYY")}),AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+ " = " +
                                                    oad.convertir("?",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+"))";
                                        String fecha="01/"+ s.trim();
                                        filtros.add(fecha);  
                                    }
                                    else if (s.length()==10){
                                        condicion += "( (" + oad.convertir(oad.convertir(nombreCampoFecha,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY"),AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+ " = " +
                                                    oad.convertir("?",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+"))";
                                        String fecha=s.trim();
                                        filtros.add(fecha);  
                                    }
                                    i++;
                                }
                            }else{
                                    if (s.length()==4){
                                        condicion += "( (" + oad.convertir(oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,new String[]{"'01/01/'",oad.convertir(nombreCampoFecha,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"YYYY")}),AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+ " = " +
                                                    oad.convertir("?",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+"))";
                                        String fecha="01/01/"+ s.trim();
                                        filtros.add(fecha);  
                                    }
                                    else if (s.length()==7){
                                        condicion += "( (" + oad.convertir(oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,new String[]{"'01/'",oad.convertir(nombreCampoFecha,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"MM/YYYY")}),AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+ " = " +
                                                    oad.convertir("?",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+"))";
                                        String fecha="01/"+ s.trim();
                                        filtros.add(fecha);  
                                    }
                                    else if (s.length()==10){
                                        condicion += "( (" + oad.convertir(oad.convertir(nombreCampoFecha,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY"),AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+ " = " +
                                                    oad.convertir("?",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+"))";
                                        String fecha=s.trim();
                                        filtros.add(fecha);  
                                    }
                                i++;
                            }
                        }
                    }else condicion +="";
                }
                condicion +=")";
            } else {
                condicion = "";
            }
        }

		m_Log.debug("********************"+condicion+"**********************");
        retorno.add(condicion);
        retorno.add(filtros);
        return retorno;
    }

    public String construirCondicionWhereConOperadoresCampoNumerico(String nombreCampo, String	strCondicion){
        String condicion="";
        if((strCondicion!=null)&&(!strCondicion.equals(""))){
            StringTokenizer elementos = new StringTokenizer(strCondicion,"&|():<>!",true);
            Vector v = new Vector();
            while	(elementos.hasMoreTokens()) {
                String elemento	= elementos.nextToken();
                if ("|".equals(elemento))
                    v.addElement("OR");
                else if ("&".equals(elemento))
                    v.addElement("AND");
                else if ("!".equals(elemento))
                    v.addElement("<>");
                else v.addElement(elemento);
            }
            String s="";
            if (v.size() > 0)	{
                int	i=0;
                condicion	= "(";
                while(i<v.size()){
                    s = (String) v.elementAt(i);
                    if ("AND".equals(s)	|| "OR".equals(s)	|| "(".equals(s) ||")".equals(s)){
                        condicion += " "+s+ " ";
                        i++;
                    } else if	(!"".equals(s.trim())){
                        if ( ("<".equals(s)) ||	(">".equals(s)) || ("<>".equals(s))	){
                            if (i+1<v.size()){
                                String valor = (String)	v.elementAt(i+1);
                                if ("=".equals(valor.trim()))	{
                                    if ((i+2) <v.size()) {
                                        valor	= (String) v.elementAt(i+2);
                                        condicion += "( "+nombreCampo+" "+s.trim()+"="+oad.convertir("'"+  valor  +"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_NUMERO,"99999999D99")+")";
                                        i+=3;
                                    } else i+=2; // ignoramos.
                                } else {
                                    condicion += "( "+nombreCampo+" "+s.trim()+oad.convertir("'"+  valor  +"'", AdaptadorSQLBD.CONVERTIR_COLUMNA_NUMERO,"99999999D99")+")";
                                    i+=2;
                                }
                            } else i+=1; //ignoramos
                        } else {
                            String valor = s.trim();
                            //m_Log.debug("valor " + valor);
                            if(i+1<v.size()){
                                String oper	= (String)v.elementAt(i+1);
                                if(m_Log.isDebugEnabled()) m_Log.debug(oper);
                                if(oper.trim().equals(":")){
                                    String op =	(String)v.elementAt(i+2);

                                    condicion += "( "+nombreCampo+" BETWEEN " +
                                                    oad.convertir("'"+  s.trim()  +"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_NUMERO,"99999999D99") + " AND " +
                                                    oad.convertir("'"+  op.trim()  +"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_NUMERO,"99999999D99")+ ")";
                                    i+=3;
                                } else {
                                    condicion += "( "+nombreCampo+" "+oper.trim() + oad.convertir("'"+ valor +"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_NUMERO,"99999999D99") + ")";
                                    i+=2;
                                }
                            } else {
                                condicion += "( "+nombreCampo+" = "+oad.convertir("'"+ valor +"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_NUMERO,"99999999D99")+")";
                                i+=2;
                            }
                        }
                    } else condicion +="";
                }
                condicion +=")";
            } else {
                condicion = "";
            }
        }
        return condicion;
    }

    public static String replace(String str,	String pattern, String replace) {
        int s = 0;
        int e =0;
        StringBuffer result	= new	StringBuffer();
        if(str == null) return null;
        while ((e = str.indexOf(pattern, s)) >= 0) {
            result.append(str.substring(s, e));
            result.append(replace);
            s = e+pattern.length();
        }
        result.append(str.substring(s));
        return result.toString();
    }

    /* Funcion que construye parte de la condicion where con condiciones sobre los valores de un atributo de una tabla del join y permite	que sean nulos */

    public String construirCondicionWhereConOperadoresNuloYJoin(String nmbCampoTablaP, String nmbCampoTablaS,	String nmbCampoTablaSJoin, String strCondicion,	boolean conOpIntervalo,	boolean campoAlfanumerico){

        strCondicion = strCondicion.trim();
        String res="(";
        int posNulo	= strCondicion.indexOf("#",0);
        if ( posNulo >= 0){ // Permite campo nulo.
            res += "( "+nmbCampoTablaP+" IS NULL )  ";
            // Quitar condicion nulo.
            if (posNulo==0){ // Es el primero.
                int posOp = (strCondicion.indexOf("|",0) > strCondicion.indexOf("&",0) )?strCondicion.indexOf("|",0):strCondicion.indexOf("&",0);
                if (posOp >= 0)
                    strCondicion = strCondicion.substring(posOp+1);
                else // No hay operadores
                    strCondicion = "";
            } else { //	No es	primero.
                int posOp =	0;
                int posOp2=	(strCondicion.indexOf("|",0) > strCondicion.indexOf("&",0) )?strCondicion.indexOf("|",0):strCondicion.indexOf("&",0);
                while	((posOp2>0) && (posOp2 < posNulo)){
                    posOp	= posOp2;
                    posOp2= (strCondicion.indexOf("|",posOp+1) > strCondicion.indexOf("&",posOp+1) )?strCondicion.indexOf("|",posOp+1):strCondicion.indexOf("&",posOp+1);
                    if(m_Log.isDebugEnabled()) m_Log.debug("Posiciones posNulo posOp posOp2 "+ posNulo + " "+ posOp +" "+posOp2);
                }
                strCondicion = strCondicion.substring(0,posOp) + strCondicion.substring(posNulo+1);
            }
        }
        if (!"".equals(strCondicion.trim())){
            if (posNulo >= 0) res += " OR ";
            res += "( ( "+nmbCampoTablaP +"="+nmbCampoTablaSJoin +") ";
            String c = construirCondicionWhereConOperadores(nmbCampoTablaS, strCondicion.trim(), conOpIntervalo,campoAlfanumerico);
            if (!"".equals(c.trim())){
                res += " AND ("+c+")";
            }
            res += ")";
        }
        res += ")";
        return res;
    }

    public static String escapeValorParaXML(String str) {
        String aux = str;
        aux = TransformacionAtributoSelect.replace(aux,"&","&amp;");
        aux = TransformacionAtributoSelect.replace(aux,"<","&lt;");
        return aux;
    }

}
