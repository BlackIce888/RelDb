/*
 * @author Andreas Diesendorf (2343841) <andreas_d@hotmail.de>, Björn Burzlaff (2874851) <bjoern.b96@web.de>
 */
package reldb.bdo;

import java.util.ArrayList;
import java.util.List;


public class Table {
    private String tableName;
    private List<Column> columnList;

  
    public Table() {
        this.columnList = new ArrayList<Column>();
    }

 
    public String getTableName() {
        return this.tableName;
    }


    public void setTableName(String tableName) {
        this.tableName = tableName;
    }


    public void addColumn(Column col) {
        this.columnList.add(col);
    }

    
    public int columnCount() {
        return this.columnList.size();
    }


    public Column getColumn(int index) {
        return this.columnList.get(index);
    }


    public void setColumn(int index, Column column) {
        this.columnList.set(index, column);
    }
}
