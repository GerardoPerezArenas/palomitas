package es.altia.util.persistance.searchcriterias.stdsql;

import es.altia.util.persistance.searchcriterias.SearchCriteria;

import java.sql.PreparedStatement;
import java.sql.SQLException;


public class FieldBetweenSearchCriteria extends SearchCriteria {
	private String pField = null;
	
	private String pFrom = null;
	private String pTo = null;



	public FieldBetweenSearchCriteria(String field) {
		this.pField = field;
	}//constructor
	
	public String getFrom() {
		return pFrom;
	}//getFrom

	public void setFrom(String from) {
		this.pFrom = from.trim().toLowerCase();
	}//setFrom

	public String getTo() {
		return pTo;
	}//getTo

	public void setTo(String to) {
		this.pTo = to.trim().toLowerCase();
	}//setTo

	
	public int bind(PreparedStatement preparedStatement, int i) 
	 throws SQLException {
		int result = i;

		preparedStatement.setString(result++, pFrom);
		preparedStatement.setString(result++, pTo);
		return result;
	}//bind
	
	public String toSQLString() {
		return "( (LOWER("+pField+") >= ?) AND (LOWER("+pField+") <= ?) )";
	}//toSQLString
	
	public String toString() {
		return toSQLString();
	}//toString
	
}//class
