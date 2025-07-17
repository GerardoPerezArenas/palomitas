/*
 * Documento.java
 *
 * Created on 5 de febrero de 2002, 10:58
 */

package es.altia.agora.technical;

import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.service.jdbc.JDBCWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.Vector;

/**
 *
 * @author  erik
 * @version
 */

public class Documento {

   private String nomPdf = "";
   private String nomPlantilla = "";
   private Vector vecDatos = null;
   private Vector vecCoord = null;

   protected static Config m_ConfigTechnical;
   protected static Log m_Log =
            LogFactory.getLog(Documento.class.getName());

   protected static String sql_nom_doc_pdf;
   protected static String sql_coord_x_pdf;
   protected static String sql_coord_y_pdf;

   public Documento(String codDoc, String[] arrayValores) throws TechnicalException {

      //Queremos usar el fichero de configuracion techserver
      m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
      sql_nom_doc_pdf = m_ConfigTechnical.getString("SQL.documento.nom_doc_pdf");
      sql_coord_x_pdf = m_ConfigTechnical.getString("SQL.documento.coord_x_pdf");
      sql_coord_y_pdf = m_ConfigTechnical.getString("SQL.documento.coord_y_pdf");

      //Obtiene de la B.D. el nombre de la plantilla a partir de su codigo "codDoc"
      this.generaNomPlantilla(codDoc);
      //Genera un vector cuyos elementos son del tipo [contenido, coordenadaX, coordenadaY]
      this.generaVectorDatos(codDoc, arrayValores);
      //Obtenemos el nombre del documento PDF
      Date fecha = new Date();
      String time = "" + fecha.getTime();
      this.nomPdf = this.nomPlantilla+"_"+time;
      if (m_Log.isDebugEnabled()) m_Log.debug("Nombre pdf: "+this.nomPdf);
      //Genera salida por consola de los datos (se podria eliminar esta funcion)
      this.visualizarDatos();
   }

   public Documento(String codDoc, Vector arrayValores) throws TechnicalException {

      //Queremos usar el fichero de configuracion techserver
      m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
      sql_nom_doc_pdf = m_ConfigTechnical.getString("SQL.documento.nom_doc_pdf");

      //Obtiene de la B.D. el nombre de la plantilla a partir de su codigo "codDoc"
      this.generaNomPlantilla(codDoc);
      if (m_Log.isDebugEnabled()) m_Log.debug("NOMBRE de la Plantillaaaaaaaaaaaaaaaaaaa:" + this.getNomPlantilla());
      //Genera un vector cuyos elementos son las lineas a escribir
      this.vecDatos = arrayValores;
      //Obtenemos el nombre del documento PDF
      Date fecha = new Date();
      String time = "" + fecha.getTime();
      this.nomPdf = this.nomPlantilla+"_"+time;
      if (m_Log.isDebugEnabled()) m_Log.debug("Nombre pdf: "+this.nomPdf);
   }

   public Documento(String codDoc, Vector arrayValores, String opcion) throws TechnicalException {

      //Queremos usar el fichero de configuracion techserver
      m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
      sql_nom_doc_pdf = m_ConfigTechnical.getString("SQL.documento.nom_doc_pdf");
      sql_coord_x_pdf = m_ConfigTechnical.getString("SQL.documento.coord_x_pdf");
      sql_coord_y_pdf = m_ConfigTechnical.getString("SQL.documento.coord_y_pdf");

      //Obtiene de la B.D. el nombre de la plantilla a partir de su codigo "codDoc"
      this.generaNomPlantilla(codDoc);
      if (m_Log.isDebugEnabled()) m_Log.debug("NOMBRE de la Plantillaaaaaaaaaaaaaaaaaaa:" + this.getNomPlantilla());

      //Obtiene el vector de registros (un registro por pagina)
      this.vecDatos = arrayValores;

      if ("P".equals(opcion))  // Documento con varias paginas a partir de una plantilla
      {
         //Genera un vector cuyos elementos son las coordenadas de los parametros
         this.generaVectorCoordenadas(codDoc);
      }
      //Obtenemos el nombre del documento PDF
      Date fecha = new Date();
      String time = "" + fecha.getTime();
      this.nomPdf = this.nomPlantilla+"_"+time;
      if (m_Log.isDebugEnabled()) m_Log.debug("Nombre pdf: "+this.nomPdf);
   }


   public String getNomPdf() {
      return this.nomPdf;
   }

   public String getNomPlantilla() {
      return this.nomPlantilla;
   }

   public Vector getVecDatos() {
      return this.vecDatos;
   }

   public Vector getVecCoord() {
      return this.vecCoord;
   }

    /*public void setNomPdf(String nombre) {
        this.nomPdf = nombre;
    }

    public void setNomPlantilla(String nombre) {
        this.nomPlantilla = nombre;
    }

    public void setVecDatros(Vector datos) {
        this.vecDatos = datos;
    }*/

   private void generaNomPlantilla(String codDoc) throws TechnicalException {

      //Obtiene de la B.D. el nombre de la plantilla a partir de su codigo "codDoc"
      String sql = m_ConfigTechnical.getString("SQL.documento.obtenerNomPlantilla");

      Vector params = new Vector();
      JDBCWrapper sqlExec = new JDBCWrapper();
      try {
         params.addElement(codDoc);
         if(m_Log.isDebugEnabled()) {
             m_Log.debug("sql: "+sql);
             m_Log.debug("Parametros: "+params.elementAt(0));
         }
         sqlExec.execute(sql, params);
         while (sqlExec.next())
         {
            this.nomPlantilla = sqlExec.getString(sql_nom_doc_pdf);
         }

      } catch (Exception e) {
         m_Log.error("Error en GenerarNomPlantilla" + e);
      } finally {
            //Debemos cerrar la conexion de la BD
            if (sqlExec!=null) sqlExec.close();
      }
   }


   private void generaVectorDatos (String codDoc, String[] arrayValores) throws TechnicalException {

      // Genera un vector cuyos elementos son del tipo [contenido, coordenadaX, coordenadaY]
      String sql = m_ConfigTechnical.getString("SQL.documento.obtenerCoordenadas");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);

      Vector vecDatosAux = new Vector();
      Vector params = new Vector();
      JDBCWrapper sqlExec = new JDBCWrapper();
      try {
         params.addElement(codDoc);
         if(m_Log.isDebugEnabled()){
             m_Log.debug("sql: "+sql);
             m_Log.debug("Parametros: "+params.elementAt(0));
         }
         sqlExec.execute(sql, params);
         int cont = 0;
         while (sqlExec.next()) {
            String arrayDatos[] = new String[3];
            arrayDatos[0] = arrayValores[cont];
            arrayDatos[1] = sqlExec.getString(sql_coord_x_pdf);
            arrayDatos[2] = sqlExec.getString(sql_coord_y_pdf);
            cont++;
            vecDatosAux.addElement(arrayDatos);
         }
         this.vecDatos = vecDatosAux;
      } catch (Exception e) {
         m_Log.error("Error en GeneraVectorDatos");
      } finally {
         //Debemos cerrar la conexion de la BD
            if (sqlExec!=null) sqlExec.close();
      }
   }

   private void generaVectorCoordenadas (String codDoc) throws TechnicalException {

      // Genera un vector cuyos elementos son del tipo [coordenadaX, coordenadaY]
      String sql = m_ConfigTechnical.getString("SQL.documento.obtenerCoordenadas");

      Vector vecCoordAux = new Vector();
      Vector params = new Vector();
      JDBCWrapper sqlExec = new JDBCWrapper();
      try {
         params.addElement(codDoc);
         if(m_Log.isDebugEnabled()){
             m_Log.debug("sql: "+sql);
             m_Log.debug("Parametros: "+params.elementAt(0));
         }
         sqlExec.execute(sql, params);
         while (sqlExec.next()) {
            String arrayCoord[] = new String[2];
            arrayCoord[0] = sqlExec.getString(sql_coord_x_pdf);
            arrayCoord[1] = sqlExec.getString(sql_coord_y_pdf);
            vecCoordAux.addElement(arrayCoord);
         }
         this.vecCoord = vecCoordAux;
      } catch (Exception e) {
         m_Log.error("Error en generaVectorCoordenadas()");
      } finally {
         //Debemos cerrar la conexion de la BD
            if (sqlExec!=null) sqlExec.close();
      }
   }

   private void visualizarDatos() {

      if (m_Log.isDebugEnabled()) m_Log.debug("Nombre plantilla: "+this.nomPlantilla);
      for (int i=0; i<this.vecDatos.size(); i++) {
         String arrayAux[] = new String[3];
         arrayAux = (String[])vecDatos.elementAt(i);
          if (m_Log.isDebugEnabled()){
              m_Log.debug("Propiedad " + i);
              m_Log.debug("   Valor: " + arrayAux[0]);
              m_Log.debug("   Coordenadas X: " + arrayAux[1]);
              m_Log.debug("   Coordenadas Y: " + arrayAux[2]);
          }
      }
   }

}
