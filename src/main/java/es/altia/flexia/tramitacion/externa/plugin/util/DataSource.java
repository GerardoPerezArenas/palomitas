package es.altia.flexia.tramitacion.externa.plugin.util;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.dbcp.BasicDataSource;


/**
 * Clase encarga de recuperar una conexión a la base de datos
 */
public class DataSource {

    private String url;
    private String driver;
    private String username;
    private String password;
    // Valores por defecto
    private int maxActive   = 50;
    private int maxWait     = 5000;
    private int maxIdle     = 2;
    private int minIdle     = 0;
    private int initialSize = 10;

    /**
     * Constructor con la información básica necesaria para obtener una conexión a la base de datos
     * @param url
     * @param driver
     * @param username
     * @param password
     */
    public DataSource(String url,String driver,String username,String password){
        this.url      = url;
        this.driver   = driver;
        this.username = username;
        this.password = password;
    }


    /**
     * Obtiene una conexión a la base de datos
     * @return La conexión a la BBDD o null sino se ha podido obtener
     */
    public Connection getConnection(){
        Connection con = null;
        BasicDataSource ds = new BasicDataSource();
        try{
            ds.setUrl(url);
            ds.setDriverClassName(driver);
            ds.setUsername(username);
            ds.setPassword(password);
            ds.setMaxActive(maxActive);
            ds.setMaxWait(maxWait);
            ds.setMaxIdle(maxIdle);
            ds.setMinIdle(minIdle);
            ds.setInitialSize(initialSize);
            con = ds.getConnection();

        }catch(SQLException e){
            e.printStackTrace();
        }
        return con;
    }


    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the driver
     */
    public String getDriver() {
        return driver;
    }

    /**
     * @param driver the driver to set
     */
    public void setDriver(String driver) {
        this.driver = driver;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the maxActive
     */
    public int getMaxActive() {
        return maxActive;
    }

    /**
     * @param maxActive the maxActive to set
     */
    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    /**
     * @return the maxWait
     */
    public int getMaxWait() {
        return maxWait;
    }

    /**
     * @param maxWait the maxWait to set
     */
    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    /**
     * @return the maxIdle
     */
    public int getMaxIdle() {
        return maxIdle;
    }

    /**
     * @param maxIdle the maxIdle to set
     */
    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    /**
     * @return the minIdle
     */
    public int getMinIdle() {
        return minIdle;
    }

    /**
     * @param minIdle the minIdle to set
     */
    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    /**
     * @return the initialSize
     */
    public int getInitialSize() {
        return initialSize;
    }

    /**
     * @param initialSize the initialSize to set
     */
    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }
}
