// NOMBRE DEL PAQUETE
package es.altia.agora.business.formularios.persistence.manual;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.interfaces.user.web.formularios.FichaFormularioForm;
import es.altia.catalogoformularios.model.solicitudes.vo.FormularioTramitadoVO;
import es.altia.catalogoformularios.util.exceptions.InternalErrorException;
import es.altia.catalogoformularios.util.sql.GeneralOperations;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORsDAO;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;

import java.io.IOException;
import java.net.MalformedURLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.upload.FormFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: EntidadesesDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class FichaFormularioDAO  {
  private static FichaFormularioDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(FichaFormularioDAO.class.getName());

  /*Estas variables se van a inicializar en el constructor porque dependen de la organizacion.
   Habrá que hacer dos métodos, uno que contemple la organización y otro que no: el que usa
   la organización será usado por Flexia, y el antiguo por el portal de formularios.*/
    protected static Config ConfigPlantillas = ConfigServiceHelper.getConfig("formulariosPdf");
    private static String origenPlantillas;
    private static String dirPlantillas ;
    private static String servidor;
    private static String contexto;
  
  protected FichaFormularioDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
    origenPlantillas = ConfigPlantillas.getString("origenPlantillas");
    dirPlantillas = ConfigPlantillas.getString ("directorioPlantillas");
    servidor = ConfigPlantillas.getString("servidorPlantillas");
    contexto = ConfigPlantillas.getString("contextoPlantillas");
  }

    protected FichaFormularioDAO(String organizacion) {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
    origenPlantillas = ConfigPlantillas.getString(organizacion+"/origenPlantillas");
    dirPlantillas = ConfigPlantillas.getString (organizacion+"/directorioPlantillas");
    servidor = ConfigPlantillas.getString(organizacion+"/servidorPlantillas");
    contexto = ConfigPlantillas.getString(organizacion+"/contextoPlantillas");
  }

  public static FichaFormularioDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (FichaFormularioDAO.class) {
        if (instance == null) {
          instance = new FichaFormularioDAO();
        }
      }
    }
    return instance;
  }

  public static FichaFormularioDAO getInstance(String organizacion) {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (FichaFormularioDAO.class) {
        if (instance == null) {
          instance = new FichaFormularioDAO(organizacion);
        }
      }
    }
    return instance;
  }

  public Vector eliminarFormulario(String codForm, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    String sql = "";
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      stmt = conexion.createStatement();
      sql = "DELETE FROM F_DEF_FORM WHERE " +
        campos.getString("SQL.F_DEF_FORM.codigo")+"="+ codForm;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt.executeUpdate(sql);
      sql = "DELETE FROM F_PLANTILLA2_FORM WHERE " +
        campos.getString("SQL.F_PLANTILLA2_FORM.codigo")+"="+ codForm;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt.executeUpdate(sql);
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    Vector lista = new Vector();
    lista = FormulariosDAO.getInstance().getListaFormulariosSinParametros(params);
    return lista;
  }


    public Vector modificarFormulario(GeneralValueObject gVO, String[] params){
      AdaptadorSQLBD abd = null;
      Connection conexion = null;
      Statement stmt = null;
      ResultSet rs = null;
      int resBorrar=0, resTramites=0;
      String sql = "",sqlBorrar = "";
      try{
          //m_Log.debug("A por el OAD");
          abd = new AdaptadorSQLBD(params);
          //m_Log.debug("A por la conexion");
          conexion = abd.getConnection();
          abd.inicioTransaccion(conexion);
          sql = "UPDATE F_DEF_FORM SET " +
              //campos.getString("SQL.F_DEF_FORM.codigoVisible")+"='"+ gVO.getAtributo("formCod") +"'," +
              campos.getString("SQL.F_DEF_FORM.descripcion")+"='"+ gVO.getAtributo("formNombre") +"'," +
              //campos.getString("SQL.F_DEF_FORM.version")+"="+ gVO.getAtributo("formVersion") +"," +
              campos.getString("SQL.F_DEF_FORM.fechaAlta")+"='"+ gVO.getAtributo("fechaAlta") +"'";
          if (!gVO.getAtributo("fechaBaja").equals("") && !gVO.getAtributo("fechaBaja").equals("null"))
              sql = sql +","+ campos.getString("SQL.F_DEF_FORM.fechaBaja")+"='"+ gVO.getAtributo("fechaBaja") +"',";
          else
              sql = sql +","+ campos.getString("SQL.F_DEF_FORM.fechaBaja")+"=null,";
          sql += campos.getString("SQL.F_DEF_FORM.tipo")+"='"+ gVO.getAtributo("codTipo") +"'," +
              campos.getString("SQL.F_DEF_FORM.procedimiento")+"='"+ gVO.getAtributo("codProcedimiento") +"'," +
              campos.getString("SQL.F_DEF_FORM.area")+"='"+ gVO.getAtributo("codArea") +"'," +
              campos.getString("SQL.F_DEF_FORM.visible")+"='"+ gVO.getAtributo("visible") +"'," +
              campos.getString("SQL.F_DEF_FORM.instruc")+"='"+ gVO.getAtributo("instrucciones") +"'," +
              campos.getString("SQL.F_DEF_FORM.dTramite")+"='"+ gVO.getAtributo("dTramite") +"'," +
              campos.getString("SQL.F_DEF_FORM.gTramite")+"='"+ gVO.getAtributo("gTramite") +"'," +
              campos.getString("SQL.F_DEF_FORM.cerrarT1")+"='"+ gVO.getAtributo("cerrarT1") +"'," +
              campos.getString("SQL.F_DEF_FORM.instancia")+"='"+ gVO.getAtributo("gInstancia") +"'," +
              campos.getString("SQL.F_DEF_FORM.registro")+"='"+ gVO.getAtributo("gRegistro") +"'," +
              campos.getString("SQL.F_DEF_FORM.unidades")+"='"+ gVO.getAtributo("accesible") +"'," +
              campos.getString("SQL.F_DEF_FORM.dmte")+"='"+ gVO.getAtributo("codTipoDemandante") +"',"+
              campos.getString("SQL.F_DEF_FORM.tipoFirma")+"='"+ gVO.getAtributo("codTipoFirma") +"',"+
              campos.getString("SQL.F_DEF_FORM.codRelacion")+"="+ gVO.getAtributo("codFormRel") + "," +
              "FDF_TIPO_RESTRICCION ='"+ gVO.getAtributo("tipoRestriccionFormulario") + "'";

          if (gVO.getAtributo("gRegistro").equals("1")){
               sql += ", FDF_REGISTRO_ORIGEN="+ gVO.getAtributo("codUnidadRegOrigen") + "," +
                       " FDF_REGISTRO_DESTINO="+ gVO.getAtributo("codUnidadRegDestino") + " ";
          }else {
               sql += ", FDF_REGISTRO_DESTINO=null, FDF_REGISTRO_ORIGEN=null ";
          }

              if (gVO.getAtributo("fichero")!="") sql = sql +","+ campos.getString("SQL.F_DEF_FORM.plantilla")+"=?";
          sql = sql + " WHERE " + campos.getString("SQL.F_DEF_FORM.codigo")+"="+ gVO.getAtributo("codigoInterno");

          if(m_Log.isDebugEnabled()) m_Log.debug(sql);
          PreparedStatement ps = conexion.prepareStatement(sql);
          if (gVO.getAtributo("fichero")!="") {
              FormFile fichero = (FormFile)gVO.getAtributo("fichero");
              if(m_Log.isDebugEnabled()) m_Log.debug("Fichero1 correcto");
              byte[] doc = fichero.getFileData();
              if(m_Log.isDebugEnabled()) m_Log.debug(doc);
              ps.setBinaryStream(1, fichero.getInputStream(), fichero.getFileSize());
              //ps.setBytes(1,doc);
          }
          ps.executeUpdate();
          ps.close();

          if (gVO.getAtributo("fichero2")!="") {
        	  //Hay que hacer delete e insert para asegurar el funcionamiento cuando se inntercambia entre
        	  //distintos modos de almacenar el formulario.
        	  sql="DELETE FROM F_PLANTILLA2_FORM WHERE FDF_CODIGO="+ gVO.getAtributo("codigoInterno");
        	  ps = conexion.prepareStatement(sql);
        	  ps.executeUpdate();
        	  if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        	  
        	  sql="INSERT INTO F_PLANTILLA2_FORM (FDF_CODIGO,FDF_PLANTILLA) VALUES ("+ gVO.getAtributo("codigoInterno") +",?)";

              if(m_Log.isDebugEnabled()) m_Log.debug(sql);
              ps = conexion.prepareStatement(sql);
              if (gVO.getAtributo("fichero2")!="") {
                  FormFile fichero2 = (FormFile)gVO.getAtributo("fichero2");
                  if(m_Log.isDebugEnabled()) m_Log.debug("Fichero2 correcto");
                  byte[] doc = fichero2.getFileData();
                  if(m_Log.isDebugEnabled()) m_Log.debug(doc);
                  ps.setBinaryStream(1, fichero2.getInputStream(), fichero2.getFileSize());
                  //ps.setBytes(1,doc);
              }
              ps.executeUpdate();
              ps.close();
          }

          // PESTAÑA DE TRÁMITES
          Vector listaTramProc = (Vector) gVO.getAtributo("listaTramProc");
          Vector listaTramCod = (Vector) gVO.getAtributo("listaTramCod");
          Vector listaTramRel = (Vector) gVO.getAtributo("listaTramRel");
          Vector listaTramEst = (Vector) gVO.getAtributo("listaTramEst");
          if(m_Log.isDebugEnabled()) m_Log.debug("LISTA PROC TRAMITES -> "+listaTramProc);
          if(m_Log.isDebugEnabled()) m_Log.debug("LISTA COD TRAMITES  -> "+listaTramCod);
          if(m_Log.isDebugEnabled()) m_Log.debug("LISTA REL TRAMITES  -> "+listaTramRel);
          if(m_Log.isDebugEnabled()) m_Log.debug("LISTA EST TRAMITES  -> "+listaTramEst);

          sqlBorrar = "DELETE FROM F_DEFFORM_TRA WHERE " + campos.getString("SQL.F_DEFFORM_TRA.formulario") + "=" + gVO.getAtributo("codigoInterno");
          if(m_Log.isDebugEnabled()) m_Log.debug(sqlBorrar);
          ps = conexion.prepareStatement(sqlBorrar);
          if(m_Log.isDebugEnabled()) m_Log.debug("despues del createStatement");
          resBorrar	= ps.executeUpdate();
          if(m_Log.isDebugEnabled()) m_Log.debug("despues del executeUpDate");
          if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas en el	eliminar de	F_DEFFORM_TRA son :::::::::::::: : " + resBorrar);
          ps.close();

          for(int j=0;j<listaTramProc.size();j++)	{
              sql = "INSERT	INTO F_DEFFORM_TRA (" + campos.getString("SQL.F_DEFFORM_TRA.procedimiento")	+ ","
                      +	campos.getString("SQL.F_DEFFORM_TRA.formulario") + ","
                      + campos.getString("SQL.F_DEFFORM_TRA.municipio") +	","
                      +	campos.getString("SQL.F_DEFFORM_TRA.tramite") + ","
                      +	campos.getString("SQL.F_DEFFORM_TRA.relacion") + ","
                      + campos.getString("SQL.F_DEFFORM_TRA.estado") + ") VALUES ('"
                      + listaTramProc.elementAt(j) + "'," + gVO.getAtributo("codigoInterno") + ","
                      + gVO.getAtributo("municipio") + "," + listaTramCod.elementAt(j) + ",'"
                      + listaTramRel.elementAt(j)+ "','" + listaTramEst.elementAt(j) + "')";
              if(m_Log.isDebugEnabled()) m_Log.debug(sql);
              ps = conexion.prepareStatement(sql);
              resTramites += ps.executeUpdate();
              ps.close();
          }
          if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas en el insert  de E_ENT son resCondEnt:::::::::::::: : "	+ resTramites);


          // PESTAÑA DE RESTRICCIONES
          Vector listaRestriccionesUOR = (Vector) gVO.getAtributo("listaRestriccionesUOR");
          Vector listaRestriccionesCargo = (Vector) gVO.getAtributo("listaRestriccionesCargo");
          if(m_Log.isDebugEnabled()) m_Log.debug("LISTA UOR RESTRICCIONES    -> "+listaRestriccionesUOR);
          if(m_Log.isDebugEnabled()) m_Log.debug("LISTA CARGO RESTRICCIONES  -> "+listaRestriccionesCargo);

          sqlBorrar = "DELETE FROM F_RESTRICCION WHERE " + campos.getString("SQL.F_RESTRICCIONES_FORM.formulario") + "=" + gVO.getAtributo("codigoInterno");
          if(m_Log.isDebugEnabled()) m_Log.debug(sqlBorrar);
          ps = conexion.prepareStatement(sqlBorrar);
          if(m_Log.isDebugEnabled()) m_Log.debug("despues del createStatement");
          resBorrar	= ps.executeUpdate();
          ps.close();
          if(m_Log.isDebugEnabled()) m_Log.debug("despues del executeUpDate");
          if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas en el	eliminar de	F_RESTRICCIONES_FORM son :::::::::::::: : " + resBorrar);

          for(int j=0;j<listaRestriccionesUOR.size();j++)	{
              if(m_Log.isDebugEnabled()) m_Log.debug("RESTRICCIONES --> CARGO --> "+listaRestriccionesCargo.elementAt(j));

                  sql = "INSERT	INTO F_RESTRICCION (" + campos.getString("SQL.F_RESTRICCIONES_FORM.formulario")	+ ","
                          +	campos.getString("SQL.F_RESTRICCIONES_FORM.uor") + ","
                          + campos.getString("SQL.F_RESTRICCIONES_FORM.cargo") +	") VALUES ("
                          + gVO.getAtributo("codigoInterno") + "," + listaRestriccionesUOR.elementAt(j)+ "," + listaRestriccionesCargo.elementAt(j) + ")";

                  if(m_Log.isDebugEnabled()) m_Log.debug(sql);

                  ps = conexion.prepareStatement(sql);
                  resTramites += ps.executeUpdate();
                  ps.close();
          }


      }catch (Exception e){
        rollBackTransaction(abd,conexion,e);
      }finally{
        commitTransaction(abd,conexion);
      }
      Vector lista = new Vector();
      lista = FormulariosDAO.getInstance().getListaFormulariosSinParametros(params);
      return lista;
    }

    public Vector modificarFormularioFichero(FichaFormularioForm form, String[] params){
      AdaptadorSQLBD abd = null;
      Connection conexion = null;
      //Statement stmt = null;
      //ResultSet rs = null;
      String sql = "";
      try{
          //m_Log.debug("A por el OAD");
          abd = new AdaptadorSQLBD(params);
          //m_Log.debug("A por la conexion");
          conexion = abd.getConnection();
          abd.inicioTransaccion(conexion);
          //stmt = conexion.createStatement();
          sql = "UPDATE F_DEF_FORM SET " +
              campos.getString("SQL.F_DEF_FORM.plantilla")+"=?";
          sql = sql + " WHERE " + campos.getString("SQL.F_DEF_FORM.codigo")+"="+ form.getCodigo();

          if(m_Log.isDebugEnabled()) m_Log.debug(sql);
          PreparedStatement ps = conexion.prepareStatement(sql);
          FormFile fichero = form.getFichero();
          if (fichero!=null){
              if(m_Log.isDebugEnabled()) m_Log.debug("Fichero correcto");
              byte[] doc = fichero.getFileData();
              if(m_Log.isDebugEnabled()) m_Log.debug(doc);
              //java.io.InputStream st = new java.io.ByteArrayInputStream(doc);
              //ps.setBinaryStream(1,st,doc.length);
              if(m_Log.isDebugEnabled()) m_Log.debug("AKI");
              ps.setBinaryStream(1, fichero.getInputStream(), fichero.getFileSize());
              //ps.setBytes(1, doc);
          }
            else {
              if(m_Log.isDebugEnabled()) m_Log.debug("Fichero incorrecto");
              ps.setNull(1,java.sql.Types.BLOB);
          }
          if(m_Log.isDebugEnabled()) m_Log.debug("Fichero procesado");
          ps.executeUpdate();
          //stmt = conexion.createStatement();
          //stmt.executeUpdate(sql);
          //m_Log.debug("las filas afectadas en el insert son : " + res);
      }catch (Exception e){
        rollBackTransaction(abd,conexion,e);
      }finally{
        commitTransaction(abd,conexion);
      }
      Vector lista = new Vector();
      lista = FormulariosDAO.getInstance().getListaFormulariosSinParametros(params);
      return lista;
    }

    public byte[] getFicheroFormulario(String codigo, String tipo, String[] params){
      byte[] fichero = null;
      AdaptadorSQLBD abd = null;
      Connection conexion = null;
      Statement stmt = null;
      ResultSet rs = null;
      String sql = "";
      try{
          //m_Log.debug("A por el OAD");
          abd = new AdaptadorSQLBD(params);
          //m_Log.debug("A por la conexion");
          conexion = abd.getConnection();
          abd.inicioTransaccion(conexion);
          //stmt = conexion.createStatement();
          if(m_Log.isDebugEnabled()) m_Log.debug("TIPO........"+tipo);
          String tabla="";
          if (tipo.equals("p2")) {
            tabla="F_PLANTILLA2_FORM";
          }
          else {
            tabla="F_DEF_FORM";
          }
          sql = "SELECT " + campos.getString("SQL.F_DEF_FORM.plantilla")+" FROM "+tabla+" WHERE " + campos.getString("SQL.F_DEF_FORM.codigo")+"="+ codigo;

          if(m_Log.isDebugEnabled()) m_Log.debug(sql);
          stmt = conexion.createStatement();
          rs = stmt.executeQuery(sql);
          while (rs.next()){
              InputStream st = rs.getBinaryStream(campos.getString("SQL.F_DEF_FORM.plantilla"));
              ByteArrayOutputStream ot = new ByteArrayOutputStream();
              int c;
              while ((c = st.read())!= -1){
                ot.write(c);
              }
              ot.flush();
              fichero = ot.toByteArray();
          }
      }catch (NullPointerException n){
          /*Capturamos al excepción de fichero nulo para lanzar un mensaje de alerta al usuario.*/
          m_Log.debug("------Excepción de nulo en BD. No existe el fichero.-------");
          return null;
      }catch (Exception e){
        rollBackTransaction(abd,conexion,e);
      }finally{
        commitTransaction(abd,conexion);
      }
        return fichero;
    }

    public String validarVisualizacionFichero(String codigo, String tipo, String[] params) throws MalformedURLException, IOException{
    
        if (origenPlantillas.equals("BD")){
            if (getFicheroFormulario(codigo,tipo,params)!=null) return "noNulo";
            else return "nulo";
        }
        else if (origenPlantillas.equals("DIRECTORIO")){
        String nombre = codigo + "_";
        if (tipo.equals("p1")) nombre+="edicion.pdf"; 
        else nombre+="impresion.pdf";
        
        try{
            URL dir = new URL(servidor+contexto+dirPlantillas+nombre);
            URLConnection urlConnection = dir.openConnection();
            InputStream iStream = urlConnection.getInputStream();
            return "noNulo";
        }
        catch(FileNotFoundException e){
            return "nulo";
        }
        }
        return "nulo";
    }
            
  public int proximoElemento(String[] params){
      AdaptadorSQLBD abd = null;
      Connection conexion = null;
      Statement stmt = null;
      ResultSet rs = null;
      String sql = "";
      int resultado=1;
      try{
        abd = new AdaptadorSQLBD(params);
        conexion = abd.getConnection();
        abd.inicioTransaccion(conexion);
        sql = "SELECT FDF_CODIGO FROM F_DEF_FORM ORDER BY FDF_CODIGO DESC";
        stmt = conexion.createStatement();
        rs = stmt.executeQuery(sql);
        if (rs.next()) resultado=rs.getInt(campos.getString("SQL.F_DEF_FORM.codigo"))+1;
      }catch (Exception e){
        rollBackTransaction(abd,conexion,e);
      }finally{
        commitTransaction(abd,conexion);
      }
    return resultado;
  }

    public GeneralValueObject cargarFormulario(String codForm, String[] params){
      AdaptadorSQLBD abd = null;
      Connection conexion = null;
      Statement stmt = null;
      ResultSet rs = null;
      String sql = "";
      GeneralValueObject gVO= new GeneralValueObject();
      try{
        //m_Log.debug("A por el OAD");
        abd = new AdaptadorSQLBD(params);
        //m_Log.debug("A por la conexion");
        conexion = abd.getConnection();
        abd.inicioTransaccion(conexion);
        sql = "SELECT " + campos.getString("SQL.F_DEF_FORM.codigo") +
                "," + campos.getString("SQL.F_DEF_FORM.codigoVisible") +
                "," + campos.getString("SQL.F_DEF_FORM.descripcion") +
                "," + campos.getString("SQL.F_DEF_FORM.version") +
                "," + campos.getString("SQL.F_DEF_FORM.fechaAlta") +
                "," + campos.getString("SQL.F_DEF_FORM.fechaBaja") +
                "," + campos.getString("SQL.F_DEF_FORM.tipo") +
                "," + campos.getString("SQL.F_DEF_FORM.procedimiento") +
                "," + campos.getString("SQL.F_DEF_FORM.area") +
                "," + campos.getString("SQL.F_DEF_FORM.visible") +
                "," + campos.getString("SQL.F_DEF_FORM.instruc") +
                "," + campos.getString("SQL.F_DEF_FORM.dTramite") +
                "," + campos.getString("SQL.F_DEF_FORM.gTramite") +
                "," + campos.getString("SQL.F_DEF_FORM.cerrarT1") +
                "," + campos.getString("SQL.F_DEF_FORM.instancia") +
                "," + campos.getString("SQL.F_DEF_FORM.registro") +
                "," + campos.getString("SQL.F_DEF_FORM.unidades") +
                "," + campos.getString("SQL.F_DEF_FORM.dmte") +
                "," + campos.getString("SQL.F_DEF_FORM.tipoFirma") +
                "," + campos.getString("SQL.F_DEF_FORM.codRelacion") +
                "," + campos.getString("SQL.F_DEF_FORM.plantilla") +
                "," + campos.getString("SQL.F_DEF_FORM.cerrarT1") +
                "," + campos.getString("SQL.F_DEF_FORM.tipoFirma") +
                ",FDF_TIPO_RESTRICCION, FDF_REGISTRO_ORIGEN, FDF_REGISTRO_DESTINO "  +
                " FROM F_DEF_FORM WHERE "+campos.getString("SQL.F_DEF_FORM.codigo")+"="+codForm;
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt = conexion.createStatement();
        rs = stmt.executeQuery(sql);
        if (rs.next()){
            gVO.setAtributo("codigo",rs.getString(campos.getString("SQL.F_DEF_FORM.codigo")));
            gVO.setAtributo("codVisible",rs.getString(campos.getString("SQL.F_DEF_FORM.codigoVisible")));
            gVO.setAtributo("nombre",rs.getString(campos.getString("SQL.F_DEF_FORM.descripcion")));
            gVO.setAtributo("version",rs.getString(campos.getString("SQL.F_DEF_FORM.version")));
            gVO.setAtributo("fAlta",rs.getDate(campos.getString("SQL.F_DEF_FORM.fechaAlta")));
            gVO.setAtributo("fBaja",rs.getDate(campos.getString("SQL.F_DEF_FORM.fechaBaja")));
            gVO.setAtributo("tipo",rs.getString(campos.getString("SQL.F_DEF_FORM.tipo")));
            gVO.setAtributo("procedimiento",rs.getString(campos.getString("SQL.F_DEF_FORM.procedimiento")));
            gVO.setAtributo("area",rs.getString(campos.getString("SQL.F_DEF_FORM.area")));
            gVO.setAtributo("visible",rs.getString(campos.getString("SQL.F_DEF_FORM.visible")));
            gVO.setAtributo("instruc",rs.getString(campos.getString("SQL.F_DEF_FORM.instruc")));
            gVO.setAtributo("dTramite",rs.getString(campos.getString("SQL.F_DEF_FORM.dTramite")));
            gVO.setAtributo("gTramite",rs.getString(campos.getString("SQL.F_DEF_FORM.gTramite")));
            gVO.setAtributo("cerrarT1",rs.getString(campos.getString("SQL.F_DEF_FORM.cerrarT1")));
            gVO.setAtributo("instancia",rs.getString(campos.getString("SQL.F_DEF_FORM.instancia")));
            gVO.setAtributo("registro",rs.getString(campos.getString("SQL.F_DEF_FORM.registro")));
            gVO.setAtributo("unidades",rs.getString(campos.getString("SQL.F_DEF_FORM.unidades")));
            gVO.setAtributo("dmte",rs.getString(campos.getString("SQL.F_DEF_FORM.dmte")));
            gVO.setAtributo("tipoFirma",rs.getString(campos.getString("SQL.F_DEF_FORM.tipoFirma")));
            gVO.setAtributo("codRel",rs.getString(campos.getString("SQL.F_DEF_FORM.codRelacion")));
            gVO.setAtributo("cerrarT1",rs.getString(campos.getString("SQL.F_DEF_FORM.cerrarT1")));
            gVO.setAtributo("tipoFirma",rs.getString(campos.getString("SQL.F_DEF_FORM.tipoFirma")));
            gVO.setAtributo("tipoRestriccionFormulario", rs.getString("FDF_TIPO_RESTRICCION"));
            gVO.setAtributo("unidadRegOrigen", rs.getString("FDF_REGISTRO_ORIGEN"));
            gVO.setAtributo("unidadRegDestino", rs.getString("FDF_REGISTRO_DESTINO"));
    
            m_Log.debug("Se ha cargado el formulario "+gVO);

            //if(m_Log.isDebugEnabled()) m_Log.debug("FICHERO");
            //byte[] aux = this.getFicheroFormulario(codForm, params);
            //if(m_Log.isDebugEnabled()) m_Log.debug("FICHERO OK -> "+aux);

            //String path = guardaBlobEnFicheroTemporal(rs.getBinaryStream(campos.getString("SQL.F_DEF_FORM.plantilla")));
            //if(m_Log.isDebugEnabled()) m_Log.debug("PATH -> "+path);
            //gVO.setAtributo("plantilla",path);
            gVO.setAtributo("plantilla","");
            //if(m_Log.isDebugEnabled()) m_Log.debug("PATH OK");
        }
        String area = (String) gVO.getAtributo("area");
        if (area==null || area.equals("")) {
           sql = "SELECT " + campos.getString("SQL.E_PRO.area") + " FROM E_PRO WHERE " +
                    campos.getString("SQL.E_PRO.codigoProc") + "='" + (String)gVO.getAtributo("procedimiento") + "'";
           if(m_Log.isDebugEnabled()) m_Log.debug(sql);
           rs.close();
           stmt.close();
           stmt = conexion.createStatement();
           rs = stmt.executeQuery(sql);
           if (rs.next()){
               gVO.setAtributo("area", rs.getString(campos.getString("SQL.E_PRO.area")));
           }
        }

        // obtenemos los codigos visibles de las unidades organicas de registro origen y destino
        UORsDAO uorsDao = UORsDAO.getInstance();       
        if ((String)gVO.getAtributo("unidadRegOrigen") != null && !gVO.getAtributo("unidadRegOrigen").equals("")){
            Vector listaUorOrigen = uorsDao.getListaUORsPorCodigo(Integer.valueOf((String)gVO.getAtributo("unidadRegOrigen")), params);

            if (listaUorOrigen!=null && listaUorOrigen.size()==1){
                UORDTO uorOrigen = (UORDTO)listaUorOrigen.firstElement();
                gVO.setAtributo("codVisRegOrigen", uorOrigen.getUor_cod_vis());
                gVO.setAtributo("descVisRegOrigen", uorOrigen.getUor_nom());
            }
        }
        if (gVO.getAtributo("unidadRegDestino") != null && !gVO.getAtributo("unidadRegDestino").equals("")){
            Vector listaUorDestino = uorsDao.getListaUORsPorCodigo(Integer.valueOf((String)gVO.getAtributo("unidadRegDestino")), params);

            if (listaUorDestino!=null && listaUorDestino.size()==1){
                UORDTO uorDestino = (UORDTO)listaUorDestino.firstElement();
                gVO.setAtributo("codVisRegDestino", uorDestino.getUor_cod_vis());
                gVO.setAtributo("descVisRegDestino", uorDestino.getUor_nom());
            }
        }

        //m_Log.debug("las filas afectadas en el insert son : " + res);
      }catch (Exception e){
        rollBackTransaction(abd,conexion,e);
      }finally{
        commitTransaction(abd,conexion);
      }
      return gVO;
    }

    public Vector cargarAreasPorDefecto(String[] params){
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        Vector resultados = new Vector();
        try{
            //m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            //m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            sql = "SELECT " + campos.getString("SQL.E_PRO.area") + "," + campos.getString("SQL.E_PRO.codigoProc") + " FROM E_PRO";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()){
                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo("area", rs.getString(campos.getString("SQL.E_PRO.area")));
                gVO.setAtributo("procedimiento", rs.getString(campos.getString("SQL.E_PRO.codigoProc")));
                resultados.add(gVO);
            }
        }catch (Exception e){
            rollBackTransaction(abd,conexion,e);
        }finally{
            commitTransaction(abd,conexion);
        }
        return resultados;
    }

    public Vector cargarTramitesFormulario(String codForm, int municipio, String[] params){
      Vector resultado = new Vector();
      AdaptadorSQLBD abd = null;
      Connection conexion = null;
      Statement stmt = null;
      ResultSet rs = null;
      String sql = "";
      try{
            //m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            //m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            // Creamos la select con los parametros adecuados.
            sql = "SELECT " + campos.getString("SQL.F_DEFFORM_TRA.formulario") +
                    "," + campos.getString("SQL.F_DEFFORM_TRA.procedimiento") +
                    "," + campos.getString("SQL.F_DEFFORM_TRA.municipio") +
                    "," + campos.getString("SQL.F_DEFFORM_TRA.tramite") +
                    "," + campos.getString("SQL.E_TML.valor") +
                    "," + campos.getString("SQL.F_DEFFORM_TRA.relacion") +
                    "," + campos.getString("SQL.F_DEFFORM_TRA.estado") +
                    " FROM F_DEFFORM_TRA, E_TML WHERE " + campos.getString("SQL.F_DEFFORM_TRA.tramite") + "=" + campos.getString("SQL.E_TML.codTramite")
                    + " AND " + campos.getString("SQL.F_DEFFORM_TRA.procedimiento") + "=" + campos.getString("SQL.E_TML.codProcedimiento")
                    + " AND " + campos.getString("SQL.F_DEFFORM_TRA.municipio") + "=" + campos.getString("SQL.E_TML.codMunicipio")
                    + " AND " + campos.getString("SQL.E_TML.codCampoML") +"='NOM'"
                    + " AND " + campos.getString("SQL.E_TML.idioma") +"='"+campos.getString("idiomaDefecto")+"'"
                    + " AND " + campos.getString("SQL.E_TML.codMunicipio") +"=" + municipio
                    + " AND "+ campos.getString("SQL.F_DEFFORM_TRA.formulario")+"="+codForm;

            String[] orden = {campos.getString("SQL.F_DEFFORM_TRA.procedimiento"),"1"};
            sql += abd.orderUnion(orden);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
              GeneralValueObject gVO = new GeneralValueObject();
              gVO.setAtributo("formulario",rs.getString(campos.getString("SQL.F_DEFFORM_TRA.formulario")));
              gVO.setAtributo("procedimiento",rs.getString(campos.getString("SQL.F_DEFFORM_TRA.procedimiento")));
              gVO.setAtributo("tramite",rs.getString(campos.getString("SQL.E_TML.valor")));
              gVO.setAtributo("codTramite",rs.getString(campos.getString("SQL.F_DEFFORM_TRA.tramite")));
              gVO.setAtributo("relacion",rs.getString(campos.getString("SQL.F_DEFFORM_TRA.relacion")));
              gVO.setAtributo("estado",rs.getString(campos.getString("SQL.F_DEFFORM_TRA.estado")));
              resultado.add(gVO);
            }
      }catch (Exception e){
        rollBackTransaction(abd,conexion,e);
      }finally{
        commitTransaction(abd,conexion);
      }
      return resultado;
    }

    public Vector cargarRestriccionesFormulario(String codForm, String[] params){
      Vector resultado = new Vector();
      AdaptadorSQLBD abd = null;
      Connection conexion = null;
      Statement stmt = null;
      ResultSet rs = null;
      String sql = "";
      try{
            //m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            //m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            // Creamos la select con los parametros adecuados.
            sql = "SELECT " + campos.getString("SQL.F_RESTRICCIONES_FORM.uor") + "," + campos.getString("SQL.A_UOR.nombre") +
                    "," + campos.getString("SQL.A_UOR.codigoVisible") + "," + campos.getString("SQL.F_RESTRICCIONES_FORM.cargo") +
                    "," + campos.getString("SQL.A_CAR.nombre") + "," + campos.getString("SQL.A_CAR.codigoVisible") +
                    "," + campos.getString("SQL.F_RESTRICCIONES_FORM.formulario") +
                    " FROM F_RESTRICCION,A_UOR,A_CAR WHERE " +
                    campos.getString("SQL.F_RESTRICCIONES_FORM.uor") + "="  + campos.getString("SQL.A_UOR.codigo") + " AND " +
                    campos.getString("SQL.F_RESTRICCIONES_FORM.cargo") + "="  + campos.getString("SQL.A_CAR.codigo") + " AND " +
                    campos.getString("SQL.F_RESTRICCIONES_FORM.formulario") + "=" + codForm;

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
              GeneralValueObject gVO = new GeneralValueObject();
              gVO.setAtributo("formulario",rs.getString(campos.getString("SQL.F_RESTRICCIONES_FORM.formulario")));
              gVO.setAtributo("cod_uor",rs.getString(campos.getString("SQL.F_RESTRICCIONES_FORM.uor")));
              gVO.setAtributo("cod_visible_uor",rs.getString(campos.getString("SQL.A_UOR.codigoVisible")));
              gVO.setAtributo("desc_uor",rs.getString(campos.getString("SQL.A_UOR.nombre")));
              gVO.setAtributo("cod_cargo",rs.getString(campos.getString("SQL.F_RESTRICCIONES_FORM.cargo")));
              gVO.setAtributo("cod_visible_cargo",rs.getString(campos.getString("SQL.A_CAR.codigoVisible")));
              gVO.setAtributo("desc_cargo",rs.getString(campos.getString("SQL.A_CAR.nombre")));
              resultado.add(gVO);
            }
      }catch (Exception e){
        rollBackTransaction(abd,conexion,e);
      }finally{
        commitTransaction(abd,conexion);
      }
      return resultado;
    }

    public static String guardaBlobEnFicheroTemporal(InputStream in) throws Exception
    {
       // Dejamos al sistema operativo que decida en qué directorio dejar el fichero temporal
      if (in != null) {
          File f = File.createTempFile("forms_",".pdf");
          FileOutputStream out = new FileOutputStream(f.getAbsolutePath());
          int c = 0;
          while ( (c = in.read()) >= 0 )
                out.write(c);
          out.flush();
          out.close();
          return f.getAbsolutePath();
      }
      else
          return "";
    }

    public Vector altaFormulario(GeneralValueObject gVO, String[] params){
      AdaptadorSQLBD abd = null;
      Connection conexion = null;
      Statement stmt = null;
      ResultSet rs = null;
      int resBorrar =0, resTramites=0;
      String sql = "",sqlBorrar = "";
      int cod =0;
      try{
        //m_Log.debug("A por el OAD");
        abd = new AdaptadorSQLBD(params);
        //m_Log.debug("A por la conexion");
        conexion = abd.getConnection();
        abd.inicioTransaccion(conexion);
        cod= proximoElemento(params);
        sql = "INSERT INTO F_DEF_FORM("+
            campos.getString("SQL.F_DEF_FORM.codigo")+","+
            campos.getString("SQL.F_DEF_FORM.codigoVisible")+","+
            campos.getString("SQL.F_DEF_FORM.descripcion")+","+
            campos.getString("SQL.F_DEF_FORM.version")+","+
            campos.getString("SQL.F_DEF_FORM.fechaAlta")+",";
            if (!gVO.getAtributo("fechaBaja").equals("") && !gVO.getAtributo("fechaBaja").equals("null")) sql = sql + campos.getString("SQL.F_DEF_FORM.fechaBaja")+",";
            sql = sql + campos.getString("SQL.F_DEF_FORM.tipo")+","+
            campos.getString("SQL.F_DEF_FORM.procedimiento")+","+
            campos.getString("SQL.F_DEF_FORM.area")+","+
            campos.getString("SQL.F_DEF_FORM.visible")+","+
            campos.getString("SQL.F_DEF_FORM.instruc")+","+
            campos.getString("SQL.F_DEF_FORM.dTramite")+","+
            campos.getString("SQL.F_DEF_FORM.gTramite")+","+
            campos.getString("SQL.F_DEF_FORM.cerrarT1")+","+
            campos.getString("SQL.F_DEF_FORM.instancia")+","+
            campos.getString("SQL.F_DEF_FORM.registro")+","+
            campos.getString("SQL.F_DEF_FORM.unidades")+","+
            campos.getString("SQL.F_DEF_FORM.dmte")+ "," +
            campos.getString("SQL.F_DEF_FORM.tipoFirma")+ "," +
            campos.getString("SQL.F_DEF_FORM.codRelacion")+ "," +
            campos.getString("SQL.F_DEF_FORM.plantilla") + "," +
            "FDF_TIPO_RESTRICCION, FDF_REGISTRO_ORIGEN, FDF_REGISTRO_DESTINO ";
        sql = sql + ") VALUES ("+cod+",'" +
            gVO.getAtributo("formCod") +"','" +
            gVO.getAtributo("formNombre") +"'," +
            gVO.getAtributo("formVersion") +"," +
            abd.convertir("'"+((String)gVO.getAtributo("fechaAlta"))+"'", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+",'";
        if (!gVO.getAtributo("fechaBaja").equals("") && !gVO.getAtributo("fechaBaja").equals("null")) sql = sql + gVO.getAtributo("fechaBaja") +"','";
        sql = sql + gVO.getAtributo("codTipo") +"','" +
            gVO.getAtributo("codProcedimiento") +"','" +
            gVO.getAtributo("codArea") +"','" +
            gVO.getAtributo("visible") +"','" +
            gVO.getAtributo("instrucciones") +"','" +
            gVO.getAtributo("dTramite") +"','" +
            gVO.getAtributo("gTramite") +"','" +
            gVO.getAtributo("cerrarT1") +"','" +
            gVO.getAtributo("gInstancia") +"','" +
            gVO.getAtributo("gRegistro") +"','" +
            gVO.getAtributo("accesible") +"','" +
            gVO.getAtributo("codTipoDemandante")+"','" + //DMTE
            gVO.getAtributo("codTipoFirma")+"'," + //TIPO FIRMA
            gVO.getAtributo("codFormRel") +"," + "?,'" +
            gVO.getAtributo("tipoRestriccionFormulario") + "'";

         // comprobamos si el  formulario genera registro, en este caso debemos grabar la unidades de registro origen y destino seleccionadas
         if (gVO.getAtributo("gRegistro").equals("1")){
               sql += "," + gVO.getAtributo("codUnidadRegOrigen") + ", " + gVO.getAtributo("codUnidadRegDestino") + " ";
          }else {
               sql += ",null, null ";
          }

        sql = sql +")";
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        PreparedStatement ps = conexion.prepareStatement(sql);
        m_Log.debug ("----> COMPROBACION DE FICHERO = " + gVO.getAtributo("fichero").equals(""));
        if (gVO.getAtributo("fichero")!=null && !gVO.getAtributo("fichero").equals("")){
            if(m_Log.isDebugEnabled()) m_Log.debug("Fichero correcto");
            FormFile fichero = (FormFile)gVO.getAtributo("fichero");
            byte[] doc = fichero.getFileData();
            if(m_Log.isDebugEnabled()) m_Log.debug(doc);
            ps.setBinaryStream(1, fichero.getInputStream(), fichero.getFileSize());
            //ps.setBytes(1,doc);
        }
          else {
            if(m_Log.isDebugEnabled()) m_Log.debug("Fichero incorrecto");
            ps.setNull(1,java.sql.Types.LONGVARBINARY);
        }


        ps.executeUpdate();

        ps.close();

        // INI INSERCCIÓN DE LA PLANTILLA 2
        sql = "INSERT INTO F_PLANTILLA2_FORM("+
              campos.getString("SQL.F_PLANTILLA2_FORM.codigo")+","+
              campos.getString("SQL.F_PLANTILLA2_FORM.plantilla");
        sql = sql + ") VALUES ("+cod+",?)";        
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        ps = conexion.prepareStatement(sql);
        if (gVO.getAtributo("fichero2")!=null && !gVO.getAtributo("fichero2").equals("")){
            if(m_Log.isDebugEnabled()) m_Log.debug("Fichero correcto");
            FormFile fichero2 = (FormFile)gVO.getAtributo("fichero2");
            byte[] doc = fichero2.getFileData();
            if(m_Log.isDebugEnabled()) m_Log.debug(doc);
            ps.setBinaryStream(1, fichero2.getInputStream(), fichero2.getFileSize());
            //ps.setBytes(1,doc);
        }
          else {
            if(m_Log.isDebugEnabled()) m_Log.debug("Fichero incorrecto");
            ps.setNull(1,java.sql.Types.LONGVARBINARY);
        }
        ps.executeUpdate();
        // FIN INSERCCIÓN DE LA PLANTILLA 2

        ps.close();

      // PESTAÑA DE TRÁMITES
      Vector listaTramProc = (Vector) gVO.getAtributo("listaTramProc");
      Vector listaTramCod = (Vector) gVO.getAtributo("listaTramCod");
      Vector listaTramRel = (Vector) gVO.getAtributo("listaTramRel");
      Vector listaTramEst = (Vector) gVO.getAtributo("listaTramEst");
      if(m_Log.isDebugEnabled()) m_Log.debug("LISTA PROC TRAMITES -> "+listaTramProc);
      if(m_Log.isDebugEnabled()) m_Log.debug("LISTA COD TRAMITES  -> "+listaTramCod);
      if(m_Log.isDebugEnabled()) m_Log.debug("LISTA REL TRAMITES  -> "+listaTramRel);
      if(m_Log.isDebugEnabled()) m_Log.debug("LISTA EST TRAMITES  -> "+listaTramEst);

      for(int j=0;j<listaTramProc.size();j++)	{
          sql = "INSERT	INTO F_DEFFORM_TRA (" + campos.getString("SQL.F_DEFFORM_TRA.procedimiento")	+ ","
                  +	campos.getString("SQL.F_DEFFORM_TRA.formulario") + ","
                  + campos.getString("SQL.F_DEFFORM_TRA.municipio") +	","
                  +	campos.getString("SQL.F_DEFFORM_TRA.tramite") + ","
                  +	campos.getString("SQL.F_DEFFORM_TRA.relacion") + ","
                  + campos.getString("SQL.F_DEFFORM_TRA.estado") + ") VALUES ('"
                  + listaTramProc.elementAt(j) + "'," + cod + ","
                  + gVO.getAtributo("municipio") + "," + listaTramCod.elementAt(j) + ",'"
                  + listaTramRel.elementAt(j)+ "','" + listaTramEst.elementAt(j) + "')";

        if(m_Log.isDebugEnabled()) m_Log.debug(sql);

        stmt = conexion.createStatement();
        resTramites += stmt.executeUpdate(sql);
        stmt.close();
      }
      if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas en el insert  de E_ENT son resCondEnt:::::::::::::: : "	+ resTramites);

      // PESTAÑA DE RESTRICCIONES
      Vector listaRestriccionesUOR = (Vector) gVO.getAtributo("listaRestriccionesUOR");
      Vector listaRestriccionesCargo = (Vector) gVO.getAtributo("listaRestriccionesCargo");
      if(m_Log.isDebugEnabled()) m_Log.debug("LISTA UOR RESTRICCIONES    -> "+listaRestriccionesUOR);
      if(m_Log.isDebugEnabled()) m_Log.debug("LISTA CARGO RESTRICCIONES  -> "+listaRestriccionesCargo);

      for(int j=0;j<listaRestriccionesUOR.size();j++)	{
          if(m_Log.isDebugEnabled()) m_Log.debug("RESTRICCIONES --> CARGO --> "+listaRestriccionesCargo.elementAt(j));
              sql = "INSERT	INTO F_RESTRICCION (" + campos.getString("SQL.F_RESTRICCIONES_FORM.formulario")	+ ","
                      +	campos.getString("SQL.F_RESTRICCIONES_FORM.uor") + ","
                      + campos.getString("SQL.F_RESTRICCIONES_FORM.cargo") +	") VALUES ("
                      + cod + "," + listaRestriccionesUOR.elementAt(j)+ "," + listaRestriccionesCargo.elementAt(j) + ")";
              if(m_Log.isDebugEnabled()) m_Log.debug(sql);

              stmt = conexion.createStatement();
              resTramites += stmt.executeUpdate(sql);
              stmt.close();

      }


      }catch (Exception e){
        rollBackTransaction(abd,conexion,e);
      }finally{
        commitTransaction(abd,conexion);
      }
      Vector lista = new Vector();
      lista = FormulariosDAO.getInstance().getListaFormulariosSinParametros(params);
      return lista;
    }
    
    
    
/*
 * Accedemos al fichero "interfazFormulariosXML.properties" para consultar las posibles precargas
 * de los formularios.
 * */    
public Vector getListaPrecargas(){
	Vector lista = new Vector();
	
	Config m_Conf = ConfigServiceHelper.getConfig("interfazFormulariosXML");
	String listadoPrecargas = m_Conf.getString("XML.listado");

    StringTokenizer tokenizer = new StringTokenizer(listadoPrecargas,",");
    String token = "";
	m_Log.debug ("Lista de precargas de datos XML **************************");

	while (tokenizer.hasMoreTokens()) {
        GeneralValueObject gVO = new GeneralValueObject();
		token = tokenizer.nextToken();
		gVO.setAtributo("descripcion", token);
		lista.add(gVO);
		}

	return lista;
}

public Vector getPrecargasFormulario(String codForm, String[] params){
	
	Vector lista = new Vector();
	AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
		conexion=abd.getConnection();
		String sql="SELECT FPX_IDENTIFICADOR FROM F_PRECARGA_XML WHERE FPX_FORM="+codForm;
		ps = conexion.prepareStatement(sql);
		rs = ps.executeQuery();
        if(m_Log.isDebugEnabled()) m_Log.debug("getPrecargasFormulario"+sql);
		
		
		while (rs.next()){
			GeneralValueObject gVO = new GeneralValueObject();
			gVO.setAtributo("descripcion", rs.getString("FPX_IDENTIFICADOR"));
			lista.add(gVO);
		}
		
	} catch (BDException e) {
		rollBackTransaction(abd,conexion,e);
	} catch (SQLException e) {
		rollBackTransaction(abd,conexion,e);
	}finally{
    commitTransaction(abd,conexion);
	}
	return lista;
}    

public int setPrecargasFormulario(String codForm, Vector precargas, String[] params){
	
	AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
    Connection conexion = null;
    PreparedStatement ps = null;
    int resultado=0;
    int secuencia=0;

		try {
			conexion=abd.getConnection();
			
			//Borramos las asociaciones en BBD
			eliminarPrecargas(codForm,params);
	        
	        
	        //Grabamos los datos cargados del formulario
	        String sql="INSERT INTO F_PRECARGA_XML (FPX_COD,FPX_FORM,FPX_IDENTIFICADOR) VALUES (?,"+ codForm +",?)";
	        
	        for(int i=0;i<precargas.size();i++){
	        	secuencia=calcularNuevaSecuencia(conexion);
		        if(m_Log.isDebugEnabled()) m_Log.debug("setPrecargasFormulario: "+sql);
		        ps = conexion.prepareStatement(sql);
		        ps.setInt(1, secuencia);
		        ps.setString(2, precargas.elementAt(i).toString());
		        ps.executeUpdate();
	        }
	        
		} catch (BDException e) {
			resultado=-1;
			m_Log.debug("Error en la conexión a la base de datos.");
    		e.printStackTrace();
        } catch (SQLException e) {
        	resultado=-1;
			m_Log.debug("Error en la consulta a la base de datos.");
			e.printStackTrace();
		} catch (InternalErrorException e) {
			resultado=-1;
			m_Log.debug("Error en la consulta a la base de datos.");
			e.printStackTrace();
		}finally{
            commitTransaction(abd,conexion);
        }
        return resultado;
}



public int eliminarPrecargas(String codForm, String[] params){
	
	AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
    Connection conexion = null;
    PreparedStatement ps = null;
    int resultado=0;
    
    try {
			conexion=abd.getConnection();
			
			//Borramos las asociaciones en BBD
			String sql="DELETE FROM F_PRECARGA_XML WHERE FPX_FORM="+codForm;
			ps = conexion.prepareStatement(sql);
			ps.executeUpdate();
	        if(m_Log.isDebugEnabled()) m_Log.debug("eliminarPrecargas: "+sql);
	        
		} catch (BDException e) {
			resultado=-1;
			m_Log.debug("Error en la conexión a la base de datos.");
    		e.printStackTrace();
        } catch (SQLException e) {
        	resultado=-1;
			m_Log.debug("Error en la consulta a la base de datos.");
			e.printStackTrace();
		}finally{
            commitTransaction(abd,conexion);
        }
        return resultado;
}    




private int calcularNuevaSecuencia(Connection con) throws InternalErrorException{

PreparedStatement preparedStatement = null;
ResultSet rs = null;
try{

    String sql = "SELECT MAX(FPX_COD) FROM  F_PRECARGA_XML";
    preparedStatement = con.prepareStatement(sql);

    if(m_Log.isDebugEnabled()) m_Log.debug("calcularNuevaSecuencia de precargas: " + sql);

    rs = preparedStatement.executeQuery();

    int sec = 0;
    if (rs.next()) {
        sec = rs.getInt(1);
    }else{
        throw new InternalErrorException("No se ha podido calcular la nueva secuencia");
    }
    
    return (sec+1);

	} catch (SQLException e) {
		throw new InternalErrorException(e);
	}
}




public String getCodNoVisible(String codForm, String[] params){
	
	String resultado="";
	AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
		conexion=abd.getConnection();
		String sql="SELECT FDF_CODIGO FROM F_DEF_FORM WHERE FDF_COD_VIS='"+codForm+"'";
		ps = conexion.prepareStatement(sql);
		rs = ps.executeQuery();
        if(m_Log.isDebugEnabled()) m_Log.debug("getCodNoVisible"+sql);
		
		
		if (rs.next()){
			resultado=rs.getString("FDF_CODIGO");
		}
		
	} catch (BDException e) {
		rollBackTransaction(abd,conexion,e);
	} catch (SQLException e) {
		rollBackTransaction(abd,conexion,e);
	}finally{
    commitTransaction(abd,conexion);
	}
	return resultado;
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
      m_Log.error(ex.getMessage());
    }
  }

}