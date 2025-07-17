/*______________________________BOF_________________________________*/
package es.altia.util.persistance.searchcriterias.stdsql;



import es.altia.util.jdbc.JdbcOperations;
import es.altia.util.persistance.searchcriterias.SearchCriteria;

import java.util.Iterator;
import java.util.Collections;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Calendar;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * @author
 */
public class DateFieldYearEqualSearchCriteria extends SearchCriteria {

	private String pField = null;
	private Integer pYear = null;

	public DateFieldYearEqualSearchCriteria(String field) {
		this.pField = field;
	}//constructor

	public Integer getYear() {
		return pYear;
	}//getYear

	public void setYear(Integer to) {
		this.pYear = to;
	}//setYear


	public int bind(PreparedStatement preparedStatement, int i)
	 throws SQLException {
		int result = i;

		result = JdbcOperations.bindIntegerToStatement(this.getYear(), preparedStatement, result);
		return result;
	}//bind




	public String toSQLString() {
		return " ( YEAR("+pField+") = ? ) ";
	}//toSQLString

	public String toString() {
		return toSQLString();
	}//toString

}//class

/*______________________________EOF_________________________________*/