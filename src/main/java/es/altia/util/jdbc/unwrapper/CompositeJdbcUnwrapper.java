/*______________________________BOF_________________________________*/
package es.altia.util.jdbc.unwrapper;

import es.altia.util.collections.CollectionsFactory;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.Iterator;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @version $\Date$ $\Revision$
 */
public class CompositeJdbcUnwrapper implements JdbcUnwrapper {
    /*_______Constants______________________________________________*/

    /*_______Atributes______________________________________________*/
    protected Collection elements;

    /*_______Operations_____________________________________________*/
    public CompositeJdbcUnwrapper(Collection elements) {
        this.elements = elements;
    }

    public CompositeJdbcUnwrapper() {
        this.elements = CollectionsFactory.getInstance().newSet();
    }

    public CompositeJdbcUnwrapper addUnwrapper(JdbcUnwrapper u) {
        this.elements.add(u);
        return this;
    }


    /*_______Operations_____________________________________________*/
    public Connection unwrap(Connection conn) {
        Connection result = conn;
        boolean stop = false;
        do {
            boolean allTheSame = true;
            for (Iterator iterator = elements.iterator(); (iterator.hasNext() && allTheSame);) {
                JdbcUnwrapper jdbcUnwrapper = (JdbcUnwrapper) iterator.next();
                final Connection unwrapped = jdbcUnwrapper.unwrap(result);
                allTheSame = (unwrapped == result);
            }//for
            stop = allTheSame;
        } while (!stop);
        return result;
    }//unwrap

    public DataSource unwrap(DataSource ds) {
        DataSource result = ds;
        boolean stop = false;
        do {
            boolean allTheSame = true;
            for (Iterator iterator = elements.iterator(); (iterator.hasNext() && allTheSame);) {
                JdbcUnwrapper jdbcUnwrapper = (JdbcUnwrapper) iterator.next();
                final DataSource unwrapped = jdbcUnwrapper.unwrap(result);
                allTheSame = (unwrapped == result);
            }//for
            stop = allTheSame;
        } while (!stop);
        return result;
    }

    public Statement unwrap(Statement st) {
        Statement result = st;
        boolean stop = false;
        do {
            boolean allTheSame = true;
            for (Iterator iterator = elements.iterator(); (iterator.hasNext() && allTheSame);) {
                JdbcUnwrapper jdbcUnwrapper = (JdbcUnwrapper) iterator.next();
                final Statement unwrapped = jdbcUnwrapper.unwrap(result);
                allTheSame = (unwrapped == result);
            }//for
            stop = allTheSame;
        } while (!stop);
        return result;
    }

    public PreparedStatement unwrap(PreparedStatement ps) {
        PreparedStatement result = ps;
        boolean stop = false;
        do {
            boolean allTheSame = true;
            for (Iterator iterator = elements.iterator(); (iterator.hasNext() && allTheSame);) {
                JdbcUnwrapper jdbcUnwrapper = (JdbcUnwrapper) iterator.next();
                final PreparedStatement unwrapped = jdbcUnwrapper.unwrap(result);
                allTheSame = (unwrapped == result);
            }//for
            stop = allTheSame;
        } while (!stop);
        return result;
    }

    public ResultSet unwrap(ResultSet rs) {
        ResultSet result = rs;
        boolean stop = false;
        do {
            boolean allTheSame = true;
            for (Iterator iterator = elements.iterator(); (iterator.hasNext() && allTheSame);) {
                JdbcUnwrapper jdbcUnwrapper = (JdbcUnwrapper) iterator.next();
                final ResultSet unwrapped = jdbcUnwrapper.unwrap(result);
                allTheSame = (unwrapped == result);
            }//for
            stop = allTheSame;
        } while (!stop);
        return result;
    }
}//class
/*______________________________EOF_________________________________*/
