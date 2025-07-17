/*______________________________BOF_________________________________*/
package es.altia.util.persistance.impl;

import es.altia.util.persistance.PrimaryKey;

/**
 * @author
 */
public class StringPrimaryKey implements PrimaryKey {
    private String pPK = null;

    public StringPrimaryKey(String PK) {
        pPK = PK;
    }

    public String getPK() {
        return pPK;
    }
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StringPrimaryKey)) return false;

        final StringPrimaryKey stringPrimaryKey = (StringPrimaryKey) o;

        if (pPK != null ? !pPK.equals(stringPrimaryKey.pPK) : stringPrimaryKey.pPK != null) return false;

        return true;
    }
    public int hashCode() {
        return (pPK != null ? pPK.hashCode() : 0);
    }

    public String toString(){
        return pPK;
    }//toString
}//class
/*______________________________EOF_________________________________*/