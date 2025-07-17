package es.altia.util;

/**
 *
 * @author oscar.rodriguez
 */
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DERValue implements DER
{

  // Fields.
  // ------------------------------------------------------------------------

  private final int tagClass;

  private final boolean constructed;

  private final int tag;

  private int length;

  private final Object value;

  private byte[] encoded;

  // Constructor.
  // ------------------------------------------------------------------------

  public DERValue(int tag, int length, Object value, byte[] encoded)
  {
    tagClass = tag & 0xC0;
    this.tag = tag & 0x1F;
    constructed = (tag & CONSTRUCTED) == CONSTRUCTED;
    this.length = length;
    this.value = value;
    if (encoded != null)
      this.encoded = (byte[]) encoded.clone();
  }

  public DERValue(int tag, Object value)
  {
    this(tag, 0, value, null);
  }

  public DERValue(byte[] encoded)
  {
     this(0,encoded.length,null,encoded);
  }

  // Instance methods.
  // ------------------------------------------------------------------------
  public int getExternalTag()
  {
    return tagClass | tag | (constructed ? 0x20 : 0x00);
  }

  public int getTag()
  {
    return tag;
  }

  public int getTagClass()
  {
    return tagClass;
  }

  public boolean isConstructed()
  {
    return constructed;
  }

  public int getLength()
  {
    if (encoded == null)
      {
        try
          {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            length = DERWriter.write(out, this);
            encoded = out.toByteArray();
          }
        catch (IOException ioe)
          {
            encoded = new byte[0];
          }
      }
    return length;
  }

  public Object getValue()
  {
    return value;
  }

  public byte[] getEncoded()
  {
    if (encoded == null)
      {
        try
          {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            length = DERWriter.write(out, this);
            encoded = out.toByteArray();
          }
        catch (IOException ioe)
          {
            encoded = new byte[0];
          }
      }
    return (byte[]) encoded.clone();
  }

  public int getEncodedLength()
  {
    if (encoded == null)
      {
        try
          {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            length = DERWriter.write(out, this);
            encoded = out.toByteArray();
          }
        catch (IOException ioe)
          {
            encoded = new byte[0];
          }
      }
    return encoded.length;
  }

  public String toString()
  {
    return "DERValue [ tag=" + tag + ", class=" + tagClass + ", constructed="
      + constructed + ", value=" + value + " ]";
  }
}

