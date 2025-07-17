package es.altia.arboles.impl;

import es.altia.arboles.Arbol;
import es.altia.arboles.Nodo;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;

import java.util.ArrayList;
import java.io.Serializable;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

/**
 * Representa árbol.
 * User: Esteban Sánchez Soler
 */
public class ArbolImpl implements Arbol, Serializable {
    private Nodo raiz;
    private static final String INDENTACION = "  "; // usada para indentar toString
    private Logger log = Logger.getLogger(ArbolImpl.class);
    
    public ArbolImpl() {

    }

    public ArbolImpl(Nodo raiz) {
        this.raiz = raiz;
    }

    /* (non-Javadoc)
      * @see es.altia.arboles.impl.Arbol#getRaiz()
      */
    public Nodo getRaiz() {
        return raiz;
    }

    /* (non-Javadoc)
      * @see es.altia.arboles.impl.Arbol#setRaiz(es.altia.arboles.Nodo)
      */
    public void setRaiz(Nodo raiz) {
        this.raiz = raiz;
    }


    /* (non-Javadoc)
      * @see es.altia.arboles.impl.Arbol#borrarNodo(java.lang.Object)
      */
    public Nodo borrarNodo(Object o) {
        // buscar nodo a borrar
        Nodo aBorrar = buscarEnProfNodo(raiz, o);
        // si no existe un nodo con ese campo "información", devolver null
        if(aBorrar == null) {
            return null;
        }
        else {
            // borrar referencia al nodo en el padre
            aBorrar.getPadre().borrarHijo(o);
        }

        return aBorrar;
    }

    private boolean visitar(Nodo nodo, Object o) {
        if((nodo.getInformacion() != null)&&(nodo.getInformacion().equals(o) == true)) {
            return true;
        }
        return false;
    }

    /*
      *  (non-Javadoc)
      * @see es.altia.arboles.Arbol#buscarEnProfNodo(es.altia.arboles.Nodo, java.lang.Object)
      */
    public Nodo buscarEnProfNodo(Nodo nodo, Object o) throws IllegalArgumentException {


        // si este nodo es el que busco
        if(visitar(nodo, o) == true) {
            return nodo;
        }
        // no es el nodo que busco
        else {
            // si tiene hijos, buscar en ellos
            if(nodo.getHijos().size() > 0) {
                ArrayList hijos = nodo.getHijos();
                for(int i=0; i<hijos.size(); i++) {
                    Nodo nodoHijo = buscarEnProfNodo((Nodo)hijos.get(i), o);
                    // si a he encontrado el nodo,
                    if(nodoHijo != null) {
                        return nodoHijo;
                    }
                }
            }
        }
        return null;
    }

    public int contarNodos() {
        int cuenta = 0;

        if(raiz != null) {
            cuenta = contar(raiz);
        }

        return cuenta;
    }

    private int contar(Nodo nodo) {
        int cuenta_parcial = 1;

        if(nodo.getHijos().size() > 0) {
            for(int i=0; i<nodo.getHijos().size(); i++) {
                cuenta_parcial += contar((Nodo)nodo.getHijos().get(i));
            }
            return cuenta_parcial;
        }
        else {
            return 1;
        }
    }

    /* (non-Javadoc)
      * @see java.lang.Object#toString()
      */
    public String toString() {
        String s = toStringNodo(raiz, "");

        return s;
    }

    private String toStringNodo(Nodo nodo, String indent) {
        // escribir el texto de este nodo
        StringBuffer s = new StringBuffer(indent);
        if(nodo.getInformacion() != null) {
            s.append(nodo.getInformacion().toString());
        }
        s.append("\n");

        // si tiene hijos, añadir su texto indentando
        if(nodo.getHijos().size() > 0) {
            ArrayList hijos = nodo.getHijos();
            for(int i=0; i<hijos.size(); i++) {
                s.append(toStringNodo((Nodo)hijos.get(i), indent + INDENTACION));
            }
        }
        return s.toString();
    }

    private boolean visitarClave(Nodo nodo, Object clave) {
        if((nodo.getClave() != null)&&(nodo.getClave().equals(clave) == true)) {
            return true;
        }
        return false;
    }

    /*
      *  (non-Javadoc)
      * @see es.altia.arboles.Arbol#buscarEnProfNodo(es.altia.arboles.Nodo, java.lang.Object)
      */
    public Nodo buscarEnProfNodoClave(Nodo nodo, Object clave) throws IllegalArgumentException {


        // si este nodo es el que busco
        if(visitarClave(nodo, clave) == true) {
            return nodo;
        }
        // no es el nodo que busco
        else {
            // si tiene hijos, buscar en ellos
            if(nodo.getHijos().size() > 0) {
                ArrayList hijos = nodo.getHijos();
                for(int i=0; i<hijos.size(); i++) {
                    Nodo nodoHijo = buscarEnProfNodoClave((Nodo)hijos.get(i), clave);
                    // si a he encontrado el nodo,
                    if(nodoHijo != null) {
                        return nodoHijo;
                    }
                }
            }
        }
        return null;
    }

    public String toJavascript(String treeVar, String iconoAlta, String iconoBaja, String icoNVAlta, String icoNVBaja,
                               String accion, String estilo) {
        StringBuffer s = new StringBuffer("");

        if(raiz.getHijos() != null) {
            for(int i=0; i<raiz.getHijos().size(); i++) {
                NodoImpl hijo = (NodoImpl)raiz.getHijos().get(i);
                UORDTO dto = (UORDTO)hijo.getInformacion();
                if(dto.getUor_estado().equals("A")) {
                    s.append(toJavascriptNodo(hijo, treeVar, iconoAlta, iconoBaja, icoNVAlta, icoNVBaja,
                            accion, estilo));
                }
                else {
                    s.append(toJavascriptNodo(hijo, treeVar, iconoAlta, iconoBaja, icoNVAlta, icoNVBaja,
                            accion, estilo));
                }
            }
        }
        return s.toString();
    }

   


    /**
     * Devuelve el texto con la instrucción Javascript para, usando xtree.js, crear el subárbol
     * a partir del nodo del arumento
     * @param nodo Nodo en cuestión
     * @param varPadre Nodo padre del nodo
     * @param icoAlta Nombre del archivo con el icono representando UORs de alta
     * @param icoBaja Nombre del archivo con el icono representando UORs de baja
     * @param icoNVAlta Nombre del archivo con el icono representando UORs no visibles desde el registro
     * @param accion Nombre del listener cuando se selecciona el nodo
     * @param estilo Estilo del nodo
     * @return Instrucción javascript para crear el subárbol
     */
    private String toJavascriptNodo(Nodo nodo, String varPadre, String icoAlta, String icoBaja,
                                    String icoNVAlta, String icoNVBaja, String accion, String estilo) {

        StringBuffer s = new StringBuffer("");
        UORDTO dto = (UORDTO)nodo.getInformacion();
        String icono;

        // elección del icono xa el nodo que vamos a escribir
        if(dto.getUor_no_registro() == '1') {
            if(dto.getUor_estado().equals("A")) {
                icono = icoNVAlta;
            }
            else {
                icono = icoNVBaja;
            }

        }
        else {
        if(dto.getUor_estado().equals("A")) {
            icono = icoAlta;
        }
        else {
            icono = icoBaja;
        }
        }

        // si tiene hijos, añadir al padre
        if(nodo.getHijos().size() > 0) {
        	s.append("var " + varPadre.concat("a") + " = new WebFXTreeItem('" + dto.getUor_cod_vis().trim() +
                    " - " + StringEscapeUtils.escapeJavaScript(dto.getUor_nom().trim()) + "','javascript:{" + accion + ";}'," +
                    varPadre + "," + icono + "," + icono + ",'" + estilo + "');\n");
            for(int i=0; i<nodo.getHijos().size(); i++) {
                Nodo n = (Nodo)nodo.getHijos().get(i);
                s.append(toJavascriptNodo(n, varPadre.concat("a"), icoAlta, icoBaja, icoNVAlta, icoNVBaja, accion, estilo));
            }
        }
        // no tiene hijos
        else {
        	s.append("new WebFXTreeItem('" + dto.getUor_cod_vis().trim() + " - " + StringEscapeUtils.escapeJavaScript(dto.getUor_nom().trim()) +
                    "','javascript:{" + accion + ";}'," +
                    varPadre + "," + icono + "," + icono + ",'" + estilo + "');\n");
        }
        /*
        var nodo = new WebFXTreeItem('850','javascript:{hacerAlgo();}',
        treeUsuarios,icoUsuario,icoUsuario,'webfx-tree-item-area');
        */
        return s.toString();
    }


    /** Retira las tildes de un String
     * @param cadena: Cadena de la que se retiran las tildes
     * @return String a devolver
     */
    private String retirarTildes(String cadena){
        return cadena.toUpperCase().replace("Á","A").replace("É","E").replace("Í","I").replace("Ó","O").replace("Ú","U").replace("'","");
    }
     

    /*
      *  (non-Javadoc)
      * @see es.altia.arboles.Arbol#buscarEnProfNodo(es.altia.arboles.Nodo, java.lang.Object)
      */
    public Nodo buscarHijoCumpleCriterioEnProfNodo(Nodo nodo, Object o) throws IllegalArgumentException {

        // si este nodo es el que busco
        if(visitar(nodo, o) == true) {
            return nodo;
        }
        // no es el nodo que busco
        else {
            // si tiene hijos, buscar en ellos
            if(nodo.getHijos().size() > 0) {
                ArrayList hijos = nodo.getHijos();
                for(int i=0; i<hijos.size(); i++) {
                    Nodo nodoHijo = buscarEnProfNodo((Nodo)hijos.get(i), o);
                    // si a he encontrado el nodo,
                    if(nodoHijo != null) {
                        return nodoHijo;
                    }
                }
            }
        }
        return null;
    }
}

