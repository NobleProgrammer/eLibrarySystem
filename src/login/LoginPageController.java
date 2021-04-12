/*
    Authorized Access Only!
    Admin Panel Only.
    Name: Ammar Talat Joharji
    ID: 1742559
 */
package login;

import admin.panel.mainController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import data.model.LibraryEngine;
import data.model.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class LoginPageController implements Initializable {

    @FXML
    JFXTextField usernameSignup;
    @FXML
    JFXPasswordField passwordSignup;
    @FXML
    JFXRadioButton adminRadioButton;
    @FXML
    JFXRadioButton clientRadioButton;
    @FXML
    ToggleGroup radioGroup;
    @FXML
    JFXButton signupButton;
    //-------------------------------------
    @FXML
    JFXTextField usernameLogin;
    @FXML
    JFXPasswordField passwordLogin;
    @FXML
    JFXButton loginButton;
    @FXML
    AnchorPane pane;
    @FXML
    StackPane stackPane;
    //-------------------------------------
    LibraryEngine engine;

    @FXML
    void signUpHandler(ActionEvent event) throws IOException, ClassNotFoundException {
        if (usernameSignup.getText().equals("") || passwordSignup.getText().equals("") || (!adminRadioButton.isSelected()) && !clientRadioButton.isSelected()) {
            makeOkAlert("Sign up failed.\nPlease fill all the fields");
        } else if (engine.searchUser(usernameSignup.getText()) != -1) {
            makeOkAlert("Sign up failed.\nThis username isn't available. please try another.");
        } else {
            boolean isAdmin = false;
            if (adminRadioButton.isSelected()) {
                isAdmin = true;
            }
            engine.signup(usernameSignup.getText(), passwordSignup.getText(), isAdmin);
            makeOkAlert("You have signed up successfully as " + ((isAdmin) ? "Admin" : "Client") + ".");
        }
    }

    @FXML
    void loginHandler(ActionEvent event) throws IOException, ClassNotFoundException {
        User temp = engine.login(usernameLogin.getText(), passwordLogin.getText());
        if (usernameLogin.getText().equals("") || passwordLogin.getText().equals("")) {
            makeOkAlert("Logging in failed.\nPlease fill all the fields.");
        } else if (temp == null) {
            makeOkAlert("Logging in failed.\nThis username is not signed in. Please sign in first.");
        } else {
            //User temp = engine.getUsers().get(engine.searchUser(usernameLogin.getText()));
            
            if (temp.getUsername().equals(usernameLogin.getText()) && temp.getPassword().equals(passwordLogin.getText())) {
                //User tempUser = engine.getUsers().get(engine.searchUser(usernameLogin.getText()));
                if (temp.isIsAdmin()) {
                    //If the current guy who wants to log in is an admin then,
                    loadAdminMain(temp);
                } else {
                    //TODO: Write code for the Client users.
                    loadClientMain(temp);
                }
            }
            else {
                makeOkAlert("Logging in failed.\nIncorrect username or password, please try again.");
            }
        }
    }

    public void loadAdminMain(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/panel/main.fxml"));
            Parent root = loader.load();
            mainController adminMainController = loader.getController();
            loader.setController(adminMainController);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("E-Library System (Admin: " + user.getUsername() + ")");
            adminMainController.setEngine(engine , user);
            stage.show();
            Stage primaryStage = (Stage) this.pane.getScene().getWindow();
            primaryStage.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void loadClientMain(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/panel/main.fxml"));
            Parent root = loader.load();
            client.panel.mainController clientMainController = loader.getController();
            loader.setController(clientMainController);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("E-Library System (Admin: " + user.getUsername() + ")");
            clientMainController.setEngine(engine , user);
            stage.show();
            Stage primaryStage = (Stage) this.pane.getScene().getWindow();
            primaryStage.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void makeOkAlert(String text) {
        BoxBlur blur = new BoxBlur(3, 3, 3);
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXButton button = new JFXButton("Ok");
        JFXDialog dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.TOP);
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
            dialog.close();
        });
        dialogLayout.setHeading(new Label(text));
        dialogLayout.setActions(button);
        dialog.show();
        pane.setEffect(blur);
        dialog.setOnDialogClosed(new EventHandler<JFXDialogEvent>() {
            @Override
            public void handle(JFXDialogEvent event) {
                pane.setEffect(null);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.radioGroup = new ToggleGroup();
        this.adminRadioButton.setToggleGroup(radioGroup);
        this.clientRadioButton.setToggleGroup(radioGroup);
        try {
            engine = new LibraryEngine();
        } catch (IOException ex) {
            Logger.getLogger(LoginPageController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LoginPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
