/*
 * Copyright 2014 Rainer Stoermer
 */
package reldb.app;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import reldb.bdo.OracleUser;
import reldb.data.DbConnection;
import reldb.data.DbConnectionSingletonFactory;
import reldb.pres.ImdbSearch;

/**
 * Realizes the login-service in the application logic-layer. manages login and logout for both postgres and oracle
 * using the appropriate classes of the data-layer.
 *
 * @author Rainer Stoermer
 */
public class LoginApp {

    private DbConnectionSingletonFactory dbConFactory;
    private DbConnection dbConOracle;

    /**
     * Performs login to Oracle-DBMS using data layer and singleton-BDOs OracleUser and DatabaseData
     *
     * @return true if login successful, false if not
     */
    public boolean oracleUserLogin() {
        // use the SingletonFactory to get the one and only DbConnection-Instance
        dbConFactory = DbConnectionSingletonFactory.getDbConnectionSingletonFactory();
        dbConOracle = dbConFactory.getDbConnection("oracle");
        dbConOracle.openConnection();

        try {
            // check success of openConnection -> connection must be valid...
            if (!dbConOracle.getConnection().isClosed() && dbConOracle.getConnection().isValid(0)) {
                Logger.getLogger(ImdbSearch.class.getName()).log(Level.INFO, "Oracle-Login successful for user {0}", OracleUser.getInstance().getUsername());
                return true;
            } else {
                Logger.getLogger(ImdbSearch.class.getName()).log(Level.SEVERE, "Oracle-Login failed for user {0}", OracleUser.getInstance().getUsername());
                return false;
            }
        } catch (NullPointerException | SQLException ex) {
            Logger.getLogger(ImdbSearch.class.getName()).log(Level.SEVERE, "Error opening oracle connection, connection invalid: " + ex.getMessage());
        }
        return false;
    }

    /**
     * Logout the oracle-User (close connection and clear oracle-user/password).
     */
    public void oracleUserLogout() {
        // use SingletonFactory to get appropriate DbConnection...
        dbConFactory = DbConnectionSingletonFactory.getDbConnectionSingletonFactory();
        dbConOracle = dbConFactory.getDbConnection("oracle");

        try {
            if (dbConOracle.getConnection().isValid(0)) {
                dbConOracle.closeConnection();
            }
            Logger.getLogger(ImdbSearch.class.getName()).log(Level.INFO, "Oracle-Logout successful for user {0}", OracleUser.getInstance().getUsername());
            OracleUser.getInstance().clearUser();
        } catch (NullPointerException | SQLException ex) {
            Logger.getLogger(ImdbSearch.class.getName()).log(Level.SEVERE, "Error closing oracle connection, connection invalid: " + ex.getMessage());
        }
    }
}
