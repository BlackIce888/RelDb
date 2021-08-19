/*
 * @author Andreas Diesendorf (2343841) <andreas_d@hotmail.de>, Björn Burzlaff (2874851) <bjoern.b96@web.de>
 */
package reldb.pres.searchResults.detailPerson;

import java.net.URL;
import static java.util.Objects.isNull;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import reldb.bdo.PersonBiography;


public class Read implements Initializable{
    
    
    @FXML
    private TextArea bioLabel;
    
    private Stage stage;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void setBio(String bio){
        bioLabel.setText(bio);
    }
}
