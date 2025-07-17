package es.altia.util.persistance.searchcriterias.stdsql;

import es.altia.util.persistance.searchcriterias.SearchCriteria;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class IntFieldInSearchCriteria extends SearchCriteria {
	private String pField = null;
	private int[] pIn;
    
	public IntFieldInSearchCriteria(String field) {
		this.pField = field;
	}//constructor
	
	public IntFieldInSearchCriteria(String field, int[] inputSet) {
		this.pField = field;
		this.pIn = inputSet;
	}//constructor

	public int[] getInputSet() {
		return pIn;
	}//getInputSet
	public void setInputSet(int[] to) {
		this.pIn = to;
	}//setInputSet

	public int bind(PreparedStatement preparedStatement, int i)
	 throws SQLException {
		int result = i;
		return result;
	}//bind
	
	public String toSQLString() {
		String sql=" ("+pField+" IN (";
		int i = 0;
		int l = pIn.length;
		while ( i < (l-1) ) {
			sql=sql+pIn[i]+",";
			i++;
		}//while
		if (l > 0) sql = sql + pIn[l-1] + ") )";
		return sql;
	}//toSQLString
	
	public String toString() {
		return toSQLString();
	}//toString
	
}//class
