/*
 * @author Andreas Diesendorf (2343841) <andreas_d@hotmail.de>, Björn Burzlaff (2874851) <bjoern.b96@web.de>
 */
package reldb.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import reldb.bdo.DatabaseSettings;
import reldb.bdo.CharName;
import reldb.bdo.Person;
import reldb.bdo.Title;


public class CharNameDAOImpl {
    
    private DbConnectionSingletonFactory dbConFactory;
    private DbConnection dbCon;
    private Connection oracleCon;
    private String oracleSchemaStr;
    
    public CharNameDAOImpl(){
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
    
    
    public ObservableList<CharName> getPersonsByKeyword(String keywords, int match) {
        PreparedStatement prepStmt;
        ObservableList<CharName> charNameList = FXCollections.observableArrayList();

        // Initialize query
        String query = "SELECT char_name.id, char_name.name "
                + "FROM " + oracleSchemaStr + "char_name "
                + "WHERE ";

        String[] keywordArray = keywords.split(" ");

        // Fill the query for the prepared Statement
        switch (match) {
            case 1:
                // match all
                for (int i = 0; i < keywordArray.length; i++) {
                    query += "char_name.name LIKE ? ";
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
                    query += "char_name.name LIKE ? ";
                    if (i < keywordArray.length - 1) {
                        query += "OR ";
                    }
                    if (i == keywordArray.length - 1) {
                        query += ")";
                    }
                }
                break;
            case 3:
                // match exact
                query += "char_name.name LIKE ? ";
                break;
        }

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

            ResultSet resultSetCharName = prepStmt.executeQuery();

            TitleDAOImpl title = new TitleDAOImpl();
            Title titleTmp = new Title();
            PersonDAOImpl person = new PersonDAOImpl();
            Person personTmp = new Person();
            
            while (resultSetCharName.next()) {
                CharName charName = new CharName();
                charName.setId(resultSetCharName.getInt("id"));
                charName.setName(resultSetCharName.getString("name"));
                
                
                
                PreparedStatement prepStmtTmp;


                // Initialize query
                String queryTmp = "SELECT cast_info.person_id, cast_info.movie_id "
                                + "FROM " + oracleSchemaStr + "cast_info "
                                + "WHERE cast_info.person_role_id = ? ";

                try {
                    prepStmtTmp = oracleCon.prepareStatement(queryTmp);
                    prepStmtTmp.setInt(1, charName.getId());
                    ResultSet resultSetCharNameInfo = prepStmtTmp.executeQuery();

                    if(resultSetCharNameInfo.next()){
                        if(handleNullValue(resultSetCharNameInfo.getInt("movie_id"), resultSetCharNameInfo) != null){
                        titleTmp = title.getTitleById(resultSetCharNameInfo.getInt("movie_id"));
                        charName.setMovieName(titleTmp.getTitle());
                        if(titleTmp.getProduction_year() != null){
                            charName.setMovieYear(titleTmp.getProduction_year());
                        }
                        }
                        if(handleNullValue(resultSetCharNameInfo.getInt("person_id"), resultSetCharNameInfo) != null){
                            personTmp = person.getPersonById(resultSetCharNameInfo.getInt("person_id"));
                            charName.setActorName(personTmp.getName());
                        }
                    }

                    prepStmtTmp.close();
                    resultSetCharNameInfo.close();

                    } catch (SQLException ex) {
                        Logger.getLogger(CharNameDAOImpl.class.getName()).log(Level.SEVERE, "Error while trying to load PreparedStatement: " + ex.getMessage());
                    }
                
                
                
                
                charNameList.add(charName);
            }
            prepStmt.close();
            resultSetCharName.close();

            return charNameList;
        } catch (SQLException ex) {
            Logger.getLogger(CharNameDAOImpl.class.getName()).log(Level.SEVERE, "Error while trying to load PreparedStatement: " + ex.getMessage());
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
