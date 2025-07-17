package es.altia.agora.business.informes.persistence.manual;

import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author sin atribuir
 * @version 1.0
 */

public class InformesDAO{
  //Para el fichero de configuracion tecnico.
  protected static Config campos;
  //Para informacion de logs.
  protected static Log m_Log =
            LogFactory.getLog(InformesDAO.class.getName());

  private static InformesDAO instance = null;

  private String select = "";
  private String from = " FROM ";
  private String where = " WHERE ";
  private String groupby = "";
  private String orderby = "";
  private String whereHistoricos = "";
  private static final String[] historicos = {"tra","utr","cls"};
  private boolean mostrarPendientes = false;
  private boolean mostrarExpedientes = false;
  private boolean mostrarHistoricos = false;
  private boolean leerPendientes = true;
  private boolean leerExpedientes = true;
  private boolean leerHistoricos = true;
  private String todos ="";

  private String lenguaje = null;
  private String municipio = null;

  private HashMap nom;

  protected InformesDAO() {
    super();

    nom = new HashMap();
    nom.put("tra_nombre","TRAMITE");
    nom.put("tpr_nombre","TIPOPROCEDIMIENTO");
    nom.put("are_nombre","AREA");
    nom.put("utr_nombre","UNIDAD");
    nom.put("pro_nombre","PROCEDIMIENTO");
    nom.put("cls_nombre","CLASIFICACIONTRAMITE");

    //Columnas que aparecen en la vista
    //nom.put("tra_columna","TRA_COD");
    // La consulta del tramite es por su descripcion y no por el codigo
    nom.put("tra_columna","TML_TRA");
    nom.put("tpr_columna","PRO_TIP");

    // Se comenta porque la búsqueda por código del procedimiento se hace por el campo PML_COD y no por PRO_ARE
    nom.put("are_columna","PRO_ARE");
    //nom.put("are_columna","PML_COD");
    nom.put("utr_columna","CRO_UTR");
    nom.put("pro_columna","CRO_PRO");
    nom.put("cls_columna","TRA_CLS");

    //noms en las que se buscan los nom y descripciones en distintos lenguajes
    nom.put("tra_tabla","E_TML");
    nom.put("tpr_tabla", GlobalNames.ESQUEMA_GENERICO+"A_TPML");
    nom.put("are_tabla",GlobalNames.ESQUEMA_GENERICO+"A_AML");
    nom.put("utr_tabla","A_UOR");
    nom.put("pro_tabla","E_PML");
    nom.put("cls_tabla",GlobalNames.ESQUEMA_GENERICO+"A_CML");

    //Genero de los parámetros de entrada: filtros y criterios de agrupacion
    nom.put("tra_genero","Todos");
    nom.put("tpr_genero","Todos");
    nom.put("are_genero","Todas");
    nom.put("utr_genero","Todas");
    nom.put("pro_genero","Todos");
    nom.put("cls_genero","Todas");

    //nom que saldrán en la página para las columnas
    nom.put("tra_pagina","inf_TRA");
    nom.put("tpr_pagina","inf_TPR");
    nom.put("are_pagina","inf_ARE");
    nom.put("utr_pagina","inf_UTR");
    nom.put("pro_pagina","inf_PRO");
    nom.put("cls_pagina","inf_CLS");

    //nom de los parámetros para posteriores consultas de expedientes
    nom.put("tra_param","codTramite");
    nom.put("tpr_param","codTipoProced");
    nom.put("are_param","codArea");
    nom.put("utr_param","codUnidadTram");
    nom.put("pro_param","codProcedimiento");
    nom.put("cls_param","codClasifTramite");

    //Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    lenguaje = campos.getString("idiomaDefecto");
  }

  public static InformesDAO getInstance() {
    //si no hay ninguna instancia de esta clase tenemos que crear una.
    synchronized(InformesDAO.class){
      if (instance == null)
         instance = new InformesDAO();
       }
    return instance;
  }

  /****************************************************************************/
  public Vector generarInforme(String[] params, Vector filtros, Vector grupos, GeneralValueObject paramsPagina)
  {

    String sql = "";
    AdaptadorSQLBD bd = null;
    Connection con=null;
    ResultSet rs=null;
    Vector estadisticas = null;

    select = "";
    from = " FROM ";
    where = " WHERE ";
    groupby = "";
    orderby = "";
    whereHistoricos="";
    mostrarHistoricos = false;
    leerPendientes = true; leerExpedientes = true; leerHistoricos = true;
    String cadena = "";

    try{
      bd = new AdaptadorSQLBD(params);
      con = bd.getConnection();

      //Columnas de dichas tablas en las que se encuentran dichas descripciones
      nom.put("tra_desc",bd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,
                             new String[]{bd.convertir("TRA_COD",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null),"'§¥'",bd.convertir("TRA_COU",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null),"'-'","TML_VALOR"}));
      nom.put("tpr_desc",bd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,
                             new String[]{bd.convertir("PRO_TIP",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null),"'§¥'","TPML_VALOR"}));
      nom.put("are_desc",bd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,
                             new String[]{bd.convertir("PRO_ARE",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null),"'§¥'","AML_VALOR"}));
      nom.put("utr_desc",bd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,
                             new String[]{bd.convertir("CRO_UTR",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null),"'§¥'","UOR_NOM"}));
      nom.put("pro_desc",bd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,
                             new String[]{bd.convertir("CRO_PRO",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null),"'§¥'","PML_VALOR"}));
      nom.put("cls_desc",bd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,
                             new String[]{bd.convertir("TRA_CLS",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null),"'§¥'",bd.convertir("TRA_CLS",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null),"'-'","CML_VALOR"}));

      this.municipio = (String)paramsPagina.getAtributo("municipio");

      String stiempo = (String)paramsPagina.getAtributo("Tiempo");
      int tiempo = 0;
      if (stiempo.equalsIgnoreCase("SEM")) tiempo = 1;
      else if (stiempo.equalsIgnoreCase("MES")) tiempo = 2;
      else if (stiempo.equalsIgnoreCase("TRI")) tiempo = 3;
      else if (stiempo.indexOf("||")>0) tiempo = 4;

      String verPend = (String)paramsPagina.getAtributo("VerPend"); 
      if (verPend.equals("true")) mostrarPendientes = true;
      else mostrarPendientes = false;
      String verVol = (String)paramsPagina.getAtributo("VerVol");
      if (verVol.equals("true")) mostrarExpedientes = true;
      else mostrarExpedientes = false;
      String verFin = (String)paramsPagina.getAtributo("VerFin");
      if (verFin.equals("true")) {
        mostrarHistoricos = true;
      } else {
          mostrarHistoricos = false;
      }
      todos = (String) paramsPagina.getAtributo("todos");


      establecerFiltros(filtros);
      establecerGrupos(grupos,bd);

      estadisticas = buscar(con, filtros, grupos, tiempo, bd, stiempo); 

      bd.devolverConexion(con);

    }catch (Exception e) {
      try{
       bd.devolverConexion(con);
      }catch(BDException bde) { bde.getMensaje(); }
      e.printStackTrace();
      //rollBackTransaction(bd,con,e);
    }


    return estadisticas;
  }

  public String verExisteExpedientes(String[] params, Vector filtros, Vector grupos, GeneralValueObject paramsPagina)
  {

    String sql = "";
    AdaptadorSQLBD bd = null;
    Connection con=null;
    ResultSet rs=null;
    String respuesta = "";

    select = "";
    from = " FROM ";
    where = " WHERE ";
    groupby = "";
    orderby = "";
    whereHistoricos="";
    mostrarHistoricos = false;
    leerPendientes = true; leerExpedientes = true; leerHistoricos = true;

    try{
      bd = new AdaptadorSQLBD(params);
      con = bd.getConnection();

      nom.put("tra_desc",bd.funcionCadena(bd.FUNCIONCADENA_CONCAT,
                             new String[]{"TRA_COD","'§¥'","TRA_COU","'-'","TML_VALOR"}));
      nom.put("are_desc",bd.funcionCadena(bd.FUNCIONCADENA_CONCAT,
                             new String[]{"PRO_ARE","'§¥'","AML_VALOR"}));
      nom.put("tpr_desc",bd.funcionCadena(bd.FUNCIONCADENA_CONCAT,
                             new String[]{bd.convertir("PRO_TIP",bd.CONVERTIR_COLUMNA_TEXTO,null),"'§¥'","TPML_VALOR"}));
      nom.put("utr_desc",bd.funcionCadena(bd.FUNCIONCADENA_CONCAT,
                             new String[]{"CRO_UTR","'§¥'","UOR_NOM"}));
      nom.put("pro_desc",bd.funcionCadena(bd.FUNCIONCADENA_CONCAT,
                             new String[]{"CRO_PRO","'§¥'","PML_VALOR"}));
      nom.put("cls_desc",bd.funcionCadena(bd.FUNCIONCADENA_CONCAT,
                             new String[]{"TRA_CLS","'§¥'","TRA_CLS","'-'","CML_VALOR"}));

      this.municipio = (String)paramsPagina.getAtributo("municipio");

      String stiempo = (String)paramsPagina.getAtributo("Tiempo");
      int tiempo = 0;
      if ("SEM".equalsIgnoreCase(stiempo)) tiempo = 1;
      else if ("MES".equalsIgnoreCase(stiempo)) tiempo = 2;
      else if ("TRI".equalsIgnoreCase(stiempo)) tiempo = 3;
      else if (stiempo.indexOf("#")>0 ) tiempo = 4;

      String verPend = (String)paramsPagina.getAtributo("VerPend");
      if (verPend.equals("true")) mostrarPendientes = true;
      else mostrarPendientes = false;
      String verVol = (String)paramsPagina.getAtributo("VerVol");
      if (verVol.equals("true")) mostrarExpedientes = true;
      else mostrarExpedientes = false;
      String verFin = (String)paramsPagina.getAtributo("VerFin");
      if (verFin.equals("true")) {
        mostrarHistoricos = true;
        //mostrarHistoricos = mostrarHistoricos(filtros, grupos);
      } else {
          mostrarHistoricos = false;
      }
      todos = (String) paramsPagina.getAtributo("todos");


      establecerFiltros(filtros);
      establecerGrupos(grupos,bd);

      respuesta = existeExpedientes(con, filtros, grupos, tiempo, stiempo); 

      bd.devolverConexion(con);

    }catch (Exception e) {
      try{
       bd.devolverConexion(con);
      }catch(BDException bde) { bde.getMensaje(); }
      e.printStackTrace();
      //rollBackTransaction(bd,con,e);
    }


    return respuesta;
  }

  private String existeExpedientes(Connection con, Vector filtros, Vector grupos, int tiempo, String intervalo)
  {
    String respuesta = "";

    try{
        String whereBack = where;
        String selectBack = select;
        String fromBack = from;

        Statement stmt = null;
        ResultSet rsExpedientes = null;

        if (mostrarExpedientes){
        //Consulta expedientes
        where=whereBack;
        select=selectBack;
        from = fromBack;
        select+=" COUNT (EXP_FEF) AS CERRADOS,"+
                " COUNT(DISTINCT CASE WHEN EXP_FEF IS NULL THEN CRO_NUM ELSE NULL END) AS ENTRAMITACION,"+
                " COUNT(DISTINCT CRO_NUM) AS TOTALES ";
        from += " EXPEDIENTES ";
        if (!where.equals(" WHERE ")) where+="AND";
        where += "  (EXP_FEF IS NOT NULL OR CRO_FEF IS NULL)";
        where +=" AND CRO_MUN="+municipio;
        String sqlExpedientes = "SELECT "+select+from+where+groupby+orderby;
        if(m_Log.isDebugEnabled()) m_Log.debug("sqlExpedientes: "+sqlExpedientes);

        stmt = con.createStatement();
        rsExpedientes = stmt.executeQuery(sqlExpedientes);
        if(rsExpedientes.next()) {
            respuesta = "hayExpedientes";
        } else {
            respuesta = "noHayExpedientes";
        }
      }
    }catch(Exception e){
      e.printStackTrace();
    }


    return respuesta;

  }


  private ArrayList<ArrayList<Integer>>  rellenarClausulaWhen(Vector grupos){

        int totalCeros = 0;
        int longitud = grupos.size();
        ArrayList<ArrayList<Integer>> salida = new ArrayList<ArrayList<Integer>>();

        for(int j=0;j<grupos.size();j++)
        {
                totalCeros = j+1;
                ArrayList<Integer> datos = new ArrayList<Integer>();
                int contador=0;
                for(int i=0;i<longitud;i++){
                    if(contador<totalCeros)
                        datos.add(0);
                    else
                        datos.add(1);
                    contador++;
                }// for
                salida.add(datos);
        }

      return salida;
  }

  /****************************************************************************/
  private Vector buscar(Connection con, Vector filtros, Vector grupos, int tiempo, AdaptadorSQLBD bd, String intervalo)
  {
    Vector estadisticas = new Vector();
    Statement stmt = null;
    ResultSet rsPendientes = null;
    ResultSet rsExpedientes = null;
    ResultSet rsHistoricos = null;

    int posicion_intervalo = 0;
    
    try{

      String whereBack = where;
      String selectBack = select;
      String fromBack = from;

      

        String sqlExternaSelectPendHist1 = "";
        String sqlExternaSelectPendHist2 = "";
        String sqlExternaSelectPendHist = "";
        String sumaExpedientesExterna = "sum(expedientes) as expedientes";
        String sqlExternaSelectExpedientes = "";
        String sqlExternaGroupBy = "";
        String caseExp = "(CASE WHEN";

    ArrayList camposAgrupacion =  new ArrayList();
    for (int i=0; i<grupos.size(); i++){
       String grupo = (String)grupos.elementAt(i);
       String descripcion = (String) nom.get(grupo+"_desc");
       String nombre = (String) nom.get(grupo+"_nombre");
       String columna = (String) nom.get(grupo+"_columna");
       String tabla = (String) nom.get(grupo+"_tabla");
       String prefijo = tabla.substring(tabla.lastIndexOf("_")+1);//tabla.substring(2);
       if(m_Log.isDebugEnabled()){
           m_Log.debug("descripcion : " + descripcion);
           m_Log.debug("nombre : " + nombre);
           m_Log.debug("columna : " + columna);
           m_Log.debug("tabla : " + tabla);
           m_Log.debug("prefijo : " + prefijo);
       }

        // Usamos #### para las filas resumen para garantizar que salen primeras en la ordenación
        sqlExternaSelectPendHist1 = "(CASE GROUPING ("+nombre+") WHEN 1 THEN '####' ELSE "+nombre+" END) AS "+
                    nombre + ", "+ sqlExternaSelectPendHist1;
        caseExp += " GROUPING("+nombre+")=1 ";
        if (i!=grupos.size()-1) caseExp += "AND";
        else caseExp += "THEN";
        camposAgrupacion.add(nombre);

    }
     sqlExternaGroupBy = bd.rollup((String[])camposAgrupacion.toArray(new String[]{}));

     sqlExternaSelectExpedientes = "SELECT "+ sqlExternaSelectPendHist1 ;
     sqlExternaSelectPendHist1 = "SELECT "+ sqlExternaSelectPendHist1 + "sum(tareas) as tareas,  ";
     sqlExternaSelectPendHist2 = " , round(sum(multi)/sum(tareas),0) as tiempos";


     /*****************/
      String caseExpWhen = "";
     /**** CLAUSULA WHEN PARA LA CONSULTA DE EXPEDIENTES PENDIENTES  ***/
     ArrayList<ArrayList<Integer>> datos =  rellenarClausulaWhen(grupos);
     String whereExpedientes="";
     if(mostrarPendientes){
         from+=" TAREAS";
        if (!where.equals(" WHERE ")) where+="AND";
         whereExpedientes = where; 
        where+=" CRO_FEF IS NULL" ;
        where+=" AND CRO_MUN="+municipio;
        
        posicion_intervalo = intervalo.indexOf("||");
        if(posicion_intervalo!=-1){
            String fechaInicio = intervalo.substring(0,posicion_intervalo);
            String fechaFin    = intervalo.substring(posicion_intervalo + 2);
            if(fechaInicio!=null && !"".equals(fechaInicio) && fechaFin!=null && !"".equals(fechaFin)){
                // original          
                where += " AND (CRO_FEI BETWEEN " + bd.convertir("'"+ fechaInicio +"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+ " AND " +
                         bd.convertir("'"+ fechaFin +"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY") + ")";        
            }
        }
        
        whereExpedientes+=" CRO_MUN="+municipio;
     }

        boolean procedimiento = false;
        boolean tramite = false;
        boolean unidad = false;
        boolean tipoProc = false;
        boolean area = false;
        boolean clasifTramite = false;
      
            String grupo_sel = (String)grupos.elementAt(0);
            if (((String) nom.get(grupo_sel+"_nombre")).toUpperCase().equals("PROCEDIMIENTO"))
                procedimiento = true;
            if (((String) nom.get(grupo_sel+"_nombre")).toUpperCase().equals("TRAMITE"))
                tramite = true;
            if (((String) nom.get(grupo_sel+"_nombre")).toUpperCase().equals("UNIDAD"))
                unidad=true;
            if (((String) nom.get(grupo_sel+"_nombre")).toUpperCase().equals("TIPOPROCEDIMIENTO"))
                tipoProc=true;
            if (((String) nom.get(grupo_sel+"_nombre")).toUpperCase().equals("AREA"))
                area=true;
            if (((String) nom.get(grupo_sel+"_nombre")).toUpperCase().equals("CLASIFICACIONTRAMITE"))
                clasifTramite=true;
        
    

    for (int j=0; j<datos.size()-1; j++)
    {
        ArrayList<Integer> valores = datos.get(j);
          if(valores!=null && valores.size()>0)
                caseExpWhen +=" WHEN ";

        for (int i=0; i<grupos.size(); i++)
        {
               String grupo = (String)grupos.elementAt(i);
               String nombre = (String) nom.get(grupo+"_nombre");

               
               caseExpWhen += " GROUPING(" +  nombre + ")=##";

                if (i!=grupos.size()-1)
                    caseExpWhen += " AND ";
                else{
                    caseExpWhen += " THEN ";
                     if (mostrarPendientes)
                    {
                        
 

                        String  caseWhenExpPend = "";
                        if (procedimiento){
                            String charIndex = bd.charIndex("'§¥'", "PROCEDIMIENTO","1","1")  + "-1";
                            String substring = bd.substr("PROCEDIMIENTO","1",charIndex);
                            caseWhenExpPend = "(select COUNT(distinct(cro_num)) "+ from + where + "  AND CRO_PRO = " + substring + ")";
                        }
                        else if (tramite){ 
                            String charIndex = bd.charIndex("'§¥'", "TRAMITE","1","1")  + "-1";
                            String substring = bd.substr("TRAMITE","1",charIndex);
                            caseWhenExpPend = "(select COUNT(distinct(cro_num)) "+ from + where + "  AND CRO_TRA = " + substring + ")";
                        }
                        else if (unidad){
                            String charIndex = bd.charIndex("'§¥'", "UNIDAD","1","1")  + "-1";
                            String substring = bd.substr("UNIDAD","1",charIndex);
                            caseWhenExpPend = "(select COUNT(distinct(cro_num)) "+ from + where + "  AND CRO_UTR = " + substring + ")";
                        }
                        else if (tipoProc){
                            String charIndex = bd.charIndex("'§¥'", "TIPOPROCEDIMIENTO","1","1")  + "-1";
                            String substring = bd.substr("TIPOPROCEDIMIENTO","1",charIndex);
                            caseWhenExpPend = "(select COUNT(distinct(cro_num)) "+ from + where + "  AND PRO_TIP = " + substring + ")";
                        }
                        else if (area){
                            String charIndex = bd.charIndex("'§¥'", "AREA","1","1")  + "-1";
                            String substring = bd.substr("AREA","1",charIndex);
                            caseWhenExpPend = "(select COUNT(distinct(cro_num)) "+ from + where + "  AND PRO_ARE = " + substring + ")";
                        }
                        else if (clasifTramite){
                            String charIndex = bd.charIndex("'§¥'", "CLASIFICACIONTRAMITE","1","1")  + "-1";
                            String substring = bd.substr("CLASIFICACIONTRAMITE","1",charIndex);
                            caseWhenExpPend = "(select COUNT(distinct(cro_num)) "+ from + where + "  AND TRA_CLS = " + substring + ")";
                        }
                                                                                                    
                        else
                            caseWhenExpPend = "(select COUNT(distinct(cro_num)) "+ from + where + " )";

                        caseExpWhen += caseWhenExpPend;
                    }
                }
        }// for

         // Para la consulta de la parte When generada, hay que sustituir los ## por los valores correspondientes
         for(int h=0;h<valores.size();h++){
             Integer valor = valores.get(h);
             caseExpWhen=caseExpWhen.replaceFirst("##", valor.toString());

         }
    }// for

   // INICIO: Se añade en el último grouping la comparación del código del trámite si hay 3 o más agrupaciones
    if(grupos.size()>=3){
       String charIndexTra = bd.charIndex("'§¥'", "TRAMITE","1","1")  + "-1";
       String substringTra  = bd.substr("TRAMITE","1",charIndexTra);
       caseExpWhen = caseExpWhen.substring(0,caseExpWhen.length()-1);
       if (tramite) caseExpWhen += " AND TRA_COD=" + substringTra;
       caseExpWhen += " )";
       m_Log.debug(" ==========> caseExpWhen: " + caseExpWhen) ;

       
    }
   // FIN: Se añade en el último grouping la comparación del código del trámite

    caseExpWhen += " ELSE sum(expedientes) END) AS expedientes ";

     /******************/

    /** SI EL GESTOR DE BASE DE DATOS ES ORACLE SE HACE EL SIGUIENTE ALTER DE LA SESIÓN */
    ResourceBundle bundle = ResourceBundle.getBundle("techserver");
    String gestor = bundle.getString("CON.gestor");

    if("oracle".equalsIgnoreCase(gestor)){
        String alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
        String alter2 = "ALTER SESSION SET NLS_SORT=BINARY";

        stmt = con.createStatement();
        stmt.executeQuery(alter1);
        stmt.executeQuery(alter2);
        stmt.close();
    }


      //Consulta tareas pendientes
      if (mostrarPendientes){
          
          String fechaInicio="";
           if("oracle".equalsIgnoreCase(gestor)){
                fechaInicio="(to_date(to_char(cro_fei, 'DD-MM-YYY')))";
           }else fechaInicio="CRO_FEI";
          
        select+=" COUNT(*) AS TAREAS," +
                " COUNT(DISTINCT(CRO_NUM)) AS EXPEDIENTES," +
                bd.funcionMatematica(bd.FUNCIONMATEMATICA_ROUND,
                                    new String[]{bd.funcionMatematica(bd.FUNCIONMATEMATICA_AVG,new String[]{
                                            bd.convertir(bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE,null)+"- "+fechaInicio, bd.CONVERTIR_COLUMNA_NUMERO,null)}),"0"})+" AS TIEMPOS " +
                ", COUNT(*)*" + bd.funcionMatematica(bd.FUNCIONMATEMATICA_AVG,new String[]{
                                            bd.convertir(bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE,null)+"- "+fechaInicio, bd.CONVERTIR_COLUMNA_NUMERO,null)}) + " AS multi";

        String sqlPendientes = "SELECT "+select+from+where+groupby;
        String caseExpPend = "(select COUNT(distinct(cro_num)) "+ from + where + " ) ";

        sqlExternaSelectPendHist = sqlExternaSelectPendHist1 + caseExp + caseExpPend + caseExpWhen + sqlExternaSelectPendHist2;
        sqlPendientes = sqlExternaSelectPendHist + " FROM (" + sqlPendientes + ") a " + sqlExternaGroupBy + orderby;

        if(m_Log.isDebugEnabled()) m_Log.debug("sqlPendientes: "+sqlPendientes);
        stmt = con.createStatement();
        rsPendientes = stmt.executeQuery(sqlPendientes);
      }


    /** SI EL GESTOR DE BASE DE DATOS ES ORACLE SE HACE EL SIGUIENTE ALTER DE LA SESIÓN */
    if("oracle".equalsIgnoreCase(gestor)){
        String alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
        String alter2 = "ALTER SESSION SET NLS_SORT=BINARY";

        stmt = con.createStatement();
        stmt.executeQuery(alter1);
        stmt.executeQuery(alter2);
        stmt.close();
    }


        String caseEnTramitacion = "";
        String caseSubtotales = "";
        String subtotalesFin = "";

        for (int j = 0; j < datos.size() - 1; j++) {
            ArrayList<Integer> valores = datos.get(j);
            if (valores != null && valores.size() > 0) {
                caseEnTramitacion += " WHEN ";
                caseSubtotales += " WHEN ";
                subtotalesFin += " WHEN ";
            }

            String agrupNivel2 = (String) nom.get(grupos.elementAt(0) + "_nombre");
            for (int i = 0; i < grupos.size(); i++) {

                String grupo = (String) grupos.elementAt(i);
                String nombre = (String) nom.get(grupo + "_nombre");


                caseEnTramitacion += " GROUPING(" + nombre + ")=##";
                caseSubtotales += " GROUPING(" + nombre + ")=##";
                subtotalesFin += " GROUPING(" + nombre + ")=##";
                if (i != grupos.size() - 1) {
                    caseEnTramitacion += " AND ";
                    caseSubtotales += " AND ";
                    subtotalesFin += " AND ";
                } else {

                    String charIndexProc = bd.charIndex("'§¥'", "PROCEDIMIENTO", "1", "1") + "-1";
                    String substringProc = bd.substr("PROCEDIMIENTO", "1", charIndexProc);
                    String charIndexUtr = bd.charIndex("'§¥'", "UNIDAD", "1", "1") + "-1";
                    String substringUtr = bd.substr("UNIDAD", "1", charIndexUtr);
                    String charIndexArea = bd.charIndex("'§¥'", "AREA", "1", "1") + "-1";
                    String substringArea = bd.substr("AREA", "1", charIndexArea);
                    String charIndexTipo = bd.charIndex("'§¥'", "TIPOPROCEDIMIENTO", "1", "1") + "-1";
                    String substringTipo = bd.substr("TIPOPROCEDIMIENTO", "1", charIndexTipo);

                    
                    if (agrupNivel2.equals("PROCEDIMIENTO")) {
                        caseEnTramitacion += " THEN (select COUNT(DISTINCT CASE WHEN EXP_FEF IS NULL THEN CRO_NUM ELSE NULL END) "
                                + from + where
                                + " AND CRO_PRO = " + substringProc + ")";
                        caseSubtotales += " THEN (select COUNT(DISTINCT CRO_NUM) "
                                + from + whereExpedientes
                                + " AND CRO_PRO = " + substringProc + ")";
                        subtotalesFin += "then (select COUNT (DISTINCT CASE WHEN EXP_FEF IS not NULL THEN CRO_NUM ELSE NULL END)"
                                + from + whereExpedientes + " AND CRO_PRO = " + substringProc + ")";

                    } else if (agrupNivel2.equals("UNIDAD")) {
                        caseEnTramitacion += " THEN (select COUNT(DISTINCT CASE WHEN EXP_FEF IS NULL THEN CRO_NUM ELSE NULL END) "
                                + from + where
                                + " AND CRO_UTR = " + substringUtr + ")";
                        caseSubtotales += " THEN (select COUNT(DISTINCT CRO_NUM) "
                                + from + whereExpedientes
                                + " AND CRO_UTR = " + substringUtr + ")";
                        subtotalesFin += "then (select COUNT (DISTINCT CASE WHEN EXP_FEF IS not NULL THEN CRO_NUM ELSE NULL END)"
                                + from + whereExpedientes + " AND CRO_UTR = " + substringUtr + ")";

                    } else if (agrupNivel2.equals("AREA")) {
                        caseEnTramitacion += " THEN (select COUNT(DISTINCT CASE WHEN EXP_FEF IS NULL THEN CRO_NUM ELSE NULL END) "
                                + from + where
                                + " AND PRO_ARE = " + substringArea + ")";
                        caseSubtotales += " THEN (select COUNT(DISTINCT CRO_NUM) "
                                + from + whereExpedientes
                                + " AND PRO_ARE = " + substringArea + ")";
                        subtotalesFin += "then (select COUNT (DISTINCT CASE WHEN EXP_FEF IS not NULL THEN CRO_NUM ELSE NULL END)"
                                + from + whereExpedientes + " AND PRO_ARE= " + substringArea + ")";
                    } else if (agrupNivel2.equals("TIPOPROCEDIMIENTO")) {
                        caseEnTramitacion += " THEN (select COUNT(DISTINCT CASE WHEN EXP_FEF IS NULL THEN CRO_NUM ELSE NULL END) "
                                + from + where
                                + " AND PRO_TIP = " + substringTipo + ")";
                        caseSubtotales += " THEN (select COUNT(DISTINCT CRO_NUM) "
                                + from + whereExpedientes
                                + " AND PRO_TIP = " + substringTipo + ")";
                        subtotalesFin += "then (select COUNT (DISTINCT CASE WHEN EXP_FEF IS not NULL THEN CRO_NUM ELSE NULL END)"
                                + from + whereExpedientes + " AND PRO_TIP= " + substringTipo + ")";
                    } else {
                        caseEnTramitacion += " THEN (select COUNT(DISTINCT CASE WHEN EXP_FEF IS NULL THEN CRO_NUM ELSE NULL END) "
                                + from + where + " )";
                        caseSubtotales += " THEN (select COUNT(DISTINCT CRO_NUM) "
                                + from + whereExpedientes + " )";
                        subtotalesFin += "then (select COUNT (DISTINCT CASE WHEN EXP_FEF IS not NULL THEN CRO_NUM ELSE NULL END)"
                                + from + whereExpedientes + ") ";
                    }
                }

            }// for

            // Para la consulta de la parte When generada, hay que sustituir los ## por los valores correspondientes
            m_Log.debug(valores);
            for (int h = 0; h < valores.size(); h++) {
                Integer valor = valores.get(h);
                caseEnTramitacion = caseEnTramitacion.replaceFirst("##", valor.toString());
                caseSubtotales = caseSubtotales.replaceFirst("##", valor.toString());
                subtotalesFin = subtotalesFin.replaceFirst("##", valor.toString());

            }
        }// for

        // INICIO: Se añade en el último grouping la comparación del código del trámite si hay 3 o más agrupaciones
        if (grupos.size() >= 3) {
            String charIndexTra = bd.charIndex("'§¥'", "TRAMITE", "1", "1") + "-1";
            String substringTra = bd.substr("TRAMITE", "1", charIndexTra);
            caseEnTramitacion = caseEnTramitacion.substring(0, caseEnTramitacion.length() - 1);
            caseSubtotales = caseSubtotales.substring(0, caseSubtotales.length() - 1);
            subtotalesFin = subtotalesFin.substring(0, subtotalesFin.length() - 1);
            if (tramite) {
                caseEnTramitacion += " AND TRA_COD=" + substringTra;
                caseSubtotales += " AND TRA_COD=" + substringTra;
                subtotalesFin += " AND TRA_COD=" + subtotalesFin;
            }
            caseEnTramitacion += " )";
            caseSubtotales += " )";
            subtotalesFin += " )";

        }

        caseEnTramitacion += "ELSE sum(entramitacion) END) AS entramitacion";
        caseSubtotales += " ELSE sum(totales) END) AS totales";
        subtotalesFin += " ELSE sum(cerrados) END) AS cerrados";


      if (mostrarExpedientes){
        //Consulta expedientes
        where=whereBack;
        select=selectBack;
        from = fromBack;
            select += " COUNT (DISTINCT CASE WHEN EXP_FEF IS NOT NULL THEN CRO_NUM ELSE NULL END) AS CERRADOS,"
                    + " COUNT(DISTINCT CASE WHEN cro_FEF IS NULL THEN CRO_NUM ELSE NULL END) AS ENTRAMITACION,"
                    + " COUNT(DISTINCT CRO_NUM) AS TOTALES ";
            from += " TAREAS ";
            if (!where.equals(" WHERE ")) {
                where += "AND";
            }
        where +=" CRO_MUN="+municipio;
            String caseCerrados = "(select COUNT (DISTINCT CASE WHEN EXP_FEF IS not NULL THEN CRO_NUM ELSE NULL END) " + from + whereExpedientes + " ) ";
            String caseTotales = "(select COUNT (DISTINCT CRO_NUM) " + from + whereExpedientes + " ) ";
        String sqlExpedientes = "SELECT "+select+from+where+groupby;
            String caseSubtotalTramitacion = " (select COUNT(DISTINCT CASE WHEN EXP_FEF IS NULL THEN CRO_NUM ELSE NULL END) " + from + where + " )";
            sqlExternaSelectExpedientes = sqlExternaSelectExpedientes + caseExp + caseCerrados + subtotalesFin + ","
                    + caseExp + caseSubtotalTramitacion + caseEnTramitacion + ","
                    + caseExp + caseTotales + caseSubtotales;
          sqlExpedientes = sqlExternaSelectExpedientes + " FROM (" + sqlExpedientes + ") a " + sqlExternaGroupBy + orderby;

        if(m_Log.isDebugEnabled()) m_Log.debug("sqlExpedientes: "+sqlExpedientes);

        stmt = con.createStatement();
        rsExpedientes = stmt.executeQuery(sqlExpedientes);
      }


     /********************* OPERACIONES PARA LOS WHEN ***************/
     if(mostrarHistoricos){
        select = selectBack;
        from = fromBack;
        where = whereBack;

        from += " TAREAS ";
        if (!where.equals(" WHERE ")) where+="AND";
        where+=" CRO_MUN="+municipio;
        where+=" AND CRO_FEF IS NOT NULL " ;
     }

     String caseExpHistoricoWhen ="";
    for (int j=0; j<datos.size()-1; j++)
    {
        ArrayList<Integer> valores = datos.get(j);
        if(valores!=null && valores.size()>0)
                caseExpHistoricoWhen +=" WHEN ";

        for (int i=0; i<grupos.size(); i++)
        {
               String grupo = (String)grupos.elementAt(i);
               String nombre = (String) nom.get(grupo+"_nombre");

                // Usamos #### para las filas resumen para garantizar que salen primeras en la ordenación
               caseExpHistoricoWhen += "GROUPING(" +  nombre + ")=##";

                if (i!=grupos.size()-1)
                    caseExpHistoricoWhen += " AND ";
                else{
                    caseExpHistoricoWhen += " THEN ";
                    if(mostrarHistoricos)
                    {
                        String  caseWhenExpPend = "";
                        if (procedimiento){
                            String charIndex = bd.charIndex("'§¥'", "PROCEDIMIENTO","1","1")  + "-1";
                            String substring = bd.substr("PROCEDIMIENTO","1",charIndex);
                            if(tiempo!=4)
                                caseWhenExpPend = "(select COUNT(distinct(cro_num)) "+ from + where + "  AND CRO_PRO = " + substring + ")";
                            else{
                                int posicion = intervalo.indexOf("||");
                                if(posicion!=-1){
                                    String fechaInicio = intervalo.substring(0,posicion);
                                    String fechaFin    = intervalo.substring(posicion + 2);
                                    String whereAuxiliar = "";
                                    if(fechaInicio!=null && !"".equals(fechaInicio) && fechaFin!=null && !"".equals(fechaFin)){
                                        // original          
                                        whereAuxiliar += " AND (CRO_FEF BETWEEN " + bd.convertir("'"+ fechaInicio +"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+ " AND " +
                                                 bd.convertir("'"+ fechaFin +"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY") + ")";        
                                        
                                        caseWhenExpPend = "(select COUNT(distinct(cro_num)) "+ from + where + whereAuxiliar + "  AND CRO_PRO = " + substring + ")";
                                    }
                                }     
                                
                            }
                        }
                        else if (tramite){ 
                            String charIndex = bd.charIndex("'§¥'", "TRAMITE","1","1")  + "-1";
                            String substring = bd.substr("TRAMITE","1",charIndex);
                            caseWhenExpPend = "(select COUNT(distinct(cro_num)) "+ from + where + "  AND CRO_TRA = " + substring + ")";
                        }
                        else if (unidad){
                            String charIndex = bd.charIndex("'§¥'", "UNIDAD","1","1")  + "-1";
                            String substring = bd.substr("UNIDAD","1",charIndex);
                            caseWhenExpPend = "(select COUNT(distinct(cro_num)) "+ from + where + "  AND CRO_UTR = " + substring + ")";
                        }
                        else if (tipoProc){
                            String charIndex = bd.charIndex("'§¥'", "TIPOPROCEDIMIENTO","1","1")  + "-1";
                            String substring = bd.substr("TIPOPROCEDIMIENTO","1",charIndex);
                            caseWhenExpPend = "(select COUNT(distinct(cro_num)) "+ from + where + "  AND PRO_TIP = " + substring + ")";
                        }
                        else if (area){
                            String charIndex = bd.charIndex("'§¥'", "AREA","1","1")  + "-1";
                            String substring = bd.substr("AREA","1",charIndex);
                            caseWhenExpPend = "(select COUNT(distinct(cro_num)) "+ from + where + "  AND PRO_ARE = " + substring + ")";
                        }
                        else if (clasifTramite){
                            String charIndex = bd.charIndex("'§¥'", "CLASIFICACIONTRAMITE","1","1")  + "-1";
                            String substring = bd.substr("CLASIFICACIONTRAMITE","1",charIndex);
                            caseWhenExpPend = "(select COUNT(distinct(cro_num)) "+ from + where + "  AND TRA_CLS = " + substring + ")";
                        }
                                                                                                    
                        else
                            caseWhenExpPend = "(select COUNT(distinct(cro_num)) "+ from + where + " )";
                                                                        
                        caseExpHistoricoWhen += caseWhenExpPend;
                    }
                }
             }// for
             // Para la consulta de la parte When generada, hay que añadirle los valores
             for(int h=0;h<valores.size();h++){
                 Integer valor = valores.get(h);
                 caseExpHistoricoWhen=caseExpHistoricoWhen.replaceFirst("##", valor.toString());
             }
    }// for


     // INICIO: Se añade en el último grouping la comparación del código del trámite si hay 3 o más agrupaciones
    if(grupos.size()>=3){
       String charIndexTra = bd.charIndex("'§¥'", "TRAMITE","1","1")  + "-1";
       String substringTra  = bd.substr("TRAMITE","1",charIndexTra);
       caseExpHistoricoWhen = caseExpHistoricoWhen.substring(0,caseExpHistoricoWhen.length()-1);
        if (tramite) caseExpHistoricoWhen += " AND TRA_COD=" + substringTra ;
       caseExpHistoricoWhen+= " )";
       m_Log.debug(" ==========> caseExpHistoricoWhen: " + caseExpHistoricoWhen);
    }
    // FIN: Se añade en el último grouping la comparación del código del trámite
    caseExpHistoricoWhen += " ELSE sum(expedientes) END) AS expedientes ";

    /** SI EL GESTOR DE BASE DE DATOS ES ORACLE SE HACE EL SIGUIENTE ALTER DE LA SESIÓN */
    if("oracle".equalsIgnoreCase(gestor)){
        String alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
        String alter2 = "ALTER SESSION SET NLS_SORT=BINARY";

        stmt = con.createStatement();
        stmt.executeQuery(alter1);
        stmt.executeQuery(alter2);
        stmt.close();
    }


       /********************* FIN DE LAS OPERACIONES PARA LOS WHEN ***************/
      //Consulta historicos
      if (mostrarHistoricos)
      {
        String whereTMedios = null;
        switch(tiempo){
          case 0: {
            whereTMedios="";
            break;
          }
          case 1: {
            whereTMedios=" AND CRO_FEF > ("+bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE,null)+"-7) ";
            break;
          }
          case 2: {
            whereTMedios=" AND CRO_FEF > ("+bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE,null)+"-30) ";
            break;
          }
        case 3: {
          whereTMedios=" AND CRO_FEF > ("+bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE,null)+"-90) ";
          break;
          }
        case 4: {
          posicion_intervalo = intervalo.indexOf("||");
          // original
          //whereTMedios= "AND CRO_FEF >'" + intervalo.substring(0, posicion_intervalo) +"' and CRO_FEF < '" + intervalo.substring(posicion_intervalo+2) +"' ";
          whereTMedios = " AND (CRO_FEF BETWEEN " + bd.convertir("'"+ intervalo.substring(0,posicion_intervalo) + "'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+ " AND " +
                          bd.convertir("'"+ intervalo.substring(posicion_intervalo +2) + "'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY") + ")";
          
          break;
          }
        }

        String fechaInicio="";
           if("oracle".equalsIgnoreCase(gestor)){
                fechaInicio="(to_date(to_char(cro_fei, 'DD-MM-YYY')))";
           }else fechaInicio="CRO_FEI";
           
           //bd.convertir(bd.convertir("CRO_FEI",bd.CONVERTIR_COLUMNA_TEXTO,'DD-MM-YYY'), bd.CONVERTIR_COLUMNA_FECHA,null)
           
        select+=" COUNT(*) AS TAREAS," +
                " COUNT(DISTINCT(CRO_NUM)) AS EXPEDIENTES," +
                bd.funcionMatematica(bd.FUNCIONMATEMATICA_ROUND,
                                    new String[]{bd.funcionMatematica(bd.FUNCIONMATEMATICA_AVG,new String[]{
                                            bd.convertir("CRO_FEF- "+fechaInicio,
                                                    bd.CONVERTIR_COLUMNA_NUMERO,null)}),"0"})+" AS TIEMPOS " +
                ", COUNT(*)*" + bd.funcionMatematica(bd.FUNCIONMATEMATICA_AVG,new String[]{
                                            bd.convertir("CRO_FEF - "+fechaInicio,
                                                    bd.CONVERTIR_COLUMNA_NUMERO,null)}) + " AS multi";

        String sqlHistoricos = "SELECT "+select+from+where+whereTMedios+groupby;
        
        String caseExpHist = null;
        if(tiempo!=4)
            caseExpHist = "(select COUNT(DISTINCT(cro_num)) "+ from + where + " )";
        else
            caseExpHist = "(select COUNT(DISTINCT(cro_num)) "+ from + where + whereTMedios + " )";
        
        sqlExternaSelectPendHist = sqlExternaSelectPendHist1 + caseExp + caseExpHist + caseExpHistoricoWhen + sqlExternaSelectPendHist2;
        sqlHistoricos = sqlExternaSelectPendHist + " FROM (" + sqlHistoricos + ") a " + sqlExternaGroupBy + orderby;


        if(m_Log.isDebugEnabled()) m_Log.debug("sqlHistoricos: "+sqlHistoricos);
        stmt = con.createStatement();

        rsHistoricos = stmt.executeQuery(sqlHistoricos);
      }


      Vector titulos = new Vector();

      //Se recuperan los titulos para las columnas de agrupacion y datos

      for (int i=0; i<grupos.size();i++){
        String grupo = (String)grupos.elementAt(i);
        String titulo = (String) nom.get(grupo+"_pagina");
        titulos.add(titulo);
      }
      GeneralValueObject titulosVO = new GeneralValueObject();
      titulosVO.setAtributo("titulos", titulos);
      if (mostrarPendientes) {
          titulosVO.setAtributo("VerPend","true");
      }
      if (mostrarExpedientes) {
          titulosVO.setAtributo("VerVol","true");
      }
      if (mostrarHistoricos) {
        titulosVO.setAtributo("VerFin","true");
        titulosVO.setAtributo("tiempo",new Integer(tiempo));
      }
      titulosVO.setAtributo("todos",todos);
      estadisticas.add(titulosVO);

      Vector agrupacionesAnt = new Vector();

      while (seleccionarLecturas(rsPendientes,rsExpedientes,rsHistoricos,grupos)){

        Vector agrupacionesAux = new Vector();

        GeneralValueObject resultado = new GeneralValueObject();

        if (rsPendientes!=null){
          if (leerPendientes){
            String valor = rsPendientes.getString("TAREAS");
            if ((valor.length()>3)&&(valor.charAt(0)!='-'))
              valor=valor.substring(0,valor.length()-3)+"."+valor.substring(valor.length()-3);
            resultado.setAtributo("tareasPendientes",valor);
            valor = rsPendientes.getString("EXPEDIENTES");
            if ((valor.length()>3)&&(valor.charAt(0)!='-'))
              valor=valor.substring(0,valor.length()-3)+"."+valor.substring(valor.length()-3);
            resultado.setAtributo("expedientesPendientes",valor);
            valor = new Long(rsPendientes.getLong("TIEMPOS")).toString();
            if ((valor.length()>3)&&(valor.charAt(0)!='-'))
              valor=valor.substring(0,valor.length()-3)+"."+valor.substring(valor.length()-3);
            resultado.setAtributo("tiemposPendientes",valor+" d.");
          }
          else {
            resultado.setAtributo("tareasPendientes","0");
            resultado.setAtributo("expedientesPendientes","0");
            resultado.setAtributo("tiemposPendientes","0");
          }
        }


        if (rsHistoricos!=null){
          if (leerHistoricos){
            String valor = rsHistoricos.getString("TAREAS");
            if ((valor.length()>3)&&(valor.charAt(0)!='-'))
              valor=valor.substring(0,valor.length()-3)+"."+valor.substring(valor.length()-3);
            resultado.setAtributo("tareasHistoricas",valor);
            valor = rsHistoricos.getString("EXPEDIENTES");
            if ((valor.length()>3)&&(valor.charAt(0)!='-'))
              valor=valor.substring(0,valor.length()-3)+"."+valor.substring(valor.length()-3);
            resultado.setAtributo("expedientesHistoricos",valor);
            valor = new Long(rsHistoricos.getLong("TIEMPOS")).toString();
            if ((valor.length()>3)&&(valor.charAt(0)!='-'))
              valor=valor.substring(0,valor.length()-3)+"."+valor.substring(valor.length()-3);
            resultado.setAtributo("tiemposHistoricos",valor+" d.");
          }
          else {
            resultado.setAtributo("tareasHistoricas","0");
            resultado.setAtributo("expedientesHistoricos","0");
            resultado.setAtributo("tiemposHistoricos","0");
          }
        }

        if (rsExpedientes!=null){
          if (leerExpedientes){
            String valor = rsExpedientes.getString("CERRADOS");
            if ((valor.length()>3)&&(valor.charAt(0)!='-'))
              valor=valor.substring(0,valor.length()-3)+"."+valor.substring(valor.length()-3);
            resultado.setAtributo("expedientesCerrados",valor);
            valor = rsExpedientes.getString("ENTRAMITACION");
            if ((valor.length()>3)&&(valor.charAt(0)!='-'))
              valor=valor.substring(0,valor.length()-3)+"."+valor.substring(valor.length()-3);
            resultado.setAtributo("expedientesEnTramitacion",valor);
            valor = rsExpedientes.getString("TOTALES");
            if ((valor.length()>3)&&(valor.charAt(0)!='-'))
              valor=valor.substring(0,valor.length()-3)+"."+valor.substring(valor.length()-3);
            resultado.setAtributo("expedientesTotales",valor);
          }
          else {
            resultado.setAtributo("expedientesCerrados","0");
            resultado.setAtributo("expedientesEnTramitacion","0");
            resultado.setAtributo("expedientesTotales","0");
          }
        }

       Vector agrupaciones = new Vector();
       Hashtable parametros = new Hashtable();
       String agrupacionAnt = "";
       boolean todosIguales = true;

       for (int i=0; i<grupos.size(); i++){
          String grupo = (String)grupos.elementAt(i);
          String nombre = (String) nom.get(grupo+"_nombre");
          String agrupacion = null;
          if (leerExpedientes) {
              agrupacion=rsExpedientes.getString(nombre);
          }
          else if (leerPendientes) {
              agrupacion=rsPendientes.getString(nombre);
          }
          else if (leerHistoricos) {
              agrupacion=rsHistoricos.getString(nombre);
          }

          if (i<agrupacionesAnt.size()) agrupacionAnt=(String)agrupacionesAnt.elementAt(i);

          //Se guarda en agrupacionesAnt el valor antes de modificarlo
          agrupacionesAux.add(agrupacion);

          //Se almacenan los valores de los criterios de agrupacion, para crear los enlaces
          //posteriormente en la pagina
          nombre = (String) nom.get(grupo+"_param");
          String valor = null;
          if (agrupacion != null && agrupacion.indexOf("§¥")!=-1) {
            valor = agrupacion.substring(0,agrupacion.indexOf("§¥"));
          }
          else valor = "todos";
          for(int m=0;m<filtros.size();m++) {
              GeneralValueObject fVO = new GeneralValueObject();
              fVO = (GeneralValueObject) filtros.elementAt(m);
              String n = (String) fVO.getAtributo("nombre");
              String v = (String) fVO.getAtributo("valor");
              if("codArea".equals(nombre) && "are".equals(n) && "todos".equals(valor)) {
                  valor = v;
                  break;
              }
              if("codUnidadTram".equals(nombre) && "utr".equals(n) && "todos".equals(valor)) {
                  valor = v;
                  break;
              }
              if("codTipoProced".equals(nombre) && "tpr".equals(n) && "todos".equals(valor)) {
                  valor = v;
                  break;
              }
              if("codProcedimiento".equals(nombre) && "pro".equals(n) && "todos".equals(valor)) {
                  valor = v;
                  break;
              }
              if("codClasifTramite".equals(nombre) && "cls".equals(n) && "todos".equals(valor)) {
                  valor = v;
                  break;
              }
              if("codTramite".equals(nombre) && "tra".equals(n) && "todos".equals(valor)) {
                  valor = v;
                  break;
              }
          }
          parametros.put(nombre,valor);
          String t = Integer.toString(tiempo);
          parametros.put("tiempo",t);
          parametros.put("intervalo",intervalo);

          if ((todosIguales) && agrupacion != null && (agrupacion.equals(agrupacionAnt))) agrupacion="";
          else todosIguales=false;

          if (agrupacion != null && (agrupacion.equals("####"))&&(i==0)) agrupacion = "inf_Resumen";

          if (agrupacion != null && agrupacion.indexOf("§¥")!=-1)
            agrupacion = agrupacion.substring(agrupacion.indexOf("§¥")+2);

          if ( (nombre.equals("codTramite")) && agrupacion != null && (agrupacion.equals("999-")) ){
            agrupacion += "inf_PendAsign";
          }
          agrupaciones.add(agrupacion);
        }

        resultado.setAtributo("agrupaciones",agrupaciones);
        resultado.setAtributo("parametros",parametros);

        agrupacionesAnt = new Vector();
        for (int i=0; i<agrupacionesAux.size();i++)
          agrupacionesAnt.add(agrupacionesAux.elementAt(i));

        estadisticas.add(resultado);

      }

    }catch(Exception e){
      e.printStackTrace();
    }finally{
        try{
             ResourceBundle bundle = ResourceBundle.getBundle("techserver");
             String gestor = bundle.getString("CON.gestor");
             if("oracle".equalsIgnoreCase(gestor)){
                Statement st_alter;
                String alter1 = "ALTER SESSION SET NLS_COMP=BINARY";
                String alter2 = "ALTER SESSION SET NLS_SORT=SPANISH";
                st_alter = con.createStatement();
                st_alter.executeQuery(alter1);
                st_alter.executeQuery(alter2);

                st_alter.close();
            }
            
            if(stmt!=null) stmt.close();
            if(rsPendientes!=null) rsPendientes.close();
            if(rsExpedientes!=null) rsExpedientes.close();
            if(rsHistoricos!=null) rsHistoricos.close();

        }catch(SQLException e){
            e.printStackTrace();
        }        
    }


    return estadisticas;

  }

  /******************************************************************************/
  private void establecerFiltros(Vector filtros)
  {
      //**EL PRO_MUN, PRO_EJE, LENG... HABRA QUE SACARLOS DE ALGUN OTRO SITIO
    for (int i=0; i<filtros.size(); i++){
      GeneralValueObject filtro = (GeneralValueObject)filtros.elementAt(i);
      String nombre = (String)filtro.getAtributo("nombre");
      String valor = (String)filtro.getAtributo("valor");
      if (i!=0) where+="AND ";
      where+= nom.get(nombre+"_columna")+"='"+valor+"' ";
      if(m_Log.isDebugEnabled()) m_Log.debug("establecerFiltros en el DAO " + where);
    }
  }

  private void establecerGrupos(Vector grupos,AdaptadorSQLBD bd)
  {
    try{
    for (int i=0; i<grupos.size(); i++){
      String grupo = (String)grupos.elementAt(i);
      String descripcion = (String) nom.get(grupo+"_desc");
      String nombre = (String) nom.get(grupo+"_nombre");
      String columna = (String) nom.get(grupo+"_columna");
      String tabla = (String) nom.get(grupo+"_tabla");
      String prefijo = tabla.substring(tabla.lastIndexOf("_")+1);//tabla.substring(2);
      if(m_Log.isDebugEnabled()){
          m_Log.debug("descripcion : " + descripcion);
          m_Log.debug("nombre : " + nombre);
          m_Log.debug("columna : " + columna);
          m_Log.debug("tabla : " + tabla);
          m_Log.debug("prefijo : " + prefijo);
      }

      select = descripcion+" AS "+ nombre+", " + select;

      from+=tabla+", ";

      if (i!=0) where+=" AND ";
      else if (!where.equals(" WHERE ")) where+=" AND ";

      if (grupo.equals("tra")) {
          where+= " " + prefijo+"_TRA = TRA_COD AND TML_PRO = CRO_PRO ";
      } else {
          where+=prefijo+"_COD="+columna+" ";
      }

      if (!grupo.equals("utr")){
        if (grupo.equals("tra"))
          where+=" AND "+prefijo+"_LENG ='"+lenguaje+"' "+ " AND "+prefijo+"_CMP ='NOM' ";
        else where+=" AND "+prefijo+"_LENG='"+lenguaje+"' "+ " AND "+prefijo+"_CMP='NOM' ";
      }

      if (i==0){
        groupby+=" GROUP BY "+descripcion;
        orderby+=" ORDER BY "+nombre + bd.COLLATE_LATIN;
      }

      else {
        groupby+=", "+descripcion;
        orderby+=" ,"+nombre+ bd.COLLATE_LATIN;
      }

      boolean esHistorico = false;
      for (int h=0;h<historicos.length;h++) {
        if (grupo.equals(historicos[h])){
          esHistorico = true;
          break;
        }
      }
      if (esHistorico)  whereHistoricos+= " AND H."+columna+"<>A."+columna+" ";

    }

    }catch(Exception e){
      e.printStackTrace();
    }

    return;
  }


  private boolean mismoResultado(ResultSet rsPendientes,ResultSet rsExpedientes, Vector grupos)
  {
    boolean mismo = true;

    try{

      for (int i=0; i<grupos.size(); i++){
        String grupo = (String)grupos.elementAt(i);
        String nombre = (String) nom.get(grupo+"_nombre");
        String colPendientes = rsPendientes.getString(nombre);
        String colExpedientes = rsExpedientes.getString(nombre);
        if (!colPendientes.equals(colExpedientes)) mismo=false;
      }

    }catch(Exception e){
      e.printStackTrace();
    }

    return mismo;
  }


  private boolean seleccionarLecturas(ResultSet rsPendientes, ResultSet rsExpedientes, ResultSet rsHistoricos, Vector grupos)
  {
    String grupo = null;
    String nombre = null;
    String colPendientes = null;
    String colExpedientes = null;
    String colHistoricos = null;

    boolean hayPendientes = true;
    boolean hayExpedientes = true;
    boolean hayHistoricos = true;

    try{

      if (rsPendientes==null) colPendientes = "zzzzzz";
      else if (leerPendientes){
        if (hayPendientes = rsPendientes.next()) {}
        else colPendientes = "zzzzzz";
      }


      if (rsExpedientes==null) colExpedientes = "zzzzzz";
      else if (leerExpedientes){
        if (hayExpedientes = rsExpedientes.next()) {}
        else colExpedientes = "zzzzzz";
      }


      if (rsHistoricos==null) colHistoricos = "zzzzzz";
      else if (leerHistoricos){
        if (hayHistoricos = rsHistoricos.next()) {}
        else colHistoricos = "zzzzzz";
      }


      for (int i=0; i<grupos.size(); i++){
        grupo = (String)grupos.elementAt(i);
        nombre = (String) nom.get(grupo+"_nombre");

        //Para el caso en que leer sea false y el rs este vacio (intenta leer pero el cursor no apunta
        //a un resultado
        if ((rsPendientes!=null)&&(hayPendientes)) try{
          colPendientes = rsPendientes.getString(nombre);
          }catch(SQLException e) { colPendientes = "zzzzzz"; }

        if ((rsExpedientes!=null)&&(hayExpedientes)) try{
          colExpedientes = rsExpedientes.getString(nombre);
          }catch (SQLException e) { colExpedientes = "zzzzzz"; }

        if ((rsHistoricos!=null)&&(hayHistoricos)) try{
          colHistoricos = rsHistoricos.getString(nombre);
          }catch(SQLException e){ colHistoricos = "zzzzzz"; }



          //Si todos carecen de elementos para leer o no se deben mostrar
          if ( (colPendientes.equals("zzzzzz"))&&(colExpedientes.equals("zzzzzz"))&&(colHistoricos.equals("zzzzzz")) )
            return false;


        //COMIENZO DE LAS COMPARACIONES POR ORDEN ALFABETICO
        if (colPendientes.compareTo(colExpedientes)<0) {
          leerPendientes = true;
          leerExpedientes = false;

          if (colPendientes.compareTo(colHistoricos)<0)
            leerHistoricos = false;  //pendientes es previa a los otros
          else if (colPendientes.compareTo(colHistoricos)>0){
            leerHistoricos = true;
            leerPendientes = false;  //historicos es previa a los otros
          }
          else leerHistoricos = true; //pendientes e historicos son previas
        }


        else if (colExpedientes.compareTo(colPendientes)<0) {
          leerExpedientes = true;
          leerPendientes = false;

          if (colExpedientes.compareTo(colHistoricos)<0)
            leerHistoricos = false;  //expedientes es previa a los otros
          else if (colExpedientes.compareTo(colHistoricos)>0){
            leerHistoricos = true;
            leerPendientes = false;  //historicos es previa a los otros
          }
          else leerHistoricos = true; //expedientes e historicos son previas
        }

        else if (colPendientes.compareTo(colExpedientes)==0) {

          if (colPendientes.compareTo(colHistoricos)<0){
            //historicos es previa a los otros, que son iguales
            leerHistoricos = false;
            leerPendientes = true;
            leerExpedientes = true;
          }

          if (colPendientes.compareTo(colHistoricos)>0) {
            //expedientes y pendientes son previas
            leerHistoricos = true;
            leerPendientes = false;
            leerExpedientes = false;
          }

          if (colPendientes.compareTo(colHistoricos)==0){
            //todos son iguales
            leerHistoricos = true;
            leerPendientes = true;
            leerExpedientes = true;
          }

        }


        if ( ((rsPendientes!=null)&&(!leerPendientes))||
             ((rsExpedientes!=null)&&(!leerExpedientes))||
             ((rsHistoricos!=null)&&(!leerHistoricos)) )
          break;

      }


    }catch(Exception e){
      e.printStackTrace();
    }

    return ( ((rsPendientes!=null)&&(leerPendientes)&&(hayPendientes))||((rsExpedientes!=null)
        &&(leerExpedientes)&&(hayExpedientes))||((rsHistoricos!=null)&&(leerHistoricos)&&(hayHistoricos)) );

  }


  private boolean mostrarHistoricos(Vector filtros, Vector grupos)
  {
    boolean mostrar = false;
    for (int i=0; i<historicos.length; i++){
      for (int j=0; j<filtros.size(); j++){
        GeneralValueObject filtro = (GeneralValueObject)filtros.elementAt(j);
        String nomFiltro = (String)filtro.getAtributo("nombre");
        if (nomFiltro.equalsIgnoreCase(historicos[i])) return true;
      }
      for (int j=0; j<grupos.size(); j++){
        String grupo = (String)grupos.elementAt(j);
        if (grupo.equalsIgnoreCase(historicos[i])) return true;
      }
    }

    return mostrar;
  }



  private void rollBackTransaction(AdaptadorSQLBD bd,Connection con,Exception e){
    try {
      bd.rollBack(con);
    }catch (Exception e1) {
      e1.printStackTrace();
    }finally {
      e.printStackTrace();
      m_Log.error(e.getMessage());
    }
  }

  private void commitTransaction(AdaptadorSQLBD bd,Connection con){
    try{
      bd.finTransaccion(con);
      bd.devolverConexion(con);
    }catch (Exception ex) {
      ex.printStackTrace();
      m_Log.error("SQLException: " + ex.getMessage());
    }
  }



}