package es.altia.agora.business.integracionsw.util;

import java.util.HashMap;
import java.util.Collection;

public class TraductorTipoBasico {

    private static HashMap<String, String> traductor = new HashMap<String, String>();

    static {

        traductor.put("int", "1");
        traductor.put("string", "2");
        traductor.put("dateTime", "3");
        traductor.put("byte", "4");
        traductor.put("boolean", "5");
        traductor.put("integer", "6");
        traductor.put("float", "7");
        traductor.put("double", "8");
        traductor.put("long", "9");
        traductor.put("decimal", "10");
        traductor.put("QName", "11");
        traductor.put("short", "12");
        traductor.put("base64Bynary", "13");
        traductor.put("hexBynary", "14");
        traductor.put("anyType", "15");      // Vamos a considerar el tipo anyType como un String por comodidad.

    }

    TraductorTipoBasico() {}

    public static int getCodigo(String claveTipo) {
        String value = traductor.get(claveTipo);
        if (value != null) return Integer.parseInt(value);
        else return -1;
    }

    public static String getClave(int codigo) {
        Collection<String> keys = traductor.keySet();
        for (String key : keys) {
            String strCodigo = traductor.get(key);
            if (Integer.parseInt(strCodigo) == codigo) return key;
        }
        return "";
    }
}
