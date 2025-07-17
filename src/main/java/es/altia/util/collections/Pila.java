package es.altia.util.collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Stack;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * Created by IntelliJ IDEA.
 * User: Daniel.Sambad
 * Date: 08-may-2006
 * Time: 19:00:36
 * To change this template use File | Settings | File Templates.
 */
public class Pila {
    public Stack pila;
    public String[] camposExpediente;
    public int[] tiposExpediente;
    public String[] datosExpediente;
    public String[] camposTramite;
    public int[] tiposTramite;
    public String[] datosTramite;
    
    private String camposAnalizados;
    
    protected static Log m_Log =
            LogFactory.getLog(Pila.class.getName());
    private static String[] elementosEspeciales={"(",")","OPERADOR_AND","OPERADOR_OR","<>","<=",">=","<",">","=","LIKE","IS NULL","IS NOT NULL","BETWEEN"};

    public Pila() {
        this.pila = new Stack();
        this.camposAnalizados = "";
    }

    public Stack getPila() {
        return pila;
    }

    public void setPila(Stack pila) {
        this.pila = pila;
    }

    public String pop(){
        return (String) pila.pop();
    }

    public void push(String elemento) {
        pila.push(elemento.trim());
    }

    private int getPosicionPrimerCaracterEspecial(String cadena) {
        int pos=-1;
        for (int i=0;i<elementosEspeciales.length;i++) {
            int aux = cadena.indexOf(elementosEspeciales[i]);
            if (aux != -1)
                if ((pos==-1) || (aux<pos)) pos=aux;
        }
        return pos;
    }

    private String getPrimerCaracterEspecial(String cadena) {
        int pos=-1;
        String especial="";
        for (int i=0;i<elementosEspeciales.length;i++) {
            int aux = cadena.indexOf(elementosEspeciales[i]);
            if (aux != -1)
                if ((pos==-1) || (aux<pos)) {
                    pos=aux;
                    especial = elementosEspeciales[i];
                }
        }
        return especial;
    }

    public Stack rellenarPila(String cadena) throws Exception{
     try {
        String aux="("+cadena+")", especial="", aux2="";
        aux = aux.replaceAll(" AND "," OPERADOR_AND ");
        aux = aux.replaceAll(" OR "," OPERADOR_OR ");
        m_Log.debug("aux: "+aux);
        int pos=0;
        m_Log.debug("CADENA INICIO.........."+cadena);
        while ((aux!=null) && (!aux.equals(""))) {
            // Recoger posicion caracter especial
            pos = getPosicionPrimerCaracterEspecial(aux);
            // Recoger caracter especial
            especial =getPrimerCaracterEspecial(aux);
            // Si no encuentra ninguna se acaban las insercciones
            if (pos==-1) aux="";
            // Si la posicion es mayor que el primer caracter se inserta el trozo que hay antes, que es un campo o valor del mismo
            if (pos>0) {
                aux2=aux.substring(0,pos);
                push(aux2);
            }
            else aux2="";
            m_Log.debug("ESPECIAL...."+especial);
            m_Log.debug("CAMPO   ...."+aux2);
            m_Log.debug("PILA......................."+pila);
            //Inserccion del caracter especial
            push(especial);
            // Si encontramos un ')' entonces buscamos el parétesis de apertura que le corresponde, analizando la sentencia
            if (especial.equals(")")) {
                // Eliminamos el paréntesis de cerrado
                pop();
                // Primer campo en la pila es el valor
                String valor=pop();
                // Segundo campo en la pila es el comparador
                if ((valor.equals("IS NULL"))||(valor.equals("IS NOT NULL"))) { // El operador NULL,NOT NULL no tiene segundo valor
                    String campo=pop();
                    camposAnalizados += campo + "|";
                    // Eliminamos el paréntesis de apertura
                    pop();
                    m_Log.debug("Llama a compararCampo con "+campo+" "+valor);
                    push(compararCampo(campo,valor,""));
                }
                else {
                    String comparador=pop();
                    if (comparador.equals("(")) { //Caso en el que un valor está entre dos paréntesis
                        push(valor);
                    }
                    // Tercer campo en la pila es el campo
                    else if (comparador.equals("OPERADOR_AND") || (comparador.equals("OPERADOR_OR"))) {
                        String cad=comparador+" "+valor;
                        String aux3=pop();
                        while (!aux3.equals("(")) {
                            cad=aux3+" "+cad;
                            aux3=pop();
                        }
                        push(analizar(cad));
                    }
                    else {
                        String campo=pop();
                        camposAnalizados += campo + "|";
                        // Eliminamos el paréntesis de apertura
                        pop();
                        m_Log.debug("Llama a compararCampo con "+campo+" "+comparador+" "+valor);
                        push(compararCampo(campo,comparador,valor));
                    }
                }
            }

            // Recomponemos la cadena sin los trozos que insertamos
            aux=aux.substring(pos+especial.length(),aux.length()).trim();
        }
     } catch (Exception e) {
         throw e;
     }
        return pila;
    }

    private int buscarCampo(String campo, String[] matriz) {
        m_Log.debug("LOG MATRIZ: "+matriz.length);
        int resultado = -1;
        for (int i=0; i< matriz.length; i++) {
            if (matriz[i].equals(campo)) {
                resultado=i;
                break;
            }
        }
        return resultado;
    }

    private String compararCampo(String campo, String comparador, String valor) throws Exception{
        boolean resultado=false;
        try {
        m_Log.debug("LOG EXPEDIENTE: "+camposExpediente.length);
        int posicionExpediente = buscarCampo(campo, camposExpediente);
        m_Log.debug("POSICION EXPEDIENTE: "+posicionExpediente);
        if (posicionExpediente!=-1) {
            if (camposExpediente[posicionExpediente].toUpperCase().equals(campo.toUpperCase())) {
                int tipo=tiposExpediente[posicionExpediente];
                switch (tipo) {
                    case 8:
                            //los tipos 8 y 1 (numerico calculado y numerico, respectivamente) se comparan de la misma forma
                    case 1:
                        m_Log.debug("EN TIPO 1 DE EXPEDIENTE");
                        m_Log.debug("VALOR: "+valor);
                        m_Log.debug("datosExpediente[posicionExpediente]: "+datosExpediente[posicionExpediente]);
                        m_Log.debug("COMPARADOR: "+comparador);
                        if (comparador.equals("IS NULL"))
                            resultado = (datosExpediente[posicionExpediente] == null);
                        else if (datosExpediente[posicionExpediente] == null)
                            resultado = false;
                        else if (comparador.equals("IS NOT NULL"))
                            resultado = true;
                        else if (comparador.equals("="))
                            resultado = (Double.valueOf(datosExpediente[posicionExpediente]).doubleValue()==Double.valueOf(valor).doubleValue());
                        else if (comparador.equals("<>")){
                            m_Log.debug("VALOR EXPEDIENTE "+Double.valueOf(datosExpediente[posicionExpediente]).doubleValue());
                            m_Log.debug("VALOR            "+Double.valueOf(valor).doubleValue());
                            resultado = (Double.valueOf(datosExpediente[posicionExpediente]).doubleValue()!=Double.valueOf(valor).doubleValue());
                        }
                        else if (comparador.equals("<")){
                            m_Log.debug("VALOR EXPEDIENTE "+Double.valueOf(datosExpediente[posicionExpediente]).doubleValue());
                            m_Log.debug("VALOR            "+Double.valueOf(valor).doubleValue());
                            resultado = (Double.valueOf(datosExpediente[posicionExpediente]).doubleValue()<Double.valueOf(valor).doubleValue());
                        }
                        else if (comparador.equals(">")){
                            resultado = (Double.valueOf(datosExpediente[posicionExpediente]).doubleValue()>Double.valueOf(valor).doubleValue());
                        }
                        else if (comparador.equals("<=")){
                            resultado = (Double.valueOf(datosExpediente[posicionExpediente]).doubleValue()<=Double.valueOf(valor).doubleValue());
                        }
                        else if (comparador.equals(">=")){
                            resultado = (Double.valueOf(datosExpediente[posicionExpediente]).doubleValue()>=Double.valueOf(valor).doubleValue());
                        }
                        else if (comparador.equals("BETWEEN")){
                            String[] matriz = valor.split(",");
                            double valor1 = Double.valueOf(matriz[0]).doubleValue();
                            double valor2 = Double.valueOf(matriz[1]).doubleValue();
                            resultado = (Double.valueOf(datosExpediente[posicionExpediente]).doubleValue()<=valor2)
                                     && (Double.valueOf(datosExpediente[posicionExpediente]).doubleValue()>=valor1);
                        }
                        break;
                    case 6:
                    case 10:
                            //los tipos 6, 10 y 2 (desplegable, desplegable externo y texto, respectivamente) se comparan de la misma forma
                    case 2:
                        m_Log.debug("EN TIPO 2 DE EXPEDIENTE");
                        String aux="";
                        m_Log.debug("datosExpediente[posicionExpediente] " + datosExpediente[posicionExpediente]);

                        if (!valor.equals("") && valor.startsWith("'") && valor.endsWith("'")){
                            if(valor.length()<3) 
                                aux = "";
                            else
                            aux =valor.substring(1,valor.length()-1).trim(); // Le quito las comillas
                        }
                        else
                            aux = valor;

                        m_Log.debug("AUX: "+aux);
                        m_Log.debug("datosExpediente[posicionExpediente]: "+datosExpediente[posicionExpediente]);
                        m_Log.debug("COMPARADOR: "+comparador);
                        if (comparador.equals("IS NULL"))
                            resultado = (datosExpediente[posicionExpediente] == null);
                        else if (datosExpediente[posicionExpediente] == null)
                            resultado = false;
                        else if (comparador.equals("IS NOT NULL"))
                            resultado = true;
                        else if (comparador.equals("="))
                            resultado = (datosExpediente[posicionExpediente].equals(aux));
                        else if (comparador.equals("<>"))
                            resultado = (!datosExpediente[posicionExpediente].equals(aux));
                        else if (comparador.equals("LIKE")){
                            m_Log.debug("En comparador LIKE");
                            m_Log.debug("aux en tipo 2: _"+aux+"_");
                            m_Log.debug("datosExpediente[posicionExpediente]: "+datosExpediente[posicionExpediente]);
                            boolean ini=false,fin=false;
                            if (aux.startsWith("%")) {
                                ini=true;
                                aux = aux.substring(1,aux.length()).trim();
                            }
                            if (aux.endsWith("%")) {
                                fin=true;
                                aux = aux.substring(0,aux.length()-1).trim();
                            }
                            m_Log.debug("INI "+ini+"  ; FIN "+fin);
                            if (ini && fin) resultado = (datosExpediente[posicionExpediente].indexOf(aux) != -1);
                            else if (ini) resultado = (datosExpediente[posicionExpediente].endsWith(aux));
                            else if (fin) resultado = (datosExpediente[posicionExpediente].startsWith(aux));
                            else resultado = (datosExpediente[posicionExpediente].equals(aux));
                        }
                        break;
                    case 9:
                            //los tipos 9 y 3 (fecha calculada y fecha, respectivamente) se comparan de la misma forma
                    case 3:
                        m_Log.debug("EN TIPO 3 DE EXPEDIENTE");
                        aux="";
                        
                        if (!valor.equals("") && valor.startsWith("'") && valor.endsWith("'")){
                            if(valor.length()<3) 
                                aux = "";
                            else
                                aux =valor.substring(1,valor.length()-1).trim(); // Le quito las comillas
                        }
                        else
                            aux = valor;
                        /*
                        if (!valor.equals(""))
                            aux =valor.substring(1,valor.length()-1).replaceAll(" ",""); // Le quito las comillas */
                        SimpleDateFormat formateador = new SimpleDateFormat ( "dd/MM/yyyy" );
                        Date fechaExpediente=null;
                        if (comparador.equals("IS NULL"))
                            resultado = (datosExpediente[posicionExpediente] == null);
                        else if (datosExpediente[posicionExpediente] == null)
                            resultado = false;
                        else {
                        try {
                            fechaExpediente = formateador.parse(datosExpediente[posicionExpediente]);
                        } catch (ParseException e) {
                                if(m_Log.isDebugEnabled()) m_Log.debug("Invalid Date Parser Exception ");
                            e.printStackTrace();
                        }
                        Date fechaValor=null;
                        if (!aux.equals("")) {
                            try {
                                fechaValor = formateador.parse(aux);
                            } catch (ParseException e) {
                                    if(m_Log.isDebugEnabled()) m_Log.debug("Invalid Date Parser Exception ");
                                e.printStackTrace();
                            }
                        }
                            if(m_Log.isDebugEnabled()){
                              m_Log.debug("fechaExpediente: "+fechaExpediente);
                              m_Log.debug("fechaValor: "+fechaValor);
                              m_Log.debug("COMPARADOR: "+comparador);
                            }
                            if (comparador.equals("IS NOT NULL"))
                                resultado = (datosExpediente[posicionExpediente] != null);
                            else if (comparador.equals("="))
                            resultado = fechaExpediente.equals(fechaValor);
                        else if (comparador.equals("<>")){
                            resultado = !fechaExpediente.equals(fechaValor);
                        }
                        else if (comparador.equals("<")){
                            resultado = fechaExpediente.before(fechaValor);
                        }
                        else if (comparador.equals(">")){
                            resultado = fechaExpediente.after(fechaValor);
                        }
                        else if (comparador.equals("<=")){
                            resultado = fechaExpediente.before(fechaValor)||fechaExpediente.equals(fechaValor);
                        }
                        else if (comparador.equals(">=")){
                            resultado = fechaExpediente.after(fechaValor)||fechaExpediente.equals(fechaValor);
                        }
                        else if (comparador.equals("BETWEEN")){
                            aux=aux.replace("' , '","','");
                            String[] matriz = aux.split("','");
                            Date fecha1=null;
                            if (!matriz[0].equals("")) {
                                try {
                                    fecha1 = formateador.parse(matriz[0]);
                                } catch (ParseException e) {
                                    if(m_Log.isDebugEnabled()) m_Log.debug("Invalid Date Parser Exception ");
                                    e.printStackTrace();
                                }
                            }
                            Date fecha2=null;
                            if (!matriz[1].equals("")) {
                                try {
                                    fecha2 = formateador.parse(matriz[1]);
                                } catch (ParseException e) {
                                        if(m_Log.isDebugEnabled()) m_Log.debug("Invalid Date Parser Exception ");
                                    e.printStackTrace();
                                }
                            }
                                if(m_Log.isDebugEnabled()){
                                  m_Log.debug("fecha1: "+fecha1);
                                  m_Log.debug("fecha2: "+fecha2);
                                }
                            resultado = (fechaExpediente.before(fecha2)||fechaExpediente.equals(fecha2)) &&
                                        (fechaExpediente.after(fecha1)||fechaExpediente.equals(fecha1));
                        }
                        }
                        break;
                }
            }
        } else {
            m_Log.debug("LOG TRAMITES: "+camposTramite.length);
            int posicionTramites = buscarCampo(campo, camposTramite);
            m_Log.debug("POSICION TRAMITES: "+posicionTramites);
            if (posicionTramites!=-1) {
                if (camposTramite[posicionTramites].toUpperCase().equals(campo.toUpperCase())) {
                    int tipo=tiposTramite[posicionTramites];
                    switch (tipo) {
                        case 8:
                            //los tipos 8 y 1 (numerico calculado y numerico, respectivamente) se comparan de la misma forma
                        case 1:
                            m_Log.debug("EN TIPO 1 DE TRAMITES");
                            m_Log.debug("VALOR: "+valor);
                            m_Log.debug("DATOSTRAMITES[posicionTramites]: "+datosTramite[posicionTramites]);
                            m_Log.debug("COMPARADOR: "+comparador);
                            if (comparador.equals("IS NULL")){
                                resultado = (datosTramite[posicionTramites] == null);
                            }
                            else if (datosTramite[posicionTramites] == null) {
                                resultado = false;
                            }
                            else if (comparador.equals("IS NOT NULL")){
                                resultado = true;
                            }
                            else if (comparador.equals("="))
                                resultado = (Double.valueOf(datosTramite[posicionTramites]).doubleValue()==Double.valueOf(valor).doubleValue());
                            else if (comparador.equals("<>")){
                                resultado = (Double.valueOf(datosTramite[posicionTramites]).doubleValue()!=Double.valueOf(valor).doubleValue());
                            }
                            else if (comparador.equals("<")){
                                resultado = (Double.valueOf(datosTramite[posicionTramites]).doubleValue()<Double.valueOf(valor).doubleValue());
                            }
                            else if (comparador.equals(">")){
                                resultado = (Double.valueOf(datosTramite[posicionTramites]).doubleValue()>Double.valueOf(valor).doubleValue());
                            }
                            else if (comparador.equals("<=")){
                                resultado = (Double.valueOf(datosTramite[posicionTramites]).doubleValue()<=Double.valueOf(valor).doubleValue());
                            }
                            else if (comparador.equals(">=")){
                                resultado = (Double.valueOf(datosTramite[posicionTramites]).doubleValue()>=Double.valueOf(valor).doubleValue());
                            }
                            else if (comparador.equals("BETWEEN")){
                                String[] matriz = valor.split(",");
                                double valor1 = Double.valueOf(matriz[0]).doubleValue();
                                double valor2 = Double.valueOf(matriz[1]).doubleValue();
                                resultado = (Double.valueOf(datosTramite[posicionTramites]).doubleValue()<=valor2)
                                         && (Double.valueOf(datosTramite[posicionTramites]).doubleValue()>=valor1);
                            }
                            break;
                        case 6:
                        case 10:
                            //los tipos 6, 10 y 2 (desplegable, desplegable externo y texto, respectivamente) se comparan de la misma forma
                        case 2:
                            m_Log.debug("EN TIPO 2 DE TRAMITES");
                            String aux="";

                            if (!valor.equals("") && valor.startsWith("'") && valor.endsWith("'"))
                            {
                                if(valor.length()<3)
                                    aux = "";
                                else
                                    aux =valor.substring(1,valor.length()-1).trim(); // Le quito las comillas
                            }
                            else
                                aux = valor;

                            m_Log.debug("AUX: "+aux);
                            m_Log.debug("DATOSTRAMITES[posicionTramites]: "+datosTramite[posicionTramites]);
                            m_Log.debug("COMPARADOR: "+comparador);
                            if (comparador.equals("IS NULL")){
                                resultado = (datosTramite[posicionTramites] == null);
                            }
                            else if (datosTramite[posicionTramites] == null){
                                resultado = false;
                            }
                            else if (comparador.equals("IS NOT NULL")){
                                resultado = true;
                            }
                            else if (comparador.equals("="))
                                resultado = (datosTramite[posicionTramites].equals(aux));
                            else if (comparador.equals("<>")){
                                resultado = (!datosTramite[posicionTramites].equals(aux));
                            }
                            else if (comparador.equals("LIKE")){
                                boolean ini=false,fin=false;
                                if (aux.startsWith("%")) {
                                    ini=true;
                                    aux = aux.substring(1,aux.length());
                                }
                                if (aux.endsWith("%")) {
                                    fin=true;
                                    aux = aux.substring(0,aux.length()-1);
                                }
                                m_Log.debug("INI "+ini+"  ; FIN "+fin);
                                if (ini && fin) resultado = (datosTramite[posicionTramites].indexOf(aux) != -1);
                                else if (ini) resultado = (datosTramite[posicionTramites].endsWith(aux));
                                else if (fin) resultado = (datosTramite[posicionTramites].startsWith(aux));
                                else resultado = (datosTramite[posicionTramites].equals(aux));
                            }
                            break;
                        case 9:
                            //los tipos 9 y 3 (fecha calculada y fecha, respectivamente) se comparan de la misma forma
                        case 3:
                            m_Log.debug("EN TIPO 3 DE TRAMITES");
                            aux="";
                            if (!valor.equals("") && valor.startsWith("'") && valor.endsWith("'"))
                            {
                                if(valor.length()<3)
                                    aux = "";
                                else
                                    aux =valor.substring(1,valor.length()-1).trim(); // Le quito las comillas
                            }
                            else
                                aux = valor;

                            /*
                            if (!valor.equals(""))
                                aux =valor.substring(1,valor.length()-1).replaceAll(" ",""); // Le quito las comillas */
                            m_Log.debug("AUX: "+aux);
                            m_Log.debug("COMPARADOR: "+comparador);
                            SimpleDateFormat formateador = new SimpleDateFormat ( "dd/MM/yyyy" );
                            if (comparador.equals("IS NULL")){
                                resultado = (datosTramite[posicionTramites] == null);
                            }
                            else if (datosTramite[posicionTramites] == null){
                                resultado = false;
                            }
                            else {
                            Date fechaTramites=null;
                            try {
                                fechaTramites = formateador.parse(datosTramite[posicionTramites]);
                            } catch (ParseException e) {
                                    if(m_Log.isDebugEnabled()) m_Log.debug("Invalid Date Parser Exception ");
                                e.printStackTrace();
                            }
                            Date fechaValor=null;
                            if (!aux.equals("")) {
                                try {
                                    fechaValor = formateador.parse(aux);
                                } catch (ParseException e) {
                                        if(m_Log.isDebugEnabled()) m_Log.debug("Invalid Date Parser Exception ");
                                    e.printStackTrace();
                                }
                            }
                                if (comparador.equals("IS NOT NULL")){
                                    resultado = (datosTramite[posicionTramites] != null);
                                }
                                else if (comparador.equals("="))
                                resultado = fechaTramites.equals(fechaValor);
                            else if (comparador.equals("<>")){
                                resultado = !fechaTramites.equals(fechaValor);
                            }
                            else if (comparador.equals("<")){
                                resultado = fechaTramites.before(fechaValor);
                            }
                            else if (comparador.equals(">")){
                                resultado = fechaTramites.after(fechaValor);
                            }
                            else if (comparador.equals("<=")){
                                resultado = fechaTramites.before(fechaValor)||fechaTramites.equals(fechaValor);
                            }
                            else if (comparador.equals(">=")){
                                resultado = fechaTramites.after(fechaValor)||fechaTramites.equals(fechaValor);
                            }
                            else if (comparador.equals("BETWEEN")){
                                String[] matriz = aux.split("','");
                                Date fecha1=null;
                                if (!matriz[0].equals("")) {
                                    try {
                                        fecha1 = formateador.parse(matriz[0]);
                                    } catch (ParseException e) {
                                            if(m_Log.isDebugEnabled()) m_Log.debug("Invalid Date Parser Exception ");
                                        e.printStackTrace();
                                    }
                                }
                                Date fecha2=null;
                                if (!matriz[1].equals("")) {
                                    try {
                                        fecha2 = formateador.parse(matriz[1]);
                                    } catch (ParseException e) {
                                            if(m_Log.isDebugEnabled()) m_Log.debug("Invalid Date Parser Exception ");
                                        e.printStackTrace();
                                    }
                                }
                                    if(m_Log.isDebugEnabled()){
                                      m_Log.debug("FECHA1: "+fecha1);
                                      m_Log.debug("FECHA2: "+fecha2);
                                    }
                                resultado = (fechaTramites.before(fecha2)||fechaTramites.equals(fecha2)) &&
                                            (fechaTramites.after(fecha1)||fechaTramites.equals(fecha1));
                            }
                            }
                            break;
                    }
                }
            } else if (comparador.equals("IS NULL")){	
                resultado = true;
            }
        }
        } catch (Exception e) {
            throw e;
        }
        m_Log.debug("CompararCampo devuelve: "+resultado);
        if (resultado) return "1";
        else return "0";
    }

    private String analizar(String cad) {
        boolean resultado1 = false;
//        String aux = cad.replaceAll("AND","&&");
//        aux = aux.replaceAll("OR","||");
        String aux=cad;
        String[] matriz = aux.split("OPERADOR_OR");
        for (int i=0; i< matriz.length; i++) {
            boolean resultado2=true;
            String[] matriz2 = matriz[i].split("OPERADOR_AND");
            for (int j=0; j< matriz2.length; j++) {
                if (matriz2[j].trim().equals("0")) {
                    resultado2=false;
                    break;
                }
            }
            if (matriz2.length==0) resultado2 = false;
            if (matriz.length>1)
                resultado1 = resultado1 || resultado2 || Boolean.getBoolean(matriz[i].trim());
            else
                resultado1 = resultado2;
            if (resultado1) break;
        }
        if (resultado1) return "1";
        else return "0";
    }
    public boolean getResultado() {
        String temp = (String)pila.peek();
        if (temp.equals("1")) return true;
        else return false;
    }

    public String getCamposAnalizados() {
        return this.camposAnalizados;
    }

}
