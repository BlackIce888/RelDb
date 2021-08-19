/*
 * @author Andreas Diesendorf (2343841) <andreas_d@hotmail.de>, Björn Burzlaff (2874851) <bjoern.b96@web.de>
 */
package reldb.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static java.util.Objects.isNull;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import reldb.bdo.*;


public class TitleDAOImpl {

    private DbConnectionSingletonFactory dbConFactory;
    private DbConnection dbCon;
    private Connection oracleCon;
    private String oracleSchemaStr;
    private Map<Integer, Object> prepStmtMap = new HashMap<>();
    private Integer prepStmtCounter = 1;

    public TitleDAOImpl(){
        dbConFactory = DbConnectionSingletonFactory.getDbConnectionSingletonFactory();
        dbCon = dbConFactory.getDbConnection("oracle");
        oracleCon = dbCon.getConnection();
        if (!DatabaseSettings.ORACLE_SCHEMA.isEmpty()) {
            oracleSchemaStr = DatabaseSettings.ORACLE_SCHEMA + ".";
        }
        try {
            oracleCon.setAutoCommit(false);
        } catch (SQLException ex) {
            Logger.getLogger(MovieCompanyDAOImpl.class.getName()).log(Level.SEVERE, "Error setting autocommit: " + ex);
        }
    }

    private Integer getAndIncreaseStmtCounter() {
        return prepStmtCounter++;
    }

    private void fillPrepStmt(PreparedStatement preparedStatement) {
        try {
            for (Map.Entry<Integer, Object> entry : prepStmtMap.entrySet()) {
                System.out.println("----------------------------------------");
                System.out.println("Key:");
                System.out.println(entry.getKey());
                System.out.println("Value:");
                System.out.println(entry.getValue());
                System.out.println("----------------------------------------");

                if (entry.getValue() instanceof String) {
                    String strVal = (String) entry.getValue();
                    preparedStatement.setString(entry.getKey(), strVal);
                }
                if (entry.getValue() instanceof Integer) {
                    Integer intVal = (Integer) entry.getValue();
                    preparedStatement.setInt(entry.getKey(), intVal);
                }
                if (entry.getValue() instanceof Float) {
                    Float floatVal = (Float) entry.getValue();
                    preparedStatement.setFloat(entry.getKey(), floatVal);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(TitleDAOImpl.class.getName()).log(Level.SEVERE, "Error while trying to load PreparedStatement: " + ex.getMessage());
        }
    }

    private String prepareMatchSubQuery(String query, Integer match, String[] keywordArray) {
        switch (match) {
            case 1:
                // match all
                for (int i = 0; i < keywordArray.length; i++) {
                    query += "title.title LIKE ?";
                    if (i < keywordArray.length - 1) {
                        query += " AND ";
                    }
                }
                break;
            case 2:
                // match any
                for (int i = 0; i < keywordArray.length; i++) {
                    if (i == 0) {
                        query += "(";
                    }
                    query += "title.title LIKE ?";
                    if (i < keywordArray.length - 1) {
                        query += " OR ";
                    }
                    if (i == keywordArray.length - 1) {
                        query += ")";
                    }
                }
                break;
            case 3:
                // match exact
                query += "title.title LIKE ? ";
                break;
        }
        return query;
    }

    private String[] splitKeywords(String keywords) {
        return keywords.split(" ");
    }

    private String prepareKindSubQuery(List<Integer> kindIdList) {
        String subQuery = null;
        if (!kindIdList.isEmpty()) {
            subQuery = "(";
            ListIterator listIterator = kindIdList.listIterator();
            while (listIterator.hasNext()) {
                subQuery += "title.kind_id = ?";
                prepStmtMap.put(getAndIncreaseStmtCounter(), listIterator.next());
                if (listIterator.hasNext()) {
                    subQuery += " OR ";
                }
            }
            subQuery += ")";
        }
        return subQuery;
    }

    private String prepareGenreSubQuery(List<String> genreList) {
        String subQuery = null;
        if (!genreList.isEmpty()) {
            subQuery = "title.id IN (SELECT movie_info.movie_id FROM " + oracleSchemaStr + "movie_info WHERE movie_info.info_type_id = 3 AND (";
            ListIterator listIterator = genreList.listIterator();
            while (listIterator.hasNext()) {
                subQuery += "(to_char(movie_info.info) = ?)";
                prepStmtMap.put(getAndIncreaseStmtCounter(), listIterator.next());
                if (listIterator.hasNext()) {
                    subQuery += " OR ";
                }
            }
            subQuery += "))";
        }
        return subQuery;
    }

    private String preparePersonSubQuery(List<PersonFilter> personFilterList) {
        String subQuery1 = null;
        if (!personFilterList.isEmpty()) {
            subQuery1 = "title.id IN (SELECT cast_info.movie_id FROM " + oracleSchemaStr + "cast_info WHERE ";
            String subQuery2 = "cast_info.person_id IN (SELECT name.id FROM " + oracleSchemaStr + "name WHERE name LIKE ?)";

            ListIterator listIterator = personFilterList.listIterator();
            PersonFilter personFilter = (PersonFilter) listIterator.next();
            subQuery1 += (personFilter.isExcluded() ? "NOT (" : "(") + "cast_info.role_id = ? AND " + subQuery2 + ")";
            prepStmtMap.put(getAndIncreaseStmtCounter(), personFilter.getRoleTypeId());
            prepStmtMap.put(getAndIncreaseStmtCounter(), "%" + personFilter.getKeywords() + "%");
            while (listIterator.hasNext()) {
                personFilter = (PersonFilter) listIterator.next();
                subQuery1 += (personFilter.isExcluded() ? " AND NOT (" : " AND (") + "cast_info.role_id = ? AND " + subQuery2 + ")";
                prepStmtMap.put(getAndIncreaseStmtCounter(), "%" + personFilter.getKeywords() + "%");
                prepStmtMap.put(getAndIncreaseStmtCounter(), personFilter.getRoleTypeId());
            }
            subQuery1 += ")";
        }
        return subQuery1;
    }

    private String prepareCharSubQuery(List<CharacterFilter> characterFilterList) {
        String subQuery1 = null;
        if (!characterFilterList.isEmpty()) {
            subQuery1 = "title.id IN (SELECT cast_info.movie_id FROM " + oracleSchemaStr + "cast_info WHERE ";
            String subQuery2 = "cast_info.person_role_id IN (SELECT char_name.id FROM " + oracleSchemaStr + "char_name WHERE char_name.name LIKE ?)";

            ListIterator listIterator = characterFilterList.listIterator();
            CharacterFilter characterFilter = (CharacterFilter) listIterator.next();
            subQuery1 += (characterFilter.isExcluded() ? "NOT (" : "(") + subQuery2 + ")";
            prepStmtMap.put(getAndIncreaseStmtCounter(), "%" + characterFilter.getKeywords() + "%");
            while (listIterator.hasNext()) {
                characterFilter = (CharacterFilter) listIterator.next();
                subQuery1 += (characterFilter.isExcluded() ? " AND NOT (" : " AND (") + subQuery2 + ")";
                prepStmtMap.put(getAndIncreaseStmtCounter(), "%" + characterFilter.getKeywords() + "%");
            }
            subQuery1 += ")";
        }
        return subQuery1;
    }

    private String prepareInfoSubQuery(List<InfoFilter> infoFilterList) {
        String subQuery = null;
        List<String> queryList = new ArrayList<>();
        for (InfoFilter infoFilter : infoFilterList) {
            switch (infoFilter.getRowId()) {
                case 0:
                    if (infoFilter.isSelected()) {
                        String pYearQuery;
                        if (infoFilter.isAfterValueSet() && infoFilter.isBeforeValueSet()) {
                            if (infoFilter.getBeforeValueAsInt() > infoFilter.getAfterValueAsInt()) {
                                pYearQuery = "title.production_year BETWEEN ? AND ?";
                                prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getAfterValueAsInt());
                                prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getBeforeValueAsInt());
                            } else if (infoFilter.getBeforeValueAsInt() < infoFilter.getAfterValueAsInt()) {
                                pYearQuery = "title.production_year BETWEEN ? AND ?";
                                prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getBeforeValueAsInt());
                                prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getAfterValueAsInt());
                            } else {
                                pYearQuery = "title.production_year = ?";
                                prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getBeforeValueAsInt());
                            }
                        } else if (!infoFilter.isAfterValueSet() && infoFilter.isBeforeValueSet()) {
                            pYearQuery = "title.production_year < ?";
                            prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getBeforeValueAsInt());
                        } else if (infoFilter.isAfterValueSet() && !infoFilter.isBeforeValueSet()) {
                            pYearQuery = "title.production_year > ?";
                            prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getAfterValueAsInt());
                        } else {
                            pYearQuery = "title.production_year IS NOT null";
                        }
                        queryList.add(pYearQuery);
                    }
                    break;
                case 1:
                    String ratingQuery;
                    if (infoFilter.isSelected()) {
                        ratingQuery = "title.id IN (SELECT movie_info_idx.id FROM " + oracleSchemaStr + "movie_info_idx WHERE movie_info_idx.info_type_id = 101 AND ";
                        if (infoFilter.isAfterValueSet() && infoFilter.isBeforeValueSet()) {
                            if (infoFilter.getBeforeValueAsFloat() > infoFilter.getAfterValueAsFloat()) {
                                ratingQuery += "movie_info_idx.rating BETWEEN ? AND ?";
                                prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getAfterValueAsFloat());
                                prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getBeforeValueAsFloat());
                            } else if (infoFilter.getBeforeValueAsFloat() < infoFilter.getAfterValueAsFloat()) {
                                ratingQuery += "movie_info_idx.rating BETWEEN ? AND ?";
                                prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getBeforeValueAsFloat());
                                prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getAfterValueAsFloat());
                            } else {
                                ratingQuery += "movie_info_idx.rating = ?";
                                prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getBeforeValueAsFloat());
                            }
                        } else if (!infoFilter.isAfterValueSet() && infoFilter.isBeforeValueSet()) {
                            ratingQuery += "movie_info_idx.rating < ?";
                            prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getBeforeValueAsFloat());
                        } else if (infoFilter.isAfterValueSet() && !infoFilter.isBeforeValueSet()) {
                            ratingQuery += "movie_info_idx.rating > ?";
                            prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getAfterValueAsFloat());
                        } else {
                            ratingQuery += "movie_info_idx.rating IS NOT null";
                        }
                        queryList.add(ratingQuery +")");
                    }
                    break;
                case 2:
                    if (infoFilter.isSelected()) {
                        String votesQuery;
                        votesQuery = "title.id IN (SELECT movie_info_idx.id FROM " + oracleSchemaStr + "movie_info_idx WHERE movie_info_idx.info_type_id = 100 AND ";
                        if (infoFilter.isAfterValueSet() && infoFilter.isBeforeValueSet()) {
                            if (infoFilter.getBeforeValueAsInt() > infoFilter.getAfterValueAsInt()) {
                                votesQuery += "movie_info_idx.votes BETWEEN ? AND ?";
                                prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getAfterValueAsInt());
                                prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getBeforeValueAsInt());
                            } else if (infoFilter.getBeforeValueAsInt() < infoFilter.getAfterValueAsInt()) {
                                votesQuery += "movie_info_idx.votes BETWEEN ? AND ?";
                                prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getBeforeValueAsInt());
                                prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getAfterValueAsInt());
                            } else {
                                votesQuery += "movie_info_idx.votes = ?";
                                prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getBeforeValueAsInt());
                            }
                        } else if (!infoFilter.isAfterValueSet() && infoFilter.isBeforeValueSet()) {
                            votesQuery += "movie_info_idx.votes < ?";
                            prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getBeforeValueAsInt());
                        } else if (infoFilter.isAfterValueSet() && !infoFilter.isBeforeValueSet()) {
                            votesQuery += "movie_info_idx.votes > ?";
                            prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getAfterValueAsInt());
                        } else {
                            votesQuery += "movie_info_idx.votes IS NOT null";
                        }
                        queryList.add(votesQuery + ")");
                    }
                    break;
                case 3:
                    if (infoFilter.isSelected()) {
                        String budgetQuery;
                        budgetQuery = "title.id IN (SELECT movie_info.id FROM " + oracleSchemaStr + "movie_info WHERE movie_info.info_type_id = 105 AND ";
                        if (infoFilter.isAfterValueSet() && infoFilter.isBeforeValueSet()) {
                            if (infoFilter.getBeforeValueAsInt() > infoFilter.getAfterValueAsInt()) {
                                budgetQuery += "movie_info.budget BETWEEN ? AND ?";
                                prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getAfterValueAsInt());
                                prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getBeforeValueAsInt());
                            } else if (infoFilter.getBeforeValueAsInt() < infoFilter.getAfterValueAsInt()) {
                                budgetQuery += "movie_info.budget BETWEEN ? AND ?";
                                prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getBeforeValueAsInt());
                                prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getAfterValueAsInt());
                            } else {
                                budgetQuery += "movie_info.budget = ?";
                                prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getBeforeValueAsInt());
                            }
                        } else if (!infoFilter.isAfterValueSet() && infoFilter.isBeforeValueSet()) {
                            budgetQuery += "movie_info.budget < ?";
                            prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getBeforeValueAsInt());
                        } else if (infoFilter.isAfterValueSet() && !infoFilter.isBeforeValueSet()) {
                            budgetQuery += "movie_info.budget > ?";
                            prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getAfterValueAsInt());
                        } else {
                            budgetQuery += "movie_info.budget IS NOT null";
                        }
                        queryList.add(budgetQuery + ")");
                    }
                    break;
                case 4:
                    if (infoFilter.isSelected()) {
                        String runtimeQuery;
                        runtimeQuery = "title.id IN (SELECT movie_info.id FROM " + oracleSchemaStr + "movie_info WHERE movie_info.info_type_id = 1 AND ";
                        if (infoFilter.isAfterValueSet() && infoFilter.isBeforeValueSet()) {
                            if (infoFilter.getBeforeValueAsInt() > infoFilter.getAfterValueAsInt()) {
                                runtimeQuery += "movie_info.runtime BETWEEN ? AND ?";
                                prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getAfterValueAsInt());
                                prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getBeforeValueAsInt());
                            } else if (infoFilter.getBeforeValueAsInt() < infoFilter.getAfterValueAsInt()) {
                                runtimeQuery += "movie_info.runtime BETWEEN ? AND ?";
                                prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getBeforeValueAsInt());
                                prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getAfterValueAsInt());
                            } else {
                                runtimeQuery += "movie_info.runtime = ?";
                                prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getBeforeValueAsInt());
                            }
                        } else if (!infoFilter.isAfterValueSet() && infoFilter.isBeforeValueSet()) {
                            runtimeQuery += "movie_info.runtime < ?";
                            prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getBeforeValueAsInt());
                        } else if (infoFilter.isAfterValueSet() && !infoFilter.isBeforeValueSet()) {
                            runtimeQuery += "movie_info.runtime > ?";
                            prepStmtMap.put(getAndIncreaseStmtCounter(), infoFilter.getAfterValueAsInt());
                        } else {
                            runtimeQuery += "movie_info.runtime IS NOT null";
                        }
                        queryList.add(runtimeQuery + ")");
                    }
                    break;
                default:
                    break;
            }
        }
        if (!queryList.isEmpty()) {
            ListIterator queryIterator = queryList.listIterator();
            while (queryIterator.hasNext()) {
                subQuery += queryIterator.next();
                if (queryIterator.hasNext()) {
                    subQuery += " AND ";
                }
            }
        }
        return subQuery;
    }

    public ObservableList<Title> getTitlesByExtendedSearch(ExtendedSearch input) {
        PreparedStatement prepStmt;
        ObservableList<Title> titleList = FXCollections.observableArrayList();

        String[] titleKeywordArray = splitKeywords(input.getTitleKeywords());

        // Initialize query
        String query = "SELECT title.id, title.title, title.production_year, title.episode_of_id, kind_type.kind "
                + "FROM " + oracleSchemaStr + "title, " + oracleSchemaStr + "kind_type WHERE ";

        List<String> subQueryList = new ArrayList<>();

        // Fill the query for the prepared Statement and fill them
        String matchQuery = prepareMatchSubQuery("", input.getMatch(), titleKeywordArray);
        switch (input.getMatch()) {
            case 1:
                // match all
            case 2:
                // match any
                for (int i = 0; i < titleKeywordArray.length; i++) {
                    prepStmtMap.put(getAndIncreaseStmtCounter(), "%" + titleKeywordArray[i] + "%");
                }
                break;
            case 3:
                // match exact
                prepStmtMap.put(getAndIncreaseStmtCounter(), input.getTitleKeywords());
                break;
        }

        String kindQuery = prepareKindSubQuery(input.getSelectedKindIds());
        String genreQuery = prepareGenreSubQuery(input.getGenreList());
        String personQuery = preparePersonSubQuery(input.getPersonFilters());
        String charQuery = prepareCharSubQuery(input.getCharFilters());
        String infoQuery = prepareInfoSubQuery(input.getInfoFilters());

        if (matchQuery != null) {
            subQueryList.add(matchQuery);
        }
        if (kindQuery != null) {
            subQueryList.add(kindQuery);
        }
        if (genreQuery != null) {
            subQueryList.add(genreQuery);
        }
        if (personQuery != null) {
            subQueryList.add(personQuery);
        }
        if (charQuery != null) {
            subQueryList.add(charQuery);
        }
        if (infoQuery != null) {
            subQueryList.add(infoQuery);
        }

        ListIterator subQueryIterator = subQueryList.listIterator();
        while (subQueryIterator.hasNext()) {
            query += subQueryIterator.next();
            if (subQueryIterator.hasNext()) {
                query += " AND ";
            }
        }

        System.out.println("Full Query: " + query);

        try {
            prepStmt = oracleCon.prepareStatement(query);

            fillPrepStmt(prepStmt);

            ResultSet resultSetTitle = prepStmt.executeQuery();

            while (resultSetTitle.next()) {
                Title title = new Title();

                title.setId(resultSetTitle.getInt("id"));
                title.setTitle(resultSetTitle.getString("title"));
                title.setProduction_year(handleNullValue(resultSetTitle.getInt("production_year"), resultSetTitle));
                title.setKind(resultSetTitle.getString("kind"));
                title.setEpisode_of_id(handleNullValue(resultSetTitle.getInt("episode_of_id"), resultSetTitle));

                titleList.add(title);
            }
            prepStmt.close();
            resultSetTitle.close();

            return titleList;

        } catch (SQLException ex) {
            Logger.getLogger(TitleDAOImpl.class.getName()).log(Level.SEVERE, "Error while trying to load PreparedStatement: " + ex.getMessage());
        }

        return null;
    }

    
    public Title getTitleById(Integer titleId) {
        PreparedStatement prepStmt;
        Title title = new Title();

        // Initialize query
        String query = "SELECT * "
                + "FROM " + oracleSchemaStr + "title, " + oracleSchemaStr + "kind_type "
                + "WHERE title.id = ?";

        try {
            prepStmt = oracleCon.prepareStatement(query);
            prepStmt.setInt(1, titleId);
            ResultSet resultSetTitle = prepStmt.executeQuery();

            if(!resultSetTitle.next()){
                return null;
            }
            title.setId(resultSetTitle.getInt("id"));
            title.setTitle(resultSetTitle.getString("title"));
            title.setImdb_index(resultSetTitle.getString("imdb_index"));
            title.setProduction_year(handleNullValue(resultSetTitle.getInt("production_year"), resultSetTitle));
            title.setImdb_id(handleNullValue(resultSetTitle.getInt("imdb_id"), resultSetTitle));
            title.setPhonetic_code(resultSetTitle.getString("phonetic_code"));
            title.setSeason_nr(handleNullValue(resultSetTitle.getInt("season_nr"), resultSetTitle));
            title.setEpisode_nr(handleNullValue(resultSetTitle.getInt("episode_nr"), resultSetTitle));
            title.setSeries_years(resultSetTitle.getString("series_years"));
            title.setMd5sum(resultSetTitle.getString("md5sum"));
            title.setKind_id(handleNullValue(resultSetTitle.getInt("kind_id"), resultSetTitle));
            title.setKind(resultSetTitle.getString("kind"));
            title.setEpisode_of_id(handleNullValue(resultSetTitle.getInt("episode_of_id"), resultSetTitle));
            prepStmt.close();
            resultSetTitle.close();

            return title;
        } catch (SQLException ex) {
            Logger.getLogger(TitleDAOImpl.class.getName()).log(Level.SEVERE, "Error while trying to load PreparedStatement: " + ex.getMessage());
        }

        return null;
    }

    public ObservableList<Title> getTitlesByKeyword(String keywords, int match) {
        PreparedStatement prepStmt;
        ObservableList<Title> titleList = FXCollections.observableArrayList();

        // Initialize query
        String query = "SELECT title.id, title.title, title.production_year, title.episode_of_id, kind_type.kind "
                + "FROM " + oracleSchemaStr + "title, " + oracleSchemaStr + "kind_type "
                + "WHERE title.kind_id = kind_type.id AND ";

        String[] keywordArray = keywords.split(" ");

        // Fill the query for the prepared Statement
        query = prepareMatchSubQuery(query, match, keywordArray);

        try {
            prepStmt = oracleCon.prepareStatement(query);

            // Fill the prepared Statement with the keywords
            switch (match) {
                case 1:
                // match all
                case 2:
                    // match any
                    for (int i = 0; i < keywordArray.length; i++) {
                        prepStmt.setString(i + 1, "%" + keywordArray[i] + "%");
                    }
                    break;
                case 3:
                    // match exact
                    prepStmt.setString(1, keywords);
                    break;
            }

            ResultSet resultSetTitle = prepStmt.executeQuery();

            while (resultSetTitle.next()) {
                Title title = new Title();
                
                title.setId(resultSetTitle.getInt("id"));
                title.setTitle(resultSetTitle.getString("title"));
                title.setProduction_year(handleNullValue(resultSetTitle.getInt("production_year"), resultSetTitle));
                title.setKind(resultSetTitle.getString("kind"));
                title.setEpisode_of_id(handleNullValue(resultSetTitle.getInt("episode_of_id"), resultSetTitle));
                
                titleList.add(title);
            }
            prepStmt.close();
            resultSetTitle.close();

            return titleList;
        } catch (SQLException ex) {
            Logger.getLogger(TitleDAOImpl.class.getName()).log(Level.SEVERE, "Error while trying to load PreparedStatement: " + ex.getMessage());
        }

        return null;
    }
    
    
    
    public ObservableList<Title> getTitlesByPersonId(int person_id) {
        PreparedStatement prepStmt;
        ObservableList<Integer> castList = FXCollections.observableArrayList();
        ObservableList<Title> titleList = FXCollections.observableArrayList();

        // Initialize query
        String query = "SELECT cast_info.movie_id "
                + "FROM " + oracleSchemaStr + "cast_info "
                + "WHERE cast_info.person_id = ?";

        try {
            prepStmt = oracleCon.prepareStatement(query);
            prepStmt.setInt(1, person_id);
            ResultSet resultSetTitle = prepStmt.executeQuery();

            while (resultSetTitle.next()) {
                castList.add(resultSetTitle.getInt("movie_id"));
            }
            prepStmt.close();
            resultSetTitle.close();

            for(int movie_id : castList){
                titleList.add(getTitleById(movie_id));
            }
            
            return titleList;
        } catch (SQLException ex) {
            Logger.getLogger(TitleDAOImpl.class.getName()).log(Level.SEVERE, "Error while trying to load PreparedStatement: " + ex.getMessage());
        }

        return null;
    }
    
    
    public ObservableList<Title> getLinkesTitlesByTitleId(int movie_id) {
        PreparedStatement prepStmt;
        ObservableList<Title> titleList = FXCollections.observableArrayList();

        // Initialize query
        String query = "SELECT movie_link.linked_movie_id "
                + "FROM " + oracleSchemaStr + "movie_link "
                + "WHERE movie_link.movie_id = ?";

        try {
            prepStmt = oracleCon.prepareStatement(query);
            prepStmt.setInt(1, movie_id);
            ResultSet resultSetTitle = prepStmt.executeQuery();

            while (resultSetTitle.next()) {
                if(handleNullValue(resultSetTitle.getInt("linked_movie_id"), resultSetTitle) != null){
                    titleList.add(getTitleById(resultSetTitle.getInt("linked_movie_id")));
                }
            }
            prepStmt.close();
            resultSetTitle.close();


            
            return titleList;
        } catch (SQLException ex) {
            Logger.getLogger(TitleDAOImpl.class.getName()).log(Level.SEVERE, "Error while trying to load PreparedStatement: " + ex.getMessage());
        }

        return null;
    }
    
    
    
    public Title getFullTitleById(Integer titleId) {
        PreparedStatement prepStmt;
        Title title = new Title();

        // Initialize query
        String query = "SELECT * "
                + "FROM " + oracleSchemaStr + "title, " + oracleSchemaStr + "kind_type "
                + "WHERE title.id = ?";

        try {
            prepStmt = oracleCon.prepareStatement(query);
            prepStmt.setInt(1, titleId);
            ResultSet resultSetTitle = prepStmt.executeQuery();

            if(!resultSetTitle.next()){
                return null;
            }
            title.setId(resultSetTitle.getInt("id"));
            title.setTitle(resultSetTitle.getString("title"));
            title.setImdb_index(resultSetTitle.getString("imdb_index"));
            title.setProduction_year(handleNullValue(resultSetTitle.getInt("production_year"), resultSetTitle));
            title.setImdb_id(handleNullValue(resultSetTitle.getInt("imdb_id"), resultSetTitle));
            title.setPhonetic_code(resultSetTitle.getString("phonetic_code"));
            title.setSeason_nr(handleNullValue(resultSetTitle.getInt("season_nr"), resultSetTitle));
            title.setEpisode_nr(handleNullValue(resultSetTitle.getInt("episode_nr"), resultSetTitle));
            title.setSeries_years(resultSetTitle.getString("series_years"));
            title.setMd5sum(resultSetTitle.getString("md5sum"));
            title.setKind_id(handleNullValue(resultSetTitle.getInt("kind_id"), resultSetTitle));
            title.setKind(resultSetTitle.getString("kind"));
            title.setEpisode_of_id(handleNullValue(resultSetTitle.getInt("episode_of_id"), resultSetTitle));
            prepStmt.close();
            resultSetTitle.close();
            
            
            
            
            
            //episode_of
            if(!isNull(title.getEpisode_of_id())){
                prepStmt = oracleCon.prepareStatement("SELECT title FROM " + oracleSchemaStr + "title WHERE id = ?");
                prepStmt.setInt(1, title.getEpisode_of_id());
                resultSetTitle = prepStmt.executeQuery();
                if (resultSetTitle.next()) {
                    title.setEpisodeOf(resultSetTitle.getString("title"));
                }
                resultSetTitle.close();
                prepStmt.close();
            }

            //rating
            prepStmt = oracleCon.prepareStatement("SELECT info, note FROM " + oracleSchemaStr + "movie_info_idx mii, " + oracleSchemaStr + "title t "
                    + "WHERE t.id = mii.movie_id AND t.id = ? AND mii.info_type_id = ?");
            prepStmt.setInt(1, titleId);
            prepStmt.setInt(2, 101);
            resultSetTitle = prepStmt.executeQuery();
            if (resultSetTitle.next()) {
                title.setRating(resultSetTitle.getString("info"));
            }

            //votes
            prepStmt.setInt(1, titleId);
            prepStmt.setInt(2, 100);
            resultSetTitle = prepStmt.executeQuery();
            if (resultSetTitle.next()) {
                title.setVotes(resultSetTitle.getString("info"));
            }
            resultSetTitle.close();
            prepStmt.close();

            //genre
            prepStmt = oracleCon.prepareStatement("SELECT info, note FROM " + oracleSchemaStr + "movie_info mi, " + oracleSchemaStr + "title t "
                    + "WHERE t.id = mi.movie_id AND t.id = ? AND mi.info_type_id = ?");
            prepStmt.setInt(1, titleId);
            prepStmt.setInt(2, 3);
            resultSetTitle = prepStmt.executeQuery();
            while (resultSetTitle.next()) {
                title.getGenres().add(resultSetTitle.getString("info"));
            }

            //books
            prepStmt.setInt(1, titleId);
            prepStmt.setInt(2, 92);
            resultSetTitle = prepStmt.executeQuery();
            while (resultSetTitle.next()) {
                title.getBooks().add(resultSetTitle.getString("info"));
            }
            resultSetTitle.close();

            //taglines
            prepStmt.setInt(1, titleId);
            prepStmt.setInt(2, 9);
            resultSetTitle = prepStmt.executeQuery();
            while (resultSetTitle.next()) {
                title.getTaglines().add(resultSetTitle.getString("info"));
            }
            resultSetTitle.close();

            //budgets
            prepStmt.setInt(1, titleId);
            prepStmt.setInt(2, 105);
            resultSetTitle = prepStmt.executeQuery();
            while (resultSetTitle.next()) {
                title.getBudgets().add(resultSetTitle.getString("info"));
            }
            resultSetTitle.close();

            //runtimes
            prepStmt.setInt(1, titleId);
            prepStmt.setInt(2, 1);
            resultSetTitle = prepStmt.executeQuery();
            while (resultSetTitle.next()) {
                TitleRuntime rt = new TitleRuntime();
                rt.setRuntime(resultSetTitle.getString("info"));
                if (!isNull(resultSetTitle.getString("note"))) {
                    rt.setNote(resultSetTitle.getString("note"));
                } else {
                    rt.setNote("(normal version)");
                }

                title.getRuntimes().add(rt);
            }
            resultSetTitle.close();

            //plots
            prepStmt.setInt(1, titleId);
            prepStmt.setInt(2, 98);
            resultSetTitle = prepStmt.executeQuery();
            while (resultSetTitle.next()) {
                TitlePlot plot = new TitlePlot();
                plot.setPlot(resultSetTitle.getString("info"));
                plot.setAuthor(resultSetTitle.getString("note"));
                title.getPlot().add(plot);
            }
            resultSetTitle.close();
            prepStmt.close();

            //fskGermany
            prepStmt = oracleCon.prepareStatement("SELECT info, note FROM " + oracleSchemaStr + "movie_info mi, " + oracleSchemaStr + "title t "
                    + "WHERE t.id = mi.movie_id AND t.id = ? AND mi.info_type_id = ? AND (mi.info LIKE 'Germany:%' OR mi.info LIKE 'West Germany:%')");
            prepStmt.setInt(1, titleId);
            prepStmt.setInt(2, 5);
            resultSetTitle = prepStmt.executeQuery();
            while (resultSetTitle.next()) {
                String fskString;
                fskString = resultSetTitle.getString("info").replace("West ", "");
                fskString = fskString.replace("Germany:", "");
                fskString += " (Germany) ";
                if (!isNull(resultSetTitle.getString("note"))) {
                    fskString += resultSetTitle.getString("note");
                }
                title.getFskGermany().add(fskString);
            }
            resultSetTitle.close();
            prepStmt.close();

            //releasedatesGermany
            prepStmt = oracleCon.prepareStatement("SELECT info, note FROM " + oracleSchemaStr + "movie_info mi, " + oracleSchemaStr + "title t "
                    + "WHERE t.id = mi.movie_id AND t.id = ? AND mi.info_type_id = ? AND (mi.info LIKE 'Germany:%' OR mi.info LIKE 'West Germany:%')");
            prepStmt.setInt(1, titleId);
            prepStmt.setInt(2, 16);
            resultSetTitle = prepStmt.executeQuery();
            while (resultSetTitle.next()) {
                String releaseDateString;
                releaseDateString = resultSetTitle.getString("info").replace("West ", "");
                releaseDateString = releaseDateString.replace("Germany:", "");
                releaseDateString += " (Germany) ";
                if (!isNull(resultSetTitle.getString("note"))) {
                    releaseDateString += resultSetTitle.getString("note");
                }
                title.getReleaseDatesGermany().add(releaseDateString);
            }
            resultSetTitle.close();
            prepStmt.close();
            
            

            
            return title;
        } catch (SQLException ex) {
            Logger.getLogger(TitleDAOImpl.class.getName()).log(Level.SEVERE, "Error while trying to load PreparedStatement: " + ex.getMessage());
        }

        return null;
    }
    
    
    
    private Integer handleNullValue(Integer colValue, ResultSet rs) throws SQLException {
        if (rs.wasNull()) {
            return null;
        } else {
            return colValue;
        }
    }
    
}
