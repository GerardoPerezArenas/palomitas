package es.altia.agora.business.gestionInformes.particular.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.gestionInformes.particular.CodigoEtiqueta;
import es.altia.agora.business.gestionInformes.particular.InformeParticularFacade;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class InformeEstadisticasRegistro implements InformeParticularFacade {
	
    protected static Log m_Log = LogFactory.getLog(InformeEstadisticasRegistro.class.getName());
    protected static Config m_Conf = ConfigServiceHelper.getConfig("common");

    private String[] paramsBD;
    
    private ArrayList<CodigoEtiqueta> opcion;
    private ArrayList<CodigoEtiqueta> tipo;
    private static final ArrayList<CodigoEtiqueta> formato;
    
    static {
    	
    	
    	formato = new ArrayList<CodigoEtiqueta>();
    	formato.add(new CodigoEtiqueta("PDF", "PDF"));
    	formato.add(new CodigoEtiqueta("EXCEL", "EXCEL"));
    }
    
    
    public void setParamsBD(String[] params) {
        this.paramsBD = params;
    }
    
	
	public ArrayList<CodigoEtiqueta> getCodigosEtiquetasSelect(String idCampo, UsuarioValueObject usuario) throws TechnicalException {

		ResourceBundle infParticularProps = ResourceBundle.getBundle
			("es.altia.agora.business.gestionInformes.particular.impl.InformeEstadisticasRegistro");
		if (usuario.getIdioma()==ConstantesDatos.IDIOMA_GALLEGO)
			infParticularProps = ResourceBundle.getBundle
				("es.altia.agora.business.gestionInformes.particular.impl.InformeEstadisticasRegistro_gl_ES");
	        	
		
    if (idCampo.equals("opcion")) {
    	opcion = new ArrayList<CodigoEtiqueta>();
    	opcion.add(new CodigoEtiqueta("01", infParticularProps.getString("Registro/opcion/dia")));
    	opcion.add(new CodigoEtiqueta("02", infParticularProps.getString("Registro/opcion/semana")));
    	opcion.add(new CodigoEtiqueta("03", infParticularProps.getString("Registro/opcion/quincena")));
    	opcion.add(new CodigoEtiqueta("04", infParticularProps.getString("Registro/opcion/mes")));
    	opcion.add(new CodigoEtiqueta("05", infParticularProps.getString("Registro/opcion/anho")));
    	return opcion;

    }
    else if (idCampo.equals("tipo")){
    	tipo = new ArrayList<CodigoEtiqueta>();
    	tipo.add(new CodigoEtiqueta("E", infParticularProps.getString("Registro/tipo/entrada")));
    	tipo.add(new CodigoEtiqueta("S", infParticularProps.getString("Registro/tipo/salida")));
    	tipo.add(new CodigoEtiqueta("A", infParticularProps.getString("Registro/tipo/ambos")));
    	return tipo;
    }
    else if (idCampo.equals("uors")){
    	return recuperaUorsRegistro(usuario);
    }
    else if (idCampo.equals("formato")){
    	return formato;
    }
    return new ArrayList<CodigoEtiqueta>();
	}
	
	private ArrayList<CodigoEtiqueta> recuperaUorsRegistro(UsuarioValueObject usuario) throws TechnicalException {
		AdaptadorSQLBD abd = new AdaptadorSQLBD(paramsBD);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<CodigoEtiqueta> codsEtiquetas = new ArrayList<CodigoEtiqueta>();
        
        String sql="SELECT UOR_COD, UOR_NOM FROM A_UOR, "+ GlobalNames.ESQUEMA_GENERICO +
        "A_ORG,"+ GlobalNames.ESQUEMA_GENERICO +"A_UOU " +
        		"WHERE UOU_ORG=? AND UOU_ENT=? AND UOU_USU=? AND UOR_TIP='1' AND UOU_UOR=UOR_COD " +
        		"AND UOU_ORG=ORG_COD AND UOR_NO_VIS ='0'";
        m_Log.debug("recuperaUorsRegistro: " +sql);
        
            try {
				con = abd.getConnection();
				ps = con.prepareStatement(sql);
	            ps.setInt(1, usuario.getOrgCod());
	            ps.setInt(2, usuario.getEntCod());
	            ps.setInt(3, usuario.getIdUsuario());
	            rs = ps.executeQuery();
	            
	            while (rs.next()) {
	                String codigo = rs.getString(1);
	                String etiqueta = rs.getString(2);
	                codsEtiquetas.add(new CodigoEtiqueta(codigo, etiqueta));
	            }
	            
			return codsEtiquetas;
            } catch (BDException bde) {
            	throw new TechnicalException(bde.getMensaje(), bde);
			} catch (SQLException sqle) {
				throw new TechnicalException(sqle.getMessage(), sqle);
			} finally {
				closeResultSet(rs);
				closeStatement(ps);
				devolverConexion(abd, con);
			}

	}

	public Collection recuperaDatosInforme(GeneralValueObject datosEntrada, UsuarioValueObject usuario) throws TechnicalException {

		ResourceBundle infParticularProps = ResourceBundle.getBundle
			("es.altia.agora.business.gestionInformes.particular.impl.InformeEstadisticasRegistro");
		if (usuario.getIdioma()==ConstantesDatos.IDIOMA_GALLEGO)
			infParticularProps = ResourceBundle.getBundle
				("es.altia.agora.business.gestionInformes.particular.impl.InformeEstadisticasRegistro_gl_ES");
		
		AdaptadorSQLBD abd = new AdaptadorSQLBD(paramsBD);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
		String  tipo= (String) datosEntrada.getAtributo("tipo");
		String fechaDesde = (String) datosEntrada.getAtributo("fechaDesde");
		String fechaHasta = (String) datosEntrada.getAtributo("fechaHasta");
		String opcion = (String) datosEntrada.getAtributo("opcion");
		Vector uors = (Vector) datosEntrada.getAtributo("listauors");	
		String listaUors="";
		
		for(int t=0; t < uors.size();t++){
			listaUors+=uors.get(t);
			if (t<uors.size()-1) listaUors+=",";
		}
		
		m_Log.debug("PARÁMETROS DEL REQUEST = " + tipo + " - " + fechaDesde + " - " +
				fechaHasta + " - " + opcion + "-" + listaUors );
		String sql = "";
		
		HashMap datosUors = recuperarUorsSeleccionada (paramsBD,uors);
		Vector codUors = (Vector) datosUors.get("codigos");
		Vector <String> nomUors = (Vector) datosUors.get("nombres"); //Titulos de columnas
		Collection<DatoEstadisticaRegistro> datosInforme = new ArrayList<DatoEstadisticaRegistro>(); //return
                Vector fechas = new Vector();
                try {
		fechas = calcularFechas (opcion,fechaDesde,fechaHasta);
                }catch (ParseException e) {}
		Vector <String> fila = new Vector <String>();
		DatoEstadisticaRegistro der = new DatoEstadisticaRegistro();
		//añado el titulo de los totales
		nomUors.add("TOTAL");
		der.setUors(nomUors);
		Vector<Vector<String>> filas = new Vector<Vector<String>>();
		String fechaInicio="";
		String fechaFin="";
		String condicion="";
		der.setTitulo(infParticularProps.getString("Registro/titulo"+tipo));
		der.setUsuario(usuario.getNombreUsu());
        SimpleDateFormat d= new SimpleDateFormat("dd/mm/yyyy");
        Date dia = new Date();
		dia.getTime();
		der.setFecha(d.format(dia));
		
		if (tipo.equals("E")) {
			condicion = " AND RES_TIP='E' ";
		}
		else if (tipo.equals("S")){
			condicion = " AND RES_TIP='S' ";
		}
		
		
		try{
			con = abd.getConnection();
			
			for(int i=0;i<fechas.size()-1;i++){//Consulto los registros para cada fecha y Uor
				fila = new Vector<String>();
				fila.add((String) fechas.get(i));
				fechaInicio=(String) fechas.get(i);
				

					if (i==fechas.size()-1) fechaFin=fechaHasta;
					else fechaFin=(String) fechas.get(i+1);

				int j;
				for (j=0;j<codUors.size();j++){					
					
					sql="select uor_nom AS UOR," + 
		    		abd.convertir("RES_FEC", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY")+
			        " as fecha,count(*) as contador " +
			    	"from r_res, a_uor where  res_uor=uor_cod and RES_FEC>="+ 
			    	abd.convertir("'"+fechaInicio+"'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY")+
			    	" and RES_FEC<"+
			    	abd.convertir("'"+fechaFin+"'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY")+
					" and uor_cod='"+ (String)codUors.get(j) +
					"'   "+ condicion + " group by uor_nom,"+
			    	abd.convertir("RES_FEC", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY");
					
					ps = con.prepareStatement(sql);
					
					rs = ps.executeQuery();
					int cont = 0;
                    while (rs.next()){
                        cont += rs.getInt("CONTADOR");
					}

    				fila.add(String.valueOf(cont));
				}
				

				
				if(j==codUors.size()){
					sql="select count(*) as contador " +
			    	"from r_res, a_uor where  res_uor=uor_cod and RES_FEC>="+ 
			    	abd.convertir("'"+fechaInicio+"'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY")+
			    	" and RES_FEC<"+
			    	abd.convertir("'"+fechaFin+"'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY")+
					" and uor_cod in ("+ listaUors + ") "+ condicion ;
				}
				else{
					sql="select count(*) as contador " +
			    	"from r_res, a_uor where  res_uor=uor_cod and RES_FEC>="+ 
			    	abd.convertir("'"+fechaInicio+"'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY")+
			    	" and RES_FEC<"+
			    	abd.convertir("'"+fechaFin+"'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY")+
					" and uor_cod in ("+ listaUors + ") "+ condicion ;
				}
				//m_Log.debug(sql);
		    	ps = con.prepareStatement(sql);
		    	rs = ps.executeQuery();
				if (rs.next()) fila.add(String.valueOf(rs.getInt("CONTADOR")));

				
				
				filas.add(fila);
			}
			
			/*Debemos crear una última fila con las sumas por UOR's*/
	    	int k=0;
			fila = new Vector<String>();
	    	fila.add("TOTAL");
			for (int j=0;j<codUors.size();j++){
		    	sql="select count(*) as contador " +
		    	"from r_res, a_uor where  res_uor=uor_cod and RES_FEC>="+ 
		    	abd.convertir("'"+fechaDesde+"'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY")+
		    	" and RES_FEC<"+
		    	abd.convertir("'"+fechaFin+"'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY")+
				" and uor_cod='"+ (String)codUors.get(j) + "'   "+ condicion ;
				m_Log.debug(sql);
		    	ps = con.prepareStatement(sql);
		    	rs = ps.executeQuery();
		    	if (rs.next()) {
		    		fila.add(String.valueOf(rs.getInt("CONTADOR")));
		    		k=k+rs.getInt("CONTADOR");
		    	}
		    	}
			fila.add(String.valueOf(k));
			
			filas.add(fila);
			
			
			/*Cerramos la info del documento XML*/
			der.setFilas(filas);
			datosInforme.add(der);			
			
		} catch (BDException bde) {
			bde.printStackTrace();
			throw new TechnicalException(bde.getMensaje(), bde);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			throw new TechnicalException(sqle.getMessage(), sqle);
		}
		finally{
            closeResultSet(rs);
            closeStatement(ps);
            devolverConexion(abd, con);
		}
		return datosInforme;
		
	}
	

	private HashMap recuperarUorsSeleccionada(String[] paramsBD, Vector uors) {
		AdaptadorSQLBD abd = new AdaptadorSQLBD(paramsBD);
		PreparedStatement ps;
		ResultSet rs;
		String sql="";
		Vector<String> nomUors=new Vector<String>();
		Vector<String> codUors=new Vector<String>();
		HashMap<String, Vector<String>> datos = new HashMap<String, Vector<String>>();

			Connection con;
			try {
				
				con = abd.getConnection();
				sql ="SELECT UOR_NOM, UOR_COD FROM A_UOR WHERE UOR_COD=? order by uor_nom";
				ps = con.prepareStatement(sql);
				m_Log.debug(sql);
				
				for(int i=0; i<uors.size();i++){
					ps.setString(1, (String)uors.get(i));				
					rs = ps.executeQuery();
					rs.next();
					nomUors.add(rs.getString("UOR_NOM"));
					codUors.add(rs.getString("UOR_COD"));
				}
				datos.put("codigos", codUors);
				datos.put("nombres", nomUors);

			} catch (BDException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return datos;
	}

        
	private Vector calcularFechas(String opcion, String fechaDesde, String fechaHasta) throws ParseException {					            
                Vector<String> fechas = new Vector<String>(); 
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date dateDesde = dateFormat.parse(fechaDesde);
                Date dateHasta = dateFormat.parse(fechaHasta);
		Calendar desde = Calendar.getInstance();
		Calendar hasta = Calendar.getInstance();
                desde.setTime(dateDesde);
                hasta.setTime(dateHasta);
		long timeDesde = desde.getTimeInMillis();
                long timeHasta = hasta.getTimeInMillis();
                Date dAux;
		while(timeDesde<=timeHasta){
                        dAux = desde.getTime();                        
			fechas.addElement(dateFormat.format(dAux));	                        
			if((opcion.equals("01")))desde.add(Calendar.DAY_OF_MONTH, 1);				
			else if((opcion.equals("02")))desde.add(Calendar.DAY_OF_MONTH, 7);					
			else if((opcion.equals("03")))desde.add(Calendar.DAY_OF_MONTH, 14);						
			else if((opcion.equals("04")))desde.add(Calendar.MONTH, 1);					
			else if((opcion.equals("05")))desde.add(Calendar.YEAR, 1);
                        timeDesde = desde.getTimeInMillis();
		}
        dAux = desde.getTime();                        
		fechas.addElement(dateFormat.format(dAux));
		
		return fechas;
	}


	public GeneralValueObject recuperarDatosRequest(HttpServletRequest request) {
		 GeneralValueObject datosRequest = new GeneralValueObject();
	        datosRequest.setAtributo("tipo", request.getParameter("tipo"));
	        datosRequest.setAtributo("formato", request.getParameter("formato"));
	        datosRequest.setAtributo("opcion", request.getParameter("opcion"));
	        datosRequest.setAtributo("fechaDesde", request.getParameter("fechaDesde"));
	        datosRequest.setAtributo("fechaHasta", request.getParameter("fechaHasta"));
	        datosRequest.setAtributo("listauors", (Vector) listaTokens(request.getParameter("listauors")));
	        return datosRequest;
	}


	/**
	 * Creacion del informe pdf.
	 * */
	public String generarInforme(GeneralValueObject datosEntrada, Collection datosInforme,
			UsuarioValueObject usuVO, String url, String servlet) throws TechnicalException {
		
		m_Log.debug ("--> El informe se generará con formato =" + datosEntrada.getAtributo("formato"));
		
		ParserInformeEstadisticasRegistro pier = new ParserInformeEstadisticasRegistro();
		
		if(datosEntrada.getAtributo("formato").equals("PDF"))
			return pier.generarInformePDF(datosInforme, usuVO, url, paramsBD);
		else
			return pier.generarInformeExcel(datosInforme, usuVO, url, paramsBD);
		
	}
	
	
	
    private void closeResultSet(ResultSet rs) throws TechnicalException {
        try {
            if (rs != null) rs.close();
        } catch (SQLException sqle) {
            throw new TechnicalException(sqle.getMessage(), sqle);
        }
    }
    
    private void closeStatement(Statement stmt) throws TechnicalException {
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException sqle) {
            throw new TechnicalException(sqle.getMessage(), sqle);
        }
    }
    
    private void devolverConexion(AdaptadorSQLBD bd, Connection con) throws TechnicalException {
        try {
            if (con != null) bd.devolverConexion(con);
        } catch (BDException bde) {
            throw new TechnicalException(bde.getMensaje());
        }
    }
	
	
    private Vector listaTokens(String listSelecc)	{
        Vector<String> lista = new Vector<String>();
        StringTokenizer codigos =	null;
        if (listSelecc != null) {
          codigos = new StringTokenizer(listSelecc,"§¥",false);
          while	(codigos.hasMoreTokens()) {
            String cod = codigos.nextToken();
            lista.addElement(cod);
          }
        }
        return lista;
      }
    
    
}