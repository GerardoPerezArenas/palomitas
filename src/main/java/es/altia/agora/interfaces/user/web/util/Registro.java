package es.altia.agora.interfaces.user.web.util;



import java.text.*;

import java.util.*;

import javax.servlet.http.*;



/*

 * Clase que hereda de una Hashtable.

 * En el registro se introducen objetos accesibles por una clave.

 */

public class Registro extends Hashtable

{



// --------------- ATRIBUTOS -----------------------



    private String _id;

    private String _descripcion;



// --------------- ACCESORS -----------------------



    public String getID()

    {

        _id = (String)super.get("ID");



        return _id;

    }



    public void setID(String cadena)

    {

        super.put("ID",cadena);

    }



    public String getDesc()

    {

        _descripcion = (String)super.get("DESC");



        return _descripcion;

    }



    public void setDesc(String cadena)

    {

        super.put("DESC",cadena);

    }



// --------------- PUBLIC -----------------------





	/// Poner un string

	public void setString( String FIELD, String value )

	{

	  //if ( FIELD==null || value==null )

	  //	  System.err.println("Registro:setString FIELD||value null: " + FIELD + " " + value );



      if (FIELD==null)

	        System.err.println("Registro:setString FIELD||value null: " + FIELD + " " + value );



      if(value == null)

        put(FIELD,"");

      else

	  if(value.equals(" "))

	    put( FIELD, value);

	  else

	    put( FIELD, value.trim() );

  }



	/// Poner un int

	public void setInt( String FIELD, int value )

	{

	  if ( FIELD==null )

		  System.err.println("Registro:setInt FIELD:" + FIELD + " " + value );

	  put( FIELD, new Integer(value) );

    }



	/// Poner un Integer

	public void setInt( String FIELD, Integer value )

	{

	  if ( FIELD==null )

		  System.err.println("Registro:setInt FIELD:" + FIELD + " " + value );

	  put( FIELD, value );

    }







	/// Poner un long

	public void setLong( String FIELD, long value )

	{

	  if ( FIELD==null )

		  System.err.println("Registro:setInt FIELD:" + FIELD + " " + value );

	  put( FIELD, new Long(value) );

    }



    /// Poner un String

    public void setLong( String FIELD, String value )

	{

	  if ( FIELD==null )

		  System.err.println("Registro:setInt FIELD:" + FIELD + " " + value );

      if (value.equals(""))

          put(FIELD,new Long(-1));

	  else

	      put( FIELD, new Long(value) );

    }





	/// Poner un Long

	public void setLong( String FIELD, Long value )

	{

	  if ( FIELD==null )

		  System.err.println("Registro:setInt FIELD:" + FIELD + " " + value );

	  put( FIELD, value );

    }







	/// Poner un short

	public void setShort( String FIELD, short value )

	{

	  if ( FIELD==null )

		  System.err.println("Registro:setShort FIELD:" + FIELD + " " + value );

	  put( FIELD, new Short(value) );

    }





	/// Poner un short

	public void setShort( String FIELD, Short value )

	{

	  if ( FIELD==null )

		  System.err.println("Registro:setShort FIELD:" + FIELD + " " + value );

	  put( FIELD, value );

    }







	/// Poner un double

	public void setDouble( String FIELD, double value )

	{

	  if ( FIELD==null )

		  System.err.println("Registro:setDouble FIELD: " + FIELD + " " + value );

	  put( FIELD, new Double(value) );

    }



	/// Poner un Double

	public void setDouble( String FIELD, Double value )

	{

	  if ( FIELD==null )

		  System.err.println("Registro:setDouble FIELD: " + FIELD + " " + value );

	  put( FIELD, value );

    }







	/// Quitar un int

	public int getInt( String FIELD )

	{

		return ((Integer)get(FIELD)).intValue();

	}



	/// Quitar un Integer

	public Integer getInteger( String FIELD )

	{

		return ((Integer)get(FIELD));

	}







	/// Quitar un long

	public long getLong( String FIELD )

	{

	    if (get(FIELD) == null)

	        return -1;

	    else

		    return ((Long)get(FIELD)).longValue();

	}



	/// Quitar un Long

	public Long getLongC( String FIELD )

	{

		return ((Long)get(FIELD));

	}





	/// Quitar un short

	public short getShort( String FIELD )

	{

	    if (get(FIELD) == null)

	        return -1;

	    else

    		return ((Short)get(FIELD)).shortValue();

	}





	/// Quitar un Short

	public Short getShortC( String FIELD )

	{

		return ((Short)get(FIELD));

	}





	/// Quitar un double

	public double getDouble( String FIELD )

	{

	    if (get(FIELD) == null)

	        return -1;

	    else

    		return ((Double)get(FIELD)).doubleValue();

	}



	/// Quitar un Double

	public Double getDoubleC( String FIELD )

	{

  		return ((Double)get(FIELD));

	}







	/// Quitar un string

	public String getString( String FIELD )

	{

	    if(get(FIELD) == null)

	        return "";

	    else

		    return ((String)get(FIELD));

	}







	/// Quitar un int

	public String getIntToString( String FIELD )

	{

	    if (get(FIELD) == null)

	        return "";

        else

                return ((Integer)get(FIELD)).toString();

	}



	/// Quitar un Long

	public String getLongToString( String FIELD )

	{

	    if (get(FIELD) == null)

	        return "";

        else

        {

            if (((Long)get(FIELD)).longValue()== -1)

                return "";

            else

                return ((Long)get(FIELD)).toString();

        }

    }





	/// Quitar un short

	public String getShortToString( String FIELD )

	{

	    if (get(FIELD) == null)

	        return "";

        else

            return ((Short)get(FIELD)).toString();

    }





	public static String formatearDoble( double valor)

	{

	    NumberFormat nf = NumberFormat.getInstance(Locale.GERMANY);

	    nf.setMaximumFractionDigits(2);



		return nf.format(valor);

	}





	/// Quitar un double

	public String getDoubleToString( String FIELD )	{
    if (get(FIELD) == null)
      return "";

    NumberFormat nf = NumberFormat.getInstance(Locale.GERMANY);
    nf.setMaximumFractionDigits(2);
    double doble = ((Double)get(FIELD)).doubleValue();
		return nf.format(doble);

	}





	/// Quitar un double

	public String getDoubleToStringWithMinimunDecimals( String FIELD )

	{

	    if (get(FIELD) == null)

	        return "";



	    NumberFormat nf = NumberFormat.getInstance(Locale.GERMANY);

	    nf.setMinimumFractionDigits(2);

	    nf.setMaximumFractionDigits(2);



		return nf.format(((Double)get(FIELD)).doubleValue());

	}





    public static void getStringCompNull(Registro r, StringBuffer stb, String cadena)

    {

        if(r.getString(cadena).trim().equals(""))

            stb.append("null,");

        else

        {

            stb.append("\"")

               .append(r.getString(cadena).toUpperCase())

               .append("\",");

       }

    }



    public void actualizarRegistroAux( HttpServletRequest request )

    {

       Enumeration nombres_parametros = request.getParameterNames();

       while (nombres_parametros.hasMoreElements())

       {

           String nombreParametro = (String)nombres_parametros.nextElement();

           if(nombreParametro.startsWith("_"))

           {

                this.setString(nombreParametro,request.getParameter(nombreParametro));

           }

       }

    }



    public void actualizarRegistroBD( HttpServletRequest request )

    {

       Enumeration nombresParametros = request.getParameterNames();

       while (nombresParametros.hasMoreElements())

       {

           String nombreParametro = (String)nombresParametros.nextElement();

           if(!nombreParametro.startsWith("_"))

           {

                this.setString(nombreParametro,request.getParameter(nombreParametro));

           }

       }

    }



    public static Vector crearVectorRegistros(HttpServletRequest request )

    {

        Vector aux=new Vector();

        Registro reg1=new Registro();

        Registro reg2=new Registro();

        reg1.actualizarRegistroAux(request);

        reg2.actualizarRegistroBD(request);

        aux.addElement(reg1);

        aux.addElement(reg2);

        return(aux);

    }





}

