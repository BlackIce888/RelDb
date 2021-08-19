/*
 * @author Andreas Diesendorf (2343841) <andreas_d@hotmail.de>, Björn Burzlaff (2874851) <bjoern.b96@web.de>
 */
package reldb.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import reldb.bdo.DatabaseSettings;
import reldb.bdo.Person;
import reldb.bdo.PersonBiography;


public class PersonDAOImpl {
    
    
    private DbConnectionSingletonFactory dbConFactory;
    private DbConnection dbCon;
    private Connection oracleCon;
    private String oracleSchemaStr;
    
    public PersonDAOImpl(){
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
    
    
    public Person getFullPersonById (Integer titleId) {
        PreparedStatement prepStmt;
        Person person = new Person();

        // Initialize query
        String query = "SELECT name.id, name.name, name.gender "
                + "FROM " + oracleSchemaStr + "name "
                + "WHERE name.id = ?";

        try {
            prepStmt = oracleCon.prepareStatement(query);
            prepStmt.setInt(1, titleId);
            ResultSet resultSetPerson = prepStmt.executeQuery();

            if(!resultSetPerson.next()){
                return null;
            }
            person.setNameId(resultSetPerson.getInt("id"));
            person.setName(resultSetPerson.getString("name"));
            person.setGender(resultSetPerson.getString("gender"));
            
            PreparedStatement prepStmtTmp;

            // Initialize query
            String queryTmp = "SELECT info "
                                + "FROM " + oracleSchemaStr + "person_info "
                                + "WHERE person_info.person_id = ? AND person_info.info_type_id = ?";

            try {
                //birthDate
                prepStmtTmp = oracleCon.prepareStatement(queryTmp);
                prepStmtTmp.setInt(1, person.getNameId());
                prepStmtTmp.setInt(2, 21);
                ResultSet resultSetPersonInfo = prepStmtTmp.executeQuery();

                if(resultSetPersonInfo.next()){
                    person.setBirthDate(resultSetPersonInfo.getString("info"));
                }
                resultSetPersonInfo.close();
                
                //birthNotes
                prepStmtTmp.setInt(1, person.getNameId());
                prepStmtTmp.setInt(2, 20);
                resultSetPersonInfo = prepStmtTmp.executeQuery();

                if(resultSetPersonInfo.next()){
                    person.setBirthNotes(resultSetPersonInfo.getString("info"));
                }
                resultSetPersonInfo.close();
                
                //deathDate
                prepStmtTmp.setInt(1, person.getNameId());
                prepStmtTmp.setInt(2, 23);
                resultSetPersonInfo = prepStmtTmp.executeQuery();

                if(resultSetPersonInfo.next()){
                    person.setDeathDate(resultSetPersonInfo.getString("info"));
                }
                resultSetPersonInfo.close();
                
                //deathNotes
                prepStmtTmp.setInt(1, person.getNameId());
                prepStmtTmp.setInt(2, 39);
                resultSetPersonInfo = prepStmtTmp.executeQuery();

                if(resultSetPersonInfo.next()){
                    person.setDeathNotes(resultSetPersonInfo.getString("info"));
                }
                resultSetPersonInfo.close();
                
                //height
                prepStmtTmp.setInt(1, person.getNameId());
                prepStmtTmp.setInt(2, 22);
                resultSetPersonInfo = prepStmtTmp.executeQuery();

                if(resultSetPersonInfo.next()){
                    person.setHeight(resultSetPersonInfo.getString("info"));
                }
                resultSetPersonInfo.close();
                prepStmtTmp.close();
                
                //biography
                queryTmp = "SELECT info, note "
                            + "FROM " + oracleSchemaStr + "person_info "
                            + "WHERE person_info.person_id = ? AND person_info.info_type_id = ?";
                prepStmtTmp = oracleCon.prepareStatement(queryTmp);
                prepStmtTmp.setInt(1, person.getNameId());
                prepStmtTmp.setInt(2, 19);
                resultSetPersonInfo = prepStmtTmp.executeQuery();
                while(resultSetPersonInfo.next()){
                    PersonBiography personBio = new PersonBiography();
                    personBio.setBiography(resultSetPersonInfo.getString("info"));
                    personBio.setNote(resultSetPersonInfo.getString("note"));
                    person.getBiographies().add(personBio);
                }
                resultSetPersonInfo.close();
                prepStmtTmp.close();
                
                //roles
                queryTmp = "SELECT Distinct role_type.role "
                            + "FROM " + oracleSchemaStr + "role_type "
                            + "WHERE role_type.id IN"
                                + "(SELECT cast_info.role_id "
                                + "FROM " + oracleSchemaStr + "cast_info "
                                + "WHERE cast_info.person_id = ?)";
                prepStmtTmp = oracleCon.prepareStatement(queryTmp);
                prepStmtTmp.setInt(1, person.getNameId());
                resultSetPersonInfo = prepStmtTmp.executeQuery();
                while(resultSetPersonInfo.next()){
                    person.getRoles().add(resultSetPersonInfo.getString("role"));
                }
                resultSetPersonInfo.close();
                prepStmtTmp.close();
                

           } catch (SQLException ex) {
                Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "Error while trying to load PreparedStatement: " + ex.getMessage());
            }
            
            
            
            prepStmt.close();
            resultSetPerson.close();

            return person;
        } catch (SQLException ex) {
            Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "Error while trying to load PreparedStatement: " + ex.getMessage());
        }

        return null;
    }
    
    
    public Person getPersonById(Integer titleId) {
        PreparedStatement prepStmt;
        Person person = new Person();

        // Initialize query
        String query = "SELECT name.id, name.name, name.gender "
                + "FROM " + oracleSchemaStr + "name "
                + "WHERE name.id = ?";

        try {
            prepStmt = oracleCon.prepareStatement(query);
            prepStmt.setInt(1, titleId);
            ResultSet resultSetPerson = prepStmt.executeQuery();

            if(!resultSetPerson.next()){
                return null;
            }

            person.setNameId(resultSetPerson.getInt("id"));
            person.setName(resultSetPerson.getString("name"));
            person.setGender(resultSetPerson.getString("gender"));

            prepStmt.close();
            resultSetPerson.close();
            
                query = "SELECT Distinct role_type.role "
                            + "FROM " + oracleSchemaStr + "role_type "
                            + "WHERE role_type.id IN"
                                + "(SELECT cast_info.role_id "
                                + "FROM " + oracleSchemaStr + "cast_info "
                                + "WHERE cast_info.person_id = ?)";
                prepStmt = oracleCon.prepareStatement(query);
                prepStmt.setInt(1, person.getNameId());
                resultSetPerson = prepStmt.executeQuery();
                while(resultSetPerson.next()){
                    person.getRoles().add(resultSetPerson.getString("role"));
                }
                resultSetPerson.close();
                prepStmt.close();

            return person;
        } catch (SQLException ex) {
            Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "Error while trying to load PreparedStatement: " + ex.getMessage());
        }

        return null;
    }
    
    public ObservableList<Person> getPersonsByKeyword(String keywords, int match) {
        PreparedStatement prepStmt;
        ObservableList<Person> personList = FXCollections.observableArrayList();

        // Initialize query
        String query = "SELECT name.id, name.name, name.gender "
                + "FROM " + oracleSchemaStr + "name "
                + "WHERE ";

        String[] keywordArray = keywords.split(" ");

        // Fill the query for the prepared Statement
        switch (match) {
            case 1:
                // match all
                for (int i = 0; i < keywordArray.length; i++) {
                    query += "name.name LIKE ? ";
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
                    query += "name.name LIKE ? ";
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
                query += "name.name LIKE ? ";
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

            ResultSet resultSetPerson = prepStmt.executeQuery();

            while (resultSetPerson.next()) {
                Person person = new Person();
                
                person.setNameId(resultSetPerson.getInt("id"));
                person.setName(resultSetPerson.getString("name"));
                person.setGender(resultSetPerson.getString("gender"));
                
                
                PreparedStatement prepStmtTmp;


                // Initialize query
                String queryTmp = "SELECT info "
                                + "FROM " + oracleSchemaStr + "person_info "
                                + "WHERE person_info.person_id = ? AND person_info.info_type_id = ?";

                try {
                    prepStmtTmp = oracleCon.prepareStatement(queryTmp);
                    prepStmtTmp.setInt(1, person.getNameId());
                    prepStmtTmp.setInt(2, 21);
                    ResultSet resultSetPersonInfo = prepStmtTmp.executeQuery();

                    if(resultSetPersonInfo.next()){
                        person.setBirthDate(resultSetPersonInfo.getString("info"));
                    }

                    prepStmtTmp.close();
                    resultSetPersonInfo.close();

                    } catch (SQLException ex) {
                        Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "Error while trying to load PreparedStatement: " + ex.getMessage());
                    }
                
                
                personList.add(person);
            }
            prepStmt.close();
            resultSetPerson.close();

            return personList;
        } catch (SQLException ex) {
            Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "Error while trying to load PreparedStatement: " + ex.getMessage());
        }

        return null;
    }
    
    public ObservableList<Person> getPersonsByTitleId(int titleId){
        PreparedStatement prepStmt;
        ObservableList<Person> personList = FXCollections.observableArrayList();
        Person person = new Person();

        // Initialize query
        String query = "SELECT cast_info.person_id "
                + "FROM " + oracleSchemaStr + "cast_info "
                + "WHERE movie_id = ?";

        try {
            prepStmt = oracleCon.prepareStatement(query);
            prepStmt.setInt(1, titleId);
            ResultSet resultSetPerson = prepStmt.executeQuery();

            while(resultSetPerson.next()){
                if(handleNullValue(resultSetPerson.getInt("person_id"), resultSetPerson) != null){
                    person = getPersonById(resultSetPerson.getInt("person_id"));
                    personList.add(person);
                }
            }
            prepStmt.close();
            resultSetPerson.close();
            
            

            return personList;
        } catch (SQLException ex) {
            Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "Error while trying to load PreparedStatement: " + ex.getMessage());
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
