/*
 * @author Andreas Diesendorf (2343841) <andreas_d@hotmail.de>, Björn Burzlaff (2874851) <bjoern.b96@web.de>
 */
package reldb.pres.rent;

import java.net.URL;
import java.time.LocalDate;
import static java.util.Objects.isNull;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import reldb.app.RentApp;
import reldb.bdo.Customer;
import reldb.bdo.RentTitle;


public class RateMovieController implements Initializable {
    
    @FXML
    private Label titleLabel;
    @FXML
    private Slider rateSlider;
    @FXML
    private TextField noteField;
    
    private Stage stage;
    
    private RentApp rent;
    
    private Customer customer;
    
    private RentTitle rentTitle;
    
    private ObservableList<RentTitle> rentTitleList;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rent = new RentApp();
            
    }
    
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void setTitle(Customer customer, RentTitle rentTitle){
        this.rentTitle = rentTitle;
        this.customer = customer;
        rentTitle = rent.getRatingsByCustomer(customer,rentTitle);
        titleLabel.setText(rentTitle.getTitle().getTitle());
        if(!isNull(rentTitle.getRating())){
            rateSlider.setValue(rentTitle.getRating());
        }
        noteField.setText(rentTitle.getRatingNote());
    }
    
    public void save(){
        rentTitle.setRatingNote(noteField.getText());
        rentTitle.setRating(rateSlider.getValue());
        rent.updateRating(rentTitle, customer);
        this.stage.close();
    }
    

    
}
