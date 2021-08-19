/*
 * @author Andreas Diesendorf (2343841) <andreas_d@hotmail.de>, Björn Burzlaff (2874851) <bjoern.b96@web.de>
 */
package reldb.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static java.util.Objects.isNull;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import reldb.bdo.Customer;
import reldb.bdo.DatabaseSettings;
import reldb.bdo.RentTitle;
import reldb.bdo.Title;


public class CustomerDAOImpl {
    
    private DbConnectionSingletonFactory dbConFactory;
    private DbConnection dbCon;
    private Connection oracleCon;
    private String oracleSchemaStr;
    private String userNameclean;
    private String userName;
    
    public CustomerDAOImpl(){
        dbConFactory = DbConnectionSingletonFactory.getDbConnectionSingletonFactory();
        dbCon = dbConFactory.getDbConnection("oracle");
        oracleCon = dbCon.getConnection();
        if (!DatabaseSettings.ORACLE_SCHEMA.isEmpty()) {
            oracleSchemaStr = DatabaseSettings.ORACLE_SCHEMA + ".";
        }
        try {
            oracleCon.setAutoCommit(false);
            userNameclean = dbCon.getConnection().getSchema();
            userName = userNameclean + ".";
        } catch (SQLException ex) {
            Logger.getLogger(MovieCompanyDAOImpl.class.getName()).log(Level.SEVERE, "Error setting autocommit: " + ex);
        }
    }
    
    
    public void createCustomer(Customer customer) {
        PreparedStatement prepStmt;

        String query = "INSERT INTO " + userName + "customer (id,name, birthdate, street, city, postcode) VALUES (customer_seq.nextval,?,?,?,?,?)";
        try {
            prepStmt = oracleCon.prepareStatement(query);
            prepStmt.setString(1, customer.getName());
            if (!isNull(customer.getBirthdate())) {
                prepStmt.setDate(2, new java.sql.Date(customer.getBirthdate().toEpochDay() * 1000 * 60 * 60 * 24));
            } else {
                prepStmt.setDate(2, null);
            }
            prepStmt.setString(3, customer.getStreet());
            prepStmt.setString(4, customer.getCity());
            prepStmt.setString(5, customer.getPostcode());
            prepStmt.executeUpdate();
            Statement stmt = oracleCon.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT customer_seq.currval AS id FROM DUAL");
            if (rs.next()) {
                customer.setId(rs.getInt("id"));
            } else {
                customer.setId(0);
            }
            oracleCon.commit();

            prepStmt.close();

            
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAOImpl.class.getName()).log(Level.SEVERE, "Error while saving new customer: "
                    + ex.getMessage());
        }
        
    }
    
    public void updateCustomer(Customer customer) {
        PreparedStatement prepStmt;
        String query = "UPDATE " + userName + "customer SET name = ?, birthdate = ?, street = ?, city = ?, postcode = ? WHERE id = ?";
        try {
            prepStmt = oracleCon.prepareStatement(query);
            prepStmt.setString(1, customer.getName());
            if (!isNull(customer.getBirthdate())) {
                prepStmt.setDate(2, new java.sql.Date(customer.getBirthdate().toEpochDay() * 1000 * 60 * 60 * 24));
            } else {
                prepStmt.setDate(2, null);
            }
            prepStmt.setString(3, customer.getStreet());
            prepStmt.setString(4, customer.getCity());
            prepStmt.setString(5, customer.getPostcode());
            prepStmt.setInt(6, customer.getId());
            prepStmt.executeUpdate();
            oracleCon.commit();
            prepStmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAOImpl.class.getName()).log(Level.SEVERE, "Error while updating customer: "
                    + ex.getMessage());
        }
    }
    
    public void deleteCustomer(Customer customer) {
        PreparedStatement prepStmt;
        String query = "DELETE FROM " + userName + "customer WHERE id = ?";
        try {
            prepStmt = oracleCon.prepareStatement(query);
            prepStmt.setInt(1, customer.getId());
            prepStmt.executeUpdate();
            oracleCon.commit();
            prepStmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAOImpl.class.getName()).log(Level.SEVERE, "Error while deleting customer: "
                    + ex.getMessage());
        }
    }
    
    public Customer getCustomerById(Integer id) {
        PreparedStatement prepStmt;
        Customer customer = new Customer();
        ResultSet resultSetCustomer;
        String query = "SELECT id, name, birthdate, street, city, postcode FROM " + userName + "customer WHERE id = ?";
        try {
            prepStmt = oracleCon.prepareStatement(query);
            prepStmt.setInt(1, id);
            resultSetCustomer = prepStmt.executeQuery();
            while (resultSetCustomer.next()) {
                customer.setId(resultSetCustomer.getInt("id"));
                customer.setName(resultSetCustomer.getString("name"));
                if(!isNull(resultSetCustomer.getDate("birthdate"))){
                    customer.setBirthdate(resultSetCustomer.getDate("birthdate").toLocalDate());
                }
                customer.setStreet(resultSetCustomer.getString("street"));
                customer.setCity(resultSetCustomer.getString("city"));
                customer.setPostcode(resultSetCustomer.getString("postcode"));
            }
            prepStmt.close();
            resultSetCustomer.close();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAOImpl.class.getName()).log(Level.SEVERE, "Error while querying database: "
                    + ex.getMessage());
            return null;
        }
        return customer;
    }
    
    
    
    public ObservableList<Customer> getCustomersList() {
        PreparedStatement prepStmt;
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        Customer customer = new Customer();
        ResultSet resultSetCustomer;
        String query = "SELECT id, name, birthdate, street, city, postcode FROM " + userName + "customer";
        try {
            prepStmt = oracleCon.prepareStatement(query);
            resultSetCustomer = prepStmt.executeQuery();
            while (resultSetCustomer.next()) {
                customer = new Customer();
                customer.setId(resultSetCustomer.getInt("id"));
                customer.setName(resultSetCustomer.getString("name"));
                if(!isNull(resultSetCustomer.getDate("birthdate"))){
                    customer.setBirthdate(resultSetCustomer.getDate("birthdate").toLocalDate());
                }
                customer.setStreet(resultSetCustomer.getString("street"));
                customer.setCity(resultSetCustomer.getString("city"));
                customer.setPostcode(resultSetCustomer.getString("postcode"));
                customerList.add(customer);
            }
            prepStmt.close();
            resultSetCustomer.close();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAOImpl.class.getName()).log(Level.SEVERE, "Error while querying database: "
                    + ex.getMessage());
            return null;
        }
        return customerList;
    }
    
    
    
    public void createRent(RentTitle rent, Customer customer) {
        PreparedStatement prepStmt;
        String query = "INSERT INTO " + userName + "title_rent (id,customer_id, movie_id, rent_date, return_date, note) VALUES (title_rent_seq.nextval,?,?,?,?,?)";
        try {
            prepStmt = oracleCon.prepareStatement(query);
            prepStmt.setInt(1, customer.getId());
            prepStmt.setInt(2, rent.getTitle().getId());
            if (!isNull(rent.getRentDate())) {
                prepStmt.setDate(3, new java.sql.Date(rent.getRentDate().toEpochDay() * 1000 * 60 * 60 * 24));
            }
            if (!isNull(rent.getReturnDate())) {
                prepStmt.setDate(4, new java.sql.Date(rent.getReturnDate().toEpochDay() * 1000 * 60 * 60 * 24));
            } else {
                prepStmt.setDate(4, null);
            }
            prepStmt.setString(5, rent.getNote());
            prepStmt.executeUpdate();
            Statement stmt = oracleCon.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT title_rent_seq.currval AS id FROM DUAL");
            if (rs.next()) {
                rent.setId(rs.getInt("id"));
            } else {
                rent.setId(0);
            }
            oracleCon.commit();
            prepStmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAOImpl.class.getName()).log(Level.SEVERE, "Error while saving new titleRent: " 
                    + ex.getMessage());
        }
    }
    
    public void updateRent(RentTitle rent, Customer customer) {
        PreparedStatement prepStmt;
        String query = "UPDATE " + userName + "title_rent SET customer_id = ?, movie_id = ?, rent_date = ?, return_date = ?, note = ? "
                + "WHERE id = ?";
        try {
            prepStmt = oracleCon.prepareStatement(query);
            prepStmt.setInt(1, customer.getId());
            prepStmt.setInt(2, rent.getTitle().getId());
            if (!isNull(rent.getRentDate())) {
                prepStmt.setDate(3, new java.sql.Date(rent.getRentDate().toEpochDay() * 1000 * 60 * 60 * 24));
            } else {
                prepStmt.setDate(3, null);
            }
            if (!isNull(rent.getReturnDate())) {
                prepStmt.setDate(4, new java.sql.Date(rent.getReturnDate().toEpochDay() * 1000 * 60 * 60 * 24));
            } else {
                prepStmt.setDate(4, null);
            }
            prepStmt.setString(5, rent.getNote());
            prepStmt.setInt(6, rent.getId());
            prepStmt.executeUpdate();
            oracleCon.commit();
            prepStmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAOImpl.class.getName()).log(Level.SEVERE, "Error while updating titleRent: " 
                    + ex.getMessage());
        }
    }
    
    
    
    public ObservableList<RentTitle> getRentsList(Customer customer) {
        PreparedStatement prepStmt;
        ObservableList<RentTitle> ratingList = FXCollections.observableArrayList();
        ResultSet resultSetRating;
        String query;
        TitleDAOImpl titleDAO = new TitleDAOImpl();
        Title title;
        try {
            query = "SELECT * "
                    + "FROM " + userName + "title_rent "
                    + "WHERE customer_id = ?";
            prepStmt = oracleCon.prepareStatement(query);
            prepStmt.setInt(1, customer.getId());
            resultSetRating = prepStmt.executeQuery();

            while (resultSetRating.next()) {
                title = new Title();
                RentTitle rent = new RentTitle();
                rent.setId(resultSetRating.getInt("id"));
                rent.setRentDate(resultSetRating.getDate("rent_date").toLocalDate());
                if(!isNull(resultSetRating.getDate("return_date"))){
                    rent.setReturnDate(resultSetRating.getDate("return_date").toLocalDate());
                }
                rent.setNote(resultSetRating.getString("note"));
                title = titleDAO.getTitleById(resultSetRating.getInt("movie_id"));
                rent.setTitle(title);
                ratingList.add(rent);
            }
            prepStmt.close();
            resultSetRating.close();
            return ratingList;
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAOImpl.class.getName()).log(Level.SEVERE, "Error while trying to process query: "
                    + ex.getMessage(), ex);
        }
        return null;
    }
    
    
    public RentTitle getRatingsByCustomer(Customer customer, RentTitle title) {
        PreparedStatement prepStmt;
        ObservableList<RentTitle> ratingList = FXCollections.observableArrayList();
        ResultSet resultSetRating;
        String query;
        TitleDAOImpl titleDAO = new TitleDAOImpl();
        try {
            query = "SELECT title_rating.id, title_rating.rating, title_rating.note "
                    + "FROM " + userName + "title_rating "
                    + "WHERE customer_id = ? AND movie_id = ?";
            prepStmt = oracleCon.prepareStatement(query);
            prepStmt.setInt(1, customer.getId());
            prepStmt.setInt(2, title.getTitle().getId());
            resultSetRating = prepStmt.executeQuery();
            
            RentTitle rating = new RentTitle();

            while(resultSetRating.next()){
            rating.setRatingId(resultSetRating.getInt("id"));
            rating.setRating(resultSetRating.getDouble("rating"));
            rating.setRatingNote(resultSetRating.getString("note"));
            ratingList.add(rating);
            }
            prepStmt.close();
            resultSetRating.close();
            return rating;
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAOImpl.class.getName()).log(Level.SEVERE, "Error while trying to process query: "
                    + ex.getMessage(), ex);
        }
        return null;
    }
    
    public void createRating(RentTitle rent, Customer customer) {
        PreparedStatement prepStmt;
        String query = "INSERT INTO " + userName + "title_rating (id,customer_id, movie_id, rating, note) VALUES (title_rating_seq.nextval,?,?,?,?)";
        try {
            prepStmt = oracleCon.prepareStatement(query);
            prepStmt.setInt(1, customer.getId());
            prepStmt.setInt(2, rent.getTitle().getId());
            prepStmt.setDouble(3, rent.getRating());
            prepStmt.setString(4, rent.getRatingNote());
            prepStmt.executeUpdate();
            Statement stmt = oracleCon.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT title_rating_seq.currval AS id FROM DUAL");
            if (rs.next()) {
                rent.setRatingId(rs.getInt("id"));
            } else {
                rent.setRatingId(0);
            }
            oracleCon.commit();
            prepStmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAOImpl.class.getName()).log(Level.SEVERE, "Error while saving new titleRating: " 
                    + ex.getMessage());
        }
    }
    
    public void updateRating(RentTitle rent, Customer customer) {
        PreparedStatement prepStmt;
        String query = "UPDATE " + userName + "title_rating SET customer_id = ?, movie_id = ?, rating = ?, note = ? "
                + "WHERE id = ?";
        try {
            prepStmt = oracleCon.prepareStatement(query);
            prepStmt.setInt(1, customer.getId());
            prepStmt.setInt(2, rent.getTitle().getId());
            prepStmt.setDouble(4, rent.getRating());
            prepStmt.setString(4, rent.getRatingNote());
            prepStmt.setInt(5, rent.getRatingId());
            prepStmt.executeUpdate();
            oracleCon.commit();
            prepStmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAOImpl.class.getName()).log(Level.SEVERE, "Error while updating titleRating: " 
                    + ex.getMessage());
        }
    }
    
}
