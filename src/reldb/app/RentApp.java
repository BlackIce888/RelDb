/*
 * @author Andreas Diesendorf (2343841) <andreas_d@hotmail.de>, Björn Burzlaff (2874851) <bjoern.b96@web.de>
 */
package reldb.app;

import java.util.ArrayList;
import java.util.List;
import static java.util.Objects.isNull;
import javafx.collections.ObservableList;
import reldb.bdo.Column;
import reldb.bdo.Customer;
import reldb.bdo.ForeignKey;
import reldb.bdo.PrimaryKey;
import reldb.bdo.RentTitle;
import reldb.bdo.Table;
import reldb.bdo.Title;
import reldb.data.CreateTable;
import reldb.data.CustomerDAOImpl;

public class RentApp {
    
    
    private CreateTable create;
    
    private CustomerDAOImpl customerDAO;
    
    
    public RentApp(){
        CreateTable create = new CreateTable();
        this.customerDAO = new CustomerDAOImpl();
        
        List<Table> oracleTables = new ArrayList<>();
        Table table;
        
        List<PrimaryKey> primaryKeys = new ArrayList<>();
        PrimaryKey primary;
        
        List<ForeignKey> foreignKeys = new ArrayList<>();
        ForeignKey foreign;
        
        
        table = new Table();
        table.setTableName("customer");
        
        Column column = new Column();
        column.setColumnName("id");
        column.setColumnTypeName("int");
        column.setNullable(false);
        table.addColumn(column);
        
        column = new Column();
        column.setColumnName("name");
        column.setColumnTypeName("varchar(45)");
        column.setNullable(false);
        table.addColumn(column);
        
        column = new Column();
        column.setColumnName("birthdate");
        column.setColumnTypeName("date");
        column.setNullable(true);
        table.addColumn(column);
        
        column = new Column();
        column.setColumnName("Street");
        column.setColumnTypeName("varchar(50)");
        column.setNullable(true);
        table.addColumn(column);
        
        column = new Column();
        column.setColumnName("city");
        column.setColumnTypeName("varchar(50)");
        column.setNullable(true);
        table.addColumn(column);
        
        column = new Column();
        column.setColumnName("postcode");
        column.setColumnTypeName("char(5)");
        column.setNullable(true);
        table.addColumn(column);
        
        oracleTables.add(table);
        
        primary = new PrimaryKey();
        
        primary.setPrimaryKeyColumn("id");
        primary.setPrimaryKeyName("customer_id");
        primary.setTableName("customer");
        
        primaryKeys.add(primary);
        
        
        table = new Table();
        table.setTableName("title_rent");
        
        column = new Column();
        column.setColumnName("id");
        column.setColumnTypeName("int");
        column.setNullable(false);
        table.addColumn(column);
        
        column = new Column();
        column.setColumnName("customer_id");
        column.setColumnTypeName("int");
        column.setNullable(false);
        table.addColumn(column);
        
        column = new Column();
        column.setColumnName("movie_id");
        column.setColumnTypeName("int");
        column.setNullable(false);
        table.addColumn(column);
        
        column = new Column();
        column.setColumnName("rent_date");
        column.setColumnTypeName("date");
        column.setNullable(false);
        table.addColumn(column);
        
        column = new Column();
        column.setColumnName("return_date");
        column.setColumnTypeName("date");
        column.setNullable(true);
        table.addColumn(column);
        
        column = new Column();
        column.setColumnName("note");
        column.setColumnTypeName("varchar(255)");
        column.setNullable(true);
        table.addColumn(column);
        
        oracleTables.add(table);
        
        primary = new PrimaryKey();
        
        primary.setPrimaryKeyColumn("id");
        primary.setPrimaryKeyName("rent_id");
        primary.setTableName("title_rent");
        
        primaryKeys.add(primary);
        
        foreign = new ForeignKey();
        
        foreign.setTableName("customer");
        foreign.setColumnName("id");
        foreign.setForeignTableName("title_rent");
        foreign.setForeignColumn("customer_id");
        foreign.setForeignKeyName("rent_customer_id");
        
        foreignKeys.add(foreign);
        
        table = new Table();
        table.setTableName("title_rating");
        
        column = new Column();
        column.setColumnName("id");
        column.setColumnTypeName("int");
        column.setNullable(false);
        table.addColumn(column);
        
        column = new Column();
        column.setColumnName("customer_id");
        column.setColumnTypeName("int");
        column.setNullable(false);
        table.addColumn(column);
        
        column = new Column();
        column.setColumnName("movie_id");
        column.setColumnTypeName("int");
        column.setNullable(false);
        table.addColumn(column);
        
        column = new Column();
        column.setColumnName("rating");
        column.setColumnTypeName("int");
        column.setNullable(false);
        table.addColumn(column);
        
        column = new Column();
        column.setColumnName("note");
        column.setColumnTypeName("varchar(255)");
        column.setNullable(true);
        table.addColumn(column);
        
        oracleTables.add(table);
        
        primary = new PrimaryKey();
        
        primary.setPrimaryKeyColumn("id");
        primary.setPrimaryKeyName("rating_id");
        primary.setTableName("title_rating");
        
        primaryKeys.add(primary);
        
        foreign = new ForeignKey();
        
        foreign.setTableName("customer");
        foreign.setColumnName("id");
        foreign.setForeignTableName("title_rating");
        foreign.setForeignColumn("customer_id");
        foreign.setForeignKeyName("rating_customer_id");
        
        foreignKeys.add(foreign);
        
        List<String> existingTablenames;
        
        existingTablenames = create.existingTables(oracleTables);
        
        
        
        if(existingTablenames.isEmpty()){
            create.createTables(oracleTables);
            create.createPrimaryKeys(primaryKeys);
            create.createForeignKeys(foreignKeys);
        }
        
        
    }
    
    public void createCustomer(Customer customer){
        customerDAO.createCustomer(customer);
    }
    
    public void deleteCustomer(Customer customer){
        customerDAO.deleteCustomer(customer);
    }
    
    public void updateCustomer(Customer customer){
        customerDAO.updateCustomer(customer);
    }
    
    public void rent(RentTitle rent, Customer customer){
        customerDAO.createRent(rent, customer);
    }
    
    public void updateRent(RentTitle rent, Customer customer){
        customerDAO.updateRent(rent, customer);
    }
    
    
    public Customer getCustomerById(int id){
        return customerDAO.getCustomerById(id);
    }
    
    public ObservableList<Customer> getCustomersList(){
        return customerDAO.getCustomersList();
    }
    
    public RentTitle getRatingsByCustomer(Customer customer, RentTitle title){
        return customerDAO.getRatingsByCustomer(customer,title);
    }
    
    public void updateRating(RentTitle title, Customer customer){
        if (isNull(title.getRatingId())){
            customerDAO.createRating(title, customer);
        }else{
            customerDAO.updateRating(title, customer);
        }
    }
    
    public ObservableList<RentTitle> getRentsList(Customer customer){
        return customerDAO.getRentsList(customer);
    }
    
    
}
