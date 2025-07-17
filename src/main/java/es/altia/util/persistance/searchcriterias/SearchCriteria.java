/*______________________________BOF_________________________________*/
package es.altia.util.persistance.searchcriterias;

import es.altia.util.persistance.daocommands.SQLFormatter;
import es.altia.util.persistance.daocommands.stdsql.StdSQLFormatter;

import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * @author
 * @version $\Date$ $\Revision$
 */
public class SearchCriteria {
    /*_______Constants______________________________________________*/
    protected static final String EMPTY_STR = "";

    /*_______Atributes______________________________________________*/
    protected SQLFormatter pSQLFormatter = null;
    protected String pFinalQueryString = null;
    private boolean expedienteHistorico;

    /*_______Operations_____________________________________________*/
    public SearchCriteria() {}

    public SearchCriteria(String finalQueryString) {
        pFinalQueryString = finalQueryString;
    }

	public int bind(PreparedStatement preparedStatement, int i)
		throws SQLException {
		return i;
	}//bind

	public String toSQLString() {
		return (pFinalQueryString!=null)?pFinalQueryString:EMPTY_STR;
	}//toSQLString
	
	public String toString() {
		return toSQLString();
	}//toString

    public String getSQLStringToPrepare() {
        return toSQLString();
    }//getSQLStringToPrepare

    public String getSQLStringToExecuteDirectly() {
        return toSQLString();
    }//getSQLStringToExecuteDirectly

    public SearchCriteria and(SearchCriteria other) {
        return new AndCriteria(this,other);
    }//and

    public SearchCriteria or(SearchCriteria other) {
        return new OrCriteria(this,other);
    }//or

    public SQLFormatter getSQLFormatter() {
        return (pSQLFormatter!=null)?pSQLFormatter:StdSQLFormatter.getInstance();
    }//getSQLFormatter
    public void setSQLFormatter(SQLFormatter SQLFormatter) {
        pSQLFormatter = SQLFormatter;
    }

    /**
     * @return the expedienteHistorico
     */
    public boolean isExpedienteHistorico() {
        return expedienteHistorico;
    }

    /**
     * @param expedienteHistorico the expedienteHistorico to set
     */
    public void setExpedienteHistorico(boolean expedienteHistorico) {
        this.expedienteHistorico = expedienteHistorico;
    }

}//class
/*______________________________EOF_________________________________*/
