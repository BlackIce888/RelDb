/*
 *  Copyright 2014 Rainer Stoermer
 */
package reldb.pres;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import reldb.app.LoginApp;
import reldb.pres.login.LoginController;
import reldb.pres.mainmenu.MainMenuController;

/**
 *
 * @author Rainer Stoermer
 */
public class ImdbSearch extends Application {

    private Stage stage;
    private static Handler handler;
    private static final int LOG_SIZE = 1024000;
    private static final int LOG_ROTATION_COUNT = 1;
    private LoginApp loginService;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // set a handler for using a logfile for outputting log-messages
            handler = new FileHandler("imdbsearch.log", LOG_SIZE, LOG_ROTATION_COUNT);
            handler.setFormatter(new SimpleFormatter());
            Logger.getLogger("").addHandler(handler);
        } catch (IOException | SecurityException ex) {
            Logger.getLogger("").log(Level.SEVERE, null, ex);
        }

        Application.launch(ImdbSearch.class, (java.lang.String[]) null);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Open main stage
            stage = primaryStage;
            stage.setTitle("IMDB Search");
            stage.setResizable(false);
            primaryStage.show();
            gotoLogin();
        } catch (Exception ex) {
            Logger.getLogger(ImdbSearch.class.getName()).log(Level.SEVERE, "Error loading login-fxml-file and controller" + ex.getMessage());
        }
    }

    /**
     * gets main stage for further usage in parent controllers
     * @return mainstage
     */
    public Stage getMainStage() {
        return this.stage;
    }

    /**
     * closes the application
     */
    public void quit() {
        Logger.getLogger(ImdbSearch.class.getName()).log(Level.INFO, "Closing Application...");
        stage.close();
    }

    /**
     * Override to the standard application stop-method: adds database-logout to close connection prior to application
     * stop.
     *
     * @throws java.lang.Exception
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        this.loginService = new LoginApp();
        loginService.oracleUserLogout();
    }

    /**
     * replaces Content of primary stage to fxml-scene
     *
     * @param fxml fxml-ressource to load into primary stage
     *
     * @return the controller of fxml-ressource
     *
     * @throws Exception
     */
    public Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(ImdbSearch.class.getResource(fxml));
        Parent page;
        try (InputStream in = ImdbSearch.class.getResourceAsStream(fxml)) {
            page = loader.load(in);
        }
        Scene scene = new Scene(page);
        stage.setScene(scene);
        stage.sizeToScene();

        return (Initializable) loader.getController();
    }

    /**
     * loads the loginController
     */
    private void gotoLogin() {
        try {
            stage.setMinWidth(450);
            stage.setMinHeight(327);
            LoginController loginController = (LoginController) replaceSceneContent("login/login.fxml");
            loginController.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(ImdbSearch.class.getName()).log(Level.SEVERE, "Error loading login-fxml-file and controller" + ex.getMessage(), ex);
        }
    }

    /**
     * loads the mainmenuController
     */
    public void gotoMainMenu() {
        try {
            stage.setResizable(true);
            stage.setMinWidth(450);
            stage.setMinHeight(400 + 25);
            MainMenuController mainMenuController = (MainMenuController) replaceSceneContent("mainmenu/mainMenu.fxml");
            mainMenuController.setApp(this);
            stage.setHeight(800d);
            stage.setWidth(1200d);
        } catch (Exception ex) {
            Logger.getLogger(ImdbSearch.class.getName()).log(Level.SEVERE, "Error loading mainmenu-fxml-file and controller" + ex.getMessage(), ex);
        }
    }
}
