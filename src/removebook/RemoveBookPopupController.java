/*
    Authorized Access Only!
    Admin Panel Only.
    Name: Ammar Talat Joharji
    ID: 1742559
 */
package removebook;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import data.model.Book;
import data.model.LibraryEngine;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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

public class RemoveBookPopupController implements Initializable {

    @FXML
    Label bookName;
    @FXML
    Label bookAuthor;
    @FXML
    Label publishYear;
    @FXML
    Label bookCategory;
    @FXML
    Label bookBuyPrice;
    @FXML
    Label bookRentPrice;
    @FXML
    JFXRadioButton byName;
    @FXML
    JFXRadioButton byID;
    @FXML
    ToggleGroup radioGroup;
    @FXML
    JFXTextField nameText;
    @FXML
    JFXTextField idText;
    @FXML
    StackPane stackPane;
    @FXML
    AnchorPane pane;
    @FXML
    JFXButton remove;
    @FXML
    JFXButton search;
    @FXML
    Label notFound;
    //-----------------------------
    LibraryEngine engine;
    Book book;

    @FXML
    public void searchHandler(ActionEvent event) {
        resetLabels();
        if ((!(byName.isSelected()) && !(byID.isSelected()))) {
            makeOkAlert("Please select search method.");
        } else if (byName.isSelected() && nameText.getText().equals("")) {
            makeOkAlert("Please fill all the fields.");
        } else if (byID.isSelected() && idText.getText().equals("")) {
            makeOkAlert("Please fill all the fields.");
        } else if (byName.isSelected()) {
            int index = engine.searchBook(nameText.getText());
            if (index != -1) {
                this.book = engine.getStoreBooks().get(index);
                notFound.setVisible(false);
                bookName.setText(engine.getStoreBooks().get(index).getName());
                bookAuthor.setText(engine.getStoreBooks().get(index).getAuthor());
                bookBuyPrice.setText(engine.getStoreBooks().get(index).getBuyPrice() + "");
                bookCategory.setText(engine.getStoreBooks().get(index).getCategory());
                bookRentPrice.setText(engine.getStoreBooks().get(index).getRentPrice() + "");
                publishYear.setText(engine.getStoreBooks().get(index).getPublishYear() + "");
            } else {
                notFound.setVisible(true);
            }
        } else {
            int a = engine.searchBook(2);
            System.out.println(engine.getStoreBooks().get(a).getName());
            int index = engine.searchBook(Integer.parseInt(idText.getText()));
            if (index != -1) {
                this.book = engine.getStoreBooks().get(index);
                notFound.setVisible(false);
                bookName.setText(engine.getStoreBooks().get(index).getName());
                bookAuthor.setText(engine.getStoreBooks().get(index).getAuthor());
                bookBuyPrice.setText(engine.getStoreBooks().get(index).getBuyPrice() + "");
                bookCategory.setText(engine.getStoreBooks().get(index).getCategory());
                bookRentPrice.setText(engine.getStoreBooks().get(index).getRentPrice() + "");
                publishYear.setText(engine.getStoreBooks().get(index).getPublishYear() + "");
            } else {
                notFound.setVisible(true);
            }
        }
    }

    @FXML
    public void removeHandler(ActionEvent event) throws IOException, ClassNotFoundException {
        if (bookName.getText().equals("")) {
            makeOkAlert("You must search the book to remove it.");
        } else {
            if (byID.isSelected()) {
                System.out.println(book.getBookID());
                engine.removeBook(book.getBookID());
            }
            else {
                engine.removeBook(book.getName());
            }
            Stage primaryStage = (Stage) this.bookAuthor.getScene().getWindow();
            primaryStage.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    
    @FXML
    void radioListener(MouseEvent event) {
        if (byName.isSelected()) {
            idText.setText("");
            idText.setEditable(false);
            nameText.setEditable(true);
        } else {
            nameText.setText("");
            nameText.setEditable(false);
            idText.setEditable(true);
        }
    }

    public void setEngine(LibraryEngine engine) {
        this.engine = engine;
        this.radioGroup = new ToggleGroup();
        this.byName.setToggleGroup(radioGroup);
        this.byID.setToggleGroup(radioGroup);
        intOnly(idText);
        notFound.setVisible(false);
        nameText.setEditable(false);
        idText.setEditable(false);
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

    private void intOnly(JFXTextField tf) {
        tf.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                tf.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }
    
    void resetLabels(){
        this.bookName.setText("");
        this.bookAuthor.setText("");
        this.bookCategory.setText("");
        this.bookBuyPrice.setText("");
        this.bookRentPrice.setText("");
        this.publishYear.setText("");
    }
}
