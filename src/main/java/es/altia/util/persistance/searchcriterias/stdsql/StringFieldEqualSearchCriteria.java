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
import es.altia.util.persistance.searchcriterias.SearchCriteria;
import es.altia.util.persistance.*;
import es.altia.util.persistance.exceptions.*;
import es.altia.util.jdbc.*;


public class StringFieldEqualSearchCriteria extends SearchCriteria {
	
	private static final int ID = 2;
	private String pField = null;
	private String pTo = null;

    public StringFieldEqualSearchCriteria(String field, String to) {
        this.pField = field;
        this.pTo = to;
    }//constructor

	public StringFieldEqualSearchCriteria(String field) {
		this.pField = field;
	}//constructor
	
	public String getTo() {
		return pTo;
	}//getTo

	public void setTo(String to) {
		this.pTo = to;
	}//setTo

	
	public int bind(PreparedStatement preparedStatement, int i) 
	 throws SQLException {
		int result = i;

		preparedStatement.setString(result++, pTo);
		return result;
	}//bind
	
	public String toSQLString() {
		return " ("+pField+"=?) ";
	}//toSQLString
	
	public int getId() {
		return ID;
	}//getId
	
	public String toString() {
		return toSQLString();
	}//toString
	
}//class
