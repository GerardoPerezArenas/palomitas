/*______________________________BOF_________________________________*/
package es.altia.util.security.certificates.exceptions;

import java.security.GeneralSecurityException;
import java.util.Date;

/**
 * @author
 * @version $\Date$ $\Revision$
 */
public class RevokedCertificateException extends GeneralSecurityException {
    /*_______Constants______________________________________________*/
    public static final byte REASON_NOT_SPECIFIED = 0;
    public static final byte REASON_BROKEN_KEY = 1;
    public static final byte REASON_BROKEN_CA = 2;
    public static final byte REASON_CHANGED_SUBJECT_INFO = 3;
    public static final byte REASON_CHANGED_CERTIFICATE = 4;
    public static final byte REASON_CHANGED_TARGET = 5;
    public static final byte REASON_SUSPENDED_TEMPORALY = 6;
    public static final byte REASON_MUST_BE_REMOVED_FROM_ANOTHER_CRL = 8;
    public static final byte REASON_PRIVILEDGE_RETIRED = 9;
    public static final byte REASON_BROKEN_AA = 10;

    /*_______Atributes______________________________________________*/
    byte pReasonCode = -1;
    Date pRevocationDate = null;

    /*_______Operations_____________________________________________*/
    public RevokedCertificateException(byte reasonCode, Date revocationDate) {
        super("Revoked certificate on "+revocationDate+" due to reason "+reasonCode);
        pReasonCode = reasonCode;
        pRevocationDate = revocationDate;
    }//constructor

    public byte getReasonCode() {
        return pReasonCode;
    }

    public Date getRevocationDate() {
        return pRevocationDate;
    }
}//class
/*______________________________EOF_________________________________*/
