/*
 * @author Andreas Diesendorf (2343841) <andreas_d@hotmail.de>, Björn Burzlaff (2874851) <bjoern.b96@web.de>
 */

package reldb.bdo;

public class CharacterFilter {
    private Boolean isExcluded = false;
    private String keywords = null;

    public CharacterFilter() {
    }

    public Boolean isExcluded() {
        return isExcluded;
    }

    public void setExcluded(Boolean excluded) {
        isExcluded = excluded;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
