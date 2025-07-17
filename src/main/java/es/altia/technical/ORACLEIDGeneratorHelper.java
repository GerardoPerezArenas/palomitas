/* Natxo Technology */

package es.altia.technical;

import es.altia.common.service.jdbc.JDBCWrapper;
import es.altia.common.exception.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ORACLEIDGeneratorHelper {

    protected static Log m_Log =
            LogFactory.getLog(ORACLEIDGeneratorHelper.class.getName());

    public ORACLEIDGeneratorHelper() {

    }

    public static long getNextId(String secuencia) throws TechnicalException {
        long seqNum = 0;
        String sentencia = "SELECT " + secuencia + ".NEXTVAL FROM DUAL";
        JDBCWrapper sqlExec = new JDBCWrapper();
        m_Log.debug("ENTRANDO....");
        try {
            m_Log.debug("ENTRANDO....");
            sqlExec.execute(sentencia);
            if (sqlExec.next()) {
                if (m_Log.isDebugEnabled()) m_Log.debug("Obteniendo valor de secuencia " + sqlExec.getLong("NEXTVAL"));
                seqNum = (new Long(sqlExec.getLong("NEXTVAL"))).longValue();
            }
        } catch (Exception pe) {
            throw new TechnicalException("SQL Exception while updating sequence : \n" + pe.getMessage());
        } finally { sqlExec.close(); }
        return seqNum;
    }
}
