/*
 * @author Andreas Diesendorf (2343841) <andreas_d@hotmail.de>, Björn Burzlaff (2874851) <bjoern.b96@web.de>
 */
package reldb.bdo;


public class PrimaryKey {
    private String tableName = "";
    private String PrimaryKeyColumn = "";
    private String PrimaryKeyName = "";

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    public String getTableName() {
        return tableName;
    }

    
    public void setPrimaryKeyColumn(String PrimaryKeyColumn) {
        this.PrimaryKeyColumn = PrimaryKeyColumn;
    }

    public String getPrimaryKeyColumn() {
        return PrimaryKeyColumn;
    }

    
    public void setPrimaryKeyName(String PrimaryKeyName) {
        this.PrimaryKeyName = PrimaryKeyName;
    }

    public String getPrimaryKeyName() {
        return PrimaryKeyName;
    }

    

}
