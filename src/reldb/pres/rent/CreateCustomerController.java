/*
 * @author Andreas Diesendorf (2343841) <andreas_d@hotmail.de>, Björn Burzlaff (2874851) <bjoern.b96@web.de>
 */
package reldb.pres.rent;

import java.io.IOException;
import java.net.URL;
import static java.util.Objects.isNull;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import reldb.app.RentApp;
import reldb.bdo.Customer;
import reldb.bdo.Title;
import reldb.pres.ImdbSearch;


public class CreateCustomerController implements Initializable {
    
    
    @FXML
    private TextField nameField;
    @FXML
    private DatePicker birthdateField;
    @FXML
    private TextField streetField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField postcodeField;
    @FXML
    private TextField idField;

    
    private Stage stage;
    
    private RentApp rent;
    
    private RentMovieController rentController;
    private Customer customer;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
            rent = new RentApp();
    }
    
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void setController(RentMovieController rentController){
        this.rentController = rentController;
    }
    
    public void setCustomer(Customer customer){
        this.customer = customer;
        nameField.setText(customer.getName());
        birthdateField.setValue(customer.getBirthdate());
        streetField.setText(customer.getStreet());
        cityField.setText(customer.getCity());
        postcodeField.setText(customer.getPostcode());
    }
    

    public void createCustomer(){
        customer = new Customer();
        customer.setName(nameField.getText());
        customer.setBirthdate(birthdateField.getValue());
        customer.setStreet(streetField.getText());
        customer.setCity(cityField.getText());
        customer.setPostcode(postcodeField.getText());
        rent.createCustomer(customer);
        
    }
    
    public void deleteCustomer(){
        customer.setName(nameField.getText());
        customer.setBirthdate(birthdateField.getValue());
        customer.setStreet(streetField.getText());
        customer.setCity(cityField.getText());
        customer.setPostcode(postcodeField.getText());
        rent.deleteCustomer(customer);
    }
    
    public void updateCustomer(){
        customer.setName(nameField.getText());
        customer.setBirthdate(birthdateField.getValue());
        customer.setStreet(streetField.getText());
        customer.setCity(cityField.getText());
        customer.setPostcode(postcodeField.getText());
        rent.updateCustomer(customer);
    }
    
    public void selectCustomerById(){
        customer = rent.getCustomerById(Integer.parseInt(idField.getText()));
        nameField.setText(customer.getName());
        birthdateField.setValue(customer.getBirthdate());
        streetField.setText(customer.getStreet());
        cityField.setText(customer.getCity());
        postcodeField.setText(customer.getPostcode());
    }
    
    public void returnCustomer(){
        
    }
    
    public void rent(){
        if(!isNull(rentController)){
            rentController.setCustomer(this.customer);
        }
        this.stage.close();
    }
    
    public void returnTitle(){
        try{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ImdbSearch.class.getResource("rent/returnMovie.fxml"));

        Parent returnMovieController = loader.load();
        
        ReturnMovieController controller = loader.getController();

        Scene scene = new Scene(returnMovieController);
        Stage ReturnMovieStage = new Stage();
        //modal.initModality(Modality.APPLICATION_MODAL);
        ReturnMovieStage.setTitle("Film Zurückgeben");
        ReturnMovieStage.setMinWidth(250);
        ReturnMovieStage.setScene(scene);
        controller.setStage(ReturnMovieStage);
        
        ReturnMovieStage.show();
        
        controller.setCustomer(customer);
        
        }catch(IOException e){
            System.out.println(e);
        }
    }
    
    public void selectCustomerByList(){
        try{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ImdbSearch.class.getResource("rent/getCustomer.fxml"));

        Parent getCustomerController = loader.load();
        
        GetCustomerController controller = loader.getController();

        Scene scene = new Scene(getCustomerController);
        Stage GetCustomerStage = new Stage();
        //modal.initModality(Modality.APPLICATION_MODAL);
        GetCustomerStage.setTitle("Kunden Liste");
        GetCustomerStage.setMinWidth(250);
        GetCustomerStage.setScene(scene);
        controller.setStage(GetCustomerStage);
        
        GetCustomerStage.show();
        
        controller.setController(this);
        
        }catch(IOException e){
            System.out.println(e);
        }
    }
    
    
}
