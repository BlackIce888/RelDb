/*
 * Copyright 2014 Rainer Stoermer
 */
package reldb.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import reldb.bdo.DatabaseSettings;
import reldb.bdo.OracleUser;

/**
 * Class to establish a connection to oracle.
 *
 *
 * @author Rainer Stoermer
 */
public class DbConnectionOracle implements DbConnection {

    private final String DB_DRIVER = "oracle.jdbc.OracleDriver";
    private Connection con;

    /**
     * constructor
     */
    protected DbConnectionOracle() {
    }

    /**
     *
     * @return the oracle sql.connection
     */
    @Override
    public Connection getConnection() {
        return con;
    }

    /**
     * Opens the Oracle-sql.connection, using OracleUser-Data and DatabaseSettings for connecting.
     *
     * @return the open sql.connection for the Oracle-DBMS
     */
    @Override
    public Connection openConnection() {
        OracleUser user = OracleUser.getInstance();
        return openConnection(user.getUsername(), user.getPassword(), DB_DRIVER, DatabaseSettings.ORACLE_DATABASE_URL);
    }

    /**
     * Resets the connection (tries to close it and then to re-open the connection)
     *
     * @return the oracle sql.connection
     */
    @Override
    public Connection resetConnection() {
        try {
            if (!con.isClosed() && con.isValid(0)) {
                closeConnection();
            }
        } catch (NullPointerException | SQLException ex) {
            Logger.getLogger(DbConnectionOracle.class.getName()).log(Level.SEVERE, "Error trying to close connection: " + ex.getMessage());
        }

        OracleUser user = OracleUser.getInstance();
        return openConnection(user.getUsername(), user.getPassword(), DB_DRIVER, DatabaseSettings.ORACLE_DATABASE_URL);
    }

    /**
     * opens the connection, used by parameterless openConnection()-Method
     *
     * @param username       oracle-username
     * @param password       oracle-password
     * @param dbDriver       oracle-JDBC-driver
     * @param dbServerString connection-String
     *
     * @return the successfully opened connection, or null if failed
     */
    private Connection openConnection(String username, String password, String dbDriver, String dbServerString) {

        // without username and/or password no connection possible
        if (username.equals("") || password.equals("")) {
            Logger.getLogger(DbConnectionOracle.class.getName()).log(Level.SEVERE, "Error: Trying to connect to database without userdata");
            return null;
        }

        // load JDBC-Driver
        try {
            Class.forName(dbDriver);
        } catch (ClassNotFoundException e) {
            Logger.getLogger(DbConnectionOracle.class.getName()).log(Level.SEVERE, "Error while loading JDBC-Driver: " + e.getMessage());
            return null;
        }

        // attempt to connect to dbms
        try {
            con = DriverManager.getConnection(dbServerString, username, password);
            con.setAutoCommit(true);
            con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            Logger.getLogger(DbConnectionOracle.class.getName()).log(Level.INFO, "Database connection established");
        } catch (NullPointerException | SQLException e) {
            // Connection failed
            Logger.getLogger(DbConnectionOracle.class.getName()).log(Level.SEVERE, "Error while trying to connect to database: " + e.getMessage());
            return null;
        }

        return con;
    }

    /**
     * Closes the connection
     */
    @Override
    public void closeConnection() {
        try {
            con.close();
            Logger.getLogger(DbConnectionOracle.class.getName()).log(Level.INFO, "Database connection closed");
        } catch (NullPointerException | SQLException e) {
            Logger.getLogger(DbConnectionOracle.class.getName()).log(Level.SEVERE, "Error trying to close database connection: " + e.getMessage());
        }
    }
}
