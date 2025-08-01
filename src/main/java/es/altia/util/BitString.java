/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.util;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Immutable representation of a bit string, which is equivalent to a
 * byte array except some number of the rightmost bits are ignored. For
 * example, this could be the bit string:
 *
 * <pre>   00010101 11101101 11010xxx</pre>
 *
 * <p>Where the "xxx" represents three bits that should be ignored, and
 * can have any value.
 *
 * @author Casey Marshall (rsdio@metastatic.org)
 */
public class BitString implements Cloneable, Comparable, java.io.Serializable
{

  // Fields.
  // ------------------------------------------------------------------------

  /** The bits themselves. */
  private final byte[] bytes;

  /**
   * The exportable byte array. This array has the ignored bits
   * removed.
   */
  private transient byte[] externBytes;

  /** The number of bits ignored at the end of the byte array. */
  private final int ignoredBits;

  /** This bit string as a boolean array. */
  private transient boolean[] boolVal;

  // Constructors.
  // ------------------------------------------------------------------------

  /**
   * Create a new bit string, shifting the given byte array if needed.
   *
   * @param bytes The byte array holding the bit string.
   * @param ignoredBits The number of bits to ignore.
   * @param doShift Pass true in this parameter if the byte array has
   * not yet been shifted left by <i>ignoredBits</i>.
   * @throws IllegalArgumentException If <i>ignoredBits</i> is negative
   * or greater than 7.
   * @throws NullPointerException If <i>bytes</i> is null.
   */
  public BitString(byte[] bytes, int ignoredBits, boolean doShift)
  {
    this(bytes, 0, bytes.length, ignoredBits, doShift);
  }

  /**
   * Create a new bit string, shifting the given byte array if needed.
   *
   * @param bytes The byte array holding the bit string.
   * @param offset The offset where the meaningful bytes begin.
   * @param length The number of meaningful bytes.
   * @param ignoredBits The number of bits to ignore.
   * @param doShift Pass true in this parameter if the byte array has
   * not yet been shifted left by <i>ignoredBits</i>.
   * @throws IllegalArgumentException If <i>ignoredBits</i> is negative
   * or greater than 7.
   * @throws NullPointerException If <i>bytes</i> is null.
   */
  public BitString(byte[] bytes, int offset, int length,
                   int ignoredBits, boolean doShift)
  {
    if (ignoredBits < 0 || ignoredBits > 7)
      throw new IllegalArgumentException();
    if (bytes == null)
      throw new NullPointerException();
    if (doShift && ignoredBits > 0)
      {
        this.externBytes = new byte[length];
        System.arraycopy(bytes, offset, externBytes, 0, length);
        this.bytes = new BigInteger(externBytes).shiftLeft(ignoredBits)
                       .toByteArray();
      }
    else
      {
        this.bytes = new byte[length];
        System.arraycopy(bytes, offset, this.bytes, 0, length);
      }
    this.ignoredBits = ignoredBits;
  }

  /**
   * Create a new bit string.
   *
   * @param bytes The byte array holding the bit string.
   * @param offset The offset where the meaningful bytes begin.
   * @param length The number of meaningful bytes.
   * @param ignoredBits The number of bits to ignore.
   * @throws IllegalArgumentException If <i>ignoredBits</i> is negative
   * or greater than 7.
   * @throws NullPointerException If <i>bytes</i> is null.
   */
  public BitString(byte[] bytes, int offset, int length, int ignoredBits)
  {
    this(bytes, offset, length, ignoredBits, false);
  }

  /**
   * Create a new bit string.
   *
   * @param bytes The byte array holding the bit string.
   * @param ignoredBits The number of bits to ignore.
   * @throws IllegalArgumentException If <i>ignoredBits</i> is negative
   * or greater than 7.
   * @throws NullPointerException If <i>bytes</i> is null.
   */
  public BitString(byte[] bytes, int ignoredBits)
  {
    this(bytes, 0, bytes.length, ignoredBits, false);
  }

  /**
   * Create a new bit string.
   *
   * @param bytes The byte array holding the bit string.
   * @param offset The offset where the meaningful bytes begin.
   * @param length The number of meaningful bytes.
   * @throws NullPointerException If <i>bytes</i> is null.
   */
  public BitString(byte[] bytes, int offset, int length)
  {
    this(bytes, offset, length, 0, false);
  }

  /**
   * Create a new bit string.
   *
   * @param bytes The byte array holding the bit string.
   * @throws NullPointerException If <i>bytes</i> is null.
   */
  public BitString(byte[] bytes)
  {
    this(bytes, 0, bytes.length, 0, false);
  }

  // Instance methods.
  // ------------------------------------------------------------------------

  /**
   * Return this bit string as a byte array, with the ignored bits
   * trimmed off. The byte array is cloned every time this method is
   * called to prevent modification.
   *
   * @return The trimmed byte array.
   */
  public byte[] toByteArray()
  {
    if (ignoredBits == 0)
      return (byte[]) bytes.clone();
    if (externBytes == null)
      externBytes = new BigInteger(bytes).shiftRight(ignoredBits).toByteArray();
    return (byte[]) externBytes.clone();
  }

  /**
   * Returns this bit string as a byte array, with the ignored bits
   * present. The byte array is cloned every time this method is
   * called to prevent modification.
   *
   * @return The byte array.
   */
  public byte[] getShiftedByteArray()
  {
    return (byte[]) bytes.clone();
  }

  /**
   * Returns the number of ignored bits.
   *
   * @return The number of ignored bits.
   */
  public int getIgnoredBits()
  {
    return ignoredBits;
  }

  /**
   * Returns the size, in bits, of this bit string.
   *
   * @return The size of this bit string.
   */
  public int size()
  {
    return (bytes.length << 3) - ignoredBits;
  }

  /**
   * Return this bit string as a boolean array. The value returned is of
   * size {@link #size()}, and each <code>true</code> value
   * corresponding to each "1" in this bit string. The boolean array is
   * cloned before it is returned.
   *
   * @return The boolean array.
   */
  public boolean[] toBooleanArray()
  {
    if (boolVal == null)
      {
        boolVal = new boolean[size()];
        for (int i = 0, j = 7, k = 0; i < boolVal.length; i++)
          {
            boolVal[i] = (bytes[k] & 1 << j--) != 0;
            if (j < 0)
              {
                j = 7;
                k++;
              }
          }
      }
    return (boolean[]) boolVal.clone();
  }

  public Object clone()
  {
    try
      {
        return super.clone();
      }
    catch (CloneNotSupportedException cce)
      {
        throw new InternalError(cce.getMessage());
      }
  }

  public int compareTo(Object o)
  {
    BitString that = (BitString) o;
    if (this.equals(that))
      return 0;
    if (this.bytes.length != that.bytes.length)
      return (this.bytes.length < that.bytes.length) ? -1 : 1;
    if (this.ignoredBits != that.ignoredBits)
      return (this.ignoredBits < that.ignoredBits) ? -1 : 1;
    for (int i = 0; i < this.bytes.length; i++)
      if (this.bytes[i] != that.bytes[i])
        return (this.bytes[i] < that.bytes[i]) ? -1 : 1;
    return 0; // not reached.
  }

  public boolean equals(Object o)
  {
    if (this == o)
      return true;
    if (!(o instanceof BitString))
      return false;
    BitString that = (BitString) o;
    // True for cloned instances.
    if (this.bytes == that.bytes && this.ignoredBits == that.ignoredBits)
      return true;
    if (this.ignoredBits == that.ignoredBits)
      return Arrays.equals(this.bytes, that.bytes);
    return false;
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    for (int i = 0, j = 7, k = 0; i < size(); i++)
      {
        sb.append((bytes[k] & 1 << j) != 0 ? "1" : "0");
        j--;
        if (j < 0)
          {
            j = 7;
            k++;
          }
      }
    return sb.toString();
  }
}
