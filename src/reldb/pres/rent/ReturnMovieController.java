/*
 * @author Andreas Diesendorf (2343841) <andreas_d@hotmail.de>, Björn Burzlaff (2874851) <bjoern.b96@web.de>
 */
package reldb.pres.rent;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import reldb.app.RentApp;
import reldb.bdo.Customer;
import reldb.bdo.RentTitle;
import reldb.pres.ImdbSearch;


public class ReturnMovieController implements Initializable {
    
    
    @FXML
    private TableView<RentTitle> rentTable;
    @FXML
    private TableColumn<RentTitle, String> titleCol;
    @FXML
    private TableColumn<RentTitle, LocalDate> rentDateCol;
    @FXML
    private TableColumn<RentTitle, LocalDate> returnDateCol;
    @FXML
    private TableColumn<RentTitle, String> noteCol;
    
    @FXML
    private Label titleLabel;
    @FXML
    private DatePicker rentDatePicker;
    @FXML
    private DatePicker returnDatePicker;
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
            
        rentTitle = new RentTitle();
        titleCol.setCellValueFactory(new PropertyValueFactory<>("titleName"));
        rentDateCol.setCellValueFactory(new PropertyValueFactory<>("rentDate"));
        returnDateCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        noteCol.setCellValueFactory(new PropertyValueFactory<>("note"));
    }
    
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void setCustomer(Customer customer){
        this.customer = customer;
        this.rentTitleList = rent.getRentsList(customer);
        this.rentTable.setItems(rentTitleList);
    }
    
    public void save(){
        rentTitle.setNote(noteField.getText());
        rentTitle.setRentDate(rentDatePicker.getValue());
        rentTitle.setReturnDate(returnDatePicker.getValue());
        rent.updateRent(rentTitle, customer);
        rentTable.refresh();
    }
    
    public void transfer(){
        
        if(!rentTable.getSelectionModel().isEmpty()){
            rentTitle = rentTable.getSelectionModel().getSelectedItem();
            titleLabel.setText(rentTable.getSelectionModel().getSelectedItem().getTitle().getTitle());
            rentDatePicker.setValue(rentTable.getSelectionModel().getSelectedItem().getRentDate());
            returnDatePicker.setValue(rentTable.getSelectionModel().getSelectedItem().getReturnDate());
            noteField.setText(rentTable.getSelectionModel().getSelectedItem().getNote());
        }else{
            System.out.println("\n Es ist keine Zeile ausgewählt. \n");
        }
    }
    
    public void rate(){
        if(!rentTable.getSelectionModel().isEmpty()){
            rentTitle = rentTable.getSelectionModel().getSelectedItem();
        try{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ImdbSearch.class.getResource("rent/rateMovie.fxml"));

        Parent rateMovieController = loader.load();
        
        RateMovieController controller = loader.getController();

        Scene scene = new Scene(rateMovieController);
        Stage RateMovieStage = new Stage();
        //modal.initModality(Modality.APPLICATION_MODAL);
        RateMovieStage.setTitle("Bewerten");
        RateMovieStage.setMinWidth(250);
        RateMovieStage.setScene(scene);
        controller.setStage(RateMovieStage);
        
        RateMovieStage.show();
        
        controller.setTitle(this.customer,rentTitle);
        
        }catch(IOException e){
            System.out.println(e);
        }
        }else{
            System.out.println("Sie haben keinen Film ausgewählt");
        }
        
    }
}
