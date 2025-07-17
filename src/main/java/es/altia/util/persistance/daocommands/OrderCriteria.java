/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands;

import es.altia.util.commons.BasicTypesOperations;

/**
  * @version      $\Date$ $\Revision$
  *
  **/
public class OrderCriteria {
    /*_______Constants______________________________________________*/
    private static final String ASC = " ASC ";
    private static final String DESC = " DESC ";

    /*_______Attributes_____________________________________________*/
	protected String sql=null;

    /*_______Operations_____________________________________________*/
    /**
     * @param attributeName the attribute name
     */
	public OrderCriteria(String attributeName) {
		sql=attributeName;
	}//constructor

    /**
     * @param attributeName the attribute name
     * @param asc (true -> ASC, false -> DESC)
     */
    public OrderCriteria(String attributeName, boolean asc) {
        sql=attributeName+(asc?ASC:DESC);
    }//constructor

    public OrderCriteria(String[] attributeNames) {
        sql = BasicTypesOperations.toString(attributeNames,",");
    }//constructor

	public String toSQLString(){
		return sql;
	}//toSQLString
	
	public String toString(){
		return toSQLString();
	}//toString
	
}//class
/*______________________________EOF_________________________________*/
