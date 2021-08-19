/*
 *  Copyright 2014 Rainer Stoermer
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
import reldb.bdo.MovieCompany;

/**
 *
 * @author Rainer Stoermer
 */
public class MovieCompanyDAOImpl implements MovieCompanyDAO {

    private DbConnectionSingletonFactory dbConFactory;
    private DbConnection dbCon;
    private Connection oracleCon;
    private String oracleSchemaStr;

    /**
     * constructor, initializes Database-Connection
     */
    public MovieCompanyDAOImpl() {
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

    /**
     * retrieves a list of movie_companies to a given title id.
     *
     * @param titleId
     *
     * @return list of movie_companies
     */
    @Override
    public ObservableList<MovieCompany> getMovieCompaniesByTitleId(Integer titleId) {
        PreparedStatement prepStmt;
        ObservableList<MovieCompany> companyList = FXCollections.observableArrayList();

        // Initialize query
        String query = "SELECT cn.name, mc.note, ct.kind "
                + "FROM " + oracleSchemaStr + "company_name cn, " + oracleSchemaStr + "movie_companies mc JOIN " + oracleSchemaStr + "company_type ct ON mc.company_type_id = ct.id "
                + "WHERE mc.movie_id = ? AND cn.id = mc.company_id";

        try {
            prepStmt = oracleCon.prepareStatement(query);
            prepStmt.setInt(1, titleId);
            ResultSet companyRs = prepStmt.executeQuery();

            while (companyRs.next()) {
                MovieCompany company = new MovieCompany();

                // Set content of current company
                company.setName(companyRs.getString("name"));
                company.setNote(companyRs.getString("note"));
                company.setKind(companyRs.getString("kind"));

                companyList.add(company);
            }
            prepStmt.close();
            companyRs.close();

            return companyList;
        } catch (SQLException ex) {
            Logger.getLogger(MovieCompanyDAOImpl.class.getName()).log(Level.SEVERE, "Error while trying to load PreparedStatement: " + ex.getMessage());
        }

        return null;
    }

    /**
     * retrieves a list of movie_companies to a given keyword-String
     *
     * @param keywords seperated by blanks
     * @param match    1: match all, 2: match any, 3: match exact
     *
     * @return list of movie_companies
     */
    @Override
    public ObservableList<MovieCompany> getMovieCompaniesByKeyword(String keywords, int match) {
        PreparedStatement prepStmt;
        ObservableList<MovieCompany> companyList = FXCollections.observableArrayList();

        // Initialize query
        String query = "SELECT cn.name, mc.note, ct.kind "
                + "FROM " + oracleSchemaStr + "company_name cn, " + oracleSchemaStr + "movie_companies mc JOIN " + oracleSchemaStr + "company_type ct ON mc.company_type_id = ct.id "
                + "WHERE cn.id = mc.company_id AND ";

        String[] keywordArray = keywords.split(" ");

        // Fill the query for the prepared Statement
        switch (match) {
            case 1:
                // match all
                for (int i = 0; i < keywordArray.length; i++) {
                    query += "cn.name LIKE ? ";
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
                    query += "cn.name LIKE ? ";
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
                query += "cn.name LIKE ? ";
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

            ResultSet companyRs = prepStmt.executeQuery();

            while (companyRs.next()) {
                MovieCompany company = new MovieCompany();

                // Set content of current company
                company.setName(companyRs.getString("name"));
                company.setNote(companyRs.getString("note"));
                company.setKind(companyRs.getString("kind"));

                companyList.add(company);
            }
            prepStmt.close();
            companyRs.close();

            return companyList;
        } catch (SQLException ex) {
            Logger.getLogger(MovieCompanyDAOImpl.class.getName()).log(Level.SEVERE, "Error while trying to load PreparedStatement: " + ex.getMessage());
        }

        return null;
    }
}
