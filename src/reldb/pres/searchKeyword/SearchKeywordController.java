/*
 *  Copyright 2014 Rainer Stoermer
 */
package reldb.pres.searchKeyword;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import reldb.app.SearchApp;
import reldb.pres.ImdbSearch;
import reldb.pres.mainmenu.MainMenuController;
import reldb.pres.searchResults.SearchResultsController;

/**
 * FXML Controller class
 *
 * @author Rainer Stoermer
 */
public class SearchKeywordController implements Initializable {
    
    @FXML
    private TextField keywordTextField;
    @FXML
    private CheckBox titleCB;
    @FXML
    private CheckBox personCB;
    @FXML
    private CheckBox characterCB;
    @FXML
    private CheckBox companiesCB;
    @FXML
    private CheckBox selectAllCB;
    @FXML
    private RadioButton matchAllRadioButton;
    @FXML
    private RadioButton matchAnyRadioButton;
    @FXML
    private RadioButton matchExactRadioButton;

    private ImdbSearch application;
    private static SearchApp searches;
    
    private MainMenuController mainController;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    /**
     * Setter for the application
     *
     * @param application
     */
    public void setApp(ImdbSearch application, MainMenuController mainController) {
        this.application = application;
        this.mainController = mainController;
    }
    
    public void processSearch(ActionEvent event) {
        searches = new SearchApp();
        
        String keywords = keywordTextField.getText();
        
        int match = 0;
        if(matchAllRadioButton.isSelected()){
            match = 1;
        }else if(matchAnyRadioButton.isSelected()){
            match = 2;
        }else{
            match = 3;
        }
        
        if(titleCB.isSelected()){
            searches.searchTitle(keywords, match);
        }else{ 
        }
        if(personCB.isSelected()){
            searches.searchPerson(keywords, match);
        }else{
        }
        if(characterCB.isSelected()){
            searches.searchCharName(keywords, match);
        }else{
        }
        if(companiesCB.isSelected()){
            searches.searchMovieCompany(keywords, match);
        }else{
        }

        try{
            showResultsView();
        } catch(IOException e){
            System.out.println(e);
        }
        
    }
    
    public void selectAll(ActionEvent event) {
        if(selectAllCB.isSelected()){
            titleCB.setSelected(true);
            personCB.setSelected(true);
            characterCB.setSelected(true);
            companiesCB.setSelected(true);
        }else{
            titleCB.setSelected(false);
            personCB.setSelected(false);
            characterCB.setSelected(false);
            companiesCB.setSelected(false);
        }
    }

    public static void showResultsView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ImdbSearch.class.getResource("searchResults/searchResults.fxml"));
        Parent searchResultsController = loader.load();

        SearchResultsController controller = loader.getController();

        Scene scene = new Scene(searchResultsController);
        Stage searchResultStage = new Stage();
        //modal.initModality(Modality.APPLICATION_MODAL);
        searchResultStage.setTitle("Suchergebnisse");
        searchResultStage.setMinWidth(250);
        searchResultStage.setScene(scene);
        controller.setStage(searchResultStage);

        searchResultStage.show();
           
        controller.setResults(searches.titleList, searches.personList, searches.companyList ,searches.charNameList);
    }
    
    public void processSearchKeyword(ActionEvent actionEvent) {
        this.mainController.processSearchKeyword(actionEvent);
    }

    public void processExtendedSearch(ActionEvent actionEvent) {
        this.mainController.processExtendedSearch(actionEvent);
    }

    public void processQuit(ActionEvent actionEvent) {
        application.quit();
    }

    public void processViewRentLists(ActionEvent actionEvent) {
        this.mainController.processViewRentLists(actionEvent);
    }

}
