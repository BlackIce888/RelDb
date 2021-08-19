/*
 * @author Andreas Diesendorf (2343841) <andreas_d@hotmail.de>, Björn Burzlaff (2874851) <bjoern.b96@web.de>
 */
package reldb.pres.rent;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import reldb.app.RentApp;
import reldb.bdo.Customer;
import reldb.bdo.RentTitle;


public class GetCustomerController implements Initializable {
    
    
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, Integer> idCol;
    @FXML
    private TableColumn<Customer, String> nameCol;
    @FXML
    private TableColumn<Customer, LocalDate> birthdateCol;
    @FXML
    private TableColumn<Customer, String> streetCol;
    
    private Stage stage;
    
    private RentApp rent;
    
    private CreateCustomerController customerController;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rent = new RentApp();
            
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        birthdateCol.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
        streetCol.setCellValueFactory(new PropertyValueFactory<>("street"));
            
        this.customerTable.setItems(rent.getCustomersList());
    }
    
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void setController(CreateCustomerController customerController){
        this.customerController = customerController;
    }
    
    public void select(){
        if(!customerTable.getSelectionModel().isEmpty()){
            customerController.setCustomer(customerTable.getSelectionModel().getSelectedItem());
            this.stage.close();
        }else{
            System.out.println("Sie haben keinen Kunden ausgewählt.");
        }
    }
    

}
