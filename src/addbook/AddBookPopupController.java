/*
    Authorized Access Only!
    Admin Panel Only.
    Name: Ammar Talat Joharji
    ID: 1742559
 */
package addbook;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import data.model.Book;
import data.model.LibraryEngine;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class AddBookPopupController implements Initializable {
    
    @FXML JFXTextField bookName;
    @FXML JFXTextField bookAuthor;
    @FXML JFXTextField publishYear;
    @FXML JFXComboBox<String> bookCategory;
    @FXML JFXTextField bookBuyPrice;
    @FXML JFXTextField bookRentPrice;
    @FXML JFXRadioButton free;
    @FXML JFXRadioButton price;
    @FXML JFXButton add;
    @FXML ToggleGroup radioGroup;
    @FXML StackPane stackPane;
    @FXML AnchorPane pane;
    //-----------------------------
    LibraryEngine engine;
    
    @FXML
    void radioListener(MouseEvent event){
        if (free.isSelected()) {
            bookRentPrice.setText("");
            bookRentPrice.setEditable(false);
        }
        else {
            bookRentPrice.setEditable(true);
        }
    }
    
    @FXML
    void addListener(ActionEvent event) throws IOException, ClassNotFoundException{
        if (bookName.getText().equals("") || bookAuthor.getText().equals("") || publishYear.getText().equals("") ||
            bookBuyPrice.getText().equals("") || bookCategory.getSelectionModel().isEmpty() || (!free.isSelected()) && !price.isSelected()) {
            makeOkAlert("Please fill all the needed fields.");
        }
        else if (price.isSelected() && bookRentPrice.getText().equals("")){
            makeOkAlert("Please fill all the needed fields.");
        }
        else if (free.isSelected()){
            Book book = new Book(bookName.getText(), bookAuthor.getText(), bookCategory.getSelectionModel().getSelectedItem(), Integer.parseInt(publishYear.getText()), new Date(), Double.parseDouble(bookBuyPrice.getText()) , 0);
            if (this.engine.addStoreBook(book)) {
                Stage primaryStage = (Stage) this.bookAuthor.getScene().getWindow();
                primaryStage.close();
            }
            else {
                makeOkAlert("This book already exists, please try another.");
            }
        }
        else {
            Book book = new Book(bookName.getText(), bookAuthor.getText(), bookCategory.getSelectionModel().getSelectedItem(), Integer.parseInt(publishYear.getText()), new Date(), Double.parseDouble(bookBuyPrice.getText()) , Double.parseDouble(bookRentPrice.getText()));
            if (this.engine.addStoreBook(book)) {
                Stage primaryStage = (Stage) this.bookAuthor.getScene().getWindow();
                primaryStage.close();
            }
            else {
                makeOkAlert("This book already exists, please try another.");
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }    
    
    public void setEngine(LibraryEngine engine){
        this.engine = engine;
        this.radioGroup = new ToggleGroup();
        this.free.setToggleGroup(radioGroup);
        this.price.setToggleGroup(radioGroup);
        bookRentPrice.setEditable(false);
        this.bookCategory.getItems().addAll(engine.getCategories());
        intOrDoubleOnly(publishYear , false);
        intOrDoubleOnly(bookRentPrice , true);
        intOrDoubleOnly(bookBuyPrice, true);
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

    private void intOrDoubleOnly(JFXTextField tf, boolean isDouble) {
        if (isDouble) {
            tf.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    tf.setText(oldValue);
                }
            });
        } else {
            tf.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if (!newValue.matches("\\d*")) {
                    tf.setText(newValue.replaceAll("[^\\d]", ""));
                }
            });
        }
    }

}
