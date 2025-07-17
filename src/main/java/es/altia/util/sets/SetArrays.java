package es.altia.util.sets;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class SetArrays {

    private List elementos;

    /**
     * Constructor
     * @param l . Lista de elementos de tipo Object[]
     * @param esConjunto . Indica si la lista de elementos que se le pasa es un conjunto o no
     */
    public SetArrays(List l, boolean esConjunto) {
        elementos = new ArrayList();
        if (!esConjunto) {
            boolean meter = true;
            for (int i=0;i<l.size();i++) {
                Object[] valuesL = (Object[]) l.get(i);
                meter = true;
                for (int j=0;j<elementos.size();j++) {
                    Object[] valuesElementos = (Object[]) elementos.get(j);
                    if (Arrays.equals(valuesL, valuesElementos)) {
                        meter = false;
                        break;
                    }
                }
                if (meter) {
                    elementos.add(valuesL);
                }
            }
        } else {
            elementos = new ArrayList(l);
        }
    }

    /**
     *
     * @return la lista de elementos de este conjunto
     */
    public List getElementos() {
        return elementos;
    }

    /**
     *
     * @param s . Conjunto con el que se quiere hacer una union
     * @return la union de ambos conjuntos
     */
    public SetArrays union(SetArrays s) {
        List resultados = new ArrayList(elementos);

        boolean meter = true;
        for (int i=0;i<s.getElementos().size();i++) {
            Object[] valuesS = (Object[]) s.getElementos().get(i);
            meter = true;
            for (int j=0;j<elementos.size();j++) {
                Object[] valuesElementos = (Object[]) elementos.get(j);
                if (Arrays.equals(valuesElementos, valuesS)) {
                    meter = false;
                    break;
                }
            }
            if (meter) {
                resultados.add(valuesS);
            }
        }
        return new SetArrays(resultados, true);
    }

    /**
     *
     * @param s . Conjunto con el que se quiere hacer la interseccion
     * @return la interseccion de ambos conjuntos
     */
    public SetArrays intersection(SetArrays s) {
        List resultados = new ArrayList();

        boolean meter = false;
        for (int i=0;i<s.getElementos().size();i++) {
            Object[] valuesS = (Object[]) s.getElementos().get(i);
            meter = false;
            for (int j=0;j<elementos.size();j++) {
                Object[] valuesElementos = (Object[]) elementos.get(j);
                if (Arrays.equals(valuesElementos, valuesS)) {
                    meter = true;
                    break;
                }
            }
            if (meter) {
                resultados.add(valuesS);
            }
        }
        return new SetArrays(resultados, true);
    }

    /**
     *
     * @param s . Conjunto B en la operacion (A - B)
     * @return la diferencia entre ambos conjuntos
     */
    public SetArrays difference(SetArrays s) {
        List resultados = new ArrayList(elementos);

        for (int i=0;i<elementos.size();i++) {
            Object[] valuesElementos = (Object[]) elementos.get(i);
            for (int j=0;j<s.getElementos().size();j++) {
                Object[] valuesS = (Object[]) s.getElementos().get(j);
                if (Arrays.equals(valuesElementos, valuesS)) {
                    resultados.remove(i);
                    i--;
                    break;
                }
            }
        }
        return new SetArrays(resultados, true);
    }

    /**
     *
     * @param element
     * @return true si el elemento esta contenido en el conjunto
     */
    public boolean content(Object[] element) {
        if (elementos.size()==0) {
            return false;
        }
        Object[] elemento = (Object[]) elementos.get(0);
        if (elemento.length!=element.length) {
            return false;
        }
        boolean result = true;
        for (int j=0;j<elemento.length;j++) {
            result = result && (elemento[j].equals(element[j]));
        }
        if (result) {
            return true;
        }
        for (int i=1;i<elementos.size();i++) {
            elemento = (Object[]) elementos.get(i);
            if (elemento.length!=element.length) {
                return false;
            }
            result = true;
            for (int j=0;j<elemento.length;j++) {
                result = result && (elemento[j].equals(element[j]));
            }
            if (result) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        String s = "";
        for (int i=0;i<elementos.size();i++) {
            Object[] values = (Object[]) elementos.get(i);
            s = s + "\n elemento " + i + ": ";
            for (int j=0;j<values.length;j++) {
                s = s + values[j].toString();
            }
        }
        return s;
    }
}
