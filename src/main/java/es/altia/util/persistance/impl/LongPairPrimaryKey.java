/*______________________________BOF_________________________________*/
package es.altia.util.persistance.impl;

import es.altia.util.persistance.PrimaryKey;
import es.altia.util.persistance.PrimaryKey;

import java.util.Iterator;
import java.util.Collections;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Calendar;


/**
 * @author
 */
public class LongPairPrimaryKey implements PrimaryKey {

    private long pk=-1;
    private long secondPk=-1;

    public LongPairPrimaryKey(long k, long secondKey) {
        pk = k;
        secondPk = secondKey;
    }

    public long getId() {
        return pk;
    }

    public long getSecondId() {
        return secondPk;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LongPairPrimaryKey)) return false;

        final LongPairPrimaryKey singleLongPrimaryKey = (LongPairPrimaryKey) o;

        return ((pk == singleLongPrimaryKey.pk) && ((secondPk == singleLongPrimaryKey.secondPk)));
    }
    public int hashCode() {
        return (int) (pk ^ (pk >>> 32)) + (int)(secondPk ^ (secondPk >>> 32));
    }
    public String toString() {
        return String.valueOf(pk)+","+String.valueOf(secondPk);
    }//toString
}//class

/*______________________________EOF_________________________________*/