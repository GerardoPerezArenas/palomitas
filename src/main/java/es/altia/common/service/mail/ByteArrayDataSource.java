package es.altia.common.service.mail;

import java.io.*;

import javax.activation.*;

/**
* Wird benötigt für das versenden von MIME-Messages.
*
*/
public class ByteArrayDataSource implements DataSource
{
  /**
  * Die Daten des ByteArrayDataSource.
  */
  private byte[] data;

  /**
  * Der Content-Type des ByteArrayDataSource
  */
  private String type;

  /**
  * Der Name des ByteArrayDataSource
  */
  private String name;

  /**
  * Create a datasource from an input stream
  * @param is Inputstream to set data to.
  * @param type content-type of the datasource.
  */
  public ByteArrayDataSource(InputStream is, String type)
  {
    this(is, type, null);
  }

  /**
  * Create a datasource from an input stream
  * @param is Inputstream to set data to.
  * @param type content-type of the datasource.
  * @param name name of the datasource.
  */
  public ByteArrayDataSource(InputStream is, String type, String name)
  {
    this.type = type;
    if (name != null)
    {
      this.name = name;
    }
    try
    {
      BufferedInputStream in = new BufferedInputStream(is);
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      int ch;

      byte[] buf = new byte[1024];
      int len;

      while ((len = in.read(buf)) > 0)
      {
        os.write(buf, 0, len);
      }
      data = os.toByteArray();
    }
    catch (IOException ioex)
    {
    }
  }

  /**
  * Create a datasource from a byte array
  * @param data data to be set.
  * @param type content-type of the datasource.
  */
  public ByteArrayDataSource(byte[] data, String type)
  {
    this(data, type, null);
  }

  /**
  * Create a datasource from a byte array
  * @param data data to be set.
  * @param type content-type of the datasource.
  * @param name name of the datasource.
  */
  public ByteArrayDataSource(byte[] data, String type, String name)
  {
    this.data = data;
    this.type = type;
    if (name != null)
    {
      this.name = name;
    }
  }

  /**
  * Create a datasource from a String
  * @param data data to be set.
  * @param type content-type of the datasource.
  */
  public ByteArrayDataSource(String data, String type)
  {
    this(data, type, null);
  }

  /**
  * Create a datasource from a String
  * @param data data to be set.
  * @param type content-type of the datasource.
  * @param name name of the datasource.
  */
  public ByteArrayDataSource(String data, String type, String name)
  {
    try
    {
      // Assumption that the string contains only ascii
      // characters ! Else just pass in a charset into this
      // constructor and use it in getBytes()
      this.data = data.getBytes("iso-8859-1");
    }
    catch (UnsupportedEncodingException uex)
    {
    }
    this.type = type;
    if (name != null)
    {
      this.name = name;
    }
  }

  /**
  * Liefert die Daten des Datasource als InputStream.
  * @return die Daten des Datasource als InputStream.
  */
  public InputStream getInputStream() throws IOException
  {
    if (data == null) throw new IOException("no data");
    return new ByteArrayInputStream(data);
  }

  /**
  *
  */
  public OutputStream getOutputStream() throws IOException
  {
    throw new IOException("cannot do this");
  }

  /**
  * Liefert den ContentType des Datasource.
  * @return den ContentType des Datasource
  */
  public String getContentType()
  {
    return type;
  }

  /**
  * Liefert den Namen des Datasource.
  * @return Name des Datasource.
  */
  public String getName()
  {
    return name;
  }
}

