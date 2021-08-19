/*
 * @author Andreas Diesendorf (2343841) <andreas_d@hotmail.de>, Björn Burzlaff (2874851) <bjoern.b96@web.de>
 */

package reldb.bdo;

import java.util.ArrayList;
import java.util.List;

public class PersonFilter {
    private Boolean isExcluded = false;
    private String roleType = null;
    private String keywords = null;

    public PersonFilter() {
    }

    public Boolean isExcluded() {
        return isExcluded;
    }

    public void setExcluded(Boolean excluded) {
        isExcluded = excluded;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Integer getRoleTypeId () {
        Integer roleId = null;
        switch (getRoleType()) {
            case "Actor":
                roleId = 1;
                break;
            case "Actress":
                roleId = 2;
                break;
            case "Producer":
                roleId = 3;
                break;
            case "Writer":
                roleId = 4;
                break;
            case "Cinematographer":
                roleId = 5;
                break;
            case "Composer":
                roleId = 6;
                break;
            case "Costume Designer":
                roleId = 7;
                break;
            case "Director":
                roleId = 8;
                break;
            case "Editor":
                roleId = 9;
                break;
            case "Miscellaneous Crew":
                roleId = 10;
                break;
            case "Production Designer":
                roleId = 11;
                break;
            case "Guest":
                roleId = 12;
                break;
            default:
                break;
        }
        return roleId;
    }
}
