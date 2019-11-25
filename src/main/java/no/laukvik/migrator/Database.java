package no.laukvik.migrator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {
    private String url;
    private String driver;
    private String user;
    private String password;


    public Database(){
    }

    public Database url(String url){
        this.url = url;
        return this;
    }

    public Database driver(String driver) throws ClassNotFoundException {
        this.driver = driver;
        Class.forName(driver);
        return this;
    }

    public Database username(String user){
        this.user = user;
        return this;
    }

    public Database password(String password){
        this.password = password;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public String getDriver() {
        return driver;
    }

    public Connection getConnection() throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", password);
        return DriverManager.getConnection(url, props);
    }

}
