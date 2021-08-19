/*
 *  Copyright 2014 Rainer Stoermer
 */
package reldb.data;

/**
 * SingletonFactory to use the DbConnection-Classes as a singleton.
 *
 * @author Rainer Stoermer
 */
public class DbConnectionSingletonFactory {

    static private DbConnectionSingletonFactory dbFactory;

    private DbConnectionOracle dbConOracle;

    // private constructor, we don't want the class to be instantiated from
    // others.
    private DbConnectionSingletonFactory() {
//	this.dbConPostgres = new DbConnectionPostgres();
//        this.dbConOracle = new DbConnectionOracle();
    }

    /**
     * Singleton Getter
     *
     * @return the singletonFactory-Instance
     */
    public synchronized static DbConnectionSingletonFactory getDbConnectionSingletonFactory() {
        if (dbFactory == null) {
            dbFactory = new DbConnectionSingletonFactory();
        }
        return dbFactory;
    }

    /**
     * Getter for the DbConnection, use parameter to choose Oracle-Instance or PostgreSQL-Instance
     *
     * @param dbms postgres or oracle
     *
     * @return DbConnection for given dbms-parameter as singleton
     */
    public synchronized DbConnection getDbConnection(String dbms) {

        switch (dbms) {
            case "oracle":
                if (this.dbConOracle == null) {
                    this.dbConOracle = new DbConnectionOracle();
                }
                return (DbConnection) this.dbConOracle;
        }
        return null;
    }
}
