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
public class SingleLongPrimaryKey implements PrimaryKey {

    private long pk=-1;

    public SingleLongPrimaryKey(long k) {
        pk = k;
    }
    public long getId() {
        return pk;
    }
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SingleLongPrimaryKey)) return false;

        final SingleLongPrimaryKey singleLongPrimaryKey = (SingleLongPrimaryKey) o;

        if (pk != singleLongPrimaryKey.pk) return false;

        return true;
    }
    public int hashCode() {
        return (int) (pk ^ (pk >>> 32));
    }
    public String toString() {
        return String.valueOf(pk);
    }//toString
}//class

/*______________________________EOF_________________________________*/