package es.altia.util;

/**
 *
 * @author oscar.rodriguez
 */
public interface DER
{

  // Constants.
  // ------------------------------------------------------------------------

  public static final int UNIVERSAL   = 0x00;
  public static final int APPLICATION = 0x40;
  public static final int CONTEXT     = 0x80;
  public static final int PRIVATE     = 0xC0;

  public static final int CONSTRUCTED = 0x20;

  public static final int ANY               = 0x00;
  public static final int BOOLEAN           = 0x01;
  public static final int INTEGER           = 0x02;
  public static final int BIT_STRING        = 0x03;
  public static final int OCTET_STRING      = 0x04;
  public static final int NULL              = 0x05;
  public static final int OBJECT_IDENTIFIER = 0x06;
  public static final int REAL              = 0x09;
  public static final int ENUMERATED        = 0x0a;
  public static final int RELATIVE_OID      = 0x0d;

  public static final int SEQUENCE = 0x10;
  public static final int SET      = 0x11;

  public static final Object CONSTRUCTED_VALUE = new Object();

  public static final int NUMERIC_STRING   = 0x12;
  public static final int PRINTABLE_STRING = 0x13;
  public static final int T61_STRING       = 0x14;
  public static final int VIDEOTEX_STRING  = 0x15;
  public static final int IA5_STRING       = 0x16;
  public static final int GRAPHIC_STRING   = 0x19;
  public static final int ISO646_STRING    = 0x1A;
  public static final int GENERAL_STRING   = 0x1B;

  public static final int UTF8_STRING      = 0x0C;
  public static final int UNIVERSAL_STRING = 0x1C;
  public static final int BMP_STRING       = 0x1E;

  public static final int UTC_TIME         = 0x17;
  public static final int GENERALIZED_TIME = 0x18;
}

