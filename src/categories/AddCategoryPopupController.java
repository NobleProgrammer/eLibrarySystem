/*
    Authorized Access Only!
    Admin Panel Only.
    Name: Ammar Talat Joharji
    ID: 1742559
 */
package categories;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import data.model.LibraryEngine;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Amar
 */
public class AddCategoryPopupController implements Initializable {
    LibraryEngine engine;

    @FXML JFXTextField catName;
    @FXML JFXButton cancel;
    @FXML JFXButton add;
    @FXML StackPane stackPane;
    @FXML AnchorPane pane;
    
    
    @FXML
    void buttonHandler(ActionEvent event) throws IOException {
        if (event.getSource() == add) {
            if (this.engine.addCategory(catName.getText())) {
                Stage primaryStage = (Stage) this.catName.getScene().getWindow();
                primaryStage.close();
            }
            else {
                makeOkAlert("This category already exists, please try another.");
            }
        }
        else {
            Stage primaryStage = (Stage) this.catName.getScene().getWindow();
            primaryStage.close();
        }
        
    }
    
    public void setEngine(LibraryEngine engine){
        this.engine = engine;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
}
