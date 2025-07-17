package es.altia.util.conexion;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;

public class Cursor {
	public int Indice = -1;
	public java.util.Hashtable hash = new java.util.Hashtable();
	public java.util.Hashtable hash_Nombres = new java.util.Hashtable();
	public static final String CAMPO_ERROR = "ERROR";
	public static final String CAMPO_DATOS = "DATOS";
	public static final int ERROR = -999;
	public static final float INI_FLOAT = 0;
	public static final double INI_DOUBLE = 0.0;
	public static final int INI_ENTERO = 0;
	public static final long INI_LONG = 0;

	public Cursor() {

	}

	public Cursor(int error, java.util.Vector Datos) {
		Indice = -1;
		this.hash.put(
			es.altia.util.conexion.Cursor.CAMPO_ERROR,
			new Integer(error));
		this.hash.put(es.altia.util.conexion.Cursor.CAMPO_DATOS, Datos);
	}

	public Cursor(
		int error,
		java.util.Vector Datos,
		java.util.Vector NombresColumnas) {
		Indice = -1;
		this.hash.put(
			es.altia.util.conexion.Cursor.CAMPO_ERROR,
			new Integer(error));
		this.hash.put(es.altia.util.conexion.Cursor.CAMPO_DATOS, Datos);

		for (int i = 1; i <= NombresColumnas.size(); i++) {
			this.hash_Nombres.put(
				(((String) NombresColumnas.elementAt(i - 1)).toUpperCase()),
				new Integer(i));
		}
	}

	public Cursor(java.sql.ResultSet resultSet, int numCampos) {
		boolean esPrimeraTupla = true;
		Indice = -1;
		this.hash.put(
			es.altia.util.conexion.Cursor.CAMPO_ERROR,
			new Integer(0));
		java.util.Vector vectorDatos = new java.util.Vector();
		try {
			//es.altia.util.Debug.println("numCampos " + numCampos);
			while (resultSet.next()) {
				java.util.Vector Aux = new java.util.Vector();
				for (int i = 1; i <= numCampos; i++) {
					if (esPrimeraTupla)
						hash_Nombres.put(
							(resultSet.getMetaData().getColumnName(i))
								.toString()
								.toUpperCase(),
							new Integer(i));
					Aux.addElement(resultSet.getObject(i));
				}
				esPrimeraTupla = false;

				vectorDatos.addElement(Aux);
			}
			//es.altia.util.Debug.println("" + vectorDatos);
			this.hash.put(
				es.altia.util.conexion.Cursor.CAMPO_DATOS,
				vectorDatos);
		} catch (Exception e) {
			this.hash.put(
				es.altia.util.conexion.Cursor.CAMPO_ERROR,
				new Integer(es.altia.util.conexion.Cursor.ERROR));
			this.hash.put(
				es.altia.util.conexion.Cursor.CAMPO_DATOS,
				new java.util.Vector());
		}
	}

	public Cursor(java.sql.ResultSet resultSet, int numCampos, int numTuplas) {
		boolean esPrimeraTupla = true;
		this.Indice = -1;
		this.hash.put(
			es.altia.util.conexion.Cursor.CAMPO_ERROR,
			new Integer(0));
		java.util.Vector vectorDatos = new java.util.Vector();
		try {
			//es.altia.util.Debug.println(numTuplas + " numCampos " + numCampos);
			int Tupla = 0;
			while (resultSet.next() && (Tupla < numTuplas)) {
				java.util.Vector Aux = new java.util.Vector();
				for (int i = 1; i <= numCampos; i++) {
					if (esPrimeraTupla)
						hash_Nombres.put(
							(resultSet.getMetaData().getColumnName(i))
								.toString()
								.toUpperCase(),
							new Integer(i));
					Aux.addElement(resultSet.getObject(i));
				}
				esPrimeraTupla = false;
				vectorDatos.addElement(Aux);
				Tupla++;
				//es.altia.util.Debug.println("" + Tupla);
			}
			//es.altia.util.Debug.println("" + vectorDatos);
			this.hash.put(
				es.altia.util.conexion.Cursor.CAMPO_DATOS,
				vectorDatos);
		} catch (Exception e) {
			this.hash.put(
				es.altia.util.conexion.Cursor.CAMPO_ERROR,
				new Integer(es.altia.util.conexion.Cursor.ERROR));
			this.hash.put(
				es.altia.util.conexion.Cursor.CAMPO_DATOS,
				new java.util.Vector());
		}
	}

	public boolean next() {
		this.Indice++;
		return Indice
			< ((java.util.Vector) this
				.hash
				.get(es.altia.util.conexion.Cursor.CAMPO_DATOS))
				.size();
	}

	public String getString(int Columna) {
		String Devolver = "";
		try {
			return (
				(java.util.Vector)
					(
						(java.util.Vector) this.hash.get(
							es
								.altia
								.util
								.conexion
								.Cursor
								.CAMPO_DATOS))
								.elementAt(
					this.Indice))
				.elementAt(Columna - 1)
				.toString();
		} catch (Exception e) {

		}

		return Devolver;
	}

	public String getStringNull(int Columna) {
		String Devolver = null;
		try {
			return (
				(java.util.Vector)
					(
						(java.util.Vector) this.hash.get(
							es
								.altia
								.util
								.conexion
								.Cursor
								.CAMPO_DATOS))
								.elementAt(
					this.Indice))
				.elementAt(Columna - 1)
				.toString();
		} catch (Exception e) {

		}

		return Devolver;
	}

	public String getString(String nombreColumna) {
		int Columna =
			(((Integer) hash_Nombres.get(nombreColumna.toUpperCase()))
				.intValue());
		//return ((java.util.Vector)((java.util.Vector)this.hash.get(es.altia.util.conexion.Cursor.CAMPO_DATOS)).elementAt(this.Indice)).elementAt(Columna-1).toString();
		return (getString(Columna));
	}

	public String getStringNull(String nombreColumna) {
		int Columna =
			(((Integer) hash_Nombres.get(nombreColumna.toUpperCase()))
				.intValue());
		//return ((java.util.Vector)((java.util.Vector)this.hash.get(es.altia.util.conexion.Cursor.CAMPO_DATOS)).elementAt(this.Indice)).elementAt(Columna-1).toString();
		return (getStringNull(Columna));
	}

	public int getint(int Columna) {
		int Devolver = es.altia.util.conexion.Cursor.INI_ENTERO;

		try {
			Devolver =
				Integer.parseInt(
					((java
						.util
						.Vector) ((java.util.Vector) this
						.hash
						.get(es.altia.util.conexion.Cursor.CAMPO_DATOS))
						.elementAt(this.Indice))
						.elementAt(Columna - 1)
						.toString());
		} catch (Exception e) {

		}
		return Devolver;
	}

	public Integer getInteger(int Columna) {
		Integer Devolver = null;

		try {
			Devolver =
				new Integer(
					((java
						.util
						.Vector) ((java.util.Vector) this
						.hash
						.get(es.altia.util.conexion.Cursor.CAMPO_DATOS))
						.elementAt(this.Indice))
						.elementAt(Columna - 1)
						.toString());
		} catch (Exception e) {

		}
		return Devolver;
	}

	public int getint(String nombreColumna) {
		int Columna =
			(((Integer) hash_Nombres.get(nombreColumna.toUpperCase()))
				.intValue());
		return (getint(Columna));
	}

	public Integer getInteger(String nombreColumna) {
		int Columna =
			(((Integer) hash_Nombres.get(nombreColumna.toUpperCase()))
				.intValue());
		return (getInteger(Columna));
	}

	public long getlong(int Columna) {
		long Devolver = es.altia.util.conexion.Cursor.INI_LONG;

		try {
			Devolver =
				Long.parseLong(
					((java
						.util
						.Vector) ((java.util.Vector) this
						.hash
						.get(es.altia.util.conexion.Cursor.CAMPO_DATOS))
						.elementAt(this.Indice))
						.elementAt(Columna - 1)
						.toString()
						.trim());
		} catch (Exception e) {

		}

		return Devolver;
	}

	public Long getLong(int Columna) {
		Long Devolver = null;

		try {
			Devolver =
				new Long(
					((java
						.util
						.Vector) ((java.util.Vector) this
						.hash
						.get(es.altia.util.conexion.Cursor.CAMPO_DATOS))
						.elementAt(this.Indice))
						.elementAt(Columna - 1)
						.toString()
						.trim());
		} catch (Exception e) {

		}

		return Devolver;
	}

	public long getlong(String nombreColumna) {
		int Columna =
			(((Integer) hash_Nombres.get(nombreColumna.toUpperCase()))
				.intValue());
		return (getlong(Columna));
	}

	public Long getLong(String nombreColumna) {
		int Columna =
			(((Integer) hash_Nombres.get(nombreColumna.toUpperCase()))
				.intValue());
		return (getLong(Columna));
	}

	public float getfloat(int Columna) {
		float Devolver = es.altia.util.conexion.Cursor.INI_FLOAT;

		try {
			Devolver =
				Float.parseFloat(
					((java
						.util
						.Vector) ((java.util.Vector) this
						.hash
						.get(es.altia.util.conexion.Cursor.CAMPO_DATOS))
						.elementAt(this.Indice))
						.elementAt(Columna - 1)
						.toString()
						.trim());
		} catch (Exception e) {

		}

		return Devolver;
	}

	public float getfloat(String nombreColumna) {
		int Columna =
			(((Integer) hash_Nombres.get(nombreColumna.toUpperCase()))
				.intValue());
		return (getfloat(Columna));
	}

	public Float getFloat(int Columna) {
		Float Devolver = null;

		try {
			Devolver =
				new Float(
					((java
						.util
						.Vector) ((java.util.Vector) this
						.hash
						.get(es.altia.util.conexion.Cursor.CAMPO_DATOS))
						.elementAt(this.Indice))
						.elementAt(Columna - 1)
						.toString()
						.trim());
		} catch (Exception e) {

		}

		return Devolver;
	}

	public Float getFloat(String nombreColumna) {
		int Columna =
			(((Integer) hash_Nombres.get(nombreColumna.toUpperCase()))
				.intValue());
		return (getFloat(Columna));
	}

	public double getdouble(int Columna) {
		double Devolver = es.altia.util.conexion.Cursor.INI_DOUBLE;

		try {
			Devolver =
				Double.parseDouble(
					((java
						.util
						.Vector) ((java.util.Vector) this
						.hash
						.get(es.altia.util.conexion.Cursor.CAMPO_DATOS))
						.elementAt(this.Indice))
						.elementAt(Columna - 1)
						.toString()
						.trim());
		} catch (Exception e) {

		}

		return Devolver;
	}

	public double getdouble(String nombreColumna) {
		int Columna =
			(((Integer) hash_Nombres.get(nombreColumna.toUpperCase()))
				.intValue());
		return (getdouble(Columna));
	}

	public Double getDouble(int Columna) {
		Double Devolver = null;

		try {
			Devolver =
				new Double(
					((java
						.util
						.Vector) ((java.util.Vector) this
						.hash
						.get(es.altia.util.conexion.Cursor.CAMPO_DATOS))
						.elementAt(this.Indice))
						.elementAt(Columna - 1)
						.toString()
						.trim());
		} catch (Exception e) {

		}

		return Devolver;
	}

	public Double getDouble(String nombreColumna) {
		int Columna =
			(((Integer) hash_Nombres.get(nombreColumna.toUpperCase()))
				.intValue());
		return (getDouble(Columna));
	}

	public Object getObject(int Columna) {
		return (
			(java
				.util
				.Vector) ((java.util.Vector) this
				.hash
				.get(es.altia.util.conexion.Cursor.CAMPO_DATOS))
				.elementAt(this.Indice))
				.elementAt(
			Columna - 1);

	}

	public Object getObject(String nombreColumna) {
		int Columna =
			(((Integer) hash_Nombres.get(nombreColumna.toUpperCase()))
				.intValue());
		return (getObject(Columna));
	}

    public String getClob(int Columna) {
        String resultado = "";
        try {
            Config conf = ConfigServiceHelper.getConfig("techserver");
            String gestor = conf.getString("CON.gestor");
            if (gestor.equalsIgnoreCase("ORACLE")) {
                java.sql.Clob clob =
                        (java.sql.Clob) this.getObject(Columna);
                java.io.Reader cr = clob.getCharacterStream();
                java.io.CharArrayWriter ot = new java.io.CharArrayWriter();
                if (cr != null) {
                    int c;
                    while ((c = cr.read()) != -1) {
                        ot.write(c);
                    }
                    resultado = ot.toString();
                }
            } else if (gestor.equalsIgnoreCase("SQLSERVER")){
                resultado = (String) this.getObject(Columna);
            } else{
                throw new Exception("NO EXISTE IMPLEMENTACION PARA LA BASE DE DATOS: " + gestor + " SELECCIONADA ACTUALMENTE");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public String getClob(String nombreColumna) {
        int Columna =
                (((Integer) hash_Nombres.get(nombreColumna.toUpperCase()))
                        .intValue());
        return (getClob(Columna));
    }

	public void setString(int Columna, String valor) {
		java.util.Vector vectorFila =
			((java
				.util
				.Vector) ((java.util.Vector) this
				.hash
				.get(es.altia.util.conexion.Cursor.CAMPO_DATOS))
				.elementAt(this.Indice));
		vectorFila.setElementAt(valor, Columna - 1);
	}

	public void setString(String nombreColumna, String valor) {
		int Columna =
			(((Integer) hash_Nombres.get(nombreColumna.toUpperCase()))
				.intValue());
		setString(Columna, valor);

		//return ((java.util.Vector)((java.util.Vector)this.hash.get(es.altia.util.conexion.Cursor.CAMPO_DATOS)).elementAt(this.Indice)).elementAt(Columna-1).toString();

		//java.util.Vector vectorFila = ((java.util.Vector)((java.util.Vector)this.hash.get(es.altia.util.conexion.Cursor.CAMPO_DATOS)).elementAt(this.Indice));

		//vectorFila.setElementAt(valor,Columna-1);

		// return (getString(Columna));
	}

	public void setInteger(int Columna, Integer valor) {
		java.util.Vector vectorFila =
			((java
				.util
				.Vector) ((java.util.Vector) this
				.hash
				.get(es.altia.util.conexion.Cursor.CAMPO_DATOS))
				.elementAt(this.Indice));
		vectorFila.setElementAt(valor, Columna - 1);
	}

	public void setInteger(String nombreColumna, Integer valor) {
		int Columna =
			(((Integer) hash_Nombres.get(nombreColumna.toUpperCase()))
				.intValue());
		setInteger(Columna, valor);
	}

	public boolean esVacio() {
		return !(
			((java.util.Vector) this
				.hash
				.get(es.altia.util.conexion.Cursor.CAMPO_DATOS))
				.size()
				> 0);
	}

	public int numTuplas() {
		return (
			((java.util.Vector) this
				.hash
				.get(es.altia.util.conexion.Cursor.CAMPO_DATOS))
				.size());
	}

	public int numColumnas() {
		int numCol = 0;
		java.util.Vector tuplas =
			((java.util.Vector) this
				.hash
				.get(es.altia.util.conexion.Cursor.CAMPO_DATOS));
		if (tuplas != null) {
			if (tuplas.size() > 0)
				numCol = ((java.util.Vector) tuplas.elementAt(0)).size();
		}
		return numCol;
	}

	public void anadirColumna(String nombre) {
		this.hash_Nombres.put(
			nombre.toUpperCase(),
			new Integer(this.numColumnas() + 1));
	}

	public void anadiraTupla(Object objeto) {
		java.util.Vector tupla =
			((java
				.util
				.Vector) ((java.util.Vector) this
				.hash
				.get(es.altia.util.conexion.Cursor.CAMPO_DATOS))
				.elementAt(this.Indice));

		tupla.addElement(objeto);
		(
			(java.util.Vector) this.hash.get(
				es.altia.util.conexion.Cursor.CAMPO_DATOS)).setElementAt(
			tupla,
			this.Indice);
		//es.altia.util.Debug.println("Acabamos de añadir el campo..."+tupla);

	}

}
