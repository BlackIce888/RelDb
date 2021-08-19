/*
 * @author Andreas Diesendorf (2343841) <andreas_d@hotmail.de>, Björn Burzlaff (2874851) <bjoern.b96@web.de>
 */
package reldb.pres.searchResults.detailTitle;

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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import reldb.bdo.MovieCompany;
import reldb.bdo.Person;
import reldb.bdo.Title;
import reldb.bdo.TitlePlot;
import reldb.bdo.TitleRuntime;
import reldb.data.MovieCompanyDAOImpl;
import reldb.data.PersonDAOImpl;
import reldb.data.TitleDAOImpl;
import reldb.pres.ImdbSearch;
import reldb.pres.rent.CreateCustomerController;
import reldb.pres.rent.RentMovieController;
import reldb.pres.searchResults.SearchResultsController;
import reldb.pres.searchResults.detailPerson.DetailPersonController;

public class DetailTitleController implements Initializable {
    
    
    @FXML
    private TableView<Title> titleTable;
    @FXML
    private TableColumn<Title, String> titleCol;
    @FXML
    private TableColumn<Title, Integer> yearCol;
    @FXML
    private TableColumn<Title, String> kindCol;
    
    @FXML
    private TableView<Person> castTable;
    @FXML
    private TableColumn<Person, String> rolesCol;
    @FXML
    private TableColumn<Person, String> personNameCol;
    @FXML
    private TableColumn<Person, String> genderCol;
    
    @FXML
    private TableView<MovieCompany> companiesTable;
    @FXML
    private TableColumn<MovieCompany, String> companyNameCol;
    @FXML
    private TableColumn<MovieCompany, String> companyTypeCol;
    @FXML
    private TableColumn<MovieCompany, String> companyNoteCol;
    
    @FXML
    private Label nameLabel;
    @FXML
    private Label ratedLabel;
    @FXML
    private Label kindLabel;
    @FXML
    private Label genreLabel;
    @FXML
    private Label taglineLabel;
    @FXML
    private Label runtimesLabel;
    @FXML
    private Label releasLabel;
    @FXML
    private Label votesLabel;
    @FXML
    private Label budgetLabel;
    @FXML
    private TextArea plotLabel;
    @FXML
    private TextArea booksLabel;
    
    
    
    private Stage stage;
    
    private Title title;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("production_year"));
        kindCol.setCellValueFactory(new PropertyValueFactory<>("kind"));
        
        rolesCol.setCellValueFactory(new PropertyValueFactory<>("rolesString"));
        personNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        
        companyNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        companyTypeCol.setCellValueFactory(new PropertyValueFactory<>("kind"));
        companyNoteCol.setCellValueFactory(new PropertyValueFactory<>("note"));
    }
    
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    
    public void setTitle(Title titleTmp){
        PersonDAOImpl personDAO = new PersonDAOImpl();
        TitleDAOImpl titleDAO = new TitleDAOImpl();
        MovieCompanyDAOImpl companyDAO = new MovieCompanyDAOImpl();
        
        ObservableList<Title> titleList = FXCollections.observableArrayList();
        ObservableList<Person> castList = FXCollections.observableArrayList();
        ObservableList<MovieCompany> companiesList = FXCollections.observableArrayList();
        
        Title title = titleDAO.getFullTitleById(titleTmp.getId());
        
        
        nameLabel.setText(title.getTitle());
        kindLabel.setText(title.getKind());
        votesLabel.setText(title.getVotes());
        
        for(String genres : title.getGenres()){
            genreLabel.setText(genreLabel.getText() + " " + genres);
        }
        for(String tagline : title.getTaglines()){
            taglineLabel.setText(taglineLabel.getText() + " " + tagline);
        }
        for(TitleRuntime runtimes : title.getRuntimes()){
            runtimesLabel.setText(runtimesLabel.getText() + " " + runtimes.getRuntime());
        }
        for(String release : title.getReleaseDatesGermany()){
            releasLabel.setText(releasLabel.getText() + " " + release);
        }
        for(String budget : title.getBudgets()){
            budgetLabel.setText(budgetLabel.getText() + " " + budget);
        }
        for(TitlePlot plot : title.getPlot()){
            plotLabel.setText(plotLabel.getText() + " " + plot.getPlot());
        }
        for(String book : title.getBooks()){
            booksLabel.setText(booksLabel.getText() + " " + book);
        }
        for(String rated : title.getFskGermany()){
            ratedLabel.setText(ratedLabel.getText() + " " + rated);
        }

        castList = personDAO.getPersonsByTitleId(title.getId());
        castTable.setItems(castList);
        
        titleList = titleDAO.getLinkesTitlesByTitleId(title.getId());
        titleTable.setItems(titleList);
        
        companiesList = companyDAO.getMovieCompaniesByTitleId(title.getId());
        companiesTable.setItems(companiesList);
        this.title = title;
        
    }
    
    public void showPersonDetail(){
            try{
                    if(!castTable.getSelectionModel().isEmpty()){
                        showDetailPerson(castTable.getSelectionModel().getSelectedItem());
                    }else{
                        System.out.println("\n Es ist keine Zeile ausgewählt. \n");
                    }
                } catch(IOException e){
                    System.out.println(e);
                }
    }
    
    public void showTitleDetail() {
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
    
    public void rentTitle(){
        try{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ImdbSearch.class.getResource("rent/rentMovie.fxml"));

        Parent rentMovieController = loader.load();
        
        RentMovieController controller = loader.getController();

        Scene scene = new Scene(rentMovieController);
        Stage RentMovieStage = new Stage();
        //modal.initModality(Modality.APPLICATION_MODAL);
        RentMovieStage.setTitle("Ausleihen");
        RentMovieStage.setMinWidth(250);
        RentMovieStage.setScene(scene);
        controller.setStage(RentMovieStage);
        
        RentMovieStage.show();
        
        controller.setTitle(this.title);
        
        }catch(IOException e){
            System.out.println(e);
        }
    }
}
