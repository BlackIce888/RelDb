/*
 * @author Andreas Diesendorf (2343841) <andreas_d@hotmail.de>, Björn Burzlaff (2874851) <bjoern.b96@web.de>
 */
package reldb.bdo;


public class ForeignKey {
    private String TableName = "";
    private String columnName = "";
    private String foreignTableName = "";
    private String foreignColumn = "";
    private String foreignKeyName = "";


    public void setTableName(String TableName) {
        this.TableName = TableName;
    }
    
    public String getTableName() {
        return TableName;
    }


    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    
    public String getColumnName() {
        return columnName;
    }

    
    public void setForeignTableName(String foreignTableName) {
        this.foreignTableName = foreignTableName;
    }

    public String getForeignTableName() {
        return foreignTableName;
    }


    public void setForeignColumn(String foreignColumn) {
        this.foreignColumn = foreignColumn;
    }
    
    public String getForeignColumn() {
        return foreignColumn;
    }


    public void setForeignKeyName(String foreignKeyName) {
        this.foreignKeyName = foreignKeyName;
    }

    public String getForeignKeyName() {
        return foreignKeyName;
    }
}
