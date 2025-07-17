package es.altia.util.persistance.searchcriterias.stdsql;

import java.sql.Connection;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Collection;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Locale;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.altia.util.exceptions.*;
import es.altia.util.persistance.*;
import es.altia.util.persistance.searchcriterias.SearchCriteria;
import es.altia.util.persistance.exceptions.*;
import es.altia.util.jdbc.*;

/**
 * @deprecated Use EqualSearchCriteria instead
 */
public class NumericFieldEqualSearchCriteria extends SearchCriteria {
	
	private String pField = null;
	
	private Long pToLong = null;
	private Integer pToInteger = null;
	private int pType = 0; // 0 -> Integer, 1 -> Long



	public NumericFieldEqualSearchCriteria(String field) {
		this.pField = field;
	}//constructor
	
	public Long getToLong() {
		return pToLong;
	}//getToLong

	public void setToLong(Long to) {
		this.pToLong = to;
		this.pToInteger = null;
		this.pType = 1;
	}//setToLong

	public Integer getToInteger() {
		return pToInteger;
	}//getToInteger

	public void setToInteger(Integer to) {
		this.pToInteger = to;
		this.pToLong = null;
		this.pType = 0;
	}//setToInteger

	
	public int bind(PreparedStatement preparedStatement, int i) 
	 throws SQLException {
		int result = i;

		if (pType == 0 ) {
			result = JdbcOperations.bindIntegerToStatement(this.getToInteger(), preparedStatement, result);
		} else if (pType == 1 ) {
			result = JdbcOperations.bindLongToStatement(this.getToLong(), preparedStatement, result);
		}//if
		return result;
	}//bind
	



	public String toSQLString() {
		return " ("+pField+"=?) ";
	}//toSQLString
	
	public String toString() {
		return toSQLString();
	}//toString
	
}//class
