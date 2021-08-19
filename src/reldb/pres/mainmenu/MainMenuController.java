/*
 *  Copyright 2014 Rainer Stoermer
 */
package reldb.pres.mainmenu;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import reldb.pres.ImdbSearch;
import reldb.pres.rent.CreateCustomerController;
import reldb.pres.searchExtended.SearchExtendedController;
import reldb.pres.searchKeyword.SearchKeywordController;

/**
 * FXML Controller class
 *
 * @author Rainer Stoermer
 */
public class MainMenuController implements Initializable {

    @FXML
    private StackPane mainPane;

    private ImdbSearch application;
    
    private MainMenuController mainController;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainController = this;
    }

    /**
     * Setter for the application
     *
     * @param application
     */
    public void setApp(ImdbSearch application) {
        this.application = application;
    }

    
    



    //TODO: implement the following methods
    public void processSearchKeyword(ActionEvent actionEvent) {
        try {
            SearchKeywordController searchKeywordController = (SearchKeywordController) application.replaceSceneContent("searchKeyword/searchKeyword.fxml");
            searchKeywordController.setApp(application, mainController);
        } catch (IOException | RuntimeException ex) {
            Logger.getLogger(ImdbSearch.class.getName()).log(Level.SEVERE, "Error loading fxml-file and controller" + ex.getMessage(), ex);
        } catch (Exception ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void processExtendedSearch(ActionEvent actionEvent) {
        try {
            SearchExtendedController searchExtendedController = (SearchExtendedController) application.replaceSceneContent("searchExtended/searchExtended.fxml");
            searchExtendedController.setApp(application, mainController);
        } catch (IOException | RuntimeException ex) {
            Logger.getLogger(ImdbSearch.class.getName()).log(Level.SEVERE, "Error loading fxml-file and controller" + ex.getMessage(), ex);
        } catch (Exception ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void processQuit(ActionEvent actionEvent) {
        application.quit();
    }

    public void processViewRentLists(ActionEvent actionEvent) {
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
        
        
        }catch(IOException e){
            System.out.println(e);
        }
    }

}
