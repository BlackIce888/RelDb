/*
 * @author Andreas Diesendorf (2343841) <andreas_d@hotmail.de>, Björn Burzlaff (2874851) <bjoern.b96@web.de>
 */
package reldb.bdo;

public class InfoFilter {
    private Integer rowId;
    private String filter;
    private Boolean isSelected = false;
    private String beforeValue = null;
    private String afterValue = null;

    public InfoFilter() {
    }

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter() {
        switch (this.getRowId()) {
            case 0 :
                this.filter = "production_year";
                break;
            case 1 :
                this.filter = "rating";
                break;
            case 2 :
                this.filter = "votes";
                break;
            case 3 :
                this.filter = "budget";
                break;
            case 4 :
                this.filter = "runtime";
                break;
            default :
                this.filter = null;
                break;
        }
    }

    public Boolean isSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public String getBeforeValue() {
        return beforeValue;
    }

    public void setBeforeValue(String beforeValue) {
        this.beforeValue = beforeValue;
    }

    public String getAfterValue() {
        return afterValue;
    }

    public void setAfterValue(String afterValue) {
        this.afterValue = afterValue;
    }

    public Boolean isAfterValueSet() {
        return getAfterValue() != null;
    }

    public Boolean isBeforeValueSet() {
        return getBeforeValue() != null;
    }

    public Integer getBeforeValueAsInt() {
        return Integer.parseInt(getBeforeValue());
    }

    public Integer getAfterValueAsInt() {
        return Integer.parseInt(getAfterValue());
    }

    public Float getBeforeValueAsFloat() {
        return Float.parseFloat(getBeforeValue());
    }

    public Float getAfterValueAsFloat() {
        return Float.parseFloat(getAfterValue());
    }
}
