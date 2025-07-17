package es.altia.util.persistance.searchcriterias.stdsql;

import es.altia.util.jdbc.JdbcOperations;
import es.altia.util.persistance.daocommands.SQLFormatter;
import es.altia.util.persistance.daocommands.stdsql.StdSQLFormatter;
import es.altia.util.persistance.searchcriterias.SearchCriteria;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;


public class DateFieldBetweenSearchCriteria extends SearchCriteria {
	private String pField = null;
	private Calendar pFrom = null;
	private Calendar pTo = null;

	public DateFieldBetweenSearchCriteria(String field) {
		this.pField = field;
	}//constructor
	
	public Calendar getFrom() {
		return pFrom;
	}//getFrom
	public void setFrom(Calendar from) {
		this.pFrom = from;
	}//setFrom

	public Calendar getTo() {
		return pTo;
	}//getTo
	public void setTo(Calendar to) {
		this.pTo = to;
	}//setTo

	public int bind(PreparedStatement preparedStatement, int i)
	 throws SQLException {
		int result = i;
		if (pFrom!=null) result = JdbcOperations.bindCalendarToStatement(pFrom, preparedStatement, result);
		if (pTo!=null) result = JdbcOperations.bindCalendarToStatement(pTo, preparedStatement, result);
		return result;
	}//bind


	public String toSQLString() {
        StringBuffer buff = new StringBuffer("( ");
        if ( (pFrom!=null) && (pTo!=null) ) {
            buff.append(pField).append(" BETWEEN (?) AND (?)");
        } else if (pFrom!=null) {
            buff.append(pField).append(" > (?)");
        } else if (pTo!=null) {
            buff.append(pField).append(" < (?)");
        } else {
            buff.append("1=1");
        }//if
        buff.append(" )");
        return buff.toString();
	}//toSQLString
	
	public String toString() {
		return toSQLString();
	}//toString
	
    public String getSQLStringToExecuteDirectly() {
        SQLFormatter formatter = getSQLFormatter();
        StringBuffer buff = new StringBuffer("( ");
        if ( (pFrom!=null) && (pTo!=null) ) {
            buff.append(pField).append(" BETWEEN ");
            buff.append("(").append(formatter.formatValue(pFrom,StdSQLFormatter.TYPE_CALENDAR_DATE)).append(")");
            buff.append(" AND ");
            buff.append("(").append(formatter.formatValue(pTo,StdSQLFormatter.TYPE_CALENDAR_DATE)).append(")");
        } else if (pFrom!=null) {
            buff.append(pField).append(" > ");
            buff.append(formatter.formatValue(pFrom,StdSQLFormatter.TYPE_CALENDAR_DATE));
        } else if (pTo!=null) {
            buff.append(pField).append(" < ");
            buff.append(formatter.formatValue(pTo,StdSQLFormatter.TYPE_CALENDAR_DATE));
        } else {
            buff.append("1=1");
        }//if
        buff.append(" )");
        return buff.toString();
    }//getSQLStringToExecuteDirectly

}//class
