/*
 *  Copyright 2014 Rainer Stoermer
 */
package reldb.bdo;

/**
 * Simple BDO to change server-data without touching the data-layer-classes.
 *
 * @author Rainer Stoermer
 */
public class DatabaseSettings {

    /**
     * final for Oracle-Server-JDBC-String
     */
    public static final String ORACLE_DATABASE_URL = "jdbc:oracle:thin:@dbvm02.iai.uni-bonn.de:1521:dbvm02";

    /**
     * final for Oracle-schemaname Be careful, ORACLE_SCHEMA must be always upper case!
     */
    public static final String ORACLE_SCHEMA = "IMDB";
}
