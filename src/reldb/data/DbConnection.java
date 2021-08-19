/*
 *  Copyright 2014 Rainer Stoermer
 */
package reldb.data;

import java.sql.Connection;

/**
 * Interface for a DbConnection
 *
 * @author Rainer Stoermer
 */
public interface DbConnection {

    Connection openConnection();

    Connection getConnection();

    void closeConnection();

    Connection resetConnection();
}
