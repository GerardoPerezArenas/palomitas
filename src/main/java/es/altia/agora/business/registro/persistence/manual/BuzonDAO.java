package es.altia.agora.business.registro.persistence.manual;


import es.altia.agora.business.registro.BuzonValueObject;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.util.conexion.*;

import java.sql.*;

import java.text.SimpleDateFormat;

import java.util.Vector;

public class BuzonDAO {

    //instancia única
    private static BuzonDAO instance = null;
    //Para el fichero de configuracion tecnico
    protected static Config m_ConfigTechnical;
    //Para los mensajes de error localizados
    protected static Config m_ConfigError;
    //Para informacion de logs
    protected static Log m_Log =
            LogFactory.getLog(BuzonDAO.class.getName());

    //protected String sgbd;
    protected String eje;
    protected String num;
    protected String fec;
    protected String fed;
    protected String tip;
    protected String asu;
    protected String uor;
    protected String dep;
    protected String orgd;
    protected String entd;
    // tiruritata
    //protected String depd;
    protected String unid;
    protected String mod;
    // tiruritata
    //protected String dod;
    protected String uod;
    protected String ttr;
    protected String ntr;
    protected String tdo;
    protected String tpe;
    protected String ter;
    protected String dom;
    protected String tnv;
    protected String act;
    protected String est;
    protected String dil;

    //protected String uororg;
    //protected String uorent;
    protected String uornom;
    protected String uorcod;
    protected String deporg;
    protected String depent;
    protected String depnom;
    protected String depcod;
    protected String entnom;
    protected String entorg;
    protected String entcod;
    protected String orgdes;
    protected String orgcod;

    /**
     * Construye un nuevo BuzonDAO. Es protected, por lo que la unica manera de
     * instanciar esta clase es usando el factory method <code>getInstance</code>
     */
    protected BuzonDAO() {
        super();
        //Queremos usar el fichero de configuracion techserver
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        //Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");

        //sgbd = m_ConfigTechnical.getString("sgbd");//gestor usado
        eje = m_ConfigTechnical.getString("SQL.R_RES.ejercicio");
        num = m_ConfigTechnical.getString("SQL.R_RES.numeroAnotacion");
        fec = m_ConfigTechnical.getString("SQL.R_RES.fechaAnotacion");
        fed = m_ConfigTechnical.getString("SQL.R_RES.fechaDocumento");
        tip = m_ConfigTechnical.getString("SQL.R_RES.tipoReg");
        asu = m_ConfigTechnical.getString("SQL.R_RES.asunto");
        uor = m_ConfigTechnical.getString("SQL.R_RES.codUnidad");
        dep = m_ConfigTechnical.getString("SQL.R_RES.codDpto");
        orgd = m_ConfigTechnical.getString("SQL.R_RES.orgDestAnot");
        entd = m_ConfigTechnical.getString("SQL.R_RES.entidadDestino");
        // tiruritata
        //depd = m_ConfigTechnical.getString("SQL.R_RES.dptoDestino");
        unid = m_ConfigTechnical.getString("SQL.R_RES.unidRegDestino");
        mod = m_ConfigTechnical.getString("SQL.R_RES.tipoAnotacionDestino");
        // tiruritata
        //dod = m_ConfigTechnical.getString("SQL.R_RES.departOrigDest");
        uod = m_ConfigTechnical.getString("SQL.R_RES.unidOrigDest");
        ttr = m_ConfigTechnical.getString("SQL.R_RES.tipoTransporte");
        ntr = m_ConfigTechnical.getString("SQL.R_RES.numTransporte");
        tdo = m_ConfigTechnical.getString("SQL.R_RES.tipoDoc");
        tpe = m_ConfigTechnical.getString("SQL.R_RES.tipoRemitente");
        ter = m_ConfigTechnical.getString("SQL.R_RES.codTercero");
        dom = m_ConfigTechnical.getString("SQL.R_RES.domicTercero");
        tnv = m_ConfigTechnical.getString("SQL.R_RES.modifInteresado");
        act = m_ConfigTechnical.getString("SQL.R_RES.actuacion");
        est = m_ConfigTechnical.getString("SQL.R_RES.estAnot");
        dil = m_ConfigTechnical.getString("SQL.R_RES.diligencia");
//	uororg = m_ConfigTechnical.getString("SQL.A_UOR.organizacion");
//	uorent = m_ConfigTechnical.getString("SQL.A_UOR.entidad");
        uornom = m_ConfigTechnical.getString("SQL.A_UOR.nombre");
        uorcod = m_ConfigTechnical.getString("SQL.A_UOR.codigo");
        deporg = m_ConfigTechnical.getString("SQL.A_DEP.organizacion");
        depent = m_ConfigTechnical.getString("SQL.A_DEP.entidad");
        depnom = m_ConfigTechnical.getString("SQL.A_DEP.nombre");
        depcod = m_ConfigTechnical.getString("SQL.A_DEP.codigo");
        entnom = m_ConfigTechnical.getString("SQL.A_ENT.nombre");
        entorg = m_ConfigTechnical.getString("SQL.A_ENT.organizacion");
        entcod = m_ConfigTechnical.getString("SQL.A_ENT.codigo");
        orgdes = m_ConfigTechnical.getString("SQL.A_ORG.descripcion");
        orgcod = m_ConfigTechnical.getString("SQL.A_ORG.codigo");
    }

    /**
     * Método encargado de rellenar el Buzon con las ocurrencias que coincidan
     * con la organización, entidad y departamento del usuario actual
     * @return El vector con toda la información
     */
    public Vector load(int organizaciong, int entidadg, int departamentog,
                       int unidadg, String[] params){
        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("load BUZON");
        Vector buzon = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        try{
            m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            m_Log.debug("A por el Statement");
            stmt = conexion.createStatement();
            //preparo los parametros del join
            /*añado a la select el valor de organizaciong y entidadg ya que son
          las del usuario actual, por eso no hay ORG ni ENT en R_RES, pero luego
          me haran falta porque tengo que pasar su nombre al VO*/
            String partefrom = eje + "," + num + "," + fec + "," + tip + "," + asu
                    + "," + organizaciong + " as RES_ORG," + orgdes + ","
                    + entidadg + " as RES_ENT," + entnom + "," + dep + ","
                    + depnom + "," + uor + "," + uornom;
            /*busco todos los registros que me tengan como destino y esten
          pendientes de buzon RES_EST = 1*/
            String partewhere = orgd + "=" + organizaciong + " and " + entd + "=" +
                    // tiruritata
                    entidadg + /*" and " + depd + "=" + departamentog +*/
                    " and " + unid + "=" + unidadg + " and " + est +
                    "=1";
            String[] join = {"R_RES", "INNER", "A_ORG", organizaciong + "=" +
                    orgcod, "INNER", "A_ENT", organizaciong + "=" + entorg + " AND " +
                    entidadg + "=" + entcod, "INNER", "A_DEP", organizaciong + "=" +
                    deporg + " and " + entidadg + "=" + depent +	" and " + dep + "="
                    + depcod, "INNER", "A_UOR", /*organizaciong + "=" + uororg + " and " +
            entidadg + "=" + uorent + " and " +*/ uor + "=" + uorcod, "false"};
            //hago el join

            String sql = abd.join(partefrom,partewhere,join);
            if(m_Log.isDebugEnabled()) m_Log.debug("a por el join: " + sql);
            //ahora voy usar una función del OAD, preparo primero los parámetros
            String[] parametros = {fec/*ordenaré por fecha*/, "3"/*la posición
		que ocupa el parámetro anterior en la select*/};

            sql = sql + abd.orderUnion(parametros);//ya tengo la sentencia
            if(m_Log.isDebugEnabled()) m_Log.debug("a por el order: " + sql);
            ResultSet rs = stmt.executeQuery(sql);//la ejecuto
            int columnas = 13;//se q voy a tener 13 columnas pq la select la he hecho yo
            while(rs.next()){//mientras queden registros por tratar
                Date aux = null;
                int codigo = -1;
                String entrada = "";
                int ejercicio = -1;
                int numero = -1;
                String fecha = "";
                String tipo = "";
                String asunto = "";
                int organizacion = -1;
                String orgdescripcion = "";
                int entidad = -1;
                String entnombre = "";
                int departamento = -1;
                String depnombre = "";
                int unidad = -1;
                String uornombre = "";
                //ResultSet empieza en 1, no en 0.
                for(int i=1; i < columnas+1; i++){
                    if(i == 3)//numero
                        aux = rs.getDate(i);//cojo la fecha
                    else if(i == 1 || i==2 || i == 6 || i == 8 || i == 10 || i == 12)
                        codigo = rs.getInt(i);
                    else{
                        entrada = rs.getString(i);//cojo el valor de la columna i
                        if (entrada != null)//si no es nulo
                            entrada = entrada.trim();//le quito los blancos
                    }
                    switch(i){//a tratarlo
                        case 1://ejercicio
                            ejercicio = codigo;
                            break;
                        case 2://numero
                            numero = codigo;
                            break;
                        case 3://fecha
                            SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");//indico el formato que quiero
                            fecha = s.format(aux);//la formateo
                            break;
                        case 4://tipo
                            tipo = entrada;
                            break;
                        case 5://asunto
                            asunto = entrada;
                            break;
                        case 6://codigo de organizacion
                            organizacion = codigo;
                            break;
                        case 7://nombre de la organizacion
                            orgdescripcion = entrada;
                            break;
                        case 8://codigo de entidad
                            entidad = codigo;
                            break;
                        case 9://nombre de la entidad
                            entnombre = entrada;
                            break;
                        case 10://codigo de departamento
                            departamento = codigo;
                            break;
                        case 11://nombre del departamento
                            depnombre = entrada;
                            break;
                        case 12://codigo de unidad
                            unidad = codigo;
                            break;
                        case 13://nombre de la unidad
                            uornombre = entrada;
                            break;
                    }
                }
                //meto los datos en un VO
                BuzonValueObject bvo = new BuzonValueObject(ejercicio, numero,
                        fecha, tipo, asunto, organizacion, orgdescripcion, entidad,
                        entnombre, departamento, depnombre, unidad, uornombre);

                buzon.add(bvo);//lo añado al buzon*/

            }
        }
        catch(BDException bde){//
            m_Log.error("Problema técnico de JDBC " + bde.getMessage());
        }
        catch(SQLException sqle){
            m_Log.error("Problema técnico de JDBC " + sqle.getMessage());
        }
        catch(Exception e){
            m_Log.error("Otros errores" + e.getMessage());
        }
        finally{
            try{
                if (stmt!=null) stmt.close();
                abd.devolverConexion(conexion);
            }
            catch(BDException bde){
                m_Log.error("Problema técnico de JDBC " + bde.getMessage());
            }
            catch(SQLException sqle){
                m_Log.error("Problema técnico de JDBC " + sqle.getMessage());
            }
        }
        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("load REGISTRO");
        return buzon;
    }

    /**
     * Factory method para el<code>Singelton</code>.
     * @return La unica instancia de SelectListaDAO.The only CustomerDAO instance.
     */
    public static BuzonDAO getInstance() {
        //si no hay ninguna instancia de esta clase tenemos que crear una
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread)
            // Las invocaciones de este metodo
            synchronized(BuzonDAO.class) {
                if (instance == null) {
                    instance = new BuzonDAO();
                }
            }
        }
        return instance;
    }
}
