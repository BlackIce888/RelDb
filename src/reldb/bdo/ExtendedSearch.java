/*
 * @author Andreas Diesendorf (2343841) <andreas_d@hotmail.de>, Björn Burzlaff (2874851) <bjoern.b96@web.de>
 */

package reldb.bdo;

import java.util.ArrayList;
import java.util.List;

public class ExtendedSearch {
    public String titleKeywords;
    public Integer match;
    public List<String> kindList;
    public List<String> genreList;
    public List<InfoFilter> infoFilters;
    public List<PersonFilter> personFilters;
    public List<CharacterFilter> charFilters;

    public ExtendedSearch(String titleKeywords, Integer match) {
        this.titleKeywords = titleKeywords;
        this.match = match;
    }

    public String getTitleKeywords() {
        return titleKeywords;
    }

    public void setTitleKeywords(String titleKeywords) {
        this.titleKeywords = titleKeywords;
    }

    public Integer getMatch() {
        return match;
    }

    public void setMatch(Integer match) {
        this.match = match;
    }

    public List<String> getKindList() {
        return kindList;
    }

    public void setKindList(List<String> kindList) {
        this.kindList = kindList;
    }

    public List<String> getGenreList() {
        return genreList;
    }

    public void setGenreList(List<String> genreList) {
        this.genreList = genreList;
    }

    public List<InfoFilter> getInfoFilters() {
        return infoFilters;
    }

    public void setInfoFilters(List<InfoFilter> infoFilters) {
        this.infoFilters = infoFilters;
    }

    public List<PersonFilter> getPersonFilters() {
        return personFilters;
    }

    public void setPersonFilters(List<PersonFilter> personFilters) {
        this.personFilters = personFilters;
    }

    public List<CharacterFilter> getCharFilters() {
        return charFilters;
    }

    public void setCharFilters(List<CharacterFilter> charFilters) {
        this.charFilters = charFilters;
    }

    public List<Integer> getSelectedKindIds () {
        List<Integer> kindIndexList = new ArrayList<>();
        getKindList().forEach(kind -> {
            switch (kind) {
                case "Movie":
                    kindIndexList.add(1);
                    break;
                case "TV Series":
                    kindIndexList.add(2);
                    break;
                case "TV Movie":
                    kindIndexList.add(3);
                    break;
                case "Video Movie":
                    kindIndexList.add(4);
                    break;
                case "TV Mini Series":
                    kindIndexList.add(5);
                    break;
                case "Video Game":
                    kindIndexList.add(6);
                    break;
                case "Episode":
                    kindIndexList.add(7);
                    break;
                default:
                    break;
            }
        });
        return kindIndexList;
    }
}
