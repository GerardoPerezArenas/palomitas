package es.altia.agora.business.geninformes.utils;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.Cursor;
import es.altia.util.HashtableWithNull;
import es.altia.agora.business.geninformes.utils.XeracionInformes.EstructuraEntidades;
import es.altia.agora.business.geninformes.utils.XeracionInformes.NodoEntidad;
import es.altia.agora.business.geninformes.utils.bd.UtilesXerador;
import es.altia.agora.business.geninformes.utils.bd.EstructuraXerador;

import es.altia.agora.business.registro.persistence.AnotacionRegistroManager;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.util.commons.DateOperations;
import java.util.Vector;
import java.util.Iterator;
import java.util.Collection;
import java.util.ResourceBundle;

//import jet.api.CatalogAPI;
//import jet.api.Designer;

/**
 * @author jgd
 * Clase UtilidadesXerador en el fichero UtilidadesXerador.java,creado el 19-mar-2003.
 * 
 */

public final class UtilidadesXerador {

    protected static Log m_Log = LogFactory.getLog(UtilidadesXerador.class.getName()); // Para información de logs

    class GeneradorEntidadesVO {
        GeneradorEntidadesVO() {
        }

        public String texto = null;
        public java.util.Vector hijos = null;
        public java.util.Vector campos = null;

    }

    private UtilidadesXerador() {
    }


    /**
     * Method ObtenerArrayJSEntidadesCamposInforme.
     * @param entrada que contiene MNUCOD si se desea seleccionar solo las
     * entidades asociadas a una pantalla
     * @param hdlSesion
     * @return String Array de JS conteniendo [CODIGO,NOME,COD_CAMPOINFORME,
     * NOMECAMPO,TIPO,LONXITUDE]
     * @throws Exception
     */
    public static String ObtenerArrayJSEntidadesCamposInforme(
            es.altia.util.HashtableWithNull entrada)
            throws Exception {
        es.altia.util.conexion.Cursor cursor =
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .UtilidadesXerador
                        .ObtenerCursorEntidadesCamposInforme(entrada);
        return (
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .Utilidades
                        .ConvertirCursorToArrayJSEscaped(cursor));

    }

    /**
     * Method ObtenerCursorEntidadesCamposInforme.
     * @param entrada
     * @param hdlSesion
     * @return Cursor
     * @throws Exception
     */
    private static es
            .altia
            .util
            .conexion
            .Cursor ObtenerCursorEntidadesCamposInforme(
            es.altia.util.HashtableWithNull entrada)
            throws Exception {
        es.altia.util.conexion.Cursor cursorLocal = null;
        cursorLocal =
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .bd
                        .UtilesXerador
                        .ConsultaEntidadesCamposInforme(entrada);
        return cursorLocal;
    }

    /**
     * Method ObtenerArrayJSFontesInforme.
     * @param entrada
     * @param hdlSesion
     * @return String
     * @throws Exception
     */
    public static String ObtenerArrayJSFontesInforme(
            es.altia.util.HashtableWithNull entrada)
            throws Exception {
        es.altia.util.conexion.Cursor cursor =
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .UtilidadesXerador
                        .ObtenerCursorFontesInforme(entrada);
        return (
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .Utilidades
                        .ConvertirCursorToArrayJSEscaped(cursor));

    }

    /**
     * Method ObtenerCursorFontesInforme.
     * @param entrada
     * @param hdlSesion
     * @return Cursor
     * @throws Exception
     */
    private static es.altia.util.conexion.Cursor ObtenerCursorFontesInforme(
            es.altia.util.HashtableWithNull entrada)
            throws Exception {
        es.altia.util.conexion.Cursor cursorLocal = null;
        cursorLocal =
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .bd
                        .UtilesXerador
                        .ConsultaFontesInforme(entrada);
        return cursorLocal;
    }
    public static es.altia.util.conexion.Cursor consultaNomeFonte(
            es.altia.util.HashtableWithNull entrada)
            throws Exception {
        es.altia.util.conexion.Cursor cursorLocal =
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .bd
                        .UtilesXerador
                        .ConsultaNomeFonte(
                                entrada);
        return cursorLocal;

    }

    public static es.altia.util.conexion.Cursor consultaDatosEntidadeInforme(
            es.altia.util.HashtableWithNull entrada)
            throws Exception {
        es.altia.util.conexion.Cursor cursorLocal = null;
        String posicion = null;

        String codEntidade = (String) entrada.get("COD_ENTIDADEINFORME");

        m_Log.debug(
                "XINF: Consulto Entidade de informe:"
                        + entrada.get("COD_ENTIDADEINFORME")
                        + ".");

        cursorLocal =
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .bd
                        .UtilesXerador
                        .ConsultaDatosEntidadeInforme(entrada);

        return cursorLocal;
    }

    public static java.util.Vector consultaDatosCamposXerador(
            es.altia.util.HashtableWithNull entrada)
            throws Exception {
        java.util.Vector salida = null;
        es.altia.util.HashtableWithNull temp = null;
        es.altia.util.HashtableWithNull item = null;
        es.altia.util.conexion.Cursor cursorLocal = null;
        String posicion = null;


        java.util.Collection col = (java.util.Collection) entrada.get("CAMPOS");
        if (col != null) {
            java.util.Iterator iter = col.iterator();
            salida = new java.util.Vector();
            while (iter.hasNext()) {
                item = (es.altia.util.HashtableWithNull) iter.next();
                m_Log.debug(
                        "XINF: Consulto dato de posicion:"
                                + item.get("POSICION")
                                + ".");
                item.put("PARAMS", entrada.get("PARAMS"));
                cursorLocal =
                        es
                                .altia
                                .agora
                                .business
                                .geninformes
                                .utils
                                .bd
                                .UtilesXerador
                                .ConsultaDatosCampoXerador(item);

                if ((cursorLocal != null) && (cursorLocal.next())) {
                    temp = new es.altia.util.HashtableWithNull();
                    temp.put("CODIGO", cursorLocal.getString("CODIGO"));
                    temp.put("NOME", cursorLocal.getString("NOME"));
                    temp.put("CAMPO", cursorLocal.getString("CAMPO"));
                    temp.put("TIPO", cursorLocal.getString("TIPO"));
                    temp.put("LONXITUDE", cursorLocal.getString("LONXITUDE"));
                    temp.put("NOMEAS", cursorLocal.getString("NOMEAS"));
                    salida.add(temp);
                }
            }

        }
        return salida;
    }

    /**
     * Method ObtenerArrayJSTiposEtiquetaInforme.
     * @param entrada
     * @param hdlSesion
     * @return String
     * @throws Exception
     */
    public static String ObtenerArrayJSTiposEtiquetaInforme(
            es.altia.util.HashtableWithNull entrada)
            throws Exception {
        es.altia.util.conexion.Cursor cursor =
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .UtilidadesXerador
                        .ObtenerCursorTiposEtiquetaInforme(entrada);
        return (
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .Utilidades
                        .ConvertirCursorToArrayJSEscaped(cursor));

    }

    /**
     * Method ObtenerCursorTiposEtiquetaInforme.
     * @param entrada
     * @param hdlSesion
     * @return Cursor
     * @throws Exception
     */
    private static es
            .altia
            .util
            .conexion
            .Cursor ObtenerCursorTiposEtiquetaInforme(
            es.altia.util.HashtableWithNull entrada)
            throws Exception {
        es.altia.util.conexion.Cursor cursorLocal = null;
        cursorLocal =
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .bd
                        .UtilesXerador
                        .ConsultaTiposEtiquetaInforme(entrada);
        return cursorLocal;
    }

    /**
     * Method ObtenerArrayJSEjecutarConsultaEscaped esta funcion usa js_escape sobre los strings en el cursor/es
     *
     * @param    nomejb              a  String
     * @param    metodo              an int
     * @param    hash                a  HashtableWithNull
     * @param    hdlSesion           a  Handle
     *
     * @return   a String
     *
     * @throws   Exception
     *
     */
    public static String ObtenerArrayJSEjecutarConsultaEscaped(
            String nomejb,
            int metodo,
            es.altia.util.HashtableWithNull hash)
            throws Exception {
        m_Log.debug("Ini de ObtenerArrayJSEjecutarConsulta");
        es.altia.util.conexion.Cursor cursor =
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .UtilidadesXerador
                        .ejecutarConsulta(
                                nomejb,
                                metodo,
                                hash);
        return (
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .Utilidades
                        .ConvertirCursorToArrayJSEscaped(cursor));
    }

    /**
     * Method ejecutarConsulta.
     * @param nomejb
     * @param metodo
     * @param hash
     * @param hdlSesion
     * @return Cursor
     * @throws Exception
     */
    private static es.altia.util.conexion.Cursor ejecutarConsulta(
            String nomejb,
            int metodo,
            es.altia.util.HashtableWithNull hash)
            throws Exception {
        es.altia.util.conexion.Cursor Devolver = null;

        m_Log.debug(
                "En utilXera.ejecutarConsulta antes de CallEJB: nomeejeb="
                        + nomejb
                        + ".");

        m_Log.debug(
                "En utilXera.ejecutarConsulta despues de CallEJB: nomeejeb="
                        + nomejb
                        + ".");
       
        m_Log.debug("Ejecutado " + nomejb + "##" + Devolver);
        return (Devolver);
    }

    /**
     * Method creaQuerySQL.
     * @param entrada  an  es.altia.util.HashtableWithNull
     * @return String
     * @throws Exception
     */
    public static String creaQuerySQL(es.altia.util.HashtableWithNull entrada)
            throws Exception {
        java.util.Vector datosCampos = null;
        es.altia.util.HashtableWithNull temp =
                new es.altia.util.HashtableWithNull();

        es.altia.util.HashtableWithNull tempEntrada =
                new es.altia.util.HashtableWithNull();
        java.util.Vector camposSeleccion =
                (java.util.Vector) entrada.get("CAMPOSSELECCION");
        java.util.Vector camposOrde =
                (java.util.Vector) entrada.get("CAMPOSORDE");
        java.util.Vector camposCondicion =
                (java.util.Vector) entrada.get("CAMPOSCONDICION");
        boolean queryEstadistica =
                ((entrada.get("FORMATO") != null)
                        && (entrada.get("FORMATO").toString().equals("E")));

        String query = "SELECT ";

        temp.put("CAMPOS", camposSeleccion);

        datosCampos =
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .UtilidadesXerador
                        .consultaDatosCamposXerador(temp);
        String camposGroupBy = " ";

        if (datosCampos != null) {
            int cont = 0;
            java.util.Iterator iter = datosCampos.iterator();
            while (iter.hasNext()) {
                temp = (es.altia.util.HashtableWithNull) iter.next();
                if (cont > 0) {
                    query += ",";
                    camposGroupBy += (!temp.get("TIPO").toString().equals("F"))
                            ? ","
                            : " ";
                }
                cont++;
                query += formateaCampo(temp);
                camposGroupBy += (!temp.get("TIPO").toString().equals("F"))
                        ? formateaCampo(temp)
                        : " ";
            }
        }

        query += " FROM ";
        es.altia.util.conexion.Cursor salida =
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .UtilidadesXerador
                        .consultaDatosEntidadeInforme(entrada);

        salida.next();
        String tablasEntidade = (String) salida.getString("CLAUSULAFROM");
        query += tablasEntidade;

        //
        // Añado los joins necesarios
        //

        query += " WHERE ";
        String joinsEntidade = (String) salida.getString("CLAUSULAWHERE");
        //
        // Añado los WHERE del filtro (conector,campo,operador,valor)
        //
        temp.put("CAMPOS", camposCondicion);

        datosCampos =
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .UtilidadesXerador
                        .consultaDatosCamposXerador(temp);

        String wheresUsuario = null;

        if (datosCampos != null) {
            java.util.Iterator iter = datosCampos.iterator();
            java.util.Iterator iterEntrada = camposCondicion.iterator();
            String conector = null;
            String operador = null;
            String valor = null;
            wheresUsuario = "";
            while (iter.hasNext()) {
                temp = (es.altia.util.HashtableWithNull) iter.next();
                tempEntrada =
                        (es.altia.util.HashtableWithNull) iterEntrada.next();
                conector = (String) tempEntrada.get("CLAUSULA");
                operador = tempEntrada.get("OPERADOR").toString();
                valor =
                        formateaCampoCondicion(
                                (String) temp.get("TIPO"),
                                tempEntrada.get("VALOR").toString());
                m_Log.debug(
                        "EL conector es:"
                                + conector
                                + ".operador="
                                + operador
                                + ".valor="
                                + valor
                                + ".");
                wheresUsuario
                        += (((conector != null) && (!conector.trim().equals("")))
                        ? conector
                        : "")
                        +" "
                        + temp.get("CAMPO").toString()
                        + " "
                        + operador
                        + " "
                        + valor
                        + " ";
            }
        }

        if ((joinsEntidade != null) && !(joinsEntidade.trim().equals(""))) {
            String campoWhere =
                    sub(
                            joinsEntidade,
                            "<COD_CENTRO>",
                            " '" + entrada.get("CENCOD").toString() + "' ");
            campoWhere =
                    sub(campoWhere, "<ANO_TRABAJO>", " @\"ANO_TRABALLO\" ");

            query += campoWhere;
            if ((wheresUsuario != null) && !(wheresUsuario.trim().equals("")))
                query += " AND " + wheresUsuario;
        } else { //Non hai joins xerais
            if ((wheresUsuario != null) && !(wheresUsuario.trim().equals("")))
                query += wheresUsuario;
        }

        //
        // Agora campos GROUP BY
        //
        if (queryEstadistica)
            query += " GROUP BY " + camposGroupBy + " ";


        // Tengo que saber adicionalmente la posicion de cada uno de los campos que se
        // utilizan para ordenar en la seleccion para poder poner el numero en vez
        // del campo y que funcione en todos los casos
        //
        // Esto NO ES TRIVIAL.

        temp.put("CAMPOS", camposOrde);

        datosCampos =
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .UtilidadesXerador
                        .consultaDatosCamposXerador(temp);

        if (datosCampos != null) {
            java.util.Iterator iter = datosCampos.iterator();
            java.util.Iterator iterEntrada = camposOrde.iterator();
            int cont = 0;
            String queryOrder = "";
            String orden = null;

            while (iter.hasNext()) {
                temp = (es.altia.util.HashtableWithNull) iter.next();
                tempEntrada =
                        (es.altia.util.HashtableWithNull) iterEntrada.next();
                orden =
                        tempEntrada.get("TIPOORDE").toString().equals("A")
                                ? "ASC"
                                : "DESC";
                if (cont > 0)
                    queryOrder += ",";
                int posicion =
                        Integer.parseInt(tempEntrada.get("POSICION").toString())
                                + 1;
                //Por que el ORDER BY empieza en 1
                queryOrder += posicion + " " + orden + " ";
                cont++;
            }
            if (queryOrder.trim().length() > 0)
                query += " ORDER BY " + queryOrder;
        }

        m_Log.debug("XINF:En creaQuerySQL. Creei o string:" + query + ".");
        return query;

    }


    public static String creaString(int cuantos) {
        String salida = null;
        char[] caracteres = new char[cuantos];
        for (int i = 0; i < cuantos; i++)
            caracteres[i] =
                    es
                            .altia
                            .agora
                            .business
                            .geninformes
                            .utils
                            .ConstantesXerador
                            .CARACTER_MAXIMO_ANCHO;

        salida = new String(caracteres);
        return salida;
    }

    public static es
            .altia
            .util
            .conexion
            .Cursor ObtenerCursorValoresCampoInforme(
            es.altia.util.HashtableWithNull entrada)
            throws Exception {
        es.altia.util.conexion.Cursor cursorLocal = null;
        // Obtengo Clausulas from y where de ENTIDADEINFORME
        cursorLocal =
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .bd
                        .UtilesXerador
                        .ConsultaClausulasEntidadeInforme(entrada);
        cursorLocal.next();
        String campoFrom = cursorLocal.getString("CLAUSULAFROM");
        String campoWhere = cursorLocal.getString("CLAUSULAWHERE");

        // Obtengo cual es el select de CAMPOINFORME
        java.util.Collection col = new java.util.Vector();
        es.altia.util.HashtableWithNull temp1 =
                new es.altia.util.HashtableWithNull();
        temp1.put("COD_CAMPOINFORME", entrada.get("COD_CAMPOINFORME"));
        col.add(temp1);

        cursorLocal =
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .bd
                        .UtilesXerador
                        .ConsultaDatosCampoXerador(temp1);
        cursorLocal.next();
        String campoSelect = cursorLocal.getString("CAMPO");
        String tipo = cursorLocal.getString("TIPO");
        String[] params =
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .GeneradorInformesMgr
                        .get_params();
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        if (tipo.equals("D"))
            campoSelect = oad.convertir(campoSelect, oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY");

        if (cursorLocal.getStringNull("SELECTVALORES") != null) {
            campoSelect = cursorLocal.getString("SELECTVALORES");
            campoFrom = cursorLocal.getString("FROMVALORES");
            campoWhere = cursorLocal.getString("WHEREVALORES");
        }

        if (campoSelect.indexOf("CURNOM") >= 0) {
            // Cuando pide valor donde haya curso
            campoSelect += ","
                    + ConstantesXerador.CAMPO_ORDENACION_CURSOS
                    + " ";
            entrada.put("CLAUSULAORDER", "2");
        }
        entrada.put("CAMPOSELECT", campoSelect);
        entrada.put("CLAUSULAFROM", campoFrom);
        campoWhere =
                sub(
                        campoWhere,
                        "<COD_CENTRO>",
                        " '" + entrada.get("CENCOD").toString() + "' ");
        campoWhere =
                sub(
                        campoWhere,
                        "<ANO_TRABAJO>",
                        " " + entrada.get("MTRIDE").toString() + " ");
        entrada.put("CLAUSULAWHERE", campoWhere);

        cursorLocal =
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .bd
                        .UtilesXerador
                        .ConsultaValoresCampoInforme(entrada);
        return cursorLocal;
    }

    public static final String sub(String s, String s1, String s2) {
        int i = 0;
        if ((i = s.indexOf(s1, i)) >= 0) {
            char ac[] = s.toCharArray();
            char ac1[] = s2.toCharArray();
            int j = s1.length();
            StringBuffer stringbuffer = new StringBuffer(ac.length);
            stringbuffer.append(ac, 0, i).append(ac1);
            i += j;
            int k;
            for (k = i;(i = s.indexOf(s1, i)) > 0; k = i) {
                stringbuffer.append(ac, k, i - k).append(ac1);
                i += j;
            }

            stringbuffer.append(ac, k, ac.length - k);
            return stringbuffer.toString();
        } else {
            return s;
        }
    }

    private static String formateaCampo(es.altia.util.HashtableWithNull temp) {
        String salida = null;

        try {
            salida = temp.get("CAMPO").toString();
        } catch (Exception e) {
            salida = temp.get("CAMPO").toString();
        }
        return salida;
    }

    private static String formateaCampoCondicion(String tipo, String valor) {
        String salida = null;

        try {
            String[] params =
                    es
                            .altia
                            .agora
                            .business
                            .geninformes
                            .GeneradorInformesMgr
                            .get_params();
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            if (tipo.equals("D"))
                salida = " " + oad.convertir(valor, oad.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY")
                        +" ";
            else if (tipo.equals("A"))
                salida = " '" + valor + "' ";
            else
                salida = " " + valor + " ";
        } catch (Exception e) {
            salida = valor;
        }
        return salida;
    }

    public static java.util.Vector cargaVectorDatosCampos(
            es.altia.util.HashtableWithNull param)
            throws Exception {

        java.util.Vector vectorPosicions = null;
        //
        // Devolve un vector cos nomes e lonxitudes dos campos en orde.
        //
        es.altia.util.HashtableWithNull temp =
                new es.altia.util.HashtableWithNull();

        java.util.Collection colCampos =
                (java.util.Collection) param.get("CAMPOSSELECCION");

        // Para estatística
        //
        boolean atopadoCount = false;
        if ((colCampos != null) && (colCampos.size() > 0)) {
            if (param.get("FORMATO").toString().equals("E")) {
                java.util.Iterator iter = colCampos.iterator();
                while (iter.hasNext()) {
                    temp = (es.altia.util.HashtableWithNull) iter.next();
                    if (temp
                            .get("COD_CAMPOINFORME")
                            .toString()
                            .equals(ConstantesXerador.CODIGO_CAMPO_COUNT))
                        atopadoCount = true;
                }
               
                String cix =
                        (temp.get("COD_INFORMEXERADOR") != null)
                                ? temp.get("COD_INFORMEXERADOR").toString()
                                : null;
                int ultima = Integer.parseInt(temp.get("POSICION").toString());
                temp = new es.altia.util.HashtableWithNull();
                temp.put("COD_INFORMEXERADOR", cix);
                temp.put("POSICION", new Integer(ultima + 1));
                temp.put(
                        "COD_CAMPOINFORME",
                        es
                                .altia
                                .agora
                                .business
                                .geninformes
                                .utils
                                .ConstantesXerador
                                .CODIGO_CAMPO_COUNT);
                if (!atopadoCount)
                    colCampos.add(temp);
            }
            colCampos = new java.util.Vector(colCampos);
            temp = new es.altia.util.HashtableWithNull();
            temp.put("CAMPOS", colCampos);
            vectorPosicions =
                    es
                            .altia
                            .agora
                            .business
                            .geninformes
                            .utils
                            .UtilidadesXerador
                            .consultaDatosCamposXerador(temp);
        }
        return vectorPosicions;
    }

    public static float convierteTamanoFonte(String tamanoFonte) {
        Float temp =
                (Float) es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .ConstantesXerador
                        .EQUIVALENCIA_TAMANOS_FONTE
                        .get(tamanoFonte);
        return ((temp != null) ? temp.floatValue() : 0.34f);
    }

    public static String getNomeFonte(String codigo) {
        String salida = null;
        es.altia.util.conexion.Cursor cur = null;
        es.altia.util.HashtableWithNull temp =
                new es.altia.util.HashtableWithNull();
        temp.put("COD_FONTELETRA", codigo);
        try {
            cur =
                    es
                            .altia
                            .agora
                            .business
                            .geninformes
                            .utils
                            .bd
                            .UtilesXerador
                            .ConsultaNomeFonte(
                                    temp);
            if (cur.next())
                salida = cur.getString("NOME");
        } catch (Exception e) {
        }

        return salida;
    }

    public static es
            .altia
            .agora
            .business
            .geninformes
            .utils
            .XeracionInformes
            .EstructuraEntidades generaArbolCompleto(
            es
                    .altia
                    .agora
                    .business
                    .geninformes
                    .utils
                    .XeracionInformes
                    .EstructuraEntidades ee)
            throws Exception {
        es
                .altia
                .agora
                .business
                .geninformes
                .utils
                .XeracionInformes
                .CampoSeleccionInforme campoS =
                null;

        es
                .altia
                .agora
                .business
                .geninformes
                .utils
                .XeracionInformes
                .EstructuraEntidades salida =
                new es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .XeracionInformes
                        .EstructuraEntidades("0", ee.getCodEntidadeInforme());
        es
                .altia
                .agora
                .business
                .geninformes
                .utils
                .XeracionInformes
                .EstructuraEntidades tempEE =
                null;

        es
                .altia
                .agora
                .business
                .geninformes
                .utils
                .XeracionInformes
                .EstructuraEntidades tempEE2 =
                null;

        java.util.Vector nuevosHijosNivel = null;
        Long salidaEstructura = null;
        java.util.Iterator iterCond = null;
        java.util.Iterator iterHijosNivel = null;

        java.util.Vector hijosNivel = new java.util.Vector();
        hijosNivel.add(salida);

        // Primer bucle recorre cada nivel
        while (hijosNivel != null) {

            iterHijosNivel = hijosNivel.iterator();
            nuevosHijosNivel = new java.util.Vector();
            while (iterHijosNivel.hasNext()) {
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .XeracionInformes
                        .EstructuraEntidades hijo =
                        (es
                                .altia
                                .agora
                                .business
                                .geninformes
                                .utils
                                .XeracionInformes
                                .EstructuraEntidades) iterHijosNivel
                                .next();

                es.altia.util.HashtableWithNull tabla =
                        new es.altia.util.HashtableWithNull();
                tabla.put("COD_ENTIDADEINFORME", hijo.getCodEntidadeInforme());
                es.altia.util.conexion.Cursor cursorCampos =
                        es
                                .altia
                                .agora
                                .business
                                .geninformes
                                .utils
                                .bd
                                .UtilesXerador
                                .ConsultaCamposPorEntidadeInforme(tabla);

                while (cursorCampos.next()) {
                    campoS =
                            new es
                                    .altia
                                    .agora
                                    .business
                                    .geninformes
                                    .utils
                                    .XeracionInformes
                                    .CampoSeleccionInforme();
                    campoS.setCodCampoInforme(cursorCampos.getString("CODIGO"));
                    campoS.setNomeCampo(cursorCampos.getString("NOME"));

                    tempEE.addCampo(campoS);
                }

                tabla.put("COD_ENTIDADEINFORME", hijo.getCodEntidadeInforme());
                es.altia.util.conexion.Cursor cursorEnts =
                        es
                                .altia
                                .agora
                                .business
                                .geninformes
                                .utils
                                .bd
                                .UtilesXerador
                                .ConsultaSubEntidadesPorEntidadeInforme(tabla);

                while (cursorEnts.next()) {
                    tempEE2 =
                            new es
                                    .altia
                                    .agora
                                    .business
                                    .geninformes
                                    .utils
                                    .XeracionInformes
                                    .EstructuraEntidades(
                                    "0",
                                    cursorEnts.getString("CODIGO"));
                    tempEE2.setNomeEntidade(cursorEnts.getString("NOME"));
                    tempEE2.setCodPai(hijo.getCodEntidadeInforme());

                    tempEE.addHijo(tempEE2);
                    nuevosHijosNivel.add(tempEE2);
                }


            }
            hijosNivel = nuevosHijosNivel;

        }
        return salida;

    }

    public static String generaDTD(
            es
                    .altia
                    .agora
                    .business
                    .geninformes
                    .utils
                    .XeracionInformes
                    .EstructuraEntidades ee) {
        String salida = null;
        salida = "<!DOCTYPE DOCUMENT [\n";
        salida = "<!ELEMENT DOCUMENT (";
        salida += ee.getNomeEntidade() + ")*>\n";
        //salida = "<!ELEMENT " + ee.getNomeEntidade() + " (";

        java.util.Vector ents = new java.util.Vector();
        java.util.Vector campos = new java.util.Vector();
        java.util.Iterator iterCampos = null;
        java.util.Iterator iterEnts = null;
        es
                .altia
                .agora
                .business
                .geninformes
                .utils
                .XeracionInformes
                .EstructuraEntidades itemEnt =
                null;

        GeneradorEntidadesVO salidaGeneradorEntidades = null;
        es
                .altia
                .agora
                .business
                .geninformes
                .utils
                .XeracionInformes
                .CampoSeleccionInforme itemSeleccion =
                null;
        StringBuffer sbCampos = new StringBuffer();

        ents.add(ee);

        int posicion = 0;

        while (ee != null) {
            salidaGeneradorEntidades = generaDTDEntidad(ee);
            iterEnts = salidaGeneradorEntidades.hijos.iterator();
            salida += salidaGeneradorEntidades.texto;
            while (iterEnts.hasNext()) {
                itemEnt =
                        (es
                                .altia
                                .agora
                                .business
                                .geninformes
                                .utils
                                .XeracionInformes
                                .EstructuraEntidades) iterEnts
                                .next();
                if (!ents.contains(itemEnt))
                    ents.add(itemEnt);

            }
            iterCampos = salidaGeneradorEntidades.campos.iterator();
            while (iterCampos.hasNext()) {
                itemSeleccion =
                        (es
                                .altia
                                .agora
                                .business
                                .geninformes
                                .utils
                                .XeracionInformes
                                .CampoSeleccionInforme) iterCampos
                                .next();
                if (!campos.contains(itemSeleccion)) {
                    campos.add(itemSeleccion);
                    sbCampos.append(generaDTDCampo(itemSeleccion));
                }
            }

            posicion++;
            if (posicion < ents.size())
                ee =
                        (
                                es
                                        .altia
                                        .agora
                                        .business
                                        .geninformes
                                        .utils
                                        .XeracionInformes
                                        .EstructuraEntidades) ents
                                .elementAt(
                                        posicion);
            else
                ee = null;

        }


        salida += sbCampos.toString();
        salida += "]>";

        return salida;
    }

    private static String generaDTDCampo(
            es
                    .altia
                    .agora
                    .business
                    .geninformes
                    .utils
                    .XeracionInformes
                    .CampoSeleccionInforme cs) {
        String elementoCampo =
                "<!ELEMENT " + cs.getNomeCampo() + " (#PCDATA) >";
        elementoCampo += "\n";
        return elementoCampo;
    }

    public static GeneradorEntidadesVO generaDTDEntidad(
            es
                    .altia
                    .agora
                    .business
                    .geninformes
                    .utils
                    .XeracionInformes
                    .EstructuraEntidades ee) {

        UtilidadesXerador ux = new UtilidadesXerador();

        UtilidadesXerador.GeneradorEntidadesVO salida =
                (ux.new GeneradorEntidadesVO());

        salida.campos = new java.util.Vector();

        salida.texto = "<!ELEMENT " + ee.getNomeEntidade() + " (";

        String listaCampos = new String();

        java.util.Vector camposSeleccion = ee.getCamposSeleccion();
        java.util.Iterator iterCampos = null;
        es
                .altia
                .agora
                .business
                .geninformes
                .utils
                .XeracionInformes
                .CampoSeleccionInforme itemSeleccion =
                null;
        boolean primero = true;

        if (camposSeleccion != null) {
            iterCampos = camposSeleccion.iterator();

            while (iterCampos.hasNext()) {
                itemSeleccion =
                        (es
                                .altia
                                .agora
                                .business
                                .geninformes
                                .utils
                                .XeracionInformes
                                .CampoSeleccionInforme) iterCampos
                                .next();
                if (!primero)
                    listaCampos += ",";
                else
                    primero = false;
                listaCampos += itemSeleccion.getNomeCampo() + "?";
                salida.campos.add(itemSeleccion);
            }
        }

        String listaHijos = new String();

        java.util.Vector hijosNivel = ee.getHijos();
        salida.hijos = hijosNivel;

        java.util.Iterator iterHijosNivel = null;
        es
                .altia
                .agora
                .business
                .geninformes
                .utils
                .XeracionInformes
                .EstructuraEntidades hijo =
                null;

        java.util.Vector nuevosHijosNivel = null;
        Long salidaEstructura = null;
        java.util.Iterator iterCond = null;

        // Primer bucle recorre cada nivel
        while (hijosNivel != null) {

            iterHijosNivel = hijosNivel.iterator();
            nuevosHijosNivel = new java.util.Vector();
            // Bucle recorre hijos de un nivel
            primero = true;
            while (iterHijosNivel.hasNext()) {

                hijo =
                        (es
                                .altia
                                .agora
                                .business
                                .geninformes
                                .utils
                                .XeracionInformes
                                .EstructuraEntidades) iterHijosNivel
                                .next();

                if (!primero)
                    listaHijos += ",";
                else
                    primero = false;

                listaHijos += hijo.getNomeEntidade() + "*";

            }
        }

        salida.texto += listaCampos;
        if (listaHijos.length() > 0)
            salida.texto += "," + listaHijos;
        salida.texto += ") >";

        java.util.Iterator iterTemp = null;

        if (hijo.getHijos() != null) {
            iterTemp = hijo.getHijos().iterator();
            int posicionHijo = 0;
            es
                    .altia
                    .agora
                    .business
                    .geninformes
                    .utils
                    .XeracionInformes
                    .EstructuraEntidades eeTemp =
                    null;

            while (iterTemp.hasNext()) {
                eeTemp =
                        (es
                                .altia
                                .agora
                                .business
                                .geninformes
                                .utils
                                .XeracionInformes
                                .EstructuraEntidades) iterTemp
                                .next();
                eeTemp.setCodPai(salidaEstructura.toString());
                eeTemp.setPosicion(posicionHijo + "");
                posicionHijo++;

            }
            nuevosHijosNivel.addAll(hijo.getHijos());

        }

        return salida;

    }


    public static String creaQuerySQLEE(
            es
                    .altia
                    .agora
                    .business
                    .geninformes
                    .utils
                    .XeracionInformes
                    .EstructuraEntidades ee)
            throws Exception {
        String salida = null;

        String select = "";
        String from = "";
        String whereJoins = "";
        String whereUsuario = "";
        String orderBy = "";
        String groupBy = "";
        boolean primero = true;
        select = "";
        from = "";
        whereJoins = "";
        orderBy = "";
        groupBy = "";
        String orderByCreados = "";
        String whereJoinsCreados = "";
        String wheresUsuarioCreados = "";

        java.util.Vector hijos = new java.util.Vector();
        hijos.add(ee);
        int cont = 0;
        int numCamposSeleccion = 1;
        Integer numSel = new Integer(0);

        while (ee != null) {
            //
            // Consultar las vistas de las subentidades
            //

            if (!primero)
                select += ",";
            select += construyeListaCamposSeleccion(ee);

            //
            // From de entidades y subentidades
            //

            if (!primero)
                from += ",";
            from += construyeFromEntidades(ee);

            //
            // Condiciones de Join entre entidades y subentidades
            //

            whereJoinsCreados = construyeJoinsEntSubent(ee);
            if ((!primero)) {
                if ((whereJoinsCreados != null)
                        && (whereJoinsCreados.trim().length() > 0))
                    whereJoins += " AND " + whereJoinsCreados;
            } else {
                if ((whereJoinsCreados != null)
                        && (whereJoinsCreados.trim().length() > 0))
                    whereJoins += whereJoinsCreados;
            }

            //
            // Condiciones de usuario
            //
            wheresUsuarioCreados = construyeCondicionesUsuario(ee);
            if (!primero) {
                if ((wheresUsuarioCreados != null)
                        && (wheresUsuarioCreados.trim().length() > 0))
                    whereUsuario += " AND " + wheresUsuarioCreados;
            } else {
                if ((wheresUsuarioCreados != null)
                        && (wheresUsuarioCreados.trim().length() > 0))
                    whereUsuario += wheresUsuarioCreados;
            }


            orderByCreados = construyeListaOrderBy(ee, numCamposSeleccion);
            if ((!primero)) {
                if ((orderByCreados != null)
                        && (orderByCreados.trim().length() > 0))
                    orderBy += " , " + orderByCreados;
            } else {
                if ((orderByCreados != null)
                        && (orderByCreados.trim().length() > 0))
                    orderBy += orderByCreados;
            }

            numCamposSeleccion += (ee.getCamposSeleccion() != null)
                    ? (ee.getCamposSeleccion().size())
                    : 0;
            primero = false;
            salida =
                    "SELECT "
                            + select
                            + " FROM "
                            + from+" "
                            + ((whereJoins != null)
                            && (whereJoins.trim().length() > 0) ? " WHERE " : "")
                            + whereJoins
                            +((whereJoins != null)
                            && (whereJoins.trim().length() > 0) ? " AND " : ((whereUsuario != null)
                            && (whereUsuario.trim().length() > 0))?" WHERE ":" ")
                            + whereUsuario
                            + ((orderBy != null)
                            && (orderBy.trim().length() > 0) ? " ORDER BY " : "")
                            + orderBy;

            if (ee.getHijos() != null)
                hijos.addAll(ee.getHijos());

            cont++;

            ee =
                    (es
                            .altia
                            .agora
                            .business
                            .geninformes
                            .utils
                            .XeracionInformes
                            .EstructuraEntidades) ((hijos != null)
                            ? ((cont < hijos.size()) ? hijos.elementAt(cont) : null)
                            : null);
        }
        return salida;
    }


    /**
     * Method construyeJoinsEntSubent. COnstruye la lista de Joins entre un
     * padre ee y todos sus subentidades
     * @param ee
     * @return String
     * @throws Exception
     * */
    private static String construyeJoinsEntSubent(
            es
                    .altia
                    .agora
                    .business
                    .geninformes
                    .utils
                    .XeracionInformes
                    .EstructuraEntidades ee)
            throws Exception {
        es
                .altia
                .agora
                .business
                .geninformes
                .utils
                .XeracionInformes
                .EstructuraEntidades hijo =
                null;
        String[] params =
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .GeneradorInformesMgr
                        .get_params();

        es.altia.agora.business.geninformes.utils.bd.EntSubent ese =
                new es.altia.agora.business.geninformes.utils.bd.EntSubent(null);

        es.altia.util.conexion.Cursor cursorEse = null;

        String codEntidade = ee.getCodEntidadeInforme();
        java.util.Vector hijos = ee.getHijos();
        String salida = "";
        es.altia.util.HashtableWithNull tabla =
                new es.altia.util.HashtableWithNull();
        if (hijos != null) {
            java.util.Iterator iterHijos = hijos.iterator();

            while (iterHijos.hasNext()) {
                hijo =
                        (es
                                .altia
                                .agora
                                .business
                                .geninformes
                                .utils
                                .XeracionInformes
                                .EstructuraEntidades) iterHijos
                                .next();
                tabla.clear();
                tabla.put("PARAMS", params);
                tabla.put("ENT", codEntidade);
                tabla.put("SUBENT", hijo.getCodEntidadeInforme());

                cursorEse = ese.ConsultaCamposJoinEntSubent(tabla);

                String codEnt = null;
                String codSubEnt = null;
                String campoEnt = null;
                String campoSubEnt = null;
                String outer = null;
                boolean primero = true;
                while (cursorEse.next()) {
                    codEnt = cursorEse.getString("ENT");
                    codSubEnt = cursorEse.getString("SUBENT");
                    campoEnt = cursorEse.getString("CAMPO_ENT");
                    campoSubEnt = cursorEse.getString("CAMPO_SUBENT");
                    outer = cursorEse.getString("OUTER_JOIN");
                    if (!primero)
                        salida += " AND ";
                    else
                        primero = false;
                    salida += " "
                            + campoEnt
                            + (((outer != null) && outer.equals("L")) ? "(+)" : "")
                            + "="
                            + campoSubEnt
                            + (((outer != null) && outer.equals("R")) ? "(+)" : "")
                            + " ";
                }

            }
        }
        return salida;
    }

    private static String construyeJoinsEntSubent(
            es
                    .altia
                    .agora
                    .business
                    .geninformes
                    .utils
                    .XeracionInformes
                    .EstructuraEntidades ee,
            es
                    .altia
                    .agora
                    .business
                    .geninformes
                    .utils
                    .XeracionInformes
                    .EstructuraEntidades hijo)
            throws Exception {

        String codEntidade = ee.getCodEntidadeInforme();
        String salida = "";
        es.altia.util.HashtableWithNull tabla =
                new es.altia.util.HashtableWithNull();

        tabla.clear();
        tabla.put("ENT", codEntidade);
        tabla.put("SUBENT", hijo.getCodEntidadeInforme());
        es.altia.util.conexion.Cursor cursorEse = null;

        es.altia.agora.business.geninformes.utils.bd.EntSubent ese =
                new es.altia.agora.business.geninformes.utils.bd.EntSubent(null);
        cursorEse = ese.ConsultaCamposJoinEntSubent(tabla);

        String codEnt = null;
        String codSubEnt = null;
        String campoEnt = null;
        String campoSubEnt = null;
        String outer = null;
        boolean primero = true;
        while (cursorEse.next()) {
            codEnt = cursorEse.getString("ENT");
            codSubEnt = cursorEse.getString("SUBENT");
            campoEnt = cursorEse.getString("CAMPO_ENT");
            campoSubEnt = cursorEse.getString("CAMPO_SUBENT");
            outer = cursorEse.getString("OUTER_JOIN");
            if (!primero)
                salida += " AND ";
            else
                primero = false;
            salida += " "
                    + campoEnt
                    + (((outer != null) && outer.equals("L")) ? "(+)" : "")
                    + "="
                    + campoSubEnt
                    + (((outer != null) && outer.equals("R")) ? "(+)" : "")
                    + " ";
        }

        return salida;
    }

    private static String construyeListaCamposSeleccion(
            es
                    .altia
                    .agora
                    .business
                    .geninformes
                    .utils
                    .XeracionInformes
                    .EstructuraEntidades ee)
            throws Exception {
        String salida = "";
        es.altia.util.conexion.Cursor cursorLocal = null;
        es.altia.util.HashtableWithNull tempTabla =
                new es.altia.util.HashtableWithNull();

        es
                .altia
                .agora
                .business
                .geninformes
                .utils
                .XeracionInformes
                .CampoSeleccionInforme itemSeleccion =
                null;
        java.util.Vector camposSel = ee.getCamposSeleccion();
        boolean primero = true;
        if (camposSel != null) {
            int cont = 0;
            java.util.Iterator iterSel = camposSel.iterator();
            while (iterSel.hasNext()) {
                if (!primero)
                    salida += ",";
                else
                    primero = false;
                itemSeleccion =
                        (es
                                .altia
                                .agora
                                .business
                                .geninformes
                                .utils
                                .XeracionInformes
                                .CampoSeleccionInforme) iterSel
                                .next();
                tempTabla.clear();
                tempTabla.put("PARAMS",es.altia.agora.business.geninformes.GeneradorInformesMgr.get_params());
                tempTabla.put("COD_CAMPOINFORME",itemSeleccion.getCodCampoInforme());

                cursorLocal =	es.altia.agora.business.geninformes.utils.bd.UtilesXerador.ConsultaDatosCampoXerador(tempTabla);

                if ((cursorLocal.next())) {
                    itemSeleccion.setNomeAs(cursorLocal.getString("NOMEAS"));
                }

                salida += itemSeleccion.getCampo()
                        + " AS "
                        + itemSeleccion.getNomeAs();
                cont++;
            }

        }

        return salida;

    }

    private static String construyeFromEntidades(
            es
                    .altia
                    .agora
                    .business
                    .geninformes
                    .utils
                    .XeracionInformes
                    .EstructuraEntidades ee)
            throws Exception {

        return ee.getNomeVista();

    }

    private static String construyeCondicionesUsuario(
            es
                    .altia
                    .agora
                    .business
                    .geninformes
                    .utils
                    .XeracionInformes
                    .EstructuraEntidades ee)
            throws Exception {
        String salida = "";
        es
                .altia
                .agora
                .business
                .geninformes
                .utils
                .XeracionInformes
                .CampoCondicionInforme itemSeleccion =
                null;
        java.util.Vector camposSel = ee.getCamposCondicion();
        boolean primero = true;
        if (camposSel != null) {
            java.util.Iterator iterSel = camposSel.iterator();
            while (iterSel.hasNext()) {
                itemSeleccion =
                        (es
                                .altia
                                .agora
                                .business
                                .geninformes
                                .utils
                                .XeracionInformes
                                .CampoCondicionInforme) iterSel
                                .next();

                if (!primero)
                    salida += " "
                            + (((itemSeleccion.getValor() != null)
                            && (itemSeleccion.getClausula() != null))
                            ? itemSeleccion.getClausula()
                            : "")
                            + " ";
                else
                    primero = false;

                if (itemSeleccion.getValor() != null) {
                    salida += itemSeleccion.getCampo()
                            + " "
                            + itemSeleccion.getOperador()
                            + " "
                            + formateaCampoCondicion(
                            itemSeleccion.getTipoCampo(),
                            itemSeleccion.getValor());
                } else
                    salida += "";
            }

        }

        return salida;

    }

    private static String construyeListaOrderBy(
            es
                    .altia
                    .agora
                    .business
                    .geninformes
                    .utils
                    .XeracionInformes
                    .EstructuraEntidades ee,
            int inicial)
            throws Exception {
        String salida = "";

        es
                .altia
                .agora
                .business
                .geninformes
                .utils
                .XeracionInformes
                .CampoOrdeInforme itemSeleccion =
                null;
        java.util.Vector camposSel = ee.getCamposOrde();
        boolean primero = true;
        if (camposSel != null) {
            java.util.Iterator iterSel = camposSel.iterator();
            int cont = 0;
            while (iterSel.hasNext()) {
                itemSeleccion =(es.altia.agora.business.geninformes.utils.XeracionInformes
                        .CampoOrdeInforme) iterSel.next();
                String posicion = itemSeleccion.getPosicion();
                int pos = Integer.parseInt(posicion);
                pos = pos +1;

                if (!primero)
                    salida += " , ";
                else
                    primero = false;

                if (itemSeleccion.getTipoOrde() != null) {
                    salida += (pos)+ " "+ ((itemSeleccion.getTipoOrde().equals("A"))
                            ? " ASC": " DESC")+ " ";
                } else
                    salida += "";
                cont++;
            }

        }

        return salida;
    }

    public static String creaQuerySQLEE2(
            es
                    .altia
                    .agora
                    .business
                    .geninformes
                    .utils
                    .XeracionInformes
                    .EstructuraEntidades ee)
            throws Exception {
        String salida = null;

        String select = "";
        String from = "";
        String whereJoins = "";
        String whereUsuario = "";
        String orderBy = "";
        String groupBy = "";
        boolean primero = true;
        select = " SELECT ";
        from = " FROM ";
        whereJoins = " WHERE ";
        orderBy = " ORDER BY ";
        groupBy = " GROUP BY ";

        java.util.Vector hijos = new java.util.Vector();
        hijos.add(ee);
        int cont = 0;
        int numCamposSeleccion = 1;
        Integer numSel = new Integer(0);

        String camposJoinConPadre = camposJoinConPadre(ee);
        if (camposJoinConPadre.trim().length() > 0)
            select += camposJoinConPadre + ",";

        select += construyeListaCamposSeleccion(ee);
        String unionCamposJoinConHijos = unionCamposJoinConHijos(ee);
        if (unionCamposJoinConHijos.trim().length() > 0)
            select += "," + unionCamposJoinConHijos + " ";

        from += construyeFromEntidadesArriba(ee);


        if (!primero)
            whereJoins += " WHERE ";
        whereJoins += construyeJoinsEntSubentArriba(ee);
        if (whereJoins.trim().length() > 10)
            whereUsuario += " AND ";

        whereUsuario += construyeCondicionesUsuarioArriba(ee);

        es
                .altia
                .agora
                .business
                .geninformes
                .utils
                .XeracionInformes
                .EstructuraEntidades tempEE =
                null;
        whereUsuario += construyeCondicionesUsuarioArriba(ee);


        if (!primero)
            orderBy += " , ";
        orderBy += construyeListaOrderBy(ee, numCamposSeleccion);

        numCamposSeleccion += (ee.getCamposSeleccion() != null)
                ? (ee.getCamposSeleccion().size())
                : 0;
        primero = false;
        salida = select + from + whereJoins + whereUsuario + groupBy + orderBy;

        if (ee.getHijos() != null)
            hijos.addAll(ee.getHijos());

        cont++;

        ee =
                (es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .XeracionInformes
                        .EstructuraEntidades) ((hijos != null)
                        ? ((cont < hijos.size()) ? hijos.elementAt(cont) : null)
                        : null);
        return salida;
    }


    /**
     * Method camposJoinConPadre. 			Devuelve la lista de campos de
     * enlace (los del padre) entre esta subentidad y su entidad padre, si esta
     * entidad tiene padre
     * @param ee La subentidad
     * @return String La lista de campos para poner en el select
     */
    private static String camposJoinConPadre(
            es
                    .altia
                    .agora
                    .business
                    .geninformes
                    .utils
                    .XeracionInformes
                    .EstructuraEntidades ee)
            throws Exception {
        String salida = "";
        java.util.Vector contenedor = new java.util.Vector();
        if (ee.getPai() != null) {
            es.altia.util.HashtableWithNull tabla =
                    new es.altia.util.HashtableWithNull();

            tabla.clear();
            tabla.put("ENT", ee.getPai().getCodEntidadeInforme());
            tabla.put("SUBENT", ee.getCodEntidadeInforme());
            es.altia.util.conexion.Cursor cursorEse = null;

            es.altia.agora.business.geninformes.utils.bd.EntSubent ese =
                    new es.altia.agora.business.geninformes.utils.bd.EntSubent(
                            null);
            cursorEse = ese.ConsultaCamposJoinEntSubent(tabla);

            String codEnt = null;
            String codSubEnt = null;
            String campoEnt = null;
            String campoSubEnt = null;
            String outer = null;
            boolean primero = true;

            while (cursorEse.next()) {
                codEnt = cursorEse.getString("ENT");
                codSubEnt = cursorEse.getString("SUBENT");
                campoEnt = cursorEse.getString("CAMPO_ENT");
                campoSubEnt = cursorEse.getString("CAMPO_SUBENT");
                outer = cursorEse.getString("OUTER_JOIN");
                if (!primero)
                    salida += " , ";
                else
                    primero = false;
                salida += campoSubEnt;
                contenedor.add(campoSubEnt);
            }

            ee.setColeccionJoinsConPadre(contenedor);
        }

        return salida;
    }

    private static String unionCamposJoinConHijos(
            es
                    .altia
                    .agora
                    .business
                    .geninformes
                    .utils
                    .XeracionInformes
                    .EstructuraEntidades ee)
            throws Exception {
        String salida = "";
        if (ee != null) {
            es.altia.util.HashtableWithNull tabla =
                    new es.altia.util.HashtableWithNull();

            tabla.clear();
            tabla.put("ENT", ee.getCodEntidadeInforme());

            java.util.Vector hijos = ee.getHijos();
            if ((hijos != null) && (hijos.size() > 0)) {
                java.util.Iterator iterHijos = hijos.iterator();
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .XeracionInformes
                        .EstructuraEntidades hijo =
                        null;
                java.util.Vector contenedor = new java.util.Vector();

                while (iterHijos.hasNext()) {
                    hijo =
                            (es
                                    .altia
                                    .agora
                                    .business
                                    .geninformes
                                    .utils
                                    .XeracionInformes
                                    .EstructuraEntidades) iterHijos
                                    .next();
                    tabla.put("SUBENT", hijo.getCodEntidadeInforme());

                    es.altia.util.conexion.Cursor cursorEse = null;

                    es.altia.agora.business.geninformes.utils.bd.EntSubent ese =
                            new es
                                    .altia
                                    .agora
                                    .business
                                    .geninformes
                                    .utils
                                    .bd
                                    .EntSubent(
                                    null);
                    cursorEse = ese.ConsultaCamposJoinEntSubent(tabla);

                    String codEnt = null;
                    String codSubEnt = null;
                    String campoEnt = null;
                    String campoSubEnt = null;
                    String outer = null;
                    boolean primero = true;
                    while (cursorEse.next()) {
                        codEnt = cursorEse.getString("ENT");
                        codSubEnt = cursorEse.getString("SUBENT");
                        campoEnt = cursorEse.getString("CAMPO_ENT");
                        campoSubEnt = cursorEse.getString("CAMPO_SUBENT");
                        outer = cursorEse.getString("OUTER_JOIN");
                        if ((!primero) && (!contenedor.contains(campoEnt)))
                            salida += " , ";
                        else
                            primero = false;
                        if (!contenedor.contains(campoEnt)) {
                            salida += campoEnt;
                            contenedor.add(campoEnt);
                        }

                    }

                }
                ee.setColeccionUnionJoinsConHijos(contenedor);

            }
        }
        return salida;
    }

    private static String construyeJoinsEntSubentArriba(
            es
                    .altia
                    .agora
                    .business
                    .geninformes
                    .utils
                    .XeracionInformes
                    .EstructuraEntidades ee)
            throws Exception {
        String salida = "";
        if (ee.getPai() != null) {
            String codEntidade = ee.getPai().getCodEntidadeInforme();

            es.altia.util.HashtableWithNull tabla =
                    new es.altia.util.HashtableWithNull();
            es
                    .altia
                    .agora
                    .business
                    .geninformes
                    .utils
                    .XeracionInformes
                    .EstructuraEntidades pai =
                    ee;
            boolean primeroPai = true;
            while (pai != null) {
                if (!primeroPai)
                    salida += " AND ";
                else
                    primeroPai = false;

                codEntidade = pai.getPai().getCodEntidadeInforme();

                tabla.clear();
                tabla.put("ENT", codEntidade);
                tabla.put("SUBENT", pai.getCodEntidadeInforme());
                es.altia.util.conexion.Cursor cursorEse = null;

                es.altia.agora.business.geninformes.utils.bd.EntSubent ese =
                        new es.altia.agora.business.geninformes.utils.bd.EntSubent(
                                null);
                cursorEse = ese.ConsultaCamposJoinEntSubent(tabla);

                String codEnt = null;
                String codSubEnt = null;
                String campoEnt = null;
                String campoSubEnt = null;
                String outer = null;
                boolean primero = true;
                while (cursorEse.next()) {
                    codEnt = cursorEse.getString("ENT");
                    codSubEnt = cursorEse.getString("SUBENT");
                    campoEnt = cursorEse.getString("CAMPO_ENT");
                    campoSubEnt = cursorEse.getString("CAMPO_SUBENT");
                    outer = cursorEse.getString("OUTER_JOIN");
                    if (!primero)
                        salida += " AND ";
                    else
                        primero = false;
                    salida += " "
                            + campoEnt
                            + (((outer != null) && outer.equals("L")) ? "(+)" : "")
                            + "="
                            + campoSubEnt
                            + (((outer != null) && outer.equals("R")) ? "(+)" : "")
                            + " ";
                }

                pai = pai.getPai();
            }
        }
        return salida;
    }

    private static String construyeCondicionesUsuarioArriba(
            es
                    .altia
                    .agora
                    .business
                    .geninformes
                    .utils
                    .XeracionInformes
                    .EstructuraEntidades ee)
            throws Exception {
        String salida = "";
        es
                .altia
                .agora
                .business
                .geninformes
                .utils
                .XeracionInformes
                .CampoCondicionInforme itemSeleccion =
                null;
        es
                .altia
                .agora
                .business
                .geninformes
                .utils
                .XeracionInformes
                .EstructuraEntidades pai =
                ee;
        boolean primeroPai = true;

        while (pai != null) {
            if (!primeroPai)
                salida += " AND ";
            else
                primeroPai = false;

            java.util.Vector camposSel = pai.getCamposCondicion();
            boolean primero = true;
            if (camposSel != null) {
                java.util.Iterator iterSel = camposSel.iterator();
                while (iterSel.hasNext()) {
                    itemSeleccion =
                            (es
                                    .altia
                                    .agora
                                    .business
                                    .geninformes
                                    .utils
                                    .XeracionInformes
                                    .CampoCondicionInforme) iterSel
                                    .next();

                    if (!primero)
                        salida += " "
                                + (((itemSeleccion.getValor() != null)
                                && (itemSeleccion.getClausula() != null))
                                ? itemSeleccion.getClausula()
                                : "")
                                + " ";
                    else
                        primero = false;

                    if (itemSeleccion.getValor() != null) {
                        salida += itemSeleccion.getCampo()
                                + " "
                                + itemSeleccion.getOperador()
                                + " "
                                + formateaCampoCondicion(
                                itemSeleccion.getTipoCampo(),
                                itemSeleccion.getValor());
                    } else
                        salida += "";
                }

            }
            pai = pai.getPai();
        }
        return salida;

    }

    private static String construyeFromEntidadesArriba(
            es
                    .altia
                    .agora
                    .business
                    .geninformes
                    .utils
                    .XeracionInformes
                    .EstructuraEntidades ee)
            throws Exception {
        String salida = "";
        String codEntidade = ee.getPai().getCodEntidadeInforme();

        es.altia.util.HashtableWithNull tabla =
                new es.altia.util.HashtableWithNull();
        es
                .altia
                .agora
                .business
                .geninformes
                .utils
                .XeracionInformes
                .EstructuraEntidades pai =
                ee;
        boolean primeroPai = true;
        String nomeVista = null;

        while (pai != null) {
            if (!primeroPai)
                salida += " , ";
            else
                primeroPai = false;

            nomeVista = pai.getNomeVista();

            salida += nomeVista;

            pai = pai.getPai();
        }
        return salida;
    }

    public static Cursor ejecutarConsulta(String[] params, EstructuraEntidades ee, Vector parametros)
    throws Exception {

        HashtableWithNull tabla = new HashtableWithNull(); 
        tabla.put("CONSULTA", ee.getConsultaSQL());

        if ((parametros != null) && (parametros.size() > 0)) {
            Iterator iterParam = parametros.iterator();
            int cont = 1;
            while (iterParam.hasNext()) {
                m_Log.debug("PARAMETRO " + cont + " DE LA CONSULTA:" + iterParam.next().toString());
                cont++;
            }
        } // Ejecutar la consulta.
        tabla.put("PARAMETROS", parametros);
        tabla.put("PARAMS", params);

        Cursor salida = UtilesXerador.ejecutaConsulta(tabla);
        m_Log.debug("SE HA EJECUTADO LA CONSULTA : " + ee.getConsultaSQL());
        ee.setConsultaEjecutada(true);
        ee.setCursorConsulta(salida);

        return salida;
    }


   

    /**
     * Comprueba si una etiqueta es de tipo fecha
     * @param nombreEtiqueta: Nombre de la etiqueta
     * @param params: Parámetros de conexión a la BBDD
     * @return Un boolean
     */
    private static boolean estaEtiquetaFechaSeleccionada(String nombreEtiqueta,Vector etiquetas){
        boolean exito = false;

        try{
            if(etiquetas.contains(nombreEtiqueta)){
                exito = true;
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return exito;
    }

   

    public static void construyeEstructuraDatos(String[] params, EstructuraEntidades ee2,Vector etiquetasFecha)
    throws Exception {

        //
        // coleccionNodosPrimerNivel contiene un Map de los nodos de Primer nivel de
        //						tipo es.altia.agora.business.geninformes.utils.XeracionInformes.NodoEntidad
        //						La clave del Map es el tipo de Entidad (nomeEntidade)
        //
        java.util.Vector hijos = new java.util.Vector();

        java.util.Map coleccionNodosPrimerNivel = new java.util.HashMap();

        es.altia.util.HashtableWithNull tabla =	new es.altia.util.HashtableWithNull();
        Cursor cursorSalida = null;
        es.altia.agora.business.geninformes.utils.XeracionInformes.NodoEntidad nodo =null;
        int contador = 0;
        hijos.add(ee2);
        es.altia.agora.business.geninformes.utils.XeracionInformes.EstructuraEntidades ee =null;
        java.util.Vector parametros = null;


        while (contador < hijos.size()) {
            m_Log.debug("CONTADOR: " + contador + " | HIJOS: " + hijos.size());
            ee =(EstructuraEntidades) hijos.elementAt(contador);

            parametros = ee.getValoresParametrosConsulta();
            // Ejecutar la consulta de la estructuraEntidades si no ha sido ejecutada ya

            if (ee.isConsultaEjecutada()) cursorSalida = ee.getCursorConsulta();
            else cursorSalida = ejecutarConsulta(params, ee, parametros);            

            java.util.HashMap tempCampos = null;
            String nomeColumna = null;
            java.util.Iterator iterColumnas = null;

            boolean etiquetasRegistro = false;
            while (cursorSalida.next()) {
                tempCampos = new java.util.HashMap();
                iterColumnas = cursorSalida.hash_Nombres.keySet().iterator();

                while (iterColumnas.hasNext()) {
                    /** original
                    nomeColumna = iterColumnas.next().toString();
                    tempCampos.put(nomeColumna, cursorSalida.getString(nomeColumna));
                     **/
                    nomeColumna = iterColumnas.next().toString();
                    String valor = cursorSalida.getString(nomeColumna);
                    tempCampos.put(nomeColumna, valor);
                    
                     String formatoAlternativo = "yyyy/MM/dd";
                    if(ee2.getNomeEntidade().equalsIgnoreCase("REGISTRO") && ee2.getDescTipoInforme().equals("justificante") && 
                            nomeColumna.toUpperCase().contains("FECHA")){	
                        String formatoFecha = ee2.getFormatoFecha();	
                        if(formatoFecha!=null && !formatoFecha.equals("")){	
                            formatoAlternativo = formatoFecha;	
                        }
                        if(etiquetasFecha==null) etiquetasFecha = new Vector();	
                        etiquetasFecha.add(nomeColumna);
                    }


                    if(etiquetasFecha!=null && estaEtiquetaFechaSeleccionada(nomeColumna,etiquetasFecha)){
                        String dd = DateOperations.formatoAlternativoCampoFechaHora(valor,formatoAlternativo);
                        if(dd!=null && !"".equals(dd))
                            tempCampos.put(nomeColumna + ConstantesDatos.SUFIJO_ETIQUETA_FEC_ALTERNATIVA, dd);

                    }
                    
                }// while
              
                if(ee.getNomeEntidade().equalsIgnoreCase("EXPEDIENTE") && !etiquetasRegistro){
                    etiquetasRegistro = true;
                    GeneralValueObject anotacion = AnotacionRegistroManager.getInstance().getAnotacionMasAntigua((String)parametros.get(1), params);
                    tempCampos.put(ConstantesDatos.ETIQ_FEC_ENTRADA_ANOTACION_REGISTRO,anotacion.getAtributo(ConstantesDatos.FECHA_ENTRADA_REGISTRO_ANOTACION));
                    tempCampos.put(ConstantesDatos.ETIQ_NUM_ENTRADA_ANOTACION_REGISTRO,anotacion.getAtributo(ConstantesDatos.NUMERO_REGISTRO_ANOTACION));
                }                

                nodo =
                        new es
                                .altia
                                .agora
                                .business
                                .geninformes
                                .utils
                                .XeracionInformes
                                .NodoEntidad();
                nodo.setCampos(tempCampos);
               
                nodo.setNomeEntidade(ee.getNomeEntidade());

                if (ee.getPai() != null) {

                    java.util.Collection instPai =
                            ee.getPai().getListaInstancias();
                    boolean atopado = false;
                    java.util.Iterator iterInst = instPai.iterator();
                    es
                            .altia
                            .agora
                            .business
                            .geninformes
                            .utils
                            .XeracionInformes
                            .NodoEntidad paiCandidato =
                            null;

                    while ((!atopado) && (iterInst.hasNext())) {
                        paiCandidato =
                                (es
                                        .altia
                                        .agora
                                        .business
                                        .geninformes
                                        .utils
                                        .XeracionInformes
                                        .NodoEntidad) iterInst
                                        .next();
                        atopado =
                                coincidenCamposJoin(params,
                                        ee.getPai().getCodEntidadeInforme(),
                                        ee.getCodEntidadeInforme(),
                                        paiCandidato,
                                        nodo);
                        if (atopado) {
                            paiCandidato.addHijoTipo(
                                    ee.getNomeEntidade(),
                                    nodo);
                        }
                    }
                }

                ee.getListaInstancias().add(nodo);

            }



            hijos.addAll(ee.getHijos());
            contador++;
        }


    }


    public static void construyeEstructuraDatosRelacion(String[] params, EstructuraEntidades ee2,Vector etiquetasFecha)
    throws Exception {

        //
        // coleccionNodosPrimerNivel contiene un Map de los nodos de Primer nivel de
        //						tipo es.altia.agora.business.geninformes.utils.XeracionInformes.NodoEntidad
        //						La clave del Map es el tipo de Entidad (nomeEntidade)
        //
        java.util.Vector hijos = new java.util.Vector();

        java.util.Map coleccionNodosPrimerNivel = new java.util.HashMap();

        es.altia.util.HashtableWithNull tabla =	new es.altia.util.HashtableWithNull();
        es.altia.util.conexion.Cursor cursorSalida = null;
        es.altia.agora.business.geninformes.utils.XeracionInformes.NodoEntidad nodo =null;
        int contador = 0;
        hijos.add(ee2);
        es.altia.agora.business.geninformes.utils.XeracionInformes.EstructuraEntidades ee =null;
        java.util.Vector parametros = null;

        while (contador < hijos.size()) {
            ee = (EstructuraEntidades) hijos.elementAt(contador);
            parametros = ee2.getValoresParametrosConsulta();
            cursorSalida = ejecutarConsulta(params, ee, parametros);

            java.util.HashMap tempCampos = null;
            String nomeColumna = null;
            java.util.Iterator iterColumnas = null;

            boolean etiquetasRegistro = false;
            while (cursorSalida.next()) {
                tempCampos = new java.util.HashMap();
                iterColumnas = cursorSalida.hash_Nombres.keySet().iterator();

                while (iterColumnas.hasNext()) {
                    /** original
                    nomeColumna = iterColumnas.next().toString();
                    tempCampos.put(nomeColumna, cursorSalida.getString(nomeColumna));
                    m_Log.info("CAMPO: "+ nomeColumna + " ; VALOR "+cursorSalida.getString(nomeColumna));
                     **/
                    nomeColumna = iterColumnas.next().toString();
                    String valor = cursorSalida.getString(nomeColumna);
                    tempCampos.put(nomeColumna, valor);
                    m_Log.debug("CAMPO: "+ nomeColumna + " ; VALOR "+ valor);

                    if(etiquetasFecha!=null && estaEtiquetaFechaSeleccionada(nomeColumna,etiquetasFecha)){
                        String dd = DateOperations.formatoAlternativoCampoFecha(valor,"yyyy/MM/dd");
                        if(dd!=null && !"".equals(dd))
                            tempCampos.put(nomeColumna + ConstantesDatos.SUFIJO_ETIQUETA_FEC_ALTERNATIVA, dd);
                    }
                }

                /**** PARA AÑADIR LOS VALORES DE LAS ETIQUETAS FECHA REGISTRO Y NUM ENTRADA ANOTACIÓN MÁS ANTIGUA ASOCIADA AL EXPEDIENTE ****/
                if(ee.getNomeEntidade().equalsIgnoreCase("RELACION") && !etiquetasRegistro){
                    etiquetasRegistro = true;
                    GeneralValueObject anotacion = AnotacionRegistroManager.getInstance().getAnotacionMasAntigua((String)parametros.get(1), params);
                    tempCampos.put(ConstantesDatos.ETIQ_FEC_ENTRADA_ANOTACION_REGISTRO,anotacion.getAtributo(ConstantesDatos.FECHA_ENTRADA_REGISTRO_ANOTACION));
                    tempCampos.put(ConstantesDatos.ETIQ_NUM_ENTRADA_ANOTACION_REGISTRO,anotacion.getAtributo(ConstantesDatos.NUMERO_REGISTRO_ANOTACION));
                }


                nodo = new NodoEntidad();
                nodo.setCampos(tempCampos);
                nodo.setNomeEntidade(ee.getNomeEntidade());

                if (ee.getPai() != null) {

                    Collection instPai = ee.getPai().getListaInstancias();

                    Iterator iterInst = instPai.iterator();
                    NodoEntidad paiCandidato;

                    while (iterInst.hasNext()) {
                        paiCandidato = (NodoEntidad) iterInst.next();
                        if (!iterInst.hasNext()) {
                            paiCandidato.addHijoTipo(ee.getNomeEntidade(), nodo);
                        }
                    }
                }

                ee.getListaInstancias().add(nodo);

            }

            hijos.addAll(ee.getHijos());
            contador++;
        }


    }
    /**
     * Method coincidenCamposJoin.
     * @param codEntidadePai Codigo de la entidad del padre candidato
     * @param codEntidade   Codigo de la entidad del nodo actual
     * @param nodoPaiCandidato Nodo del padre candidato
     * @param nodo  Nodo actual
     * @return boolean
     */
    private static boolean coincidenCamposJoin(String[] params,
                                               String codEntidadePai,
                                               String codEntidade,
                                               es
                                                       .altia
                                                       .agora
                                                       .business
                                                       .geninformes
                                                       .utils
                                                       .XeracionInformes
                                                       .NodoEntidad nodoPaiCandidato,
                                               es
                                                       .altia
                                                       .agora
                                                       .business
                                                       .geninformes
                                                       .utils
                                                       .XeracionInformes
                                                       .NodoEntidad nodo)
            throws Exception {
        boolean salida = true;


        java.util.Map camposPai = nodoPaiCandidato.getCampos();
        java.util.Map camposNodo = nodo.getCampos();


            String campoEnt = "NUMEXPEDIENTE";
            String campoSubEnt = "NUMEROEXPEDIENTE";
            salida
                    &= camposPai.get(campoEnt).toString().equals(
                    camposNodo.get(campoSubEnt).toString());

        return salida;

    }

    public static EstructuraEntidades construyeEstructuraEntidadesInforme(String[] params, String codInforme, String codEstructuraRaiz)
    throws Exception {

        EstructuraEntidades salida = new EstructuraEntidades(codInforme, codEstructuraRaiz);

        EstructuraEntidades tempEE2;
        HashtableWithNull tabla2 =new HashtableWithNull();

        EstructuraXerador ex = new EstructuraXerador(null);
        tabla2.put("COD_ESTRUCTURA", codEstructuraRaiz);
        tabla2.put("PARAMS", params);
        Cursor cursorEstructura = ex.ConsultaEstructuraEntidadesInforme(tabla2);
        cursorEstructura.next();

        //ESTRUCTURAINFORME.COD_ESTRUCTURA AS COD_ESTRUCTURA,
        //ESTRUCTURAINFORME.COD_ENTIDADEINFORME AS COD_ENTIDADEINFORME,
        //ESTRUCTURAINFORME.COD_PAI AS COD_PAI,
        //ESTRUCTURAINFORME.CONSULTASQL AS CONSULTASQL,
        //ESTRUCTURAINFORME.POSICION AS POSICION,
        //EI.NOME AS NOME,EI.CLAUSULAFROM AS CLAUSULAFROM,EI.CLAUSULAWHERE AS CLAUSULAWHERE ,EI.NOMEVISTA AS NOMEVISTA
        m_Log.debug(
                "codEstructuraRaiz es:"
                        + codEstructuraRaiz
                        + ".del cursor:"
                        + cursorEstructura.hash
                        + "hash_Nombres:"
                        + cursorEstructura.hash_Nombres
                        + ".");

        salida.setCodEstructura(cursorEstructura.getString("COD_ESTRUCTURA"));
        salida.setCodEntidadeInforme(cursorEstructura.getString("COD_ENTIDADEINFORME"));
        salida.setNomeEntidade(cursorEstructura.getString("NOME"));
        salida.setPai(null);
        salida.setCodPai(cursorEstructura.getString("COD_PAI"));

        salida.setConsultaSQL(cursorEstructura.getClob("CONSULTASQL"));

        salida.setPosicion(cursorEstructura.getString("POSICION"));
        salida.setConsultaEjecutada(false);
        salida.setCursorConsulta(null);

        java.util.Vector nuevosHijosNivel = null;
        Long salidaEstructura = null;
        java.util.Iterator iterCond = null;
        java.util.Iterator iterHijosNivel = null;

        java.util.Vector hijosNivel = new java.util.Vector();
        hijosNivel.add(salida);

        // Primer bucle recorre cada nivel
        while ((hijosNivel != null) && (hijosNivel.size() > 0)) {
            m_Log.debug("Primer bucle");
            iterHijosNivel = hijosNivel.iterator();
            nuevosHijosNivel = new java.util.Vector();
            while (iterHijosNivel.hasNext()) {
                m_Log.debug("Segundo bucle");
                es.altia.agora.business.geninformes.utils.XeracionInformes
                        .EstructuraEntidades hijo =(es.altia.agora.business.geninformes.utils.XeracionInformes
                        .EstructuraEntidades) iterHijosNivel
                        .next();

                es.altia.util.HashtableWithNull tabla =
                        new es.altia.util.HashtableWithNull();
                tabla.put("COD_ENTIDADEINFORME", hijo.getCodEntidadeInforme());
                tabla.put("PARAMS", params);


                tabla.put("COD_ESTRUCTURA", hijo.getCodEstructura());
                es.altia.util.conexion.Cursor cursorEnts =
                        ex.ConsultaHijosEstructuraInforme(tabla);
                m_Log.debug("1");

                while (cursorEnts.next()) {
                    tempEE2 =
                            new es
                                    .altia
                                    .agora
                                    .business
                                    .geninformes
                                    .utils
                                    .XeracionInformes
                                    .EstructuraEntidades(
                                    "0",
                                    cursorEnts.getString("COD_ENTIDADEINFORME"));
                    tempEE2.setCodEstructura(
                            cursorEnts.getString("COD_ESTRUCTURA"));
                    tempEE2.setCodEntidadeInforme(
                            cursorEnts.getString("COD_ENTIDADEINFORME"));
                    tempEE2.setNomeEntidade(cursorEnts.getString("NOME"));
                    tempEE2.setPai(hijo);
                    tempEE2.setCodPai(cursorEnts.getString("COD_PAI"));

                    tempEE2.setConsultaSQL(cursorEnts.getClob("CONSULTASQL"));
                    tempEE2.setPosicion(cursorEnts.getString("POSICION"));
                    tempEE2.setConsultaEjecutada(false);
                    tempEE2.setCursorConsulta(null);

                    hijo.addHijo(tempEE2);
                    nuevosHijosNivel.add(tempEE2);
                }


            }
            hijosNivel = nuevosHijosNivel;

        }
        return salida;

    }
    
    
    
    public static EstructuraEntidades construyeEstructuraEntidadesInformeDocumento(String[] params, String codInforme, String codEstructuraRaiz, Vector<String> parametros)
    throws Exception {

        EstructuraEntidades salida = new EstructuraEntidades(codInforme, codEstructuraRaiz);

        EstructuraEntidades tempEE2;
        HashtableWithNull tabla2 =new HashtableWithNull();

        EstructuraXerador ex = new EstructuraXerador(null);
        tabla2.put("COD_ESTRUCTURA", codEstructuraRaiz);
        tabla2.put("PARAMS", params);
        Cursor cursorEstructura = ex.ConsultaEstructuraEntidadesInforme(tabla2);
        cursorEstructura.next();

        //ESTRUCTURAINFORME.COD_ESTRUCTURA AS COD_ESTRUCTURA,
        //ESTRUCTURAINFORME.COD_ENTIDADEINFORME AS COD_ENTIDADEINFORME,
        //ESTRUCTURAINFORME.COD_PAI AS COD_PAI,
        //ESTRUCTURAINFORME.CONSULTASQL AS CONSULTASQL,
        //ESTRUCTURAINFORME.POSICION AS POSICION,
        //EI.NOME AS NOME,EI.CLAUSULAFROM AS CLAUSULAFROM,EI.CLAUSULAWHERE AS CLAUSULAWHERE ,EI.NOMEVISTA AS NOMEVISTA
        m_Log.debug(
                "codEstructuraRaiz es:"
                        + codEstructuraRaiz
                        + ".del cursor:"
                        + cursorEstructura.hash
                        + "hash_Nombres:"
                        + cursorEstructura.hash_Nombres
                        + ".");

        salida.setCodEstructura(cursorEstructura.getString("COD_ESTRUCTURA"));
        salida.setCodEntidadeInforme(cursorEstructura.getString("COD_ENTIDADEINFORME"));
        salida.setNomeEntidade(cursorEstructura.getString("NOME"));
        salida.setPai(null);
        salida.setCodPai(cursorEstructura.getString("COD_PAI"));

        salida.setConsultaSQL(cursorEstructura.getClob("CONSULTASQL"));

        salida.setPosicion(cursorEstructura.getString("POSICION"));
        salida.setConsultaEjecutada(false);
        salida.setCursorConsulta(null);

        java.util.Vector nuevosHijosNivel = null;
        Long salidaEstructura = null;
        java.util.Iterator iterCond = null;
        java.util.Iterator iterHijosNivel = null;

        java.util.Vector hijosNivel = new java.util.Vector();
        hijosNivel.add(salida);

        // Primer bucle recorre cada nivel
        while ((hijosNivel != null) && (hijosNivel.size() > 0)) {
            m_Log.debug("Primer bucle");
            iterHijosNivel = hijosNivel.iterator();
            nuevosHijosNivel = new java.util.Vector();
            while (iterHijosNivel.hasNext()) {
                m_Log.debug("Segundo bucle");
                es.altia.agora.business.geninformes.utils.XeracionInformes
                        .EstructuraEntidades hijo =(es.altia.agora.business.geninformes.utils.XeracionInformes
                        .EstructuraEntidades) iterHijosNivel
                        .next();

                es.altia.util.HashtableWithNull tabla =
                        new es.altia.util.HashtableWithNull();
                tabla.put("COD_ENTIDADEINFORME", hijo.getCodEntidadeInforme());
                tabla.put("PARAMS", params);


                tabla.put("COD_ESTRUCTURA", hijo.getCodEstructura());
                es.altia.util.conexion.Cursor cursorEnts =
                        ex.ConsultaHijosEstructuraInforme(tabla);
                m_Log.debug("1");

                while (cursorEnts.next()) {
                    tempEE2 =
                            new es
                                    .altia
                                    .agora
                                    .business
                                    .geninformes
                                    .utils
                                    .XeracionInformes
                                    .EstructuraEntidades(
                                    "0",
                                    cursorEnts.getString("COD_ENTIDADEINFORME"));
                    tempEE2.setCodEstructura(
                            cursorEnts.getString("COD_ESTRUCTURA"));
                    tempEE2.setCodEntidadeInforme(
                            cursorEnts.getString("COD_ENTIDADEINFORME"));
                    tempEE2.setNomeEntidade(cursorEnts.getString("NOME"));
                    tempEE2.setPai(hijo);
                    tempEE2.setCodPai(cursorEnts.getString("COD_PAI"));

                    tempEE2.setConsultaSQL(cursorEnts.getClob("CONSULTASQL"));
                    tempEE2.setPosicion(cursorEnts.getString("POSICION"));
                    tempEE2.setConsultaEjecutada(false);
                    tempEE2.setCursorConsulta(null);
                    tempEE2.setValoresParametrosConsulta(parametros);

                    hijo.addHijo(tempEE2);
                    nuevosHijosNivel.add(tempEE2);
                }


            }
            hijosNivel = nuevosHijosNivel;

        }
        return salida;

    }

    public static String ObtenerArrayJSAplicacionesUsuario(
            es.altia.util.HashtableWithNull entrada)
            throws Exception {
        es.altia.util.conexion.Cursor cursor =
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .UtilidadesXerador
                        .ObtenerCursorAplicacionesUsuario(entrada);
        return (
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .Utilidades
                        .ConvertirCursorToArrayJSEscaped(cursor));

    }

    /**
     * Method ObtenerCursorEntidadesCamposInforme.
     * @param entrada
     * @param hdlSesion
     * @return Cursor
     * @throws Exception
     */
    private static es
            .altia
            .util
            .conexion
            .Cursor ObtenerCursorAplicacionesUsuario(
            es.altia.util.HashtableWithNull entrada)
            throws Exception {
        es.altia.util.conexion.Cursor cursorLocal = null;
        cursorLocal =
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .bd
                        .UtilesXerador
                        .ConsultaAplicacionesUsuario(entrada);
        return cursorLocal;
    }

    public static String ObtenerArrayJSEtiquetasPlantillaInforme(
            es.altia.util.HashtableWithNull entrada)
            throws Exception {
        es.altia.util.conexion.Cursor cursor =
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .UtilidadesXerador
                        .ObtenerCursorEtiquetasPlantillaInforme(entrada);
        return (
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .Utilidades
                        .ConvertirCursorToArrayJSEscaped(cursor));

    }

    public static es
            .altia
            .util
            .conexion
            .Cursor ObtenerCursorEtiquetasPlantillaInforme(
            es.altia.util.HashtableWithNull entrada)
            throws Exception {
        es.altia.agora.business.geninformes.utils.bd.EtiqPlt etiqPlt =
                new es.altia.agora.business.geninformes.utils.bd.EtiqPlt(null);
        es.altia.util.conexion.Cursor cursorLocal = null;
        cursorLocal = etiqPlt.ConsultaEtiquetasPorInforme(entrada);

        return cursorLocal;
    }

    public static boolean tienePlantillaInforme(String[] params, String cod)
            throws Exception {
        boolean salida = false;
        es.altia.util.HashtableWithNull tabla =
                new es.altia.util.HashtableWithNull();
        tabla.put("PARAMS", params);
        tabla.put("COD_INFORMEXERADOR", cod);
        es.altia.util.conexion.Cursor cursorLocal =
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .bd
                        .UtilesXerador
                        .ConsultaTienePlantillaInforme(tabla);

        salida = !(cursorLocal.esVacio());

        return salida;

    }

    public static String ObtenerArrayJSCamposInformeEntidad(
            es.altia.util.HashtableWithNull entrada)
            throws Exception {
        es.altia.util.conexion.Cursor cursor =
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .UtilidadesXerador
                        .ObtenerCursorCamposInformeEntidad(entrada);
        return (
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .Utilidades
                        .ConvertirCursorToArrayJSEscaped(cursor));

    }

    /**
     * Method ObtenerCursorEntidadesCamposInforme.
     * @param entrada
     * @param hdlSesion
     * @return Cursor
     * @throws Exception
     */
    private static es
            .altia
            .util
            .conexion
            .Cursor ObtenerCursorCamposInformeEntidad(
            es.altia.util.HashtableWithNull entrada)
            throws Exception {
        es.altia.util.conexion.Cursor cursorLocal = null;
        cursorLocal =
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .bd
                        .UtilesXerador
                        .ConsultaCamposInformeEntidad(entrada);
        return cursorLocal;
    }

    /**
     * Method ObtenerArrayJSEntidadesInforme.
     * @param entrada
     * @return String
     * @throws Exception
     */
    public static String ObtenerArrayJSEntidadesInforme(
            es.altia.util.HashtableWithNull entrada)
            throws Exception {
        es.altia.util.conexion.Cursor cursor =
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .UtilidadesXerador
                        .ObtenerCursorEntidadesInforme(entrada);
        return (
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .Utilidades
                        .ConvertirCursorToArrayJSEscaped(cursor));

    }

    private static es.altia.util.conexion.Cursor ObtenerCursorEntidadesInforme(
            es.altia.util.HashtableWithNull entrada)
            throws Exception {
        es.altia.util.conexion.Cursor cursorLocal = null;
        cursorLocal =
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .bd
                        .UtilesXerador
                        .ConsultaEntidadesInforme(entrada);
        return cursorLocal;
    }



    public static String ObtenerArrayJSEstructurasInforme(
            es.altia.util.HashtableWithNull entrada)
            throws Exception {
        es.altia.util.conexion.Cursor cursor =
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .UtilidadesXerador
                        .ObtenerCursorEstructurasInforme(entrada);
        return (
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .Utilidades
                        .ConvertirCursorToArrayJSEscaped(cursor));

    }

    private static es.altia.util.conexion.Cursor ObtenerCursorEstructurasInforme(
            es.altia.util.HashtableWithNull entrada)
            throws Exception {
        es.altia.util.conexion.Cursor cursorLocal = null;
        cursorLocal =
                es
                        .altia
                        .agora
                        .business
                        .geninformes
                        .utils
                        .bd
                        .UtilesXerador
                        .ConsultaEstructurasInforme(entrada);
        return cursorLocal;
    }

}
