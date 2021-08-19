/*
 * @author Andreas Diesendorf (2343841) <andreas_d@hotmail.de>, Björn Burzlaff (2874851) <bjoern.b96@web.de>
 */
package reldb.pres.searchResults;

import java.io.IOException;
import java.net.URL;
import static java.util.Objects.isNull;
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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import reldb.bdo.CharName;
import reldb.bdo.MovieCompany;
import reldb.bdo.Person;
import reldb.bdo.Title;
import reldb.data.CreateTable;
import reldb.pres.ImdbSearch;
import reldb.pres.searchResults.detailPerson.DetailPersonController;
import reldb.pres.searchResults.detailTitle.DetailTitleController;


public class SearchResultsController implements Initializable {

    @FXML
    private StackPane AnchorPane;
    @FXML
    private TabPane searchResultsTabPane;

    @FXML
    private Tab titleTab;
    @FXML
    private TableView<Title> titleTable;
    @FXML
    private TableColumn<Title, String> titleCol;
    @FXML
    private TableColumn<Title, Integer> yearCol;
    @FXML
    private TableColumn<Title, String> kindCol;
    
    @FXML
    private Tab personsTab;
    @FXML
    private TableView<Person> personTable;
    @FXML
    private TableColumn<Person, String> nameCol;
    @FXML
    private TableColumn<Person, String> genderCol;
    @FXML
    private TableColumn<Person, String> birthCol;
    
    
    @FXML
    private Tab charNameTab;
    @FXML
    private TableView<CharName> charNameTable;
    @FXML
    private TableColumn<CharName, String> charNameCol;
    @FXML
    private TableColumn<CharName, String> actorCol;
    @FXML
    private TableColumn<CharName, String> charNameTitleCol;
    @FXML
    private TableColumn<CharName, String> charNameYearCol;
    
    @FXML
    private Tab movieCompaniesTab;
    @FXML
    private TableView<MovieCompany> companyTable;
    @FXML
    private TableColumn<MovieCompany, String> companyNameCol;
    @FXML
    private TableColumn<MovieCompany, String> companyTypeCol;

    @FXML
    private Button detailButton;

    
    private Stage stage;
    
    private ObservableList<Person> personList;
    private ObservableList<Title> titleList;
    private ObservableList<MovieCompany> companyList;
    private ObservableList<CharName> charNameList;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("production_year"));
        kindCol.setCellValueFactory(new PropertyValueFactory<>("kind"));

        companyNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        companyTypeCol.setCellValueFactory(new PropertyValueFactory<>("kind"));

        charNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        actorCol.setCellValueFactory(new PropertyValueFactory<>("actorName"));
        charNameTitleCol.setCellValueFactory(new PropertyValueFactory<>("movieName"));
        charNameYearCol.setCellValueFactory(new PropertyValueFactory<>("movieYear"));

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        birthCol.setCellValueFactory(new PropertyValueFactory<>("birthDate"));

        searchResultsTabPane.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) -> detailButton.setDisable(newValue.longValue() >= 2));

        AnchorPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            double width = newValue.longValue() / 5 - 30;
            titleTable.getColumns().forEach(c -> {
                if(c.getText().equals("Titel"))
                    c.setPrefWidth(3*width);
                else
                    c.setPrefWidth(width);
            });
            personTable.getColumns().forEach(c -> {
                if(c.getText().equals("Name"))
                    c.setPrefWidth(3*width);
                else
                    c.setPrefWidth(width);
            });
            charNameTable.getColumns().forEach(c -> {
                if(c.getText().equals("Jahr"))
                    c.setPrefWidth(width);
                else
                    c.setPrefWidth(1.5*width);
            });
            companyTable.getColumns().forEach(c -> {
                if(c.getText().equals("Name"))
                    c.setPrefWidth(2*width);
                else
                    c.setPrefWidth(width);
            });
        });
    }
    
    public void parseList(){
        
        
        if (titleTab.isSelected()){
            System.out.println(titleTable.getSelectionModel().getSelectedItem());
        }else if(personsTab.isSelected()){
            System.out.println(personTable.getSelectionModel().getSelectedItem().getName());
            System.out.println(personTable.getSelectionModel().getSelectedItem().getBirthDate());
            System.out.println(personTable.getSelectionModel().getSelectedItem().getGender());
        }else if(charNameTab.isSelected()){
            System.out.println(charNameTable.getSelectionModel().getSelectedItem().getName());
            System.out.println(charNameTable.getSelectionModel().getSelectedItem().getActorName());
            System.out.println(charNameTable.getSelectionModel().getSelectedItem().getMovieName());
            System.out.println(charNameTable.getSelectionModel().getSelectedItem().getMovieYear());
        }else{
            System.out.println(companyTable.getSelectionModel().getSelectedItem().getName());
            System.out.println(companyTable.getSelectionModel().getSelectedItem().getKind());
        }

    }
    
    public void detailview(){
        if (titleTab.isSelected()){
            try{
                if(!titleTable.getSelectionModel().isEmpty()){
                    showDetailTitle(titleTable.getSelectionModel().getSelectedItem());
                }else{
                    System.out.println("\n Es ist keine Zeile ausgewählt. \n");
                }
            } catch(IOException e){
                System.out.println(e);
            }
        }else if(personsTab.isSelected()){
            try{
                if(!personTable.getSelectionModel().isEmpty()){
                    showDetailPerson(personTable.getSelectionModel().getSelectedItem());
                }else{
                    System.out.println("\n Es ist keine Zeile ausgewählt. \n");
                }
            } catch(IOException e){
                System.out.println(e);
            }
        }else{
            System.out.println("\n Für diese Einträge gibt es keine Detailansicht. \n");
        }
    }
    
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void setResults(ObservableList<Title> titleList, ObservableList<Person> personList, ObservableList<MovieCompany> companyList, ObservableList<CharName> charNameList){
        this.titleList = titleList;
        this.companyList = companyList;
        this.charNameList = charNameList;
        this.personList = personList;
        if (isNull(titleList) || titleList.isEmpty()) {
            titleTab.setDisable(true);
        } else {
            titleTab.setDisable(false);
            this.titleTable.setItems(this.titleList);
        }
        
        if (isNull(companyList) || companyList.isEmpty()) {
            movieCompaniesTab.setDisable(true);
        } else {
            movieCompaniesTab.setDisable(false);
            this.companyTable.setItems(companyList);
        }
        
        if (isNull(charNameList) || charNameList.isEmpty()) {
            charNameTab.setDisable(true);
        } else {
            charNameTab.setDisable(false);
            this.charNameTable.setItems(charNameList);
        }
        
        if (isNull(personList) || personList.isEmpty()) {
            personsTab.setDisable(true);
        } else {
            personsTab.setDisable(false);
            this.personTable.setItems(personList);
        }
        
    }
    
    private void showDetailPerson (Person person)throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SearchResultsController.class.getResource("detailPerson/detailPerson.fxml"));

        Parent detailPersonController = loader.load();
        
        DetailPersonController controller = loader.getController();

        Scene scene = new Scene(detailPersonController);
        Stage detailPersonStage = new Stage();
        //modal.initModality(Modality.APPLICATION_MODAL);
        detailPersonStage.setTitle("Personendetailansischt");
        detailPersonStage.setMinWidth(250);
        detailPersonStage.setScene(scene);
        controller.setStage(detailPersonStage);
        
        detailPersonStage.show();

        controller.setPerson(person);
               
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
