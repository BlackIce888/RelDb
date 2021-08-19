/*
 * @author Andreas Diesendorf (2343841) <andreas_d@hotmail.de>, Björn Burzlaff (2874851) <bjoern.b96@web.de>
 */
package reldb.pres.searchResults.detailPerson;

import java.io.IOException;
import java.net.URL;
import static java.util.Objects.isNull;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import reldb.bdo.PersonBiography;
import reldb.pres.ImdbSearch;
import reldb.pres.rent.RentMovieController;


public class BiographyController implements Initializable{
    
    @FXML
    private TableView<PersonBiography> bioTable;
    @FXML
    private TableColumn<PersonBiography, String> biographyCol;
    @FXML
    private TableColumn<PersonBiography, String> noteCol;
    
    
    private Stage stage;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        biographyCol.setCellValueFactory(new PropertyValueFactory<>("biography"));
        noteCol.setCellValueFactory(new PropertyValueFactory<>("note"));
    }
    
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void setBio(ObservableList<PersonBiography> bioList){
        if (isNull(bioList) || bioList.isEmpty()) {
        } else {
            this.bioTable.setItems(bioList);
        }
    }
    
    public void read(){
        
        try{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ImdbSearch.class.getResource("searchResults/detailPerson/read.fxml"));

        Parent read = loader.load();
        
        Read controller = loader.getController();

        Scene scene = new Scene(read);
        Stage ReadStage = new Stage();
        //modal.initModality(Modality.APPLICATION_MODAL);
        ReadStage.setTitle("Ausleihen");
        ReadStage.setMinWidth(250);
        ReadStage.setScene(scene);
        controller.setStage(ReadStage);
        
        ReadStage.show();
        
        controller.setBio(bioTable.getSelectionModel().getSelectedItem().getBiography());
        
        }catch(IOException e){
            System.out.println(e);
        }
        bioTable.getSelectionModel().getSelectedItem().getBiography();
    }
    
}
