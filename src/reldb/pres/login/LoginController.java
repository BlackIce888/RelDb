package reldb.pres.login;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import reldb.app.LoginApp;
import reldb.bdo.OracleUser;
import reldb.pres.ImdbSearch;

/**
 * Login Controller.
 */
public class LoginController extends AnchorPane implements Initializable {

    private ImdbSearch application;
    @FXML
    private Label statusMessage;
    @FXML
    private TextField oracleUsername;
    @FXML
    private PasswordField oraclePassword;

    private OracleUser oracleDbUser;
    private LoginApp loginService;

    /**
     * Constructor
     */
    public LoginController() {
        this.loginService = new LoginApp();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        statusMessage.setText("");
        oracleUsername.setPromptText("oracle user");
        oraclePassword.setPromptText("oracle password");
        oracleDbUser = OracleUser.getInstance();
    }

    /**
     * Setter for the application
     *
     * @param application
     */
    public void setApp(ImdbSearch application) {
        this.application = application;
    }
    
    /**
     * action for the Oracle-Login-Button
     *
     * @param event
     */
    @FXML
    private void processOracleLogin(ActionEvent event) {
        oracleUsername.setText("s6andies");
        oraclePassword.setText("rsfsr90k");
        oracleDbUser.setUsername(oracleUsername.getText());
        oracleDbUser.setPassword(oraclePassword.getText());

        // Ttry to login...
        if (loginService.oracleUserLogin()) {
            statusMessage.setText("Oracle Login successful!");
            application.gotoMainMenu();
        } else {
            statusMessage.setText("Oracle Login failed, please retry!");
        }
        
        
    }

    /**
     * Logs user out of both dbms and closes application
     *
     * @param event
     */
    @FXML
    private void processQuit(ActionEvent event) {
        application.quit();
    }
}
