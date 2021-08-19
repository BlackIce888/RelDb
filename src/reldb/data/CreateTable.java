/*
 * @author Andreas Diesendorf (2343841) <andreas_d@hotmail.de>, Björn Burzlaff (2874851) <bjoern.b96@web.de>
 */
package reldb.data;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import static java.util.Objects.isNull;
import java.util.logging.Level;
import java.util.logging.Logger;
import reldb.bdo.DatabaseSettings;
import reldb.bdo.ForeignKey;
import reldb.bdo.PrimaryKey;
import reldb.bdo.Table;


public class CreateTable {
    
    private DbConnectionSingletonFactory dbConFactory;
    private DbConnection dbCon;
    private Connection oracleCon;
    private String oracleSchemaStr;
    private String userName;
    private String userNameclean;
    
    public CreateTable(){
        dbConFactory = DbConnectionSingletonFactory.getDbConnectionSingletonFactory();
        dbCon = dbConFactory.getDbConnection("oracle");
        oracleCon = dbCon.getConnection();
        if (!DatabaseSettings.ORACLE_SCHEMA.isEmpty()) {
            oracleSchemaStr = DatabaseSettings.ORACLE_SCHEMA + ".";
        }
        try {
            oracleCon.setAutoCommit(false);
            userNameclean = dbCon.getConnection().getSchema();
            userName = userNameclean + ".";
        } catch (SQLException ex) {
            Logger.getLogger(MovieCompanyDAOImpl.class.getName()).log(Level.SEVERE, "Error setting autocommit: " + ex);
        }
        
    }
    
    public List<String> existingTables(List<Table> oracleTables) {
        DatabaseMetaData dbMetaData = null;
        String[] tableTypes = {"TABLE"};
        List<String> existingTableList = new ArrayList<>();

        //System.out.println("\n ---Checking for existing tables--- \n");
        
        try {
            dbMetaData = oracleCon.getMetaData();
        } catch (SQLException except) {
            System.out.println("Error retrieving OracleSQL Metadata.");
            System.out.println(except);
        }

        try {
            ResultSet resultSetTables = dbMetaData.getTables(oracleCon.getCatalog(), userNameclean, null, tableTypes);

            while (resultSetTables.next()) {
                for (Table tmpTable : oracleTables) {
                    if (tmpTable.getTableName().toUpperCase().equals(resultSetTables.getString("TABLE_NAME").toUpperCase())) {
                        String tableName = new String();
                        tableName = resultSetTables.getString("TABLE_NAME");
                        existingTableList.add(tableName);
                        //System.out.println("existing table:");
                        //System.out.println(tableName);
                    }
                }
            }
            resultSetTables.close();

            if (existingTableList.isEmpty()) {
                System.out.println("No tables already exist in Oracle.");
            } else {
                //System.out.println("There are already some tables in this schema.");
            }
            return existingTableList;

        } catch (SQLException except) {
            System.out.println("Error accessing OracleSQL data from tables.");
            System.out.println(except);
        }

        return null;
    }


    


    public void createTables(List<Table> oracleTables) {

        String query = "";
        
        System.out.println("\n --Start creating tables--\n");
        
        for (Table tmpTable : oracleTables) {

            System.out.println("Creating Table: " + tmpTable.getTableName());
            
            query = "CREATE TABLE " + userName + tmpTable.getTableName() + " (";

            // get all columns and their attributes
            for (int i = 0; i < tmpTable.columnCount(); i++) {
                query += tmpTable.getColumn(i).getColumnName() + " " + tmpTable.getColumn(i).getColumnTypeName();
                if (!isNull(tmpTable.getColumn(i).getColumnPrecision())) {
                    query += "(" + tmpTable.getColumn(i).getColumnPrecision();
                    if (!isNull(tmpTable.getColumn(i).getColumnScale())) {
                        query += "," + tmpTable.getColumn(i).getColumnScale();
                    }
                    query += ")";
                }

                if (!tmpTable.getColumn(i).getNullable()) {
                    query += " NOT NULL";
                }

                if (i < tmpTable.columnCount() - 1) {
                    query += ",";
                }
            }
            query += ")";

            Statement state;
            try {
                state = oracleCon.createStatement();
                state.executeUpdate(query);
                
                

                query = "CREATE SEQUENCE " + tmpTable.getTableName() + "_seq \n"
                        + "  START WITH 1 \n"
                        + "  INCREMENT BY 1 \n"
                        + "  nomaxvalue";
                state = oracleCon.createStatement();
                state.executeUpdate(query);
                state.close();
                

            } catch (SQLException except) {
                System.out.println("Error creating OracleSQL table.");
                System.out.println(except);
            }
        }
    }


    public void createPrimaryKeys(List<PrimaryKey> postgreSQLPrimaryKey) {
        String query = "";

        for (PrimaryKey tmpPK : postgreSQLPrimaryKey) {

            query = "ALTER TABLE " + userName + tmpPK.getTableName() + " ADD CONSTRAINT " + tmpPK.getPrimaryKeyName() + " PRIMARY KEY (" + tmpPK.getPrimaryKeyColumn() + ")";

            Statement state;
            try {
                state = oracleCon.createStatement();
                state.executeUpdate(query);

            } catch (SQLException except) {
                System.out.println("Error creating OracleSQL PrimaryKeys.");
                System.out.println(except);
            }
        }
    }


    public void createForeignKeys(List<ForeignKey> postgreSQLForeignKey) {
        String query = "";

        List<ForeignKey> oracleForeignKey = postgreSQLForeignKey;
        //List<ForeignKey> oracleForeignKey = uniqueForeignKey(postgreSQLForeignKey);

        for (ForeignKey tmpForeignKey : oracleForeignKey) {

            query = "ALTER TABLE " + userName + tmpForeignKey.getForeignTableName() + " ADD CONSTRAINT "
                    + tmpForeignKey.getForeignKeyName() + " FOREIGN KEY (" + tmpForeignKey.getForeignColumn() + ")"
                    + " REFERENCES " + userName + tmpForeignKey.getTableName() + "(" + tmpForeignKey.getColumnName() + ")";

            Statement state;
            try {
                state = oracleCon.createStatement();
                state.executeUpdate(query);

            } catch (SQLException except) {
                System.out.println("Error creating OracleSQL ForeignKeys.");
                System.out.println(except);
            }
        }
    }
    
}
