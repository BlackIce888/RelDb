/*
 * @author Andreas Diesendorf (2343841) <andreas_d@hotmail.de>, Björn Burzlaff (2874851) <bjoern.b96@web.de>
 */
package reldb.pres.rent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import reldb.app.RentApp;
import reldb.bdo.Customer;
import reldb.bdo.RentTitle;
import reldb.bdo.Title;
import reldb.pres.ImdbSearch;


public class RentMovieController implements Initializable {
    
    @FXML
    private Label titleLabel;
    @FXML
    private Label idLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private DatePicker dateField;
    @FXML
    private TextField noteField;
    
    
    private Stage stage;
    private RentApp rentApp;
    private Title title;
    private Customer customer;
    
    
    public void initialize(URL location, ResourceBundle resources) {
            rentApp = new RentApp();
            customer = new Customer();
    }
    
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void setTitle(Title title){
        this.title = title;
        titleLabel.setText(title.getTitle());
    }
    
    public void setCustomer(Customer customer){
        this.customer = customer;
        idLabel.setText(String.valueOf(customer.getId()));
        nameLabel.setText(customer.getName());
    }
    
    public void rent(){
        RentTitle renttitle = new RentTitle();
        renttitle.setRentDate(dateField.getValue());
        renttitle.setNote(noteField.getText());
        renttitle.setTitle(title);
        
        rentApp.rent(renttitle,customer);
        
        System.out.println("Erfolgreich ausgeliehen.");
        this.stage.close();
    }
    
   public void getCustomer(){
        try{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ImdbSearch.class.getResource("rent/createCustomer.fxml"));

        Parent createCustomerController = loader.load();
        
        CreateCustomerController controller = loader.getController();

        Scene scene = new Scene(createCustomerController);
        Stage CreateCustomerStage = new Stage();
        //modal.initModality(Modality.APPLICATION_MODAL);
        CreateCustomerStage.setTitle("Kunde");
        CreateCustomerStage.setMinWidth(250);
        CreateCustomerStage.setScene(scene);
        controller.setStage(CreateCustomerStage);
        
        CreateCustomerStage.show();
        
        controller.setController(this);
        
        }catch(IOException e){
            System.out.println(e);
        }
       
   }
}
