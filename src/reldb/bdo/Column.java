/*
 * @author Andreas Diesendorf (2343841) <andreas_d@hotmail.de>, Björn Burzlaff (2874851) <bjoern.b96@web.de>
 */
package reldb.bdo;

public class Column {
    private String columnName;
    private String columnTypeName;
    private Integer columnPrecision;
    private Integer columnScale;
    private Boolean nullable;


    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    
    public String getColumnName() {
        return columnName;
    }

    
    public void setColumnTypeName(String columnTypeName) {
        this.columnTypeName = columnTypeName;
    }

    public String getColumnTypeName() {
        return columnTypeName;
    }

    
    public void setColumnPrecision(Integer columnPrecision) {
        this.columnPrecision = columnPrecision;
    }
    
    public Integer getColumnPrecision() {
        return columnPrecision;
    }
    

    public void setColumnScale(Integer columnScale) {
        this.columnScale = columnScale;
    }
    
    public Integer getColumnScale() {
        return columnScale;
    }

    
    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }
    
    public Boolean getNullable() {
        return nullable;
    }


    
}
