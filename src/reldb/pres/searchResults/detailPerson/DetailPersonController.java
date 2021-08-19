/*
 * @author Andreas Diesendorf (2343841) <andreas_d@hotmail.de>, Björn Burzlaff (2874851) <bjoern.b96@web.de>
 */
package reldb.pres.searchResults.detailPerson;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import reldb.bdo.Person;
import reldb.bdo.Title;
import reldb.data.PersonDAOImpl;
import reldb.data.TitleDAOImpl;
import reldb.pres.searchResults.SearchResultsController;
import reldb.pres.searchResults.detailTitle.DetailTitleController;

public class DetailPersonController implements Initializable {
    
    @FXML
    private TableView<Title> titleTable;
    @FXML
    private TableColumn<Title, String> titleCol;
    @FXML
    private TableColumn<Title, Integer> yearCol;
    @FXML
    private TableColumn<Title, String> kindCol;
    
    @FXML
    private Label nameLabel;
    @FXML
    private Label heightLabel;
    @FXML
    private Label birthDateLabel;
    @FXML
    private Label birthNotesLabel;
    @FXML
    private Label deathDateLabel;
    @FXML
    private Label deathNotesLabel;
    @FXML
    private Label rolesLabel;
    @FXML
    private TextArea biographieLabel;
    @FXML
    private Button biographieButton;
    
    
    private Stage stage;
    
    private Person person;
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("production_year"));
        kindCol.setCellValueFactory(new PropertyValueFactory<>("kind"));
        
        
    }
    
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void setPerson(Person personVar){
        this.person = personVar;
        
        PersonDAOImpl personDAO = new PersonDAOImpl();
        TitleDAOImpl titleDAO = new TitleDAOImpl();

        ObservableList<Title> titleList = FXCollections.observableArrayList();
        
        person = personDAO.getFullPersonById(person.getNameId());
        
        nameLabel.setText(person.getName());
        heightLabel.setText(person.getHeight());
        birthDateLabel.setText(person.getBirthDate());
        birthNotesLabel.setText(person.getBirthNotes());
        deathDateLabel.setText(person.getDeathDate());
        deathNotesLabel.setText(person.getDeathNotes());
        if (!person.getBiographies().isEmpty()){
            biographieLabel.setText(person.getBiographies().get(0).getBiography());
        }
        for(String role : person.getRoles()){
            rolesLabel.setText(rolesLabel.getText() + " " + role);
        }
        
        titleTable.setItems(titleDAO.getTitlesByPersonId(person.getNameId()));
        
    }
    
    
    public void showBio(){
        try{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SearchResultsController.class.getResource("detailPerson/biography.fxml"));

        Parent biographyController = loader.load();
        
        BiographyController controller = loader.getController();

        Scene scene = new Scene(biographyController);
        Stage biographyStage = new Stage();
        //modal.initModality(Modality.APPLICATION_MODAL);
        biographyStage.setTitle("Biography");
        biographyStage.setMinWidth(250);
        biographyStage.setScene(scene);
        controller.setStage(biographyStage);
        
        biographyStage.show();

        controller.setBio(person.getBiographies());
        } catch(IOException e){
                System.out.println(e);
        }
    }
    
    public void showTitleDetail(){
        try{
                if(!titleTable.getSelectionModel().isEmpty()){
                    showDetailTitle(titleTable.getSelectionModel().getSelectedItem());
                }else{
                    System.out.println("\n Es ist keine Zeile ausgewählt. \n");
                }
            } catch(IOException e){
                System.out.println(e);
            }
    }
    
    private void showDetailTitle (Title title)throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SearchResultsController.class.getResource("detailTitle/detailTitle.fxml"));

        Parent detailTitleController = loader.load();
        
        DetailTitleController controller = loader.getController();

        Scene scene = new Scene(detailTitleController);
        Stage detailTitleStage = new Stage();
        //modal.initModality(Modality.APPLICATION_MODAL);
        detailTitleStage.setTitle("Titeldetailansischt");
        detailTitleStage.setMinWidth(250);
        detailTitleStage.setScene(scene);
        controller.setStage(detailTitleStage);
        
        detailTitleStage.show();

        controller.setTitle(title);
    }
    
}
